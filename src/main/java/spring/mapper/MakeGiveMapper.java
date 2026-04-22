package spring.mapper;

import org.mapstruct.Mapper;

import spring.dto.MakeGiveDto;
import spring.model.Make;

@Mapper(componentModel = "spring")
public interface MakeGiveMapper {

	MakeGiveDto toDto(Make entity);

}
