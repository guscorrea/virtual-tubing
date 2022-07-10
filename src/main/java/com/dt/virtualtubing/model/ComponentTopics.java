package com.dt.virtualtubing.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.dt.virtualtubing.model.enums.MeasurementType;
import lombok.Data;

@Data
public class ComponentTopics {

	private static final String TUBING_TOPIC = "tubing.";

	private static final String PRESSURE = "." + MeasurementType.pressure;

	private static final String TEMPERATURE = "." + MeasurementType.temperature;

	private static final String CUSTOM = "." + MeasurementType.custom;

	private final String componentId;

	private final String baseTopicName;

	private final String pressureTopicName;

	private final String temperatureTopicName;

	private final String customTopicName;

	@Autowired
	public ComponentTopics(String componentId) {
		this.componentId = componentId;
		this.baseTopicName = TUBING_TOPIC + componentId;
		this.pressureTopicName = TUBING_TOPIC + componentId + PRESSURE;
		this.temperatureTopicName = TUBING_TOPIC + componentId + TEMPERATURE;
		this.customTopicName = TUBING_TOPIC + componentId + CUSTOM;
	}

}
