<template>
    <el-container class="home-container">
        <!-- 顶部导航栏 -->
        <Navbar />

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
                    <h1 class="section-title">介绍我们</h1>
                    <el-row gutter="40">
                        <el-col :span="12">
                            <img src="https://placehold.co/400x200" alt="图片描述"
                                style="width: 100%; height: auto; border-radius: 6px;" />
                        </el-col>
                        <el-col :span="12">
                            <p>
                                介绍我们的基本内容
                            </p>
                        </el-col>
                    </el-row>
                </section>

                <!-- 最新文章 -->
                <section class="articles">
                    <h1 class="section-title">最新文章</h1>
                    <el-row :gutter="20">
                        <el-col :span="8" v-for="(article, index) in articles" :key="index">
                            <el-card :body-style="{ padding: '20px' }" shadow="hover">
                                <img :src="article.img" class="article-img" alt="文章封面" />
                                <h3 style="margin: 10px 0;">{{ article.title }}</h3>
                                <p class="article-summary">{{ article.summary }}</p>
                                <el-button type="primary" size="small" @click="goToArticle(article.id)">阅读更多</el-button>
                            </el-card>
                        </el-col>
                    </el-row>
                </section>

                <!-- 视频课堂模块 -->
                <section class="videos">
                    <h1 class="section-title">视频课堂</h1>
                    <el-row :gutter="20">
                        <el-col :span="6" v-for="(video, index) in videos" :key="index">
                            <el-card :body-style="{ padding: '10px' }" shadow="hover">
                                <iframe :src="video.url" frameborder="0" allowfullscreen
                                    style="width: 100%; height: 200px; border-radius: 6px;"></iframe>
                                <h3 style="margin: 10px 0;">{{ video.title }}</h3>
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
import Navbar from '@/components/Navbar.vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Footer from '@/components/Footer.vue'

const activeIndex = ref('1')
const isLoggedIn = ref(false)
const router = useRouter()

const handleLogin = () => {
    router.push('/login')
}

const articles = ref([
    {
        id: 1,
        title: '如何调节焦虑情绪',
        summary: '焦虑是现代人常见的问题，本文介绍几种有效的情绪调节方法...',
        img: 'https://placehold.co/300x150'
    },
    {
        id: 2,
        title: '情绪急救包的使用指南',
        summary: '面对突发情绪崩溃，我们可以准备一个“心理急救包”来帮助自己...',
        img: 'https://placehold.co/300x150'
    },
    {
        id: 3,
        title: '倾听的力量：做一个好倾听者',
        summary: '在朋友倾诉时，如何成为一个有力的支持者？掌握同理心倾听技巧...',
        img: 'https://placehold.co/300x150'
    }
])

const videos = ref([
    { title: '如何进行情绪自我疏导', url: 'https://www.youtube.com/embed/tgbNymZ7vqY' },
    { title: '职场压力管理技巧', url: 'https://www.youtube.com/embed/tgbNymZ7vqY' },
    { title: '亲子沟通的方法', url: 'https://www.youtube.com/embed/dQw4w9WgXcQ' },
    { title: '高效时间管理', url: 'https://www.youtube.com/embed/tgbNymZ7vqY' },
    { title: '自尊与自我接纳', url: 'https://www.youtube.com/embed/dQw4w9WgXcQ' },
    { title: '沟通中的非暴力表达', url: 'https://www.youtube.com/embed/tgbNymZ7vqY' },
    { title: '认识你的压力信号', url: 'https://www.youtube.com/embed/dQw4w9WgXcQ' },
    { title: '睡眠与心理健康', url: 'https://www.youtube.com/embed/tgbNymZ7vqY' }
])


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

.article-img {
    width: 100%;
    height: 150px;
    object-fit: cover;
    border-radius: 4px;
    margin-bottom: 10px;
}

.article-summary {
    font-size: 14px;
    color: #666;
    margin-bottom: 12px;
}
</style>
