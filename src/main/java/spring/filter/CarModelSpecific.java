package spring.filter;

import org.springframework.data.jpa.domain.Specification;

import spring.model.Car;

public class CarModelSpecific {

	public static Specification<Car> hasModel(String model) {
		return (root, query, cb) -> cb.equal(cb.lower(root.get("model")), model.toLowerCase());
	}

	public static Specification<Car> hasMinYear(Integer minYear) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("year"), minYear);
	}

	public static Specification<Car> hasMaxYear(Integer maxYear) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("year"), maxYear);
	}

	public static Specification<Car> hasMake(String makeName) {
		return (root, quert, cb) -> cb.equal(cb.lower(root.get("make").get("name")), makeName);
	}

	public static Specification<Car> hasCategory(String categoryName) {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.equal(root.join("categorys").get("name"), categoryName.toLowerCase());
		};
	}

}
