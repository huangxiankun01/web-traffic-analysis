# web-traffic-analysis

# four Parts

# tracker: 主要用于采集用户访问网站行为的数据

# tracker-logServer: 用于接收tracker发送过来的日志信息并且存储在本地文件中

# backend: 用于数据清洗解析，入库到Hive
    # weblog-preparser: 解析收集到的日志，将之生成一个对应原始数据表（rawdata.web）里的实体类
    # spark-preparse-etl: 原始数据日志的预解析，保存数据到Hive中
# frontend: 用于数据分析和可视化