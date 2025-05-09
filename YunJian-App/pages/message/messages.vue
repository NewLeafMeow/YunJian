<template>
  <view class="chat-container">
    <!-- 消息列表 -->
    <view 
      ref="messageList" 
      class="message-list" 
      :style="{ height: windowHeight - inputHeight + 'px', marginBottom: inputHeight + 'px' }"
    >
      <!-- 对方消息（左对齐） -->
      <view 
        v-for="(msg, index) in messages" 
        :key="index" 
        class="message-item" 
        :class="msg.senderId != user.userId ? 'other-message' : 'my-message'"
      >
        <!-- 头像（对方在左，自己在右） -->
        <image 
          v-if="msg.senderId != user.userId" 
          class="avatar" 
          :src= "baseUrl+otheruser.avatar"
          mode="aspectFill"
        ></image>
        <image 
          v-else 
          class="avatar" 
          :src= "baseUrl+user.avatar"
          mode="aspectFill"
        ></image>

        <!-- 消息气泡 -->
        <view class="message-bubble">
          <text class="message-content">{{ msg.messageContent }}</text>
          <text class="message-time">{{ msg.time }}</text>
        </view>
      </view>
    </view>

    <!-- 输入区域 -->
    <view class="input-area" :style="{ height: inputHeight + 'px' }">
      <input 
        v-model="inputContent" 
        type="text" 
        placeholder="输入消息..." 
        class="input-box"
      />
      <button 
        @click="sendMessage" 
        class="send-btn"
        :disabled="inputContent.trim() === ''"
      >发送</button>
    </view>
  </view>
</template>

<script>
import config from '@/config'
import { getUserProfile, getUser } from '@/api/system/user'
// 导入添加会话和获取会话列表的 API 方法
import { listConversations } from '@/api/conversations/conversations'
// 导入添加参与者和获取参与者列表的 API 方法
import { listParticipants } from '@/api/participants/participants'
import { listMessages, addMessages } from '@/api/messages/messages'

export default {
  data() {
    return {
      baseUrl: config.baseUrl,
      user: null,
      otheruser: [],
      windowHeight: uni.getSystemInfoSync().windowHeight, // 屏幕高度
      inputHeight: 80, // 输入框高度
      inputContent: '', // 输入内容
      messages: [],
      conversation_id: this.$route.query.conversation,// 会话id
    };
  },
  mounted() {
    // 页面加载时滚动到底部
    this.scrollToBottom();
  },
  onLoad: async function() {
    await this.getuser();
    await this.getotheruser();
    await this.getlistMessages();
  },
  methods: {
    // 获取当前用户信息的异步方法
    async getuser() {
          // 调用 getUserProfile API 方法获取当前用户信息
          this.user = await getUserProfile();
          // 将用户信息中的 data 属性赋值给 user 数据属性
          this.user = this.user.data
          // 打印当前用户信息
          console.log('me',this.user);
    },

    async getotheruser(){
      let list1 = await listParticipants({
        conversationId: this.conversation_id,
      })
      console.log("会话参与者",list1)
      list1 = list1.rows
      for(let i=0,k=0;i<list1.length;i++){
        if(list1[i].userId != this.user.userId){
          this.otheruser[k] = await getUser(list1[i].userId);
          this.otheruser[k] = this.otheruser[k].data
          k++;
        }
      }
      console.log('对方',this.otheruser)
    },

    async getlistMessages() {
      console.log('getlistMessages方法')
      let list1 = await listMessages({
        conversationId: this.conversation_id,
      })
      this.messages = list1.rows
      console.log('mess:',this.messages);

      // 按 sentAt 属性降序排序
      this.messages.sort((a, b) => {
        console.log(a.sentAt,b.sentAt)
        return new Date(a.sentAt) - new Date(b.sentAt);
      });

      console.log('合并并排序后的列表:', this.messages);
    },
    // 发送消息
    async sendMessage() {
      console.log('会话id',this.conversation_id)
      console.log('发送的消息',this.inputContent)
      console.log('自己id',this.user.userId)
      await addMessages({
        conversationId: this.conversation_id,
        messageContent: this.inputContent,
        senderId: this.user.userId,
      });
      // 发送消息后重新获取消息列表
      await this.getlistMessages();
      // 清空输入框
      this.inputContent = '';
      // 滚动到底部
      this.scrollToBottom();
    },

    // 滚动到底部
    scrollToBottom() {
      setTimeout(() => {
        const messageList = this.$refs.messageList;
        if (messageList) {
          messageList.scrollTop = messageList.scrollHeight;
        }
      }, 100);
    }
  }
};
</script>

<style scoped>
.chat-container {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.message-list {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
  background-color: #f5f5f5;
}

.message-item {
  display: flex;
  align-items: flex-start;
  margin: 10px 0;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 15px;
}

/* 对方消息样式（左对齐） */
.other-message .message-bubble {
  background-color: #ffffff;
  border-radius: 20px;
  padding: 12px 18px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 自己消息样式（右对齐） */
.my-message {
  flex-direction: row-reverse;
}

.my-message .message-bubble {
  background-color: #007bff;
  color: white;
  border-radius: 20px;
  padding: 12px 18px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.message-bubble {
  max-width: 70%;
  word-wrap: break-word;
}

.message-content {
  font-size: 16px;
  line-height: 1.5;
}

.message-time {
  display: block;
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}

.input-area {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  padding: 15px 20px;
  background-color: white;
  border-top: 1px solid #f5f5f5;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}

.input-box {
  flex: 1;
  height: 45px;
  padding: 0 15px;
  border: 1px solid #e0e0e0;
  border-radius: 25px;
  font-size: 16px;
}

.send-btn {
  margin-left: 10px;
  padding: 0 20px;
  height: 45px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
}
</style>        