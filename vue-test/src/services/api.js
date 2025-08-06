import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080', // 替换为您的后端API地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // token过期或无效，清除本地存储并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      // 可以在这里添加路由跳转逻辑
      // router.push('/login')
    }
    return Promise.reject(error)
  }
)

// 用户相关API
export const userAPI = {
  // 登录
  // login: (credentials) => {
  //   // 测试账号
  //   const testAccounts = {
  //     'test': '123456',
  //     'admin': 'admin123',
  //     'demo': 'demo123'
  //   }

  //   // 检查是否是测试账号
  //   if (testAccounts[credentials.username] && testAccounts[credentials.username] === credentials.password) {
  //     // 模拟成功响应
  //     return Promise.resolve({
  //       data: {
  //         success: true,
  //         message: '登录成功',
  //         token: 'test-token-' + Date.now(),
  //         user: {
  //           id: 1,
  //           username: credentials.username,
  //           email: credentials.username + '@test.com',
  //           avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + credentials.username,
  //           role: 'user'
  //         }
  //       }
  //     })
  //   }

  //   // 如果不是测试账号，返回错误
  //   return Promise.reject({
  //     response: {
  //       data: {
  //         success: false,
  //         message: '用户名或密码错误，请使用测试账号：test/123456、admin/admin123、demo/demo123'
  //       }
  //     }
  //   })
  // },
  //登录
  login: (credentials) => {
    return api.post('/api/login', credentials)
  },

  // 注册
  register: (userData) => {
    return api.post('/api/register', userData)
  },

  // 获取用户信息
  getUserInfo: () => {
    return api.get('/api/user/profile')
  },

  // 更新用户信息
  updateUserInfo: (userData) => {
    return api.put('/api/user/profile', userData)
  },

  // 修改密码
  changePassword: (passwordData) => {
    return api.put('/api/user/password', passwordData)
  },

  // 忘记密码
  forgotPassword: (email) => {
    return api.post('/api/forgot-password', { email })
  },

  // 重置密码
  resetPassword: (token, newPassword) => {
    return api.post('/api/reset-password', { token, newPassword })
  },

  // 登出
  logout: () => {
    return api.post('/api/logout')
  }
}

// 博客相关API
export const blogAPI = {
  // 获取文章列表
  getPosts: (params) => {
    // 模拟文章数据
    const mockPosts = [
      {
        id: 1,
        title: '欢迎来到我的博客',
        excerpt: '这是我的第一篇博客文章，欢迎大家的阅读和评论！',
        content: '# 欢迎来到我的博客\n\n这是我的第一篇博客文章，欢迎大家的阅读和评论！\n\n## 功能特性\n\n- 📝 富文本编辑器\n- 🏷️ 分类和标签\n- 💬 评论系统\n- ❤️ 点赞功能\n\n希望这个博客系统能给大家带来良好的体验！',
        category: '随笔',
        tags: ['欢迎', '博客'],
        coverImage: 'https://picsum.photos/400/200?random=1',
        author: {
          id: 1,
          username: 'test'
        },
        views: 128,
        likes: 15,
        comments: 3,
        status: 'published',
        createdAt: '2024-01-15T10:00:00.000Z',
        updatedAt: '2024-01-15T10:00:00.000Z'
      },
      {
        id: 2,
        title: 'Vue 3 开发技巧分享',
        excerpt: '分享一些在 Vue 3 开发中的实用技巧和最佳实践。',
        content: '# Vue 3 开发技巧分享\n\n## Composition API\n\nVue 3 的 Composition API 提供了更好的逻辑复用能力。\n\n```javascript\nimport { ref, computed } from \'vue\'\n\nconst count = ref(0)\nconst doubleCount = computed(() => count.value * 2)\n```\n\n## 响应式系统\n\nVue 3 的响应式系统更加高效和灵活。',
        category: '技术',
        tags: ['Vue', 'JavaScript', '前端'],
        coverImage: 'https://picsum.photos/400/200?random=2',
        author: {
          id: 1,
          username: 'test'
        },
        views: 256,
        likes: 28,
        comments: 7,
        status: 'published',
        createdAt: '2024-01-14T15:30:00.000Z',
        updatedAt: '2024-01-14T15:30:00.000Z'
      },
      {
        id: 3,
        title: '我的生活感悟',
        excerpt: '记录一些生活中的小感悟和思考。',
        content: '# 我的生活感悟\n\n生活中总有一些值得记录的时刻，无论是开心还是难过，都是人生的一部分。\n\n## 关于成长\n\n每一次的挫折都是成长的机会，每一次的成功都是努力的回报。\n\n保持积极的心态，相信明天会更好！',
        category: '生活',
        tags: ['感悟', '生活'],
        coverImage: 'https://picsum.photos/400/200?random=3',
        author: {
          id: 1,
          username: 'test'
        },
        views: 89,
        likes: 12,
        comments: 2,
        status: 'published',
        createdAt: '2024-01-13T09:15:00.000Z',
        updatedAt: '2024-01-13T09:15:00.000Z'
      }
    ]

    return Promise.resolve({
      data: {
        success: true,
        posts: mockPosts,
        total: mockPosts.length,
        page: params?.page || 1,
        limit: params?.limit || 10
      }
    })
  },

  // 获取单篇文章
  getPost: (id) => {
    const mockPosts = [
      {
        id: 1,
        title: '欢迎来到我的博客',
        excerpt: '这是我的第一篇博客文章，欢迎大家的阅读和评论！',
        content: '# 欢迎来到我的博客\n\n这是我的第一篇博客文章，欢迎大家的阅读和评论！\n\n## 功能特性\n\n- 📝 富文本编辑器\n- 🏷️ 分类和标签\n- 💬 评论系统\n- ❤️ 点赞功能\n\n希望这个博客系统能给大家带来良好的体验！',
        category: '随笔',
        tags: ['欢迎', '博客'],
        coverImage: 'https://picsum.photos/400/200?random=1',
        author: {
          id: 1,
          username: 'test'
        },
        views: 128,
        likes: 15,
        comments: 3,
        status: 'published',
        createdAt: '2024-01-15T10:00:00.000Z',
        updatedAt: '2024-01-15T10:00:00.000Z'
      },
      {
        id: 2,
        title: 'Vue 3 开发技巧分享',
        excerpt: '分享一些在 Vue 3 开发中的实用技巧和最佳实践。',
        content: '# Vue 3 开发技巧分享\n\n## Composition API\n\nVue 3 的 Composition API 提供了更好的逻辑复用能力。\n\n```javascript\nimport { ref, computed } from \'vue\'\n\nconst count = ref(0)\nconst doubleCount = computed(() => count.value * 2)\n```\n\n## 响应式系统\n\nVue 3 的响应式系统更加高效和灵活。',
        category: '技术',
        tags: ['Vue', 'JavaScript', '前端'],
        coverImage: 'https://picsum.photos/400/200?random=2',
        author: {
          id: 1,
          username: 'test'
        },
        views: 256,
        likes: 28,
        comments: 7,
        status: 'published',
        createdAt: '2024-01-14T15:30:00.000Z',
        updatedAt: '2024-01-14T15:30:00.000Z'
      },
      {
        id: 3,
        title: '我的生活感悟',
        excerpt: '记录一些生活中的小感悟和思考。',
        content: '# 我的生活感悟\n\n生活中总有一些值得记录的时刻，无论是开心还是难过，都是人生的一部分。\n\n## 关于成长\n\n每一次的挫折都是成长的机会，每一次的成功都是努力的回报。\n\n保持积极的心态，相信明天会更好！',
        category: '生活',
        tags: ['感悟', '生活'],
        coverImage: 'https://picsum.photos/400/200?random=3',
        author: {
          id: 1,
          username: 'test'
        },
        views: 89,
        likes: 12,
        comments: 2,
        status: 'published',
        createdAt: '2024-01-13T09:15:00.000Z',
        updatedAt: '2024-01-13T09:15:00.000Z'
      }
    ]

    const post = mockPosts.find(p => p.id == id)
    if (post) {
      return Promise.resolve({
        data: {
          success: true,
          post: post
        }
      })
    } else {
      return Promise.reject({
        response: {
          data: {
            success: false,
            message: '文章不存在'
          }
        }
      })
    }
  },

  // 创建文章
  createPost: (data) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '文章创建成功',
        post: {
          id: Date.now(),
          ...data,
          author: {
            id: 1,
            username: 'test'
          },
          views: 0,
          likes: 0,
          comments: 0,
          status: 'published',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
      }
    })
  },

  // 更新文章
  updatePost: (id, data) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '文章更新成功'
      }
    })
  },

  // 删除文章
  deletePost: (id) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '文章删除成功'
      }
    })
  },

  // 点赞文章
  likePost: (id) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '点赞成功'
      }
    })
  },

  // 取消点赞
  unlikePost: (id) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '取消点赞成功'
      }
    })
  },

  // 获取评论列表
  getComments: (postId) => {
    const mockComments = [
      {
        id: 1,
        content: '写得很好，学习了！',
        author: {
          id: 2,
          username: 'visitor1'
        },
        createdAt: '2024-01-15T11:00:00.000Z'
      },
      {
        id: 2,
        content: '感谢分享，很有帮助',
        author: {
          id: 3,
          username: 'visitor2'
        },
        createdAt: '2024-01-15T12:30:00.000Z'
      }
    ]

    return Promise.resolve({
      data: {
        success: true,
        comments: mockComments
      }
    })
  },

  // 创建评论
  createComment: (postId, data) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '评论发布成功',
        comment: {
          id: Date.now(),
          content: data.content,
          author: {
            id: 1,
            username: 'test'
          },
          createdAt: new Date().toISOString()
        }
      }
    })
  },

  // 删除评论
  deleteComment: (postId, commentId) => {
    return Promise.resolve({
      data: {
        success: true,
        message: '评论删除成功'
      }
    })
  }
}

// 其他API可以在这里添加
export const otherAPI = {
  // 示例：获取数据列表
  getDataList: (params) => {
    return api.get('/api/data', { params })
  },

  // 示例：创建数据
  createData: (data) => {
    return api.post('/api/data', data)
  },

  // 示例：更新数据
  updateData: (id, data) => {
    return api.put(`/api/data/${id}`, data)
  },

  // 示例：删除数据
  deleteData: (id) => {
    return api.delete(`/api/data/${id}`)
  }
}

export default api
