package crm.service.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingFieldsException extends RuntimeException {

	private static final long serialVersionUID = 601525188703257331L;

	public MissingFieldsException(final String fieldname) {
	    super(fieldname);
    }

    public MissingFieldsException(final List<FieldError> fieldErrors) {
        super(parseFieldErrors(fieldErrors));
    }

    private static String parseFieldErrors(List<FieldError> fieldErrors) {

        final StringBuilder sb = new StringBuilder("Invalid fields: ");
        fieldErrors.stream()
                .map(FieldError::getField)
                .forEach(fieldName -> sb.append(fieldName).append(", "));

        final String responseStr = sb.toString();

        return responseStr.substring(0, responseStr.length() - 2);
    }
}
