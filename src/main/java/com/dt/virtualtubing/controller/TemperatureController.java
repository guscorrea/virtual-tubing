package com.dt.virtualtubing.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dt.virtualtubing.persistence.TemperatureRepository;
import com.dt.virtualtubing.persistence.entity.Temperature;

@RestController
public class TemperatureController {

	private final TemperatureRepository temperatureRepository;

	@Autowired
	public TemperatureController(TemperatureRepository temperatureRepository) {
		this.temperatureRepository = temperatureRepository;
	}

	@GetMapping("/v1/temperature")
	public ResponseEntity<List<Temperature>> listTemperature() {
		return new ResponseEntity<>(temperatureRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/v1/temperature/{id}")
	public ResponseEntity<List<Temperature>> getTemperature(@PathVariable("id") String id, @RequestParam(required = false) String startDateTime,
			@RequestParam(required = false) String endDateTime) {
		if (areDateFiltersInformed(startDateTime, endDateTime)) {
			List<Temperature> temperatureList = temperatureRepository.findAllByTubingIdAndDateTime(UUID.fromString(id), startDateTime,
					endDateTime);
			return new ResponseEntity<>(temperatureList, HttpStatus.OK);
		}
		return new ResponseEntity<>(temperatureRepository.findAllByTubingId(UUID.fromString(id)), HttpStatus.OK);
	}

	private boolean areDateFiltersInformed(String startDateTime, String endDateTime) {
		return StringUtils.isNotEmpty(startDateTime) && StringUtils.isNotEmpty(endDateTime);
	}

}
