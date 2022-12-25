package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.TestService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@Slf4j
public class TestController {

    private final TestService testService;

    private final Tracer tracer;

    public TestController(TestService testService, Tracer tracer) {
        this.testService = testService;
        this.tracer = tracer;
    }

    @GetMapping("/get")
    public Flux<Employee> get() {
        return Flux.deferContextual(contextView -> {
            ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY);
            String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
            log.info("In controller traceId {}", traceId);
            return testService.get()
                    .handle((t, u) -> {
                        log.info("Inside handle {}", t);
                        u.next(t);
                    });
        });
    }

}
