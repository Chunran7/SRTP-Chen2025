<template>
  <div class="login-container">
    <div class="form-wrapper">
      <div class="form-header">
        <h2>{{ isLogin ? '用户登录' : '用户注册' }}</h2>
        <div class="toggle-buttons">
          <button 
            :class="{ active: isLogin }" 
            @click="isLogin = true"
          >
            登录
          </button>
          <button 
            :class="{ active: !isLogin }" 
            @click="isLogin = false"
          >
            注册
          </button>
        </div>
      </div>

      <form @submit.prevent="handleSubmit" class="form-content">
        <!-- 用户名输入 -->
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            placeholder="请输入用户名"
            required
          />
        </div>

        <!-- 密码输入 -->
        <div class="form-group">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            required
          />
        </div>

        <!-- 注册时显示确认密码 -->
        <div v-if="!isLogin" class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            required
          />
        </div>

        <!-- 错误提示信息 -->
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-button" :disabled="loading">
          {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
        </button>
      </form>

      <!-- 忘记密码链接（仅登录时显示） -->
      <div v-if="isLogin" class="additional-links">
        <a href="#" @click.prevent="handleForgotPassword">忘记密码？</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      isLogin: true, // true表示登录模式，false表示注册模式
      loading: false,
      errorMessage: '',
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    // 处理表单提交
    async handleSubmit() {
      this.errorMessage = ''
      
      // 表单验证
      if (!this.form.username || !this.form.password) {
        this.errorMessage = '用户名和密码不能为空'
        return
      }

      // 注册时验证密码一致性
      if (!this.isLogin && this.form.password !== this.form.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致'
        return
      }

      this.loading = true

      try {
        if (this.isLogin) {
          // 登录逻辑
          await this.login()
        } else {
          // 注册逻辑
          await this.register()
        }
      } catch (error) {
        this.errorMessage = error.message || '操作失败，请重试'
      } finally {
        this.loading = false
      }
    },

    // 模拟登录
    async login() {
      // 这里应该是实际的登录API调用
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          if (this.form.username === 'admin' && this.form.password === '123456') {
            // 模拟登录成功
            localStorage.setItem('isLoggedIn', 'true')
            this.$router.push('/dashboard') // 登录成功后跳转到首页
            resolve()
          } else {
            reject(new Error('用户名或密码错误'))
          }
        }, 1000)
      })
    },

    // 模拟注册
    async register() {
      // 这里应该是实际的注册API调用
      return new Promise((resolve) => {
        setTimeout(() => {
          // 模拟注册成功
          alert('注册成功！现在可以登录了。')
          this.isLogin = true
          this.resetForm()
          resolve()
        }, 1000)
      })
    },

    // 重置表单
    resetForm() {
      this.form = {
        username: '',
        password: '',
        confirmPassword: ''
      }
    },

    // 忘记密码处理
    handleForgotPassword() {
      alert('请联系管理员重置密码')
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.form-wrapper {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.form-header {
  background: #f8f9fa;
  padding: 30px 20px 20px;
  text-align: center;
}

.form-header h2 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 24px;
}

.toggle-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.toggle-buttons button {
  padding: 8px 20px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-buttons button.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.form-content {
  padding: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.error-message {
  color: #dc3545;
  background: #f8d7da;
  padding: 10px 15px;
  border-radius: 5px;
  margin-bottom: 20px;
  font-size: 14px;
}

.submit-button {
  width: 100%;
  padding: 12px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.submit-button:hover:not(:disabled) {
  background: #0056b3;
}

.submit-button:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.additional-links {
  text-align: center;
  padding: 0 30px 30px;
}

.additional-links a {
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
}

.additional-links a:hover {
  text-decoration: underline;
}
</style>