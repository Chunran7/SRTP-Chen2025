// 导入 request.js 请求工具
import request from '@/utils/request.js'

/**
 * 管理员登录接口
 * @param {Object} loginData - 登录数据，包含 username 和 password
 * @returns {Promise} 返回 Promise 对象
 */
export const adminLoginService = (loginData) => {
    // 以 JSON 格式传递数据
    return request.post('/admin/login', loginData);
}
