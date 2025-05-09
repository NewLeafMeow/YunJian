import request from '@/utils/request'

// 查询会话参与者列表
export function listParticipants(query) {
  return request({
    url: '/participants/participants/list',
    method: 'get',
    params: query
  })
}

// 查询会话参与者详细
export function getParticipants(id) {
  return request({
    url: '/participants/participants/' + id,
    method: 'get'
  })
}

// 新增会话参与者
export function addParticipants(data) {
  return request({
    url: '/participants/participants',
    method: 'post',
    data: data
  })
}

// 修改会话参与者
export function updateParticipants(data) {
  return request({
    url: '/participants/participants',
    method: 'put',
    data: data
  })
}

// 删除会话参与者
export function delParticipants(id) {
  return request({
    url: '/participants/participants/' + id,
    method: 'delete'
  })
}
