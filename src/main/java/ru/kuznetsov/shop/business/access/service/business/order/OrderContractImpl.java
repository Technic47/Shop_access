package ru.kuznetsov.shop.business.access.service.business.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.business.access.service.business.AbstractContractImpl;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static ru.kuznetsov.shop.business.access.common.ConstValues.ORDER_MODULE;

@Service
public class OrderContractImpl extends AbstractContractImpl<OrderDto> implements OrderContract {

    private final OperationService operationService;

    protected OrderContractImpl(@Qualifier("order") WebClient webClient, OperationService operationService) {
        super(webClient);
        this.operationService = operationService;
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

    @Override
    public OrderDto create(OrderDto entity) {
        String operationId = connector.sendPostRequest(getModuleName(), new HashMap<>(), entity, String.class).get(0);
        Long entityId = operationService.getEntityIdsByOperationId(operationId).get(0);

        return getById(entityId);
    }

    @Override
    public Collection<OrderDto> createBatch(Collection<OrderDto> entities) {
        String operationId = connector.sendPostRequest(getModuleName() + "/batch", new HashMap<>(), entities, String.class).get(0);
        return operationService.getEntityIdsByOperationId(operationId)
                .stream()
                .map(this::getById)
                .toList();
    }
}
