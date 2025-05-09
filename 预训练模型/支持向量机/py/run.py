import jieba
import joblib
import logging
from flask import Flask, request
from flask_cors import CORS

# 配置日志记录
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 加载模型和向量化器路径
MODEL_PATH = r'C:\Users\24100\Desktop\预训练模型\calibrated_svm_model.pkl'
VECTORIZER_PATH = r'C:\Users\24100\Desktop\预训练模型\支持向量机\py\tfidf_vectorizer.pkl'

try:
    # 加载模型和向量化器
    svm_model = joblib.load(MODEL_PATH)
    vectorizer = joblib.load(VECTORIZER_PATH)
    logger.info("模型和向量化器加载成功")
except FileNotFoundError:
    logger.error("模型或向量化器文件未找到，请检查路径")
    raise
except Exception as e:
    logger.error(f"加载模型或向量化器时发生错误: {e}")
    raise

app = Flask(__name__)
# 添加跨域支持，允许所有域名跨域，生产环境建议指定具体域名
CORS(app)

def chinese_tokenizer(text):
    """使用 jieba 分词"""
    return " ".join(jieba.cut(text))

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # 接收原始文本
        text = request.data.decode('utf-8').strip()
        print("接收到的文本：", text)

        if not text:
            print("错误：输入文本为空")
            return "Input text is empty", 400

        # 分词处理
        tokenized_text = chinese_tokenizer(text)
        print("分词后的文本：", tokenized_text)

        # 向量化
        text_vec = vectorizer.transform([tokenized_text])
        print("向量化完成，维度：", text_vec.shape)

        # 预测
        prediction = svm_model.predict(text_vec)
        predicted_label = str(prediction[0])
        print("预测结果：", predicted_label)

        # 如果模型支持概率（训练时设置了 probability=True），可以输出匹配度
        if hasattr(svm_model, "predict_proba"):
            probabilities = svm_model.predict_proba(text_vec)
            max_prob = probabilities.max()
            print("最高匹配度：", max_prob)

            # 获取所有类别及其对应的概率
            class_labels = svm_model.classes_
            probability_dict = dict(zip(class_labels, probabilities.tolist()[0]))

            # 打印所有类别及其概率
            print("各类别及其匹配度：")
            for label, prob in probability_dict.items():
                print(f"  - {label}: {prob:.4f}")

        else:
            print("当前模型不支持 predict_proba 方法，无法输出匹配度")

        # 仅返回预测的标签
        return predicted_label

    except Exception as e:
        logger.error(f"预测过程中发生错误: {e}")
        print("预测失败：", e)
        return "Prediction failed", 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
    