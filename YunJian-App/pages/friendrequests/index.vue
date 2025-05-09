<template>
    <!-- 组件的根容器 -->
    <div>
        <!-- 无序列表，用于展示好友请求列表 -->
        <ul>
            <!-- 使用 v-for 指令遍历 friendRequests 数组，渲染每个好友请求项 -->
            <li v-for="(request, index) in friendRequests" :key="index">
                <!-- 好友请求信息容器 -->
                <div class="request-info">
                    <!-- 显示用户头像，图片链接由 baseUrl 和请求中的 avatar 拼接而成 -->
                    <img :src="baseUrl + request.avatar" alt="用户头像" class="avatar">
                    <!-- 文本信息容器 -->
                    <div class="text-info">
                        <!-- 显示用户账号和昵称 -->
                        <p class="account-nickname">账号: {{ request.userName }} 昵称: {{ request.nickName }}</p>
                        <!-- 显示好友请求的问候语 -->
                        <p class="greeting">问候语: {{ request.message }}</p>
                    </div>
                </div>
                <!-- 按钮容器，包含同意和拒绝按钮 -->
                <div class="button-container">
                    <!-- 点击同意按钮，调用 acceptRequest 方法处理该好友请求 -->
                    <button @click="acceptRequest(index)">同意</button>
                    <!-- 点击拒绝按钮，调用 rejectRequest 方法处理该好友请求 -->
                    <button @click="rejectRequest(index)">拒绝</button>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
// 导入配置文件
import config from '@/config'
// 导入请求列表和删除请求的 API 方法
import { listRequests, delRequests } from '@/api/requests/requests'
// 导入获取用户信息和用户列表的 API 方法
import { getUserProfile, listUser } from '@/api/system/user'
// 导入添加好友的 API 方法
import { addFriends } from '@/api/friends/friends'
// 导入添加会话和获取会话列表的 API 方法
import { addConversations, listConversations } from '@/api/conversations/conversations'
// 导入添加参与者和获取参与者列表的 API 方法
import { addParticipants, listParticipants } from '@/api/participants/participants'

export default {
    // 组件的数据选项
    data() {
        return {
            // 基础图片链接地址
            baseUrl: config.baseUrl,
            // 存储好友请求列表，初始值为 null
            friendRequests: null,
            // 存储当前用户信息，初始值为 null
            user: null,
        };
    },
    // 组件创建完成后自动执行的钩子函数
    async created() {
        // 调用 getUser 方法获取当前用户信息
        await this.getUser();
        // 调用 listRequestsuser 方法获取好友请求列表
        await this.listRequestsuser();
    },
    // 组件的方法选项
    methods: {
        // 获取好友请求列表的异步方法
        async listRequestsuser() {
            // 调用 listRequests API 方法，根据当前用户 ID 获取请求列表
            const list1 = await listRequests({
                requesterId: this.user.userId,
            });
            // 用于存储处理后的好友请求列表
            const list2 = [];
            // 遍历获取到的请求列表
            for (let i = 0; i < list1.rows.length; i++) {
                const item = list1.rows[i];
                // 根据请求中的 receiverId 调用 listUser API 方法获取用户信息
                const k = await listUser({
                    userId: item.receiverId,
                });
                // 将获取到的用户信息添加到 list2 数组中
                list2.push(k.rows[0]);
                // 为 list2 中的对象添加问候语信息
                list2[i].message = item.message;
                // 为 list2 中的对象添加请求 ID 信息
                list2[i].id = item.id;
                // 为 list2 中的对象添加接收者 ID 信息
                list2[i].receiverId = item.receiverId;
            }
            // 将处理后的好友请求列表赋值给 friendRequests 数据属性
            this.friendRequests = list2;
            // 打印好友请求列表信息
            console.log('请求', this.friendRequests);
        },

        // 获取当前用户信息的异步方法
        async getUser() {
            // 调用 getUserProfile API 方法获取当前用户信息
            this.user = await getUserProfile();
            // 将用户信息中的 data 属性赋值给 user 数据属性
            this.user = this.user.data
            // 打印当前用户信息
            console.log('me', this.user);
        },

        // 同意好友请求的异步方法
        async acceptRequest(index) {
            try {
                // 打印同意的好友请求的接收者 ID 信息
                console.log(`同意了 ${this.friendRequests[index].receiverId} 的好友请求`);
                // 调用 addFriends API 方法，将当前用户和请求者添加为好友
                await addFriends({
                    userId: this.user.userId,
                    friendId: this.friendRequests[index].receiverId,
                });
                // 如果请求者 ID 不等于当前用户 ID
                if (this.friendRequests[index].receiverId != this.user.userId) {
                    // 调用 addFriends API 方法，将请求者和当前用户添加为好友
                    await addFriends({
                        userId: this.friendRequests[index].receiverId,
                        friendId: this.user.userId,
                    });
                }

                // 调用 addConversations API 方法，创建一个单聊会话
                await addConversations({
                    conversationName: this.friendRequests[index].nickName,
                    isGroupChat: '0',
                });
                // 调用 listConversations API 方法，获取最新创建的会话信息
                const conversations = await listConversations({
                    orderByColumn: "createdAt",
                    isAsc: "desc",
                    pageNum: 1,
                    pageSize: 1,
                });
                // 获取最新创建的会话
                const latestConversation = conversations.rows[0];
                // 打印最新创建的会话信息
                console.log('会话', latestConversation)
                // 如果存在最新创建的会话
                if (latestConversation) {
                    // 调用 addParticipants API 方法，将当前用户添加到会话中
                    await addParticipants({
                        conversationId: latestConversation.id,
                        userId: this.user.userId,
                        participantIdentity: '普通成员',
                    });
                    // 如果请求者 ID 不等于当前用户 ID
                    if (this.friendRequests[index].receiverId != this.user.userId) {
                        // 调用 addParticipants API 方法，将请求者添加到会话中
                        await addParticipants({
                            conversationId: latestConversation.id,
                            userId: this.friendRequests[index].receiverId,
                            participantIdentity: '普通成员',
                        });
                    }
                    // 调用 delRequests API 方法，删除该好友请求
                    await delRequests(this.friendRequests[index].id);
                    // 从 friendRequests 数组中移除该请求项
                    this.friendRequests.splice(index, 1);
                }
            } catch (error) {
                // 捕获并打印处理好友请求时出现的错误信息
                console.error('处理好友请求时出错:', error);
            }
        },
        // 拒绝好友请求的异步方法
        async rejectRequest(index) {
            try {
                // 打印拒绝的好友请求的 ID 信息
                console.log(`拒绝了 ${this.friendRequests[index].id} 的好友请求`);
                // 调用 delRequests API 方法，删除该好友请求
                await delRequests(this.friendRequests[index].id);
                // 从 friendRequests 数组中移除该请求项
                this.friendRequests.splice(index, 1);
            } catch (error) {
                // 捕获并打印拒绝好友请求时出现的错误信息
                console.error('拒绝好友请求时出错:', error);
            }
        }
    }
};
</script>

<style scoped>
/* 设置标题颜色 */
h1 {
    color: #333;
}

/* 去除无序列表的默认样式 */
ul {
    list-style-type: none;
    padding: 0;
}

/* 设置列表项的样式 */
li {
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid #ccc;
    padding: 10px;
    border-radius: 5px;
}

/* 设置请求信息容器的样式 */
.request-info {
    display: flex;
    align-items: center;
}

/* 设置头像的样式 */
.avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
}

/* 设置文本信息容器的样式 */
.text-info {
    display: flex;
    flex-direction: column;
}

/* 设置账号和昵称的样式 */
.account-nickname {
    margin: 0;
    font-weight: bold;
}

/* 设置问候语的样式 */
.greeting {
    margin: 0;
    color: #666;
}

/* 设置按钮容器的样式 */
.button-container {
    display: flex;
    gap: 10px;
}

/* 设置按钮的样式 */
button {
    padding: 5px 10px;
    border: none;
    border-radius: 3px;
    cursor: pointer;
}

/* 设置按钮悬停时的样式 */
button:hover {
    opacity: 0.8;
}

/* 设置同意按钮的样式 */
button:nth-child(1) {
    background-color: #28a745;
    color: white;
}

/* 设置拒绝按钮的样式 */
button:nth-child(2) {
    background-color: #dc3545;
    color: white;
}
</style>