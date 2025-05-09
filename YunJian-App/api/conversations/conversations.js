import request from '@/utils/request'

// 查询会话列表
export function listConversations(query) {
  return request({
    url: '/conversations/conversations/list',
    method: 'get',
    params: query
  })
}

// 查询会话详细
export function getConversations(id) {
  return request({
    url: '/conversations/conversations/' + id,
    method: 'get'
  })
}

// 新增会话
export function addConversations(data) {
  return request({
    url: '/conversations/conversations',
    method: 'post',
    data: data
  })
}

// 修改会话
export function updateConversations(data) {
  return request({
    url: '/conversations/conversations',
    method: 'put',
    data: data
  })
}

// 删除会话
export function delConversations(id) {
  return request({
    url: '/conversations/conversations/' + id,
    method: 'delete'
  })
}
