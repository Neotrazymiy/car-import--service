package spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.CarImportDto;
import spring.model.Car;

@Mapper(componentModel = "spring")
public interface CarImportMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "code", ignore = true)
	@Mapping(target = "make", ignore = true)
	@Mapping(target = "categorys", ignore = true)
	void updateEntity(CarImportDto dto, @MappingTarget Car car);

}
