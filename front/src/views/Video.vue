<!-- src/views/Video.vue -->
<template>
    <el-container class="video-container">
            <Navbar />

        <!-- 页面标题 -->
        <el-main class="main">
            <div class="content">
                <h1 class="page-title">视频列表</h1>

                <!-- 视频列表 -->
                <el-row :gutter="20">
                    <el-col v-for="video in pagedVideos" :key="video.id" :span="8">
                        <el-card class="video-card" shadow="hover" :body-style="{ padding: '10px' }">
                            <!-- 视频封面 -->
                            <img :src="video.cover" class="video-cover" @click="goToVideo(video.id)" alt="视频封面" />

                            <!-- 视频标题 -->
                            <h3 class="video-title">{{ video.title }}</h3>

                            <!-- 简要描述 -->
                            <p class="video-summary">{{ video.summary }}</p>

                            <!-- 操作按钮 -->
                            <el-button type="primary" size="small" @click="goToVideo(video.id)">
                                观看视频
                            </el-button>
                        </el-card>
                    </el-col>
                </el-row>

                <!-- 分页器 -->
                <div class="pagination">
                    <el-pagination background layout="prev, pager, next" :page-size="pageSize" :total="videos.length"
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

// 模拟视频数据（后面可替换为接口数据）
const videos = ref(Array.from({ length: 30 }, (_, i) => ({
    id: i + 1,
    title: `教学视频 ${i + 1}`,
    summary: `这是第 ${i + 1} 个视频的简介，包含主要学习内容。`,
    cover: 'https://placehold.co/300x180?text=Video+Cover'
})))

const pageSize = 9   // 每页9个视频（3列 × 3行）
const currentPage = ref(1)

const pagedVideos = computed(() => {
    const start = (currentPage.value - 1) * pageSize
    return videos.value.slice(start, start + pageSize)
})

// 跳转到视频详情
const goToVideo = (id) => {
    router.push(`/video/${id}`)
}
</script>

<style scoped>
.main {
  background-color: #ffffff;
  flex: 1;
}
.page-title {
  text-align: center;
  font-size: 28px;
  color: #2c3e50;
  margin: 30px 0;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

.page-title .video-card {
    margin-bottom: 20px;
    cursor: pointer;
    transition: transform 0.2s;
}

.video-card:hover {
    transform: translateY(-5px);
}

.video-cover {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 10px;
}

.video-title {
    font-size: 18px;
    margin: 10px 0 5px;
}

.video-summary {
    font-size: 14px;
    color: #666;
    margin-bottom: 10px;
}

.pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

.content {
    width: 60%;
    margin: 0 auto;
}
.video-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
