package spring.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import spring.dto.CategoryGiveDto;
import spring.dto.CategoryRequestDto;
import spring.exception.CategoryNotFoundException;
import spring.mapper.CategoryGiveMapper;
import spring.mapper.CategoryRequestMapper;
import spring.model.Category;
import spring.repository.CategoryRepository;

@Service
@AllArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryGiveMapper categoryGiveMapper;
	private final CategoryRequestMapper categoryRequestMapper;

	@Transactional
	public CategoryGiveDto create(CategoryRequestDto dto) {
		Category category = categoryRequestMapper.toEntity(dto);
		Category saved = categoryRepository.save(category);
		return categoryGiveMapper.toDto(saved);
	}

	@Transactional(readOnly = true)
	public List<CategoryGiveDto> getAllCategorys() {
		return categoryRepository.findAll().stream().map(categoryGiveMapper::toDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<CategoryGiveDto> getAllPageCategorys(Pageable pageable) {
		return categoryRepository.findAll(pageable).map(categoryGiveMapper::toDto);
	}

	@Transactional(readOnly = true)
	public CategoryGiveDto getById(UUID id) {
		return categoryRepository.findById(id).map(categoryGiveMapper::toDto)
				.orElseThrow(() -> new CategoryNotFoundException(id));
	}

	@Transactional(readOnly = true)
	public CategoryGiveDto getByName(String name) {
		return categoryRepository.findByName(name).map(categoryGiveMapper::toDto)
				.orElseThrow(() -> new CategoryNotFoundException(name));
	}

	@Transactional
	public CategoryGiveDto update(UUID id, CategoryRequestDto dto) {
		return categoryRepository.findById(id).map(entity -> {
			categoryRequestMapper.updateEntityFromDto(dto, entity);
			return categoryRepository.save(entity);
		}).map(categoryGiveMapper::toDto).orElseThrow(() -> new CategoryNotFoundException(id));
	}

	@Transactional
	public void delete(UUID id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
		categoryRepository.delete(category);
	}

}
