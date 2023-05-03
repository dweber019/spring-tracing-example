package ch.basler.experimental.tracingexample;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

class ProxyCustomizer implements RestTemplateCustomizer {

  @Override
  public void customize(RestTemplate restTemplate) {
    HttpRoutePlanner routePlanner = new CustomRoutePlanner(new HttpHost("localhost", 8888));
    CloseableHttpClient httpClient = HttpClientBuilder.create().setRoutePlanner(routePlanner).build();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
  }

  static class CustomRoutePlanner extends DefaultProxyRoutePlanner {

    CustomRoutePlanner(HttpHost proxy) {
      super(proxy);
    }

    @Override
    protected HttpHost determineProxy(HttpHost target, HttpContext context) throws HttpException {
      return super.determineProxy(target, context);
    }

  }
}
