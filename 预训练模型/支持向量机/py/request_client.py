import requests

class RequestClient:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.base_url = f'http://{self.ip}:{self.port}'

    def send_request(self, path):
        url = f'{self.base_url}{path}'
        try:
            response = requests.get(url)
            print(response.text)
        except requests.RequestException as e:
            print(f"请求出错: {e}")

# 创建请求客户端实例
import requests
from requests.auth import HTTPBasicAuth  # 引入 Basic Auth 支持

class RequestClient:
    def __init__(self, ip, port, username, password):
        self.ip = ip
        self.port = port
        self.base_url = f'http://{self.ip}:{self.port}'
        self.auth = HTTPBasicAuth(username, password)  # 初始化 Basic Auth

    def send_request(self, path):
        url = f'{self.base_url}{path}'
        try:
            response = requests.get(url, auth=self.auth)  # 自动添加 Basic Auth 头
            print(response.text)
        except requests.RequestException as e:
            print(f"请求出错: {e}")

# 使用示例：替换用户名和密码
client = RequestClient('localhost', 8081)

# 调用实例方法发送请求
client.send_request('/posts/posts/list')