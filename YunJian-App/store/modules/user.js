import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const baseUrl = config.baseUrl  // 基础URL，通常用于处理头像或其他资源路径

const user = {
  state: {
    // 初始化用户状态，包括token、名字、头像、角色和权限，从本地存储中获取数据
    token: getToken(),
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
  },

  mutations: {
    // 设置token
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    // 设置用户名，并保存到本地存储
    SET_NAME: (state, name) => {
      state.name = name
      storage.set(constant.name, name)
    },
    // 设置用户头像，并保存到本地存储
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    // 设置用户角色，并保存到本地存储
    SET_ROLES: (state, roles) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    // 设置用户权限，并保存到本地存储
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    }
  },

  actions: {
    // 登录操作
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()  // 去除用户名的空格
      const password = userInfo.password  // 获取用户密码
      const code = userInfo.code  // 验证码
      const uuid = userInfo.uuid  // 唯一标识符
      return new Promise((resolve, reject) => {
        // 调用登录接口
        login(username, password, code, uuid).then(res => {
          setToken(res.token)  // 保存token
          commit('SET_TOKEN', res.token)  // 更新state中的token
          resolve()  // 登录成功，返回结果
        }).catch(error => {
          reject(error)  // 登录失败，返回错误信息
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        // 调用获取用户信息接口
        getInfo().then(res => {
          const user = res.user  // 获取用户信息
          // 如果用户头像为空或不存在，则使用默认头像
          const avatar = (user == null || user.avatar == "" || user.avatar == null)
            ? require("@/static/images/profile.jpg")
            : baseUrl + user.avatar
          // 如果用户名为空或不存在，设置为空字符串
          const username = (user == null || user.userName == "" || user.userName == null) ? "" : user.userName

          // 判断用户角色，如果有角色信息，保存角色和权限；如果没有角色，设置默认角色
          if (res.roles && res.roles.length > 0) {
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', username)  // 更新用户名
          commit('SET_AVATAR', avatar)  // 更新头像
          resolve(res)  // 返回用户信息
        }).catch(error => {
          reject(error)  // 获取用户信息失败，返回错误信息
        })
      })
    },

    // 退出登录
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        // 调用退出登录接口
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')  // 清空token
          commit('SET_ROLES', [])  // 清空角色
          commit('SET_PERMISSIONS', [])  // 清空权限
          removeToken()  // 删除本地存储中的token
          storage.clean()  // 清空其他本地存储的数据
          resolve()  // 登出成功，返回结果
        }).catch(error => {
          reject(error)  // 退出失败，返回错误信息
        })
      })
    }
  }
}

export default user  // 导出user模块
