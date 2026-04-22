package spring.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import spring.dto.CarImportDto;
import spring.model.Car;
import spring.model.Category;
import spring.model.Make;
import spring.repository.CategoryRepository;
import spring.repository.MakeRepository;

@Service
@AllArgsConstructor
public class CarImportService {

	private final MakeRepository makeRepository;
	private final CategoryRepository categoryRepository;

	public void carReations(CarImportDto dto, Car car) {
		if (dto.getMakeName() != null && !dto.getMakeName().trim().isEmpty()) {
			String makeName = dto.getMakeName().trim();

			Make make = makeRepository.findByCode(makeName).orElseGet(() -> {
				Make m = new Make();
				m.setCode(makeName);
				m.setName(makeName);
				return makeRepository.save(m);
			});

			car.setMake(make);
		}

		if (dto.getCategoryName() != null) {
			Set<Category> categories = dto.getCategoryName().stream().filter(s -> s != null && !s.trim().isEmpty())
					.map(String::trim).map(name -> categoryRepository.findByCode(name).orElseGet(() -> {
						Category c = new Category();
						c.setCode(name);
						c.setName(name);
						return categoryRepository.save(c);
					})).collect(Collectors.toSet());

			if (car.getCategorys() == null) {
				car.setCategorys(categories);
			} else {
				car.getCategorys().addAll(categories);
			}
		}
	}

}
