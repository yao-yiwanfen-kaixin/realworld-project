import request from '../utils/request'

export const register = (data) => {
  return request({
    url: '/users',
    method: 'post',
    data: { user: data }
  })
}

export const login = (data) => {
  return request({
    url: '/users/login',
    method: 'post',
    data: { user: data }
  })
}

export const getCurrentUser = () => {
  return request({
    url: '/user',
    method: 'get'
  })
}