package com.dt.virtualtubing.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "PDG")
public class PdgController {

	private final PdgService pdgService;

	@Autowired
	public PdgController(PdgService pdgService) {
		this.pdgService = pdgService;
	}

	@GetMapping("/v1/pdg")
	@Operation(summary = "Retrieves all PDGs.", description = "Retrieves all PDG resources in a list.", responses = {
			@ApiResponse(responseCode = "200", description = "The list of PDGs was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = Pdg.class))) }) })
	public ResponseEntity<List<Pdg>> listPdg() {
		List<Pdg> pdgList = pdgService.getAllPdgs();
		return new ResponseEntity<>(pdgList, HttpStatus.OK);
	}

	@GetMapping("/v1/pdg/{id}")
	@Operation(summary = "Retrieves a PDG.", description = "Retrieves a PDG resource with a given UUID.", responses = {
			@ApiResponse(responseCode = "200", description = "The PDG was retrieved.",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Pdg.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string", example = "Invalid UUID string")) }),
			@ApiResponse(responseCode = "404", description = "The PDG was not found in the DB.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string",
							example = "Pdg with id c5f2f64c-b02d-4635-8a34-c3d4cc2d955b not found in the database.")) }) })
	public ResponseEntity<Pdg> getPdg(@PathVariable("id") UUID id) {
		Pdg pdg = pdgService.getPdg(id);
		return new ResponseEntity<>(pdg, HttpStatus.OK);
	}

	@PostMapping("/v1/pdg/{id}")
	@Operation(summary = "Creates a PDG resource",
			description = "Sends a post request, validates input data, and saves the generated resource into the Scylla database."
					+ " Requires an existent tubing to be processed.", responses = {
			@ApiResponse(responseCode = "200", description = "PDG resource was created",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Pdg.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(type = "string", example = "Field name: must not be null")) }),
			@ApiResponse(responseCode = "404", description = "The PDG was not found in the DB.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string",
							example = "Tubing with id c5f2f64c-b02d-4635-8a34-c3d4cc2d955b not found in the database.")) }),
			@ApiResponse(responseCode = "500", description = "Unexpected error occurred",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }) })
	public ResponseEntity<Pdg> createPdg(@PathVariable("id") UUID tubingId, @RequestBody @Valid PdgRequest pdgRequest) {
		Pdg pdg = pdgService.savePdg(tubingId, pdgRequest);
		return new ResponseEntity<>(pdg, HttpStatus.OK);
	}

	@PutMapping("/v1/pdg/{id}")
	@Operation(summary = "Updates a PDG resource",
			description = "Sends a put request, validates input data, checks if the current resource exists, and saves the updated resource into the "
					+ "Scylla database.", responses = {
			@ApiResponse(responseCode = "200", description = "PDG resource was updated",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Pdg.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(type = "string", example = "Field name: must not be null")) }),
			@ApiResponse(responseCode = "404", description = "The PDG was not found in the DB.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string",
							example = "Pdg with id c5f2f64c-b02d-4635-8a34-c3d4cc2d955b not found in the database.")) }),
			@ApiResponse(responseCode = "500", description = "Unexpected error occurred",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }) })
	public ResponseEntity<Pdg> updatePdg(@PathVariable("id") UUID id, @RequestBody @Valid PdgRequest pdgRequest) {
		Pdg updatedPdg = pdgService.updatePdg(id, pdgRequest);
		return new ResponseEntity<>(updatedPdg, HttpStatus.OK);
	}

	@DeleteMapping("/v1/pdg/{id}")
	@Operation(summary = "Deletes a PDG resource", description = "Deletes a PDG resource with given UUID.",
			responses = { @ApiResponse(responseCode = "204", description = "The PDG was deleted.") })
	public ResponseEntity<Void> deletePdg(@PathVariable("id") UUID id) {
		pdgService.deletePdg(id);
		return ResponseEntity.noContent().build();
	}

}
