<template>
  <div class="home-page">
    <el-row :gutter="20">
      <!-- 左侧饼状图区域 -->
      <el-col :span="12">
        <el-card @mouseenter="handlePieMouseEnter" @mouseleave="handlePieMouseLeave">
          <template #header>
            <span>用户统计</span>
          </template>
          <div class="chart-container">
            <canvas ref="pieChart" width="300" height="300"></canvas>
            <div class="legend">
              <div>
                <span class="legend-color" style="background-color: #36A2EB;"></span>
                <span>总用户</span>
              </div>
              <div>
                <span class="legend-color" style="background-color: #FF6384;"></span>
                <span>今日登录用户</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 右侧线性统计图区域 -->
      <el-col :span="12">
        <el-card @mouseenter="handleLineMouseEnter" @mouseleave="handleLineMouseLeave">
          <template #header>
            <span>最近一月每日访问量</span>
          </template>
          <canvas ref="lineChart" width="380" height="350"></canvas>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ElRow, ElCol, ElCard } from 'element-plus';
import { list } from '@/api/monitor/logininfor';
import { listUser } from '@/api/system/user'

export default {
  name: 'HomePage',
  components: {
    ElRow,
    ElCol,
    ElCard
  },
  data() {
    return {
      // 模拟总用户和今日登录用户数据
      userStats: {
        totalUsers: 5000,
        todayLoggedInUsers: 800
      },
      // 模拟最近一月每日访问量数据
      dailyVisits: {
        labels: Array.from({ length: 30 }, (_, i) => `day${i + 1}`),
        data: Array.from({ length: 30 }, () => Math.floor(Math.random() * (2000 - 500 + 1)) + 500)
      },
      // 存储线性图 tooltip 的相关信息，用于显示数据点的具体数值
      lineTooltip: {
        visible: false,
        x: 0,
        y: 0,
        value: 0
      },
      // 存储饼状图 tooltip 的相关信息，用于显示扇形区域的具体数据
      pieTooltip: {
        visible: false,
        x: 0,
        y: 0,
        value: 0,
        label: ''
      },
      // 存储当前鼠标悬停的扇形索引，-1 表示没有悬停
      hoveredPieIndex: -1,
      // 存储当前鼠标悬停的数据点索引，-1 表示没有悬停
      hoveredLineIndex: -1
    };
  },
  created: async function () {
    await this.todayuserlist();
    await this.todaylistUser();
  },
  mounted() {
    // 页面加载完成后，绘制饼状图和线性统计图
    this.drawPieChart();
    this.drawLineChart();
    // 为线性统计图的 canvas 元素添加鼠标移动事件监听器
    this.$refs.lineChart.addEventListener('mousemove', this.handleLineMouseMove);
    // 为线性统计图的 canvas 元素添加鼠标移出事件监听器
    this.$refs.lineChart.addEventListener('mouseout', this.handleLineMouseOut);
    // 为饼状图的 canvas 元素添加鼠标移动事件监听器
    this.$refs.pieChart.addEventListener('mousemove', this.handlePieMouseMove);
    // 为饼状图的 canvas 元素添加鼠标移出事件监听器
    this.$refs.pieChart.addEventListener('mouseout', this.handlePieMouseOut);
  },
  beforeDestroy() {
    // 在组件销毁前，移除线性图鼠标移动和鼠标移出事件监听器，防止内存泄漏
    this.$refs.lineChart.removeEventListener('mousemove', this.handleLineMouseMove);
    this.$refs.lineChart.removeEventListener('mouseout', this.handleLineMouseOut);
    // 在组件销毁前，移除饼状图鼠标移动和鼠标移出事件监听器，防止内存泄漏
    this.$refs.pieChart.removeEventListener('mousemove', this.handlePieMouseMove);
    this.$refs.pieChart.removeEventListener('mouseout', this.handlePieMouseOut);
  },
  methods: {
    async todayuserlist(){
      const a = await list({
        params: {beginTime: this.getCurrentDate()+' 00:00:00', endTime: this.getCurrentDate()+' 23:59:59'},
      });
      const userNames = a.rows.map(row => row.userName); // 提取所有 userName
      const uniqueNames = new Set(userNames); // 利用 Set 去重
      this.userStats.todayLoggedInUsers = uniqueNames.size; // 不重复项数
      console.log('今日用户',this.userStats.todayLoggedInUsers);
      const b = [];
      for (const item of this.getCurrentMonthDateStrings()) {
        const a1 = await list({
          params: {beginTime: item + ' 00:00:00', endTime: item + ' 23:59:59'},
        });
        const userNames1 = a1.rows.map(row => row.userName); // 提取所有 userName
        const uniqueNames1 = new Set(userNames1); // 利用 Set 去重
        b.push(uniqueNames1.size); // 不重复项数
        console.log(item,b);
      };
      this.dailyVisits.data = b;
      console.log(this.dailyVisits.data);
    },

    async todaylistUser(){
      const a = await listUser({
      });
      this.userStats.totalUsers = a.rows.length;
      console.log('总用户',this.userStats.totalUsers);
    },

    getCurrentDate() {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    },

    getCurrentMonthDateStrings() {
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth();
        const lastDay = new Date(year, month + 1, 0).getDate();

        const dateStrings = [];
        for (let day = 1; day <= lastDay; day++) {
            const currentDate = new Date(year, month, day);
            const yearStr = currentDate.getFullYear();
            const monthStr = String(currentDate.getMonth() + 1).padStart(2, '0');
            const dayStr = String(currentDate.getDate()).padStart(2, '0');
            const formattedDate = `${yearStr}-${monthStr}-${dayStr}`;
            dateStrings.push(formattedDate);
        }
        return dateStrings;
    },

    drawPieChart() {
      // 获取饼状图 canvas 的 2D 绘图上下文
      const ctx = this.$refs.pieChart.getContext('2d');
      // 计算总用户数
      const total = this.userStats.totalUsers + this.userStats.todayLoggedInUsers;
      // 一个完整圆的弧度
      const totalAngle = 2 * Math.PI;
      // 饼状图的圆心 x 坐标
      const centerX = this.$refs.pieChart.width / 2;
      // 饼状图的圆心 y 坐标
      const centerY = this.$refs.pieChart.height / 2;
      // 饼状图的半径
      const radius = Math.min(centerX, centerY) - 10;

      // 清空画布
      ctx.clearRect(0, 0, this.$refs.pieChart.width, this.$refs.pieChart.height);

      // 计算总用户所占比例
      const totalUserRatio = this.userStats.totalUsers / total;
      // 计算总用户部分对应的弧度
      const totalUserAngle = totalUserRatio * totalAngle;
      // 绘制总用户部分
      this.drawPieSlice(ctx, centerX, centerY, radius, 0, totalUserAngle, '#36A2EB', 0);
      // 绘制今日登录用户部分
      this.drawPieSlice(ctx, centerX, centerY, radius, totalUserAngle, totalAngle, '#FF6384', 1);

      // 绘制饼状图 tooltip
      if (this.pieTooltip.visible) {
        ctx.font = '12px Arial';
        ctx.fillStyle = 'black';
        ctx.fillText(`${this.pieTooltip.label}: ${this.pieTooltip.value}`, this.pieTooltip.x - 30, this.pieTooltip.y - 10);
      }
    },
    drawPieSlice(ctx, centerX, centerY, radius, startAngle, endAngle, color, index) {
      // 如果当前扇形被悬停，放大该扇形
      let scale = 1;
      if (index === this.hoveredPieIndex) {
        scale = 1.1;
      }
      const scaledRadius = radius * scale;
      // 开始绘制路径
      ctx.beginPath();
      // 移动画笔到圆心
      ctx.moveTo(centerX, centerY);
      // 绘制圆弧
      ctx.arc(centerX, centerY, scaledRadius, startAngle, endAngle);
      // 闭合路径
      ctx.closePath();
      // 设置填充颜色
      ctx.fillStyle = color;
      // 填充路径
      ctx.fill();
    },
    drawLineChart() {
      // 获取线性统计图 canvas 的 2D 绘图上下文
      const ctx = this.$refs.lineChart.getContext('2d');
      // 线性统计图 canvas 的宽度
      const width = this.$refs.lineChart.width;
      // 线性统计图 canvas 的高度
      const height = this.$refs.lineChart.height;
      // 图表的内边距
      const padding = 40;
      // 每个数据点在 x 轴上的间距
      const xStep = (width - 2 * padding) / (this.dailyVisits.labels.length - 1);
      // 数据中的最大值
      const maxValue = Math.max(...this.dailyVisits.data);
      // 数据中的最小值
      const minValue = Math.min(...this.dailyVisits.data);
      // y 轴的缩放比例
      const yScale = (height - 2 * padding) / (maxValue - minValue);

      // 清空画布
      ctx.clearRect(0, 0, width, height);

      // 绘制坐标轴
      ctx.beginPath();
      // 移动画笔到坐标轴起点
      ctx.moveTo(padding, padding);
      // 绘制 y 轴
      ctx.lineTo(padding, height - padding);
      // 绘制 x 轴
      ctx.lineTo(width - padding, height - padding);
      // 设置线条颜色
      ctx.strokeStyle = 'black';
      // 绘制线条
      ctx.stroke();

      // 绘制 y 轴刻度
      const numTicks = 5; // 刻度数量
      const tickStep = (maxValue - minValue) / (numTicks - 1);
      for (let i = 0; i < numTicks; i++) {
        const tickValue = minValue + i * tickStep;
        const tickY = height - padding - (tickValue - minValue) * yScale;
        ctx.beginPath();
        ctx.moveTo(padding - 5, tickY);
        ctx.lineTo(padding, tickY);
        ctx.stroke();
        ctx.font = '12px Arial';
        ctx.fillStyle = 'black';
        ctx.textAlign = 'right';
        ctx.fillText(tickValue.toFixed(1), padding - 10, tickY + 5);
      }

      // 绘制折线
      ctx.beginPath();
      // 移动画笔到第一个数据点
      ctx.moveTo(padding, height - padding - (this.dailyVisits.data[0] - minValue) * yScale);
      for (let i = 1; i < this.dailyVisits.data.length; i++) {
        // 计算当前数据点的 x 坐标
        const x = padding + i * xStep;
        // 计算当前数据点的 y 坐标
        const y = height - padding - (this.dailyVisits.data[i] - minValue) * yScale;
        // 绘制到当前数据点的线条
        ctx.lineTo(x, y);
      }
      // 设置折线颜色
      ctx.strokeStyle = '#36A2EB';
      // 设置折线宽度
      ctx.lineWidth = 2;
      // 绘制折线
      ctx.stroke();

      // 绘制数据点
      for (let i = 0; i < this.dailyVisits.data.length; i++) {
        // 计算当前数据点的 x 坐标
        const x = padding + i * xStep;
        // 计算当前数据点的 y 坐标
        const y = height - padding - (this.dailyVisits.data[i] - minValue) * yScale;
        // 开始绘制路径
        ctx.beginPath();
        // 绘制数据点（圆形）
        let pointRadius = 5;
        if (i === this.hoveredLineIndex) {
          pointRadius = 8;
        }
        ctx.arc(x, y, pointRadius, 0, 2 * Math.PI);
        // 设置填充颜色
        ctx.fillStyle = '#36A2EB';
        // 填充数据点
        ctx.fill();
      }

      // 动态计算 x 轴标签显示间隔
      const labelInterval = Math.max(1, Math.floor(this.dailyVisits.labels.length / ((width - 2 * padding) / 40)));

      // 绘制 x 轴标签
      ctx.font = '12px Arial';
      for (let i = 0; i < this.dailyVisits.labels.length; i += labelInterval) {
        // 计算当前标签的 x 坐标
        const x = padding + i * xStep;
        // 绘制 x 轴标签
        ctx.fillText(this.dailyVisits.labels[i], x + 15, height - padding + 20);
      }

      // 绘制 y 轴标签
      ctx.save();
      // 平移画布原点
      ctx.translate(10, height / 2);
      // 旋转画布
      ctx.rotate(-Math.PI / 2);
      // 设置字体样式
      ctx.font = '14px Arial';
      // 设置填充颜色
      ctx.fillStyle = 'black';
      // 设置文本对齐方式
      ctx.textAlign = 'center';
      // 绘制 y 轴标签
      ctx.fillText('访问量', 0, 0);
      ctx.restore();

      // 绘制线性图 tooltip
      if (this.lineTooltip.visible) {
        ctx.font = '12px Arial';
        ctx.fillStyle = 'black';
        ctx.fillText(this.lineTooltip.value, this.lineTooltip.x, this.lineTooltip.y - 10);
      }
    },
    handleLineMouseMove(event) {
      // 获取线性统计图 canvas 的 2D 绘图上下文
      const ctx = this.$refs.lineChart.getContext('2d');
      // 线性统计图 canvas 的宽度
      const width = this.$refs.lineChart.width;
      // 线性统计图 canvas 的高度
      const height = this.$refs.lineChart.height;
      // 图表的内边距
      const padding = 40;
      // 每个数据点在 x 轴上的间距
      const xStep = (width - 2 * padding) / (this.dailyVisits.labels.length - 1);
      // 数据中的最大值
      const maxValue = Math.max(...this.dailyVisits.data);
      // 数据中的最小值
      const minValue = Math.min(...this.dailyVisits.data);
      // y 轴的缩放比例
      const yScale = (height - 2 * padding) / (maxValue - minValue);

      // 获取 canvas 在页面中的位置信息
      const rect = this.$refs.lineChart.getBoundingClientRect();
      // 计算鼠标相对于 canvas 的 x 坐标
      const mouseX = event.clientX - rect.left;
      // 计算鼠标相对于 canvas 的 y 坐标
      const mouseY = event.clientY - rect.top;

      this.hoveredLineIndex = -1;

      for (let i = 0; i < this.dailyVisits.data.length; i++) {
        // 计算当前数据点的 x 坐标
        const x = padding + i * xStep;
        // 计算当前数据点的 y 坐标
        const y = height - padding - (this.dailyVisits.data[i] - minValue) * yScale;
        // 计算鼠标与当前数据点的距离
        const distance = Math.sqrt((mouseX - x) * (mouseX - x) + (mouseY - y) * (mouseY - y));
        if (distance < 10) {
          // 如果距离小于 10 像素，显示 tooltip
          this.lineTooltip.visible = true;
          // 设置 tooltip 的 x 坐标
          this.lineTooltip.x = x;
          // 设置 tooltip 的 y 坐标
          this.lineTooltip.y = y;
          // 设置 tooltip 显示的具体数值
          this.lineTooltip.value = this.dailyVisits.data[i];
          this.hoveredLineIndex = i;
          // 重新绘制线性统计图以显示 tooltip
          this.drawLineChart();
          return;
        }
      }
      // 如果鼠标不在任何数据点附近，隐藏 tooltip
      this.handleLineMouseOut();
    },
    handleLineMouseOut() {
      // 当鼠标移出线性图 canvas 时，将 tooltip 的可见性设置为 false，即关闭显示
      this.lineTooltip.visible = false;
      this.hoveredLineIndex = -1;
      // 重新绘制线性统计图以移除 tooltip
      this.drawLineChart();
    },
    handlePieMouseMove(event) {
      // 获取饼状图 canvas 的 2D 绘图上下文
      const ctx = this.$refs.pieChart.getContext('2d');
      // 计算总用户数
      const total = this.userStats.totalUsers + this.userStats.todayLoggedInUsers;
      // 一个完整圆的弧度
      const totalAngle = 2 * Math.PI;
      // 饼状图的圆心 x 坐标
      const centerX = this.$refs.pieChart.width / 2;
      // 饼状图的圆心 y 坐标
      const centerY = this.$refs.pieChart.height / 2;
      // 饼状图的半径
      const radius = Math.min(centerX, centerY) - 10;

      // 获取 canvas 在页面中的位置信息
      const rect = this.$refs.pieChart.getBoundingClientRect();
      // 计算鼠标相对于 canvas 的 x 坐标
      const mouseX = event.clientX - rect.left;
      // 计算鼠标相对于 canvas 的 y 坐标
      const mouseY = event.clientY - rect.top;

      // 计算鼠标到圆心的距离
      const distanceToCenter = Math.sqrt((mouseX - centerX) * (mouseX - centerX) + (mouseY - centerY) * (mouseY - centerY));

      // 若鼠标不在饼图范围内，隐藏 tooltip
      if (distanceToCenter > radius) {
        this.handlePieMouseOut();
        return;
      }

      this.hoveredPieIndex = -1;

      // 计算鼠标相对于圆心的角度
      let angle = Math.atan2(mouseY - centerY, mouseX - centerX);
      if (angle < 0) {
        angle += 2 * Math.PI;
      }

      // 计算总用户所占比例
      const totalUserRatio = this.userStats.totalUsers / total;
      // 计算总用户部分对应的弧度
      const totalUserAngle = totalUserRatio * totalAngle;

      // 判断鼠标是否在总用户扇形区域内
      if (angle >= 0 && angle < totalUserAngle) {
        this.pieTooltip.visible = true;
        this.pieTooltip.x = mouseX;
        this.pieTooltip.y = mouseY;
        this.pieTooltip.value = this.userStats.totalUsers;
        this.pieTooltip.label = '总用户';
        this.hoveredPieIndex = 0;
      } 
      // 判断鼠标是否在今日登录用户扇形区域内
      else if (angle >= totalUserAngle && angle < totalAngle) {
        this.pieTooltip.visible = true;
        this.pieTooltip.x = mouseX;
        this.pieTooltip.y = mouseY;
        this.pieTooltip.value = this.userStats.todayLoggedInUsers;
        this.pieTooltip.label = '今日登录用户';
        this.hoveredPieIndex = 1;
      } 
      // 鼠标不在任何扇形区域内
      else {
        this.handlePieMouseOut();
      }

      // 重新绘制饼状图以显示 tooltip
      this.drawPieChart();
    },
    handlePieMouseOut() {
      // 当鼠标移出饼状图 canvas 时，将 tooltip 的可见性设置为 false，即关闭显示
      this.pieTooltip.visible = false;
      this.hoveredPieIndex = -1;
      // 重新绘制饼状图以移除 tooltip
      this.drawPieChart();
    },
    handlePieMouseEnter(event) {
      event.currentTarget.style.border = '2px solid #36A2EB';
    },
    handlePieMouseLeave(event) {
      event.currentTarget.style.border = '';
    },
    handleLineMouseEnter(event) {
      event.currentTarget.style.border = '2px solid #36A2EB';
    },
    handleLineMouseLeave(event) {
      event.currentTarget.style.border = '';
    }
  }
};
</script>

<style scoped lang="scss">
.home-page {
  padding: 20px;
}

.chart-container {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.legend {
  display: flex;
  flex-direction: column;
}

.legend-color {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 5px;
}
</style>    