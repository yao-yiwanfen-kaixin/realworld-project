<template>
  <div id="app">
    <nav class="navbar">
      <div class="container">
        <router-link to="/" class="navbar-brand">RealWorld</router-link>
        <ul class="nav">
          <li class="nav-item">
            <router-link to="/" class="nav-link">Home</router-link>
          </li>
          <li class="nav-item" v-if="!isLoggedIn">
            <router-link to="/login" class="nav-link">Sign in</router-link>
          </li>
          <li class="nav-item" v-if="!isLoggedIn">
            <router-link to="/register" class="nav-link">Sign up</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <router-link to="/editor" class="nav-link">New Article</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <span class="nav-link">{{ user.username }}</span>
          </li>
        </ul>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from './store/user'

const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isLoggedIn)
const user = computed(() => userStore.user || { username: '' })
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
}

.navbar {
  background: #5cb85c;
  padding: 1rem 0;
}

.container {
  max-width: 1140px;
  margin: 0 auto;
  padding: 0 15px;
}

.navbar-brand {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
}

.nav {
  display: flex;
  list-style: none;
  float: right;
}

.nav-item {
  margin-left: 1rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  opacity: 0.8;
}

.nav-link:hover {
  opacity: 1;
}
</style>