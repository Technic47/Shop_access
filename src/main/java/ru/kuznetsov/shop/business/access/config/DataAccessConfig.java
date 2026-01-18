package ru.kuznetsov.shop.business.access.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;

@Configuration
@EnableDiscoveryClient
@RequiredArgsConstructor
@Import(ParameterConfig.class)
@ComponentScan("ru.kuznetsov.shop.business.access")
public class DataAccessConfig {

    private final EurekaClient discoveryClient;

    @Value("${service.address.url}")
    private String addressUrl;

    @Value("${service.product.url}")
    private String productUrl;

    @Value("${service.product-category.url}")
    private String productCategoryUrl;

    @Value("${service.store.url}")
    private String storeUrl;

    @Value("${service.stock.url}")
    private String stockUrl;

    @Value("${service.order.url}")
    private String orderUrl;

    @Value("${service.operation.url}")
    private String operationUrl;

    @Bean
    @LoadBalanced
    @Qualifier("address")
    public WebClient getAddressClient() {
        return getWebClient(addressUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("product")
    public WebClient getProductClient() {
        return getWebClient(productUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("product-category")
    public WebClient getProductCategoryClient() {
        return getWebClient(productCategoryUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("stock")
    public WebClient getStockClient() {
        return getWebClient(stockUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("store")
    public WebClient getStoreClient() {
        return getWebClient(storeUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("operation")
    public WebClient getOperationClient() {
        return getWebClient(operationUrl);
    }

    @Bean
    @LoadBalanced
    @Qualifier("order")
    public WebClient getOrderClient() {
        return getWebClient(orderUrl);
    }

//    private WebClient getWebClient(String port) {
//        return WebClient.builder()
//                .baseUrl(baseUrl + ":" + port)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }

    private WebClient getWebClient(String url) {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(url, false);

        return WebClient.builder()
                .baseUrl(instance.getHomePageUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
