package spring.exception;

public class CarAmbiguousException extends DomainException {

	private static final long serialVersionUID = 1L;

	public CarAmbiguousException(String makeName, String model) {
		super("CAR_AMBIGUOUS",
				"Найдено несколько автомобилей для make=" + makeName + ", model=" + model + ". Укажите year.");
	}

}
