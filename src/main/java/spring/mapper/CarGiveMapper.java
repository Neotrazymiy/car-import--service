package spring.mapper;

import org.mapstruct.Mapper;

import spring.dto.CarGiveDto;
import spring.model.Car;

@Mapper(componentModel = "spring", uses = { MakeGiveMapper.class, CategoryGiveMapper.class })
public interface CarGiveMapper {

	CarGiveDto toDto(Car entity);

}
