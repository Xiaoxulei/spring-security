<template>
  <aside class="sidebar">
    <div class="profile-card">
      <div class="avatar-container">
        <img
          :src="profile.avatar"
          :alt="profile.name"
          class="avatar"
        />
      </div>

      <div class="profile-info">
        <h3 class="name">{{ profile.name }}</h3>
        <p class="bio">{{ profile.bio }}</p>
      </div>

      <div class="social-links">
        <a
          v-for="link in profile.socialLinks"
          :key="link.name"
          :href="link.url"
          :title="link.name"
          target="_blank"
          rel="noopener noreferrer"
          class="social-link"
        >
          <span class="social-icon">{{ link.icon }}</span>
        </a>
      </div>

      <div class="stats">
        <div class="stat-item">
          <span class="stat-number">{{ profile.stats.posts }}</span>
          <span class="stat-label">文章</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ profile.stats.views }}</span>
          <span class="stat-label">阅读</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ profile.stats.likes }}</span>
          <span class="stat-label">点赞</span>
        </div>
      </div>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'Sidebar',
  data() {
    return {
      profile: {
        name: 'xuxiaolei',
        bio: '热爱技术，分享生活，记录成长',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=blogger',
        socialLinks: [
          {
            name: 'GitHub',
            url: 'https://github.com',
            icon: '🐙'
          },
          {
            name: '微博',
            url: 'https://weibo.com',
            icon: '📱'
          },
          {
            name: '知乎',
            url: 'https://zhihu.com',
            icon: '💡'
          },
          {
            name: '邮箱',
            url: 'mailto:blogger@example.com',
            icon: '📧'
          }
        ],
        stats: {
          posts: 25,
          views: 1234,
          likes: 567
        }
      }
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  right: 10px;
  top: 100px;
  width: 240px;
  height: calc(100vh - 140px);
  background: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(18px) saturate(160%);
  border-left: 1.5px solid rgba(200, 220, 255, 0.25);
  box-shadow: 0 8px 32px rgba(79,140,255,0.08);
  z-index: 100;
  overflow-y: auto;
  border-radius: 24px;
}

.profile-card {
  padding: 2rem 1.5rem;
  text-align: center;
}

.avatar-container {
  margin-bottom: 1.5rem;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid rgba(79,140,255,0.2);
  box-shadow: 0 8px 24px rgba(79,140,255,0.15);
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.profile-info {
  margin-bottom: 2rem;
}

.name {
  font-size: 1.5rem;
  font-weight: 700;
  color: #4f8cff;
  margin-bottom: 0.5rem;
  background: linear-gradient(90deg,#4f8cff,#92e0ff);
  -webkit-background-clip: text;
  color: transparent;
}

.bio {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
  margin: 0;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: rgba(79,140,255,0.1);
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.3s ease;
  border: 1px solid rgba(79,140,255,0.2);
}

.social-link:hover {
  background: rgba(79,140,255,0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(79,140,255,0.2);
}

.social-icon {
  font-size: 1.5rem;
}

.stats {
  display: flex;
  justify-content: space-around;
  padding: 1.5rem 0;
  border-top: 1px solid rgba(79,140,255,0.1);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
}

.stat-number {
  font-size: 1.25rem;
  font-weight: 700;
  color: #4f8cff;
}

.stat-label {
  font-size: 0.8rem;
  color: #666;
}

@media (max-width: 1024px) {
  .sidebar {
    position: relative;
    width: 100%;
    height: auto;
    margin-bottom: 2rem;
    margin: 20px;
    border-radius: 24px;
    top: 0;
  }

  .profile-card {
    padding: 1.5rem;
  }

  .stats {
    justify-content: center;
    gap: 2rem;
  }
}
</style>
