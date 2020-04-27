package com.example.metrics.schedule;

import com.example.metrics.repository.HadoopMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricsSchedule {
    @Autowired
    private HadoopMetricsRepository hadoopMetricsRepository;

    @Scheduled(cron = "*/5 * * * * ?")
    public void getHadoopMetrics(){
        //TODO 使用HttpClientUtil类获取监控数据并写入MySQL
    }
}
