package spring.service.importt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.Validator;

import spring.dto.CarImportDto;
import spring.model.Car;
import spring.model.Category;
import spring.repository.CarRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CsvServiceIntegTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CsvService csvService;

	@Autowired
	private CarRepository carRepository;

	@Test
	void csvImportFromControllerOneCategoryTest() throws Exception {
		String csv = "Model,Year,Make,Category\n" + "Ford,2020,Focus,Sedan\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		mockMvc.perform(multipart("/api/v1/cars/import/csv").file(file)).andExpect(status().isOk());

		Car car = carRepository.findByCode("Ford").orElseThrow(null);
		assertTrue(car.getModel().equals("Ford"));
		assertTrue(car.getMake().getName().equals("Focus"));
		assertTrue(car.getYear() == 2020);
		assertTrue(car.getCategorys().stream().map(Category::getName).anyMatch("Sedan"::equals));
	}

	@Test
	void csvImportFromControllerTwoCategoryTest() throws Exception {
		String csv = "Model,Year,Make,Category\n" + "Ford,2020,Focus,\"Sedan, Minivan\"\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		mockMvc.perform(multipart("/api/v1/cars/import/csv").file(file)).andExpect(status().isOk());

		Car car = carRepository.findByCode("Ford").orElseThrow(null);
		assertTrue(car.getModel().equals("Ford"));
		assertTrue(car.getMake().getName().equals("Focus"));
		assertTrue(car.getYear() == 2020);
		assertTrue(car.getCategorys().stream().map(Category::getName).anyMatch("Sedan"::equals));
		assertTrue(car.getCategorys().stream().map(Category::getName).anyMatch("Minivan"::equals));
	}

	@Test
	void csvImportWithEmptyMakeShouldThrowException() {
		String csv = "Model,Year,Make,Category\n" + "Ford,2020,,Sedan\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> csvService.importCsv(file, Car.class, CarImportDto.class));

		assertTrue(ex.getMessage().contains("не должно быть пустым")
				|| ex.getMessage().contains("размер должен находиться в диапазоне от 3 до 30")
				|| ex.getMessage().contains("Не валидное название."));
	}

	@Test
	void csvImportWithBlankMakeShouldThrowException() {
		String csv = "Model,Year,Make,Category\n" + "Ford,2020,,Sedan\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		Exception ex = assertThrows(RuntimeException.class,
				() -> csvService.importCsv(file, Car.class, CarImportDto.class));

		assertTrue(
				ex.getMessage().contains("Не валидное название.") || ex.getMessage().contains("не должно быть пустым"));
	}

	@Test
	void csvImportWithSpacesMakeShouldThrowException() {
		String csv = "Model,Year,Make,Category\n" + "Ford,2020,   ,Sedan\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		assertThrows(RuntimeException.class, () -> csvService.importCsv(file, Car.class, CarImportDto.class));
	}

	@Test
	void csvImportWithoutMakeColumnShouldThrowException() {
		String csv = "Model,Year,Category\n" + "Ford,2020,Sedan\n";

		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csv.getBytes());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> csvService.importCsv(file, Car.class, CarImportDto.class));

		assertNotNull(ex);
	}

}
