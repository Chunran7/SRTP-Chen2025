<template>
    <div class="profile-page">
        <el-card class="profile-card">
            <h2>编辑个人资料（占位页）</h2>
            <el-form :model="form" label-width="100px">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" disabled />
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="form.nickname" />
                </el-form-item>
                <el-form-item label="简介">
                    <el-input type="textarea" v-model="form.bio" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="save">保存</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const form = reactive({ username: 'user', nickname: '', bio: '' })

const save = () => {
    ElMessage.success('已保存（占位）')
}
</script>

<style scoped>
.profile-page {
    padding: 24px;
    display: flex;
    justify-content: center
}

.profile-card {
    width: 600px
}
</style>
