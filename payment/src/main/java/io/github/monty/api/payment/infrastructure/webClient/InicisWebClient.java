package io.github.monty.api.payment.infrastructure.webClient;

import io.github.monty.api.payment.domain.repository.InicisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class InicisWebClient implements InicisRepository {
    private final WebClient webClient;

    public void requestApprovePayment() {

    }
}
