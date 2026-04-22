package spring.importt.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.CategoryImportDto;
import spring.importt.ReferenceImporter;
import spring.mapper.CategoryImportMapper;
import spring.model.Category;
import spring.repository.CategoryRepository;

@AllArgsConstructor
@Service
public class CategoryReferenceEntity implements ReferenceImporter<Category, CategoryImportDto> {

	private final CategoryRepository categoryRepository;
	private final CategoryImportMapper categoryImportMapper;

	@Override
	public Class<Category> getEntityClass() {
		return Category.class;
	}

	@Override
	public JpaRepository<Category, UUID> getRepository() {
		return categoryRepository;
	}

	@Override
	public Category createEntity() {
		return new Category();
	}

	@Override
	public String getKey(CategoryImportDto dto) {
		return dto.getName();
	}

	@Override
	public Function<String, Optional<Category>> finder() {
		return categoryRepository::findByCode;
	}

	@Override
	public BiConsumer<CategoryImportDto, Category> updater() {
		return categoryImportMapper::updateEntity;
	}

}
