<template>
  <div class="article-page">
    <div v-if="loading" class="container text-xs-center" style="padding: 3rem;">
      加载中...
    </div>

    <div v-else-if="article">
      <div class="banner">
        <div class="container">
          <h1>{{ article.title }}</h1>
          <div class="article-meta">
            <span class="author">{{ article.author.username }}</span>
            <span class="date">{{ formatDate(article.createdAt) }}</span>
            <el-button
              :type="article.favorited ? 'success' : 'default'"
              size="small"
              @click="toggleFavorite"
              style="margin-left: 1rem"
            >
              <span>❤️</span> {{ article.favoritesCount }}
            </el-button>
          </div>
        </div>
      </div>

      <div class="container page">
        <div class="article-content">
          <p>{{ article.body }}</p>
        </div>

        <hr />

        <div class="comment-section">
          <div v-if="isLoggedIn" class="comment-form">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="写下你的评论..."
            />
            <el-button
              type="primary"
              style="margin-top: 1rem"
              @click="submitComment"
              :loading="commentLoading"
            >
              发表评论
            </el-button>
          </div>

          <div v-else class="text-xs-center" style="padding: 1rem;">
            <router-link to="/login">登录</router-link> 后发表评论
          </div>

          <div v-if="comments.length === 0" class="text-xs-center" style="padding: 2rem;">
            暂无评论
          </div>

          <div v-else>
            <div v-for="comment in comments" :key="comment.id" class="comment-card">
              <p class="comment-body">{{ comment.body }}</p>
              <div class="comment-footer">
                <span class="author">{{ comment.author.username }}</span>
                <span class="date">{{ formatDate(comment.createdAt) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import { getArticle, favoriteArticle, unfavoriteArticle, getComments, addComment } from '../api/article'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()

const article = ref(null)
const comments = ref([])
const loading = ref(true)
const commentLoading = ref(false)
const newComment = ref('')

const isLoggedIn = computed(() => userStore.isLoggedIn)

const loadArticle = async () => {
  loading.value = true
  try {
    const slug = route.params.slug
    const [articleRes, commentsRes] = await Promise.all([
      getArticle(slug),
      getComments(slug)
    ])
    article.value = articleRes.article
    comments.value = commentsRes.comments
  } catch (e) {
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

const toggleFavorite = async () => {
  try {
    if (article.value.favorited) {
      await unfavoriteArticle(article.value.slug)
      article.value.favorited = false
      article.value.favoritesCount--
    } else {
      await favoriteArticle(article.value.slug)
      article.value.favorited = true
      article.value.favoritesCount++
    }
  } catch (e) {
    ElMessage.error('操作失败，请先登录')
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    return
  }

  commentLoading.value = true
  try {
    const response = await addComment(route.params.slug, newComment.value)
    comments.value.unshift(response.comment)
    newComment.value = ''
    ElMessage.success('评论成功')
  } catch (e) {
    ElMessage.error('评论失败')
  } finally {
    commentLoading.value = false
  }
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.banner {
  background: #333;
  color: white;
  padding: 2rem 0;
  margin-bottom: 2rem;
}

.banner h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.article-meta {
  display: flex;
  align-items: center;
}

.author {
  font-weight: bold;
}

.date {
  color: #999;
  font-size: 0.8rem;
  margin-left: 0.5rem;
}

.page {
  padding: 2rem 0;
}

.article-content {
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 2rem;
}

.comment-section {
  max-width: 700px;
  margin: 0 auto;
}

.comment-card {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 1rem;
  margin-bottom: 1rem;
}

.comment-body {
  margin-bottom: 0.5rem;
}

.comment-footer {
  font-size: 0.8rem;
  color: #999;
}

.text-xs-center {
  text-align: center;
  color: #999;
}
</style>