<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="帖子ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入帖子ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布帖子用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入发布帖子用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="帖子标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入帖子标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="帖子正文媒体" prop="media">
        <el-input
          v-model="queryParams.media"
          placeholder="请输入帖子正文媒体"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择发布时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="最后更新时间" prop="updatedAt">
        <el-date-picker clearable
          v-model="queryParams.updatedAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择最后更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="帖子分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入帖子分类"
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
          v-hasPermi="['posts:posts:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['posts:posts:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['posts:posts:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['posts:posts:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="id" />
      <el-table-column label="发布帖子用户ID" align="center" prop="userId" />
      <el-table-column label="帖子标题" align="center" prop="title" />
      <el-table-column label="帖子正文媒体" align="center" prop="media" />
      <el-table-column label="发布时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后更新时间" align="center" prop="updatedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="帖子状态" align="center" prop="status" />
      <el-table-column label="帖子分类" align="center" prop="category" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['posts:posts:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['posts:posts:remove']">删除</el-button>
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

    <!-- 添加或修改帖子信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="postsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发布帖子用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入发布帖子用户ID" />
        </el-form-item>
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="帖子正文媒体" prop="media">
          <el-input v-model="form.media" placeholder="请输入帖子正文媒体" />
        </el-form-item>
        <el-form-item label="发布时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择发布时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="最后更新时间" prop="updatedAt">
          <el-date-picker clearable
            v-model="form.updatedAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择最后更新时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="帖子分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入帖子分类" />
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

<script setup name="Posts">
import { listPosts, getPosts, delPosts, addPosts, updatePosts } from "@/api/posts/posts";

const { proxy } = getCurrentInstance();

const postsList = ref([]);
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
    title: null,
    media: null,
    createdAt: null,
    updatedAt: null,
    status: null,
    category: null
  },
  rules: {
    userId: [
      { required: true, message: "发布帖子用户ID不能为空", trigger: "blur" }
    ],
    title: [
      { required: true, message: "帖子标题不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询帖子信息列表 */
function getList() {
  loading.value = true;
  listPosts(queryParams.value).then(response => {
    postsList.value = response.rows;
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
    title: null,
    media: null,
    createdAt: null,
    updatedAt: null,
    status: null,
    category: null
  };
  proxy.resetForm("postsRef");
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
  title.value = "添加帖子信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getPosts(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改帖子信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["postsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updatePosts(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPosts(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除帖子信息编号为"' + _ids + '"的数据项？').then(function() {
    return delPosts(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('posts/posts/export', {
    ...queryParams.value
  }, `posts_${new Date().getTime()}.xlsx`)
}

getList();
</script>
