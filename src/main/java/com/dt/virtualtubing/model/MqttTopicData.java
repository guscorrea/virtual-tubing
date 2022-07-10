package com.dt.virtualtubing.model;

import java.util.UUID;

import com.dt.virtualtubing.model.enums.MeasurementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicData {

	private UUID componentId;

	private MeasurementType measurementType;

}
