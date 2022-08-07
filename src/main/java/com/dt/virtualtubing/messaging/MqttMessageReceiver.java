package com.dt.virtualtubing.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.dt.virtualtubing.model.MqttRequest;
import com.dt.virtualtubing.persistence.CustomMeasureRepository;
import com.dt.virtualtubing.persistence.PressureRepository;
import com.dt.virtualtubing.persistence.TemperatureRepository;
import com.dt.virtualtubing.persistence.entity.CustomMeasure;
import com.dt.virtualtubing.persistence.entity.Pressure;
import com.dt.virtualtubing.persistence.entity.Temperature;
import com.dt.virtualtubing.utils.TopicDataExtractor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MqttMessageReceiver implements MessageHandler {

	private final ObjectMapper objectMapper;

	private final TopicDataExtractor topicDataExtractor;

	private final TemperatureRepository temperatureRepository;

	private final PressureRepository pressureRepository;

	private final CustomMeasureRepository customMeasureRepository;

	@Autowired
	public MqttMessageReceiver(ObjectMapper objectMapper, TopicDataExtractor topicDataExtractor, TemperatureRepository temperatureRepository,
			PressureRepository pressureRepository, CustomMeasureRepository customMeasureRepository) {
		this.objectMapper = objectMapper;
		this.topicDataExtractor = topicDataExtractor;
		this.temperatureRepository = temperatureRepository;
		this.pressureRepository = pressureRepository;
		this.customMeasureRepository = customMeasureRepository;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
			MqttRequest mqttRequest = objectMapper.readValue(message.getPayload().toString(), MqttRequest.class);
			mqttRequest.setMqttTopicData(topicDataExtractor.getData(topic));
			log.info("Message consumed from {}  with LocalDateTime: {} and value: {}", topic, mqttRequest.getTimeStamp(), mqttRequest.getValue());

			switch (mqttRequest.getMqttTopicData().getMeasurementType()) {
			case temperature:
				saveTemperature(mqttRequest);
				break;
			case pressure:
				savePressure(mqttRequest);
				break;
			case custom:
				saveCustomMeasure(mqttRequest);
			}
		}
		catch (JsonProcessingException | IllegalArgumentException e) {
			log.error("Error deserializing Mqtt Request {}", message.getPayload());
		}
	}

	private void savePressure(MqttRequest mqttRequest) {
		Pressure pressure = new Pressure(mqttRequest.getMqttTopicData().getComponentId(), mqttRequest.getTimeStamp(), mqttRequest.getValue());
		pressureRepository.save(pressure);
	}

	private void saveTemperature(MqttRequest mqttRequest) {
		Temperature temperature = new Temperature(mqttRequest.getMqttTopicData().getComponentId(), mqttRequest.getTimeStamp(),
				mqttRequest.getValue());
		temperatureRepository.save(temperature);
	}


	private void saveCustomMeasure(MqttRequest mqttRequest) {
		CustomMeasure customMeasure = new CustomMeasure(mqttRequest.getMqttTopicData().getComponentId(), mqttRequest.getPropertyType(),
				mqttRequest.getTimeStamp(), mqttRequest.getValue());
		customMeasureRepository.save(customMeasure);
	}

}