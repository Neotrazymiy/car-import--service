package spring.importt.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.CarImportDto;
import spring.importt.CarImportUpdater;
import spring.importt.ReferenceImporter;
import spring.model.Car;
import spring.repository.CarRepository;

@Service
@AllArgsConstructor
public class CarReferenceEntity implements ReferenceImporter<Car, CarImportDto> {

	private CarRepository carRepository;
	private CarImportUpdater carImportUpdater;

	@Override
	public Class<Car> getEntityClass() {
		return Car.class;
	}

	@Override
	public JpaRepository<Car, UUID> getRepository() {
		return carRepository;
	}

	@Override
	public Car createEntity() {
		return new Car();
	}

	@Override
	public String getKey(CarImportDto dto) {
		return dto.getModel();
	}

	@Override
	public Function<String, Optional<Car>> finder() {
		return carRepository::findByCode;
	}

	@Override
	public BiConsumer<CarImportDto, Car> updater() {
		return carImportUpdater::update;
	}

}
