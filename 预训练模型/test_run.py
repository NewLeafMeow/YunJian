import requests

# 服务器地址（本地运行 Flask 服务器时）
URL = "http://127.0.0.1:5000/predict"

# 测试文本
data = "原神"

# 发送 POST 请求
response = requests.post(URL, data)

# 打印返回结果
print("Response JSON:", response)
