package spring.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.auxiliaryObjects.CreateObjects;
import spring.dto.MakeGiveDto;
import spring.dto.MakeRequestDto;
import spring.exception.MakeNotFoundException;
import spring.mapper.MakeGiveMapper;
import spring.mapper.MakeRequestMapper;
import spring.model.Make;
import spring.repository.MakeRepository;

@SpringBootTest
class MakeServiceTest {

	@Autowired
	private MakeService makeService;

	@MockBean
	private MakeRepository makeRepository;

	@Autowired
	private MakeGiveMapper makeGiveMapper;

	@Autowired
	private MakeRequestMapper makeRequestMapper;

	private static final String NAME = "Qweqweqweqwe";
	private final CreateObjects createObjects = new CreateObjects();

	@Test
	void createTest() {
		MakeRequestDto dto = createObjects.createMakeRequestDto();
		Make make = createObjects.createMake();

		when(makeRepository.save(any(Make.class))).thenReturn(make);

		MakeGiveDto result = makeService.create(dto);

		assertTrue(result.getName().equals(NAME));

		verify(makeRepository).save(any(Make.class));
	}

	@Test
	void getAllMakesTest() {
		List<Make> makes = new ArrayList<Make>();
		makes.add(createObjects.createMake());

		when(makeRepository.findAll()).thenReturn(makes);

		List<MakeGiveDto> result = makeService.getAllMakes();

		assertTrue(!result.isEmpty());
		assertTrue(result.size() == 1);

		verify(makeRepository).findAll();
	}

	@Test
	void getAllPageMakesTest() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Make> dtos = new ArrayList<Make>();
		dtos.add(createObjects.createMake());
		Page<Make> page = new PageImpl<Make>(dtos);

		when(makeRepository.findAll(pageable)).thenReturn(page);

		Page<MakeGiveDto> result = makeService.getAllPageMakes(pageable);

		assertTrue(!result.isEmpty());
		assertTrue(result.getContent().size() == 1);

		verify(makeRepository).findAll(pageable);
	}

	@Test
	void getByNameTest() {
		Make make = createObjects.createMake();

		when(makeRepository.findByName(NAME)).thenReturn(Optional.of(make));

		MakeGiveDto result = makeService.getByName(NAME);

		assertTrue(result.getName().equals(NAME));

		verify(makeRepository).findByName(NAME);
	}

	@Test
	void Exception_getByNameTest() {
		when(makeRepository.findByName(NAME)).thenReturn(Optional.empty());
		MakeNotFoundException exception = assertThrows(MakeNotFoundException.class, () -> makeService.getByName(NAME));
		assertEquals("Производитель Qweqweqweqwe - не найден.", exception.getMessage());
	}

	@Test
	void getByIdTest() {
		Make make = createObjects.createMake();
		UUID makeId = make.getId();

		when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

		MakeGiveDto result = makeService.getById(makeId);

		assertTrue(result.getName().equals(NAME));

		verify(makeRepository).findById(makeId);
	}

	@Test
	void Exception_getByIdTest() {
		UUID makeId = UUID.randomUUID();

		when(makeRepository.findById(makeId)).thenReturn(Optional.empty());
		MakeNotFoundException exception = assertThrows(MakeNotFoundException.class, () -> makeService.getById(makeId));
		assertEquals("Производитель по id: " + makeId + " - не найден.", exception.getMessage());
	}

	@Test
	void updateSameObjectTest() {
		Make make = createObjects.createMake();

		UUID makeId = make.getId();

		when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

		MakeGiveDto result = makeService.update(makeId, createObjects.createMakeRequestDto());

		assertTrue(result.getName().equals(make.getName()));

		verify(makeRepository).findById(makeId);
		verify(makeRepository, never()).saveAndFlush(make);
	}

	@Test
	void updateTest() {
		MakeRequestDto dto = createObjects.createMakeRequestDto();
		dto.setName(NAME + NAME);
		Make make = createObjects.createMake();
		UUID makeId = make.getId();

		when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));
		when(makeRepository.save(make)).thenReturn(make);

		MakeGiveDto result = makeService.update(makeId, dto);

		assertTrue(result.getName().equals(NAME + NAME));

		verify(makeRepository).findById(makeId);
		verify(makeRepository).save(make);
	}

	@Test
	void deleteTest() {
		Make make = createObjects.createMake();
		UUID makeId = make.getId();

		when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

		makeService.delete(makeId);

		verify(makeRepository).findById(makeId);
		verify(makeRepository).delete(make);
	}

}
