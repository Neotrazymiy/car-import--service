package spring.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

	@Size(max = 60)
	@Schema(description = "Car model", example = "Lanos")
	@NotBlank
	@Pattern(regexp = "^[A-Z0-9][A-Za-z0-9-]{0,19}(?: [A-Z0-9][A-Za-z0-9-]{0,19}){0,2}$", message = "Не валидное название.")
	private String model;

	@Min(1960)
	@Max(2026)
	@NotNull
	@Schema(description = "Year", example = "2020")
	private Integer year;

	@Size(min = 3, max = 30)
	@NotBlank
	@Schema(description = "Make name", example = "Daewoo")
	@Pattern(regexp = "^[A-Z][A-Za-z-]{2,14}(?: [A-Z][A-Za-z-]{2,14})?$", message = "Не валидное название.")
	private String makeName;

	@Size(min = 3, max = 60)
	@Schema(description = "Category name", example = "Sedan", requiredMode = RequiredMode.NOT_REQUIRED)
	@Pattern(regexp = "^[A-Z][A-Za-z\\/,]{2,14}(?: [A-Z][A-Za-z\\/,]{2,14}){0,3}$", message = "Не валидное название.")
	private String categoryName;

}
