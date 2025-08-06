<script setup>
import { ref, onMounted } from 'vue'
import BlogList from './components/BlogList.vue'
import BlogEditor from './components/BlogEditor.vue'
import BlogPost from './components/BlogPost.vue'
import LoginForm from './components/LoginForm.vue'
import Sidebar from './components/Sidebar.vue'

const isLoggedIn = ref(false)
const user = ref(null)
const showLogin = ref(false)
const loginCallback = ref(null)

const currentView = ref('blog-list') // 'blog-list', 'blog-editor', 'blog-post'
const currentPostId = ref(null)
const editingPostId = ref(null)

// 初始化登录状态
const initLoginState = () => {
  const token = localStorage.getItem('token')
  const userData = localStorage.getItem('user')

  if (token && userData) {
    try {
      isLoggedIn.value = true
      user.value = JSON.parse(userData)
    } catch (error) {
      console.error('恢复登录状态失败:', error)
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
}

const handleLoginSuccess = (data) => {
  isLoggedIn.value = true
  user.value = data.user
  showLogin.value = false
  if (typeof loginCallback.value === 'function') {
    loginCallback.value()
    loginCallback.value = null
  }
}

const handleLogout = () => {
  isLoggedIn.value = false
  user.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}

const handleViewPost = (postId) => {
  currentPostId.value = postId
  currentView.value = 'blog-post'
}

const handleEditPost = (postId) => {
  if (!isLoggedIn.value) {
    showLogin.value = true
    loginCallback.value = () => handleEditPost(postId)
    return
  }
  editingPostId.value = postId
  currentView.value = 'blog-editor'
}

const handleCreatePost = () => {
  if (!isLoggedIn.value) {
    showLogin.value = true
    loginCallback.value = handleCreatePost
    return
  }
  editingPostId.value = null
  currentView.value = 'blog-editor'
}

const handleBackToList = () => {
  currentView.value = 'blog-list'
  currentPostId.value = null
  editingPostId.value = null
}

const handlePostSaved = () => {
  currentView.value = 'blog-list'
  editingPostId.value = null
}

// 页面加载时初始化登录状态
onMounted(() => {
  initLoginState()
})
</script>

<template>
  <div id="app">
    <Sidebar />

    <!-- 导航栏独立出来，占满宽度 -->
    <header class="app-header">
      <div class="header-left">
        <h1>
          <span style="background: linear-gradient(90deg,#4f8cff,#92e0ff);-webkit-background-clip:text;color:transparent;">我的博客</span>
          <span style="font-size:1.5rem;">📝</span>
        </h1>
        <nav class="nav-menu">
          <button
            @click="currentView = 'blog-list'"
            :class="['nav-btn', { active: currentView === 'blog-list' }]"
          >
            文章列表
          </button>
          <button
            v-if="isLoggedIn"
            @click="handleCreatePost"
            :class="['nav-btn', { active: currentView === 'blog-editor' }]"
          >
            写文章
          </button>
        </nav>
      </div>
      <div class="header-right">
        <span v-if="isLoggedIn" class="user-info">欢迎，{{ user?.username || '用户' }}！</span>
        <button v-if="isLoggedIn" @click="handleLogout" class="logout-button">退出登录</button>
        <button v-else @click="showLogin = true" class="login-button">登录</button>
      </div>
    </header>

    <div class="main-container">
      <main class="app-main">
        <!-- 博客列表 -->
        <BlogList
          v-if="currentView === 'blog-list'"
          @view-post="handleViewPost"
          @edit-post="handleEditPost"
          @create-post="handleCreatePost"
          :is-logged-in="isLoggedIn"
        />

        <!-- 博客编辑器 -->
        <BlogEditor
          v-else-if="currentView === 'blog-editor'"
          :post-id="editingPostId"
          @back="handleBackToList"
          @saved="handlePostSaved"
        />

        <!-- 博客文章详情 -->
        <BlogPost
          v-else-if="currentView === 'blog-post'"
          :post-id="currentPostId"
          @back="handleBackToList"
          :is-logged-in="isLoggedIn"
          @show-login="showLogin = true"
        />
      </main>
    </div>

    <!-- 登录弹窗 -->
    <div v-if="showLogin" class="login-modal">
      <div class="login-modal-mask" @click="showLogin = false"></div>
      <div class="login-modal-content">
        <LoginForm @login-success="handleLoginSuccess" @close="showLogin = false" />
      </div>
    </div>
  </div>
</template>

<style scoped>
#app {
  min-height: 100vh;
}

.main-content {
  min-height: 100vh;
  background: #f8fafc;
}

.app-header {
  background: white;
  padding: 20px 40px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.app-header h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.nav-menu {
  display: flex;
  gap: 10px;
}

.nav-btn {
  background: none;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  transition: all 0.3s ease;
}

.nav-btn:hover {
  background: #f8fafc;
  color: #333;
}

.nav-btn.active {
  background: #667eea;
  color: white;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  color: #666;
  font-size: 14px;
}

.logout-button {
  background: #ef4444;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.logout-button:hover {
  background: #dc2626;
}

.login-button {
  background: #667eea;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background: #5a67d8;
}

.app-main {
  padding: 40px;
}

/* 登录弹窗样式 */
.login-modal {
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-modal-content {
  position: relative;
  z-index: 1001;
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  padding: 0;
  min-width: unset;
  max-width: unset;
  display: flex;
  align-items: center;
  justify-content: center;
  width: auto;
  height: auto;
}
.login-modal-close {
  position: absolute;
  right: 15px;
  top: 15px;
  background: none;
  border: none;
  font-size: 20px;
  color: #4f8cff;
  cursor: pointer;
  z-index: 1002;
  transition: color 0.2s;
}
.login-modal-close:hover {
  color: #92e0ff;
}
.login-modal-mask {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.18);
  z-index: 1000;
}
@media (max-width: 768px) {
  .app-header {
    padding: 15px 20px;
    flex-direction: column;
    gap: 15px;
  }
  .app-header h1 {
    font-size: 20px;
  }
  .app-main {
    padding: 20px;
  }
}
</style>
