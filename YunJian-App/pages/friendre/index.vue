<template>
    <!-- 页面容器 -->
    <view class="container">
        <!-- 自定义搜索框 -->
        <view class="custom-search">
            <!-- 输入框，使用 v-model 绑定 keyword 变量 -->
            <input
                v-model="keyword"
                placeholder="搜索用户"
                class="search-input"
            />
            <!-- 搜索按钮，点击触发 onSearch 方法 -->
            <button class="search-btn" @click="onSearch">搜索</button>
        </view>

        <!-- 用户列表，当 userList 数组长度大于 0 时显示 -->
        <view v-if="userList.length > 0">
            <!-- 遍历 userList 数组，渲染每个用户的信息 -->
            <view
                v-for="user in userList"
                :key="user.userId"
                class="user-card"
            >
                <!-- 用户卡片头部，包含头像和基本信息 -->
                <view class="card-header">
                    <!-- 显示用户头像，使用 baseUrl 和用户头像地址拼接 -->
                    <image :src="baseUrl + user.avatar || ''" class="avatar" mode="aspectFill" />
                    <!-- 用户基本信息 -->
                    <view class="card-info">
                        <!-- 显示用户昵称 -->
                        <text class="nickname">{{ user.nickName }}</text>
                        <!-- 显示用户名 -->
                        <text class="username">{{ user.userName }}</text>
                    </view>
                </view>
                <!-- 用户其他信息，包含性别和添加好友按钮 -->
                <view class="user-info">
                    <!-- 显示用户性别，根据 sex 值判断 -->
                    <text>性别：{{ user.sex === '1' ? '男' : '女' }}</text>
                    <!-- 添加好友按钮，点击触发 handleAddFriend 方法，根据是否已发送请求禁用按钮 -->
                    <button
                        class="add-friend-btn"
                        @click="handleAddFriend(user)"
                        :disabled="hasSentRequest(user)"
                    >
                        <!-- 根据是否已发送请求显示不同的按钮文本 -->
                        {{ hasSentRequest(user) ? '已发送请求' : '添加好友' }}
                    </button>
                </view>
            </view>
        </view>

        <!-- 自定义弹窗，当 isRequestModalVisible 为 true 时显示 -->
        <view v-if="isRequestModalVisible" class="modal-mask">
            <!-- 弹窗容器 -->
            <view class="modal-container">
                <!-- 弹窗头部，包含标题和关闭按钮 -->
                <view class="modal-header">
                    <!-- 弹窗标题 -->
                    <text class="modal-title">输入问候语</text>
                    <!-- 关闭按钮，点击触发 closeRequestInput 方法 -->
                    <span class="close-btn" @click="closeRequestInput">X</span>
                </view>
                <!-- 弹窗内容，包含请求信息输入框 -->
                <view class="modal-content">
                    <!-- 输入框，使用 v-model 绑定 requestMessage 变量 -->
                    <input
                        v-model="requestMessage"
                        placeholder="输入问候语"
                        class="request-message-input"
                    />
                </view>
                <!-- 弹窗底部，包含发送按钮 -->
                <view class="modal-footer">
                    <!-- 发送按钮，点击触发 sendRequestWithMessage 方法 -->
                    <button class="confirm-btn" @click="sendRequestWithMessage">发送</button>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
// 引入配置文件
import config from '@/config'
// 引入用户相关 API
import { getUserProfile, listUser } from "@/api/system/user";
// 引入请求相关 API
import { listRequests, addRequests } from "@/api/requests/requests";

import { listFriends } from "@/api/friends/friends";

export default {
    data() {
        return {
            // 基础 URL，从配置文件中获取
            baseUrl: config.baseUrl,
            // 搜索关键词
            keyword: '',
            // 用户列表
            userList: [],
            // 已发送的请求列表
            sentRequests: [],
            // 收到的请求列表
            receivedRequests: [],
            // 已有的好友列表（避免重复请求）
            friendsList: [],
            // 当前用户信息
            user: null,
            // 控制请求输入弹窗是否显示
            isRequestModalVisible: false,
            // 选中的用户信息
            selectedUser: null,
            // 请求消息内容
            requestMessage: '',
        };
    },
    onLoad: async function() {
        // 页面加载时获取用户信息
        await this.fetchUserProfile();
        // 页面加载时获取已发送的请求列表
        await this.fetchSentRequests();
        // 获取好友列表
        await this.fetchFriendsList();
    },
    methods: {
        // 获取用户信息的异步方法
        async fetchUserProfile() {
            try {
                // 调用 API 获取用户信息
                const response = await getUserProfile();
                // 将用户信息赋值给 user 变量
                this.user = response.data;
                // 打印用户信息
                console.log('用户信息', this.user);
            } catch (error) {
                // 打印错误信息
                console.log(error);
            }
        },
        // 获取已发送请求列表的异步方法
        async fetchSentRequests() {
            try {
                // 调用 API 获取已发送的请求列表，传入当前用户 ID
                const response = await listRequests({ requesterId: this.user.userId });
                // 将已发送的请求列表赋值给 sentRequests 变量
                this.sentRequests = response.rows;
                // 打印已发送的请求列表
                console.log('已发送的请求', this.sentRequests);
            } catch (error) {
                // 打印错误信息
                console.log(error);
            }
        },
        // 获取好友列表的方法
        async fetchFriendsList() {
            try {
                const response = await listFriends({ userId: this.user.userId });
                this.friendsList = response.rows; // 缓存好友列表
                console.log('好友列表',this.friendsList);
            } catch (error) {
                console.log(error);
            }
        },
        // 搜索用户的异步方法
        async onSearch() {
            // 检查搜索关键词是否为空
            if (!this.keyword.trim()) {
                // 显示提示信息
                uni.showToast({
                    title: '搜索关键词不能为空',
                    icon: 'none',
                    duration: 1500
                });
                return;
            }
            // 打印搜索关键词
            console.log(this.keyword);
            try {
                // 调用 API 搜索用户，传入搜索关键词和排序信息
                const response = await listUser({
                    userName: this.keyword,
                    orderByColumn: "createTime",
                    isAsc: "desc",
                });
                // 将搜索结果赋值给 userList 变量
                this.userList = response.rows;
                // 打印搜索结果
                console.log('搜索结果',this.userList);
            } catch (error) {
                // 打印错误信息
                console.log(error);
            }
        },
        // 处理添加好友按钮点击事件的方法
        handleAddFriend(user) {
            // 检查是否已发送请求
            if (this.hasSentRequest(user)) {
                // 显示提示信息
                uni.showToast({
                    title: '你已发送好友请求',
                    icon: 'none',
                    duration: 1500
                });
                return;
            }
            // 打开请求输入弹窗
            this.openRequestInput(user);
        },
        // 打开请求输入弹窗的方法
        openRequestInput(user) {
            // 记录选中的用户信息
            this.selectedUser = user;
            // 显示请求输入弹窗
            this.isRequestModalVisible = true;
        },
        // 关闭请求输入弹窗的方法
        closeRequestInput() {
            // 隐藏请求输入弹窗
            this.isRequestModalVisible = false;
            // 清空请求消息内容
            this.requestMessage = '';
        },
        // 发送好友请求的异步方法
        async sendRequestWithMessage() {
            try {
                // 调用 API 发送好友请求，传入发送者 ID、接收者 ID 和请求消息
                await addRequests({
                    requesterId: this.user.userId, // 发送请求的用户 ID
                    receiverId: this.selectedUser.userId, // 接收请求的用户 ID
                    message: this.requestMessage // 请求消息
                });
                // 显示发送成功提示信息
                uni.showToast({
                    title: '好友请求已发送',
                    icon: 'success',
                    duration: 1500
                });
                // 关闭请求输入弹窗
                this.closeRequestInput();
                // 发送成功后更新已发送请求列表
                this.fetchSentRequests();
            } catch (error) {
                // 打印错误信息
                console.log(error);
                // 显示发送失败提示信息
                uni.showToast({
                    title: '发送请求失败',
                    icon: 'none',
                    duration: 1500
                });
            }
        },
        // 检查是否已发送请求的方法
         hasSentRequest(user) {
            // 遍历已发送的请求列表，检查是否存在发送给该用户的请求
            return this.sentRequests.some(request => request.receiverId === user.userId) ||
            this.friendsList.some(friend => friend.friendId === user.userId) // 同步检查好友
        }
    }
};
</script>

<style lang="scss">
/* 页面容器样式 */
.container {
    padding: 20rpx;
    background-color: #f8f8f8;
}

/* 用户卡片样式 */
.user-card {
    padding: 20rpx;
    background-color: #fff;
    border-radius: 12rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    margin-bottom: 20rpx;
}

/* 用户卡片头部样式 */
.card-header {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
}

/* 用户头像样式 */
.avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    margin-right: 20rpx;
}

/* 用户昵称样式 */
.card-info .nickname {
    font-size: 32rpx;
    font-weight: 500;
    color: #333;
}

/* 用户用户名样式 */
.card-info .username {
    font-size: 28rpx;
    color: #666;
    margin-top: 8rpx;
    display: block;
}

/* 用户其他信息样式 */
.user-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 28rpx;
    color: #999;
}

/* 请求区域样式 */
.request-area {
    padding: 20rpx;
    margin-top: 30rpx;
    background-color: #fff;
    border-radius: 12rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

/* 标题样式 */
.section-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 16rpx;
    display: block;
}

/* 请求列表样式 */
.request-list {
    border-top: 1rpx solid #f5f5f5;
}

/* 请求项样式 */
.request-item {
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f5f5f5;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 28rpx;
    color: #666;
}

/* 请求时间样式 */
.request-time {
    color: #999;
}

/* 自定义搜索框样式 */
.custom-search {
    display: flex;
    height: 80rpx;
    background-color: #fff;
    border-radius: 40rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

    /* 搜索输入框样式 */
    .search-input {
        flex: 1;
        padding: 0 40rpx;
        font-size: 28rpx;
    }

    /* 搜索按钮样式 */
    .search-btn {
        width: 120rpx;
        background-color: #007AFF;
        color: #fff;
        border: none;
        font-size: 28rpx;
    }
}

/* 请求消息输入框样式 */
.request-message-input {
    padding: 10rpx;
    border: 1rpx solid #ccc;
    border-radius: 8rpx;
    font-size: 28rpx;
    margin-top: 10rpx;
}

/* 弹窗遮罩层样式 */
.modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;
}

/* 弹窗容器样式 */
.modal-container {
    background-color: #fff;
    border-radius: 12rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    width: 80%;
    max-width: 600rpx;
}

/* 弹窗头部样式 */
.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #e5e5e5;
}

/* 弹窗标题样式 */
.modal-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #333;
}

/* 关闭按钮样式 */
.close-btn {
    font-size: 30rpx;
    color: #666;
    cursor: pointer;
    user-select: none;
}

/* 弹窗内容样式 */
.modal-content {
    padding: 30rpx;
}

/* 弹窗底部样式 */
.modal-footer {
    border-top: 1rpx solid #e5e5e5;
    padding: 20rpx 30rpx;
    display: flex;
    justify-content: flex-end;
}

/* 确认按钮样式 */
.confirm-btn {
    color: #007AFF;
    font-size: 28rpx;
    background-color: transparent;
    border: none;
    cursor: pointer;
}
</style>    