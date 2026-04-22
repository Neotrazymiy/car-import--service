package spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.CategoryImportDto;
import spring.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryImportMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "code", ignore = true)
	void updateEntity(CategoryImportDto dto, @MappingTarget Category category);
}
