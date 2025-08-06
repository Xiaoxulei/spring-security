<template>
  <div class="blog-list">
    <div class="blog-header">
      <h1>博客文章</h1>
      <button v-if="isLoggedIn" @click="createNewPost" class="new-post-btn">
        <span>+</span> 写新文章
      </button>
    </div>

    <div class="blog-filters">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索文章..."
        class="search-input"
      />
      <select v-model="selectedCategory" class="category-select">
        <option value="">所有分类</option>
        <option value="技术">技术</option>
        <option value="生活">生活</option>
        <option value="随笔">随笔</option>
        <option value="教程">教程</option>
      </select>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="filteredPosts.length === 0" class="empty-state">
      <div class="empty-icon">📝</div>
      <h3>暂无文章</h3>
      <p v-if="!isLoggedIn">请先登录后再写文章</p>
      <p v-else>开始写你的第一篇文章吧！</p>
      <button v-if="isLoggedIn" @click="createNewPost" class="empty-btn">写文章</button>
    </div>

    <div v-else class="posts-grid">
      <article
        v-for="post in filteredPosts"
        :key="post.id"
        class="post-card"
        @click="viewPost(post.id)"
      >
        <div class="post-image" v-if="post.coverImage">
          <img :src="post.coverImage" :alt="post.title" />
        </div>
        <div class="post-content">
          <div class="post-meta">
            <span class="post-category">{{ post.category }}</span>
            <span class="post-date">{{ formatDate(post.createdAt) }}</span>
          </div>
          <h3 class="post-title">{{ post.title }}</h3>
          <p class="post-excerpt">{{ post.excerpt }}</p>
          <div class="post-footer">
            <div class="post-stats">
              <span class="stat">
                <span class="icon">👁️</span>
                {{ post.views }}
              </span>
              <span class="stat">
                <span class="icon">💬</span>
                {{ post.comments }}
              </span>
              <span class="stat">
                <span class="icon">❤️</span>
                {{ post.likes }}
              </span>
            </div>
            <div class="post-actions" v-if="isLoggedIn">
              <button @click.stop="editPost(post.id)" class="edit-btn">编辑</button>
              <button @click.stop="deletePost(post.id)" class="delete-btn">删除</button>
            </div>
          </div>
        </div>
      </article>
    </div>

    <div v-if="hasMorePosts" class="load-more">
      <button @click="loadMorePosts" :disabled="loadingMore" class="load-more-btn">
        {{ loadingMore ? '加载中...' : '加载更多' }}
      </button>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { blogAPI } from '../services/api.js'

export default {
  name: 'BlogList',
  props: {
    isLoggedIn: {
      type: Boolean,
      default: false
    }
  },
  emits: ['view-post', 'edit-post', 'create-post'],
  setup(props, { emit }) {
    const posts = ref([])
    const loading = ref(false)
    const loadingMore = ref(false)
    const searchQuery = ref('')
    const selectedCategory = ref('')
    const page = ref(1)
    const hasMorePosts = ref(true)

    const filteredPosts = computed(() => {
      let filtered = posts.value

      if (searchQuery.value) {
        filtered = filtered.filter(post =>
          post.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          post.content.toLowerCase().includes(searchQuery.value.toLowerCase())
        )
      }

      if (selectedCategory.value) {
        filtered = filtered.filter(post => post.category === selectedCategory.value)
      }

      return filtered
    })

    const fetchPosts = async (pageNum = 1) => {
      try {
        loading.value = pageNum === 1
        loadingMore.value = pageNum > 1

        const response = await blogAPI.getPosts({ page: pageNum, limit: 10 })

        if (pageNum === 1) {
          posts.value = response.data.posts
        } else {
          posts.value.push(...response.data.posts)
        }

        hasMorePosts.value = response.data.posts.length === 10
        page.value = pageNum
      } catch (error) {
        console.error('获取文章列表失败:', error)
      } finally {
        loading.value = false
        loadingMore.value = false
      }
    }

    const loadMorePosts = () => {
      if (!loadingMore.value && hasMorePosts.value) {
        fetchPosts(page.value + 1)
      }
    }

    const viewPost = (postId) => {
      emit('view-post', postId)
    }

    const editPost = (postId) => {
      emit('edit-post', postId)
    }

    const createNewPost = () => {
      emit('create-post')
    }

    const deletePost = async (postId) => {
      if (confirm('确定要删除这篇文章吗？')) {
        try {
          await blogAPI.deletePost(postId)
          posts.value = posts.value.filter(post => post.id !== postId)
        } catch (error) {
          console.error('删除文章失败:', error)
        }
      }
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }

    onMounted(() => {
      fetchPosts()
    })

    return {
      posts,
      loading,
      loadingMore,
      searchQuery,
      selectedCategory,
      filteredPosts,
      hasMorePosts,
      viewPost,
      editPost,
      createNewPost,
      deletePost,
      loadMorePosts,
      formatDate
    }
  }
}
</script>

<style scoped>
.blog-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.blog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.blog-header h1 {
  color: #333;
  font-size: 32px;
  font-weight: 700;
  margin: 0;
}

.new-post-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.new-post-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.blog-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.search-input,
.category-select {
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

.search-input {
  flex: 1;
  max-width: 300px;
}

.search-input:focus,
.category-select:focus {
  outline: none;
  border-color: #667eea;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-state h3 {
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.empty-state p {
  color: #666;
  margin-bottom: 30px;
  font-size: 16px;
}

.empty-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.empty-btn:hover {
  background: #5a67d8;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
}

.post-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.post-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.post-image {
  height: 200px;
  overflow: hidden;
}

.post-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.post-content {
  padding: 20px;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.post-category {
  background: #f0f4ff;
  color: #667eea;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.post-date {
  color: #999;
  font-size: 14px;
}

.post-title {
  color: #333;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  line-height: 1.4;
}

.post-excerpt {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-stats {
  display: flex;
  gap: 15px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.icon {
  font-size: 16px;
}

.post-actions {
  display: flex;
  gap: 8px;
}

.edit-btn,
.delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-btn {
  background: #f0f4ff;
  color: #667eea;
}

.edit-btn:hover {
  background: #e0e7ff;
}

.delete-btn {
  background: #fef2f2;
  color: #ef4444;
}

.delete-btn:hover {
  background: #fee2e2;
}

.load-more {
  text-align: center;
  margin-top: 40px;
}

.load-more-btn {
  background: #f8fafc;
  color: #667eea;
  border: 2px solid #e2e8f0;
  padding: 12px 30px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.load-more-btn:hover:not(:disabled) {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.load-more-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .blog-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .blog-filters {
    flex-direction: column;
  }

  .search-input {
    max-width: none;
  }

  .posts-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .post-footer {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
}
</style>
