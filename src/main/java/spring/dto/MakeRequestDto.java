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
public class MakeRequestDto {

	@Size(min = 3, max = 30)
	@NotBlank
	@Schema(description = "Make", example = "Daewoo")
	@Pattern(regexp = "^[A-Z][A-Za-z-]{2,14}(?: [A-Z][A-Za-z-]{2,14})?$", message = "Не валидное имя.")
	private String name;

}
