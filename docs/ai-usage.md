# AI 使用记录

## 一、AI 工具使用情况

本次开发主要使用 AI 工具辅助完成代码生成、架构设计、问题排查等工作。

### 使用的 AI 工具
- 代码生成与架构设计：豆包 AI
- 代码审查与优化：豆包 AI
- 问题排查与调试：豆包 AI

---

## 二、各功能模块 AI 使用记录

### 1. 后端项目初始化

#### 功能描述
Spring Boot 项目基础配置，包括 pom.xml 依赖管理、application.yml 配置、数据库初始化脚本

#### Prompt 示例
**初始 Prompt：**
```
生成一个 Spring Boot + MyBatis Plus 项目的 pom.xml 文件
```

**优化后 Prompt：**
```
生成 Spring Boot 2.7.14 版本的 pom.xml，需要包含以下依赖：
- MyBatis Plus 3.5.3.1
- JWT 0.9.1
- Spring Security
- Lombok
- H2 内存数据库
- Validation
- BCrypt 密码加密

同时生成 application.yml 配置文件，配置 H2 数据库、MyBatis Plus、JWT 参数
```

#### Prompt 演化过程
1. 初始 Prompt 过于简单，只要求生成 pom.xml
2. 发现 AI 生成的依赖版本过时且不完整，补充了具体版本号和依赖列表
3. 进一步要求同时生成 application.yml 配置，减少重复提问

#### AI 输出处理
- 直接使用了 pom.xml 的基本结构
- 手动调整了部分依赖版本号以确保兼容性
- 添加了 H2 数据库 DB_CLOSE_DELAY 参数
- 补充了 MyBatis Plus 逻辑删除配置

---

### 2. 用户注册登录模块

#### 功能描述
用户 Entity、DTO、VO、Mapper、Service、Controller、JWT 工具类、Security 配置

#### Prompt 示例
```
Spring Boot + MyBatis Plus 实现 RealWorld 规范的用户注册登录 API：
1. POST /api/users 注册
2. POST /api/users/login 登录
3. GET /api/user 获取当前用户

要求：
- 使用 BCrypt 加密密码
- JWT 认证，Token 前缀为 "Token "
- 用户名和邮箱唯一性校验
- 分层架构：Entity、DTO、VO、Mapper、Service、Controller
- RealWorld API 返回格式：{"user": {...}}
```

#### Prompt 演化过程
1. 初始："spring boot user login"
2. 发现 AI 不了解 RealWorld 规范，补充 API 路径和返回格式
3. 明确 JWT 认证前缀要求（RealWorld 规范要求 "Token " 而非 "Bearer "）
4. 要求完整的分层代码结构

#### AI 输出处理
- 修正了 JWT 认证前缀为 "Token "
- 添加了用户名和邮箱重复校验逻辑
- 统一了 API 返回格式为嵌套结构
- 修复了 Security 配置中的 CORS 跨域问题

---

### 3. 文章 CRUD 模块

#### 功能描述
文章创建、查询、更新、删除、列表分页、点赞功能

#### Prompt 示例
```
实现 RealWorld 文章模块完整功能：
Entity: Article, ArticleFavorite
API:
- POST /api/articles 创建文章
- GET /api/articles/:slug 获取文章
- PUT /api/articles/:slug 更新文章
- DELETE /api/articles/:slug 删除文章
- GET /api/articles 文章列表
- POST /api/articles/:slug/favorite 点赞
- DELETE /api/articles/:slug/favorite 取消点赞

要求：
- slug 基于标题生成，保证唯一
- 只有作者可以修改/删除文章
- 点赞使用中间表
- 返回文章点赞数和当前用户点赞状态
```

#### AI 输出处理
- slug 生成逻辑添加时间戳避免重复
- 完善了权限校验逻辑
- 处理了未登录用户访问文章列表的异常
- 优化了点赞数统计查询

---

### 4. 评论功能

#### 功能描述
添加评论、获取评论列表、删除评论

#### Prompt 示例
```
实现 RealWorld 评论 API：
- POST /api/articles/:slug/comments 添加评论
- GET /api/articles/:slug/comments 获取评论列表
- DELETE /api/articles/:slug/comments/:id 删除评论

请求体格式：{"comment": {"body": "..."}}
只有评论作者可以删除评论
```

#### AI 输出处理
- 修正了请求体嵌套解析逻辑
- 添加了删除评论的作者权限校验
- 按创建时间倒序返回评论

---

### 5. 前端 Vue3 项目

#### 功能描述
Vue3 + Vite 项目初始化、路由配置、状态管理、API 封装

#### Prompt 示例
```
生成 Vue3 + Vite 项目基础配置：
- package.json 依赖：vue 3.3、vue-router 4、pinia、axios、element-plus
- vite.config.js 配置后端代理
- main.js 集成 Pinia、Router、Element Plus
- router/index.js 路由配置
- Pinia user store 实现登录注册状态管理
- Axios 请求拦截器自动添加 Token
```

#### AI 输出处理
- 调整了 Vite 代理配置指向正确的后端端口
- 完善了 Axios 响应拦截器的错误处理
- 添加了 Element Plus 样式导入

---

## 三、AI 失败案例（至少 3 个）

### 失败案例 1：JWT 认证前缀错误

#### 问题描述
AI 生成的 JWT 认证过滤器使用了标准的 "Bearer " 前缀，但 RealWorld 规范要求使用 "Token " 前缀，导致前端请求认证失败。

#### AI 输出（错误）
```java
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
    // ...
}
```

#### 问题影响
- 前端发送的请求头 `Authorization: Token xxx` 无法被后端识别
- 所有需要认证的接口都返回 403
- 调试花费了约 30 分钟才定位到问题

#### 修正方案
手动修改为 RealWorld 规范要求的前缀：
```java
if (authHeader != null && authHeader.startsWith("Token ")) {
    String token = authHeader.substring(6);
    // ...
}
```

#### 反思
AI 通常按照通用 RESTful 规范生成代码，对于特定项目的特殊规范（如 RealWorld）不了解，需要明确指定。

---

### 失败案例 2：API 返回格式不符合 RealWorld 规范

#### 问题描述
AI 生成的 Controller 直接返回对象，没有按照 RealWorld 规范要求的嵌套格式。

#### AI 输出（错误）
```java
@PostMapping("/users")
public ResponseEntity<UserVO> register(@RequestBody RegisterRequest request) {
    UserVO userVO = userService.register(request);
    return ResponseEntity.ok(userVO);
}
```

#### 正确格式应该是：
```json
{"user": {"email": "...", "token": "...", ...}}
```

#### 问题影响
- 前端无法正确解析响应数据
- 所有 API 对接都出现问题
- 需要逐个修改 Controller 返回格式

#### 修正方案
统一包装返回值：
```java
@PostMapping("/users")
public ResponseEntity<Map<String, UserVO>> register(@RequestBody Map<String, RegisterRequest> request) {
    UserVO userVO = userService.register(request.get("user"));
    Map<String, UserVO> response = new HashMap<>();
    response.put("user", userVO);
    return ResponseEntity.ok(response);
}
```

#### 反思
AI 不了解特定项目的 API 规范，必须在 Prompt 中明确说明返回格式要求。

---

### 失败案例 3：Vue3 语法混用

#### 问题描述
AI 生成的 Vue 组件代码混用了 Options API 和 Composition API，部分代码使用了过时的语法。

#### AI 输出（错误）
```vue
<script>
import { ref } from 'vue'

export default {
  setup() {
    const form = ref({ username: '', password: '' })
    
    const submit = () => {
      // ...
    }
    
    return { form, submit }
  }
}
</script>
```

#### 问题影响
- 代码风格不统一
- 部分 Vue3 新特性无法使用
- 可读性和可维护性差

#### 修正方案
统一使用 `<script setup>` 语法：
```vue
<script setup>
import { ref } from 'vue'

const form = ref({ username: '', password: '' })

const submit = () => {
  // ...
}
</script>
```

#### 反思
需要在 Prompt 中明确指定使用 `<script setup>` 语法糖，避免 AI 生成混合风格代码。

---

## 四、AI 使用总结

### 成功经验
1. **详细的 Prompt 描述**：明确技术栈、版本号、API 规范、返回格式等细节
2. **分层生成**：按模块逐步生成，避免一次性生成过多代码
3. **人工审核**：AI 生成的代码必须经过审查和测试
4. **规范先行**：在生成代码前先明确项目规范和约定

### 失败教训
1. AI 不了解特定项目（如 RealWorld）的特殊规范
2. AI 生成的代码可能存在安全漏洞（如缺少权限校验）
3. AI 可能使用过时的语法或最佳实践
4. 复杂业务逻辑仍需要人工设计和实现

### 改进方向
1. 提供项目规范文档作为 AI 上下文
2. 使用代码审查工具检查 AI 生成的代码
3. 对 AI 生成的代码进行单元测试
4. 重要的业务逻辑尽量人工编写
