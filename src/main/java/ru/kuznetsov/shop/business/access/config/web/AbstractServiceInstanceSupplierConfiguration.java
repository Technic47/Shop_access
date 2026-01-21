package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import ru.kuznetsov.shop.business.access.config.ServiceInstanceListSupplierLoadBalancer;

public abstract class AbstractServiceInstanceSupplierConfiguration {

    private final DiscoveryClient discoveryClient;

    protected AbstractServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    abstract String getServiceName();

    @Bean
    public ServiceInstanceListSupplier getServiceInstanceListSupplier() {
        return new ServiceInstanceListSupplierLoadBalancer(discoveryClient, getServiceName());
    }
}
