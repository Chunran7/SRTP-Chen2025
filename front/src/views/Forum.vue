<template>
    <div class="forum-page container">
        <aside class="forum-aside">
            <el-button class="btn-post" @click="openPostDialog">发布主题</el-button>
            <div class="aside-list">
                <div class="aside-item active">
                    <span>全部主题</span>
                </div>
                <div class="aside-item">
                    <i class="icon">📑</i>
                    <span>丧痛支援</span>
                </div>
                <div class="aside-item">
                    <i class="icon">📢</i>
                    <span>官方消息</span>
                </div>
            </div>
        </aside>

        <main class="forum-main">
            <div class="toolbar">
                <div class="tabs">
                    <el-button class="tab" :class="{ active: sortType === 'reply' }"
                        @click="changeSort('reply')">最多回复</el-button>
                    <el-button class="tab" :class="{ active: sortType === 'publish' }"
                        @click="changeSort('publish')">最新发布</el-button>
                </div>
            </div>

            <ul class="post-list">
                <li v-for="(post, idx) in posts" :key="idx" class="post-item">
                    <div class="post-avatar">
                        <img :src="post.avatar || defaultAvatar" alt="" />
                    </div>
                    <div class="post-body">
                        <div class="post-header">
                            <h3 class="post-title">
                                {{ post.title }}
                            </h3>

                        </div>
                        <p class="post-excerpt">{{ post.excerpt }}</p>
                    </div>
                    <div class="post-right">
                        <div class="replies">{{ post.replyCount }}</div>
                        <div class="publish-time">{{ formatTime(post.createTime) }}</div>
                    </div>
                </li>
            </ul>
        </main>

        <aside class="forum-right">
            <div class="active-card">
                <div class="card-title">当月活跃排行</div>
                <ul>
                    <li v-for="(user, i) in activeUsers" :key="i">
                        <div class="user-dot" :style="{ backgroundImage: 'url(' + user.avatar + ')' }"></div>
                        <div class="user-name">{{ user.name }}</div>
                        <div class="user-count">{{ user.count }}</div>
                    </li>
                </ul>
            </div>
        </aside>
    </div>

    <!-- 发布主题对话框 -->
    <el-dialog v-model="postDialogVisible" title="发布新主题" width="500">
        <el-form :model="postForm" label-width="80px">
            <el-form-item label="标题">
                <el-input v-model="postForm.title" placeholder="请输入帖子标题" />
            </el-form-item>
            <el-form-item label="内容">
                <el-input v-model="postForm.content" type="textarea" :rows="6" placeholder="请输入帖子内容" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="postDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitPost">发布</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllpostsService } from '@/api/post.js'
import defaultAvatar from '@/assets/default.png'

const postDialogVisible = ref(false)
const sortType = ref('reply') // 默认按最新回复排序

const postForm = reactive({
    title: '',
    content: ''
})

const posts = ref([])
const activeUsers = ref([
    { avatar: defaultAvatar, name: '不愿透露姓名的小A', count: 15 },
    { avatar: defaultAvatar, name: '急诊科-李护士', count: 12 },
    { avatar: defaultAvatar, name: 'ICU-王主任', count: 9 },
    { avatar: defaultAvatar, name: '我要努力工作', count: 7 },
    { avatar: defaultAvatar, name: '外科-刘医生', count: 5 },
])

const openPostDialog = () => {
    postDialogVisible.value = true
    postForm.title = ''
    postForm.content = ''
}

// 获取帖子列表
const loadPosts = async () => {
    try {
        let sortBy = 'reply_count' // 默认按回复数排序
        let order = 'DESC'

        if (sortType.value === 'publish') {
            sortBy = 'create_time' // 按创建时间排序
        }

        console.log('排序依据:', sortBy);

        const res = await getAllpostsService({
            page: 1,
            pageSize: 20,
            sortBy: sortBy,
            order: order
        })

        // 处理返回的数据，适配前端展示
        posts.value = res.data.map(post => ({
            ...post,
            excerpt: post.content.length > 100 ? post.content.substring(0, 100) + '...' : post.content
        }))
    } catch (error) {
        console.error('获取帖子失败:', error)
        ElMessage.error('获取帖子失败')
    }
}

// 切换排序方式
const changeSort = (type) => {
    sortType.value = type
    loadPosts()
}

const submitPost = () => {
    if (!postForm.title || !postForm.content) {
        ElMessage.warning('请填写标题和内容')
        return
    }

    // 添加新帖子到列表顶部
    posts.value.unshift({
        avatar: defaultAvatar,
        title: postForm.title,
        content: postForm.content,
        excerpt: postForm.content.length > 100
            ? postForm.content.substring(0, 100) + '...'
            : postForm.content,
        replies: 0,
    })

    // 关闭对话框
    postDialogVisible.value = false
    ElMessage.success('发布成功')
}

// 组件挂载时加载数据
onMounted(() => {
    loadPosts()
})

// 格式化时间显示
const formatTime = (time) => {
    if (!time) return ''
    const date = new Date(time)
    const now = new Date()
    const diff = now - date

    // 如果是当天发布的，显示小时数
    if (diff < 24 * 60 * 60 * 1000 && now.getDate() === date.getDate()) {
        const hours = Math.floor(diff / (60 * 60 * 1000))
        if (hours > 0) {
            return `${hours}小时前`
        } else {
            const minutes = Math.floor(diff / (60 * 1000))
            return `${minutes}分钟前`
        }
    }

    // 如果是当年发布的，显示月日
    if (now.getFullYear() === date.getFullYear()) {
        return `${date.getMonth() + 1}-${date.getDate()}`
    }

    // 跨年的显示年月日
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

</script>

<style lang="scss" scoped>
.forum-page {
    display: flex;
    gap: 24px;
    padding: 24px 40px;
    box-sizing: border-box;
}

.forum-aside {
    width: 200px;
    background: transparent;
}

.btn-post {
    display: block;
    width: 100%;
    background: #007bff;
    color: #fff;
    border: none;
    padding: 12px;
    border-radius: 6px;
    font-weight: 600;
    cursor: pointer;
    margin-bottom: 18px;
    transition: all 0.18s ease;
}

.btn-post:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(0, 123, 255, 0.12)
}

.aside-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.aside-item {
    display: flex;
    align-items: center;
    gap: 10px;
    color: #5b6b7a;
    padding: 8px 6px;
    border-radius: 6px;
}

.aside-item.active {
    background: #e9f4ff;
    color: #0b63d6;
    box-shadow: 0 1px 0 rgba(11, 99, 214, 0.06);
}

.forum-main {
    flex: 1;
    background: transparent;
}

.toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
}

.tabs .tab {
    background: transparent;
    border: 1px solid transparent;
    padding: 8px 12px;
    border-radius: 6px;
    cursor: pointer;
    color: #2f4656;
}

.tabs .tab.active {
    background: #f0f7ff;
    border-color: rgba(0, 123, 255, 0.12);
    color: #0b63d6;
}

.post-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.post-item {
    display: flex;
    align-items: flex-start;
    gap: 14px;
    background: #fff;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 12px;
    box-shadow: 0 1px 0 rgba(0, 0, 0, 0.04);
    transition: box-shadow 0.18s ease, transform 0.18s ease;
}

.post-item:hover {
    box-shadow: 0 10px 30px rgba(0, 123, 255, 0.06);
    transform: translateY(-3px)
}

.post-avatar img {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    object-fit: cover;
}

.post-body {
    flex: 1;
}

.post-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 8px
}

.post-title {
    margin: 0;
    font-size: 16px;
    color: #233642;
    font-weight: 600
}

.post-excerpt {
    margin: 8px 0 0;
    color: #6b7884
}

.pin {
    color: #007bff;
    margin-right: 6px
}

.post-right {
    width: 90px;
    text-align: center;
    color: #6b7884
}


.post-right .replies {
    background: #e6f0ff;
    color: #007bff;
    padding: 6px 5px;
    border-radius: 14px;
    font-weight: 600;
    margin-bottom: 8px;
}

.post-right .publish-time {
    color: #505050fe;
    padding: 4px;
}


.forum-right {
    width: 240px;
}

.active-card {
    background: #fff;
    padding: 12px;
    border-radius: 8px;
    box-shadow: 0 1px 0 rgba(0, 0, 0, 0.04)
}

.card-title {
    font-weight: 700;
    margin-bottom: 8px;
    color: #233642
}

.active-card ul {
    list-style: none;
    padding: 0;
    margin: 0
}

.active-card li {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    border-top: 1px solid #f3f8ff
}

.user-dot {
    width: 36px;
    height: 36px;
    background-size: cover;
    background-position: center;
    border-radius: 50%
}

.user-name {
    flex: 1;
    font-weight: 600;
    color: #22333b
}

.user-count {
    color: #0b63d6
}

/* responsive */
@media (max-width: 900px) {
    .forum-page {
        padding: 12px;
        gap: 12px
    }

    .forum-aside {
        display: none
    }

    .forum-right {
        display: none
    }
}
</style>