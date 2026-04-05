<template>
    <el-container class="home-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <!-- 走马灯 -->
            <el-carousel height="200px">
                <el-carousel-item v-for="item in 4" :key="item">
                    <h3 class="carousel-text">轮播内容 {{ item }}</h3>
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
                                <img :src="article.firstPicture || 'https://placehold.co/300x150'" alt="封面图"
                                    class="article-img" />
                                <h3 class="article-title">{{ article.title }}</h3>
                                <p class="article-description">{{ truncate(article.description, 35) }}</p>
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

// 渲染 Markdown 内容为 HTML
const renderedContent = computed(() => {
    return marked(introduction.value.content || '')
})

const handleLogin = () => {
    router.push('/login')
}

// 摘要截断函数
const truncate = (text, maxLen) => {
    return text?.length <= maxLen ? text : text?.slice(0, maxLen) + '...'
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

</script>

<style scoped>
.home-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main {
    background-color: #ffffff;
    flex: 1;
    padding: 0px;
}

.carousel-text {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
    font-size: 24px;
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

/* 介绍我们部分 - 图片垂直居中 */
.introduction-row {
    display: flex;
    align-items: center;
}

.introduction-image-col {
    display: flex;
    align-items: center;
    justify-content: center;
}

.introduction-image {
    width: 100%;
    height: auto;
    border-radius: 6px;
    object-fit: cover;
}

.article-card {
    cursor: pointer;
    transition: transform 0.3s;
}

.article-card:hover {
    transform: translateY(-5px);
}

.article-img {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 4px;
}

.article-title {
    font-size: 16px;
    font-weight: bold;
    margin: 12px 0 8px;
    color: #2c3e50;
}

.article-description {
    font-size: 14px;
    color: #666;
    line-height: 1.5;
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
    /* 首行缩进 2 字符 */
    margin: 1.2em 0;
    /* 段前段后距离：上 1.2em，下 1.2em */
}

.introduction-content :deep(p:first-child) {
    margin-top: 0;
    /* 第一个段落无上边距 */
}

.introduction-content :deep(p:last-child) {
    margin-bottom: 0;
    /* 最后一个段落无下边距 */
}

.introduction-content :deep(h1),
.introduction-content :deep(h2),
.introduction-content :deep(h3),
.introduction-content :deep(h4),
.introduction-content :deep(h5),
.introduction-content :deep(h6) {
    margin: 1.5em 0 1em;
    /* 标题段前段后距离 */
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
    border-left: 4px solid #007bff;
    background-color: #f8f9fa;
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
</style>
