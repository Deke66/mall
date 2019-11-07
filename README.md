## spring-boot练手项目

### 目的
- 熟悉spring boot框架

### 场景
- 网上下单库存扣减

### 用到的组件或技术
- 唯一Id（雪花算法）
- 异步任务（spring-boot-asyn&&spring-boot-Scheduling）
- 本地缓存（guava cache）
- 第三方缓存（Redis）
- 日志追踪（slf4j)
- MySQL

### 实现
- 3种实现
    1. 不采用缓存，直接数据库实现缓存扣减
    2. 采用本地缓存，先加载数据，再进行扣减，定时将缓存数据同步数据库
    3. 采用redis缓存，先加载数据，在reids中进行缓存扣减，定时同步数据库
  
### 部署与测试
- 部署在腾讯云，用JMeter进行测试



