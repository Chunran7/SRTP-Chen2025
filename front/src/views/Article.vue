<template>
  <el-container class="article-container">
    <Navbar />

    <el-main class="main">
      <div class="content">
        <h1 class="section-title">文章列表</h1>

        <!-- 文章列表 -->

        <el-row :gutter="24">
          <el-col :span="8" v-for="article in pagedArticles" :key="article.id">
            <el-card shadow="hover" class="article-card" @click="goToArticle(article.id)">
              <img :src="article.img" alt="封面图" class="article-img" />
              <h3 class="article-title">{{ article.title }}</h3>
              <p class="article-summary">{{ truncate(article.summary, 100) }}</p>
              <div class="article-footer">
                <el-button type="primary" size="small">阅读更多</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination background layout="prev, pager, next" :total="articles.length" :page-size="pageSize"
            v-model:current-page="currentPage" />
        </div>
      </div>
    </el-main>

    <Footer />
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/Navbar.vue'
import Footer from '@/components/Footer.vue'

const router = useRouter()

// 所有文章数据
const articles = ref(Array.from({ length: 42 }, (_, i) => ({
  id: i + 1,
  title: `文章标题 ${i + 1}`,
  summary: `这是第 ${i + 1} 篇文章的摘要内容，简要描述文章主要内容，让用户快速了解文章。`,
  img: 'https://placehold.co/300x150'
})))

const pageSize = 9
const currentPage = ref(1)

// 当前页的文章
const pagedArticles = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return articles.value.slice(start, start + pageSize)
})

// 跳转文章详情
const goToArticle = (id) => {
  router.push(`/article/${id}`)
}

// 摘要截断
const truncate = (text, maxLen) => {
  return text.length <= maxLen ? text : text.slice(0, maxLen) + '...'
}
</script>

<style scoped>
.article-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  background-color: #ffffff;
  flex: 1;
}

.content {
  width: 60%;
  margin: 0 auto;
}

.section-title {
  text-align: center;
  font-size: 28px;
  color: #2c3e50;
  margin: 30px 0;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-card:hover {
  box-shadow: 0 6px 20px rgba(0, 123, 255, 0.2);
}

.article-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.article-title {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.article-summary {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.article-footer {
  text-align: right;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
