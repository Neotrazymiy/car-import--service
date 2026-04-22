package spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.CategoryRequestDto;
import spring.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {

	@Mapping(target = "cars", ignore = true)
	@Mapping(target = "id", ignore = true)
	Category toEntity(CategoryRequestDto dto);

	@Mapping(target = "cars", ignore = true)
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(CategoryRequestDto dto, @MappingTarget Category entity);

}
