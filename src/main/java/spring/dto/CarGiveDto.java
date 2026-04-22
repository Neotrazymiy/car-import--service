package spring.dto;

import java.util.Set;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarGiveDto {

	@Schema(description = "Unique car identifier", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = AccessMode.READ_ONLY)
	private UUID id;

	@Schema(description = "Car model", example = "Lanos")
	private String model;

	@Schema(description = "Year", example = "2020")
	private Integer year;

	@Schema(description = "Make")
	private MakeGiveDto make;

	@Schema(description = "Categorys to the car")
	private Set<CategoryGiveDto> categorys;

}
