<template>
  <el-container class="article-container">
    <el-main class="main">
      <div class="content">
        <h1 class="section-title">文章列表</h1>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading">
          <el-skeleton :rows="5" animated />
        </div>

        <!-- 文章列表 -->
        <el-row :gutter="24" v-else>
          <el-col :xs="24" :sm="12" :md="8" v-for="article in pagedArticles" :key="article.id">
            <el-card shadow="hover" class="article-card" @click="goToArticle(article.id)">
              <div class="card-image-wrapper">
                <img :src="getArticleImage(article.firstPicture)" alt="封面图" class="article-img"
                  @error="handleImageError" />
                <div class="card-overlay">
                  <el-tag size="small" type="success">阅读更多</el-tag>
                </div>
              </div>
              <div class="card-content">
                <h3 class="article-title">{{ article.title }}</h3>
                <p class="article-description">{{ article.description || '暂无摘要' }}</p>
                <div class="card-meta">
                  <span class="meta-item">
                    <el-icon>
                      <User />
                    </el-icon>
                    {{ article.author }}
                  </span>
                  <span class="meta-item">
                    <el-icon>
                      <Clock />
                    </el-icon>
                    {{ formatDate(article.createTime) }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination background layout="prev, pager, next, jumper, total" :total="articles.length"
            :page-size="pageSize" v-model:current-page="currentPage" />
        </div>
      </div>
    </el-main>

    <Footer />
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Clock } from '@element-plus/icons-vue'
import { getArticleListService } from '@/api/article.js'
import Footer from '@/components/Footer.vue'

const router = useRouter()

// 文章数据
const articles = ref([])
// 加载状态
const loading = ref(true)

const pageSize = 9
const currentPage = ref(1)

// 获取文章列表
const getArticleList = async () => {
  try {
    loading.value = true
    const res = await getArticleListService()
    articles.value = res.data || []
  } catch (error) {
    console.error('获取文章列表失败:', error)
    articles.value = []
  } finally {
    loading.value = false
  }
}

// 当前页的文章
const pagedArticles = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return articles.value.slice(start, start + pageSize)
})

// 跳转文章详情
const goToArticle = (id) => {
  router.push(`/article/${id}`)
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

// 获取文章图片（处理空值和无效URL）
const getArticleImage = (firstPicture) => {
  if (!firstPicture || firstPicture.trim() === '') {
    return '/images/placeholder-400x220.svg'
  }
  return firstPicture
}

// 图片加载失败时的fallback处理
const handleImageError = (e) => {
  e.target.src = '/images/placeholder-400x220.svg'
}

// 当从缓存中激活组件时调用
import { onActivated } from 'vue'
onActivated(() => {
  // 可以选择在每次返回页面时刷新数据，或者注释掉下面这行以保持数据不变
  // getArticleList()
})

onMounted(() => {
  getArticleList()
})
</script>

<style scoped>
.article-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
}

.main {
  flex: 1;
  padding: 0;
}

.content {
  width: 80%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
}

.section-title {
  text-align: center;
  font-size: 32px;
  color: #2c3e50;
  margin: 0 0 40px 0;
  font-weight: 700;
  position: relative;
  padding-bottom: 15px;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  border-radius: 2px;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
  background: #fff;
}

.article-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(64, 158, 255, 0.2);
}

.card-image-wrapper {
  position: relative;
  overflow: hidden;
}

.article-img {
  width: 100%;
  height: 220px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.article-card:hover .article-img {
  transform: scale(1.05);
}

.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.article-card:hover .card-overlay {
  opacity: 1;
}

.card-content {
  padding: 20px;
}

.article-title {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 12px;
  color: #2c3e50;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 50px;
}

.article-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  margin: 0 0 16px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 75px;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
  font-size: 13px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-item .el-icon {
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px 0;
}

.loading {
  padding: 40px 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .content {
    width: 90%;
  }
}

@media (max-width: 768px) {
  .content {
    width: 95%;
    padding: 20px 10px;
  }

  .section-title {
    font-size: 26px;
  }

  .article-img {
    height: 180px;
  }

  .article-title {
    font-size: 16px;
    min-height: 44px;
  }

  .article-description {
    min-height: 63px;
  }
}
</style>