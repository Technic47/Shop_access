package ru.kuznetsov.shop.business.access.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static ru.kuznetsov.shop.business.access.common.ConstValues.*;

@Configuration
@ComponentScan("ru.kuznetsov.shop.business.access")
public class DataAccess {

    @Value("${microservices.baseUrl}")
    private String baseUrl;

    @Bean
    @Qualifier("address")
    public WebClient getAddressClient() {
        return getWebClient(ADDRESS_PORT);
    }

    @Bean
    @Qualifier("product")
    public WebClient getProductClient() {
        return getWebClient(PRODUCT_PORT);
    }

    @Bean
    @Qualifier("product-category")
    public WebClient getProductCategoryClient() {
        return getWebClient(PRODUCT_CATEGORY_PORT);
    }

    @Bean
    @Qualifier("stock")
    public WebClient getStockClient() {
        return getWebClient(STOCK_PORT);
    }

    @Bean
    @Qualifier("store")
    public WebClient getStoreClient() {
        return getWebClient(STORE_PORT);
    }

    private WebClient getWebClient(String port){
        return WebClient.builder()
                .baseUrl(baseUrl + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
