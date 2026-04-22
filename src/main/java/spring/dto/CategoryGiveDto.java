package spring.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGiveDto {

	@Schema(description = "Unique ctegory identifier", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = AccessMode.READ_ONLY)
	private UUID id;

	@Schema(description = "Category", example = "Sedan")
	private String name;

}
