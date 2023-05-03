package ch.basler.experimental.tracingexample;

import brave.propagation.B3Propagation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

  @Bean
  public OperationCustomizer customGlobalHeaders() {
    return (Operation operation, HandlerMethod handlerMethod) -> {
      B3Propagation.get().keys().forEach(header -> {
        Parameter parameter = new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
            .name(header).description("Specification at https://github.com/openzipkin/b3-propagation");
        operation.addParametersItem(parameter);
      });

      final Schema<String> baggageSchema = new Schema<>();
      baggageSchema.type("string");
      final Parameter baggage = new Parameter()
          .in(ParameterIn.HEADER.toString())
          .name("baggage")
          .description("Additional tracing context. See <a href=\"https://www.w3.org/TR/baggage/\">baggage</a> for more information. ")
          .example("cid=test12345")
          .required(false)
          .schema(baggageSchema);
      operation.addParametersItem(baggage);
      return operation;
    };
  }
}
