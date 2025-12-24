package ru.kuznetsov.shop.business.access.service.business.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.business.AbstractContractImpl;
import ru.kuznetsov.shop.represent.contract.order.OrderStatusContract;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static ru.kuznetsov.shop.business.access.common.ConstValues.ORDER_STATUS_MODULE;

@Service
public class OrderStatusContractImpl extends AbstractContractImpl<OrderStatusDto> implements OrderStatusContract {

    protected OrderStatusContractImpl(@Qualifier("order") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return ORDER_STATUS_MODULE;
    }

    @Override
    public Collection<OrderStatusDto> getAllByOrderId(Long orderId) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("orderId", orderId),
                null,
                OrderStatusDto.class
        );
    }

    @Override
    public Optional<OrderStatusDto> getLastByOrderId(Long orderId) {
        return Optional.of(connector.sendGetRequest(
                getModuleName() + "/last",
                Collections.singletonMap("orderId", orderId),
                null,
                OrderStatusDto.class
        ).get(0));
    }

    @Override
    public Collection<OrderStatusDto> getAllByStatus(OrderStatusType status) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("status", status),
                null,
                OrderStatusDto.class
        );
    }
}
