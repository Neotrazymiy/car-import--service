package spring.service.importt;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.BaseImportDto;
import spring.importt.ReferenceImporter;
import spring.model.BaseReferenceEntity;

@Service
@AllArgsConstructor
public class RecordService {

	private final ReferenceEntityService referenceEntityService;
	private final ReferenceImportService referenceImportService;

	public <T extends BaseReferenceEntity, D extends BaseImportDto> void importRecord(List<D> dto, Class<T> clazz) {
		ReferenceImporter<T, D> importer = referenceEntityService.get(clazz);

		dto.forEach((dtos -> {
			String key = importer.getKey(dtos);
			if (key == null || key.trim().isEmpty()) {
				throw new RuntimeException("Не удалось определить ключ для " + clazz.getSimpleName());
			}
			referenceImportService.importEntity(key, dtos, importer.finder(), importer.getRepository(),
					importer::createEntity, importer.updater());
		}));
	}
}
