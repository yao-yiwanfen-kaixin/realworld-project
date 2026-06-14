# RealWorld 前后端分离项目

一个基于 RealWorld 规范的博客平台，采用 Spring Boot + Vue3 技术栈，AI 辅助开发。

## 🚀 快速开始

### 环境要求
- JDK 8+
- Node.js 16+
- Maven 3.6+

### 后端启动

```bash
cd backend

# 安装依赖并启动
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

- H2 控制台：`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:realworld`
- 用户名: `sa`
- 密码: (空)

### 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将运行在 `http://localhost:5173`

## 🛠 技术栈

### 后端
- **框架**：Spring Boot 2.7.14
- **ORM**：MyBatis Plus 3.5.3.1
- **安全**：Spring Security + JWT
- **数据库**：H2 内存数据库
- **加密**：BCrypt

### 前端
- **框架**：Vue 3.3 + Vite 4.4
- **路由**：Vue Router 4.2
- **状态管理**：Pinia 2.1
- **HTTP 客户端**：Axios 1.5
- **UI 组件库**：Element Plus 2.3

## 📁 项目结构

```
realworld-project/
├── backend/                 # 后端 Spring Boot 项目
│   ├── src/main/java/
│   │   └── com/realworld/
│   │       ├── controller/  # 控制层
│   │       ├── service/     # 业务层
│   │       ├── mapper/      # 数据访问层
│   │       ├── entity/      # 实体类
│   │       ├── dto/         # 请求DTO
│   │       ├── vo/          # 响应VO
│   │       ├── config/      # 配置类
│   │       ├── security/    # 安全认证
│   │       └── utils/       # 工具类
│   └── src/main/resources/
│       ├── application.yml  # 应用配置
│       └── schema.sql       # 数据库初始化
├── frontend/                # 前端 Vue3 项目
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   ├── store/           # Pinia 状态管理
│   │   ├── router/          # 路由配置
│   │   ├── api/             # API 封装
│   │   └── utils/           # 工具类
│   ├── package.json
│   └── vite.config.js
├── docs/
│   ├── ai-usage.md          # AI 使用记录
│   └── architecture.md      # 项目架构说明
├── README.md
└── .gitignore
```

## ✨ 功能特性

### 用户模块
- ✅ 用户注册
- ✅ 用户登录（JWT 认证）
- ✅ 获取当前用户信息

### 文章模块
- ✅ 创建文章
- ✅ 获取文章详情
- ✅ 更新文章（作者权限）
- ✅ 删除文章（作者权限）
- ✅ 文章列表分页
- ✅ 文章点赞/取消点赞

### 评论模块
- ✅ 添加评论
- ✅ 评论列表
- ✅ 删除评论（作者权限）

## 🔌 API 接口

### 用户相关
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/users | 用户注册 | 否 |
| POST | /api/users/login | 用户登录 | 否 |
| GET | /api/user | 获取当前用户 | 是 |

### 文章相关
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/articles | 文章列表 | 可选 |
| POST | /api/articles | 创建文章 | 是 |
| GET | /api/articles/:slug | 获取文章 | 否 |
| PUT | /api/articles/:slug | 更新文章 | 是 |
| DELETE | /api/articles/:slug | 删除文章 | 是 |
| POST | /api/articles/:slug/favorite | 点赞文章 | 是 |
| DELETE | /api/articles/:slug/favorite | 取消点赞 | 是 |

### 评论相关
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/articles/:slug/comments | 评论列表 | 否 |
| POST | /api/articles/:slug/comments | 添加评论 | 是 |
| DELETE | /api/articles/:slug/comments/:id | 删除评论 | 是 |

## 🤖 AI 使用总结

### 成功经验

1. **代码生成效率提升**
   - AI 快速生成基础 CRUD 代码骨架
   - 自动生成 Entity、DTO、VO、Mapper、Service、Controller 分层代码
   - 节省大量样板代码编写时间

2. **架构设计辅助**
   - AI 提供合理的目录结构建议
   - 帮助设计数据库表结构
   - 提供安全最佳实践建议

3. **问题排查**
   - 快速定位常见错误（如 CORS、依赖版本冲突）
   - 提供调试思路和解决方案

### 失败案例

1. **JWT 认证前缀错误**
   - AI 默认使用 "Bearer " 前缀，RealWorld 规范要求 "Token "
   - 导致认证失败，花费时间调试

2. **API 返回格式不规范**
   - AI 直接返回对象，RealWorld 要求嵌套格式 `{"user": {...}}`
   - 需要逐个修正 Controller 返回值

3. **Vue3 语法混用**
   - AI 生成 Options API 和 Composition API 混合代码
   - 需要统一改为 `<script setup>` 语法

### 经验教训

1. **Prompt 要详细**：明确技术栈、版本号、规范要求
2. **人工审核必不可少**：AI 生成的代码必须审查测试
3. **规范先行**：先定义项目规范再让 AI 生成代码
4. **复杂逻辑人工写**：核心业务逻辑不要完全依赖 AI

## 📝 Git 提交规范

每次提交包含以下信息：

```
[类型] feat / fix / refactor / test / docs

[任务描述]
本次完成内容

[实现思路]
为什么这样设计？是否有其他方案？

[AI参与情况]
是否使用AI + 工具 + 核心Prompt

[Prompt演化过程]
初始Prompt / 修改后Prompt / 修改原因

[AI输出处理]
是否直接使用？做了哪些修改？

[问题与反思]
遇到的问题与解决方式
```

## 📚 相关文档

- [AI 使用记录](./docs/ai-usage.md) - 详细的 AI 辅助开发过程记录
- [项目架构说明](./docs/architecture.md) - 技术架构、模块划分、数据库设计

## 👤 作者

- 学号：2023000000
- 姓名：黄璐瑶
- 项目：RealWorld AI 辅助开发综合实践

## 📄 许可证

MIT License
