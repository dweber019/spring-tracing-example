package ch.basler.experimental.tracingexample;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
class CatFactsController {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  Tracer tracer;

  @GetMapping(value = "/cats-facts")
  public String catsFacts() {
    log.info("You are at tracing endpoint");
    log.info("Contains the following baggage {}", this.tracer.getAllBaggage());
    tracer.getAllBaggage().forEach((key, value) -> log.info(String.format("Baggage KEY:%s VALUE: %s", key, value)));

    return restTemplate.exchange("http://asdfast.beobit.net/api/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<String>() {})
        .getBody();
  }
}
