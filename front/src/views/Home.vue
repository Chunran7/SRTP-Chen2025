<template>
    <el-container class="home-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <!-- 走马灯 -->
            <el-carousel height="600px" indicator-position="outside">
                <el-carousel-item v-for="item in carouselItems" :key="item.id">
                    <div class="carousel-item">
                        <img :src="item.image" :alt="item.title" class="carousel-image" />
                        <div class="carousel-overlay">
                            <h3 class="carousel-title">{{ item.title }}</h3>
                        </div>
                    </div>
                </el-carousel-item>
            </el-carousel>

            <div class="content">
                <!-- 介绍我们 -->
                <section class="introduction">
                    <h1 class="section-title">{{ introduction.title }}</h1>
                    <el-row gutter="40" class="introduction-row">
                        <el-col :span="12" class="introduction-image-col">
                            <img :src="introduction.imageUrl || '/images/introduction.png'" alt="图片描述"
                                class="introduction-image" />
                        </el-col>
                        <el-col :span="12">
                            <div class="introduction-content" v-html="renderedContent"></div>
                        </el-col>
                    </el-row>
                </section>

                <!-- 最新文章 -->
                <section class="articles">
                    <h1 class="section-title">最新文章</h1>
                    <div v-if="loading" class="loading">
                        <el-skeleton :rows="3" animated />
                    </div>
                    <el-row :gutter="24" v-else>
                        <el-col :span="8" v-for="article in articles" :key="article.id">
                            <el-card shadow="hover" class="article-card" @click="goToArticle(article.id)">
                                <div class="card-image-wrapper">
                                    <img :src="getArticleImage(article.firstPicture)" alt="封面图"
                                        class="article-img" @error="handleImageError" />
                                    <div class="card-badge">
                                        <el-tag size="small" type="primary">最新</el-tag>
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
                </section>

            </div>
        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { User, Clock } from '@element-plus/icons-vue'
import Footer from '@/components/Footer.vue'
import { getArticleLatestService } from '@/api/article.js'
import { getIntroductionService } from '@/api/admin.js'
import { marked } from 'marked'

const activeIndex = ref('1')
const isLoggedIn = ref(false)
const router = useRouter()
const articles = ref([])
const loading = ref(true)
const introduction = ref({
    title: '介绍我们',
    content: '介绍我们的基本内容',
    imageUrl: '/images/introduction.png'
})

// 轮播图数据
// 使用 Unsplash 免费图片（通过 source.unsplash.com 动态返回匹配关键词的高质量图片）
const carouselItems = ref([
    { id: 1, title: '关注医务人员心理健康', image: '/images/pic1.png' },
    { id: 2, title: '职业丧痛研究与支持', image: '/images/pic2.png' },
])

// 渲染 Markdown 内容为 HTML
const renderedContent = computed(() => {
    return marked(introduction.value.content || '')
})

const handleLogin = () => {
    router.push('/login')
}

// 格式化日期
const formatDate = (dateStr) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

const getArticleLatest = async () => {
    try {
        loading.value = true
        const res = await getArticleLatestService(6)
        articles.value = res.data || []
    } catch (error) {
        console.error('获取最新文章失败:', error)
        articles.value = []
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    getArticleLatest()
    getIntroduction()
})

const getIntroduction = async () => {
    try {
        const res = await getIntroductionService()
        // res 是 {code: 0, data: {...}}，需要访问 res.data 获取实际数据
        introduction.value = res.data
    } catch (error) {
        console.error('获取介绍信息失败:', error)
    }
}

const goToArticle = (id) => {
    router.push(`/article/${id}`)
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

</script>

<style scoped>
.home-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main {
    background-color: #f8f9fa;
    flex: 1;
    padding: 0;
}

/* 轮播图样式 */
.carousel-item {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
}

.carousel-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.carousel-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
    padding: 30px 20px 20px;
}

.carousel-title {
    color: #fff;
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.content {
    width: 75%;
    max-width: 1400px;
    margin: 0 auto;
    padding: 40px 20px;
}

.section-title {
    text-align: center;
    font-size: 32px;
    color: #2c3e50;
    margin: 40px 0 30px;
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

/* 介绍我们部分 - 图片垂直居中 */
.introduction-row {
    display: flex;
    align-items: center;
    margin-bottom: 60px;
}

.introduction-image-col {
    display: flex;
    align-items: center;
    justify-content: center;
}

.introduction-image {
    width: 100%;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    transition: transform 0.3s ease;
}

.introduction-image:hover {
    transform: scale(1.02);
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

.card-badge {
    position: absolute;
    top: 12px;
    right: 12px;
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

/* 介绍内容样式 */
.introduction-content {
    font-size: 16px;
    line-height: 1.8;
    color: #333;
    text-align: justify;
}

.introduction-content :deep(p) {
    text-indent: 2em;
    margin: 1.2em 0;
}

.introduction-content :deep(p:first-child) {
    margin-top: 0;
}

.introduction-content :deep(p:last-child) {
    margin-bottom: 0;
}

.introduction-content :deep(h1),
.introduction-content :deep(h2),
.introduction-content :deep(h3),
.introduction-content :deep(h4),
.introduction-content :deep(h5),
.introduction-content :deep(h6) {
    margin: 1.5em 0 1em;
    color: #2c3e50;
}

.introduction-content :deep(ul),
.introduction-content :deep(ol) {
    margin: 1em 0;
    padding-left: 2em;
}

.introduction-content :deep(li) {
    margin: 0.5em 0;
}

.introduction-content :deep(blockquote) {
    margin: 1em 0;
    padding: 10px 20px;
    border-left: 4px solid #409eff;
    background-color: #f0f7ff;
    color: #666;
}

.introduction-content :deep(code) {
    background-color: #f8f9fa;
    padding: 2px 6px;
    border-radius: 3px;
    font-family: 'Consolas', 'Monaco', monospace;
}

.introduction-content :deep(pre) {
    background-color: #f8f9fa;
    padding: 15px;
    border-radius: 5px;
    overflow-x: auto;
    margin: 1em 0;
}

.introduction-content :deep(img) {
    max-width: 100%;
    height: auto;
    border-radius: 5px;
    margin: 1em 0;
}

.loading {
    padding: 40px 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
    .content {
        width: 85%;
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

    .introduction-row {
        flex-direction: column;
    }

    .introduction-image-col {
        margin-bottom: 20px;
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
