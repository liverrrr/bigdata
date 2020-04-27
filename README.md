# bigdata

## flink
存放Flink相关的学习练手程序，同时也在尝试翻译Flink官方文档，链接如下：<br>
✍️ [我的博客](https://liverrrr.fun/tags/flink)
<br>
🐦 [我的语雀](https://www.yuque.com/liverrrr/bigdata)
<br>
欢迎小伙伴们来给我提意见👏👏

## spark
存放Scala、Flume、Hadoop、Hive(UDF)、ZK、Spark相关学习练手程序

## generate
Springboot Web 项目用于模拟大数据项目中日志生成

## metrics
Springboot Web + JPA 用于定时获取各类监控数据并提供接口查询

## plan
- [ ] 翻译 Flink 官网文档
  - [ ] 在翻译文章系列一中增添以下：
    - [ ] Job Managers, Task Managers, Clients
    - [ ] Task Slots and Resources
  - [ ] 在翻译文章系列五中添加：Barriers
  - [ ] 在翻译文章系列六中添加：Window Join
  - [ ] 翻译 Transformation 和 AsyncI/O
  - [ ] 翻译 Table SQL
  - [ ] 翻译 Deployment
  - [ ] 翻译 Debugging 和 Monitoring
- [ ] 完成日志上报接口并写好测试用例(generate)
- [ ] 定时获取监控数据并提供基于时间的接口查询(metrics)
  - [ ] Flume
  - [ ] Maxwell
  - [ ] Hadoop
  - [ ] HBase
- [ ] 设计可以自定义报警规则的报警系统(邮件、短信、机器人)