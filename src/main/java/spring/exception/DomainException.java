package spring.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String code;

	public DomainException(String code, String message) {
		super(message);
		this.code = code;
	}

}
