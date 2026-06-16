# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

`pxc-framework-boot4` 是一个基于 Spring Boot 4.X 的复合型框架和工具包，用于快速开发。

## 常用命令

```bash
# 构建整个项目
mvn clean package

# 构建跳过测试和格式检查
mvn clean package -DskipTests -DskipFormat

# 单模块构建
mvn clean package -pl pxc-framework-boot4-common -am

# 运行测试
mvn test

# 运行单个测试类
mvn test -Dtest=ClassName

# 格式化代码（validate 阶段自动执行）
mvn spring-javaformat:validate

# 应用代码格式化
mvn spring-javaformat:apply

# 发布（需配置 GPG）
mvn clean package -P release
```

## 模块架构

| 模块 | 说明 |
|------|------|
| `pxc-framework-boot4-bom` | Bill of Materials，统一依赖版本管理 |
| `pxc-framework-boot4-common` | 通用模块：响应封装(R/PageResponse)、异常定义、枚举接口 |
| `pxc-framework-boot4-component` | 通用组件模块 |
| `pxc-framework-boot4-jackson` | Jackson 3.x 自动化配置 |
| `pxc-framework-boot4-crypto` | 加密解密工具模块（encrypt/keygen/utils） |
| `pxc-framework-boot4-util` | 工具类模块（HTTP/IP/日期/正则等） |
| `pxc-framework-boot4-web` | Web 模块：Filter（XSS/CORS/Encoding）、异常处理、Mvc配置 |
| `pxc-framework-boot4-holiday` | 节假日相关模块（select/tree） |
| `pxc-framework-boot4-email` | 邮件发送模块（基于 Spring Boot Mail + Hutool） |
| `pxc-framework-boot4-ip2region` | IP 地址转地理位置模块 |
| `pxc-framework-boot4-redis` | Redis 模块（统一采用 Redisson） |

### 核心模块详情

**pxc-framework-boot4-common**
- `response/R<T>` - 通用响应（`success()`/`fail()`）
- `response/RPage<T>` - 分页响应
- `response/page/PageResponse<T>` - 分页对象响应
- `exception/FrameworkException` - 受检异常
- `exception/FrameworkRuntimeException` - 非受检异常
- `enums/IEnum`、`enums/IResponseEnum` - 枚举接口

**pxc-framework-boot4-web**
- `filter/XssFilter` - XSS 防护过滤器
- `filter/CorsFilter` - 跨域过滤器
- `filter/EncodingFilter` - 请求编码过滤器
- `handler/RestExceptionHandler` - 全局异常处理

## 代码规范

- `spring-javaformat-maven-plugin` 统一代码格式（validate 阶段自动执行）
- `license-maven-plugin` 管理 Apache 2.0 许可证头（year: 2026-2027）
- 所有 Java 文件使用 Lombok（`@Data` 等注解）
- 遵循 Spring Boot 3.x + Jakarta EE 命名规范（jakarta.* 命名空间）
- Parent POM 版本号管理：`${revision}` = 4.0.0

### 关键依赖版本

| 组件 | 版本 |
|------|------|
| Java | 21 |
| Spring Boot | 4.0.7 |
| Spring | 7.0.8 |
| Spring Cloud | 2025.1.1 |
| Jackson | 3.2.0 |
| Hutool | 5.8.46 |
| Redisson | 4.5.0 |
| Mybatis-plus | 3.5.16 |