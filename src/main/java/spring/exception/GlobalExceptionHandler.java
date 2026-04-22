package spring.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MakeNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ApiError handleMakeNotFound(MakeNotFoundException exception) {
		return new ApiError(exception.getCode(), exception.getMessage());
	}

	@ExceptionHandler(CarNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ApiError handleCarNotFound(CarNotFoundException exception) {
		return new ApiError(exception.getCode(), exception.getMessage());
	}

	@ExceptionHandler(CarAmbiguousException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public ApiError handleCarAmbiguous(CarAmbiguousException exception) {
		return new ApiError(exception.getCode(), exception.getMessage());
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ApiError handleCarAmbiguous(CategoryNotFoundException exception) {
		return new ApiError(exception.getCode(), exception.getMessage());
	}

}
