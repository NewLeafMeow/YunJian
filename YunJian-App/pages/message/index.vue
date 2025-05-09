<template>
  <div id="app">
    <ul>
      <li v-for="chat in chats" :key="chat.id" @click="handleChatClick(chat)">
        <img :src="chat.icon" alt="Chat Icon" />
        <div>
          <h3>{{ chat.conversationName }}</h3>
          <p>{{ chat.messageContent }}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { getUserProfile } from "@/api/system/user";
// 导入添加会话和获取会话列表的 API 方法
import { getConversations } from '@/api/conversations/conversations'
// 导入添加参与者和获取参与者列表的 API 方法
import { listParticipants } from '@/api/participants/participants'
// 会话消息
import { listMessages } from '@/api/messages/messages'

export default {
  data() {
    return {
      user: null,
      chats: [], // 初始化为空数组
    };
  },
  async created() {
    await this.getuser();
    await this.getconversations();
  },
  methods: {
    // 获取当前用户信息的异步方法
    async getuser() {
      // 调用 getUserProfile API 方法获取当前用户信息
      this.user = await getUserProfile();
      // 将用户信息中的 data 属性赋值给 user 数据属性
      this.user = this.user.data
      // 打印当前用户信息
      console.log('me', this.user);
    },
    // 获取会话列表
    async getconversations() {
      const participants = await listParticipants({
        userId: this.user.userId,
      });
      this.chats = participants.rows;
      console.log('参与的会话', this.chats);
      for (let i = 0; i < this.chats.length; i++) {
        const conversation = await getConversations(this.chats[i].conversationId);
        console.log('会话', conversation);
        this.$set(this.chats[i], 'conversationName', conversation.data.conversationName);
        const messages = await listMessages({
          conversationId: this.chats[i].conversationId,
          orderByColumn: "sentAt",
          isAsc: "desc",
          pageNum: 1,
          pageSize: 1,
        });
        const messageContent = messages.rows[0] ? messages.rows[0].messageContent : '';
        console.log('消息', messageContent);
        this.$set(this.chats[i], 'messageContent', messageContent);
      }
      console.log('会话列表', this.chats);
    },
    handleChatClick(chat) {
      console.log(`Clicked on ${chat.name}`);
      this.$tab.navigateTo(`/pages/message/messages?conversation=${chat.conversationId}`);
    }
  }
};
</script>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  cursor: pointer;
  margin: 5px;
  background-color: rgb(228, 255, 240);
  border-radius: 6px;
  font: 15px black;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

h3 {
  margin: 0;
}

p {
  margin: 0;
  color: #888;
}
</style>