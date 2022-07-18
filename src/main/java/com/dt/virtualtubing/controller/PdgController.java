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

import com.dt.virtualtubing.model.PdgRequest;
import com.dt.virtualtubing.persistence.entity.Pdg;
import com.dt.virtualtubing.service.PdgService;

@RestController
public class PdgController {

	private final PdgService pdgService;

	@Autowired
	public PdgController(PdgService pdgService) {
		this.pdgService = pdgService;
	}

	@GetMapping("/pdg")
	public ResponseEntity<List<Pdg>> listPdg() {
		List<Pdg> pdgList = pdgService.getAllPdgs();
		return new ResponseEntity<>(pdgList, HttpStatus.OK);
	}

	@GetMapping("/pdg/{id}")
	public ResponseEntity<Pdg> getPdg(@PathVariable("id") UUID id) {
		Pdg pdg = pdgService.getPdg(id);
		return new ResponseEntity<>(pdg, HttpStatus.OK);
	}

	@PostMapping("/pdg/{id}")
	public ResponseEntity<Pdg> createPdg(@PathVariable("id") UUID tubingId, @RequestBody @Valid PdgRequest pdgRequest) {
		Pdg pdg = pdgService.savePdg(tubingId, pdgRequest);
		return new ResponseEntity<>(pdg, HttpStatus.OK);
	}

	@PutMapping("/pdg/{id}")
	public ResponseEntity<Pdg> updatePdg(@PathVariable("id") UUID id, @RequestBody @Valid PdgRequest pdgRequest) {
		Pdg updatedPdg = pdgService.updatePdg(id, pdgRequest);
		return new ResponseEntity<>(updatedPdg, HttpStatus.OK);
	}

	@DeleteMapping("/pdg/{id}")
	public ResponseEntity<Void> deletePdg(@PathVariable("id") UUID id) {
		pdgService.deletePdg(id);
		return ResponseEntity.noContent().build();
	}

}
