<template>
    <div>
        <ul>
        <li v-for="(request, index) in friends" :key="index" @click="handleFriendClick(request)">
        <div class="friends-info">
            <img :src="baseUrl + request.avatar" alt="用户头像" class="avatar">
            <div class="text-info">
            <p class="account-nickname">账号: {{ request.userName }} 昵称: {{ request.nickName }}</p>
            </div>
        </div>
        </li>
    </ul>

    <!-- 弹窗组件 -->
    <div v-show="showDialog" class="dialog-mask">
        <div class="dialog-content">
        <div class="dialog-header">
            <h3>用户详情</h3>
            <span class="close-btn" @click="showDialog = false">×</span>
        </div>
        <div class="dialog-body">
            <img :src="baseUrl + selectedFriend.avatar" alt="用户头像" class="dialog-avatar">
            <div class="detail-info">
            <p><span>账号:</span> {{ selectedFriend.userName }}</p>
            <p><span>昵称:</span> {{ selectedFriend.nickName }}</p>
            <p v-if="selectedFriend.phone"><span>手机号:</span> {{ selectedFriend.phone }}</p>
            <p v-if="selectedFriend.email"><span>邮箱:</span> {{ selectedFriend.email }}</p>
            <p v-if="selectedFriend.createTime"><span>注册时间:</span> {{ formatDate(selectedFriend.createTime) }}</p>
            <div class="button-group">
                <button @click="deleteItem(selectedFriend)">删除</button><button @click="sendMessage(selectedFriend)">发消息</button>
            </div>
            </div>
        </div>
        </div>
    </div>
    </div>
</template>

<script>
import config from '@/config'
import { getUserProfile, listUser } from '@/api/system/user'
import { listFriends, delFriends } from '@/api/friends/friends'
// 导入添加会话和获取会话列表的 API 方法
import { listConversations } from '@/api/conversations/conversations'
// 导入添加参与者和获取参与者列表的 API 方法
import { listParticipants } from '@/api/participants/participants'


export default { 
    data() {
    return {
        baseUrl: config.baseUrl,
        friends: null,
        user: null,
        showDialog: false, // 控制弹窗显示
        selectedFriend: {}, // 选中的好友信息
        conversation_id: null
    };
    },
    async onLoad() {
        await this.getUser();
        await this.listfriendsuser();
    },
    methods: {
        // 格式化时间
        formatDate(time) {
        return new Date(time).toLocaleString();
        },

        async listfriendsuser(){
            const list1 = await listFriends({
                userId: this.user.userId,
            });
            const list2 = [];
            for (let i = 0; i < list1.rows.length; i++) {
                const item = list1.rows[i];
                const k = await listUser({
                    userId: item.friendId,
                });
                // 假设用户详情包含更多字段，这里合并数据
                const friendInfo = {
                ...k.rows[0],
                message: item.message,
                id: item.id,
                receiverId: item.receiverId
                };
                list2.push(friendInfo);
            }
            this.friends = list2;
            console.log('列表',this.friends);
        },

        async getUser(){
            this.user = await getUserProfile();
            this.user = this.user.data;
            console.log('me',this.user);
        },

        handleFriendClick(friend) {
            this.selectedFriend = friend; // 保存选中的好友信息
            console.log(this.selectedFriend)
            this.showDialog = true; // 显示弹窗
        },

        async sendMessage() {
            let list1 = await listParticipants({
                userId: this.user.userId,
                participantIdentity: '普通成员',
            });
            list1=list1.rows;
            console.log('1',list1);
            let list2 = await listParticipants({
                userId: this.selectedFriend.userId,
                participantIdentity: '普通成员',
            });
            list2=list2.rows;
            console.log('2',list2);

            // 找出两个列表中相同id
            for (let i=0;i<list1.length;i++){
                for(let k=0;k<list2.length;k++){
                    if(list1[i].conversationId==list2[k].conversationId){
                        this.conversation_id=list1[i].conversationId;
                    }
                }
            }
            console.log('会话id',this.conversation_id);
            this.$tab.navigateTo(`/pages/message/messages?conversation=${this.conversation_id}`);
        },

        async deleteItem() {
            try {
                if (confirm('确定要删除该好友吗？')) {
                    // 调用删除好友的 API
                    const result = await delFriends(this.selectedFriend.id);
                    if (result) {
                        // 从好友列表中移除该好友
                        this.friends = this.friends.filter(friend => friend.userId !== this.selectedFriend.userId);
                        this.showDialog = false; // 关闭弹窗
                        alert('好友删除成功');
                    } else {
                        alert('好友删除失败');
                    }
                }
            } catch (error) {
                console.error('删除好友时出错:', error);
                alert('删除好友时出现错误');
            }
        }
    }
};
</script>

<style scoped>
/* 弹窗样式 */
.dialog-mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.dialog-content {
    background: white;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    max-width: 90%;
}

.dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.close-btn {
    font-size: 1.5rem;
    cursor: pointer;
    color: #666;
}

.dialog-body {
    display: flex;
    gap: 20px;
}

.dialog-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
}

.detail-info p {
    margin: 8px 0;
    font-size: 14px;
    color: #333;
}

.detail-info span {
    font-weight: 500;
    color: #666;
    margin-right: 8px;
}

ul {
    list-style-type: none;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    }

li {
    background-image: linear-gradient(135deg, 
    #ffb7b7, #330055, #001eff, #00ff26, #e1ff00,
    #ff7c7c, #330055, #001eff, #00ff26, #e1ff00,
    #ff7c7c, #330055, #001eff, #00ff26, #e1ff00,
    #ff7c7c, #330055, #001eff, #00ff26, #e1ff00,
    #ff7c7c, #330055, #001eff, #00ff26, #e1ff00,
    #ff7c7c, #330055, #001eff, #00ff26, #e1ff00);
    margin: 15px;
    padding: 5px;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    display: flex;
    align-items: center;
    transition: box-shadow 0.3s ease;
}

li:hover {
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.friends-info {
    background-color: rgb(207, 233, 255);
    display: block;
    width: 100%;
    display: flex;
    align-items: center;
    font-size: 12px;
}

.avatar {
    width: 3.5rem;
    height: 3.5rem;
    border-radius: 50%;
    margin-right: 0.625rem;
    object-fit: cover;
}

.text-info {
    display: flex;
    flex-direction: column;
}

.account-nickname {
    margin: 0;
    font-weight: bold;
    color: #2d3748;
}
/* 新添加的按钮组样式 */
.button-group {
    display: flex;
    gap: 10px; /* 按钮之间的间距 */
}

button {
    padding: 8px 16px;
    border: none;
    width: 50%;
    border-radius: 4px;
    background-color: #007bff;
    color: white;
    cursor: pointer;
    font-size: 15px;
}

button:hover {
    background-color: #0056b3;
}
</style>