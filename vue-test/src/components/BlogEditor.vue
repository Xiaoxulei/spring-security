<template>
  <div class="blog-editor">
    <div class="editor-header">
      <button @click="goBack" class="back-btn">
        <span>←</span> 返回
      </button>
      <h1>{{ isEditing ? '编辑文章' : '写新文章' }}</h1>
      <div class="editor-actions">
        <button @click="saveDraft" :disabled="saving" class="draft-btn">
          {{ saving ? '保存中...' : '保存草稿' }}
        </button>
        <button @click="publishPost" :disabled="saving" class="publish-btn">
          {{ saving ? '发布中...' : '发布文章' }}
        </button>
      </div>
    </div>

    <div class="editor-content">
      <div class="form-group">
        <label for="title">文章标题</label>
        <input
          id="title"
          v-model="post.title"
          type="text"
          placeholder="输入文章标题..."
          class="title-input"
        />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="category">分类</label>
          <select id="category" v-model="post.category" class="category-select">
            <option value="">选择分类</option>
            <option value="技术">技术</option>
            <option value="生活">生活</option>
            <option value="随笔">随笔</option>
            <option value="教程">教程</option>
          </select>
        </div>

        <div class="form-group">
          <label for="coverImage">封面图片</label>
          <input
            id="coverImage"
            v-model="post.coverImage"
            type="url"
            placeholder="图片URL..."
            class="cover-input"
          />
        </div>
      </div>

      <div class="form-group">
        <label for="excerpt">文章摘要</label>
        <textarea
          id="excerpt"
          v-model="post.excerpt"
          placeholder="输入文章摘要..."
          rows="3"
          class="excerpt-input"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="content">文章内容</label>
        <div class="editor-toolbar">
          <button @click="insertText('**', '**')" type="button" class="toolbar-btn">粗体</button>
          <button @click="insertText('*', '*')" type="button" class="toolbar-btn">斜体</button>
          <button @click="insertText('### ', '')" type="button" class="toolbar-btn">标题</button>
          <button @click="insertText('- ', '')" type="button" class="toolbar-btn">列表</button>
          <button @click="insertText('[链接文字](', ')')" type="button" class="toolbar-btn">链接</button>
          <button @click="insertText('![图片描述](', ')')" type="button" class="toolbar-btn">图片</button>
          <button @click="insertText('```\n', '\n```')" type="button" class="toolbar-btn">代码块</button>
        </div>
        <textarea
          id="content"
          v-model="post.content"
          placeholder="开始写你的文章..."
          rows="20"
          class="content-input"
          @input="autoSave"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="tags">标签</label>
        <input
          id="tags"
          v-model="tagsInput"
          type="text"
          placeholder="输入标签，用逗号分隔..."
          class="tags-input"
          @keydown.enter="addTag"
        />
        <div class="tags-list">
          <span
            v-for="tag in post.tags"
            :key="tag"
            class="tag"
            @click="removeTag(tag)"
          >
            {{ tag }} ×
          </span>
        </div>
      </div>
    </div>

    <div v-if="showPreview" class="preview-panel">
      <div class="preview-header">
        <h3>预览</h3>
        <button @click="showPreview = false" class="close-preview">关闭预览</button>
      </div>
      <div class="preview-content" v-html="renderedContent"></div>
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { blogAPI } from '../services/api.js'

export default {
  name: 'BlogEditor',
  props: {
    postId: {
      type: [String, Number],
      default: null
    }
  },
  emits: ['back', 'saved'],
  setup(props, { emit }) {
    const post = ref({
      title: '',
      content: '',
      excerpt: '',
      category: '',
      coverImage: '',
      tags: [],
      status: 'draft'
    })

    const tagsInput = ref('')
    const saving = ref(false)
    const showPreview = ref(false)
    const errorMessage = ref('')
    const isEditing = computed(() => !!props.postId)
    const autoSaveTimer = ref(null)

    const renderedContent = computed(() => {
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
      content = content.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2">$1</a>')

      // 图片
      content = content.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1">')

      // 代码块
      content = content.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>')

      // 段落
      content = content.replace(/\n\n/g, '</p><p>')
      content = '<p>' + content + '</p>'

      return content
    })

    const insertText = (before, after) => {
      const textarea = document.getElementById('content')
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const selectedText = post.value.content.substring(start, end)

      const newText = before + selectedText + after
      post.value.content = post.value.content.substring(0, start) + newText + post.value.content.substring(end)

      // 重新设置光标位置
      setTimeout(() => {
        textarea.focus()
        textarea.setSelectionRange(start + before.length, start + before.length + selectedText.length)
      }, 0)
    }

    const addTag = () => {
      const tag = tagsInput.value.trim()
      if (tag && !post.value.tags.includes(tag)) {
        post.value.tags.push(tag)
        tagsInput.value = ''
      }
    }

    const removeTag = (tag) => {
      post.value.tags = post.value.tags.filter(t => t !== tag)
    }

    const autoSave = () => {
      if (autoSaveTimer.value) {
        clearTimeout(autoSaveTimer.value)
      }
      autoSaveTimer.value = setTimeout(() => {
        saveDraft(true) // 静默保存
      }, 3000)
    }

    const saveDraft = async (silent = false) => {
      if (!post.value.title.trim()) {
        errorMessage.value = '请输入文章标题'
        return
      }

      try {
        saving.value = true
        errorMessage.value = ''

        const postData = {
          ...post.value,
          status: 'draft'
        }

        if (isEditing.value) {
          await blogAPI.updatePost(props.postId, postData)
        } else {
          const response = await blogAPI.createPost(postData)
          emit('saved', response.data.post)
        }

        if (!silent) {
          alert('草稿已保存')
        }
      } catch (error) {
        console.error('保存失败:', error)
        errorMessage.value = '保存失败，请重试'
      } finally {
        saving.value = false
      }
    }

    const publishPost = async () => {
      if (!post.value.title.trim()) {
        errorMessage.value = '请输入文章标题'
        return
      }

      if (!post.value.content.trim()) {
        errorMessage.value = '请输入文章内容'
        return
      }

      try {
        saving.value = true
        errorMessage.value = ''

        const postData = {
          ...post.value,
          status: 'published'
        }

        if (isEditing.value) {
          await blogAPI.updatePost(props.postId, postData)
        } else {
          const response = await blogAPI.createPost(postData)
          emit('saved', response.data.post)
        }

        alert('文章发布成功！')
        goBack()
      } catch (error) {
        console.error('发布失败:', error)
        errorMessage.value = '发布失败，请重试'
      } finally {
        saving.value = false
      }
    }

    const fetchPost = async () => {
      if (!props.postId) return

      try {
        const response = await blogAPI.getPost(props.postId)
        post.value = response.data.post
      } catch (error) {
        console.error('获取文章失败:', error)
        errorMessage.value = '获取文章失败'
      }
    }

    const goBack = () => {
      emit('back')
    }

    onMounted(() => {
      if (isEditing.value) {
        fetchPost()
      }
    })

    watch(() => props.postId, () => {
      if (isEditing.value) {
        fetchPost()
      }
    })

    return {
      post,
      tagsInput,
      saving,
      showPreview,
      errorMessage,
      isEditing,
      renderedContent,
      insertText,
      addTag,
      removeTag,
      autoSave,
      saveDraft,
      publishPost,
      goBack
    }
  }
}
</script>

<style scoped>
.blog-editor {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f1f5f9;
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
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn:hover {
  background: #f1f5f9;
  color: #475569;
}

.editor-header h1 {
  color: #333;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.editor-actions {
  display: flex;
  gap: 12px;
}

.draft-btn,
.publish-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.draft-btn {
  background: #f8fafc;
  color: #64748b;
  border: 2px solid #e2e8f0;
}

.draft-btn:hover:not(:disabled) {
  background: #f1f5f9;
  color: #475569;
}

.publish-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.publish-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.draft-btn:disabled,
.publish-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.editor-content {
  display: grid;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-group label {
  color: #333;
  font-weight: 600;
  font-size: 14px;
}

.title-input,
.category-select,
.cover-input,
.excerpt-input,
.content-input,
.tags-input {
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s ease;
  font-family: inherit;
}

.title-input {
  font-size: 20px;
  font-weight: 600;
}

.content-input {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
}

.title-input:focus,
.category-select:focus,
.cover-input:focus,
.excerpt-input:focus,
.content-input:focus,
.tags-input:focus {
  outline: none;
  border-color: #667eea;
}

.editor-toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.toolbar-btn {
  background: #f8fafc;
  color: #64748b;
  border: 1px solid #e2e8f0;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toolbar-btn:hover {
  background: #f1f5f9;
  color: #475569;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.tag {
  background: #f0f4ff;
  color: #667eea;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.tag:hover {
  background: #e0e7ff;
}

.preview-panel {
  margin-top: 30px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  overflow: hidden;
}

.preview-header {
  background: #f8fafc;
  padding: 15px 20px;
  border-bottom: 2px solid #e1e5e9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.close-preview {
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  font-size: 14px;
}

.close-preview:hover {
  color: #475569;
}

.preview-content {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
  line-height: 1.6;
}

.preview-content h1,
.preview-content h2,
.preview-content h3 {
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
}

.preview-content p {
  margin-bottom: 15px;
  color: #555;
}

.preview-content ul {
  margin-bottom: 15px;
  padding-left: 20px;
}

.preview-content li {
  margin-bottom: 5px;
}

.preview-content a {
  color: #667eea;
  text-decoration: none;
}

.preview-content a:hover {
  text-decoration: underline;
}

.preview-content img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 10px 0;
}

.preview-content pre {
  background: #f8fafc;
  padding: 15px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 15px 0;
}

.preview-content code {
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
}

.error-message {
  background: #fef2f2;
  color: #dc2626;
  padding: 12px;
  border-radius: 8px;
  margin-top: 20px;
  border: 1px solid #fecaca;
}

@media (max-width: 768px) {
  .editor-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .editor-actions {
    width: 100%;
    justify-content: center;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .editor-toolbar {
    justify-content: center;
  }
}
</style>
