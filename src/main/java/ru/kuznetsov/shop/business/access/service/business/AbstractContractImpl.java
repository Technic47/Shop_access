package ru.kuznetsov.shop.business.access.service.business;

import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.connector.WebClientConnector;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class AbstractContractImpl<E extends AbstractDto> implements AbstractContract<E> {

    private final Class<E> clazz;
    protected WebClientConnector connector;

    protected AbstractContractImpl(WebClient webClient) {
        this.clazz = ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
        this.connector = new WebClientConnector(webClient);
    }

    protected abstract String getModuleName();

    @Override
    public E getById(Long id) {
        return connector.sendGetRequest(getModuleName() + "/" + id, null, null, clazz).get(0);
    }

    @Override
    public List<E> getAll() {
        return connector.sendGetRequest(getModuleName(), null, null, clazz);
    }

    @Override
    public E create(E entity) {
        return connector.sendPostRequest(getModuleName(), null, entity, clazz).get(0);
    }

    @Override
    public Collection<E> createBatch(Collection<E> entities) {
        return connector.sendPostRequest(getModuleName() + "/batch", null, entities, clazz);
    }

    @Override
    public E update(E entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        connector.sendDeleteRequest(getModuleName() + "/" + id, null, null, clazz);
    }
}
