package com.dt.virtualtubing.utils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.dt.virtualtubing.model.MqttTopicData;
import com.dt.virtualtubing.model.enums.MeasurementType;

@Component
public class TopicDataExtractor {

	public MqttTopicData getData(String topic) {
		List<String> stringData = Arrays.asList(topic.split("\\."));

		if(stringData.size() > 3) {
			throw new IllegalArgumentException("Topic contains more than 3 values.");
		}
		UUID componentId = UUID.fromString(stringData.get(1));
		MeasurementType measurementType = MeasurementType.valueOf(stringData.get(2));
		return new MqttTopicData(componentId, measurementType);

	}

}
