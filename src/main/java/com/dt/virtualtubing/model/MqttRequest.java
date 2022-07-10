package com.dt.virtualtubing.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MqttRequest {

	private MqttTopicData mqttTopicData;

	private LocalDateTime timeStamp;

	private String value;

	private String propertyType;

}
