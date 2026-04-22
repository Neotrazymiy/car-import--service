package spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.MakeRequestDto;
import spring.model.Make;

@Mapper(componentModel = "spring")
public interface MakeRequestMapper {

	@Mapping(target = "id", ignore = true)
	Make toEntity(MakeRequestDto dto);

	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(MakeRequestDto dto, @MappingTarget Make entity);

}
