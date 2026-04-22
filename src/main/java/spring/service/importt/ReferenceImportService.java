package spring.service.importt;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import spring.dto.BaseImportDto;
import spring.model.BaseReferenceEntity;

@Service
@AllArgsConstructor
public class ReferenceImportService {

	@Transactional
	public <T extends BaseReferenceEntity, D extends BaseImportDto> T importEntity(String key, D dto,
			Function<String, Optional<T>> finder, JpaRepository<T, UUID> repository, Supplier<T> supplier,
			BiConsumer<D, T> mapper) {
		T entity = finder.apply(key).orElseGet(supplier);
		if (entity.getCode() == null || entity.getCode().isEmpty() || !entity.getCode().equals(key)) {
			entity.setCode(key);
		}
		mapper.accept(dto, entity);
		return repository.save(entity);
	}
}
