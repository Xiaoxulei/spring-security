# Vue 登录系统

这是一个基于 Vue 3 + Vite 的现代化登录界面，包含完整的用户认证功能。

## 功能特性

- 🎨 现代化的UI设计，响应式布局
- 🔐 完整的用户认证流程
- 📱 移动端适配
- 🛡️ 安全的token管理
- ⚡ 基于axios的HTTP请求
- 🔄 自动错误处理和重试机制

## 项目结构

```
src/
├── components/
│   └── LoginForm.vue          # 登录表单组件
├── services/
│   └── api.js                 # API服务层
├── App.vue                    # 主应用组件
└── main.js                    # 应用入口
```

## 安装和运行

1. 安装依赖：
```bash
npm install
```

2. 启动开发服务器：
```bash
npm run dev
```

3. 构建生产版本：
```bash
npm run build
```

## 后端API接口

系统需要以下后端API接口：

### 登录接口
- **URL**: `POST /api/login`
- **请求体**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```
- **响应**:
```json
{
  "success": true,
  "token": "jwt_token_here",
  "user": {
    "id": 1,
    "username": "用户名",
    "email": "user@example.com"
  },
  "message": "登录成功"
}
```

### 注册接口
- **URL**: `POST /api/register`
- **请求体**:
```json
{
  "username": "用户名",
  "email": "邮箱",
  "password": "密码"
}
```

### 获取用户信息
- **URL**: `GET /api/user/profile`
- **Headers**: `Authorization: Bearer <token>`

### 登出接口
- **URL**: `POST /api/logout`
- **Headers**: `Authorization: Bearer <token>`

## 配置说明

### API地址配置

在项目根目录创建 `.env` 文件：

```env
# API配置
VITE_API_URL=http://localhost:3000

# 其他配置
VITE_APP_TITLE=Vue登录系统
```

### 环境变量

- `VITE_API_URL`: 后端API服务器地址
- `VITE_APP_TITLE`: 应用标题

## 使用说明

### 1. 登录流程

1. 用户输入用户名和密码
2. 点击登录按钮
3. 系统发送POST请求到 `/api/login`
4. 登录成功后保存token到localStorage
5. 跳转到主界面

### 2. 认证机制

- 使用JWT token进行身份验证
- token自动添加到请求头
- 401错误时自动清除token并跳转登录页

### 3. 错误处理

- 网络错误自动重试
- 友好的错误提示
- 登录状态管理

## 自定义配置

### 修改API地址

编辑 `src/services/api.js` 文件中的 `baseURL`：

```javascript
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000',
  // ...
})
```

### 修改登录表单

编辑 `src/components/LoginForm.vue` 文件：

- 修改表单字段
- 自定义验证规则
- 调整UI样式

### 添加新功能

1. 在 `src/services/api.js` 中添加新的API方法
2. 在组件中调用API方法
3. 处理响应和错误

## 开发建议

1. **安全性**：
   - 使用HTTPS
   - 实现CSRF保护
   - 密码加密传输

2. **用户体验**：
   - 添加加载状态
   - 表单验证
   - 错误提示

3. **性能优化**：
   - 代码分割
   - 图片优化
   - 缓存策略

## 故障排除

### 常见问题

1. **API连接失败**
   - 检查后端服务是否启动
   - 确认API地址配置正确
   - 检查网络连接

2. **CORS错误**
   - 后端需要配置CORS
   - 允许前端域名访问

3. **Token无效**
   - 检查token格式
   - 确认token未过期
   - 重新登录获取新token

## 技术栈

- **前端框架**: Vue 3
- **构建工具**: Vite
- **HTTP客户端**: Axios
- **样式**: CSS3 + Flexbox/Grid
- **状态管理**: Vue Composition API

## 许可证

MIT License 