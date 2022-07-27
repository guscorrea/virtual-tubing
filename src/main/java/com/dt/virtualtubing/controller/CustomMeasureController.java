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

import com.dt.virtualtubing.persistence.CustomMeasureRepository;
import com.dt.virtualtubing.persistence.entity.CustomMeasure;

@RestController
public class CustomMeasureController {

	private final CustomMeasureRepository customMeasureRepository;

	@Autowired
	public CustomMeasureController(CustomMeasureRepository customMeasureRepository) {
		this.customMeasureRepository = customMeasureRepository;
	}

	@GetMapping("/v1/measure")
	public ResponseEntity<List<CustomMeasure>> listMeasures() {
		return new ResponseEntity<>(customMeasureRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/v1/measure/{id}")
	public ResponseEntity<List<CustomMeasure>> getMeasure(@PathVariable("id") String id) {
		return new ResponseEntity<>(customMeasureRepository.findAllByTubing(UUID.fromString(id)), HttpStatus.OK);
	}

	@GetMapping("/v1/measure/{id}/property/{propertyName}")
	public ResponseEntity<List<CustomMeasure>> getMeasureByProperty(@PathVariable("id") String id, @PathVariable("propertyName") String propertyName,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime) {
		if (areDateFiltersInformed(startDateTime, endDateTime)) {
			List<CustomMeasure> customMeasureList = customMeasureRepository.findAllByTubingIdAndTypeAndDateTime(UUID.fromString(id), propertyName,
					startDateTime, endDateTime);
			return new ResponseEntity<>(customMeasureList, HttpStatus.OK);
		}
		return new ResponseEntity<>(customMeasureRepository.findAllByTubingIdAndType(UUID.fromString(id), propertyName), HttpStatus.OK);
	}

	private boolean areDateFiltersInformed(String startDateTime, String endDateTime) {
		return StringUtils.isNotEmpty(startDateTime) && StringUtils.isNotEmpty(endDateTime);
	}

}
