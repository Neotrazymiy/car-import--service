package spring.exception;

import java.util.UUID;

public class CategoryNotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public CategoryNotFoundException(String name) {
		super("CATEGORY_NOT_FOUND", "Категория " + name + " - не найдена.");
	}

	public CategoryNotFoundException(UUID id) {
		super("CATEGORY_NOT_FOUND", "Категория по id: " + id + " - не найдена.");
	}

}
