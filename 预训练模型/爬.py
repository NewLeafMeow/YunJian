import requests
from bs4 import BeautifulSoup
import json
import time
from concurrent.futures import ThreadPoolExecutor

# ⚙️ 配置区域
POSTS_TO_FETCH = 100         # 目标帖子总数
THREADS_PER_PAGE = 20        # 每页请求的帖子数
OUTPUT_FILE = "tieba_posts.json"
MAX_WORKERS = 5              # 并发线程数（降低以减少封禁风险）

# 🧾 请求头配置（含 BDUSS/BDUTT 以绕过反爬）
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36",
    "Referer": "https://tieba.baidu.com/",
    "BDUSS": "your_BDUSS_cookie_here",  # ⚠️ 必须填写有效的 BDUSS！
    "BDUTT": "your_BDUTT_cookie_here"   # ⚠️ 必须填写有效的 BDUTT！
}

# 🔗 首页地址
INDEX_URL = "https://tieba.baidu.com/index.html"

def fetch_index_html():
    """
    获取贴吧首页 HTML 内容
    """
    print(f"[请求] 正在请求首页: {INDEX_URL}")
    try:
        resp = requests.get(INDEX_URL, headers=HEADERS, timeout=10)
        print(f"[响应] 状态码: {resp.status_code}, URL: {INDEX_URL}")
        if resp.status_code != 200:
            print(f"[错误] 首页请求失败，状态码: {resp.status_code}")
            print(f"[响应内容] {resp.text[:500]}...")
            return ""
        return resp.text
    except Exception as e:
        print(f"[错误] 首页请求失败: {e}")
        return ""

def parse_index_html(html):
    """
    从首页 HTML 中解析推荐帖子
    """
    soup = BeautifulSoup(html, "html.parser")
    threads = []

    # 🧹 新版页面结构：推荐帖子容器类名为 .j_feed_li
    recommend_blocks = soup.select(".j_feed_li")
    if not recommend_blocks:
        print("[提示] 未找到推荐帖子容器，页面结构可能已变更")
        return threads

    for block in recommend_blocks:
        title_elem = block.select_one(".n_right a")  # 假设标题链接位于 .n_right 下的第一个 <a> 标签
        thread_id = block.get("data-thread-id")

        if not title_elem or not thread_id:
            continue

        title = title_elem.get_text(strip=True)

        # 🏷️ 提取标签（如果存在）
        tags = []
        for tag in block.select(".post_tag"):
            tag_text = tag.get_text(strip=True)
            if tag_text:
                tags.append(tag_text)

        threads.append({
            "thread_id": thread_id,
            "title": title,
            "tags": tags
        })

    print(f"[解析] 共找到 {len(threads)} 个推荐主题")
    return threads

def fetch_content(thread_id):
    """
    获取指定帖子的首楼内容
    """
    try:
        url = f"https://tieba.baidu.com/p/{thread_id}?see_lz=1"
        print(f"[请求] 正在获取帖子内容: {url}")
        resp = requests.get(url, headers=HEADERS, timeout=10)
        print(f"[响应] 状态码: {resp.status_code}, URL: {url}")

        if resp.status_code != 200:
            print(f"[错误] 非 200 响应，状态码: {resp.status_code}, URL: {url}")
            return ""

        soup = BeautifulSoup(resp.text, "html.parser")
        content_elem = soup.select_one(".p_content")
        if not content_elem:
            content_elem = soup.select_one(".post_content")

        content = content_elem.get_text(strip=True) if content_elem else ""
        print(f"[内容] 帖子 {thread_id} 已成功获取正文")
        return content

    except Exception as e:
        print(f"[错误] 帖子 {thread_id} 获取失败: {e}, URL: {url}")
        return ""

def main():
    print("⚠️ 注意：必须填写有效的 BDUSS 和 BDUTT Cookie！")
    html = fetch_index_html()
    if not html:
        print("[错误] 无法获取首页 HTML，程序终止")
        return

    # 1️⃣ 解析首页推荐帖子
    threads = parse_index_html(html)
    if not threads:
        print("[错误] 未解析到任何推荐帖子")
        return

    # 2️⃣ 并发获取正文内容
    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        contents = list(executor.map(lambda th: fetch_content(th["thread_id"]), threads))

    # 3️⃣ 合并数据并保存
    for i, thread in enumerate(threads):
        thread["content"] = contents[i]

    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        json.dump(threads, f, ensure_ascii=False, indent=2)

    print(f"🎉 已保存到 {OUTPUT_FILE}")

if __name__ == "__main__":
    main()