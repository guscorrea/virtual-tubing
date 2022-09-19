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

import com.dt.virtualtubing.model.TubingRequest;
import com.dt.virtualtubing.persistence.entity.Tubing;
import com.dt.virtualtubing.service.TubingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Tubing")
public class TubingController {

	private final TubingService tubingService;

	@Autowired
	public TubingController(TubingService tubingService) {
		this.tubingService = tubingService;
	}

	@GetMapping("/v1/tubing")
	@Operation(summary = "Retrieves all Tubings", description = "Retrieves all Tubing resources in a list.", responses = {
			@ApiResponse(responseCode = "200", description = "The list of Tubings was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = Tubing.class))) }) })
	public ResponseEntity<List<Tubing>> listTubing() {
		List<Tubing> tubingList = tubingService.getAllTubings();
		return new ResponseEntity<>(tubingList, HttpStatus.OK);
	}

	@GetMapping("/v1/tubing/{id}")
	@Operation(summary = "Retrieves a Tubing", description = "Retrieves a Tubing resource with a given UUID.", responses = {
			@ApiResponse(responseCode = "200", description = "The Tubing was retrieved.",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Tubing.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string", example = "Invalid UUID string")) }),
			@ApiResponse(responseCode = "404", description = "The Tubing was not found in the DB.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string",
							example = "Tubing with id c5f2f64c-b02d-4635-8a34-c3d4cc2d955b not found in the database.")) }) })
	public ResponseEntity<Tubing> getTubing(@PathVariable("id") UUID id) {
		Tubing tubing = tubingService.getTubing(id);
		return new ResponseEntity<>(tubing, HttpStatus.OK);
	}

	@PostMapping("/v1/tubing")
	@Operation(summary = "Creates a Tubing resource",
			description = "Sends a post request, validates input data, and saves the generated resource into the Scylla database.", responses = {
			@ApiResponse(responseCode = "200", description = "Tubing resource was created",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Tubing.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(type = "string", example = "Field name: must not be null")) }),
			@ApiResponse(responseCode = "500", description = "Unexpected error occurred",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }) })
	public ResponseEntity<Tubing> createTubing(@RequestBody @Valid TubingRequest tubingRequest) {
		Tubing tubing = tubingService.saveTubing(tubingRequest);
		return new ResponseEntity<>(tubing, HttpStatus.OK);
	}

	@PutMapping("/v1/tubing/{id}")
	@Operation(summary = "Updates a Tubing resource",
			description = "Sends a put request, validates input data, checks if the current resource exists, and saves the updated resource into the "
					+ "Scylla database.", responses = {
			@ApiResponse(responseCode = "200", description = "Tubing resource was updated",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Tubing.class)) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(type = "string", example = "Field name: must not be null")) }),
			@ApiResponse(responseCode = "404", description = "The Tubing was not found in the DB.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string",
							example = "Tubing with id c5f2f64c-b02d-4635-8a34-c3d4cc2d955b not found in the database.")) }),
			@ApiResponse(responseCode = "500", description = "Unexpected error occurred",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }) })
	public ResponseEntity<Tubing> updateTubing(@PathVariable("id") UUID id, @RequestBody @Valid TubingRequest tubingRequest) {
		Tubing updatedTubing = tubingService.updateTubing(id, tubingRequest);
		return new ResponseEntity<>(updatedTubing, HttpStatus.OK);
	}

	@DeleteMapping("/v1/tubing/{id}")
	@Operation(summary = "Deletes a Tubing resource", description = "Deletes a Tubing resource with given UUID.",
			responses = { @ApiResponse(responseCode = "204", description = "The Tubing was deleted.") })
	public ResponseEntity<Void> createTubing(@PathVariable("id") UUID id) {
		tubingService.deleteTubing(id);
		return ResponseEntity.noContent().build();
	}

}
