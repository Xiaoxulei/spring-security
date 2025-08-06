<template>
  <div class="login-container">
    <div class="login-card">
      <button class="login-close-btn" @click="$emit('close')">×</button>
      <div class="login-header">
        <h2>欢迎登录</h2>
        <p>请输入您的账号信息</p>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            id="username"
            v-model="formData.username"
            type="text"
            placeholder="请输入用户名"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            required
            :disabled="loading"
          />
        </div>

        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>

        <button
          type="submit"
          class="login-button"
          :disabled="loading"
        >
          <span v-if="loading">登录中...</span>
          <span v-else>登录</span>
        </button>
      </form>

      <div class="login-footer">
        <a href="#" @click.prevent="forgotPassword">忘记密码？</a>
        <a href="#" @click.prevent="register">注册账号</a>
      </div>
    </div>
  </div>
</template>

<script>
import { userAPI } from '../services/api.js'

export default {
  name: 'LoginForm',
  data() {
    return {
      formData: {
        username: '',
        password: ''
      },
      loading: false,
      errorMessage: ''
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const response = await userAPI.login({
          username: this.formData.username,
          password: this.formData.password
        })

        if (response.data.success) {
          // 登录成功，保存token
          localStorage.setItem('token', response.data.token)
          localStorage.setItem('user', JSON.stringify(response.data.user))

          // 触发登录成功事件
          this.$emit('login-success', response.data)

          // 可以在这里进行路由跳转
          // this.$router.push('/dashboard')
        } else {
          this.errorMessage = response.data.message || '登录失败'
        }
      } catch (error) {
        console.error('登录错误:', error)
        if (error.response) {
          this.errorMessage = error.response.data.message || '登录失败，请检查网络连接'
        } else {
          this.errorMessage = '网络错误，请稍后重试'
        }
      } finally {
        this.loading = false
      }
    },

    forgotPassword() {
      // 处理忘记密码
      console.log('忘记密码')
    },

    register() {
      // 处理注册
      console.log('注册账号')
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
}

.login-close-btn {
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
.login-close-btn:hover {
  color: #92e0ff;
}

.login-card {
  background: rgba(255,255,255,0.55);
  border-radius: 32px;
  box-shadow: 0 8px 32px rgba(79,140,255,0.18);
  padding: 32px 28px 24px 28px;
  width: 400px;
  max-width: 96vw;
  backdrop-filter: blur(18px) saturate(160%);
  border: 1.5px solid rgba(200, 220, 255, 0.25);
  position: relative;
  transition: box-shadow 0.2s;
}
.login-card:hover {
  box-shadow: 0 16px 64px rgba(79,140,255,0.22);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  font-size: 1.7rem;
  font-weight: 800;
  letter-spacing: 2px;
  margin-bottom: 0.2rem;
  background: linear-gradient(90deg,#4f8cff,#92e0ff);
  -webkit-background-clip: text;
  color: transparent;
}
.login-header p {
  color: #4f8cff;
  margin-bottom: 0.8rem;
  font-size: 1rem;
}

.login-form {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 0;
}

.form-group {
  margin-bottom: 14px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
}

.form-group input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.error-message {
  background-color: #fee;
  color: #c53030;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 14px;
  border: 1px solid #fecaca;
}

.login-button {
  width: 100%;
  margin: 0 auto;
  display: block;
  padding: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.login-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e1e5e9;
}

.login-footer a {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.login-footer a:hover {
  color: #5a67d8;
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }

  .login-footer {
    flex-direction: column;
    gap: 10px;
    text-align: center;
  }
}
</style>
