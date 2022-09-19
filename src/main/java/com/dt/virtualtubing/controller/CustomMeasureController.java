package com.dt.virtualtubing.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dt.virtualtubing.persistence.CustomMeasureRepository;
import com.dt.virtualtubing.persistence.entity.CustomMeasure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Custom Measure")
public class CustomMeasureController {

	private final CustomMeasureRepository customMeasureRepository;

	@Autowired
	public CustomMeasureController(CustomMeasureRepository customMeasureRepository) {
		this.customMeasureRepository = customMeasureRepository;
	}

	@GetMapping("/v1/measure")
	@Operation(summary = "Retrieves all custom measures.", description = "Retrieves all custom measure resources in a list.", responses = {
			@ApiResponse(responseCode = "200", description = "The list of custom measures was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = CustomMeasure.class))) }) })
	public ResponseEntity<List<CustomMeasure>> listMeasures() {
		return new ResponseEntity<>(customMeasureRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/v1/measure/{id}")
	@Operation(summary = "Retrieves a custom measure.", description = "Retrieves a custom measure resource with a given UUID.", responses = {
			@ApiResponse(responseCode = "200", description = "The custom measure was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = CustomMeasure.class))) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string", example = "Invalid UUID string")) }) })
	public ResponseEntity<List<CustomMeasure>> getMeasure(@PathVariable("id") String id) {
		return new ResponseEntity<>(customMeasureRepository.findAllByTubing(UUID.fromString(id)), HttpStatus.OK);
	}

	@GetMapping("/v1/measure/{id}/property/{propertyName}")
	@Operation(summary = "Retrieves all custom measures by property.",
			description = "Retrieves all custom measure resources in a list, filtered by UUID and a given property name. "
					+ "Start date and time can also be used as optional filtering parameters.", responses = {
			@ApiResponse(responseCode = "200", description = "The list of custom measures was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = CustomMeasure.class))) }),
			@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string", example = "Invalid UUID string")) }) })
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
