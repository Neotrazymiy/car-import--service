package spring.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

	@Schema(description = "Categorys", example = "Sedan")
	@NotBlank
	@Size(min = 3, max = 15)
	@Pattern(regexp = "^[A-Z][A-Za-z/]{2,14}$", message = "Не валидное имя.")
	String name;

}
