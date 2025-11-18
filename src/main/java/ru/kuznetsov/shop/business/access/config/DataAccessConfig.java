package ru.kuznetsov.shop.business.access.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;
import ru.kuznetsov.shop.parameter.service.ParameterService;

import static ru.kuznetsov.shop.parameter.common.ParameterKey.*;

@Configuration
@RequiredArgsConstructor
@Import(ParameterConfig.class)
@ComponentScan("ru.kuznetsov.shop.business.access")
public class DataAccessConfig {

    private final ParameterService parameterService;

    @Value("${microservices.baseUrl}")
    private String baseUrl;

    @Bean
    @Qualifier("address")
    public WebClient getAddressClient() {
        return getWebClient(parameterService.getParameterValueString(ADDRESS_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("product")
    public WebClient getProductClient() {
        return getWebClient(parameterService.getParameterValueString(PRODUCT_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("product-category")
    public WebClient getProductCategoryClient() {
        return getWebClient(parameterService.getParameterValueString(PRODUCT_CATEGORY_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("stock")
    public WebClient getStockClient() {
        return getWebClient(parameterService.getParameterValueString(STOCK_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("store")
    public WebClient getStoreClient() {
        return getWebClient(parameterService.getParameterValueString(STORE_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("operation")
    public WebClient getOperationClient() {
        return getWebClient(parameterService.getParameterValueString(OPERATION_PORT_PARAMETER));
    }

    @Bean
    @Qualifier("order")
    public WebClient getOrderClient() {
        return getWebClient(parameterService.getParameterValueString(ORDER_PORT_PARAMETER));
    }

    private WebClient getWebClient(String port) {
        return WebClient.builder()
                .baseUrl(baseUrl + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
