package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "custom_measure")
public class CustomMeasure {

	@PartitionKey
	private UUID tubingId;

	@ClusteringColumn(0)
	private String propertyType;

	@ClusteringColumn(1)
	private LocalDateTime timestamp;

	@Column
	private String value;

}
