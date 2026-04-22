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

import spring.model.Make;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql", "/sql/V1_schema.sql", "/sql/V2_value.sql" })
class MakeRepositoryTest {

	@Autowired
	private MakeRepository makeRepository;

	private static final String NAME = "qwe";

	@Test
	void getByNameTest() {
		assertTrue(makeRepository.findByName(NAME).isPresent());
	}

	@Test
	void getAllPageTest() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Make> page = makeRepository.findAll(pageable);
		assertTrue(!page.isEmpty());
		assertTrue(page.getContent().size() == 2);
	}
}
