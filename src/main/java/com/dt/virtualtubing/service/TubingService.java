package com.dt.virtualtubing.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dt.virtualtubing.config.MqttConfig;
import com.dt.virtualtubing.exception.TubingNotFoundException;
import com.dt.virtualtubing.model.ComponentTopics;
import com.dt.virtualtubing.model.TubingRequest;
import com.dt.virtualtubing.persistence.TubingRepository;
import com.dt.virtualtubing.persistence.entity.Tubing;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TubingService {

	@Resource
	private MqttConfig mqttConfig;

	private final TubingRepository tubingRepository;

	@Autowired
	public TubingService(TubingRepository tubingRepository) {
		this.tubingRepository = tubingRepository;
	}

	public List<Tubing> getAllTubings() {
		return tubingRepository.findAll();
	}

	public Tubing getTubing(UUID id) {
		Tubing tubing = tubingRepository.find(id);
		if (Objects.isNull(tubing)) {
			log.error("Tubing with id {] not found in the DB.", id);
			throw new TubingNotFoundException("Tubing with id " + id.toString() + " not found in the database.");
		}
		return tubing;
	}

	public Tubing saveTubing(TubingRequest tubingRequest) {
		log.info("Creating Tubing with name {}", tubingRequest.getName());
		Tubing tubing = Tubing.builder()
				.tubingId(UUID.randomUUID())
				.name(tubingRequest.getName())
				.tubingInfo(tubingRequest.getTubingInfo())
				.creationDateTime(LocalDateTime.now())
				.build();
		addTopics(tubing);
		return tubingRepository.save(tubing);
	}

	public Tubing updateTubing(UUID id, TubingRequest tubingRequest) {
		Tubing tubing = getTubing(id);
		log.info("Updating tubing with id {}", id);
		tubing.setName(tubingRequest.getName());
		tubing.setTubingInfo(StringUtils.defaultIfEmpty(tubingRequest.getTubingInfo(), tubing.getTubingInfo()));
		tubing.setIcvValveIsOpen(Objects.isNull(tubingRequest.getIcvValveIsOpen()) ? tubing.isIcvValveIsOpen() : tubingRequest.getIcvValveIsOpen());
		return tubingRepository.save(tubing);
	}

	public void deleteTubing(UUID id) {
		log.info("Deleting tubing with id {}", id);
		tubingRepository.delete(id);
		removeDefaultTopics(id);
	}

	public void addPdgToTubing(Tubing tubing, UUID pdgId) {
		log.info("Adding pdg {}  to Tubing with id: {}", pdgId.toString(), tubing.getTubingId());
		if (tubingHasNoPdgs(tubing)) {
			tubing.setPdgIdList(Collections.singletonList(pdgId));
			tubingRepository.save(tubing);
			return;
		}
		tubing.getPdgIdList().add(pdgId);
		tubingRepository.save(tubing);
	}

	public void removePdgFromTubing(UUID tubingId, UUID pdgId) {
		log.info("Removing pdg {}  from Tubing with id: {}", pdgId.toString(), tubingId.toString());
		Tubing tubing = tubingRepository.find(tubingId);
		if (Objects.isNull(tubing)) {
			return;
		}
		tubing.getPdgIdList().remove(pdgId);
		tubingRepository.save(tubing);
	}

	private void addTopics(Tubing tubing) {
		ComponentTopics newComponentTopics = new ComponentTopics(tubing.getTubingId().toString());

		String temperatureTopic = newComponentTopics.getTemperatureTopicName();
		log.info("Adding new topic: {}", temperatureTopic);
		mqttConfig.adapter.addTopic(temperatureTopic, 2);

		String pressureTopic = newComponentTopics.getPressureTopicName();
		log.info("Adding new topic: {}", pressureTopic);
		mqttConfig.adapter.addTopic(pressureTopic, 2);

		String customTopic = newComponentTopics.getCustomTopicName();
		log.info("Adding new topic: {}", customTopic);
		mqttConfig.adapter.addTopic(customTopic, 2);

	}

	private void removeDefaultTopics(UUID id) {
		ComponentTopics componentTopics = new ComponentTopics(id.toString());
		log.info("Removing default topics for: {}", id);
		mqttConfig.adapter.removeTopic(componentTopics.getTemperatureTopicName(),
				componentTopics.getPressureTopicName(),
				componentTopics.getCustomTopicName());
	}

	private boolean tubingHasNoPdgs(Tubing tubing) {
		return Objects.isNull(tubing.getPdgIdList());
	}

}
