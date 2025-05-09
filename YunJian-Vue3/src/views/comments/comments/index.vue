<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="评论ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入评论ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论所属帖子ID" prop="postId">
        <el-input
          v-model="queryParams.postId"
          placeholder="请输入评论所属帖子ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入评论用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入评论内容"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父评论ID，NULL表示直接评论" prop="parentCommentId">
        <el-input
          v-model="queryParams.parentCommentId"
          placeholder="请输入父评论ID，NULL表示直接评论"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择评论时间">
        </el-date-picker>
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
          v-hasPermi="['comments:comments:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['comments:comments:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['comments:comments:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['comments:comments:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commentsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评论ID" align="center" prop="id" />
      <el-table-column label="评论所属帖子ID" align="center" prop="postId" />
      <el-table-column label="评论用户ID" align="center" prop="userId" />
      <el-table-column label="评论内容" align="center" prop="content" />
      <el-table-column label="父评论ID，NULL表示直接评论" align="center" prop="parentCommentId" />
      <el-table-column label="评论时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['comments:comments:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['comments:comments:remove']">删除</el-button>
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

    <!-- 添加或修改评论对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="commentsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="评论所属帖子ID" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入评论所属帖子ID" />
        </el-form-item>
        <el-form-item label="评论用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入评论用户ID" />
        </el-form-item>
        <el-form-item label="评论内容" prop="content">
          <el-input v-model="form.content" placeholder="请输入评论内容" />
        </el-form-item>
        <el-form-item label="父评论ID，NULL表示直接评论" prop="parentCommentId">
          <el-input v-model="form.parentCommentId" placeholder="请输入父评论ID，NULL表示直接评论" />
        </el-form-item>
        <el-form-item label="评论时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择评论时间">
          </el-date-picker>
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

<script setup name="Comments">
import { listComments, getComments, delComments, addComments, updateComments } from "@/api/comments/comments";

const { proxy } = getCurrentInstance();

const commentsList = ref([]);
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
    postId: null,
    userId: null,
    content: null,
    parentCommentId: null,
    createdAt: null
  },
  rules: {
    postId: [
      { required: true, message: "评论所属帖子ID不能为空", trigger: "blur" }
    ],
    userId: [
      { required: true, message: "评论用户ID不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "评论内容不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询评论列表 */
function getList() {
  loading.value = true;
  listComments(queryParams.value).then(response => {
    commentsList.value = response.rows;
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
    postId: null,
    userId: null,
    content: null,
    parentCommentId: null,
    createdAt: null
  };
  proxy.resetForm("commentsRef");
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
  title.value = "添加评论";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getComments(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改评论";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["commentsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateComments(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addComments(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除评论编号为"' + _ids + '"的数据项？').then(function() {
    return delComments(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('comments/comments/export', {
    ...queryParams.value
  }, `comments_${new Date().getTime()}.xlsx`)
}

getList();
</script>
