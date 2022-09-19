package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
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
@Table(name = "tubing")
@Schema(description = "Tubing resource")
public class Tubing {

	@PartitionKey
	@Schema(description = "The virtual Tubing unique identifier", example = "ccf9e52b-e2e4-45d8-8884-0721d3246a53")
	private UUID tubingId;

	@Column
	@Schema(description = "The name of the Tubing resource", required = true, example = "Tubing #1")
	private String name;

	@Column
	@Schema(description = "Additional information for Tubing resource", example = "Additional info")
	private String tubingInfo;

	@Column
	@Schema(description = "List of associated PDGs with respective tubing resource")
	private List<UUID> pdgIdList;

	@Column
	@Builder.Default
	@Schema(description = "ICV Valve open (true) or closed (false) status", defaultValue = "false")
	private boolean icvValveIsOpen = false;

	@Column
	@Schema(description = "Resource creation date and time")
	private LocalDateTime creationDateTime;

}
