<template>
  <view class="app">
    <!-- 搜索框和搜索按钮的容器 -->
    <view class="search-container">
      <input
        ref="searchInput"
        class="search-input"
        v-model="searchQuery"
        @keyup.enter="handleSearch"
        placeholder="请输入搜索内容..."
      />
      <button class="search-button" @click="handleSearch">搜索</button>
    </view>

    <!-- 搜索历史容器，搜索后隐藏 -->
    <view class="history-container" v-show="!isShowPosts">
      <view
        v-for="(item, index) in searchHistory"
        :key="index"
        class="history-item"
        @click="fillSearchInput(item.content)"
      >
        <span>{{ item.content }}</span>
        <span class="delete-icon" @click.stop="deleteHistory(index)">x</span>
      </view>
    </view>

    <!-- 帖子列表组件 -->
    <view class="main-content" v-show="isShowPosts">
      <PostList
        :posts="posts"
        :isLoading="isLoading"
        :userId="userId"
        @go-to-post="goToPost"
        @like-post="likePost"
      />
    </view>
  </view>
</template>

<script>
import config from '@/config'
import { listHistory, addHistory, delHistory } from "@/api/history/history";
import { getUserProfile, getUser } from "@/api/system/user";
import { listPosts } from "@/api/posts/posts";
import { listComments } from "@/api/comments/comments";
import { listProfile, updateProfile } from "@/api/profile/profile";
import PostList from '@/components/PostList.vue'; // 引入封装的组件

export default {
  components: { PostList }, // 注册组件
  data() {
    return {
      searchQuery: "",
      searchHistory: [],
      userId: null,
      posts: [],
      isShowPosts: false, // 控制帖子显示的标志位
      pageNum: 1, // 当前页码
      pageSize: 1, // 每页显示数量
      isLoading: false, // 加载状态
      hasMore: true // 是否还有更多数据
    };
  },
  async onLoad() {
    try {
      await this.getUser();
      await this.readlistHistory();
    } catch (error) {
      console.error("加载数据时出错:", error);
    }
  },
  onReachBottom() {
    if (!this.isLoading && this.hasMore && this.searchQuery) {
      this.loadMorePosts();
    }
  },
  methods: {
    async getUser() {
      const res = await getUserProfile();
      this.userId = res.data.userId;
    },

    async getUsers(posts) {
      const users = [];
      for (let i = 0; i < posts.length; i++) {
        const user = await getUser(posts[i].userId);
        users.push(user.data);
        const commentCount = await listComments({
          postId: posts[i].id
        })
        users[i].commentCount = commentCount.rows.length;
      }
      console.log("其他信息：",users);
      return users;
    },

    fillSearchInput(content) {
      this.searchQuery = content;
    },

    async handleSearch() {
      if (this.searchQuery.trim() === "") {
        uni.showToast({
          title: "请输入搜索内容",
          icon: "none",
        });
        return;
      }

      // 添加搜索历史逻辑
      if (!this.searchHistory.some(item => item.content === this.searchQuery)) {
        try {
          await addHistory({
            userId: this.userId,
            searchTerm: this.searchQuery,
          });
          await this.readlistHistory();
        } catch (error) {
          console.error("添加搜索历史时出错:", error);
        }
      }

      // 重置分页信息
      this.pageNum = 1;
      this.hasMore = true;
      this.posts = [];

      // 搜索帖子数据
      try {
        this.isLoading = true;
        const res = await this.fetchPosts();
        let newPosts = res.map(post => this.processPost(post));
        const users = await this.getUsers(newPosts);
        newPosts = newPosts.map((post, index) => ({
          ...post,
          userName: users[index].userName,
          nickName: users[index].nickName,
          avatar: config.baseUrl + users[index].avatar,
          commentCount: users[index].commentCount,
        }));
        this.posts = newPosts;
        this.isShowPosts = true; // 显示帖子列表
        console.log("搜索到的帖子:", this.posts);
      } catch (error) {
        console.error("搜索帖子失败:", error);
        this.isShowPosts = false;
      } finally {
        this.isLoading = false;
      }
    },

    // 处理帖子数据（复用之前的处理逻辑）
    processPost(post) {
      if (post.media) {
        post.media = post.media.replace(
          /(<img\s+[^>]*src=")\/dev-api/g,
          '$1' + config.baseUrl
        );
        post.media = post.media.replace(
          /<img\s+[^>]*src="blob:[^"]+"[^>]*>/g,
          ''
        );
      }
      // 可以添加更多数据处理逻辑，如用户信息、点赞状态等
      return post;
    },

    async readlistHistory() {
      try {
        const res = await listHistory({
          pageNum: 1,
          pageSize: 30,
          userId: this.userId,
          orderByColumn: "searchTime",
          isAsc: "desc",
        });
        this.searchHistory = res.rows.map(item => ({
          id: item.id,
          content: item.searchTerm,
        }));
        console.log("搜索历史:", this.searchHistory);
        const url = 'http://127.0.0.1:5000/predict';
        let historyprofile = []; // 注意初始化时的分号
        for (let i = 0; i < this.searchHistory.length; i++) {
          const re = await fetch(url, {
            method: 'POST',
            headers: {
              'Content-Type': 'text/plain;charset=UTF-8'
            },
            body: this.searchHistory[i].content,
          });
          historyprofile.push(await re.text());
          console.log("预测结果（单次）:", historyprofile[i]); // 建议打印单次结果，避免数组过大
        }

        // ------------------- 新增统计逻辑 -------------------
        if (historyprofile.length === 0) {
          console.log("无预测结果，无法统计频率");
          return;
        }

        // 1. 统计每个元素的出现次数
        const frequency = {};
        for (const item of historyprofile) {
          frequency[item] = (frequency[item] || 0) + 1;
        }
        const str = JSON.stringify(frequency);
        console.log("频率统计结果：", str);
        let id = await listProfile({
          userId: this.userId,
        });
        id = id.rows[0].id;
        await updateProfile({
          id: id,
          searchRecordType: str
        });
      } catch (error) {
        console.error("读取搜索历史时出错:", error);
      }
    },

    async deleteHistory(index) {
      const itemToDelete = this.searchHistory[index];
      try {
        await delHistory(itemToDelete.id);
        this.searchHistory.splice(index, 1);
      } catch (error) {
        console.error('删除搜索历史记录时出错:', error);
      }
    },

    async loadMorePosts() {
      this.isLoading = true;
      this.pageNum++;
      try {
        const res = await this.fetchPosts();
        let newPosts = res.map(post => this.processPost(post));
        // 检查新帖子是否有重复ID
        const existingIds = this.posts ? this.posts.map(post => post.id) : [];
        const hasDuplicate = newPosts.some(post => existingIds.includes(post.id));
        if (hasDuplicate) {
          this.hasMore = false;
          this.isLoading = false;
          return;
        }
        const users = await this.getUsers(newPosts);
        newPosts = newPosts.map((post, index) => ({
          ...post,
          userName: users[index].userName,
          nickName: users[index].nickName,
          avatar: config.baseUrl + users[index].avatar,
          commentCount: users[index].commentCount,
        }));
        this.posts = this.posts.concat(newPosts);
        console.log("刷新帖子：",this.posts)
      } catch (error) {
        console.error("加载更多帖子失败:", error);
        this.pageNum--;
      } finally {
        this.isLoading = false;
      }
    },

    async fetchPosts() {
      let list = await listPosts({
        media: this.searchQuery,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        orderByColumn: "updatedAt",
        isAsc: "desc"
      });
      return list.rows;
    },
  }
};
</script>

<style scoped>
/* 新增帖子列表样式 */
.post-list {
  width: 100%;
  padding: 20px;
}

.post-item {
  margin: 20px 0;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.post-user {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-avatar {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  margin-right: 10px;
}

.user-name {
  font-weight: 500;
}

.post-header {
  margin: 10px 0;
}

.post-title {
  font-size: 18px;
  color: #333;
}

.post-body {
  line-height: 1.6;
  color: #666;
}

/* 保持原有搜索相关样式不变 */
.app {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  justify-content: flex-start;
  padding-top: 20px;
}

.search-container {
  width: 70%;
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
  z-index: 10;
}

.search-input {
  padding: 5px;
  height: 100%;
  width: 70%;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-button {
  padding: 5px 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #45a049;
}

.history-container {
  width: 70%;
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.history-item {
  padding: 5px 10px;
  background-color: #fff;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  border: 1px solid #ccc;
}

.delete-icon {
  cursor: pointer;
  color: #999;
}

.delete-icon:hover {
  color: #333;
}

/* 新增：加载提示样式 */
.loading-tip {
  margin-top: 20px;
  color: #999;
  font-size: 14px;
  text-align: center;
}

.action-icon {
  width: 32rpx;
  height: 32rpx;
  margin-right: 8rpx;
}

.main-content {
  width: 100%;
  padding: 20rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ececec;
}
</style>    