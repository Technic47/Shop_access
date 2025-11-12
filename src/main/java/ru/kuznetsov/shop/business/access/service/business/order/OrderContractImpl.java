package ru.kuznetsov.shop.business.access.service.business.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.business.AbstractContractImpl;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static ru.kuznetsov.shop.business.access.common.ConstValues.ORDER_MODULE;

@Service
public class OrderContractImpl extends AbstractContractImpl<OrderDto> implements OrderContract {

    protected OrderContractImpl(@Qualifier("order") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return ORDER_MODULE;
    }

    @Override
    public Collection<OrderDto> getAllByCustomerId(UUID customerId) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("customerId", customerId.toString()),
                null,
                OrderDto.class
        );
    }
}
