import { defineStore } from 'pinia'
import { login, register } from '../api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token
  },

  actions: {
    async login(credentials) {
      const response = await login(credentials)
      this.user = response.user
      this.token = response.user.token
      localStorage.setItem('token', response.user.token)
      return response
    },

    async register(userData) {
      const response = await register(userData)
      this.user = response.user
      this.token = response.user.token
      localStorage.setItem('token', response.user.token)
      return response
    },

    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
    }
  }
})