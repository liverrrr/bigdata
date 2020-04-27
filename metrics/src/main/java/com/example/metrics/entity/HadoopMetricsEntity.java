package com.example.metrics.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class HadoopMetricsEntity extends BaseEntity {
    private Double memNonHeapUsedMB;
    private Double memNonHeapMaxMB;
    private Double memHeapUsedMB;
    private Double memHeapMaxMB;
    private Double capacityTotalGB;
    private Double capacityUsedGB;
    private Double capacityRemainingGB;
    private Double capacityUsedNonDFSGB;
    private Double blockPoolUsedSpaceMB;
    private Double percentBlockPoolUsed;
    private Long totalBlocks;
    private Long totalFiles;
    private Integer numLiveDataNodes;
    private Integer underReplicatedBlocks;

    public Double getMemNonHeapUsedMB() {
        return memNonHeapUsedMB;
    }

    public void setMemNonHeapUsedMB(Double memNonHeapUsedMB) {
        this.memNonHeapUsedMB = memNonHeapUsedMB;
    }

    public Double getMemNonHeapMaxMB() {
        return memNonHeapMaxMB;
    }

    public void setMemNonHeapMaxMB(Double memNonHeapMaxMB) {
        this.memNonHeapMaxMB = memNonHeapMaxMB;
    }

    public Double getMemHeapUsedMB() {
        return memHeapUsedMB;
    }

    public void setMemHeapUsedMB(Double memHeapUsedMB) {
        this.memHeapUsedMB = memHeapUsedMB;
    }

    public Double getMemHeapMaxMB() {
        return memHeapMaxMB;
    }

    public void setMemHeapMaxMB(Double memHeapMaxMB) {
        this.memHeapMaxMB = memHeapMaxMB;
    }

    public Double getCapacityTotalGB() {
        return capacityTotalGB;
    }

    public void setCapacityTotalGB(Double capacityTotalGB) {
        this.capacityTotalGB = capacityTotalGB;
    }

    public Double getCapacityUsedGB() {
        return capacityUsedGB;
    }

    public void setCapacityUsedGB(Double capacityUsedGB) {
        this.capacityUsedGB = capacityUsedGB;
    }

    public Double getCapacityRemainingGB() {
        return capacityRemainingGB;
    }

    public void setCapacityRemainingGB(Double capacityRemainingGB) {
        this.capacityRemainingGB = capacityRemainingGB;
    }

    public Double getCapacityUsedNonDFSGB() {
        return capacityUsedNonDFSGB;
    }

    public void setCapacityUsedNonDFSGB(Double capacityUsedNonDFSGB) {
        this.capacityUsedNonDFSGB = capacityUsedNonDFSGB;
    }

    public Double getBlockPoolUsedSpaceMB() {
        return blockPoolUsedSpaceMB;
    }

    public void setBlockPoolUsedSpaceMB(Double blockPoolUsedSpaceMB) {
        this.blockPoolUsedSpaceMB = blockPoolUsedSpaceMB;
    }

    public Double getPercentBlockPoolUsed() {
        return percentBlockPoolUsed;
    }

    public void setPercentBlockPoolUsed(Double percentBlockPoolUsed) {
        this.percentBlockPoolUsed = percentBlockPoolUsed;
    }

    public Long getTotalBlocks() {
        return totalBlocks;
    }

    public void setTotalBlocks(Long totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Integer getNumLiveDataNodes() {
        return numLiveDataNodes;
    }

    public void setNumLiveDataNodes(Integer numLiveDataNodes) {
        this.numLiveDataNodes = numLiveDataNodes;
    }

    public Integer getUnderReplicatedBlocks() {
        return underReplicatedBlocks;
    }

    public void setUnderReplicatedBlocks(Integer underReplicatedBlocks) {
        this.underReplicatedBlocks = underReplicatedBlocks;
    }
}
