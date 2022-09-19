package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "custom_measure")
@Schema(description = "Custom Measure resource")
public class CustomMeasure {

	@PartitionKey
	@Schema(description = "The virtual Tubing / PDG unique identifier", example = "ccf9e52b-e2e4-45d8-8884-0721d3246a53")
	private UUID tubingId;

	@ClusteringColumn(0)
	@Schema(description = "Name of the custom measure property", example = "Density")
	private String propertyType;

	@ClusteringColumn(1)
	@Schema(description = "Data generation timestamp")
	private LocalDateTime timestamp;

	@Column
	@Schema(description = "Measured value", example = "36.2421")
	private String value;

}
