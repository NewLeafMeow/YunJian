<template>
  <!-- 个人中心页面容器，动态设置高度 -->
  <view class="mine-container" :style="{height: `${windowHeight}px`}">
    
    <!-- 顶部个人信息栏 -->
    <view class="header-section">
      <view class="flex padding justify-between">
        
        <!-- 用户头像和登录信息 -->
        <view class="flex align-center">
          <!-- 如果没有头像，显示默认头像 -->
          <view v-if="!avatar" class="cu-avatar xl round bg-white">
            <view class="iconfont icon-people text-gray icon"></view>
          </view>
          <!-- 如果有头像，显示用户头像 -->
          <image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round" mode="widthFix"></image>
          
          <!-- 未登录状态，提示点击登录 -->
          <view v-if="!name" @click="handleToLogin" class="login-tip">
            点击登录
          </view>
          <!-- 已登录状态，显示用户名 -->
          <view v-if="name" @click="handleToInfo" class="user-info">
            <view class="u_title">
              用户名：{{ name }}
            </view>
          </view>
        </view>

        <!-- 个人信息入口 -->
        <view @click="handleToInfo" class="flex align-center">
          <text>个人信息</text>
          <view class="iconfont icon-right"></view>
        </view>
      </view>
    </view>

    <!-- 功能区域 -->
    <view class="content-section">
      
      <!-- 个人中心快捷操作 -->
      <view class="mine-actions grid col-4 text-center">
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-service text-blue icon"></view>
          <text class="text">在线客服</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-community text-mauve icon"></view>
          <text class="text">反馈社区</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-dianzan text-green icon"></view>
          <text class="text">点赞我们</text>
        </view>
      </view>

      <!-- 菜单列表 -->
      <view class="menu-list">
        <view class="list-cell list-cell-arrow" @click="handleToEditInfo">
          <view class="menu-item-box">
            <view class="iconfont icon-user menu-icon"></view>
            <view>编辑资料</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleHelp">
          <view class="menu-item-box">
            <view class="iconfont icon-help menu-icon"></view>
            <view>常见问题</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleAbout">
          <view class="menu-item-box">
            <view class="iconfont icon-aixin menu-icon"></view>
            <view>关于我们</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleToSetting">
          <view class="menu-item-box">
            <view class="iconfont icon-setting menu-icon"></view>
            <view>应用设置</view>
          </view>
        </view>
      </view>

    </view>
  </view>
</template>

<script>
import storage from '@/utils/storage'

export default {
  data() {
    return {
      // 获取 Vuex 中的用户名
      name: this.$store.state.user.name,
      // 获取全局应用配置信息中的版本号
      version: getApp().globalData.config.appInfo.version
    }
  },
  computed: {
    // 获取 Vuex 中的用户头像
    avatar() {
      return this.$store.state.user.avatar
    },
    // 计算页面的高度
    windowHeight() {
      return uni.getSystemInfoSync().windowHeight - 50
    }
  },
  methods: {
    // 跳转到个人信息页面
    handleToInfo() {
      this.$tab.navigateTo('/pages/mine/info/index')
    },
    // 跳转到编辑资料页面
    handleToEditInfo() {
      this.$tab.navigateTo('/pages/mine/info/edit')
    },
    // 跳转到设置页面
    handleToSetting() {
      this.$tab.navigateTo('/pages/mine/setting/index')
    },
    // 未登录时，跳转到登录页面
    handleToLogin() {
      this.$tab.reLaunch('/pages/login')
    },
    // 跳转到头像编辑页面
    handleToAvatar() {
      this.$tab.navigateTo('/pages/mine/avatar/index')
    },
    // 退出登录
    handleLogout() {
      this.$modal.confirm('确定注销并退出系统吗？').then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$tab.reLaunch('/pages/index')
        })
      })
    },
    // 跳转到帮助页面
    handleHelp() {
      this.$tab.navigateTo('/pages/mine/help/index')
    },
    // 跳转到关于我们页面
    handleAbout() {
      this.$tab.navigateTo('/pages/mine/about/index')
    },
    // 功能建设中提示
    handleBuilding() {
      this.$modal.showToast('模块建设中~')
    }
  }
}
</script>

<style lang="scss">
/* 设置整体页面背景色 */
page {
  background-color: #f5f6f7;
}

.mine-container {
  width: 100%;
  height: 100%;

  /* 顶部个人信息栏样式 */
  .header-section {
    padding: 15px 15px 45px 15px;
    background-color: #3c96f3;
    color: white;

    .login-tip {
      font-size: 18px;
      margin-left: 10px;
    }

    .cu-avatar {
      border: 2px solid #eaeaea;

      .icon {
        font-size: 40px;
      }
    }

    .user-info {
      margin-left: 15px;

      .u_title {
        font-size: 18px;
        line-height: 30px;
      }
    }
  }

  /* 主要内容区域 */
  .content-section {
    position: relative;
    top: -50px;

    /* 个人中心快捷操作样式 */
    .mine-actions {
      display: flex;
      justify-content: center; /* 水平居中 */
      align-items: center;    /* 垂直居中 */
      margin: 15px 15px;
      padding: 20px 0px;
      border-radius: 8px;
      background-color: white;

      .action-item {
        .icon {
          font-size: 28px;
        }

        .text {
          display: block;
          font-size: 13px;
          margin: 8px 0px;
        }
      }
    }
  }
}
</style>
