<template>
  <view class="content">
    <!-- 顶部区域保持不变 -->
    <view class="header">
      <image
        class="avatar"
        v-if="avatar"
        :src="avatar"
        @click.stop="onAvatarClick"
      ></image>
      <image class="logo" src="@/static/logo.png"></image>
    </view>

    <!-- 帖子列表组件 -->
    <view class="main-content">
      <PostList
        :posts="posts"
        :isLoading="isLoading"
        :userId="userId"
        @go-to-post="goToPost"
        @like-post="likePost"
      />
    </view>

    <!-- 侧边栏及其外部覆盖层（保持原有代码） -->
    <view v-if="isDrawerOpen" class="overlay" @click="closeDrawer">
      <view class="drawer" @click.stop>
        <view class="user-info">
          <image class="avatar" v-if="avatar" :src="avatar"></image>
          <view class="info-text">
            <text class="nickname" v-if="user">昵称:{{ user }}</text>
            <text class="id" v-if="name">账号:{{ name }}</text>
          </view>
        </view>
        <view class="clickable-box" @click="onBoxClick1">
          <text>添加好友</text>
        </view>
        <view class="clickable-box" @click="onBoxClick2">
          <text>好友请求</text>
        </view>
        <view class="clickable-box" @click="onBoxClick3">
          <text>好友列表</text>
        </view>
      </view>
    </view>

    <!-- 新增：右下角固定按钮 -->
    <view class="fixed-button" @click="goToNewPage">
      <text>+</text>
    </view>
  </view>
</template>

<script>
  import config from '@/config'
  import { getUserProfile, listUser } from "@/api/system/user";
  import { listPosts } from "@/api/posts/posts";
  import { listComments } from "@/api/comments/comments";
  import { listProfile } from "@/api/profile/profile";
  import PostList from '@/components/PostList.vue'; // 引入封装的组件

  export default {
    components: { PostList }, // 注册组件
    data() {
      return {
        user: null,
        avatar: this.$store.state.user.avatar,
        name: this.$store.state.user.name,
        isDrawerOpen: false,
        posts: [],
        userId: null,
        pageNum: 1,
        pageSize: 1,
        isLoading: false,
        hasMore: true,
        userlike: null,
      };
    },
    onLoad() {
      this.getUser();
      this.getPosts();
      console.log("用户名：", this.name);
    },
    onReachBottom() {
      if (!this.isLoading && this.hasMore) {
        this.pageNum++;
        this.getPosts();
      }
    },
    methods: {
      // 获取用户信息
      async getUser() {
        const response = await getUserProfile();
        this.user = response.data.nickName;
        this.userId = response.data.userId;
        console.log("用户ID：", this.userId);
        let list1 = await listProfile({
          userId: this.userId,
        });
        list1 = list1.rows[0];
        console.log("用户画像：", list1);
        let categoryWeightList = [];
        categoryWeightList.体育 = (list1.sportsClickCount * 1 + list1.sportsLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).体育 * 5 || 0) + 1);
        categoryWeightList.娱乐 = (list1.entertainmentClickCount * 1 + list1.entertainmentLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).娱乐 * 5 || 0) + 1);
        categoryWeightList.家居 = (list1.homeClickCount * 1 + list1.homeLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).家居 * 5 || 0) + 1);
        categoryWeightList.财经 = (list1.financeClickCount * 1 + list1.financeLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).财经 * 5 || 0) + 1);
        categoryWeightList.时政 = (list1.politicsClickCount * 1 + list1.politicsLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).时政 * 5 || 0) + 1);
        categoryWeightList.房产 = (list1.realEstateClickCount * 1 + list1.realEstateLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).房产 * 5 || 0) + 1);
        categoryWeightList.科技 = (list1.technologyClickCount * 1 + list1.technologyLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).科技 * 5 || 0) + 1);
        categoryWeightList.游戏 = (list1.gamingClickCount * 1 + list1.gamingLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).游戏 * 5 || 0) + 1);
        categoryWeightList.时尚 = (list1.fashionClickCount * 1 + list1.fashionLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).时尚 * 5 || 0) + 1);
        categoryWeightList.教育 = (list1.educationClickCount * 1 + list1.educationLikeCount * 3 + 1) * (((await JSON.parse(list1.searchRecordType)).教育 * 5 || 0) + 1);
        console.log("用户画像权重：", categoryWeightList);
        // 查找权重最高的类别及其权重值
        let maxWeight = -Infinity;
        let maxCategory = '';

        for (const category in categoryWeightList) {
            if (categoryWeightList[category] > maxWeight) {
                maxWeight = categoryWeightList[category];
                maxCategory = category;
            }
        }
        console.log("用户画像权重最高的类别：", maxCategory,'权重值：', maxWeight);
        this.userlike = maxCategory;
      },
      getPosts() {
        this.isLoading = true;
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          category: this.userlike,
          orderByColumn: "updatedAt",
          isAsc: "desc",
        };
        listPosts(params)
          .then(response => {
            let newPosts = response.rows.map(post => {
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
              return post;
            });
            const existingIds = this.posts ? this.posts.map(post => post.id) : [];
            const hasDuplicate = newPosts.some(post => existingIds.includes(post.id));
            if (hasDuplicate) {
              this.hasMore = false;
              this.isLoading = false;
              return [];
            }
            return Promise.all(
              newPosts.map(post => {
                return Promise.all([
                  listUser({ pageNum: 1, pageSize: 1, userId: post.userId }),
                  listComments({ postId: post.id, parentCommentId: null })
                ])
                  .then(([userRes, commentsRes]) => {
                    if (userRes.rows && userRes.rows.length > 0) {
                      const userData = userRes.rows[0];
                      post.nickName = userData.nickName;
                      post.userName = userData.userName;
                      post.avatar = config.baseUrl + userData.avatar;
                      post.commentCount = commentsRes.rows.length;
                    }
                    return post;
                  })
                  .catch(e => {
                    console.error("获取用户信息或点赞记录失败", e);
                    return post;
                  });
              })
            );
          })
          .then(updatedPosts => {
            if (updatedPosts.length > 0) {
              if (this.pageNum === 1) {
                this.posts = updatedPosts;
              } else {
                this.posts = this.posts.concat(updatedPosts);
              }
              console.log("更新后的帖子数据：", this.posts);
            }
            this.isLoading = false;
          })
          .catch(error => {
            console.error("获取帖子失败:", error);
            this.isLoading = false;
          });
      },
      
      // 其他方法（侧边栏、跳转等）保持不变
      onAvatarClick() {
        this.isDrawerOpen = true;
      },
      closeDrawer() {
        this.isDrawerOpen = false;
      },
      onBoxClick1() {
        this.$tab.navigateTo('/pages/friendre/index')
      },
      onBoxClick2(){
        this.$tab.navigateTo('/pages/friendrequests/index')
      },
      onBoxClick3(){
        this.$tab.navigateTo('/pages/friends/index')
      },
      goToNewPage() {
        this.$tab.navigateTo('/pages/newpost');
      }
    }
  };
</script>

<style scoped>
.content {
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 原有样式保持不变 */
.header {
  position: relative;
  width: 100%;
  height: 50px;
  padding: 20rpx;
  background-color: #ffffff;
  border-bottom: 1rpx solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 70rpx;
  height: 70rpx;
  border-radius: 50%;
  position: absolute;
  left: 20rpx;
}

.logo {
  width: 100rpx;
  height: 100rpx;
}

.main-content {
  width: 100%;
  padding: 20rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ececec;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

.drawer {
  position: absolute;
  top: 0;
  left: 0;
  width: 60%;
  height: 100%;
  background-color: #fff;
  box-shadow: 2rpx 0 5rpx rgba(0, 0, 0, 0.1);
  padding: 20rpx;
}

.user-info,
.info-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10rpx;
}

.nickname,
.id {
  font-size: 28rpx;
  color: #333;
}

.clickable-box {
  padding: 20rpx;
  margin-bottom: 10rpx;
  background-color: #f9f9f9;
  border: 1rpx solid #ccc;
  border-radius: 5rpx;
  text-align: center;
  cursor: pointer;
}

.clickable-box:hover {
  background-color: #e0e0e0;
}

/* 新增：固定按钮样式 */
.fixed-button {
  position: fixed;
  right: 50rpx;
  bottom: 150rpx;
  background-color: #007aff;
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001;
}

.fixed-button text {
  color: #fff;
  font-size: 30rpx;
}

/* 新增：加载提示样式 */
.loading-tip {
  margin-top: 20rpx;
  color: #999;
  font-size: 24rpx;
}
</style>