<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发起请求的用户ID" prop="requesterId">
        <el-input
          v-model="queryParams.requesterId"
          placeholder="请输入发起请求的用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收请求的用户ID" prop="receiverId">
        <el-input
          v-model="queryParams.receiverId"
          placeholder="请输入接收请求的用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="附加消息" prop="message">
        <el-input
          v-model="queryParams.message"
          placeholder="请输入附加消息"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求创建时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择请求创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态更新时间" prop="updatedAt">
        <el-date-picker clearable
          v-model="queryParams.updatedAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择状态更新时间">
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
          v-hasPermi="['requests:requests:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['requests:requests:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['requests:requests:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['requests:requests:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="requestsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="发起请求的用户ID" align="center" prop="requesterId" />
      <el-table-column label="接收请求的用户ID" align="center" prop="receiverId" />
      <el-table-column label="请求状态" align="center" prop="status" />
      <el-table-column label="附加消息" align="center" prop="message" />
      <el-table-column label="请求创建时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态更新时间" align="center" prop="updatedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['requests:requests:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['requests:requests:remove']">删除</el-button>
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

    <!-- 添加或修改好友请求对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="requestsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发起请求的用户ID" prop="requesterId">
          <el-input v-model="form.requesterId" placeholder="请输入发起请求的用户ID" />
        </el-form-item>
        <el-form-item label="接收请求的用户ID" prop="receiverId">
          <el-input v-model="form.receiverId" placeholder="请输入接收请求的用户ID" />
        </el-form-item>
        <el-form-item label="附加消息" prop="message">
          <el-input v-model="form.message" placeholder="请输入附加消息" />
        </el-form-item>
        <el-form-item label="请求创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择请求创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态更新时间" prop="updatedAt">
          <el-date-picker clearable
            v-model="form.updatedAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择状态更新时间">
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

<script setup name="Requests">
import { listRequests, getRequests, delRequests, addRequests, updateRequests } from "@/api/requests/requests";

const { proxy } = getCurrentInstance();

const requestsList = ref([]);
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
    requesterId: null,
    receiverId: null,
    status: null,
    message: null,
    createdAt: null,
    updatedAt: null
  },
  rules: {
    requesterId: [
      { required: true, message: "发起请求的用户ID不能为空", trigger: "blur" }
    ],
    receiverId: [
      { required: true, message: "接收请求的用户ID不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "请求状态不能为空", trigger: "change" }
    ],
    message: [
      { required: true, message: "附加消息不能为空", trigger: "blur" }
    ],
    createdAt: [
      { required: true, message: "请求创建时间不能为空", trigger: "blur" }
    ],
    updatedAt: [
      { required: true, message: "状态更新时间不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询好友请求列表 */
function getList() {
  loading.value = true;
  listRequests(queryParams.value).then(response => {
    requestsList.value = response.rows;
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
    requesterId: null,
    receiverId: null,
    status: null,
    message: null,
    createdAt: null,
    updatedAt: null
  };
  proxy.resetForm("requestsRef");
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
  title.value = "添加好友请求";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRequests(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改好友请求";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["requestsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRequests(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRequests(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除好友请求编号为"' + _ids + '"的数据项？').then(function() {
    return delRequests(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('requests/requests/export', {
    ...queryParams.value
  }, `requests_${new Date().getTime()}.xlsx`)
}

getList();
</script>
