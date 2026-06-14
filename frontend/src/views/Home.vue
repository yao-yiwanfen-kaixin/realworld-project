<template>
  <div class="home-page">
    <div class="banner">
      <div class="container">
        <h1 class="logo-font">RealWorld</h1>
        <p>一个前后端分离的博客平台</p>
      </div>
    </div>

    <div class="container page">
      <div class="articles-toggle">
        <ul class="nav nav-pills outline-active">
          <li class="nav-item">
            <a class="nav-link active">全局文章</a>
          </li>
        </ul>
      </div>

      <div v-if="loading" class="article-preview text-xs-center">
        <p>加载中...</p>
      </div>

      <div v-else-if="articles.length === 0" class="article-preview">
        <p>暂无文章</p>
      </div>

      <div v-else>
        <div v-for="article in articles" :key="article.slug" class="article-preview">
          <div class="article-meta">
            <div class="info">
              <span class="author">{{ article.author.username }}</span>
              <span class="date">{{ formatDate(article.createdAt) }}</span>
            </div>
            <el-button
              :type="article.favorited ? 'success' : 'default'"
              size="small"
              @click="toggleFavorite(article)"
            >
              <span>❤️</span> {{ article.favoritesCount }}
            </el-button>
          </div>
          <router-link :to="`/article/${article.slug}`" class="preview-link">
            <h1>{{ article.title }}</h1>
            <p>{{ article.description }}</p>
            <span>阅读更多...</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticles, favoriteArticle, unfavoriteArticle } from '../api/article'
import { ElMessage } from 'element-plus'

const articles = ref([])
const loading = ref(true)

const loadArticles = async () => {
  loading.value = true
  try {
    const response = await getArticles({ limit: 20, offset: 0 })
    articles.value = response.articles
  } catch (e) {
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

const toggleFavorite = async (article) => {
  try {
    if (article.favorited) {
      await unfavoriteArticle(article.slug)
      article.favorited = false
      article.favoritesCount--
    } else {
      await favoriteArticle(article.slug)
      article.favorited = true
      article.favoritesCount++
    }
  } catch (e) {
    ElMessage.error('操作失败，请先登录')
  }
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.banner {
  background: #5cb85c;
  color: white;
  padding: 2rem;
  text-align: center;
  margin-bottom: 2rem;
}

.banner h1 {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.page {
  padding: 2rem 0;
}

.articles-toggle {
  margin-bottom: 1rem;
}

.nav-pills {
  list-style: none;
  display: flex;
}

.nav-link {
  padding: 0.5rem 1rem;
  color: #5cb85c;
  text-decoration: none;
  border-bottom: 2px solid #5cb85c;
}

.article-preview {
  border-top: 1px solid #eee;
  padding: 1.5rem 0;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.author {
  font-weight: bold;
  color: #5cb85c;
}

.date {
  color: #999;
  font-size: 0.8rem;
  margin-left: 0.5rem;
}

.preview-link {
  text-decoration: none;
  color: inherit;
}

.preview-link h1 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.preview-link p {
  color: #666;
  margin-bottom: 1rem;
}

.preview-link span {
  color: #999;
  font-size: 0.8rem;
}

.text-xs-center {
  text-align: center;
  color: #999;
}
</style>