package spring.exception;

import java.util.UUID;

public class CarNotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public CarNotFoundException(String makeName, String model, Integer year) {
		super("CAR_NOT_FOUND",
				year == null ? "Автомобиль по данным (make - " + makeName + ", model - " + model + ") - не найден."
						: "Автомобиль по данным (make - " + makeName + ", model - " + model + ", year - " + year
								+ ") - не найден.");
	}

	public CarNotFoundException(UUID id) {
		super("CAR_NOT_FOUND", "Автомобиль по id: " + id + ", - не найден.");
	}

}
