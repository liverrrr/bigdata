package com.example.metrics.repository;

import com.example.metrics.entity.HadoopMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HadoopMetricsRepository extends JpaRepository<HadoopMetricsEntity,Long> {
}
