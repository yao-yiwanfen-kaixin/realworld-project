<template>
  <div class="auth-page">
    <div class="container">
      <div class="auth-form">
        <h1 class="text-xs-center">Sign Up</h1>
        <p class="text-xs-center">
          <router-link to="/login">Have an account?</router-link>
        </p>
        <el-form :model="form" @submit.prevent="submitRegister">
          <el-form-item>
            <el-input
              v-model="form.username"
              placeholder="Username"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.email"
              placeholder="Email"
              type="email"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              placeholder="Password"
              type="password"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="btn-lg" @click="submitRegister" :loading="loading">
              Sign up
            </el-button>
          </el-form-item>
        </el-form>
        <el-alert
          v-if="error"
          :title="error"
          type="error"
          show-icon
          style="margin-top: 1rem"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const form = ref({
  username: '',
  email: '',
  password: ''
})
const loading = ref(false)
const error = ref('')

const submitRegister = async () => {
  loading.value = true
  error.value = ''
  
  try {
    await userStore.register(form.value)
    ElMessage.success('Registration successful')
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.message || 'Registration failed, please try again'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  padding: 3rem 0;
}
.auth-form {
  max-width: 500px;
  margin: 0 auto;
}
.text-xs-center {
  text-align: center;
  margin-bottom: 1rem;
}
.btn-lg {
  width: 100%;
}
</style>
