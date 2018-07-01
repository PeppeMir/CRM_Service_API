package crm.service.restapi.service.validation;

import org.springframework.validation.Errors;

public interface ParametersValidatorService {

    void checkValidationResults(Errors validationErrors);
}
