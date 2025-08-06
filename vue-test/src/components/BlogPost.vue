<template>
  <div class="blog-post">
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="!post" class="error-state">
      <h2>文章不存在</h2>
      <p>抱歉，您访问的文章不存在或已被删除。</p>
      <button @click="goBack" class="back-btn">返回列表</button>
    </div>

    <article v-else class="post-content">
      <header class="post-header">
        <button @click="goBack" class="back-btn">
          <span>←</span> 返回
        </button>

        <div class="post-meta">
          <span class="post-category">{{ post.category }}</span>
          <span class="post-date">{{ formatDate(post.createdAt) }}</span>
          <span class="post-author">作者: {{ post.author?.username || '匿名' }}</span>
        </div>

        <h1 class="post-title">{{ post.title }}</h1>

        <p v-if="post.excerpt" class="post-excerpt">{{ post.excerpt }}</p>

        <div class="post-stats">
          <span class="stat">
            <span class="icon">👁️</span>
            {{ post.views }} 阅读
          </span>
          <span class="stat">
            <span class="icon">💬</span>
            {{ post.comments }} 评论
          </span>
          <span class="stat">
            <span class="icon">❤️</span>
            {{ post.likes }} 点赞
          </span>
        </div>
      </header>

      <div v-if="post.coverImage" class="post-cover">
        <img :src="post.coverImage" :alt="post.title" />
      </div>

      <div class="post-body" v-html="renderedContent"></div>

      <footer class="post-footer">
        <div class="post-tags" v-if="post.tags && post.tags.length">
          <span class="tags-label">标签:</span>
          <span v-for="tag in post.tags" :key="tag" class="tag">
            {{ tag }}
          </span>
        </div>

        <div class="post-actions">
          <button @click="likePost" :class="['like-btn', { liked: isLiked }]">
            <span class="icon">{{ isLiked ? '❤️' : '🤍' }}</span>
            {{ isLiked ? '已点赞' : '点赞' }}
          </button>
          <button @click="sharePost" class="share-btn">
            <span class="icon">📤</span>
            分享
          </button>
        </div>
      </footer>

      <!-- 评论区 -->
      <section class="comments-section">
        <h3>评论 ({{ post.comments }})</h3>

        <div v-if="isLoggedIn" class="comment-form">
          <textarea
            v-model="newComment"
            placeholder="写下你的评论..."
            rows="4"
            class="comment-input"
          ></textarea>
          <button @click="submitComment" :disabled="submittingComment" class="comment-btn">
            {{ submittingComment ? '发送中...' : '发送评论' }}
          </button>
        </div>

        <div v-else class="login-prompt">
          <p>请先登录后再发表评论</p>
          <button @click="$emit('show-login')" class="login-btn">登录</button>
        </div>

        <div v-if="comments.length === 0" class="no-comments">
          <p v-if="!isLoggedIn">暂无评论，登录后发表第一条评论吧！</p>
          <p v-else>暂无评论，来发表第一条评论吧！</p>
        </div>

        <div v-else class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author?.username || '匿名用户' }}</span>
              <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
      </section>
    </article>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { blogAPI } from '../services/api.js'

export default {
  name: 'BlogPost',
  props: {
    postId: {
      type: [String, Number],
      required: true
    },
    isLoggedIn: {
      type: Boolean,
      default: false
    }
  },
  emits: ['back', 'show-login'],
  setup(props, { emit }) {
    const post = ref(null)
    const comments = ref([])
    const loading = ref(false)
    const isLiked = ref(false)
    const newComment = ref('')
    const submittingComment = ref(false)

    const renderedContent = computed(() => {
      if (!post.value?.content) return ''

      // 简单的Markdown渲染
      let content = post.value.content

      // 粗体
      content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')

      // 斜体
      content = content.replace(/\*(.*?)\*/g, '<em>$1</em>')

      // 标题
      content = content.replace(/^### (.*$)/gm, '<h3>$1</h3>')
      content = content.replace(/^## (.*$)/gm, '<h2>$1</h2>')
      content = content.replace(/^# (.*$)/gm, '<h1>$1</h1>')

      // 列表
      content = content.replace(/^- (.*$)/gm, '<li>$1</li>')
      content = content.replace(/(<li>.*<\/li>)/s, '<ul>$1</ul>')

      // 链接
      content = content.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank">$1</a>')

      // 图片
      content = content.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1">')

      // 代码块
      content = content.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>')

      // 段落
      content = content.replace(/\n\n/g, '</p><p>')
      content = '<p>' + content + '</p>'

      return content
    })

    const fetchPost = async () => {
      try {
        loading.value = true
        const response = await blogAPI.getPost(props.postId)
        post.value = response.data.post
        await fetchComments()
      } catch (error) {
        console.error('获取文章失败:', error)
        post.value = null
      } finally {
        loading.value = false
      }
    }

    const fetchComments = async () => {
      try {
        const response = await blogAPI.getComments(props.postId)
        comments.value = response.data.comments
      } catch (error) {
        console.error('获取评论失败:', error)
      }
    }

    const likePost = async () => {
      try {
        if (isLiked.value) {
          await blogAPI.unlikePost(props.postId)
          post.value.likes--
        } else {
          await blogAPI.likePost(props.postId)
          post.value.likes++
        }
        isLiked.value = !isLiked.value
      } catch (error) {
        console.error('点赞失败:', error)
      }
    }

    const sharePost = () => {
      if (navigator.share) {
        navigator.share({
          title: post.value.title,
          text: post.value.excerpt,
          url: window.location.href
        })
      } else {
        // 复制链接到剪贴板
        navigator.clipboard.writeText(window.location.href)
        alert('链接已复制到剪贴板')
      }
    }

    const submitComment = async () => {
      if (!newComment.value.trim()) return

      try {
        submittingComment.value = true
        const response = await blogAPI.createComment(props.postId, {
          content: newComment.value
        })

        comments.value.unshift(response.data.comment)
        post.value.comments++
        newComment.value = ''
      } catch (error) {
        console.error('发送评论失败:', error)
        alert('发送评论失败，请重试')
      } finally {
        submittingComment.value = false
      }
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    const goBack = () => {
      emit('back')
    }

    onMounted(() => {
      fetchPost()
    })

    return {
      post,
      comments,
      loading,
      isLiked,
      newComment,
      submittingComment,
      renderedContent,
      likePost,
      sharePost,
      submitComment,
      formatDate,
      goBack,
      isLoggedIn: props.isLoggedIn
    }
  }
}
</script>

<style scoped>
.blog-post {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
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

.error-state {
  text-align: center;
  padding: 60px 20px;
}

.error-state h2 {
  color: #333;
  margin-bottom: 10px;
}

.error-state p {
  color: #666;
  margin-bottom: 30px;
}

.back-btn {
  background: #f8fafc;
  color: #64748b;
  border: 2px solid #e2e8f0;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.back-btn:hover {
  background: #f1f5f9;
  color: #475569;
}

.post-content {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.post-header {
  padding: 30px;
  border-bottom: 1px solid #e1e5e9;
}

.post-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.post-category {
  background: #f0f4ff;
  color: #667eea;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.post-date,
.post-author {
  color: #666;
  font-size: 14px;
}

.post-title {
  color: #333;
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 15px;
  line-height: 1.3;
}

.post-excerpt {
  color: #666;
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 20px;
}

.post-stats {
  display: flex;
  gap: 20px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  font-size: 14px;
}

.icon {
  font-size: 16px;
}

.post-cover {
  width: 100%;
  height: 300px;
  overflow: hidden;
}

.post-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.post-body {
  padding: 30px;
  line-height: 1.8;
  font-size: 16px;
  color: #333;
}

.post-body h1,
.post-body h2,
.post-body h3 {
  color: #333;
  margin-top: 30px;
  margin-bottom: 15px;
  font-weight: 600;
}

.post-body h1 {
  font-size: 28px;
}

.post-body h2 {
  font-size: 24px;
}

.post-body h3 {
  font-size: 20px;
}

.post-body p {
  margin-bottom: 20px;
}

.post-body ul,
.post-body ol {
  margin-bottom: 20px;
  padding-left: 20px;
}

.post-body li {
  margin-bottom: 8px;
}

.post-body a {
  color: #667eea;
  text-decoration: none;
}

.post-body a:hover {
  text-decoration: underline;
}

.post-body img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px 0;
}

.post-body pre {
  background: #f8fafc;
  padding: 20px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 20px 0;
  border: 1px solid #e1e5e9;
}

.post-body code {
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
}

.post-footer {
  padding: 30px;
  border-top: 1px solid #e1e5e9;
}

.post-tags {
  margin-bottom: 20px;
}

.tags-label {
  color: #666;
  font-size: 14px;
  margin-right: 10px;
}

.tag {
  background: #f0f4ff;
  color: #667eea;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  margin-right: 8px;
  margin-bottom: 8px;
  display: inline-block;
}

.post-actions {
  display: flex;
  gap: 15px;
}

.like-btn,
.share-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  color: #64748b;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.like-btn:hover,
.share-btn:hover {
  background: #f8fafc;
  color: #475569;
}

.like-btn.liked {
  background: #fef2f2;
  color: #ef4444;
  border-color: #fecaca;
}

.comments-section {
  padding: 30px;
  border-top: 1px solid #e1e5e9;
}

.comments-section h3 {
  color: #333;
  margin-bottom: 20px;
  font-size: 20px;
}

.comment-form {
  margin-bottom: 30px;
}

.comment-input {
  width: 100%;
  padding: 15px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 16px;
  font-family: inherit;
  resize: vertical;
  margin-bottom: 15px;
}

.comment-input:focus {
  outline: none;
  border-color: #667eea;
}

.comment-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.comment-btn:hover:not(:disabled) {
  background: #5a67d8;
}

.comment-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-prompt {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.login-prompt p {
  margin-bottom: 20px;
}

.login-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-btn:hover {
  background: #5a67d8;
}

.no-comments {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment {
  padding: 20px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e1e5e9;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.comment-author {
  font-weight: 600;
  color: #333;
}

.comment-date {
  color: #666;
  font-size: 14px;
}

.comment-content {
  color: #555;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .post-header {
    padding: 20px;
  }

  .post-title {
    font-size: 24px;
  }

  .post-meta {
    flex-direction: column;
    gap: 8px;
  }

  .post-stats {
    flex-wrap: wrap;
    gap: 15px;
  }

  .post-body {
    padding: 20px;
  }

  .post-footer {
    padding: 20px;
  }

  .post-actions {
    flex-direction: column;
  }

  .comments-section {
    padding: 20px;
  }
}
</style>
