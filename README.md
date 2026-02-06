# Sample Mall Java - 企业AI Coding示例电商平台项目

## 项目简介

这是一个专为**企业AI Coding**设计的轻量级商城后端项目,聚焦"商品"与"订单"两个核心领域。

**核心特点:**
- ✅ 零数据库依赖,使用内存存储(ConcurrentHashMap)
- ✅ 示例数据来自JSON文件,便于修改和扩展
- ✅ 集成Swagger文档,支持在线API测试
- ✅ 内置浅色风格管理后台(admin.html)
- ✅ 一键启动脚本(Windows/macOS/Linux)
- ✅ 适合AI辅助编程学习和演示

**技术栈:**
- Spring Boot 2.7.5
- Java 8+
- Swagger (Springfox 3.0.0)
- 无数据库依赖

---

## 目录结构

```
sample-mall-java/
├── src/main/
│   ├── java/com/example/mall/
│   │   ├── MallBackendApplication.java    # 启动类
│   │   ├── config/SwaggerConfig.java      # Swagger配置
│   │   ├── product/                       # 商品领域
│   │   │   ├── Product.java
│   │   │   ├── ProductController.java
│   │   │   ├── ProductService.java
│   │   │   └── ProductRepository.java
│   │   └── order/                         # 订单领域
│   │       ├── Order.java
│   │       ├── OrderController.java
│   │       ├── OrderService.java
│   │       └── OrderRepository.java
│   └── resources/
│       ├── application.yml                # 配置文件
│       ├── products.json                  # 商品示例数据
│       ├── orders.json                    # 订单示例数据
│       └── static/
│           └── admin.html                 # 管理后台页面
├── pom.xml                                # Maven配置
├── .gitignore                             # Git忽略配置
├── setup-and-run.ps1                      # Windows启动脚本
└── setup-and-run.sh                       # macOS/Linux启动脚本
```

---

## 快速开始

### 方式一:使用一键启动脚本(推荐)

#### Windows

1. 打开PowerShell,进入项目目录
2. 执行脚本:
   ```powershell
   .\setup-and-run.ps1
   ```
3. 脚本会自动检测并安装Java/Maven(如果需要),然后启动应用

#### macOS/Linux

1. 打开终端,进入项目目录
2. 赋予执行权限(首次需要):
   ```bash
   chmod +x ./setup-and-run.sh
   ```
3. 执行脚本:
   ```bash
   ./setup-and-run.sh
   ```

### 方式二:手动启动

确保已安装Java 8+和Maven,然后执行:

```bash
mvn spring-boot:run
```

启动成功后,应用将运行在 `http://localhost:8080`

---

## 访问地址

- **管理后台**: http://localhost:8080/admin.html
- **Swagger文档**: http://localhost:8080/swagger-ui/index.html
- **API接口**: http://localhost:8080/api/*

---

## 示例数据说明

项目从JSON文件加载示例数据:

- **商品数据**: `src/main/resources/products.json`
  - 包含20个示例商品(水果、零食、书籍、文具、日用品等)
  - 可以直接修改JSON文件来自定义商品数据

- **订单数据**: `src/main/resources/orders.json`
  - 包含20个示例订单(覆盖2026年2月1日和2日)
  - 可以直接修改JSON文件来自定义订单数据

**修改示例数据后需要重启应用生效**

---

## 项目改进建议

### 1. 基础功能开发
- 添加商品分类功能
- 实现订单状态管理
- 增加用户模块

### 2. 业务逻辑增强
- 添加库存扣减逻辑
- 实现订单金额自动计算
- 添加数据校验(价格/库存/订单等)

### 3. 代码质量提升
- 编写单元测试
- 添加统一异常处理
- 实现日志记录
- 添加参数校验注解

### 4. 数据库集成
- 接入MySQL/PostgreSQL
- 将内存Repository替换为JPA Repository
- 添加数据库迁移脚本

### 5. 高级功能
- 实现促销活动功能
- 添加订单优惠计算
- 实现商品搜索优化
- 添加Redis缓存

---