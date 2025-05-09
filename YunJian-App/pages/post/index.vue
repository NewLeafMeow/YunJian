<template>
  <article class="post-page">
    <!-- 发帖人信息与标题 -->
    <header class="post-header">
      <div class="post-author">
        <img :src="authorAvatar" alt="Avatar" class="avatar" />
        <span class="author-name">{{ authorName }}</span>
      </div>
      <h1 class="post-title">{{ postTitle }}</h1>
    </header>

    <!-- 帖子正文 -->
    <section class="post-content">
      <p v-html="postContent"></p>
    </section>

    <!-- 评论区 -->
    <section class="comments-section">
      <h2 class="comments-title">评论区</h2>
      <ul class="comments-list">
        <li v-for="(comment, index) in parentCommentsA" :key="index" class="comment-item" @click="handleCommentClick(index)">
          <p class="comment-text">{{ comment.content }}</p>
        </li>
      </ul>
    </section>

    <!-- 固定在屏幕下方的评论编辑区域 -->
    <div class="comment-editor" v-show="showEditor">
      <textarea
        ref="textareaInput"
        v-model="newComment"
        placeholder="留下你的评论..."
        class="editor-textarea"
        :style="{ height: isFocused  ? '150px' : '50px' }"
        @focus="handleFocus"
        @blur="handleBlur"
      ></textarea>
      <button
        @click="sendComment"
        class="send-btn"
        :disabled="newComment.trim() === ''">
        发送
      </button>
    </div>
  </article>
</template>

<script>
import config from '@/config'
import { getPosts } from "@/api/posts/posts";
import { listUser, getUser } from "@/api/system/user";
import { listComments , addComments } from "@/api/comments/comments";

export default {
  data() {
    return {
      postId: this.$route.query.postId,
      authorAvatar: '',      // 发帖人头像
      authorName: '',        // 发帖人名称
      userId: '',            // 发帖人ID
      postTitle: '',         // 帖子标题
      postContent: '',       // 帖子内容 HTML 字符串
      parentCommentsA: [],   // 父评论内容列表
      newComment: '',        // 新评论内容
      isFocused: false,      // textarea 聚焦状态
      showEditor: false,     // 控制评论编辑框显示
      parentCommentId: null, //父评论id
      textareaReady: false,
      myname: this.$store.state.user.name,
      myid: null,
    };
  },
  async created() {
    if (!this.postId) {
      console.warn("缺少帖子id参数");
      return;
    }
    try {
      // 获取帖子内容
      const post = await getPosts(this.postId);
      console.log('获取帖子信息成功：', post);
      const { title, media, userId } = post.data;
      this.postTitle = title;
      this.userId = userId;
      this.postContent = media.replace(
        /src="\/dev-api/g,
        'src="' + config.baseUrl
      );

      // 获取用户信息
      const user = await getUser(userId);
      console.log('获取发帖人信息成功：', user);
      const { nickName, avatar } = user.data || {};
      this.authorName = nickName;
      this.authorAvatar = config.baseUrl + avatar;

      //获取评论数据
      const comments = await listComments({
        postId: this.postId,
        parentCommentId: null,
        orderByColumn: "createdAt",
        isAsc: "desc",
      });
      console.log('获取评论数据成功：', comments);
      this.parentCommentsA = comments.rows.map(item => ({
        id: item.id,
        content: item.content,
        parentCommentId: item.parentCommentId,
      }));
    } catch (err) {
      console.error('获取数据失败：', err);
    }
  },
  mounted() {
    const commentsSection = this.$el.querySelector('.comments-section');
    const observer = new IntersectionObserver(
      ([entry]) => {
        this.showEditor = entry.isIntersecting;
      },
      { threshold: 0.2 }
    );
    if (commentsSection) {
      observer.observe(commentsSection);
    }
  },
  computed: {
  },
  async onLoad() {
    try {
      await this.getUserId();
    } catch (error) {
      console.error('获取用户ID失败', error);
    }
  },
  methods: {
    async getUserId() {
      try {
        // 使用 listUser 接口，根据用户名查询用户数据
        const response = await listUser({
          pageNum: 1,
          pageSize: 1,
          userName: this.myname
        });
        if (response.rows && response.rows.length > 0) {
          const userData = response.rows[0];
          this.myid = userData.userId;
          console.log("获取到的用户ID：", this.myid);
        } else {
          console.error("未找到对应的用户ID");
        }
      } catch (error) {
        console.error("获取用户ID失败:", error);
      }
    },
    // 父评论聚焦
    handleCommentClick(index) {
      if (this.isFocused) {
        this.isFocused = false;
        this.parentCommentId = null;
        return;
      }
      this.isFocused = true;
      this.parentCommentId = this.parentCommentsA[index].id;
      this.$refs.textareaInput.$el.querySelector('textarea').focus();
      console.log("父评论聚焦", this.parentCommentId);
    },

    // 评论发送
    async sendComment() {
      console.log("发送评论", this.myname);
      try {
        const response = await addComments({
          content: this.newComment,
          parentCommentId: this.parentCommentId,
          postId: this.postId,
          userId: this.myid,
        })
        // 如果需要更新评论列表，可以在这里刷新页面数据或者直接将新评论添加到 parentCommentsA 中
        this.newComment = ''; // 清空输入框
        console.log('评论发送成功', response);
      } catch (error) {
        console.error('评论发送失败', error);
      }
    },
    // 评论聚焦
    handleFocus() {
      if (this.isFocused) {
        return;
      }
      this.isFocused = true;
      this.parentCommentId = null;
      console.log("评论聚焦");
    },

    // 取消聚焦
    handleBlur() {
      setTimeout(() => {
        this.isFocused = false;
        this.parentCommentId = null;
        console.log("取消聚焦");
      }, 100);
    },
  }
};
</script>

<style scoped>
/* 全局基础样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: sans-serif;
}

body {
  background-color: #f0f2f5;
  color: #333;
}

/* 帖子整体容器 */
.post-page {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: relative;
}

/* 发帖人信息与标题 */
.post-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #ececec;
}

.post-author {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
  border: 2px solid #4dabf7;
}

.author-name {
  font-size: 18px;
  font-weight: bold;
  color: #4dabf7;
}

.post-title {
  font-size: 28px;
  color: #2c3e50;
  text-align: center;
  margin-top: 10px; 
  font-size: 34px;  
  font-weight: bold; 
}

/* 帖子正文 */
.post-content {
  background-color: #fafafa;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 6px;
  margin-bottom: 30px;
}

.post-content p {
  font-size: 24px;
  line-height: 1.8;
  white-space: pre-line;
}

/* 评论区 */
.comments-section {
  background-color: #fefefe;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 6px;
  margin-bottom: 140px;
}

.comments-title {
  font-size: 22px;
  font-weight: bold;
  color: #4dabf7;
  margin-bottom: 15px;
  text-align: center;
}

.comments-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.comment-item {
  background-color: #f1f9ff;
  border: 1px solid #cce5ff;
  border-radius: 6px;
  padding: 12px 15px;
  margin-bottom: 10px;
  transition: background-color 0.3s;
}

.comment-item:hover {
  background-color: #d0ebff;
}

.comment-text {
  font-size: 14px;
  color: #333;
}

/* 评论编辑区域（固定底部，有背景） */
.comment-editor {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ffffff;
  border-top: 1px solid #ccc;
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
  z-index: 1000;
}

.editor-textarea {
  flex: 1;
  min-height: 50px;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
  background-color: #fdfdfd;
  color: #333;
  transition: height 0.3s ease; /* 添加过渡效果 */
}

.send-btn {
  padding: 8px 16px;
  font-size: 14px;
  background-color: #4dabf7;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.send-btn:disabled {
  background-color: #a5d3fa;
  cursor: not-allowed;
}

.send-btn:hover:not(:disabled) {
  background-color: #339af0;
}

/* 响应式适配 */
@media (max-width: 600px) {
  .post-page {
    margin: 20px;
    padding: 15px;
  }

  .post-title {
    font-size: 24px;
  }

  .author-name {
    font-size: 16px;
  }

  .post-content p {
    font-size: 15px;
  }

  .comments-title {
    font-size: 20px;
  }

  .comment-text {
    font-size: 13px;
  }

  .editor-textarea {
    font-size: 13px;
  }

  .send-btn {
    font-size: 13px;
    padding: 6px 12px;
  }
}
</style>
