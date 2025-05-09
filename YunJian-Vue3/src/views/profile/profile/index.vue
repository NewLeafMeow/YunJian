<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="唯一标识" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="唯一标识"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户 ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="用户 ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['profile:profile:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['profile:profile:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['profile:profile:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['profile:profile:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="profileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="用户 ID" align="center" prop="userId" />
      <el-table-column label="点击财经类的次数" align="center" prop="financeClickCount" />
      <el-table-column label="点赞财经类的次数" align="center" prop="financeLikeCount" />
      <el-table-column label="点击时政类的次数" align="center" prop="politicsClickCount" />
      <el-table-column label="点赞时政类的次数" align="center" prop="politicsLikeCount" />
      <el-table-column label="点击房产类的次数" align="center" prop="realEstateClickCount" />
      <el-table-column label="点赞房产类的次数" align="center" prop="realEstateLikeCount" />
      <el-table-column label="点击家居类的次数" align="center" prop="homeClickCount" />
      <el-table-column label="点赞家居类的次数" align="center" prop="homeLikeCount" />
      <el-table-column label="点击科技类的次数" align="center" prop="technologyClickCount" />
      <el-table-column label="点赞科技类的次数" align="center" prop="technologyLikeCount" />
      <el-table-column label="点击体育类的次数" align="center" prop="sportsClickCount" />
      <el-table-column label="点赞体育类的次数" align="center" prop="sportsLikeCount" />
      <el-table-column label="点击游戏类的次数" align="center" prop="gamingClickCount" />
      <el-table-column label="点赞游戏类的次数" align="center" prop="gamingLikeCount" />
      <el-table-column label="点击时尚类的次数" align="center" prop="fashionClickCount" />
      <el-table-column label="点赞时尚类的次数" align="center" prop="fashionLikeCount" />
      <el-table-column label="点击教育类的次数" align="center" prop="educationClickCount" />
      <el-table-column label="点赞教育类的次数" align="center" prop="educationLikeCount" />
      <el-table-column label="点击娱乐类的次数" align="center" prop="entertainmentClickCount" />
      <el-table-column label="点赞娱乐类的次数" align="center" prop="entertainmentLikeCount" />
      <el-table-column label="搜索记录类型" align="center" prop="searchRecordType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['profile:profile:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['profile:profile:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户画像对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="profileRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联 sys_user 表的用户 ID，用于标识用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入关联 sys_user 表的用户 ID，用于标识用户" />
        </el-form-item>
        <el-form-item label="用户点击财经类帖子的次数" prop="financeClickCount">
          <el-input v-model="form.financeClickCount" placeholder="请输入用户点击财经类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞财经类帖子的次数" prop="financeLikeCount">
          <el-input v-model="form.financeLikeCount" placeholder="请输入用户点赞财经类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击时政类帖子的次数" prop="politicsClickCount">
          <el-input v-model="form.politicsClickCount" placeholder="请输入用户点击时政类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞时政类帖子的次数" prop="politicsLikeCount">
          <el-input v-model="form.politicsLikeCount" placeholder="请输入用户点赞时政类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击房产类帖子的次数" prop="realEstateClickCount">
          <el-input v-model="form.realEstateClickCount" placeholder="请输入用户点击房产类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞房产类帖子的次数" prop="realEstateLikeCount">
          <el-input v-model="form.realEstateLikeCount" placeholder="请输入用户点赞房产类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击家居类帖子的次数" prop="homeClickCount">
          <el-input v-model="form.homeClickCount" placeholder="请输入用户点击家居类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞家居类帖子的次数" prop="homeLikeCount">
          <el-input v-model="form.homeLikeCount" placeholder="请输入用户点赞家居类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击科技类帖子的次数" prop="technologyClickCount">
          <el-input v-model="form.technologyClickCount" placeholder="请输入用户点击科技类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞科技类帖子的次数" prop="technologyLikeCount">
          <el-input v-model="form.technologyLikeCount" placeholder="请输入用户点赞科技类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击体育类帖子的次数" prop="sportsClickCount">
          <el-input v-model="form.sportsClickCount" placeholder="请输入用户点击体育类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞体育类帖子的次数" prop="sportsLikeCount">
          <el-input v-model="form.sportsLikeCount" placeholder="请输入用户点赞体育类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击游戏类帖子的次数" prop="gamingClickCount">
          <el-input v-model="form.gamingClickCount" placeholder="请输入用户点击游戏类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞游戏类帖子的次数" prop="gamingLikeCount">
          <el-input v-model="form.gamingLikeCount" placeholder="请输入用户点赞游戏类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击时尚类帖子的次数" prop="fashionClickCount">
          <el-input v-model="form.fashionClickCount" placeholder="请输入用户点击时尚类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞时尚类帖子的次数" prop="fashionLikeCount">
          <el-input v-model="form.fashionLikeCount" placeholder="请输入用户点赞时尚类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击教育类帖子的次数" prop="educationClickCount">
          <el-input v-model="form.educationClickCount" placeholder="请输入用户点击教育类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞教育类帖子的次数" prop="educationLikeCount">
          <el-input v-model="form.educationLikeCount" placeholder="请输入用户点赞教育类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点击娱乐类帖子的次数" prop="entertainmentClickCount">
          <el-input v-model="form.entertainmentClickCount" placeholder="请输入用户点击娱乐类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户点赞娱乐类帖子的次数" prop="entertainmentLikeCount">
          <el-input v-model="form.entertainmentLikeCount" placeholder="请输入用户点赞娱乐类帖子的次数" />
        </el-form-item>
        <el-form-item label="用户的搜索记录类型" prop="searchRecordType">
          <el-input v-model="form.searchRecordType" placeholder="请输入用户的搜索记录类型" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Profile">
import { listProfile, getProfile, delProfile, addProfile, updateProfile } from "@/api/profile/profile";

const { proxy } = getCurrentInstance();

const profileList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    userId: null,
    financeClickCount: null,
    financeLikeCount: null,
    politicsClickCount: null,
    politicsLikeCount: null,
    realEstateClickCount: null,
    realEstateLikeCount: null,
    homeClickCount: null,
    homeLikeCount: null,
    technologyClickCount: null,
    technologyLikeCount: null,
    sportsClickCount: null,
    sportsLikeCount: null,
    gamingClickCount: null,
    gamingLikeCount: null,
    fashionClickCount: null,
    fashionLikeCount: null,
    educationClickCount: null,
    educationLikeCount: null,
    entertainmentClickCount: null,
    entertainmentLikeCount: null,
    searchRecordType: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询用户画像列表 */
function getList() {
  loading.value = true;
  listProfile(queryParams.value).then(response => {
    profileList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    userId: null,
    financeClickCount: null,
    financeLikeCount: null,
    politicsClickCount: null,
    politicsLikeCount: null,
    realEstateClickCount: null,
    realEstateLikeCount: null,
    homeClickCount: null,
    homeLikeCount: null,
    technologyClickCount: null,
    technologyLikeCount: null,
    sportsClickCount: null,
    sportsLikeCount: null,
    gamingClickCount: null,
    gamingLikeCount: null,
    fashionClickCount: null,
    fashionLikeCount: null,
    educationClickCount: null,
    educationLikeCount: null,
    entertainmentClickCount: null,
    entertainmentLikeCount: null,
    searchRecordType: null
  };
  proxy.resetForm("profileRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户画像";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getProfile(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户画像";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["profileRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateProfile(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addProfile(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除用户画像编号为"' + _ids + '"的数据项？').then(function() {
    return delProfile(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('profile/profile/export', {
    ...queryParams.value
  }, `profile_${new Date().getTime()}.xlsx`)
}

getList();
</script>
<style scoped>
/* 表单容器样式 */
.app-container {
  padding: 1rem;
}

/* 查询表单样式 */
.el-form {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.el-form-item {
  flex: 1 1 auto;
  min-width: 150px;
}

.el-form-item.el-input {
  width: 100%;
}

/* 按钮行样式 */
.el-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.el-col {
  flex: 1 1 auto;
  min-width: 100px;
}

.el-button {
  width: 100%;
}

@media (min-width: 768px) {
  .el-form-item,
  .el-col {
    flex: none;
  }

  .el-button {
    width: auto;
  }
}

/* 表格样式 */
.el-table {
  width: 100%;
}

/* 分页样式 */
.pagination {
  margin-top: 1rem;
}

/* 对话框样式 */
.el-dialog {
  width: 100%;
}

@media (min-width: 768px) {
  .el-dialog {
    width: 50%;
  }
}

/* 对话框表单样式 */
.el-dialog .el-form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.el-dialog .el-form-item {
  flex: 1;
}

.el-dialog .el-input {
  width: 100%;
}

/* 对话框底部按钮样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}
</style>    