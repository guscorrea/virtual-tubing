package com.dt.virtualtubing.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
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
@Table(name = "tubing")
public class Tubing {

	@PartitionKey
	private UUID tubingId;

	@Column
	private String name;

	@Column
	private String tubingInfo;

	@Column
	private List<UUID> pdgIdList;

	@Column
	@Builder.Default
	private boolean icvValveIsOpen = false;

	@Column
	private LocalDateTime creationDateTime;

}
