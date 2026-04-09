<template>
  <el-footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3 class="footer-title">关于我们</h3>
        <p class="footer-text">医务人员职业丧痛研究团队</p>
        <p class="footer-text">致力于关注和支持医务人员的心理健康</p>
      </div>

      <div class="footer-section">
        <h3 class="footer-title">联系方式</h3>
        <p class="footer-text">邮箱: cqchen@seu.edu.cn</p>
        <el-link :underline="false" href="/contact" class="footer-link">微信公众号：医路同行-职业丧痛支持</el-link>
        <el-link :underline="false" href="/contact" class="footer-link">小红书：医路同行-职业丧痛支持</el-link>
      </div>

      <div class="footer-section">
        <h3 class="footer-title">快速链接</h3>
        <el-link :underline="false" href="/" class="footer-link">首页</el-link>
        <el-link :underline="false" href="/article" class="footer-link">文章</el-link>
        <el-link :underline="false" href="/contact" class="footer-link">联系我们</el-link>
      </div>
    </div>

    <div class="footer-bottom">
      <p class="copyright">版权所有 © {{ currentYear }} 医务人员职业丧痛研究团队</p>
      <p class="icp">
        <el-link href="https://beian.miit.gov.cn/" target="_blank" :underline="false" class="icp-link">
          苏ICP备2025230238号-1
        </el-link>
      </p>
    </div>
  </el-footer>
</template>

<script setup>
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
.footer {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: #ecf0f1;
  padding: 40px 20px 20px;
  height: auto;
  min-height: 200px;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  align-items: flex-start;
  max-width: 1200px;
  margin: 0 auto 30px;
  gap: 40px;
  flex-wrap: wrap;
}

.footer-section {
  flex: 1;
  min-width: 200px;
}

.footer-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 15px;
  color: #fff;
  position: relative;
  padding-bottom: 10px;
}

.footer-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  border-radius: 2px;
}

.footer-text {
  font-size: 14px;
  line-height: 1.8;
  margin: 8px 0;
  color: #bdc3c7;
}

.footer-link {
  display: block;
  margin: 8px 0;
  color: #bdc3c7 !important;
  transition: all 0.3s ease;
}

.footer-link:hover {
  color: #409eff !important;
  transform: translateX(5px);
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 20px;
  text-align: center;
}

.copyright {
  font-size: 14px;
  margin: 0 0 10px;
  color: #95a5a6;
}

.icp {
  font-size: 13px;
  margin: 0;
}

.icp-link {
  color: #95a5a6 !important;
  transition: color 0.3s ease;
}

.icp-link:hover {
  color: #409eff !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer {
    padding: 30px 15px 15px;
  }

  .footer-content {
    flex-direction: column;
    gap: 30px;
  }

  .footer-section {
    text-align: center;
  }

  .footer-title::after {
    left: 50%;
    transform: translateX(-50%);
  }

  .footer-link:hover {
    transform: none;
  }
}
</style>
