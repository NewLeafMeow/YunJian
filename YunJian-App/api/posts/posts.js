import request from '@/utils/request'

// 查询帖子信息列表
export function listPosts(query) {
  return request({
    url: '/posts/posts/list',
    method: 'get',
    params: query
  })
}

// 查询帖子信息详细
export function getPosts(id) {
  return request({
    url: '/posts/posts/' + id,
    method: 'get'
  })
}

// 新增帖子信息
export function addPosts(data) {
  return request({
    url: '/posts/posts',
    method: 'post',
    data: data
  })
}

// 修改帖子信息
export function updatePosts(data) {
  return request({
    url: '/posts/posts',
    method: 'put',
    data: data
  })
}

// 删除帖子信息
export function delPosts(id) {
  return request({
    url: '/posts/posts/' + id,
    method: 'delete'
  })
}
