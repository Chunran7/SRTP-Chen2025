# 管理员登录功能说明

## 功能概述
完成了管理员登录功能，包含以下特性：
- ✅ 输入格式校验（用户名、密码不能为空）
- ✅ 数据库查询验证用户是否存在
- ✅ 密码 MD5 加密校验
- ✅ JWT Token 生成和返回
- ✅ 前端调用示例

## 后端实现

### 1. 新增文件

#### AdminMapper.java
- 位置：`back/src/main/java/com/chun/back/mapper/AdminMapper.java`
- 功能：根据用户名查询管理员信息

#### AdminService.java
- 位置：`back/src/main/java/com/chun/back/service/AdminService.java`
- 功能：管理员服务接口

#### AdminServiceImpl.java
- 位置：`back/src/main/java/com/chun/back/service/impl/AdminServiceImpl.java`
- 功能：管理员服务实现类，包含登录逻辑

### 2. 修改文件

#### AdminController.java
- 位置：`back/src/main/java/com/chun/back/controller/AdminController.java`
- 修改内容：
  - 添加输入格式校验
  - 调用 Service 层查询数据库
  - 集成 JWT Token 生成
  - 返回 token 和管理员信息

## 前端实现

### 1. 新增文件

#### admin.js
- 位置：`front/src/api/admin.js`
- 功能：管理员登录 API 调用

### 2. 修改文件

#### Admin.vue
- 位置：`front/src/views/Admin.vue`
- 修改内容：
  - 将本地校验改为调用后端 API
  - 保存 JWT Token 到 sessionStorage
  - 保存管理员信息到 sessionStorage

## 数据库配置

### 执行 SQL 脚本
在 MySQL 数据库中执行以下脚本创建 admin 表：
```bash
# 进入项目目录
cd back/src/main/resources

# 执行 SQL 脚本
mysql -u root -p srtpchen < admin_table.sql
```

或者手动执行 `admin_table.sql` 中的 SQL 语句。

### 默认管理员账号
- 用户名：`seu666`
- 密码：`yilutongxing`

## API 接口说明

### 管理员登录接口

**请求地址：** `POST /api/admin/login`

**请求参数：**
```json
{
  "username": "seu666",
  "password": "yilutongxing"
}
```

**成功响应：**
```json
{
  "code": 0,
  "msg": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "adminInfo": {
      "id": 1,
      "username": "seu666",
      "nickname": "系统管理员",
      "email": null,
      "status": 1,
      ...
    }
  }
}
```

**失败响应：**
```json
{
  "code": 1,
  "msg": "用户不存在",
  "data": null
}
```

或
```json
{
  "code": 1,
  "msg": "密码错误",
  "data": null
}
```

## 使用流程

1. **初始化数据库**
   - 执行 `admin_table.sql` 创建 admin 表
   - 插入默认管理员账号

2. **启动后端服务**
   ```bash
   cd back
   mvn spring-boot:run
   ```

3. **启动前端服务**
   ```bash
   cd front
   npm run dev
   ```

4. **访问管理后台**
   - 打开浏览器访问：`http://localhost:5173/admin`（前端端口可能不同）
   - 输入用户名和密码
   - 点击登录

5. **登录成功后**
   - Token 会自动保存到 sessionStorage
   - 管理员信息会保存到 sessionStorage
   - 可以访问管理后台功能

## 技术要点

### 1. 输入校验
- 使用 Spring Validation 进行参数校验
- 前端也进行基础的空值校验

### 2. 密码加密
- 使用 MD5 工具类进行密码加密
- 数据库中存储的是密码的 MD5 哈希值
- 登录时将输入的密码进行 MD5 加密后与数据库比对

### 3. JWT Token
- 使用 `java-jwt` 库生成 Token
- Token 有效期：12 小时
- Token 中包含管理员 ID 和用户名
- 前端在后续请求中需要在 Header 中携带 Token

### 4. 安全性
- 密码不在网络上传输明文
- 使用 JWT 进行身份验证
- Token 存储在 sessionStorage 中

## 注意事项

1. **数据库连接**：确保 `application.yml` 中的数据库连接配置正确
2. **MD5 密码**：如果手动插入数据，需要使用 MD5 加密密码
3. **Token 过期**：Token 有效期为 12 小时，过期后需要重新登录
4. **跨域问题**：如果前后端分离部署，需要配置 CORS

## 扩展建议

1. **密码强度校验**：添加密码复杂度要求
2. **验证码功能**：防止暴力破解
3. **登录日志**：记录登录时间和 IP
4. **多角色权限**：支持不同级别的管理员权限
5. **Token 刷新机制**：实现无感知续期
