package spring.service.importt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AllArgsConstructor;
import spring.dto.BaseImportDto;
import spring.model.BaseReferenceEntity;

@Service
@AllArgsConstructor
public class CsvService {

	private final RecordService recordService;
	private final Validator validator;

	public <T extends BaseReferenceEntity, D extends BaseImportDto> void importCsv(MultipartFile file, Class<T> clazz,
			Class<D> dtoClass) {
		try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			CsvToBean<D> csv = new CsvToBeanBuilder<D>(reader).withType(dtoClass).withIgnoreLeadingWhiteSpace(true)
					.build();
			List<D> dtos = csv.parse();
			for (D dto : dtos) {
				Set<ConstraintViolation<D>> violations = validator.validate(dto);
				if (!violations.isEmpty()) {
					throw new RuntimeException(violations.iterator().next().getMessage());
				}
			}
			recordService.importRecord(dtos, clazz);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
