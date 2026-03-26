<template>
    <div class="admin-container">
        <!-- 登录页面 -->
        <div v-if="!isLoggedIn" class="login-wrapper">
            <!-- 动态背景装饰 -->
            <div class="login-bg-shape shape-1"></div>
            <div class="login-bg-shape shape-2"></div>
            <div class="login-bg-shape shape-3"></div>
            <div class="login-bg-shape shape-4"></div>

            <el-card class="login-box" shadow="hover">
                <div class="login-header">
                    <div class="login-logo-container">
                        <img src="@/assets/logo.png" alt="Logo" class="login-logo" />
                    </div>
                    <h2 class="login-title">后台管理系统</h2>
                    <p class="login-subtitle">安全登录，高效管理</p>
                </div>

                <el-form :model="loginForm" ref="loginFormRef" label-position="top" class="login-form">
                    <el-form-item label="管理员账号" prop="username" class="form-item">
                        <el-input v-model="loginForm.username" placeholder="请输入管理员账号" :prefix-icon="User"
                            class="login-input" />
                    </el-form-item>

                    <el-form-item label="密码" prop="password" class="form-item">
                        <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock"
                            show-password @keyup.enter="handleLogin" class="login-input" />
                    </el-form-item>

                    <div class="form-footer">
                        <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
                            立即登录
                        </el-button>
                    </div>

                    <!-- 开发模式快速入口 -->
                    <el-divider style="margin: 20px 0;">开发调试</el-divider>
                    <el-button type="info" @click="handleDevLogin" class="dev-login-btn">
                        🚀 一键进入后台（开发模式）
                    </el-button>
                    <el-alert title="提示" type="info" :closable="false" class="dev-tip">
                        仅用于本地开发调试，生产环境请正常登录
                    </el-alert>
                </el-form>
            </el-card>
        </div>

        <!-- 管理后台主界面 -->
        <el-container v-else class="main-layout">
            <el-aside width="220px" class="sidebar">
                <div class="brand">
                    <span>ADMIN PANEL</span>
                </div>
                <el-menu :default-active="activeMenu" @select="handleMenuSelect" background-color="#304156"
                    text-color="#bfcbd9" active-text-color="#409EFF">
                    <el-menu-item index="dashboard">
                        <el-icon>
                            <Odometer />
                        </el-icon>
                        <span>数据看板</span>
                    </el-menu-item>

                    <el-menu-item index="publish">
                        <el-icon>
                            <Document />
                        </el-icon>
                        <span>发布文章</span>
                    </el-menu-item>

                    <el-menu-item index="article_mgr">
                        <el-icon>
                            <Setting />
                        </el-icon>
                        <span>文章管理</span>
                    </el-menu-item>

                    <el-menu-item index="logout">
                        <el-icon>
                            <SwitchButton />
                        </el-icon>
                        <span>退出登录</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <el-container>
                <el-header class="admin-header">
                    <div class="header-left">当前位置：{{ getMenuName(activeMenu) }}</div>
                    <div class="header-right">管理员：{{ loginForm.username || 'Admin' }}</div>
                </el-header>

                <el-main class="main-content">
                    <!-- 仪表盘 -->
                    <div v-if="activeMenu === 'dashboard'">
                        <el-row :gutter="20">
                            <el-col :span="12">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>总用户数</template>
                                    <div class="data-num">{{ stats.userCount ?? '-' }}</div>
                                </el-card>
                            </el-col>
                            <el-col :span="12">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>文章总数</template>
                                    <div class="data-num">{{ stats.articleCount ?? '-' }}</div>
                                </el-card>
                            </el-col>
                        </el-row>
                        <div style="margin-top: 20px">
                            <el-alert title="欢迎回来，管理员！系统运行正常。" type="success" :closable="false" />
                        </div>
                    </div>

                    <!-- 发布文章 -->
                    <div v-if="activeMenu === 'publish'">
                        <el-card>
                            <template #header>发布新文章</template>
                            <el-form :model="articleForm" label-width="80px">
                                <el-form-item label="标题">
                                    <el-input v-model="articleForm.title" />
                                </el-form-item>
                                <el-form-item label="作者">
                                    <el-input v-model="articleForm.author" />
                                </el-form-item>
                                <el-form-item label="封面">
                                    <el-input v-model="articleForm.firstPicture" placeholder="图片 URL" />
                                </el-form-item>
                                <el-form-item label="摘要">
                                    <el-input type="textarea" v-model="articleForm.description" />
                                </el-form-item>
                                <el-form-item label="正文">
                                    <el-input type="textarea" v-model="articleForm.content" rows="10" />
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="handleArticleSubmit" :loading="loading">
                                        发布
                                    </el-button>
                                </el-form-item>
                            </el-form>
                        </el-card>
                    </div>

                    <!-- 文章管理 -->
                    <div v-if="activeMenu === 'article_mgr'">
                        <el-card>
                            <template #header>文章管理</template>
                            <el-row style="margin-bottom: 12px">
                                <el-col :span="6">
                                    <el-input v-model="articleQuery.keyword" placeholder="搜索标题或摘要" clearable
                                        @keyup.enter="fetchAdminArticles" />
                                </el-col>
                                <el-col :span="4">
                                    <el-button type="primary" @click="fetchAdminArticles">搜索</el-button>
                                </el-col>
                                <el-col :span="6" style="text-align: right">
                                    <el-checkbox v-model="articleQuery.includeDeleted" @change="fetchAdminArticles">
                                        包含已删除
                                    </el-checkbox>
                                </el-col>
                            </el-row>
                            <el-table :data="adminArticles" stripe style="width: 100%">
                                <el-table-column prop="id" label="ID" width="80" />
                                <el-table-column prop="title" label="标题" />
                                <el-table-column prop="author" label="作者" width="140" />
                                <el-table-column prop="views" label="浏览" width="100" />
                                <el-table-column prop="likeCount" label="点赞" width="100" />
                                <el-table-column label="状态" width="120">
                                    <template #default="{ row }">
                                        <el-tag :type="row.isDeleted === 1 ? 'danger' : 'success'">
                                            {{ row.isDeleted === 1 ? '已删除' : '正常' }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作" width="260">
                                    <template #default="{ row }">
                                        <el-button size="small" @click="viewArticle(row)">查看</el-button>
                                        <template v-if="row.isDeleted === 1">
                                            <el-button size="small" type="success" @click="toggleArticleDelete(row)">
                                                恢复
                                            </el-button>
                                            <el-button size="small" type="danger" @click="hardDeleteArticle(row)">
                                                彻底删除
                                            </el-button>
                                        </template>
                                        <template v-else>
                                            <el-button size="small" type="warning" @click="toggleArticleDelete(row)">
                                                删除
                                            </el-button>
                                        </template>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div style="margin-top: 12px; text-align: right">
                                <el-pagination background layout="prev, pager, next" :total="adminTotal"
                                    :page-size="articleQuery.pageSize" :current-page="articleQuery.page"
                                    @current-change="fetchAdminArticles" />
                            </div>
                        </el-card>
                    </div>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Lock, Odometer, Document, Setting, SwitchButton } from '@element-plus/icons-vue'
import request from '@/utils/request.js'
import {
    adminGetArticleList,
    adminGetArticleById,
    adminSoftDeleteArticle,
    adminRestoreArticle,
    adminHardDeleteArticle
} from '@/api/admin.js'

// ================= 状态定义 =================
const isLoggedIn = ref(
    sessionStorage.getItem('admin_logged_in') === 'true' &&
    !!localStorage.getItem('admin_token')
)

const loading = ref(false)
const activeMenu = ref('dashboard')
const loginForm = reactive({ username: '', password: '' })

const stats = ref({ userCount: 0, articleCount: 0 })

// 管理文章相关
const articleQuery = reactive({ page: 1, pageSize: 10, keyword: '', includeDeleted: false })
const adminArticles = ref([])
const adminTotal = ref(0)

const fetchAdminArticles = async () => {
    try {
        const res = await adminGetArticleList({
            page: articleQuery.page,
            pageSize: articleQuery.pageSize,
            keyword: articleQuery.keyword,
            includeDeleted: articleQuery.includeDeleted ? 1 : 0
        })
        adminArticles.value = Array.isArray(res) ? res : []
        adminTotal.value = adminArticles.value.length
    } catch (e) {
        ElMessage.error(e?.message || '获取文章列表失败')
    }
}

const viewArticle = async (row) => {
    try {
        const a = await adminGetArticleById(row.id)
        ElMessageBox.alert(a.content || a.description || '无内容', a.title || '文章预览', {
            dangerouslyUseHTMLString: false
        })
    } catch (e) { }
}

const toggleArticleDelete = async (row) => {
    try {
        if (row.isDeleted === 1) {
            await adminRestoreArticle(row.id)
            ElMessage.success('已恢复')
        } else {
            await ElMessageBox.confirm('确认删除这篇文章吗？', '删除确认', { type: 'warning' })
            await adminSoftDeleteArticle(row.id)
            ElMessage.success('已删除')
        }
        fetchAdminArticles()
    } catch (e) { }
}

const hardDeleteArticle = async (row) => {
    try {
        await ElMessageBox.confirm(
            '彻底删除将永久移除文章，无法恢复，确定继续吗？',
            '危险操作',
            { type: 'warning' }
        )
        await adminHardDeleteArticle(row.id)
        ElMessage.success('已彻底删除')
        fetchAdminArticles()
    } catch (e) { }
}

const articleForm = ref({
    title: '',
    firstPicture: '',
    description: '',
    content: '',
    author: '管理员'
})

// 监听登出事件
const onAdminLogout = () => {
    isLoggedIn.value = false
    activeMenu.value = 'dashboard'
}

// 1. 登录
const handleLogin = async () => {
    loading.value = true
    try {
        const token = await request.post('/admin/login', {
            username: loginForm.username,
            password: loginForm.password
        })

        localStorage.setItem('admin_token', token)
        sessionStorage.setItem('admin_logged_in', 'true')
        isLoggedIn.value = true

        ElMessage.success('欢迎回来')
        await fetchStats()

        if (activeMenu.value === 'article_mgr') fetchAdminArticles()
    } catch (err) {
        ElMessage.error(err?.message || '账号或密码错误')
    } finally {
        loading.value = false
    }
}

// 2. 开发模式快速登录
const handleDevLogin = () => {
    localStorage.setItem('admin_token', 'dev-token-123456')
    localStorage.setItem('adminInfo', JSON.stringify({ username: 'dev_admin', id: 1 }))
    sessionStorage.setItem('admin_logged_in', 'true')
    isLoggedIn.value = true
    ElMessage.success('已进入开发模式')
    fetchStats()
}

// 3. 菜单切换
const handleMenuSelect = (index) => {
    if (index === 'logout') {
        ElMessageBox.confirm('确定要退出登录吗？', '提示', {
            confirmButtonText: '退出',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            sessionStorage.removeItem('admin_logged_in')
            localStorage.removeItem('admin_token')
            localStorage.removeItem('adminInfo')
            isLoggedIn.value = false
            activeMenu.value = 'dashboard'
            ElMessage.info('已退出')
        })
    } else {
        activeMenu.value = index
        if (index === 'dashboard') fetchStats()
        if (index === 'article_mgr') fetchAdminArticles()
    }
}

const getMenuName = (key) => {
    const map = {
        dashboard: '数据看板',
        publish: '发布文章',
        article_mgr: '文章管理'
    }
    return map[key] || '后台首页'
}

// 4. 数据获取
const fetchStats = async () => {
    try {
        const data = await request.get('/admin/stats')
        stats.value = data || {
            userCount: 0,
            articleCount: 0
        }
    } catch (e) {
        // 401 会被 request.js 统一处理并触发 admin-logout
    }
}

// 5. 内容提交
const handleArticleSubmit = async () => {
    if (!articleForm.value.title) {
        return ElMessage.warning('请输入标题')
    }
    loading.value = true
    try {
        await request.post('/article', articleForm.value)
        ElMessage.success('发布成功')
        articleForm.value = {
            title: '',
            firstPicture: '',
            description: '',
            content: '',
            author: '管理员'
        }
    } catch (e) {
        ElMessage.error(e?.message || '发布失败')
    } finally {
        loading.value = false
    }
}

const handleVideoSubmit = async () => {
    loading.value = true
    try {
        await request.post('/video', videoForm.value)
        ElMessage.success('上传成功')
        videoForm.value = {
            title: '',
            url: '',
            cover: '',
            description: '',
            author: '管理员'
        }
    } catch (e) {
        ElMessage.error(e?.message || '上传失败')
    } finally {
        loading.value = false
    }
}

// 6. 用户封禁
const toggleUserStatus = async (user) => {
    const newStatus = user.status === 1 ? 0 : 1
    const actionText = newStatus === 1 ? '解封' : '封禁'
    try {
        await ElMessageBox.confirm(
            `确定要${actionText}用户 ${user.username} 吗？`,
            '警告',
            { type: 'warning' }
        )
        await request.put(`/admin/users/${user.id}/status?status=${newStatus}`)
        user.status = newStatus
        ElMessage.success(`已${actionText}`)
    } catch (e) { }
}

onMounted(() => {
    window.addEventListener('admin-logout', onAdminLogout)

    // 修正：session 说登录了，但没有 admin_token，则视为未登录
    if (sessionStorage.getItem('admin_logged_in') === 'true' && !localStorage.getItem('admin_token')) {
        sessionStorage.removeItem('admin_logged_in')
        isLoggedIn.value = false
    }

    if (isLoggedIn.value) {
        fetchStats()
        if (activeMenu.value === 'article_mgr') fetchAdminArticles()
    }
})

onUnmounted(() => {
    window.removeEventListener('admin-logout', onAdminLogout)
})
</script>

<style scoped>
.admin-container {
    height: 100vh;
    background-color: #f0f2f5;
}

.login-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    position: relative;
    overflow: hidden;
}

.login-bg-shape {
    position: absolute;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    animation: float 6s ease-in-out infinite;
    transition: all 0.3s ease;
}

.login-bg-shape.shape-1 {
    width: 300px;
    height: 300px;
    top: -100px;
    right: -100px;
    animation-delay: 0s;
}

.login-bg-shape.shape-2 {
    width: 400px;
    height: 400px;
    background: rgba(255, 255, 255, 0.05);
    bottom: -150px;
    left: -150px;
    animation-delay: -2s;
}

.login-bg-shape.shape-3 {
    width: 200px;
    height: 200px;
    background: rgba(255, 255, 255, 0.08);
    top: 50%;
    left: 10%;
    transform: translateY(-50%);
    animation-delay: -1s;
}

.login-bg-shape.shape-4 {
    width: 150px;
    height: 150px;
    background: rgba(255, 255, 255, 0.05);
    bottom: 20%;
    right: 15%;
    animation-delay: -3s;
}

/* 浮动动画 */
@keyframes float {

    0%,
    100% {
        transform: translateY(0px) rotate(0deg);
    }

    50% {
        transform: translateY(-20px) rotate(5deg);
    }
}

.login-box {
    width: 420px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.95);
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
}

.login-box:hover {
    transform: translateY(-5px);
    box-shadow: 0 25px 70px rgba(0, 0, 0, 0.35);
}

.login-header {
    text-align: center;
    padding: 30px 0 20px;
}

.login-logo-container {
    width: 100px;
    height: 100px;
    margin: 0 auto 20px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;
}

.login-logo-container:hover {
    transform: scale(1.05);
    box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
}

.login-logo {
    width: 60px;
    height: 60px;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.login-title {
    font-size: 28px;
    font-weight: 700;
    color: #333;
    margin: 0 0 8px;
    letter-spacing: -0.5px;
}

.login-subtitle {
    font-size: 14px;
    color: #666;
    margin: 0;
    font-weight: 400;
}

.login-form {
    padding: 0 30px 30px;
}

.form-item {
    margin-bottom: 25px;
}

.form-footer {
    margin-top: 30px;
}

.login-input {
    transition: all 0.3s ease;
}

.login-btn {
    width: 100%;
    padding: 14px 0;
    font-size: 16px;
    font-weight: 600;
    border-radius: 8px;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    position: relative;
    overflow: hidden;
}

.login-btn::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.3);
    transform: translate(-50%, -50%);
    transition: width 0.6s, height 0.6s;
}

.login-btn:hover::before {
    width: 300px;
    height: 300px;
}

.login-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(102, 126, 234, 0.5);
}

.login-btn:active {
    transform: translateY(0);
}

.login-btn .el-button__text {
    position: relative;
    z-index: 1;
}

.dev-login-btn {
    width: 100%;
    margin-bottom: 10px;
}

.dev-tip {
    font-size: 12px;
    padding: 8px;
}

:deep(.el-form-item__label) {
    font-size: 14px;
    font-weight: 600;
    color: #555;
    margin-bottom: 8px;
    display: block;
    transition: color 0.3s ease;
}

:deep(.el-form-item:hover .el-form-item__label) {
    color: #667eea;
}

:deep(.el-input__wrapper) {
    border-radius: 8px;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid #e4e7ed;
    background-color: #ffffff;
}

:deep(.el-input__wrapper:hover) {
    border-color: #667eea;
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
    transform: translateY(-1px);
}

:deep(.el-input__wrapper.is-focus) {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
    transform: translateY(-1px);
}

:deep(.el-icon-user),
:deep(.el-icon-lock) {
    color: #909399;
    transition: all 0.3s ease;
}

:deep(.el-input.is-focus .el-icon-user),
:deep(.el-input.is-focus .el-icon-lock) {
    color: #667eea;
    transform: scale(1.1);
}

/* 添加登录卡片的入场动画 */
.login-box {
    animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 响应式设计优化 */
@media (max-width: 480px) {
    .login-box {
        width: 90%;
        margin: 0 20px;
    }

    .login-bg-shape.shape-1 {
        width: 200px;
        height: 200px;
        top: -50px;
        right: -50px;
    }

    .login-bg-shape.shape-2 {
        width: 250px;
        height: 250px;
        bottom: -100px;
        left: -100px;
    }
}

.main-layout {
    height: 100%;
}

.sidebar {
    background-color: #304156;
    color: #fff;
    overflow-x: hidden;
    transition: width 0.3s;
}

.brand {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    font-weight: bold;
    background-color: #2b3649;
    color: #fff;
}

.admin-header {
    background: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
    font-size: 14px;
    color: #666;
}

.header-right {
    font-size: 14px;
    color: #666;
}

.main-content {
    padding: 20px;
}

.data-card {
    text-align: center;
}

.data-num {
    font-size: 24px;
    font-weight: bold;
    color: #409eff;
    margin-top: 10px;
}
</style>

