# pxc-framework-boot4 修复计划（按优先级）

> 依据 `code-review-summary.md` 与 `result01.json`（475 条评论）整理。
> 策略：P0 阻断性 → P1 高正确性/安全 → P2 中健壮性 → P3 低清理。

---

## P0 — 阻断性风险（建议优先，1~2 个迭代）

| # | 项 | 模块 | 风险 | 建议动作 |
|---|----|------|------|---------|
| 2 | `IpUtil` 编译失败（`getInetAddresses()` 未捕获） | util | 🔴 编译阻断 | try/catch 跳过出错网卡；顺带修 IPv6 越界 + 缺 break |
| 3 | `QRCodeUtil.readRawBytes` 无限递归 | util | 🔴 运行时崩溃 | 三个入口改走 `ImageIO.read` 后再委托 BufferedImage 版本 |
| 4 | AES 用 ECB 无认证标签 + 弱 KDF + 密钥泄漏 | crypto | 🔴 安全 | 改 AES-GCM + 随机 nonce；KDF 派生；`generateKey` 返回副本 |
| 5 | DDL 生成对 DB2/Oracle/Postgres 静默返回空 | util(ddl) | 🟠 数据损失 | 明确"不支持"信号或补齐实现，避免导出丢表 |

---

## P1 — 高风险正确性/安全（随 P0 后 1 个迭代）

| # | 项 | 模块 | 风险 | 建议动作 |
|---|----|------|------|---------|
| 6 | `OrderItems.column` 未校验 → ORDER BY 注入 | common | 🟠 安全 | 绑定期白名单校验排序字段 |
| 7 | `Pagination`/`PageRequest` pageSize=0 除零、pageNo<1 | common | 🟠 崩溃 | 构造器/setter 校验 `>=1` |
| 8 | SQL 注入：DDL 标识符/字面量转义不完整 | util(ddl) | 🟠 安全 | 完善 backtick/引号/注释转义 |
| 9 | 限流器 TTL=窗口长，令牌每周期清空失效 | redis | 🟠 功能失效 | TTL 改为窗口外更长/独立过期 |
| 10 | `RepeatSubmitLimiterAspect` 无视 timeUnit（MINUTES 当 ms） | redis | 🟠 功能失效 | 用 `timeUnit.toMillis()` 转换 |
| 11 | 防重切面嵌套调用覆盖外层 key | redis | 🟠 功能失效 | 栈/计数器方式支持嵌套 |
| 12 | `SelectOption.setKey` 可变字段入 equals/hashCode | component | 🟠 数据错误 | 身份字段不可变 或 模型重建 |
| 13 | XSS/CORS/异常处理 泄漏原始消息 & 反射任意源 | web | 🟠 安全 | 白名单来源、mapping 消息、区分 4xx/5xx |

---

## P2 — 中风险健壮性/正确性（分布到 2~3 个迭代）

| # | 项 | 模块 | 风险 |
|---|----|------|------|
| 14 | `R.isOk`/`ObjectUtil.equals`/`SelectBuilder`/`EmailUtil` 等 null 无守护 | 全模块 | NPE |
| 15 | `SpringContextUtil.getBean`/`JdbcUtil`/`ExceptionUtil` 吞噬异常返回 null | util | 难排查 |
| 16 | Email 收件人冒号/分号错误、流未关闭、静态 ACCOUNT | email | 功能/并发 |
| 17 | `BigNumberSerializer` 输出两段 JSON token、null 转 `""` | util | 非法 JSON |
| 18 | 自动配置缺 `@ConditionalOnMissingBean`、`after` 引用错误接口 | e-mail/mybatis/redis | 重复 Bean |
| 19 | `Ip2regionClient.IP_SEARCHER` 静态可见性、destroy null | ip2region | 并发/崩溃 |
| 20 | 静态共享可变状态（ACCOUNT/IP_SEARCHER/applicationContext） | 全模块 | 线程安全 |
| 21 | 内部 IP 判定、正则池手机号/QQ、`RandomUtil` +1 溢出 | util | 逻辑错误 |
| 22 | R「成功」语义：OK_0 vs OK、`ok(code,...)` 可造任意失败码 | common | 语义不一致 |
| 23 | `SpringContextUtil.applicationContext` 非 volatile | util | 可见性 |

---

## P3 — 低风险清理/文档（穿插或留待重构）

| # | 项 | 模块 |
|---|----|------|
| 24 | `ServletResponseEnum` 误标"请求方法不支持"（2 处） | common |
| 25 | `totalPages` 死字段 / `searchCount` schema 文案错误 | common |
| 26 | 文档/实现漂移（DateContext、IDatabase、RateLimiter Javadoc 等） | 全模块 |
| 27 | 常量混乱（OK=Integer vs SUCCESS=String、IS_DELETE=STATUS_NORMAL=1） | common |
| 28 | 可变集合返回内部引用、builder 暴露内部 list | component |
| 29 | 平台默认字符集（HexUtil/Base64Util/PropertiesUtil） | util |

---

## 建议执行顺序

```
P0  (5 项)  阻断：安全 + 编译 + 数据损失        → 首批 1 个迭代
P1  (8 项)  应用级正确性 + 注入                → 首批/二批
P2  (10 项) 健壮性 + 并发 + 功能               → 二~四批
P3  (6 项)  文档 + 清理                        → 穿插
```

---

## 待决策开放项

1. 修复范围：全部修，还是只修 P0+P1？
2. crypto AES 改造是否接受破坏性变更（老密文需重新加密/提供迁移兼容）？
3. DDL 采用补齐实现 vs 显式抛"不支持"（影响面小，推荐后者先保安全）？
4. Redis 白名单默认允许包范围如何界定？
5. 后续修复方式：逐项 TDD/单测，还是批量改后集中跑现有测试？
