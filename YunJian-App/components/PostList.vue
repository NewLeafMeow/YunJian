<template>
  <view class="post-list-container">
      <view v-if="posts.length" style="width: 100%;">
          <block v-for="post in posts" :key="post.id">
              <view class="post-item" @click="handleGoToPost(post)">
                  <!-- 发帖人信息 -->
                  <view class="post-user">
                      <image
                          class="user-avatar"
                          :src="post.avatar || ''"
                          alt="用户头像"
                      />
                      <view class="user-info">
                          <text class="user-name">
                              {{ post.nickName || '' }}:{{ post.userName }}
                          </text>
                          <text class="post-time">{{ post.createdAt || '' }}</text>
                      </view>
                  </view>
                  <!-- 帖子标题 -->
                  <view class="post-header">
                      <text class="post-title">{{ post.title }}</text>
                  </view>
                  <!-- 媒体内容 -->
                  <view class="post-body">
                      <rich-text :nodes="post.media || '无内容'"></rich-text>
                  </view>
                  <!-- 底部操作 -->
                  <view class="post-footer">
                      <view class="post-action">
                          <image
                              class="action-icon"
                              src="https://images.icon-icons.com/936/PNG/512/comment-white-oval-bubble_icon-icons.com_73613.png"
                              alt="评论"
                          />
                          <text class="action-text">{{ post.commentCount || 0 }}</text>
                      </view>
                      <view class="post-action" @click.stop="handleLikePost(post)">
                          <image
                              class="action-icon"
                              :src="post.isLiked ? '/static/images/post/ylike.png' : '/static/images/post/nlike.png'"
                              alt="点赞"
                          />
                          <text class="action-text">{{ post.likeCount || 0 }}</text>
                      </view>
                  </view>
              </view>
          </block>
      </view>
      <view v-else class="no-posts">
          <text>暂无帖子信息</text>
      </view>
      <!-- 加载提示 -->
      <view v-if="isLoading" class="loading-tip">正在加载更多...</view>
  </view>
</template>

<script>
import { listProfile, updateProfile } from "@/api/profile/profile";
import { listLikes, addLikes, delLikes } from "@/api/likes/likes";

export default {
  props: {
      posts: {
          type: Array,
          default: () => []
      },
      isLoading: {
          type: Boolean,
          default: false
      },
      userId: {
          type: [Number, null],
          default: null
      },
  },
  mounted() {
    // 组件首次挂载时调用（处理初始传入的 posts）
    this.fetchLikeCountsAndCheckLiked();
  },
  watch: {
    // 监听 posts 变化（处理父组件后续更新的数据）
    posts(newPosts) {
      if (newPosts.length > 0) { // 避免空数组重复请求
        this.fetchLikeCountsAndCheckLiked();
      }
    }
  },
  methods: {
      // 跳转帖子详情
      async handleGoToPost(post) {
        let listprofile = await listProfile({
          userId: this.userId,
        });
        listprofile = listprofile.rows[0];
        let category = '';
        switch (post.category) {
          case "体育":
            category = 'sportsClickCount';
            break;
          case "娱乐":
            category = 'entertainmentClickCount';
            break;
          case "家居":
            category = 'homeClickCount';
            break;
          case "房产":
            category = 'realEstateClickCount';
            break;
          case "教育":
            category = 'educationClickCount';
            break;
          case "时尚":
            category = 'fashionClickCount';
            break;
          case "时政":
            category = 'politicsClickCount';
            break;
          case "游戏":
            category = 'gamingClickCount';
            break;
          default:
            this.$tab.navigateTo(`/pages/post/index?postId=${post.id}`);
            return;
        }
        await updateProfile({
          id: listprofile.id,
          [category]: listprofile[category] + 1
        })
        this.$tab.navigateTo(`/pages/post/index?postId=${post.id}`);
      },

      // 点赞/取消点赞时更新用户分类计数（公共逻辑提取）
      async updateUserCategoryCount(post, isLike) {
        if (!this.userId) return; // 检查用户是否登录

        try {
          // 等待接口返回（关键修复点）
          const response = await listProfile({ userId: this.userId });
          const userProfile = response.rows[0] || {}; // 防止 rows 为 undefined
          let category = '';

          switch (post.category) {
            case "体育":
              category = "sportsLikeCount";
              break;
            case "娱乐":
              category = "entertainmentLikeCount";
              break;
            case "家居":
              category = "homeLikeCount";
              break;
            case "房产":
              category = "realEstateLikeCount";
              break;
            case "教育":
              category = "educationLikeCount";
              break;
            case "时尚":
              category = "fashionLikeCount";
              break;
            case "时政":
              category = "politicsLikeCount";
              break;
            case "游戏":
              category = "gamingLikeCount";
              break;
            default:
              return; // 或设置默认分类，避免无效更新
          }

          // 确保用户资料中存在该计数（避免 undefined + 1）
          userProfile[category] = typeof userProfile[category] === "number" 
            ? userProfile[category]
            : 0;

          await updateProfile({
            id: userProfile.id, // 确保 userProfile.id 存在（根据接口文档）
            [category]: isLike ? userProfile[category] + 1 : userProfile[category] - 1
          });
        } catch (error) {
          console.error("更新用户分类计数失败:", error);
        }
      },

      // 点赞操作（修改后）
      handleLikePost(post) {
        console.log("点赞的帖子ID:", post.id);
        if (post.isLiked) {
          delLikes(post.likeId)
            .then(() => {
              post.likeCount = Math.max((post.likeCount || 1) - 1, 0);
              post.isLiked = false;
              post.likeId = null;
              console.log("取消点赞成功");
              this.fetchLikeCountsAndCheckLiked();
              // 调用公共方法（传入 false 表示取消点赞）
              this.updateUserCategoryCount(post, false);
            })
            .catch((err) => {
              console.error("取消点赞失败", err);
            });
        } else {
          addLikes({
            userId: this.userId,
            targetType: "post",
            targetId: post.id,
            createdAt: null,
          })
            .then((res) => {
              post.likeCount = (post.likeCount || 0) + 1;
              post.isLiked = true;
              post.likeId = res.id;
              console.log("点赞成功");
              this.fetchLikeCountsAndCheckLiked();
              // 调用公共方法（传入 true 表示点赞）
              this.updateUserCategoryCount(post, true);
            })
            .catch((err) => {
              console.error("点赞失败", err);
            });
        }
      },

      // 查询每个帖子的点赞数量和检测用户是否点赞
      async fetchLikeCountsAndCheckLiked() {
      const promises = this.posts.map(async (post) => {
        try {
          const res = await listLikes({ targetId: post.id, targetType: "post" });
          // 使用 $set 确保属性响应式
          this.$set(post, 'likeCount', res.rows ? res.rows.length : 0);
          const userLike = res.rows.find(like => like.userId === this.userId);
          this.$set(post, 'isLiked', !!userLike);
          this.$set(post, 'likeId', userLike ? userLike.id : null);
        } catch (err) {
          console.error(`查询帖子 ${post.id} 的点赞数量和是否点赞失败:`, err);
        }
        return post;
      });

      const updatedPosts = await Promise.all(promises);
      this.$emit('update:posts', updatedPosts); // 通知父组件更新 posts
    }
  }
};
</script>

<style scoped>
/* 整体容器样式 */
.post-list-container {
  padding: 2rem;
  background-color: #f4f4f9;
  min-height: 100vh;
  box-sizing: border-box;
  width: 100%;
}

/* 帖子项样式 */
.post-item {
  background-color: #ffffff;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 1.5rem;
  transition: all 0.3s ease;
}

.post-item:hover {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 发帖人信息样式 */
.post-user {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 1rem;
  margin-bottom: 1rem;
}

.user-avatar {
  width: 3rem;
  height: 3rem;
  border-radius: 50%;
  margin-right: 1rem;
  object-fit: cover;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #333;
}

.post-time {
  font-size: 0.875rem;
  color: #777;
  margin-top: 0.25rem;
}

/* 帖子标题样式 */
.post-header {
  margin-bottom: 1rem;
}

.post-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #222;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 0.5rem;
  display: block;
}

/* 媒体内容样式 */
.post-body {
  min-height: 5rem;
  word-break: break-all;
  overflow: hidden;
  line-height: 1.75;
  margin-bottom: 1.5rem;
  color: #555;
}

/* 底部操作样式 */
.post-footer {
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1px solid #eaeaea;
  padding-bottom: 0.5rem;
  padding-top: 0.5rem;
  background-color: #d2ffbf;
  border-radius: 0 0 12px 12px;
}

.post-action {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-icon {
  width: 1.5rem;
  height: 1.5rem;
}

.action-text {
  font-size: 1rem;
  color: #666;
}

/* 无帖子信息样式 */
.no-posts {
  width: 100%;
  text-align: center;
  color: #999;
  font-size: 1.125rem;
  padding: 2rem 0;
}

/* 加载提示样式 */
.loading-tip {
  width: 100%;
  text-align: center;
  color: #999;
  font-size: 1.125rem;
  padding: 1rem 0;
}
</style>    