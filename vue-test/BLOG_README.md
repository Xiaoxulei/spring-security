# Vue 博客系统

这是一个基于 Vue 3 + Vite 的现代化博客系统，包含完整的文章管理、编辑和展示功能。

## 功能特性

- 📝 富文本文章编辑器
- 📚 文章列表展示和搜索
- 🏷️ 文章分类和标签管理
- 💬 评论系统
- ❤️ 点赞功能
- 📱 响应式设计
- 🔍 文章搜索和筛选
- 🖼️ 封面图片支持
- 📊 阅读统计

## 项目结构

```
src/
├── components/
│   ├── LoginForm.vue          # 登录表单
│   ├── BlogList.vue           # 博客列表
│   ├── BlogEditor.vue         # 文章编辑器
│   └── BlogPost.vue           # 文章详情页
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

### 文章管理接口

#### 获取文章列表
- **URL**: `GET /api/posts`
- **参数**:
  - `page`: 页码 (默认: 1)
  - `limit`: 每页数量 (默认: 10)
  - `category`: 分类筛选
  - `search`: 搜索关键词
- **响应**:
```json
{
  "success": true,
  "posts": [
    {
      "id": 1,
      "title": "文章标题",
      "excerpt": "文章摘要",
      "content": "文章内容",
      "category": "技术",
      "tags": ["Vue", "JavaScript"],
      "coverImage": "https://example.com/image.jpg",
      "author": {
        "id": 1,
        "username": "作者名"
      },
      "views": 100,
      "likes": 25,
      "comments": 10,
      "status": "published",
      "createdAt": "2024-01-01T00:00:00.000Z",
      "updatedAt": "2024-01-01T00:00:00.000Z"
    }
  ],
  "total": 100,
  "page": 1,
  "limit": 10
}
```

#### 获取单篇文章
- **URL**: `GET /api/posts/:id`
- **响应**:
```json
{
  "success": true,
  "post": {
    "id": 1,
    "title": "文章标题",
    "excerpt": "文章摘要",
    "content": "文章内容",
    "category": "技术",
    "tags": ["Vue", "JavaScript"],
    "coverImage": "https://example.com/image.jpg",
    "author": {
      "id": 1,
      "username": "作者名"
    },
    "views": 100,
    "likes": 25,
    "comments": 10,
    "status": "published",
    "createdAt": "2024-01-01T00:00:00.000Z",
    "updatedAt": "2024-01-01T00:00:00.000Z"
  }
}
```

#### 创建文章
- **URL**: `POST /api/posts`
- **Headers**: `Authorization: Bearer <token>`
- **请求体**:
```json
{
  "title": "文章标题",
  "excerpt": "文章摘要",
  "content": "文章内容",
  "category": "技术",
  "tags": ["Vue", "JavaScript"],
  "coverImage": "https://example.com/image.jpg",
  "status": "draft"
}
```

#### 更新文章
- **URL**: `PUT /api/posts/:id`
- **Headers**: `Authorization: Bearer <token>`
- **请求体**: 同创建文章

#### 删除文章
- **URL**: `DELETE /api/posts/:id`
- **Headers**: `Authorization: Bearer <token>`

### 点赞功能

#### 点赞文章
- **URL**: `POST /api/posts/:id/like`
- **Headers**: `Authorization: Bearer <token>`

#### 取消点赞
- **URL**: `DELETE /api/posts/:id/like`
- **Headers**: `Authorization: Bearer <token>`

### 评论功能

#### 获取评论列表
- **URL**: `GET /api/posts/:id/comments`
- **参数**:
  - `page`: 页码
  - `limit`: 每页数量

#### 创建评论
- **URL**: `POST /api/posts/:id/comments`
- **Headers**: `Authorization: Bearer <token>`
- **请求体**:
```json
{
  "content": "评论内容"
}
```

#### 删除评论
- **URL**: `DELETE /api/posts/:id/comments/:commentId`
- **Headers**: `Authorization: Bearer <token>`

## 数据库表结构

### 文章表 (posts)
```sql
CREATE TABLE posts (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  excerpt TEXT,
  content LONGTEXT NOT NULL,
  category VARCHAR(50),
  tags JSON,
  cover_image VARCHAR(255),
  author_id INT NOT NULL,
  views INT DEFAULT 0,
  likes INT DEFAULT 0,
  comments INT DEFAULT 0,
  status ENUM('draft', 'published') DEFAULT 'draft',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES users(id)
);
```

### 点赞表 (likes)
```sql
CREATE TABLE likes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY unique_like (user_id, post_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);
```

### 评论表 (comments)
```sql
CREATE TABLE comments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  content TEXT NOT NULL,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);
```

## 使用说明

### 1. 文章管理

1. **创建文章**：
   - 点击"写文章"按钮
   - 填写标题、分类、摘要
   - 使用富文本编辑器编写内容
   - 添加标签和封面图片
   - 保存草稿或发布文章

2. **编辑文章**：
   - 在文章列表中点击"编辑"按钮
   - 修改文章内容
   - 保存更改

3. **删除文章**：
   - 在文章列表中点击"删除"按钮
   - 确认删除操作

### 2. 文章展示

1. **文章列表**：
   - 显示所有文章卡片
   - 支持搜索和分类筛选
   - 分页加载更多文章

2. **文章详情**：
   - 完整显示文章内容
   - 支持点赞和分享
   - 评论功能

### 3. 编辑器功能

- **Markdown支持**：支持基本的Markdown语法
- **工具栏**：快速插入格式
- **自动保存**：每3秒自动保存草稿
- **预览功能**：实时预览文章效果

## 技术特性

### 前端技术栈
- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **HTTP客户端**: Axios
- **样式**: CSS3 + Flexbox/Grid
- **状态管理**: Vue Reactive

### 编辑器功能
- 富文本编辑
- Markdown语法支持
- 实时预览
- 自动保存
- 图片上传
- 标签管理

### 响应式设计
- 移动端适配
- 平板端优化
- 桌面端体验
- 触摸友好

## 自定义配置

### 修改API地址
编辑 `src/services/api.js`：
```javascript
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000',
  // ...
})
```

### 添加新分类
编辑 `src/components/BlogList.vue` 和 `src/components/BlogEditor.vue`：
```javascript
<option value="新分类">新分类</option>
```

### 自定义样式
修改各组件中的 `<style scoped>` 部分。

## 开发建议

### 1. 性能优化
- 图片懒加载
- 虚拟滚动
- 代码分割
- 缓存策略

### 2. 用户体验
- 加载状态提示
- 错误处理
- 操作反馈
- 键盘快捷键

### 3. 安全性
- 输入验证
- XSS防护
- CSRF保护
- 权限控制

## 故障排除

### 常见问题

1. **编辑器无法加载**
   - 检查网络连接
   - 确认API服务正常

2. **图片上传失败**
   - 检查文件大小限制
   - 确认文件格式支持

3. **评论无法发送**
   - 检查登录状态
   - 确认网络连接

## 扩展功能

### 可添加的功能
- 文章归档
- 标签云
- 相关文章推荐
- 文章分享
- 订阅功能
- 多语言支持
- 主题切换
- 数据统计

## 许可证

MIT License 