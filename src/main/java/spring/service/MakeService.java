package spring.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import spring.dto.MakeGiveDto;
import spring.dto.MakeRequestDto;
import spring.exception.MakeNotFoundException;
import spring.mapper.MakeGiveMapper;
import spring.mapper.MakeRequestMapper;
import spring.model.Make;
import spring.repository.MakeRepository;

@Service
@AllArgsConstructor
public class MakeService {

	private final MakeRepository makeRepository;
	private final MakeGiveMapper makeGiveMapper;
	private final MakeRequestMapper makeRequestMapper;

	@Transactional
	public MakeGiveDto create(MakeRequestDto dto) {
		Make make = makeRequestMapper.toEntity(dto);
		Make saved = makeRepository.save(make);
		return makeGiveMapper.toDto(saved);
	}

	@Transactional(readOnly = true)
	public List<MakeGiveDto> getAllMakes() {
		return makeRepository.findAll().stream().map(makeGiveMapper::toDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<MakeGiveDto> getAllPageMakes(Pageable pageable) {
		return makeRepository.findAll(pageable).map(makeGiveMapper::toDto);
	}

	@Transactional(readOnly = true)
	public MakeGiveDto getByName(String make) {
		return makeRepository.findByName(make).map(makeGiveMapper::toDto)
				.orElseThrow(() -> new MakeNotFoundException(make));
	}

	@Transactional(readOnly = true)
	public MakeGiveDto getById(UUID id) {
		return makeRepository.findById(id).map(makeGiveMapper::toDto).orElseThrow(() -> new MakeNotFoundException(id));
	}

	@Transactional
	public MakeGiveDto update(UUID id, MakeRequestDto dto) {
		return makeRepository.findById(id).map(entity -> {
			if (entity.getName().equals(dto.getName())) {
				return entity;
			}
			makeRequestMapper.updateEntityFromDto(dto, entity);
			return makeRepository.save(entity);
		}).map(makeGiveMapper::toDto).orElseThrow(() -> new MakeNotFoundException(id));
	}

	@Transactional
	public void delete(UUID id) {
		Make make = makeRepository.findById(id).orElseThrow(() -> new MakeNotFoundException(id));
		makeRepository.delete(make);
	}

}
