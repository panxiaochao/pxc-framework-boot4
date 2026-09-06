# pxc-framework-boot4 Code Review 报告

> 来源：`result01.json`（OCR 工具输出）
> 模型：qwen3.8-max | 审查 158 个文件 / 475 条评论 | 耗时 52 分 11 秒

## 总览

| 严重程度 | 数量 |
|---------|------|
| 🔴 critical | 3 |
| 🟠 high | 103 |
| 🟡 medium | 250 |
| ⚪ low | 108 |

| 类别 | 数量 |
|------|------|
| bug | 296 |
| maintainability | 86 |
| security | 29 |
| other | 22 |
| documentation | 15 |
| performance | 7 |
| style | 3 |

## 一、3 个 Critical 问题（必须立即处理）

1. **Redis 反序列化 RCE 风险** — `Boot4RedissonAutoConfiguration`
   编解码器接受所有子类型，`@class` 元数据可请求任意类，存在 gadget-based RCE 或 DoS 风险。应限制允许的包/基类型白名单。

2. **IpUtil 无法编译** — `IpUtil.java`
   `NetworkInterface.getInetAddresses()` 抛出 `SocketException`，调用处既无 try/catch 也未声明抛出，类无法编译。

3. **QRCodeUtil 无限递归** — `QRCodeUtil.java`
   `read(String)` 返回 String 文本，导致 `readRawBytes` 各重载无限递归直至 `StackOverflowError`。应先读图片再委托给 `readRawBytes(BufferedImage)`。

## 二、按模块分布

| 模块 | 评论数 | High | 说明 |
|------|-------|------|------|
| util | 284 | 64 | 问题最多：DDL 生成、Jackson、正则、系统信息等 |
| redis | 34 | 6 | 编解码安全、限流器 TTL、自动配置顺序 |
| web | 31 | 9 | CORS、XSS 过滤器绕过、异常处理泄漏 |
| common | 23 | 2 | 分页 DTO 空值/除零、常量设计 |
| mybatis-plus | 21 | 3 | 默认 Bean 缺 `@ConditionalOnMissingBean` |
| crypto | 21 | 8 | 全部为安全问题（ECB、缺 MAC、弱 KDF） |
| component | 20 | 4 | 可变 key 的 equals/hashCode、builder 空值 |
| email | 15 | 3 | 静态可变账号、收件人分隔符错误 |
| ip2region | 14 | 2 | 静态 searcher 可见性、null 不安全 destroy |
| holiday | 9 | 2 | — |
| jackson | 3 | 1 | `BigNumberSerializer` 产生非法 JSON |

## 三、核心热点问题（按模块归类）

### Crypto（加密模块，全部是安全问题）
- `AesBytesEncryptor` 用 ECB 模式无认证标签，无法检测篡改
- 密码直接用 `SecureRandom` 种子而非 KDF 派生
- `SharedKeyGenerator.generateKey()` 返回内部数组，可被调用方破坏密钥

### DDL 生成（util/meta/ddl）
- DB2/Oracle/Postgres 实现返回空串/空列表，调用方无法区分"无 DDL"还是"不支持"，导致这些库导库静默丢表
- MySQL/DM/Abstract 的标识符/字面量转义不完整，存在 SQL 注入/断句风险

### 限流与防重（redis）
- `RepeatSubmitLimiterAspect` 把 `interval` 当毫秒，无视 `timeUnit`（如 `2 MINUTES` 实际 2ms 过期）
- 限流器 TTL 等于窗口长度，令牌状态每个窗口被删光，限流失效
- 防重切面用单线程本地槽，嵌套调用会覆盖外层 key

### 分页 DTO（common）
- `PageRequest` 接受 `pageNo<=0` / `pageSize<=0`；`Pagination` 在 `pageSize==0` 时除零
- `OrderItems.column` 未校验，可能被拼接进 `ORDER BY`（SQL 注入）
- `RPageObject` 接受 null 分页，构造"成功"但 data 为 null 的响应

### 通用工具异常吞噬
- `SpringContextUtil.getBean`、`JdbcUtil.getConnection`、`ExceptionUtil` 等把真实异常转成 null/通用包装，生产故障难排查

### 自动配置
- email/mybatis-plus 缺 `@ConditionalOnMissingBean` 导致重复 Bean
- 限流切面无条件注册，且 `@AutoConfiguration(after=...)` 引用了错误的 Spring Data 接口
- `FilterAutoConfiguration` 用大小写敏感方式比较 `@ConditionalOnProperty`

## 四、横向共性问题

1. **API 边界无空值校验**：几十处公共方法/DTO 接受 null 后 NPE（`R.isOk`、`Pagination`、`EmailUtil`、`SelectBuilder`…）
2. **异常吞噬**：`SpringContextUtil`、`DownLoadUtil`、`JdbcUtil`、`RateLimiterAspect` 等掩盖原始错误
3. **静态可变状态**：`EmailUtil.ACCOUNT`、`Ip2regionClient.IP_SEARCHER`、`SpringContextUtil.applicationContext` 等无安全发布
4. **可变字段进 equals/hashCode**：`SelectOption`、`Tree`、`Coordinate`、`IndexMeta` 用作 Map/Set key 不安全
5. **文档/实现不一致**：`ServletResponseEnum` 误标"请求方法不支持"、`RateLimiter` Javadoc 承诺类型不对等

## 五、可快速修复的项

- AES 改 AES-GCM + 随机 nonce（一处解决机密性+完整性）
- 补 `@ConditionalOnMissingBean`、修正 `after` 引用
- 修复 `RepeatSubmitLimiterAspect` 用 `timeUnit.toMillis()`
- 校验 `Pagination`/`PageRequest` 的 `pageSize>=1`、`pageNo>=1`
- 修复正则池手机号/QQ 模式、`RandomUtil` 边界 +1 溢出、`EmailUtil` 分隔符、`IpUtil` 缺 break 等小问题

## 附：工具调用失败记录（4 次，影响可忽略）

| 序号 | 工具 | 文件 | 原因 |
|------|------|------|------|
| 40 | code_search | OrderItems.java | 参数解析错误（`*` 非法字符） |
| 216 | code_search | RateLimiterAutoConfiguration.java | git grep 括号未闭合 |
| 416 | code_search | CustomizeJavaTimeModule.java | git grep 括号未闭合 |
| 478 | file_read | SystemServerUtil.java | 文件路径不存在 |
