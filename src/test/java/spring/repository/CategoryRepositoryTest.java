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

import spring.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql", "/sql/V1_schema.sql", "/sql/V2_value.sql" })
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	private static final String NAME = "qweqwe";

	@Test
	void getByNameTest() {
		assertTrue(categoryRepository.findByName(NAME).isPresent());
	}

	@Test
	void getAllPageTest() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Category> page = categoryRepository.findAll(pageable);
		assertTrue(!page.isEmpty());
		assertTrue(page.getContent().size() == 2);
	}

}
