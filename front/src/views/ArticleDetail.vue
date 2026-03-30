<template>
    <el-container class="article-detail-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <div class="main-container">
                <div v-if="article" class="content">
                    <div class="article-title">{{ article.title }}</div>
                    <div class="article-info">
                        <span class="article-author">作者: {{ article.author }}</span>
                        <span class="article-time">更新时间: {{ article.createTime }}</span>
                    </div>
                    <div class="article-content" v-html="renderedContent"></div>
                </div>
                <div v-else class="article-title">正在加载...</div>
            </div>
        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import Footer from '../components/Footer.vue'
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { marked } from 'marked';
import { computed } from 'vue';
import { getArticleByIdService } from '@/api/article.js'; // 导入文章详情服务函数


const route = useRoute();
const article = ref(null);

const renderedContent = computed(() => {
    if (article.value && article.value.content) {
        return marked(article.value.content);
    }
    return '错误';
});

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
            ElMessage.error(res.msg);
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

.main-container {
    width: 60%;
    min-height: 60vh;
    background-color: #ffffff;
    margin: 30px auto;
    border-radius: 25px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.article-title {
    text-align: center;
    font-size: 32px;
    padding: 50px 0 15px 0;
    font-weight: 700;
    color: #1a202e;
    letter-spacing: 1px;
}

.article-info {
    text-align: center;
    color: #666;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 2px solid #f0f0f0;
}

.article-author {
    margin-right: 25px;
    font-size: 15px;
}

.article-time {
    margin-left: 25px;
    font-size: 15px;
}

.article-content {
    padding: 20px 50px 80px 50px;
    line-height: 1.9;
    font-size: 17px;
    width: 85%;
    text-align: justify;
    text-align-last: left;
    margin: 0 auto;
    color: #2c3e50;
}

/* ========== 标题样式 ========== */
.article-content :deep(h1) {
    font-size: 2.2em;
    margin: 1.5em 0 0.8em 0;
    font-weight: 700;
    color: #1a202e;
    padding-bottom: 0.5em;
    border-bottom: 3px solid #409eff;
    letter-spacing: 1px;
}

.article-content :deep(h2) {
    font-size: 1.8em;
    margin: 1.3em 0 0.7em 0;
    font-weight: 600;
    color: #2c3e50;
    padding-left: 15px;
    border-left: 5px solid #409eff;
    background: linear-gradient(to right, #f0f7ff, transparent);
    padding-top: 5px;
    padding-bottom: 5px;
}

.article-content :deep(h3) {
    font-size: 1.5em;
    margin: 1.2em 0 0.6em 0;
    font-weight: 600;
    color: #34495e;
    position: relative;
    padding-left: 20px;
}

.article-content :deep(h3)::before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 8px;
    height: 8px;
    background-color: #409eff;
    border-radius: 50%;
}

.article-content :deep(h4),
.article-content :deep(h5),
.article-content :deep(h6) {
    font-size: 1.3em;
    margin: 1em 0 0.5em 0;
    font-weight: 600;
    color: #34495e;
}

/* ========== 段落样式 ========== */
.article-content :deep(p) {
    margin: 1.2em 0;
    text-indent: 2em;
    line-height: 2;
}

/* ========== 引用块样式 ========== */
.article-content :deep(blockquote) {
    margin: 1.5em 0;
    padding: 15px 20px;
    background: linear-gradient(135deg, #f6f8fa 0%, #e9ecef 100%);
    border-left: 5px solid #409eff;
    border-radius: 8px;
    color: #555;
    font-style: italic;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    position: relative;
}

.article-content :deep(blockquote)::before {
    content: '"';
    position: absolute;
    top: -10px;
    left: 10px;
    font-size: 60px;
    color: #409eff;
    opacity: 0.2;
    font-family: Georgia, serif;
}

.article-content :deep(blockquote p) {
    text-indent: 0;
    margin: 0.5em 0;
}

/* ========== 代码块样式 ========== */
.article-content :deep(code) {
    padding: 3px 8px;
    background-color: #f6f8fa;
    border-radius: 4px;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
    font-size: 0.9em;
    color: #e83e8c;
    border: 1px solid #eaeaea;
}

.article-content :deep(pre) {
    margin: 1.5em 0;
    padding: 20px;
    background: linear-gradient(145deg, #1e1e1e 0%, #2d2d2d 100%);
    border-radius: 8px;
    overflow-x: auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    border: 1px solid #404040;
    position: relative;
}

.article-content :deep(pre::before) {
    content: 'Code';
    position: absolute;
    top: 0;
    right: 10px;
    background: #409eff;
    color: white;
    padding: 2px 10px;
    border-radius: 0 0 4px 4px;
    font-size: 12px;
    font-weight: 600;
}

.article-content :deep(pre code) {
    padding: 0;
    margin: 0;
    background: none;
    border: none;
    color: #d4d4d4;
    font-size: 14px;
    line-height: 1.6;
}

/* ========== 链接样式 ========== */
.article-content :deep(a) {
    color: #409eff;
    text-decoration: none;
    padding: 2px 5px;
    border-bottom: 1px dashed #409eff;
    transition: all 0.3s ease;
    position: relative;
}

.article-content :deep(a:hover) {
    color: #66b1ff;
    background-color: #f0f7ff;
    border-bottom-style: solid;
}

/* ========== 列表样式 ========== */
.article-content :deep(ul),
.article-content :deep(ol) {
    margin: 1.2em 0;
    padding-left: 2.5em;
}

.article-content :deep(li) {
    margin: 0.6em 0;
    line-height: 1.8;
}

/* 无序列表 */
.article-content :deep(ul > li) {
    list-style-type: none;
    position: relative;
}

.article-content :deep(ul > li::before) {
    content: '•';
    position: absolute;
    left: -1.5em;
    color: #409eff;
    font-size: 1.2em;
    font-weight: bold;
}

/* 有序列表 */
.article-content :deep(ol > li) {
    position: relative;
}

/* 嵌套列表 */
.article-content :deep(ul ul),
.article-content :deep(ul ol),
.article-content :deep(ol ul),
.article-content :deep(ol ol) {
    margin: 0.5em 0;
}

/* ========== 表格样式 ========== */
.article-content :deep(table) {
    width: 100%;
    margin: 1.5em 0;
    border-collapse: collapse;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.article-content :deep(table th) {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    color: white;
    padding: 14px 16px;
    text-align: left;
    font-weight: 600;
    border: none;
    font-size: 16px;
}

.article-content :deep(table td) {
    padding: 12px 16px;
    border-bottom: 1px solid #e8e8e8;
    background-color: #fff;
    transition: background-color 0.2s;
}

.article-content :deep(table tr:hover td) {
    background-color: #f5f7fa;
}

.article-content :deep(table tr:nth-child(even) td) {
    background-color: #fafafa;
}

.article-content :deep(table tr:nth-child(even):hover td) {
    background-color: #f0f7ff;
}

/* ========== 图片样式 ========== */
.article-content :deep(img) {
    max-width: 100%;
    height: auto;
    display: block;
    margin: 1.5em auto;
    border-radius: 10px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
    border: 3px solid #fff;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.article-content :deep(img:hover) {
    transform: scale(1.02);
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.16);
}

/* ========== 分割线样式 ========== */
.article-content :deep(hr) {
    height: 3px;
    margin: 2em 0;
    background: linear-gradient(to right, #409eff 0%, #66b1ff 50%, #409eff 100%);
    border: none;
    border-radius: 2px;
}

/* ========== 强调文本 ========== */
.article-content :deep(strong),
.article-content :deep(b) {
    color: #303133;
    font-weight: 700;
}

.article-content :deep(em),
.article-content :deep(i) {
    color: #e83e8c;
    font-style: italic;
}

/* ========== 删除线 ========== */
.article-content :deep(del),
.article-content :deep(s) {
    color: #999;
    text-decoration: line-through;
}

/* ========== 行内元素优化 ========== */
.article-content :deep(span) {
    color: inherit;
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

    .article-content :deep(h1) {
        font-size: 1.8em;
    }

    .article-content :deep(h2) {
        font-size: 1.5em;
    }

    .article-content :deep(h3) {
        font-size: 1.3em;
    }
}

p {
    margin: 0;
}
</style>