<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会话ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入会话ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话名称" prop="conversationName">
        <el-input
          v-model="queryParams.conversationName"
          placeholder="请输入会话名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否为群聊，0 表示单聊，1 表示群聊" prop="isGroupChat">
        <el-input
          v-model="queryParams.isGroupChat"
          placeholder="请输入是否为群聊，0 表示单聊，1 表示群聊"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话创建时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择会话创建时间">
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
          v-hasPermi="['conversations:conversations:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['conversations:conversations:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['conversations:conversations:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['conversations:conversations:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="conversationsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会话ID" align="center" prop="id" />
      <el-table-column label="会话名称" align="center" prop="conversationName" />
      <el-table-column label="是否为群聊，0 表示单聊，1 表示群聊" align="center" prop="isGroupChat" />
      <el-table-column label="会话创建时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['conversations:conversations:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['conversations:conversations:remove']">删除</el-button>
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

    <!-- 添加或修改会话对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="conversationsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话名称" prop="conversationName">
          <el-input v-model="form.conversationName" placeholder="请输入会话名称" />
        </el-form-item>
        <el-form-item label="是否为群聊，0 表示单聊，1 表示群聊" prop="isGroupChat">
          <el-input v-model="form.isGroupChat" placeholder="请输入是否为群聊，0 表示单聊，1 表示群聊" />
        </el-form-item>
        <el-form-item label="会话创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择会话创建时间">
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

<script setup name="Conversations">
import { listConversations, getConversations, delConversations, addConversations, updateConversations } from "@/api/conversations/conversations";

const { proxy } = getCurrentInstance();

const conversationsList = ref([]);
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
    conversationName: null,
    isGroupChat: null,
    createdAt: null
  },
  rules: {
    conversationName: [
      { required: true, message: "会话名称不能为空", trigger: "blur" }
    ],
    isGroupChat: [
      { required: true, message: "是否为群聊，0 表示单聊，1 表示群聊不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询会话列表 */
function getList() {
  loading.value = true;
  listConversations(queryParams.value).then(response => {
    conversationsList.value = response.rows;
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
    conversationName: null,
    isGroupChat: null,
    createdAt: null
  };
  proxy.resetForm("conversationsRef");
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
  title.value = "添加会话";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getConversations(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改会话";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["conversationsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateConversations(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addConversations(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除会话编号为"' + _ids + '"的数据项？').then(function() {
    return delConversations(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('conversations/conversations/export', {
    ...queryParams.value
  }, `conversations_${new Date().getTime()}.xlsx`)
}

getList();
</script>
