package crm.service.restapi.configuration;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Bean
    public ErrorAttributes errorAttributes() {

        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(
                    final RequestAttributes requestAttributes,
                    final boolean includeStackTrace) {

                final Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);

                // do not return the exception name in the response
                errorAttributes.remove("exception");

                return errorAttributes;
            }
        };
    }
}
