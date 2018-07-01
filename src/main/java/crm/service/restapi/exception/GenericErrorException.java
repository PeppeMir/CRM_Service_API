package crm.service.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericErrorException extends RuntimeException {

	private static final long serialVersionUID = 601525188703257331L;
	
	private final String reason;

    public GenericErrorException(final String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
