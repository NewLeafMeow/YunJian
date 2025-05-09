import requests

# 查询帖子信息列表
def list_posts(query):
    url = '/posts/posts/list'
    try:
        response = requests.get(url, params=query)
        response.raise_for_status()  # 若请求失败（状态码非 200），抛出异常
        return response.json()
    except requests.RequestException as e:
        print(f"请求出错: {e}")
        return None

# 修改帖子信息
def update_posts(data):
    url = '/posts/posts'
    try:
        response = requests.put(url, json=data)
        response.raise_for_status()  # 若请求失败（状态码非 200），抛出异常
        return response.json()
    except requests.RequestException as e:
        print(f"请求出错: {e}")
        return None
    