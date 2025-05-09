import request from '@/utils/request'

// 查询好友关系列表
export function listFriends(query) {
  return request({
    url: '/friends/friends/list',
    method: 'get',
    params: query
  })
}

// 查询好友关系详细
export function getFriends(id) {
  return request({
    url: '/friends/friends/' + id,
    method: 'get'
  })
}

// 新增好友关系
export function addFriends(data) {
  return request({
    url: '/friends/friends',
    method: 'post',
    data: data
  })
}

// 修改好友关系
export function updateFriends(data) {
  return request({
    url: '/friends/friends',
    method: 'put',
    data: data
  })
}

// 删除好友关系
export function delFriends(id) {
  return request({
    url: '/friends/friends/' + id,
    method: 'delete'
  })
}
