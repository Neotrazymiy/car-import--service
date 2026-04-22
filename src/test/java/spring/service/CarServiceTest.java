package spring.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import spring.auxiliaryObjects.CreateObjects;
import spring.dto.CarFilterRequestDto;
import spring.dto.CarGiveDto;
import spring.dto.CarRequestDto;
import spring.exception.CarNotFoundException;
import spring.mapper.CarGiveMapper;
import spring.mapper.CarRequestMapper;
import spring.model.Car;
import spring.model.Make;
import spring.repository.CarRepository;
import spring.repository.MakeRepository;

@SpringBootTest
class CarServiceTest {

	@Autowired
	private CarService carService;

	@Autowired
	private CarGiveMapper carGiveMapper;

	@Autowired
	private CarRequestMapper carRequestMapper;

	@MockBean
	private CarRepository carRepository;

	@MockBean
	private MakeRepository makeRepository;

	private static final String MODEL = "Qweqweqweqwe";
	private final CreateObjects createObjects = new CreateObjects();

	@Test
	void createTest() {
		CarRequestDto dto = createObjects.createCarRequestDto();
		Make make = createObjects.createMake();
		Car car = createObjects.createCar();

		when(makeRepository.findByName(dto.getMakeName())).thenReturn(Optional.of(make));
		when(carRepository.save(any(Car.class))).thenReturn(car);

		CarGiveDto result = carService.create(dto);

		assertTrue(result.getModel().equals(MODEL));

		verify(makeRepository).findByName(dto.getMakeName());
		verify(carRepository).save(any(Car.class));
	}

	@Test
	void getAllPageCarsTest() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Car> cars = new ArrayList<Car>();
		cars.add(createObjects.createCar());
		Page<Car> page = new PageImpl<Car>(cars);

		when(carRepository.findAll(pageable)).thenReturn(page);

		Page<CarGiveDto> result = carService.getAllPageCar(pageable);

		assertTrue(!result.isEmpty());
		assertTrue(result.getContent().size() == 1);

		verify(carRepository).findAll(pageable);
	}

	@Test
	void getAllPageCarFilterTest() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Car> cars = new ArrayList<Car>();
		cars.add(createObjects.createCar());
		Page<Car> page = new PageImpl<Car>(cars);
		CarFilterRequestDto filter = createObjects.createCarFilterRequestDto();

		when(carRepository.findAll(ArgumentMatchers.<Specification<Car>>any(), eq(pageable))).thenReturn(page);

		Page<CarGiveDto> result = carService.getAllPageFilterCar(pageable, filter);

		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getContent().get(0).getModel().equals(MODEL));

		verify(carRepository).findAll(ArgumentMatchers.<Specification<Car>>any(), eq(pageable));
	}

	@Test
	void getByIdTest() {
		UUID anyId = UUID.randomUUID();
		Car car = createObjects.createCar();

		when(carRepository.findById(anyId)).thenReturn(Optional.of(car));

		CarGiveDto result = carService.getById(anyId);

		assertTrue(result.getModel().equals(MODEL));

		verify(carRepository).findById(anyId);
	}

	@Test
	void Exception_getByIdTest() {
		UUID carId = UUID.randomUUID();
		when(carRepository.findById(carId)).thenReturn(Optional.empty());
		CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.getById(carId));
		assertEquals("Автомобиль по id: " + carId + ", - не найден.", exception.getMessage());
	}

	@Test
	void updateSameObjectTest() {
		UUID anyId = UUID.randomUUID();
		Car car = createObjects.createCar();

		when(carRepository.findById(anyId)).thenReturn(Optional.of(car));

		CarGiveDto result = carService.updateById(anyId, createObjects.createCarRequestDto());

		assertTrue(result.getModel().equals(car.getModel()));

		verify(carRepository).findById(anyId);
		verify(carRepository, never()).saveAndFlush(car);
	}

	@Test
	void updateTest() {
		CarRequestDto dto = createObjects.createCarRequestDto();
		dto.setModel(MODEL + MODEL);
		Car car = createObjects.createCar();

		UUID carId = car.getId();

		when(carRepository.findById(carId)).thenReturn(Optional.of(car));
		when(carRepository.save(car)).thenReturn(car);

		CarGiveDto result = carService.updateById(carId, dto);

		assertTrue(result.getModel().equals(MODEL + MODEL));

		verify(carRepository).findById(carId);
		verify(carRepository).save(car);
	}

	@Test
	void deleteTest() {
		UUID anyId = UUID.randomUUID();
		Car car = createObjects.createCar();

		when(carRepository.findById(anyId)).thenReturn(Optional.of(car));

		carService.delete(anyId);

		verify(carRepository).findById(anyId);
		verify(carRepository).delete(car);
	}

}
