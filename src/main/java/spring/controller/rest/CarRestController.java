package spring.controller.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import spring.dto.CarRequestDto;
import spring.dto.MakeRequestDto;
import spring.model.Car;
import spring.service.CarService;
import spring.service.importt.CsvService;
import spring.dto.CarFilterRequestDto;
import spring.dto.CarGiveDto;
import spring.dto.CarImportDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cars")
@OpenAPIDefinition(tags = { @Tag(name = "findByFilter", description = "Find car by filter"),
		@Tag(name = "findById", description = "Find car by id"), @Tag(name = "create", description = "Create car"),
		@Tag(name = "update", description = "Update car"), @Tag(name = "delete", description = "Delete car"),
		@Tag(name = "import", description = "Import csv file cars") })
public class CarRestController {

	private final CarService carService;
	private final CsvService csvService;

	@Tag(name = "findByFilter")
	@Operation(summary = "Find cars by filter")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the cars", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cars not found.", content = @Content) })
	@GetMapping("")
	public Page<CarGiveDto> giveAllPageFilterCar(Pageable pageable, CarFilterRequestDto dto) {
		return carService.getAllPageFilterCar(pageable, dto);
	}

	@Tag(name = "create")
	@Operation(summary = "Create car")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The car was created successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CarRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to car this request.", content = @Content),
			@ApiResponse(responseCode = "409", description = "The data already exists", content = @Content) })
	@PreAuthorize("hasAuthority('SCOPE_cars:create')")
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public CarGiveDto create(@Valid @RequestBody CarRequestDto dto) {
		return carService.create(dto);
	}

	@Tag(name = "findById")
	@Operation(summary = "Find car by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the car", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CarRequestDto.class))),
			@ApiResponse(responseCode = "404", description = "car not found.", content = @Content) })
	@GetMapping("/{id}")
	public CarGiveDto giveById(@PathVariable UUID id) {
		return carService.getById(id);
	}

	@Tag(name = "update")

	@Operation(summary = "Update car")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The car was update successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CarRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to car this request.", content = @Content) })
	@PreAuthorize("hasAuthority('SCOPE_cars:update')")
	@PutMapping("/{id}")
	public CarGiveDto update(@PathVariable UUID id, @Valid @RequestBody CarRequestDto dto) {
		return carService.updateById(id, dto);
	}

	@Tag(name = "delete")
	@Operation(summary = "Delete car")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Operation successful", content = @Content),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to car this request.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Car not found.", content = @Content)

	})
	@PreAuthorize("hasAuthority('SCOPE_cars:delete')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id) {
		carService.delete(id);
	}

	@Tag(name = "import")
	@Operation(summary = "Import cars from csv file")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Import successful", content = @Content),
			@ApiResponse(responseCode = "202", description = "Import running in background", content = @Content),
			@ApiResponse(responseCode = "400", description = "Еhe data is not valid", content = @Content),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "413", description = "File size exceeded", content = @Content),
			@ApiResponse(responseCode = "415", description = "Invalid file format", content = @Content),
			@ApiResponse(responseCode = "500", description = "There is an error in the code that saves this file.", content = @Content) })
	@PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
		csvService.importCsv(file, Car.class, CarImportDto.class);
		return ResponseEntity.ok("CSV imported successfully");
	}

}
