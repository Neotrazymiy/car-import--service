package spring.auxiliaryObjects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import spring.dto.CarFilterRequestDto;
import spring.dto.CarGiveDto;
import spring.dto.CarRequestDto;
import spring.dto.CategoryGiveDto;
import spring.dto.CategoryRequestDto;
import spring.dto.MakeGiveDto;
import spring.dto.MakeRequestDto;
import spring.model.Car;
import spring.model.Category;
import spring.model.Make;

public class CreateObjects {

	private static final String NAME = "Qweqweqweqwe";
	private static final Integer YEAR = 2011;
	private static final UUID ID = UUID.randomUUID();

	public Make createMake() {
		return new Make(ID, NAME, null);
	}

	public MakeRequestDto createMakeRequestDto() {
		return new MakeRequestDto(NAME);
	}

	public MakeGiveDto createMakeGiveDto() {
		return new MakeGiveDto(ID, NAME);
	}

	public Car createCar() {
		Car car = new Car();
		car.setId(ID);
		car.setModel(NAME);
		car.setYear(YEAR);
		car.setMake(createMake());
		Set<Category> categories = new HashSet<Category>();
		categories.add(createCategory());
		car.setCategorys(categories);
		return car;
	}

	public CarRequestDto createCarRequestDto() {
		return new CarRequestDto(NAME, YEAR, NAME, NAME);
	}

	public CarFilterRequestDto createCarFilterRequestDto() {
		return new CarFilterRequestDto(NAME, NAME, YEAR, YEAR, NAME);
	}

	public CarGiveDto createCarGiveDto() {
		CarGiveDto dto = new CarGiveDto();
		dto.setId(ID);
		dto.setYear(YEAR);
		dto.setModel(NAME);
		dto.setMake(createMakeGiveDto());
		Set<CategoryGiveDto> dtos = new HashSet<CategoryGiveDto>();
		dtos.add(createCategoryGiveDto());
		return dto;
	}

	public Category createCategory() {
		Category category = new Category();
		category.setId(ID);
		category.setName(NAME);
		return category;
	}

	public CategoryGiveDto createCategoryGiveDto() {
		return new CategoryGiveDto(ID, NAME);
	}

	public CategoryRequestDto createCategoryRequestDto() {
		return new CategoryRequestDto(NAME);
	}

}
