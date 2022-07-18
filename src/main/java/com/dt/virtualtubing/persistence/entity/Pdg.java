package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pdg")
public class Pdg {

	@PartitionKey
	private UUID pdgId;

	@Column
	private UUID tubingId;

	@Column
	private String name;

	@Column
	private String pdgInfo;

	@Column
	private LocalDateTime creationDateTime;

}
