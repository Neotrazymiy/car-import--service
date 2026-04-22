package spring.importt;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.CarImportDto;
import spring.mapper.CarImportMapper;
import spring.model.Car;
import spring.service.CarImportService;

@Service
@AllArgsConstructor
public class CarImportUpdater {

	private final CarImportMapper carImportMapper;
	private final CarImportService carImportService;

	public void update(CarImportDto dto, Car car) {
		carImportMapper.updateEntity(dto, car);
		carImportService.carReations(dto, car);
	}

}
