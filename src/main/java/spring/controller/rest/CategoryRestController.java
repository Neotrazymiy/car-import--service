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
import spring.dto.CategoryGiveDto;
import spring.dto.CategoryRequestDto;
import spring.service.CategoryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categorys")
public class CategoryRestController {

	private final CategoryService categoryService;

	@Operation(summary = "Find all categorys")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the categorys", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categorys not found.", content = @Content) })

	@GetMapping
	public Page<CategoryGiveDto> giveAllPageCategorys(Pageable pageable) {
		return categoryService.getAllPageCategorys(pageable);
	}

	@Operation(summary = "Find category by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the category", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CategoryRequestDto.class))),
			@ApiResponse(responseCode = "404", description = "Category not found.", content = @Content) })
	@GetMapping("/{id}")
	public CategoryGiveDto giveById(@PathVariable UUID id) {
		return categoryService.getById(id);
	}

	@Operation(summary = "Create category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The category was created successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CategoryRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to category this request.", content = @Content),
			@ApiResponse(responseCode = "409", description = "The data already exists", content = @Content) })
	@PreAuthorize("hasAuthority('SCOPE_categorys:create')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryGiveDto create(@Valid @RequestBody CategoryRequestDto dto) {
		return categoryService.create(dto);
	}

	@Operation(summary = "Update categorys")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The categorys was update successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "The data does not match the pattern", content = @Content(schema = @Schema(implementation = CategoryRequestDto.class))),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to categorys this request.", content = @Content) })
	@PreAuthorize("hasAuthority('SCOPE_categorys:update')")
	@PutMapping("/{id}")
	public CategoryGiveDto update(@PathVariable UUID id, @Valid @RequestBody CategoryRequestDto dto) {
		return categoryService.update(id, dto);
	}

	@Operation(summary = "Delete categorys")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Operation successful", content = @Content),
			@ApiResponse(responseCode = "401", description = "Please log in to continue.", content = @Content),
			@ApiResponse(responseCode = "403", description = "You do not have permission to categorys this request.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categorys not found.", content = @Content)

	})
	@PreAuthorize("hasAuthority('SCOPE_categorys:delete')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		categoryService.delete(id);
	}
}
