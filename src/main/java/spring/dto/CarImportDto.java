package spring.dto;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import spring.importt.CategoryConverter;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CarImportDto extends BaseImportDto {

	@NotBlank
	@Size(max = 60)
	@CsvBindByName(column = "Model")
	@Pattern(regexp = "^[A-Z0-9][A-Za-z0-9-]{0,19}(?: [A-Z0-9][A-Za-z0-9-]{0,19}){0,2}$", message = "Не валидное название.")
	private String model;

	@NotNull
	@Min(1960)
	@Max(2026)
	@CsvBindByName(column = "Year")
	private Integer year;

	@NotBlank
	@Size(min = 3, max = 30)
	@CsvBindByName(column = "Make")
	@Pattern(regexp = "^[A-Z][A-Za-z-]{2,14}(?: [A-Z][A-Za-z-]{2,14})?$", message = "Не валидное название.")
	private String makeName;

	@Size(max = 4)
	@CsvCustomBindByName(column = "Category", converter = CategoryConverter.class)
	private Set<String> categoryName;

}
