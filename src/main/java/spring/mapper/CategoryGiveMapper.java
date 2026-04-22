package spring.mapper;

import org.mapstruct.Mapper;

import spring.dto.CategoryGiveDto;
import spring.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryGiveMapper {

	CategoryGiveDto toDto(Category entity);

}
