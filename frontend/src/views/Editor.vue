<template>
  <div class="editor-page">
    <div class="container">
      <div class="editor-form">
        <el-form :model="form" @submit.prevent="submitArticle">
          <el-form-item>
            <el-input
              v-model="form.title"
              placeholder="文章标题"
              size="large"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.description"
              placeholder="文章简介"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.body"
              type="textarea"
              :rows="15"
              placeholder="文章内容（支持Markdown）"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitArticle" :loading="loading">
              发布文章
            </el-button>
          </el-form-item>
        </el-form>

        <el-alert
          v-if="error"
          :title="error"
          type="error"
          show-icon
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createArticle } from '../api/article'
import { ElMessage } from 'element-plus'

const router = useRouter()

const form = ref({
  title: '',
  description: '',
  body: ''
})
const loading = ref(false)
const error = ref('')

const submitArticle = async () => {
  if (!form.value.title || !form.value.body) {
    error.value = '标题和内容不能为空'
    return
  }

  loading.value = true
  error.value = ''

  try {
    const response = await createArticle(form.value)
    ElMessage.success('发布成功')
    router.push(`/article/${response.article.slug}`)
  } catch (e) {
    error.value = '发布失败，请先登录'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.editor-page {
  padding: 3rem 0;
}

.editor-form {
  max-width: 800px;
  margin: 0 auto;
}
</style>