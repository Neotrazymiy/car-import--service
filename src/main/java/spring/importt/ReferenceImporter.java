package spring.importt;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.dto.BaseImportDto;
import spring.model.BaseReferenceEntity;

public interface ReferenceImporter<T extends BaseReferenceEntity, D extends BaseImportDto> {

	Class<T> getEntityClass();

	JpaRepository<T, UUID> getRepository();

	T createEntity();

	// это значение строчки колонки которое будет записан в виде ключа в поле code и
	// так же по нему будет поиск ентити
	String getKey(D dto);

	// для нахождения сущности по ключу, если она уже есть в базе
	Function<String, Optional<T>> finder();

	BiConsumer<D, T> updater();

}
