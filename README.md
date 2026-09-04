# Campus Delivery 🍱

> 面向校园场景的外卖订餐与配送管理系统，覆盖 **用户端点餐** 与 **商家管理端** 完整业务闭环。

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) ![Redis](https://img.shields.io/badge/Redis-6-red) ![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📖 项目简介

Campus Delivery 是一套前后端分离的校园外卖系统，后端基于 Spring Boot 单体多模块架构实现。系统围绕「门店营业 → 菜品管理 → 购物车 → 下单支付 → 订单派送 → 数据报表」的完整业务链路设计，包含管理端（员工/分类/菜品/套餐/订单/报表/店铺）与用户端（地址簿/浏览点餐/购物车/订单/登录）两组 REST API。

## ✨ 核心功能

| 端 | 功能模块 | 说明 |
|---|---|---|
| 管理端 | 员工管理 | 员工登录（JWT 校验）、新增员工、启用/禁用、编辑、密码修改 |
| 管理端 | 分类 / 菜品 / 套餐 | 菜品与套餐的增删改查、启售停售、分页查询 |
| 管理端 | 订单管理 | 订单查询、派送、取消；来单提醒（WebSocket 推送） |
| 管理端 | 数据统计 | 营业额、用户、订单统计与 Excel 报表导出（Apache POI） |
| 管理端 | 店铺状态 | 营业 / 打烊状态切换，状态实时同步用户端 |
| 用户端 | 微信登录 | 微信小程序授权登录，JWT 双端令牌管理 |
| 用户端 | 浏览点餐 | 分类、菜品、套餐浏览，菜品口味规格选择 |
| 用户端 | 购物车 | 添加 / 删除 / 清空购物车 |
| 用户端 | 订单 | 下单、支付、历史订单查询、再来一单、催单（WebSocket） |
| 公共 | 地址簿 | 用户收货地址管理 |
| 公共 | 配送范围校验 | 基于百度地图 API 计算收货地址与店铺距离 |

## 🛠 技术选型

| 层次 | 技术 |
|---|---|
| 基础框架 | Spring Boot 2.7.3、Spring MVC |
| 持久层 | MyBatis、PageHelper 分页插件、Druid 连接池 |
| 数据存储 | MySQL、Redis（Spring Data Redis + Spring Cache 缓存菜品数据） |
| 认证授权 | JWT 令牌（管理端 / 用户端双令牌体系）、Spring Security Crypto 密码加密 |
| AOP | 自定义注解 + 切面实现公共字段（创建/更新时间、操作人）自动填充 |
| 实时通信 | Spring WebSocket（来单提醒、催单提醒、订单状态推送） |
| 定时任务 | Spring Task（订单状态定时处理） |
| 文件存储 | 阿里云 OSS（菜品图片等） |
| 接口文档 | Knife4j（Swagger 增强） |
| 报表导出 | Apache POI |
| 数据校验/工具 | Lombok、FastJSON、Apache Commons Lang |

## 📦 模块结构

```
campus_delivery
├── campus_delivery_common     # 公共模块：工具类、常量、异常、自定义注解、配置属性类
├── campus_delivery_pojo       # 实体模块：Entity / DTO / VO
└── campus_delivery_server     # 服务模块：业务核心（Controller / Service / Mapper）
    └── src/main/java/com/campus_delivery
        ├── annotation         # 自定义注解（如 AutoFill 公共字段填充）
        ├── aspect             # AOP 切面
        ├── config             # 配置类（Web、Redis、Knife4j、WebSocket 等）
        ├── controller
        │   ├── admin          # 管理端接口
        │   └── user           # 用户端接口
        ├── handler            # 全局异常处理器等
        ├── interceptor        # JWT 令牌校验拦截器
        ├── mapper             # MyBatis Mapper 接口
        ├── service            # 业务逻辑层
        ├── task               # 定时任务（订单状态处理）
        └── websocket          # WebSocket 服务端点
```

## 🚀 快速开始

### 环境要求

- JDK 8+
- MySQL 8.0
- Redis 6.x
- Maven 3.6+

### 启动步骤

1. **克隆项目**

```bash
git clone git@github.com:YinHuang2004/CampusDelivery-.git
```

2. **准备配置文件**

复制配置模板并填入你自己的环境信息（模板文件已脱敏，真实配置不入库）：

```bash
cd campus_delivery_server/src/main/resources
cp application-dev.yml.example application-dev.yml
```

3. **初始化数据库**

创建 MySQL 数据库并导入表结构与初始数据。

4. **构建并启动**

```bash
mvn clean package -DskipTests
java -jar campus_delivery_server/target/campus_delivery_server-1.0-SNAPSHOT.jar
```

或在 IDE 中直接运行 `CampusDeliveryApplication` 主启动类。

5. **访问接口文档**

启动后打开 Knife4j 在线文档：<http://localhost:8080/doc.html>

## 🔒 配置说明

所有环境相关的敏感配置（数据库密码、Redis 密码、OSS 密钥、微信支付密钥、JWT 密钥等）统一收敛在 `application-dev.yml` 中，该文件已被 `.gitignore` 排除，**不会提交到仓库**。仓库内提供 `application-dev.yml.example` 模板，各配置项含义见文件内注释。

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源。
