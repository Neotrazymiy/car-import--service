package spring.service.importt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

import spring.dto.BaseImportDto;
import spring.dto.CarImportDto;
import spring.importt.ReferenceImporter;
import spring.model.BaseReferenceEntity;
import spring.model.Car;

class RecordServiceTest {

	private RecordService recordService;

	@Mock
	private ReferenceImportService referenceImportService;

	@Mock
	private ReferenceEntityService referenceEntityService;

	@Mock
	private ReferenceImporter<BaseReferenceEntity, BaseImportDto> referenceImporter;

	@Mock
	private JpaRepository<BaseReferenceEntity, UUID> repository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		recordService = new RecordService(referenceEntityService, referenceImportService);
	}

	@Test
	void importEachDtotest() {
		CarImportDto dtoOne = new CarImportDto();
		dtoOne.setModel("qwe");

		CarImportDto dtoTwo = new CarImportDto();
		dtoTwo.setModel("qweqwe");

		List<CarImportDto> dtos = new ArrayList<CarImportDto>();
		dtos.add(dtoOne);
		dtos.add(dtoTwo);

		when(referenceEntityService.get(Car.class)).thenReturn(referenceImporter);
		when(referenceImporter.getKey(dtoOne)).thenReturn("qwe");
		when(referenceImporter.getKey(dtoTwo)).thenReturn("qweqwe");
		when(referenceImporter.finder()).thenReturn(code -> Optional.empty());
		when(referenceImporter.getRepository()).thenReturn(repository);
		when(referenceImporter.updater()).thenReturn((d, e) -> {
		});

		recordService.importRecord(dtos, Car.class);

		verify(referenceImportService, times(2)).importEntity(any(String.class), any(CarImportDto.class), any(),
				eq(repository), any(), any());
	}

	@Test
	void throwWhenKeyEmptyTest() {
		CarImportDto dto = new CarImportDto();
		dto.setModel("   ");
		List<CarImportDto> dtos = new ArrayList<CarImportDto>();
		dtos.add(dto);
		when(referenceEntityService.get(Car.class)).thenReturn(referenceImporter);
		when(referenceImporter.getKey(dto)).thenReturn("    ");

		assertThrows(RuntimeException.class, () -> recordService.importRecord(dtos, Car.class));
	}

}
