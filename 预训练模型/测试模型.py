import jieba
import joblib
import os

def chinese_tokenizer(text):
    """对中文文本进行分词"""
    return " ".join(jieba.cut(text))

def predict_new_text(text):
    model_path = 'calibrated_svm_model.pkl'
    vec_path = 'tfidf_vectorizer.pkl'

    if not os.path.exists(model_path) or not os.path.exists(vec_path):
        raise FileNotFoundError("模型或向量化器未找到，请先运行训练部分")

    calibrated_clf = joblib.load(model_path)
    vectorizer = joblib.load(vec_path)

    # 分词并转换为 TF-IDF 特征向量
    tokenized_text = chinese_tokenizer(text)
    text_vec = vectorizer.transform([tokenized_text])

    # 预测类别和概率
    prediction = calibrated_clf.predict(text_vec)
    probabilities = calibrated_clf.predict_proba(text_vec)

    # 返回预测结果和各类别概率
    return prediction[0], dict(zip(calibrated_clf.classes_, probabilities[0]))

# ======================================
# 🧪 示例测试：对新文本进行预测
# ======================================
if __name__ == '__main__':
    sample_text = "这是一篇关于人工智能的科技新闻。"
    predicted_label, confidence_scores = predict_new_text(sample_text)

    print(f"输入文本: {sample_text}")
    print(f"预测类别: {predicted_label}")
    print("各类别匹配度（概率）:")
    for label, score in confidence_scores.items():
        print(f"  - {label}: {score:.4f}")