package spring.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import spring.model.Car;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql", "/sql/V1_schema.sql", "/sql/V2_value.sql" })
class CarRepositoryTest {

	@Autowired
	private CarRepository carRepository;

	private static final String CAR_MODEL = "qweqweqwe";

	@Test
	void getByNameTest() {
		assertTrue(carRepository.findByModel(CAR_MODEL).isPresent());
	}

	@Test
	void getAllPageTest() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Car> page = carRepository.findAll(pageable);
		assertTrue(!page.isEmpty());
		assertTrue(page.getContent().size() == 2);
	}

}
