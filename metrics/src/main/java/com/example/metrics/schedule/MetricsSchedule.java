package com.example.metrics.schedule;

import com.alibaba.fastjson.JSONObject;
import com.example.metrics.entity.HadoopMetricsEntity;
import com.example.metrics.repository.HadoopMetricsRepository;
import com.example.metrics.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricsSchedule {
    @Autowired
    private HadoopMetricsRepository hadoopMetricsRepository;

    @Value("metrics.hadoop")
    private String hadoopUri;

    @Scheduled(cron = "*/5 * * * * ?")
    public void saveHadoopMetrics(){
        HadoopMetricsEntity hadoopMetricsEntity = new HadoopMetricsEntity();
        String jvmMetrics = HttpClientUtil.getContent(hadoopUri, "qry", "Hadoop:service=NameNode,name=JvmMetrics");
        JSONObject jvmMetricsBeans = JSONObject.parseObject(jvmMetrics).getJSONArray("beans").getJSONObject(0);
        Double memNonHeapUsedMB = jvmMetricsBeans.getDouble("MemNonHeapUsedM");
        Double memNonHeapMaxMB = jvmMetricsBeans.getDouble("MemNonHeapMaxM");
        Double memHeapUsedMB = jvmMetricsBeans.getDouble("MemHeapUsedM");
        Double memHeapMaxMB = jvmMetricsBeans.getDouble("MemHeapMaxM");
        hadoopMetricsEntity.setMemNonHeapMaxMB(memNonHeapMaxMB);
        hadoopMetricsEntity.setMemNonHeapUsedMB(memNonHeapUsedMB);
        hadoopMetricsEntity.setMemHeapUsedMB(memHeapUsedMB);
        hadoopMetricsEntity.setMemHeapMaxMB(memHeapMaxMB);

        String fsNameSystem = HttpClientUtil.getContent(hadoopUri, "qry", "Hadoop:service=NameNode,name=FSNamesystem");
        JSONObject fsNameSystemBeans = JSONObject.parseObject(fsNameSystem).getJSONArray("beans").getJSONObject(0);
        double capacityTotalGB = fsNameSystemBeans.getLong("CapacityTotal") / (1024 * 1024 * 1024 * 1.0);
        double capacityUsedGB = fsNameSystemBeans.getLong("CapacityUsed") / (1024 * 1024 * 1024 * 1.0);
        double capacityRemainingGB = fsNameSystemBeans.getLong("CapacityRemaining") / (1024 * 1024 * 1024 * 1.0);
        double capacityUsedNonDFSGB = fsNameSystemBeans.getLong("CapacityUsedNonDFS") / (1024 * 1024 * 1024 * 1.0);
        hadoopMetricsEntity.setCapacityTotalGB(capacityTotalGB);
        hadoopMetricsEntity.setCapacityUsedGB(capacityUsedGB);
        hadoopMetricsEntity.setCapacityRemainingGB(capacityRemainingGB);
        hadoopMetricsEntity.setCapacityUsedNonDFSGB(capacityUsedNonDFSGB);

        String nnInfo = HttpClientUtil.getContent(hadoopUri, "qry", "Hadoop:service=NameNode,name=NameNodeInfo");
        JSONObject nnInfoBeans = JSONObject.parseObject(nnInfo).getJSONArray("beans").getJSONObject(0);
        double blockPoolUsedSpaceMB = nnInfoBeans.getLong("BlockPoolUsedSpace") / (1024 * 1024 * 1.0);
        Double percentBlockPoolUsed = nnInfoBeans.getDouble("PercentBlockPoolUsed");
        Long totalFiles = nnInfoBeans.getLong("TotalFiles");
        Long totalBlocks = nnInfoBeans.getLong("TotalBlocks");
        hadoopMetricsEntity.setBlockPoolUsedSpaceMB(blockPoolUsedSpaceMB);
        hadoopMetricsEntity.setPercentBlockPoolUsed(percentBlockPoolUsed);
        hadoopMetricsEntity.setTotalFiles(totalFiles);
        hadoopMetricsEntity.setTotalBlocks(totalBlocks);

        String fsNameSystemState = HttpClientUtil.getContent(hadoopUri, "qry", "Hadoop:service=NameNode,name=FSNamesystemState");
        JSONObject fsNameSystemStateBeans = JSONObject.parseObject(fsNameSystemState).getJSONArray("beans").getJSONObject(0);
        Integer underReplicatedBlocks = fsNameSystemStateBeans.getInteger("UnderReplicatedBlocks");
        Integer numLiveDataNodes = fsNameSystemStateBeans.getInteger("NumLiveDataNodes");
        Integer numDeadDataNodes = fsNameSystemStateBeans.getInteger("NumDeadDataNodes");
        hadoopMetricsEntity.setUnderReplicatedBlocks(underReplicatedBlocks);
        hadoopMetricsEntity.setNumLiveDataNodes(numLiveDataNodes);
        hadoopMetricsEntity.setNumDeadDataNodes(numDeadDataNodes);

        hadoopMetricsRepository.save(hadoopMetricsEntity);
    }
}
