import request from '@/utils/request'

// 查询好友请求列表
export function listRequests(query) {
  return request({
    url: '/requests/requests/list',
    method: 'get',
    params: query
  })
}

// 查询好友请求详细
export function getRequests(id) {
  return request({
    url: '/requests/requests/' + id,
    method: 'get'
  })
}

// 新增好友请求
export function addRequests(data) {
  return request({
    url: '/requests/requests',
    method: 'post',
    data: data
  })
}

// 修改好友请求
export function updateRequests(data) {
  return request({
    url: '/requests/requests',
    method: 'put',
    data: data
  })
}

// 删除好友请求
export function delRequests(id) {
  return request({
    url: '/requests/requests/' + id,
    method: 'delete'
  })
}
