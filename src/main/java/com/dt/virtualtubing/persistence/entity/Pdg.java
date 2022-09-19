package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pdg")
@Schema(description = "PDG resource")
public class Pdg {

	@PartitionKey
	@Schema(description = "The virtual PDG unique identifier", example = "ccf9e52b-e2e4-45d8-8884-0721d3246a53")
	private UUID pdgId;

	@Column
	@Schema(description = "The virtual Tubing unique identifier", example = "ccf9e52b-e2e4-45d8-8884-0721d3246a53")
	private UUID tubingId;

	@Column
	@Schema(description = "The name of the PDG resource", required = true, example = "PDG #1")
	private String name;

	@Column
	@Schema(description = "Additional information for PDG resource", example = "Additional info")
	private String pdgInfo;

	@Column
	@Schema(description = "Resource creation date and time")
	private LocalDateTime creationDateTime;

}
