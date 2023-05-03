package ch.basler.experimental.tracingexample;

import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagation.FactoryBuilder;
import brave.baggage.BaggagePropagationCustomizer;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Slf4j
@Configuration
public class LoggingConfig {

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeHeaders(true);
    return loggingFilter;
  }

  @Bean
  public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
    return builder.additionalInterceptors((request, body, execution) -> {
          log.info("Method: {}, URI: {}, Headers: {}", request.getMethod(), request.getURI(), request.getHeaders());
          return execution.execute(request, body);
        })
        .build();
  }

  @Bean
  public RestTemplateCustomizer getRestTemplateCustomizer() {
    return new ProxyCustomizer();
  }

  @Bean
  BaggagePropagation.FactoryBuilder myPropagationFactoryBuilder(
      ObjectProvider<BaggagePropagationCustomizer> baggagePropagationCustomizers) {
    Factory delegate = B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build();
    FactoryBuilder builder = BaggagePropagation.newFactoryBuilder(delegate);
    baggagePropagationCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    return builder;
  }
}
