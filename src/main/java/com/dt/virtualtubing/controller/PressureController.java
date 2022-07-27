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

import com.dt.virtualtubing.persistence.PressureRepository;
import com.dt.virtualtubing.persistence.entity.Pressure;

@RestController
public class PressureController {

	private final PressureRepository pressureRepository;

	@Autowired
	public PressureController(PressureRepository pressureRepository) {
		this.pressureRepository = pressureRepository;
	}

	@GetMapping("/v1/pressure")
	public ResponseEntity<List<Pressure>> listPressure() {
		return new ResponseEntity<>(pressureRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/v1/pressure/{id}")
	public ResponseEntity<List<Pressure>> getPressure(@PathVariable("id") String id, @RequestParam(required = false) String startDateTime,
			@RequestParam(required = false) String endDateTime) {
		if (areDateFiltersInformed(startDateTime, endDateTime)) {
			List<Pressure> pressureList = pressureRepository.findAllByTubingIdAndDateTime(UUID.fromString(id), startDateTime, endDateTime);
			return new ResponseEntity<>(pressureList, HttpStatus.OK);
		}
		return new ResponseEntity<>(pressureRepository.findAllByTubingId(UUID.fromString(id)), HttpStatus.OK);
	}

	private boolean areDateFiltersInformed(String startDateTime, String endDateTime) {
		return StringUtils.isNotEmpty(startDateTime) && StringUtils.isNotEmpty(endDateTime);
	}

}
