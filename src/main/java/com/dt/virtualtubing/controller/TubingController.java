package com.dt.virtualtubing.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dt.virtualtubing.model.TubingRequest;
import com.dt.virtualtubing.persistence.entity.Tubing;
import com.dt.virtualtubing.service.TubingService;

@RestController
public class TubingController {

	private final TubingService tubingService;

	@Autowired
	public TubingController(TubingService tubingService) {
		this.tubingService = tubingService;
	}

	@GetMapping("/tubing")
	public ResponseEntity<List<Tubing>> listTubing() {
		List<Tubing> tubingList = tubingService.getAllTubings();
		return new ResponseEntity<>(tubingList, HttpStatus.OK);
	}

	@GetMapping("/tubing/{id}")
	public ResponseEntity<Tubing> getTubing(@PathVariable("id") UUID id) {
		Tubing tubing = tubingService.getTubing(id);
		return new ResponseEntity<>(tubing, HttpStatus.OK);
	}

	@PostMapping("/tubing")
	public ResponseEntity<Tubing> createTubing(@RequestBody @Valid TubingRequest tubingRequest) {
		Tubing tubing = tubingService.saveTubing(tubingRequest);
		return new ResponseEntity<>(tubing, HttpStatus.OK);
	}

	@PutMapping("/tubing/{id}")
	public ResponseEntity<Tubing> updateTubing(@PathVariable("id") UUID id, @RequestBody @Valid TubingRequest tubingRequest) {
		Tubing updatedTubing = tubingService.updateTubing(id, tubingRequest);
		return new ResponseEntity<>(updatedTubing, HttpStatus.OK);
	}

	@DeleteMapping("/tubing/{id}")
	public ResponseEntity<Void> createTubing(@PathVariable("id") UUID id) {
		tubingService.deleteTubing(id);
		return ResponseEntity.noContent().build();
	}

}
