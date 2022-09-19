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

import com.dt.virtualtubing.persistence.PressureRepository;
import com.dt.virtualtubing.persistence.entity.Pressure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Pressure")
public class PressureController {

	private final PressureRepository pressureRepository;

	@Autowired
	public PressureController(PressureRepository pressureRepository) {
		this.pressureRepository = pressureRepository;
	}

	@GetMapping("/v1/pressure")
	@Operation(summary = "Retrieves all pressure resources.", description = "Retrieves all pressure resources in a list.", responses = {
			@ApiResponse(responseCode = "200", description = "The list of pressure resources was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = Pressure.class))) }) })
	public ResponseEntity<List<Pressure>> listPressure() {
		return new ResponseEntity<>(pressureRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/v1/pressure/{id}")
	@Operation(summary = "Retrieves all pressure resources by UUID.", description = "Retrieves all pressure resources in a list, filtered by UUID. "
			+ "Start date and time can also be used as optional filtering parameters.",
			responses = { @ApiResponse(responseCode = "200", description = "The list of pressure resources was retrieved.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = Pressure.class))) }),
					@ApiResponse(responseCode = "400", description = "The request failed validation.", content = {
							@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(type = "string", example = "Invalid UUID string")) }) })
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
