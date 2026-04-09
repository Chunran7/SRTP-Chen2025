<template>
  <el-container class="contact-container">
    <el-main class="main">
      <div class="contact-header">
        <h1>联系我们</h1>
        <p class="subtitle">关注我们，获取更多相关信息</p>
      </div>

      <div class="contact-content">
        <!-- 微信公众号 -->
        <div class="contact-card">
          <div class="card-icon">
            <el-icon :size="48">
              <ChatDotRound />
            </el-icon>
          </div>
          <h2>微信公众号</h2>
          <p class="account-name">医路同行-职业丧痛支持</p>
          <div class="qr-code">
            <img :src="wechatQrCode" alt="微信公众号二维码" />
            <p>扫描二维码关注我们</p>
          </div>
        </div>

        <!-- 小红书账号 -->
        <div class="contact-card">
          <div class="card-icon">
            <el-icon :size="48">
              <Star />
            </el-icon>
          </div>
          <h2>小红书账号</h2>
          <p class="account-name">@医路同行-职业丧痛支持</p>
          <div class="social-link">
            <el-button type="danger" @click="openXiaohongshu">
              <el-icon>
                <Link />
              </el-icon>
              前往主页
            </el-button>
          </div>
        </div>
      </div>

      <div class="contact-footer">
        <p>如有合作意向或其他问题，请通过上述平台联系我们</p>
      </div>
    </el-main>

    <!-- 页脚 -->
    <Footer />
  </el-container>
</template>

<script setup>
import { ChatDotRound, Star, Link } from '@element-plus/icons-vue'
import Footer from '@/components/Footer.vue'
import wechatQrCode from '@/assets/QRcode.jpg'

// TODO: 请将 wechat-qr.jpg 放到 src/assets/ 目录下
// 临时使用占位图，实际使用时请替换为：import wechatQrCode from '@/assets/wechat-qr.jpg'

// 打开小红书链接
const openXiaohongshu = () => {
  window.open('https://xhslink.com/m/9cxc81M7MOs', '_blank')
}
</script>

<style scoped>
.contact-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.main {
  flex: 1;
  padding: 40px 20px;
}

.contact-header {
  text-align: center;
  margin-bottom: 50px;
}

.contact-header h1 {
  font-size: 36px;
  color: #333;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 16px;
  color: #666;
}

.contact-content {
  display: flex;
  justify-content: center;
  gap: 40px;
  flex-wrap: wrap;
  max-width: 1200px;
  margin: 0 auto;
}

.contact-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  width: 320px;
}

.contact-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-icon {
  color: #0040ff;
  margin-bottom: 20px;
}

.contact-card h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 15px;
}

.account-name {
  font-size: 18px;
  color: #666;
  margin-bottom: 25px;
  font-weight: 500;
}

.qr-code {
  margin-top: 20px;
}

.qr-code img {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 10px;
}

.qr-code p {
  font-size: 14px;
  color: #999;
}

.social-link {
  margin-top: 20px;
}

.contact-footer {
  text-align: center;
  margin-top: 60px;
  padding-top: 30px;
  border-top: 1px solid #e0e0e0;
  color: #666;
}
</style>
