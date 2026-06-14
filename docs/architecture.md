# 项目架构说明

## 一、整体架构

本项目采用**前后端分离**架构，基于 RESTful API 进行数据交互。

```
┌─────────────────┐          ┌─────────────────┐
│   Vue3 前端     │  HTTP    │  Spring Boot    │
│  (Element Plus) │ ───────> │  (MyBatis Plus) │
└─────────────────┘          └─────────────────┘
         │                           │
         │                           │
         ▼                           ▼
  Pinia 状态管理               H2 内存数据库
  Vue Router 路由
```

---

## 二、技术选型及原因

### 后端技术栈

| 技术 | 版本 | 选型原因 |
|------|------|----------|
| Spring Boot | 2.7.14 | 成熟稳定的 Java 企业级开发框架，自动配置简化开发 |
| MyBatis Plus | 3.5.3.1 | MyBatis 的增强工具，简化 CRUD 操作，提供代码生成 |
| Spring Security | 5.7.x | 强大的安全框架，支持认证和授权 |
| JWT (jjwt) | 0.9.1 | 无状态认证方案，适合前后端分离架构 |
| BCrypt | 0.4 | 安全的密码加密算法 |
| H2 Database | 2.x | 内存数据库，开发测试方便，无需额外安装 |
| Lombok | 最新 | 减少样板代码，提高开发效率 |

### 前端技术栈

| 技术 | 版本 | 选型原因 |
|------|------|----------|
| Vue 3 | 3.3.x | 最新的 Vue 版本，Composition API 更灵活 |
| Vite | 4.4.x | 新一代前端构建工具，开发体验极佳，启动速度快 |
| Vue Router | 4.2.x | Vue 官方路由管理 |
| Pinia | 2.1.x | Vue 官方推荐的新一代状态管理，替代 Vuex |
| Axios | 1.5.x | 成熟的 HTTP 客户端，支持拦截器 |
| Element Plus | 2.3.x | Vue 3 生态中最完善的 UI 组件库 |

---

## 三、前后端交互方式

### 1. 通信协议
- **协议**：HTTP/HTTPS
- **数据格式**：JSON
- **认证方式**：JWT Token

### 2. 请求头规范
```
Authorization: Token {jwt_token}
Content-Type: application/json
```

### 3. API 响应格式

**成功响应：**
```json
{
  "user": {
    "email": "user@example.com",
    "token": "jwt_token_here",
    "username": "username",
    "bio": null,
    "image": null
  }
}
```

**列表响应：**
```json
{
  "articles": [...],
  "articlesCount": 10
}
```

### 4. 跨域处理
- 后端配置 CORS 允许前端域名访问
- 开发环境使用 Vite 代理 `/api` 到后端
- 生产环境建议使用 Nginx 反向代理

---

## 四、后端模块划分

### 目录结构
```
backend/
├── src/main/java/com/realworld/
│   ├── controller/          # 控制层：处理 HTTP 请求
│   │   ├── UserController
│   │   ├── ArticleController
│   │   └── CommentController
│   ├── service/             # 业务层：业务逻辑
│   │   ├── UserService      # 接口定义
│   │   ├── ArticleService
│   │   ├── CommentService
│   │   └── impl/            # 接口实现
│   ├── mapper/              # 数据访问层：MyBatis Mapper
│   ├── entity/              # 实体类：数据库表映射
│   ├── dto/                 # 数据传输对象：请求参数
│   ├── vo/                  # 视图对象：响应数据
│   ├── config/              # 配置类
│   ├── security/            # 安全相关：JWT过滤器
│   ├── utils/               # 工具类：JWT工具
│   └── exception/           # 异常处理
└── src/main/resources/
    ├── application.yml      # 应用配置
    └── schema.sql           # 数据库初始化脚本
```

### 核心模块说明

#### 1. 用户模块 (User)
- **功能**：注册、登录、获取当前用户信息
- **认证流程**：
  1. 用户提交邮箱密码
  2. 后端验证 BCrypt 加密密码
  3. 生成 JWT Token 返回
  4. 后续请求携带 Token 认证

#### 2. 文章模块 (Article)
- **功能**：创建、查询、更新、删除、列表、点赞
- **权限控制**：只有作者可以修改/删除文章
- **Slug 生成**：标题转小写 + 连字符 + 时间戳

#### 3. 评论模块 (Comment)
- **功能**：添加评论、评论列表、删除评论
- **权限控制**：只有评论作者可以删除评论

---

## 五、前端模块划分

### 目录结构
```
frontend/
├── src/
│   ├── views/              # 页面组件
│   │   ├── Home.vue        # 首页（文章列表）
│   │   ├── Login.vue       # 登录页
│   │   ├── Register.vue    # 注册页
│   │   ├── Editor.vue      # 文章编辑页
│   │   └── Article.vue     # 文章详情页
│   ├── components/         # 公共组件
│   ├── store/              # Pinia 状态管理
│   │   └── user.js         # 用户状态
│   ├── router/             # 路由配置
│   │   └── index.js
│   ├── api/                # API 封装
│   │   ├── user.js
│   │   └── article.js
│   ├── utils/              # 工具类
│   │   └── request.js      # Axios 封装
│   ├── App.vue
│   └── main.js
├── package.json
├── vite.config.js
└── index.html
```

### 核心模块说明

#### 1. 状态管理 (Pinia)
- **user store**：管理用户登录状态、Token、用户信息
- Token 持久化到 localStorage
- 刷新页面自动恢复登录状态

#### 2. API 层封装
- 统一的 Axios 实例
- 请求拦截器自动添加 Token 头
- 响应拦截器统一处理返回数据

#### 3. 路由设计
| 路径 | 页面 | 说明 |
|------|------|------|
| / | Home | 首页文章列表 |
| /login | Login | 登录页面 |
| /register | Register | 注册页面 |
| /editor | Editor | 发布文章 |
| /article/:slug | Article | 文章详情 |

---

## 六、数据库设计

### 数据表结构

#### users 用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名，唯一 |
| email | VARCHAR(100) | 邮箱，唯一 |
| password | VARCHAR(255) | BCrypt 加密密码 |
| bio | VARCHAR(500) | 个人简介 |
| image | VARCHAR(255) | 头像URL |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 逻辑删除标记 |

#### articles 文章表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| slug | VARCHAR(255) | 文章唯一标识 |
| title | VARCHAR(255) | 标题 |
| description | VARCHAR(500) | 简介 |
| body | TEXT | 内容 |
| author_id | BIGINT | 作者ID |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 逻辑删除标记 |

#### comments 评论表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| body | TEXT | 评论内容 |
| article_id | BIGINT | 文章ID |
| author_id | BIGINT | 评论者ID |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 逻辑删除标记 |

#### article_favorites 文章点赞表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| article_id | BIGINT | 文章ID |
| created_at | TIMESTAMP | 创建时间 |

**联合唯一约束**：(user_id, article_id) 防止重复点赞

---

## 七、安全设计

### 1. 认证安全
- JWT Token 有效期 24 小时
- 密码使用 BCrypt 加密（加盐哈希）
- Token 存储在 HttpOnly Cookie 或 localStorage
- 敏感接口需要 Token 认证

### 2. 权限控制
- 文章修改/删除：只能文章作者操作
- 评论删除：只能评论作者操作
- 发布文章/评论：需要登录

### 3. 数据安全
- 逻辑删除而非物理删除
- SQL 注入防护：MyBatis Plus 参数化查询
- XSS 防护：前端转义 + 后端验证

---

## 八、部署说明

### 开发环境
1. 后端：`mvn spring-boot:run` (端口 8080)
2. 前端：`npm run dev` (端口 5173)
3. 数据库：H2 内存数据库自动初始化

### 生产环境建议
1. 后端：打包为 Jar，使用 MySQL 数据库
2. 前端：`npm run build` 打包静态资源
3. 使用 Nginx 做反向代理和静态资源服务
4. 配置 HTTPS 确保传输安全
