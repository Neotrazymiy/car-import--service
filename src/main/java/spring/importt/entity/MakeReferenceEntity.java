package spring.importt.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.MakeImportDto;
import spring.importt.ReferenceImporter;
import spring.mapper.MakeImportMapper;
import spring.model.Make;
import spring.repository.MakeRepository;

@Service
@AllArgsConstructor
public class MakeReferenceEntity implements ReferenceImporter<Make, MakeImportDto> {

	private final MakeRepository makeRepository;
	private final MakeImportMapper makeImportMapper;

	@Override
	public Class<Make> getEntityClass() {
		return Make.class;
	}

	@Override
	public JpaRepository<Make, UUID> getRepository() {
		return makeRepository;
	}

	@Override
	public Make createEntity() {
		return new Make();
	}

	@Override
	public String getKey(MakeImportDto dto) {
		return dto.getName();
	}

	@Override
	public Function<String, Optional<Make>> finder() {
		return makeRepository::findByCode;
	}

	@Override
	public BiConsumer<MakeImportDto, Make> updater() {
		return makeImportMapper::updateEntity;
	}

}
