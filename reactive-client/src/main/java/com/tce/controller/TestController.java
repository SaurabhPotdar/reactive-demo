package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.TestService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@Slf4j
public class TestController {

    private final TestService testService;

    private final Tracer tracer;

    private final ObservationRegistry registry;

    public TestController(TestService testService, Tracer tracer, ObservationRegistry registry) {
        this.testService = testService;
        this.tracer = tracer;
        this.registry = registry;
    }

    @GetMapping("/get")
    public Flux<Employee> get() {


//        return Flux.deferContextual(contextView -> {
//            ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY);
//            String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
//            log.info("In controller traceId {}", traceId);
//            return testService.get()
//                    .doOnComplete(() -> log.info("Client"))
//                    .tap(Micrometer.observation(registry));
//        });

//        return Flux.deferContextual(contextView -> {
//            ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY);
//            String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
//            log.info("In controller traceId {}", traceId);
//            return testService.get()
//                    .handle((t, u) -> {
//                        log.info("Inside handle {}", t);
//                        u.next(t);
//                    });
//        });

        //https://github.com/micrometer-metrics/micrometer-samples/blob/bb777d40daacd0dc108e20731ce4dc4f72d47a2f/webflux/src/main/java/com/example/micrometer/WebFluxApplication.java
        return Flux.deferContextual(contextView -> {
            try (ContextSnapshot.Scope scope = ContextSnapshot.setThreadLocalsFrom(contextView,
                    ObservationThreadLocalAccessor.KEY)) {
                String traceId = this.tracer.currentSpan().context().traceId();
                log.info("<ACCEPTANCE_TEST> <TRACE:{}> Hello from producer", traceId);
                return testService.get();
            }
        });

    }

}
