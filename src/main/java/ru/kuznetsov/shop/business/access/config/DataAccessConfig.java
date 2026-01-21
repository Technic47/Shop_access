package ru.kuznetsov.shop.business.access.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.config.web.*;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;

@Configuration
@EnableDiscoveryClient
@RequiredArgsConstructor
@Import(ParameterConfig.class)
@ComponentScan("ru.kuznetsov.shop.business.access")
@LoadBalancerClients({
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "address", configuration = AddressServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "product", configuration = ProductServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "product-category", configuration = ProductCategoryServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "store", configuration = StoreServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "stock", configuration = StockServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "order", configuration = OrderServiceInstanceSupplierConfiguration.class),
        @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "operation", configuration = OperationServiceInstanceSupplierConfiguration.class)
})
public class DataAccessConfig {

    @Bean
    @LoadBalanced
    @Qualifier("address")
    public WebClient.Builder getAddressClient() {
        return getWebClientBuilder("http://address/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("product")
    public WebClient.Builder getProductClient() {
        return getWebClientBuilder("http://product/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("product-category")
    public WebClient.Builder getProductCategoryClient() {
        return getWebClientBuilder("http://product-category/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("store")
    public WebClient.Builder getStoreClient() {
        return getWebClientBuilder("http://store/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("stock")
    public WebClient.Builder getStockClient() {
        return getWebClientBuilder("http://stock/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("order")
    public WebClient.Builder getOrderClient() {
        return getWebClientBuilder("http://order/");
    }

    @Bean
    @LoadBalanced
    @Qualifier("operation")
    public WebClient.Builder getOperationClient() {
        return getWebClientBuilder("http://operation/");
    }

    public WebClient.Builder getWebClientBuilder(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
