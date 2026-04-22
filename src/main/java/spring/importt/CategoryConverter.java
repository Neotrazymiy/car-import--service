package spring.importt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CategoryConverter extends AbstractBeanField<Set<String>, String> {

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		if (value == null || value.isEmpty()) {
			return Collections.EMPTY_SET;
		}
		return Arrays.stream(value.split("[,;|/]")).map(String::trim).collect(Collectors.toSet());
	}

}
