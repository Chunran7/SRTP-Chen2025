<template>
    <div class="admin-container">
        <div class="admin-layout">
            <template v-if="isLoggedIn">
                <el-aside width="220px" class="sidebar">
                    <div class="logo">管理后台</div>
                    <el-menu :default-active="activeMenu" @select="handleMenuSelect">
                        <el-menu-item index="publish">发布文章</el-menu-item>
                        <el-menu-item index="video">插入视频</el-menu-item>
                        <el-menu-item index="list">文章列表</el-menu-item>
                        <el-menu-item index="logout">退出登录</el-menu-item>
                    </el-menu>
                </el-aside>
            </template>

            <el-main class="main-area">
                <div v-if="!isLoggedIn" class="login-wrapper">
                    <el-card class="login-card">
                        <h3>管理员登录</h3>
                        <el-form :model="loginForm" label-width="80px">
                            <el-form-item label="账号">
                                <el-input v-model="loginForm.username"></el-input>
                            </el-form-item>
                            <el-form-item label="密码">
                                <el-input v-model="loginForm.password" type="password"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
                            </el-form-item>
                        </el-form>
                    </el-card>
                </div>

                <div v-else>
                    <el-card class="admin-card">
                        <div v-if="activeMenu === 'publish'">
                            <h2>发布文章</h2>
                            <el-form :model="form" ref="formRef" label-width="100px">
                                <el-form-item label="标题" prop="title"
                                    :rules="[{ required: true, message: '请输入标题', trigger: 'blur' }]">
                                    <el-input v-model="form.title"></el-input>
                                </el-form-item>

                                <el-form-item label="描述" prop="description">
                                    <el-input type="textarea" v-model="form.description"></el-input>
                                </el-form-item>

                                <el-form-item label="作者" prop="author">
                                    <el-input v-model="form.author"></el-input>
                                </el-form-item>

                                <el-form-item label="首图" prop="firstPicture">
                                    <el-input v-model="form.firstPicture" placeholder="图片URL"></el-input>
                                </el-form-item>

                                <el-form-item label="内容" prop="content">
                                    <el-input type="textarea" v-model="form.content" rows="10"></el-input>
                                </el-form-item>

                                <el-form-item>
                                    <el-button type="primary" @click="handleSubmit" :loading="loading">发布</el-button>
                                </el-form-item>
                            </el-form>
                        </div>

                        <div v-if="activeMenu === 'video'">
                            <h2>插入视频</h2>
                            <el-form :model="videoForm" label-width="100px">
                                <el-form-item label="标题">
                                    <el-input v-model="videoForm.title"></el-input>
                                </el-form-item>
                                <el-form-item label="视频URL">
                                    <el-input v-model="videoForm.url"></el-input>
                                </el-form-item>
                                <el-form-item label="封面URL">
                                    <el-input v-model="videoForm.cover"></el-input>
                                </el-form-item>
                                <el-form-item label="作者">
                                    <el-input v-model="videoForm.author"></el-input>
                                </el-form-item>
                                <el-form-item label="描述">
                                    <el-input type="textarea" v-model="videoForm.description"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="handleVideoSubmit"
                                        :loading="loading">提交视频</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-card>
                </div>
            </el-main>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request.js'
import { adminLoginService } from '@/api/admin.js'

const formRef = ref(null)
const loading = ref(false)
const form = ref({
    title: '',
    firstPicture: '',
    description: '',
    content: '',
    author: ''
})

// 登录状态和表单
const isLoggedIn = ref(sessionStorage.getItem('admin_logged_in') === 'true' && localStorage.getItem('token'))
const loginForm = ref({ username: '', password: '' })
const activeMenu = ref('publish')

const handleSubmit = async () => {
    try {
        loading.value = true
        // 调用后端 API
        const res = await request.post('/article', form.value)
        ElMessage.success(res || '发布成功')
        // 重置表单
        form.value = { title: '', firstPicture: '', description: '', content: '', author: '' }
    } catch (err) {
        console.error(err)
        ElMessage.error(err.msg || err.message || '发布失败')
    } finally {
        loading.value = false
    }
}

const videoForm = ref({ title: '', url: '', cover: '', description: '', author: '' })

const handleVideoSubmit = async () => {
    try {
        loading.value = true
        const res = await request.post('/video', videoForm.value)
        ElMessage.success(res || '视频提交成功')
        videoForm.value = { title: '', url: '', cover: '', description: '', author: '' }
    } catch (err) {
        console.error(err)
        ElMessage.error(err.msg || err.message || '提交失败')
    } finally {
        loading.value = false
    }
}

const handleLogin = async () => {
    loading.value = true
    try {
        // 调用后端登录接口
        const { username, password } = loginForm.value
        
        // 先检查前端是否为空
        if (!username) {
            ElMessage.error('请输入用户名')
            loading.value = false
            return
        }
        if (!password) {
            ElMessage.error('请输入密码')
            loading.value = false
            return
        }
        
        // 调用后端登录 API
        const result = await adminLoginService(loginForm.value)
        
        // 登录成功，保存 token 和管理员信息到 localStorage（因为 request.js 从 localStorage 读取）
        localStorage.setItem('token', result.data.token)
        localStorage.setItem('adminInfo', JSON.stringify(result.data.adminInfo))
        sessionStorage.setItem('admin_logged_in', 'true')
        
        isLoggedIn.value = true
        ElMessage.success('登录成功')
    } catch (err) {
        console.error(err)
        ElMessage.error(err.msg || err.message || '登录失败')
    } finally {
        loading.value = false
    }
}

const handleMenuSelect = (key) => {
    if (key === 'logout') {
        sessionStorage.removeItem('admin_logged_in')
        localStorage.removeItem('token')
        localStorage.removeItem('adminInfo')
        isLoggedIn.value = false
        ElMessage.info('已退出')
    } else {
        activeMenu.value = key
    }
}
</script>

<style scoped>
.admin-container {
    padding: 24px;
}

.admin-card {
    max-width: 900px;
    margin: 24px auto;
}

.admin-layout {
    display: flex;
}

.sidebar {
    background: #fff;
    border-right: 1px solid #eee;
    padding-top: 20px;
}

.main-area {
    flex: 1;
    padding: 24px;
}

.login-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: calc(100vh - 48px);
}

.login-card {
    width: 420px;
}
</style>
