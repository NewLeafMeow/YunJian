import request from '@/utils/request'

// 查询关注关系列表
export function listFollows(query) {
  return request({
    url: '/follows/follows/list',
    method: 'get',
    params: query
  })
}

// 查询关注关系详细
export function getFollows(followerId) {
  return request({
    url: '/follows/follows/' + followerId,
    method: 'get'
  })
}

// 新增关注关系
export function addFollows(data) {
  return request({
    url: '/follows/follows',
    method: 'post',
    data: data
  })
}

// 修改关注关系
export function updateFollows(data) {
  return request({
    url: '/follows/follows',
    method: 'put',
    data: data
  })
}

// 删除关注关系
export function delFollows(followerId) {
  return request({
    url: '/follows/follows/' + followerId,
    method: 'delete'
  })
}
