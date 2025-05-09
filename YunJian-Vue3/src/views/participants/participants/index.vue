<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="记录ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入记录ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话ID" prop="conversationId">
        <el-input
          v-model="queryParams.conversationId"
          placeholder="请输入会话ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参与用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入参与用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="加入时间" prop="joinedAt">
        <el-date-picker clearable
          v-model="queryParams.joinedAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择加入时间">
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
          v-hasPermi="['participants:participants:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['participants:participants:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['participants:participants:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['participants:participants:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="participantsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="id" />
      <el-table-column label="会话ID" align="center" prop="conversationId" />
      <el-table-column label="参与用户ID" align="center" prop="userId" />
      <el-table-column label="加入时间" align="center" prop="joinedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.joinedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会话参与者身份" align="center" prop="participantIdentity" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['participants:participants:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['participants:participants:remove']">删除</el-button>
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

    <!-- 添加或修改会话参与者对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="participantsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话ID" prop="conversationId">
          <el-input v-model="form.conversationId" placeholder="请输入会话ID" />
        </el-form-item>
        <el-form-item label="参与用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入参与用户ID" />
        </el-form-item>
        <el-form-item label="加入时间" prop="joinedAt">
          <el-date-picker clearable
            v-model="form.joinedAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择加入时间">
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

<script setup name="Participants">
import { listParticipants, getParticipants, delParticipants, addParticipants, updateParticipants } from "@/api/participants/participants";

const { proxy } = getCurrentInstance();

const participantsList = ref([]);
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
    conversationId: null,
    userId: null,
    joinedAt: null,
    participantIdentity: null
  },
  rules: {
    conversationId: [
      { required: true, message: "会话ID不能为空", trigger: "blur" }
    ],
    userId: [
      { required: true, message: "参与用户ID不能为空", trigger: "blur" }
    ],
    participantIdentity: [
      { required: true, message: "会话参与者身份不能为空", trigger: "change" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询会话参与者列表 */
function getList() {
  loading.value = true;
  listParticipants(queryParams.value).then(response => {
    participantsList.value = response.rows;
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
    conversationId: null,
    userId: null,
    joinedAt: null,
    participantIdentity: null
  };
  proxy.resetForm("participantsRef");
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
  title.value = "添加会话参与者";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getParticipants(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改会话参与者";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["participantsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateParticipants(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addParticipants(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除会话参与者编号为"' + _ids + '"的数据项？').then(function() {
    return delParticipants(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('participants/participants/export', {
    ...queryParams.value
  }, `participants_${new Date().getTime()}.xlsx`)
}

getList();
</script>
