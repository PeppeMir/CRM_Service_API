package crm.service.restapi.service.validation;

import crm.service.restapi.exception.MissingFieldsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service("parametersValidatorService")
public class ParametersValidatorServiceImpl implements ParametersValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void checkValidationResults(final Errors validationErrors) {
        if (validationErrors.hasFieldErrors()) {
            logger.error("Validation error: {}", validationErrors.getFieldErrors());
            throw new MissingFieldsException(validationErrors.getFieldErrors());
        }
    }
}
