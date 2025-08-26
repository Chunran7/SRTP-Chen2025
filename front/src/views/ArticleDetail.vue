<template>
    <el-container class="article-detail-container">
        <!-- 顶部导航栏 -->
        <Navbar />

        <!-- 主体内容 -->
        <el-main class="main">
            <div class="content">
                <div v-if="article" class="article-title">{{ article.title }}</div>
                <div v-else class="article-title">正在加载...</div>



            </div>

        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import Navbar from '../components/Navbar.vue'
import Footer from '../components/Footer.vue'
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const article = ref(null);

const getArticleDetail = async () => {
    try {
        const id = route.params.id;
        const res = await axios.get(`/article/${id}`)
        console.log('后端返回的数据:', res)
        if (res.data.code === 0) {
            article.value = res.data.data;
        } else {
            ElMessage.error(res.data.msg);
        }
    } catch (err) {
        console.error('Failed to fetch article detail:', err);
    }


}

onMounted(() => {
    getArticleDetail()
})

</script>

<style scoped>
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

.content {
    width: 60%;
    min-height: 60vh;
    background-color: #ffffff;
    margin: 30px auto;
    border-radius: 25px;
}
</style>