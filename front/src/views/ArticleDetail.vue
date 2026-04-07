<template>
    <el-container class="article-detail-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <div class="main-container">
                <!-- 文章存在时显示内容 -->
                <div v-if="article" class="content">
                    <div class="article-header">
                        <div class="article-title">{{ article.title }}</div>
                        <div class="article-summary" v-if="article.description">
                            <el-alert
                                :title="article.description"
                                type="info"
                                :closable="false"
                                show-icon
                            />
                        </div>
                    </div>
                    <div class="article-info">
                        <span class="article-author">
                            <el-icon><User /></el-icon>
                            作者: {{ article.author }}
                        </span>
                        <span class="article-time">
                            <el-icon><Clock /></el-icon>
                            更新时间: {{ formatDate(article.createTime) }}
                        </span>
                        <span class="article-views" v-if="article.views">
                            <el-icon><View /></el-icon>
                            阅读: {{ article.views }}
                        </span>
                    </div>
                    <div class="article-content markdown-body" v-html="renderedContent"></div>
                </div>
                
                <!-- 文章不存在时显示错误提示 -->
                <div v-else-if="loadError" class="error-container">
                    <el-result
                        icon="warning"
                        title="文章不存在"
                        sub-title="抱歉，您访问的文章不存在或已被删除"
                    >
                        <template #extra>
                            <el-button type="primary" @click="goBack">返回文章列表</el-button>
                        </template>
                    </el-result>
                </div>
                
                <!-- 加载中状态 -->
                <div v-else class="loading-container">
                    <el-skeleton :rows="10" animated />
                </div>
            </div>
        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import Footer from '../components/Footer.vue'
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { marked } from 'marked';
import { computed } from 'vue';
import { User, Clock, View } from '@element-plus/icons-vue';
import { getArticleByIdService } from '@/api/article.js'; // 导入文章详情服务函数


const route = useRoute();
const router = useRouter();
const article = ref(null);
const loadError = ref(false);

const renderedContent = computed(() => {
    if (article.value && article.value.content) {
        return marked(article.value.content);
    }
    return '错误';
});

// 格式化日期
const formatDate = (dateStr) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

const getArticleDetail = async () => {
    try {
        const id = route.params.id;
        // 使用API服务函数替代直接调用request
        const res = await getArticleByIdService(id);
        console.log('后端返回的数据:', res)
        
        if (res.code === 0) {
            console.log('文章数据:', res.data); // 添加这行来查看具体字段
            article.value = res.data;
        } else {
            // 文章不存在或已被删除
            loadError.value = true;
        }
    } catch (err) {
        console.error('Failed to fetch article detail:', err);
        loadError.value = true;
    }
}

// 返回文章列表
const goBack = () => {
    router.push('/article');
}

onMounted(() => {
    getArticleDetail()
})

</script>

<style>
@import '@/assets/styles/markdown-theme.css';
</style>

<style scoped>
/* 引入外部公共样式 */

.article-detail-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main {
    border: 10cm;
    background-color: #f3f3f3;
    flex: 1;
    padding: 20px 0;
}

.main-container {
    width: 60%;
    min-height: 60vh;
    background-color: #ffffff;
    margin: 30px auto;
    border-radius: 25px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* 错误提示容器样式 */
.error-container {
    padding: 80px 20px;
    text-align: center;
}

/* 加载骨架屏容器样式 */
.loading-container {
    padding: 50px;
}

.article-title {
    text-align: center;
    font-size: 32px;
    padding: 50px 0 15px 0;
    font-weight: 700;
    color: #1a202e;
    letter-spacing: 1px;
}

.article-summary {
    max-width: 800px;
    margin: 20px auto;
}

.article-summary :deep(.el-alert) {
    background-color: #f0f7ff;
    border: 1px solid #d9ecff;
}

.article-info {
    text-align: center;
    color: #666;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 2px solid #f0f0f0;
    display: flex;
    justify-content: center;
    gap: 30px;
    flex-wrap: wrap;
}

.article-author,
.article-time,
.article-views {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 15px;
}

.article-author .el-icon,
.article-time .el-icon,
.article-views .el-icon {
    font-size: 16px;
    color: #409eff;
}

.article-content {
    padding: 20px 50px 80px 50px;
    width: 85%;
    margin: 0 auto;
}

/* ========== 响应式设计 ========== */
@media (max-width: 1200px) {
    .main-container {
        width: 75%;
    }

    .article-content {
        width: 90%;
        padding: 20px 30px 60px 30px;
    }
}

@media (max-width: 768px) {
    .main-container {
        width: 90%;
        margin: 20px auto;
    }

    .article-title {
        font-size: 26px;
        padding: 30px 0 10px 0;
    }

    .article-content {
        width: 95%;
        padding: 15px 20px 40px 20px;
        font-size: 16px;
    }
}

p {
    margin: 0;
}
</style>