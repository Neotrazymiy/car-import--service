package spring.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import spring.dto.CategoryGiveDto;
import spring.dto.CategoryRequestDto;
import spring.exception.CategoryNotFoundException;
import spring.mapper.CategoryGiveMapper;
import spring.mapper.CategoryRequestMapper;
import spring.model.Category;
import spring.repository.CategoryRepository;

@SpringBootTest
class CategoryServiceTest {

	@Autowired
	private CategoryService categoryService;

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryGiveMapper categoryGiveMapper;

	@Autowired
	private CategoryRequestMapper categoryRequestMapper;

	private final CreateObjects createObjects = new CreateObjects();
	private static final String NAME = "Qweqweqweqwe";

	@Test
	void createTest() {
		CategoryRequestDto dto = createObjects.createCategoryRequestDto();
		Category category = createObjects.createCategory();

		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		CategoryGiveDto result = categoryService.create(dto);

		assertTrue(result.getName().equals(NAME));

		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	void getAllCategorysTest() {
		List<Category> categorys = new ArrayList<Category>();
		categorys.add(createObjects.createCategory());

		when(categoryRepository.findAll()).thenReturn(categorys);

		List<CategoryGiveDto> result = categoryService.getAllCategorys();

		assertTrue(!result.isEmpty());
		assertTrue(result.size() == 1);

		verify(categoryRepository).findAll();
	}

	@Test
	void getAllPageCategorysTest() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Category> dtos = new ArrayList<Category>();
		dtos.add(createObjects.createCategory());
		Page<Category> page = new PageImpl<Category>(dtos);

		when(categoryRepository.findAll(pageable)).thenReturn(page);

		Page<CategoryGiveDto> result = categoryService.getAllPageCategorys(pageable);

		assertTrue(!result.isEmpty());
		assertTrue(result.getContent().size() == 1);

		verify(categoryRepository).findAll(pageable);
	}

	@Test
	void getByNameTest() {
		Category category = createObjects.createCategory();

		when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(category));

		CategoryGiveDto result = categoryService.getByName(NAME);

		assertTrue(result.getName().equals(NAME));

		verify(categoryRepository).findByName(NAME);
	}

	@Test
	void Exception_getByNameTest() {
		when(categoryRepository.findByName(NAME)).thenReturn(Optional.empty());
		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> categoryService.getByName(NAME));
		assertEquals("Категория Qweqweqweqwe - не найдена.", exception.getMessage());
	}

	@Test
	void getByIdTest() {
		Category category = createObjects.createCategory();
		UUID categoryId = category.getId();

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		CategoryGiveDto result = categoryService.getById(categoryId);

		assertTrue(result.getName().equals(NAME));

		verify(categoryRepository).findById(categoryId);
	}

	@Test
	void Exception_getByIdTest() {
		UUID categoryId = UUID.randomUUID();
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> categoryService.getById(categoryId));
		assertEquals("Категория по id: " + categoryId + " - не найдена.", exception.getMessage());
	}

	@Test
	void updateTest() {
		CategoryRequestDto dto = createObjects.createCategoryRequestDto();
		dto.setName(NAME + NAME);
		Category category = createObjects.createCategory();
		UUID categoryId = category.getId();

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.save(category)).thenReturn(category);

		CategoryGiveDto result = categoryService.update(categoryId, dto);

		assertTrue(result.getName().equals(NAME + NAME));

		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).save(category);
	}

	@Test
	void deleteTest() {
		Category category = createObjects.createCategory();
		UUID categoryId = category.getId();

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		categoryService.delete(categoryId);

		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).delete(category);
	}

}
