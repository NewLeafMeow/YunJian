import pandas as pd
import jieba
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.svm import LinearSVC
from sklearn.calibration import CalibratedClassifierCV
from sklearn.metrics import classification_report
from sklearn.metrics import accuracy_score
import joblib
from tqdm import tqdm
import os

# 设置进度条显示
tqdm.pandas()

# -------------------------------------
# 1. 加载数据
# -------------------------------------
print("【1/8】加载数据...")
df = pd.read_csv("数据1.csv")  # 替换为你的文件路径
df.columns = ["label", "content"]  # 确保列名正确
df = df.dropna()  # 删除空值

# -------------------------------------
# 2. 中文分词
# -------------------------------------
print("【2/8】中文分词处理...")

def chinese_tokenizer(text):
    return " ".join(jieba.cut(text))

df['content'] = df['content'].progress_apply(chinese_tokenizer)

# -------------------------------------
# 3. 加载停用词表
# -------------------------------------
print("【3/8】加载停用词表...")

def load_stop_words(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        # 直接读取为列表，而不是集合
        stop_words = f.read().splitlines()
    return stop_words

stop_words = load_stop_words("chinese.txt")  # 替换为你自己的停用词文件路径

# -------------------------------------
# 4. 特征提取：TF-IDF 向量化
# -------------------------------------
print("【4/8】特征提取（TF-IDF）...")

vectorizer = TfidfVectorizer(
    max_features=10000,
    ngram_range=(1, 3),
    min_df=3,
    max_df=0.95,
    stop_words=stop_words,
    use_idf=True,
    smooth_idf=True,
    sublinear_tf=True,
    norm='l2',
    analyzer='word'
)

X = vectorizer.fit_transform(df['content'])
y = df['label']

print(f"特征矩阵X的形状: {X.shape}")
print(f"标签y的形状: {y.shape}")

# -------------------------------------
# 5. 划分训练集和测试集
# -------------------------------------
print("【5/8】划分训练集和测试集...")
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# -------------------------------------
# 6. 训练 LinearSVC 模型
# -------------------------------------
print("【6/8】开始训练 LinearSVC 模型...")
svm_model = LinearSVC(C=1.0, max_iter=1000, class_weight='balanced', random_state=42)
svm_model.fit(X_train, y_train)

# -------------------------------------
# 7. 使用 CalibratedClassifierCV 校准模型以支持 predict_proba
# -------------------------------------
print("【7/8】校准模型以支持概率输出...")
calibrated_clf = CalibratedClassifierCV(svm_model, cv='prefit', method='sigmoid')
calibrated_clf.fit(X_train, y_train)  # 这里使用已经训练好的 svm_model，cv='prefit' 表示不再重新交叉验证

# -------------------------------------
# 8. 在测试集上评估
# -------------------------------------
print("【8/8】模型评估中...")
y_pred = calibrated_clf.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)
print(f"准确率 Accuracy: {accuracy:.4f}")
print("\n分类报告：")
print(classification_report(y_test, y_pred))

# -------------------------------------
# 9. 保存模型和向量化器
# -------------------------------------
print("【9/10】保存模型和向量化器...")
joblib.dump(calibrated_clf, 'calibrated_svm_model.pkl')
joblib.dump(vectorizer, 'tfidf_vectorizer.pkl')
print("模型已保存到：calibrated_svm_model.pkl 和 tfidf_vectorizer.pkl")

# -------------------------------------
# 10. 使用模型进行新文本预测（带匹配度）
# -------------------------------------
print("【10/10】加载模型并进行新文本预测...")

def predict_new_text(text):
    model_path = 'calibrated_svm_model.pkl'
    vec_path = 'tfidf_vectorizer.pkl'

    if not os.path.exists(model_path) or not os.path.exists(vec_path):
        raise FileNotFoundError("模型或向量化器未找到，请先运行训练部分")

    calibrated_clf = joblib.load(model_path)
    vectorizer = joblib.load(vec_path)

    text_vec = vectorizer.transform([text])
    prediction = calibrated_clf.predict(text_vec)
    probabilities = calibrated_clf.predict_proba(text_vec)

    return prediction[0], dict(zip(calibrated_clf.classes_, probabilities[0]))

# 示例预测
sample_text = "这是一篇关于人工智能的科技新闻。"
predicted_label, confidence_scores = predict_new_text(sample_text)
print(f"\n输入文本: {sample_text}")
print(f"预测类别: {predicted_label}")
print("各类别匹配度（概率）:")
for label, score in confidence_scores.items():
    print(f"  - {label}: {score:.4f}")