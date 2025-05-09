<template>
  <!-- 新建帖子的视图 -->
  <view class="new-post">
    <!-- 标题输入框 -->
    <input
      v-model="title"
      placeholder="请输入标题"
      class="post-title-input"
    />

    <!-- 富文本输入框使用 view -->
    <view
      ref="contentBox"
      class="post-content-input"
      contenteditable="true"
      @input="updateContentWithEvent"
      @click="saveCursorPosition"
      @keyup="saveCursorPosition"
    ></view>

    <!-- 图片上传区域 -->
    <view class="image-upload">
      <button @click="chooseImage">上传图片</button>
    </view>

    <!-- 发布按钮 -->
    <view class="submit">
      <button @click="publishPost">发布</button>
    </view>
  </view>
</template>

<script>
// 引入添加帖子的 API
import { addPosts } from "@/api/posts/posts";
// 引入获取用户信息的 API
import { getUserProfile } from "@/api/system/user";
// 引入配置文件
import config from "@/config";

export default {
  data() {
    return {
      title: "", // 帖子标题
      content: "", // 存储富文本 HTML 内容
      cursorPosition: null, // 记录光标位置，用于插入图片
      userId: null, // 用户ID
      selectedImage: "", // 保存本地选择的图片路径
      selectedImageList: [], // 存储所有本地图片路径
    };
  },
  mounted() {
    // 组件挂载后获取用户信息
    this.getUser();
  },
  computed: {
    // 从 Vuex 中获取用户名
    name() {
      return this.$store.state.user.name;
    },
    // 从 Vuex 中获取用户 token
    token() {
      return this.$store.state.user.token;
    }
  },
  methods: {
    // 选择图片，并插入到光标位置，保存本地路径，等待发布时上传
    chooseImage() {
      // 调用 uni.chooseImage 选择图片
      uni.chooseImage({
        count: 1, // 一次只能选择一张图片
        success: (res) => {
          // 获取选择图片的本地路径
          const localPath = res.tempFilePaths[0];
          console.log("选取图片：", localPath);

          // 将本地路径添加到 selectedImageList 数组中
          this.selectedImageList.push(localPath);

          // 构建插入到富文本中的图片标签
          const imgTag = `<img src="${localPath}" style="max-width:100%;" />`;
          // 在光标位置插入图片标签
          this.insertAtCursor(imgTag);
        },
        fail: (err) => {
          // 选择图片失败时打印错误信息
          console.error("选择图片失败", err);
        }
      });
    },

    // 记录光标位置
    saveCursorPosition() {
      // 获取当前的选择范围
      const selection = window.getSelection();
      if (selection && selection.rangeCount > 0) {
        // 记录当前的选择范围
        this.cursorPosition = selection.getRangeAt(0);
      }
    },

    // 在光标处插入 HTML 代码（参考原先代码）
    insertAtCursor(html) {
      // 获取富文本输入框的引用
      const contentBox = this.$refs.contentBox;
      if (this.cursorPosition) {
        // 如果记录了光标位置
        const range = this.cursorPosition;
        const div = document.createElement("div");
        div.innerHTML = html;
        const frag = document.createDocumentFragment();
        let node;
        while ((node = div.firstChild)) {
          frag.appendChild(node);
        }
        // 删除选中的内容
        range.deleteContents(); 
        // 在光标位置插入 HTML 内容
        range.insertNode(frag);

        // 检查插入的最后一个节点是否存在
        if (frag.lastChild) {
          // 重新设置光标位置
          range.setStartAfter(frag.lastChild);
          range.setEndAfter(frag.lastChild);
          const selection = window.getSelection();
          selection.removeAllRanges();
          selection.addRange(range);
        } else {
          // 如果没有节点，则尝试调用 focus（仅当存在 focus 方法时）
          if (contentBox.focus && typeof contentBox.focus === 'function') {
            contentBox.focus();
          }
        }
      } else {
        // 如果光标位置未记录，则追加到末尾
        // 判断是否需要通过 $el 获取真实 DOM
        if (contentBox.$el) {
          contentBox.$el.innerHTML += html;
        } else {
          contentBox.innerHTML += html;
        }
      }
      // 更新 content 数据
      this.updateContent(); 
    },

    // 通过事件参数直接获取最新内容，
    // 如果模块已封装为组件，则使用 this.$refs.contentBox.$el.innerHTML
    updateContentWithEvent(e) {
      const contentBox = this.$refs.contentBox;
      if (contentBox.$el) {
        // 获取富文本输入框的 HTML 内容
        this.content = contentBox.$el.innerHTML;
      } else {
        this.content = e.target.innerHTML;
      }
      console.log("更新内容:", this.content);
    },

    // 主动更新内容的方法，兼容不同情况
    updateContent() {
      const contentBox = this.$refs.contentBox;
      if (contentBox) {
        if (contentBox.$el && contentBox.$el.innerHTML) {
          // 获取富文本输入框的 HTML 内容
          this.content = contentBox.$el.innerHTML;
        } else {
          this.content = contentBox.innerHTML;
        }
        console.log("更新内容:", this.content);
      }
    },

    // 获取用户信息
    getUser() {
      // 调用 getUserProfile API 获取用户信息
      getUserProfile()
        .then(response => {
          // 记录用户 ID
          this.userId = response.data.userId;
          console.log("用户信息：", response);
          console.log("用户ID：", this.userId);
        })
        .catch(err => {
          // 获取用户信息失败时打印错误信息
          console.error("获取用户信息失败：", err);
        });
    },

    // 发布帖子：更新富文本内容，上传图片（如有），再调用接口发布帖子
    async publishPost() {
      // 更新富文本内容
      this.updateContent();

      // 判断标题与内容是否为空
      if (!this.title.trim() || !this.content.trim()) {
        uni.showToast({ title: "标题和内容不能为空", icon: "none" });
        return;
      }

      let htmlContent = this.content;
      const uploadMap = new Map(); // localPath => serverUrl

      // 遍历本地图片路径数组，上传图片
      for (let localPath of this.selectedImageList) {
        try {
          const uploadUrl = config.baseUrl + `/common/upload`;
          const res = await new Promise((resolve, reject) => {
            // 调用 uni.uploadFile 上传图片
            uni.uploadFile({
              url: uploadUrl,
              filePath: localPath,
              name: "file",
              header: {
                Authorization: this.token
              },
              success: (res) => {
                const result = JSON.parse(res.data);
                if (result.code === 200 && result.url) {
                  // 上传成功，返回服务器图片路径
                  resolve(result.url);
                } else {
                  // 上传失败或格式异常
                  reject("上传失败或格式异常");
                }
              },
              fail: (err) => reject(err)
            });
          });
          // 将本地路径和服务器路径存入 Map
          uploadMap.set(localPath, res);
        } catch (e) {
          // 上传失败时打印错误信息
          console.error("上传失败：", localPath, e);
          uni.showToast({ title: "部分图片上传失败", icon: "none" });
          return;
        }
      }

      // 替换富文本中的 src
      uploadMap.forEach((serverUrl, localPath) => {
        htmlContent = htmlContent.replace(
          new RegExp(`src=["']${localPath}["']`, 'g'),
          `src="${serverUrl}"`
        );
      });

      // 构造请求体
      const postData = {
        userId: this.userId,
        title: this.title,
        media: htmlContent
      };

      try {
        // 调用 addPosts API 发布帖子
        const response = await addPosts(postData);
        uni.showToast({ title: "发布成功", icon: "success" });
        console.log("发布成功", response);
        // 可以清空表单、跳转页面等
        this.$router.push({ path: '/pages/index' });
      } catch (err) {
        // 发布失败时打印错误信息
        console.error("发布失败：", err);
        uni.showToast({ title: "发布失败", icon: "none" });
      }
    }
  }
};
</script>

<style scoped>
.new-post {
  padding: 20rpx;
}
.post-title-input {
  width: 100%;
  height: 80rpx;
  border: 1px solid #ddd;
  padding: 10rpx;
  font-size: 30rpx;
  margin-bottom: 20rpx;
}
.post-content-input {
  width: 100%;
  min-height: 300rpx;
  border: 1px solid #ddd;
  padding: 10rpx;
  font-size: 30rpx;
  margin-bottom: 20rpx;
  background-color: #fff;
  white-space: pre-wrap;
}
.image-upload {
  margin-bottom: 20rpx;
}
.submit {
  margin-top: 20rpx;
  text-align: center;
}
</style>