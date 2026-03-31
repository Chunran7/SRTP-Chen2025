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

/**
 * 获取文章列表
 * @returns {Promise} 返回 Promise 对象
 */
export const getArticleListService = () => {
    return request.get('/article/list');
}

/**
 * 删除文章
 * @param {Number} id - 文章 ID
 * @returns {Promise} 返回 Promise 对象
 */
export const deleteArticleService = (id) => {
    return request.delete(`/article/${id}`);
}

/**
 * 发布文章
 * @param {Object} articleData - 文章数据
 * @returns {Promise} 返回 Promise 对象
 */
export const publishArticleService = (articleData) => {
    return request.post('/article', articleData);
}

/**
 * 管理员获取文章列表（支持分页、搜索、包含已删除）
 * @param {Object} params - 查询参数，包含 page、pageSize、keyword、includeDeleted
 * @returns {Promise} 返回 Promise 对象
 */
export const adminGetArticleList = (params) => {
    return request.get('/admin/article/list', { params });
}

/**
 * 管理员根据 ID 获取文章详情
 * @param {Number} id - 文章 ID
 * @returns {Promise} 返回 Promise 对象
 */
export const adminGetArticleById = (id) => {
    return request.get(`/admin/article/${id}`);
}

/**
 * 管理员软删除文章
 * @param {Number} id - 文章 ID
 * @returns {Promise} 返回 Promise 对象
 */
export const adminSoftDeleteArticle = (id) => {
    return request.delete(`/admin/article/${id}/soft`);
}

/**
 * 管理员恢复已删除的文章
 * @param {Number} id - 文章 ID
 * @returns {Promise} 返回 Promise 对象
 */
export const adminRestoreArticle = (id) => {
    return request.put(`/admin/article/${id}/restore`);
}

/**
 * 管理员彻底删除文章（硬删除）
 * @param {Number} id - 文章 ID
 * @returns {Promise} 返回 Promise 对象
 */
export const adminHardDeleteArticle = (id) => {
    return request.delete(`/admin/article/${id}/hard`);
}

/**
 * 获取介绍信息
 * @returns {Promise} 返回 Promise 对象
 */
export const getIntroductionService = () => {
    return request.get('/admin/introduction');
}

/**
 * 更新介绍信息
 * @param {Object} introData - 介绍信息数据，包含 title、content、imageUrl
 * @returns {Promise} 返回 Promise 对象
 */
export const updateIntroductionService = (introData) => {
    return request.put('/admin/introduction', introData);
}

/**
 * 管理员更新文章
 * @param {Number} id - 文章 ID
 * @param {Object} articleData - 文章数据，包含 title、content 等
 * @returns {Promise} 返回 Promise 对象
 */
export const adminUpdateArticle = (id, articleData) => {
    return request.put(`/admin/article/${id}`, articleData);
}
