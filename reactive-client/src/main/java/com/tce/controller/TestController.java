package com.tce.controller;

import com.tce.config.HeaderInjectingHandler;
import com.tce.model.Employee;
import com.tce.service.TestService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
        this.registry.observationConfig().observationHandler(new HeaderInjectingHandler());
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

        Observation observation = Observation.start("webclient-sample", registry);
        final String[] traceId = new String[1];
        return Flux.just(observation).flatMap(span -> {
                    observation.scoped(() -> {
                        log.info("<ACCEPTANCE_TEST> <TRACE:{}> Hello from consumer",
                                this.tracer.currentSpan().context().traceId());
                        traceId[0] = this.tracer.currentSpan().context().traceId();
                    });
                    return WebClient.builder().build().get()
                            .uri("http://localhost:8081/employees")
                            .header("X-B3-TraceId", traceId[0])
//                            .header("X-B3-SpanId", tracer.currentSpan().context().spanId())
                            .header("traceId", traceId[0])
//                            .header("spanId", tracer.currentSpan().context().spanId())
                            .retrieve()
                            .bodyToFlux(Employee.class);
                }).doFinally(signalType -> observation.stop())
                .contextWrite(context -> context.put(ObservationThreadLocalAccessor.KEY, observation));

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
//        return Flux.deferContextual(contextView -> {
//            try (ContextSnapshot.Scope scope = ContextSnapshot.setThreadLocalsFrom(contextView,
//                    ObservationThreadLocalAccessor.KEY)) {
//                String traceId = this.tracer.currentSpan().context().traceId();
//                log.info("<ACCEPTANCE_TEST> <TRACE:{}> Hello from producer", traceId);
//                return testService.get();
//            }
//        });

    }

}
