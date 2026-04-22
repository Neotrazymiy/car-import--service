package spring.exception;

import java.util.UUID;

public class MakeNotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public MakeNotFoundException(String makeName) {
		super("MAKE_NOT_FOUND", "Производитель " + makeName + " - не найден.");
	}

	public MakeNotFoundException(UUID id) {
		super("MAKE_NOT_FOUND", "Производитель по id: " + id + " - не найден.");
	}

}
