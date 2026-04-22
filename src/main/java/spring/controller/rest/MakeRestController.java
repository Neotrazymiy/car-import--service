package spring.controller.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import spring.dto.MakeRequestDto;
import spring.dto.MakeGiveDto;
import spring.service.MakeService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/makes")
public class MakeRestController {

	private final MakeService makeService;

	@Operation(summary = "Create make")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The make was created successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = MakeRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to make this request.", content = @Content),
			@ApiResponse(responseCode = "409", description = "The data already exists", content = @Content) })
	@PostMapping
	@PreAuthorize("hasAuthority('SCOPE_makes:create')")
	@ResponseStatus(HttpStatus.CREATED)
	public MakeGiveDto create(@Valid @RequestBody MakeRequestDto dto) {
		return makeService.create(dto);
	}

	@Operation(summary = "Find make by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the make", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = MakeRequestDto.class))),
			@ApiResponse(responseCode = "404", description = "Make not found.", content = @Content) })
	@GetMapping("/{id}")
	public MakeGiveDto giveById(@PathVariable UUID id) {
		return makeService.getById(id);
	}

	@Operation(summary = "Find all makes")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the makes", content = @Content),
			@ApiResponse(responseCode = "404", description = "Makes not found.", content = @Content) })
	@GetMapping("")
	public Page<MakeGiveDto> giveAllPageMakes(Pageable pageable) {
		return makeService.getAllPageMakes(pageable);
	}

	@Operation(summary = "Update make")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The make was update successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = MakeRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to make this request.", content = @Content) })
	@PreAuthorize("hasAuthority('SCOPE_makes:update')")
	@PutMapping("/{id}")
	public MakeGiveDto update(@PathVariable UUID id, @Valid @RequestBody MakeRequestDto dto) {
		return makeService.update(id, dto);
	}

	@Operation(summary = "Delete make")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Operation successful", content = @Content),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to make this request.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Make not found.", content = @Content)

	})
	@PreAuthorize("hasAuthority('SCOPE_makes:delete')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		makeService.delete(id);
	}

}
