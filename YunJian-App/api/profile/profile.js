import request from '@/utils/request'

// 查询用户画像列表
export function listProfile(query) {
  return request({
    url: '/profile/profile/list',
    method: 'get',
    params: query
  })
}

// 查询用户画像详细
export function getProfile(id) {
  return request({
    url: '/profile/profile/' + id,
    method: 'get'
  })
}

// 新增用户画像
export function addProfile(data) {
  return request({
    url: '/profile/profile',
    method: 'post',
    data: data
  })
}

// 修改用户画像
export function updateProfile(data) {
  return request({
    url: '/profile/profile',
    method: 'put',
    data: data
  })
}

// 删除用户画像
export function delProfile(id) {
  return request({
    url: '/profile/profile/' + id,
    method: 'delete'
  })
}
