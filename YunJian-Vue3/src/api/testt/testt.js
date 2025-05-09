import request from '@/utils/request'

// 查询测试列表
export function listTestt(query) {
  return request({
    url: '/testt/testt/list',
    method: 'get',
    params: query
  })
}

// 查询测试详细
export function getTestt(id) {
  return request({
    url: '/testt/testt/' + id,
    method: 'get'
  })
}

// 新增测试
export function addTestt(data) {
  return request({
    url: '/testt/testt',
    method: 'post',
    data: data
  })
}

// 修改测试
export function updateTestt(data) {
  return request({
    url: '/testt/testt',
    method: 'put',
    data: data
  })
}

// 删除测试
export function delTestt(id) {
  return request({
    url: '/testt/testt/' + id,
    method: 'delete'
  })
}
