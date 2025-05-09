import request from '@/utils/request'

// 查询点赞记录列表
export function listLikes(query) {
  return request({
    url: '/likes/likes/list',
    method: 'get',
    params: query
  })
}

// 查询点赞记录详细
export function getLikes(id) {
  return request({
    url: '/likes/likes/' + id,
    method: 'get'
  })
}

// 新增点赞记录
export function addLikes(data) {
  return request({
    url: '/likes/likes',
    method: 'post',
    data: data
  })
}

// 修改点赞记录
export function updateLikes(data) {
  return request({
    url: '/likes/likes',
    method: 'put',
    data: data
  })
}

// 删除点赞记录
export function delLikes(id) {
  return request({
    url: '/likes/likes/' + id,
    method: 'delete'
  })
}
