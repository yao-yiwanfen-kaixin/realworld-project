import request from '../utils/request'

export const getArticles = (params) => {
  return request({
    url: '/articles',
    method: 'get',
    params
  })
}

export const getArticle = (slug) => {
  return request({
    url: `/articles/${slug}`,
    method: 'get'
  })
}

export const createArticle = (data) => {
  return request({
    url: '/articles',
    method: 'post',
    data: { article: data }
  })
}

export const updateArticle = (slug, data) => {
  return request({
    url: `/articles/${slug}`,
    method: 'put',
    data: { article: data }
  })
}

export const deleteArticle = (slug) => {
  return request({
    url: `/articles/${slug}`,
    method: 'delete'
  })
}

export const favoriteArticle = (slug) => {
  return request({
    url: `/articles/${slug}/favorite`,
    method: 'post'
  })
}

export const unfavoriteArticle = (slug) => {
  return request({
    url: `/articles/${slug}/favorite`,
    method: 'delete'
  })
}

export const getComments = (slug) => {
  return request({
    url: `/articles/${slug}/comments`,
    method: 'get'
  })
}

export const addComment = (slug, body) => {
  return request({
    url: `/articles/${slug}/comments`,
    method: 'post',
    data: { comment: { body } }
  })
}

export const deleteComment = (slug, id) => {
  return request({
    url: `/articles/${slug}/comments/${id}`,
    method: 'delete'
  })
}