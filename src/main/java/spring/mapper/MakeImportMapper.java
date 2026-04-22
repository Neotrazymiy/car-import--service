package spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.MakeImportDto;
import spring.model.Make;

@Mapper(componentModel = "spring")
public interface MakeImportMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "code", ignore = true)
	void updateEntity(MakeImportDto dto, @MappingTarget Make make);

}
