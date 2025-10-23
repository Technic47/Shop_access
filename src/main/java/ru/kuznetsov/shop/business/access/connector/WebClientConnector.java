package ru.kuznetsov.shop.business.access.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.kuznetsov.shop.business.access.exceptions.ServiceException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class WebClientConnector {

    @Value("${web.client.retry}")
    private long RETRY;
    @Value("${web.client.max-attempts}")
    private int MAX_ATTEMPTS;

    private final WebClient webClient;

    public WebClientConnector(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> List<T> sendGetRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        return sendRequest(HttpMethod.GET, uri, queryParams, body, clazz);
    }

    public <T> List<T> sendPostRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        return sendRequest(HttpMethod.POST, uri, queryParams, body, clazz);
    }

    public <T> void sendDeleteRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        sendRequest(HttpMethod.DELETE, uri, queryParams, body, clazz);
    }

    private <T> List<T> sendRequest(HttpMethod method, String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        Mono<List<T>> mono = webClient.method(method)
                .uri(uriBuilder -> {
                    uriBuilder.path(uri);
                    for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                        uriBuilder.queryParam(entry.getKey(), entry.getValue());
                    }
                    return uriBuilder.build();
                })
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    System.out.println("Логирование 4** ошибок");
                    return response.bodyToMono(String.class).map(RuntimeException::new);
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    System.out.println("Логирование 5** ошибок");
                    return Mono.empty();
                })
                .bodyToFlux(clazz)
                .retryWhen(Retry.backoff(MAX_ATTEMPTS, Duration.ofMillis(RETRY))
                        .jitter(0.75)
                        .doBeforeRetry(x -> System.out.println("Повторная попытка отправки " + x.totalRetries()))
                        .filter(throwable -> throwable instanceof ServiceException
                                || throwable instanceof WebClientRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("Повторные попытки исчерпаны. Внешний сервис недоступен.",
                                    HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .onErrorResume(ServiceException.class, err -> {
                    System.out.println("Код ошибки: " + err.getStatusCode());
                    err.printStackTrace();
                    return Mono.empty();
                })
                .collectList();
        return mono.block();
    }
}
