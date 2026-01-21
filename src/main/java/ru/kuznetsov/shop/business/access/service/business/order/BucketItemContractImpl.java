package ru.kuznetsov.shop.business.access.service.business.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.business.AbstractContractImpl;
import ru.kuznetsov.shop.represent.contract.order.BucketItemContract;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static ru.kuznetsov.shop.business.access.common.ConstValues.ORDER_BUCKET_MODULE;

@Service
public class BucketItemContractImpl extends AbstractContractImpl<BucketItemDto> implements BucketItemContract {

    protected BucketItemContractImpl(@Qualifier("order") WebClient.Builder webClientBuilder) {
        super(webClientBuilder);
    }

    @Override
    protected String getModuleName() {
        return ORDER_BUCKET_MODULE;
    }

    @Override
    public Collection<BucketItemDto> getAllByCustomerId(UUID customerId) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("customerId", customerId.toString()),
                null,
                BucketItemDto.class
        );
    }

    @Override
    public Collection<BucketItemDto> getAllByOrderId(Long orderId) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("orderId", orderId),
                null,
                BucketItemDto.class
        );
    }
}
