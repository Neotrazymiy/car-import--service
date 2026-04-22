package spring.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import spring.dto.CarRequestDto;
import spring.exception.CarNotFoundException;
import spring.exception.MakeNotFoundException;
import spring.dto.CarFilterRequestDto;
import spring.dto.CarGiveDto;
import spring.filter.CarModelSpecific;
import spring.mapper.CarRequestMapper;
import spring.mapper.CarGiveMapper;
import spring.model.Car;
import spring.model.Make;
import spring.repository.CarRepository;
import spring.repository.MakeRepository;

@Service
@AllArgsConstructor
public class CarService {

	private final CarRepository carRepository;
	private final CarGiveMapper carGiveMapper;
	private final CarRequestMapper carRequestMapper;
	private final MakeRepository makeRepository;

	@Transactional
	public CarGiveDto create(CarRequestDto dto) {
		Car car = carRequestMapper.toEntity(dto);
		Make make = makeRepository.findByName(dto.getMakeName())
				.orElseThrow(() -> new MakeNotFoundException(dto.getMakeName()));
		car.setMake(make);
		Car saved = carRepository.save(car);
		return carGiveMapper.toDto(saved);

	}

	@Transactional(readOnly = true)
	public Page<CarGiveDto> getAllPageCar(Pageable pageable) {
		return carRepository.findAll(pageable).map(carGiveMapper::toDto);
	}

	@Transactional(readOnly = true)
	public Page<CarGiveDto> getAllPageFilterCar(Pageable pageable, CarFilterRequestDto dto) {
		Specification<Car> spec = Specification.where(null);
		spec = spec.and(CarModelSpecific.hasModel(dto.getModel()))
				.and(CarModelSpecific.hasMinYear(dto.getMinYear()).and(CarModelSpecific.hasMaxYear(dto.getMaxYear())))
				.and(CarModelSpecific.hasMake(dto.getMakeName()))
				.and(CarModelSpecific.hasCategory(dto.getCategoryName()));
		return carRepository.findAll(spec, pageable).map(carGiveMapper::toDto);
	}

	@Transactional(readOnly = true)
	public CarGiveDto getById(UUID carId) {
		return carRepository.findById(carId).map(carGiveMapper::toDto)
				.orElseThrow(() -> new CarNotFoundException(carId));
	}

	@Transactional
	public CarGiveDto updateById(UUID carId, CarRequestDto dto) {
		return carRepository.findById(carId).map(entity -> {
			boolean changed = checkReccuringObject(entity, dto) || checkReccuringEntity(entity, dto);
			if (!changed) {
				return entity;
			}
			carRequestMapper.updateEntityFromDto(dto, entity);
			return carRepository.save(entity);
		}).map(carGiveMapper::toDto).orElseThrow(() -> new CarNotFoundException(carId));
	}

	private boolean checkReccuringObject(Car entity, CarRequestDto dto) {
		boolean reccuringModel = entity.getModel().equals(dto.getModel());
		boolean reccuringYear = entity.getYear().equals(dto.getYear());
		return !reccuringModel || !reccuringYear;
	}

	private boolean checkReccuringEntity(Car entity, CarRequestDto dto) {
		boolean reccuringMake = entity.getMake().getName().equals(dto.getMakeName());
		boolean changed = false;

		if (!reccuringMake) {
			Make newMake = makeRepository.findByName(dto.getMakeName())
					.orElseThrow(() -> new MakeNotFoundException(dto.getMakeName()));
			entity.setMake(newMake);
			changed = true;
		}
		return changed;
	}

	@Transactional
	public void delete(UUID carId) {
		Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
		carRepository.delete(car);
	}
}
