# Mall-Cloud

本项目是一个基于 Spring Cloud / Alibaba 微服务框架和 Nacos，Gateway，Sentinel，Seata，Redis，Kafka 等企业级常用组件的生鲜电商系统，采用主流的互联网技术架构，支持服务注册和发现、服务熔断降级、分布式事务等，拥有完整的购物下单流程以及后台管理功能，适合二次开发。

项目预览地址：http://cloud.mall.jjzhong.com:3000/ （因服务器配置问题，第一次加载可能较慢，启用浏览器缓存可提高浏览体验）

## 主要功能

### 业务流程

* **购物功能**：包括从用户注册、登录、商品浏览到加入购物车、下单、支付、发货、确认收货等一系列业务流程的处理。
* **后台管理**：管理员后台发货、取消订单、增加/修改/删除商品、订单量统计、批量上传商品等功能。

### 项目特色

* **网关路由动态加载**：使用Nacos作为网关的配置中心，当检测到路由更改时，Gateway 会实时同步路由配置，无需重启网关服务。
* **分布式事务**：为保证事务的一致性，采用 Seata 作为分布式事务解决方案，在下单出错时对全局事务进行回滚。
* **消息驱动微服务**：构建基于 Spring Cloud Stream 的消息驱动微服务，在发送邮箱验证码、返还库存时异步处理，提高响应速度。
* **异步任务**：编写代码实现商品批量入库的异步任务，并利用 AOP 实现对异步任务的实时监控与执行状态查询。

## 技术栈

| 组件 / 框架    | 框架                         | 版本          |
| -------------- | ---------------------------- | ------------- |
| 微服务框架     | Spring Cloud                 | 2021.0.4      |
| 服务注册和发现 | Nacos                        | 2.2.1         |
| 网关           | Spring Cloud Gateway         | 3.1.4         |
| 分布式事务     | Seata                        | 2021.0.4      |
| 数据持久层框架 | MyBatis                      | 2.3.0         |
| 服务调用       | OpenFeign                    | 3.1.4         |
| 分布式缓存     | Redis                        | 3.2.12        |
| 消息队列       | Kafka                        | 2.8.2         |
| 链路追踪       | Spring Cloud Sleuth & Zipkin | 3.1.4 & 2.2.8 |
| 文档           | SpringDoc                    | 1.6.15        |