package spring.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import spring.dto.CarRequestDto;
import spring.model.Car;

@Mapper(componentModel = "spring")
public interface CarRequestMapper {

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "make", ignore = true)
	@Mapping(target = "categorys", ignore = true)
	Car toEntity(CarRequestDto dto);

	@InheritConfiguration
	void updateEntityFromDto(CarRequestDto dto, @MappingTarget Car Entity);

}
