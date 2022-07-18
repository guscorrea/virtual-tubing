package com.dt.virtualtubing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dt.virtualtubing.config.MqttConfig;
import com.dt.virtualtubing.exception.PdgNotFoundException;
import com.dt.virtualtubing.model.ComponentTopics;
import com.dt.virtualtubing.model.PdgRequest;
import com.dt.virtualtubing.persistence.PdgRepository;
import com.dt.virtualtubing.persistence.entity.Pdg;
import com.dt.virtualtubing.persistence.entity.Tubing;

@Service
public class PdgService {

	@Resource
	private MqttConfig mqttConfig;

	private final PdgRepository pdgRepository;

	private final TubingService tubingService;

	@Autowired
	public PdgService(PdgRepository pdgRepository, TubingService tubingService) {
		this.pdgRepository = pdgRepository;
		this.tubingService = tubingService;
	}

	public List<Pdg> getAllPdgs() {
		return pdgRepository.findAll();
	}

	public Pdg getPdg(UUID id) {
		Pdg pdg = pdgRepository.find(id);
		if (Objects.isNull(pdg)) {
			throw new PdgNotFoundException("Pdg with id " + id.toString() + " not found in the database.");
		}
		return pdg;
	}

	public Pdg savePdg(UUID tubingId, PdgRequest pdgRequest) {
		Tubing tubing = tubingService.getTubing(tubingId);
		System.out.println("Creating Pdg with name " + pdgRequest.getName());
		Pdg pdg = Pdg.builder()
				.pdgId(UUID.randomUUID())
				.tubingId(tubingId)
				.name(pdgRequest.getName())
				.pdgInfo(pdgRequest.getPdgInfo())
				.creationDateTime(LocalDateTime.now())
				.build();
		tubingService.addPdgToTubing(tubing, pdg.getPdgId());
		addTopics(pdg);
		return pdgRepository.save(pdg);
	}

	public Pdg updatePdg(UUID id, PdgRequest pdgRequest) {
		Pdg pdg = getPdg(id);
		System.out.println("Updating pdg with id " + id);
		pdg.setName(pdgRequest.getName());
		pdg.setPdgInfo(StringUtils.defaultIfEmpty(pdgRequest.getPdgInfo(), pdg.getPdgInfo()));
		return pdgRepository.save(pdg);
	}

	public void deletePdg(UUID id) {
		System.out.println("Deleting pdg with id " + id);
		Pdg pdg = getPdg(id);
		tubingService.removePdgFromTubing(pdg.getTubingId(), pdg.getPdgId());
		pdgRepository.delete(pdg.getPdgId());
		removeDefaultTopics(id);
	}

	private void addTopics(Pdg pdg) {
		ComponentTopics newComponentTopics = new ComponentTopics(pdg.getPdgId().toString());

		String temperatureTopic = newComponentTopics.getTemperatureTopicName();
		System.out.println("Adding new topic: " + temperatureTopic);
		mqttConfig.adapter.addTopic(temperatureTopic, 2);

		String pressureTopic = newComponentTopics.getPressureTopicName();
		System.out.println("Adding new topic: " + pressureTopic);
		mqttConfig.adapter.addTopic(pressureTopic, 2);

		String customTopic = newComponentTopics.getCustomTopicName();
		System.out.println("Adding new topic: " + customTopic);
		mqttConfig.adapter.addTopic(customTopic, 2);

	}

	private void removeDefaultTopics(UUID id) {
		ComponentTopics componentTopics = new ComponentTopics(id.toString());
		mqttConfig.adapter.removeTopic(componentTopics.getTemperatureTopicName(),
				componentTopics.getPressureTopicName(),
				componentTopics.getCustomTopicName());
	}

}
