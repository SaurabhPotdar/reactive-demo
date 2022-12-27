package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.TestService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.observability.DefaultSignalListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

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

        Observation parent = Observation.start("parent", registry);
        Observation child = Observation.start("child", registry)
                .parentObservation(parent);

        return testService.get()
                .tap(() -> new DefaultSignalListener<>() {
                    @Override
                    public void doFirst() throws Throwable {
                        log.info("We're using tap() -> there will be Observation in thread local here ["
                                + registry.getCurrentObservation() + "]");
                    }
                })
                .transformDeferredContextual((employeeFlux, contextView) -> employeeFlux.doOnNext(employee -> {
                    try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(contextView)) {
                        log.info("We're retrieving thread locals from Reactor Context - there will be Observation in thread local here ["
                                + registry.getCurrentObservation() + "]");
                    }
                }))
                .handle((BiConsumer<Employee, SynchronousSink<Employee>>) (employee, synchronousSink) -> {
                    log.info("We're using handle() -> There will be Observation in thread local here ["
                            + registry.getCurrentObservation() + "]");
                    synchronousSink.next(employee);
                })
                .doFinally(signalType -> child.stop())
                .contextWrite(context -> context.put(ObservationThreadLocalAccessor.KEY, child))
                .contextCapture()
                .doOnComplete(parent::stop);

    }

    @GetMapping("/test")
    public Mono<Integer> test() {
        Observation parent = Observation.start("parent", registry);
        Observation child = Observation.start("child", registry)
                .parentObservation(parent);
        Mono<Integer> block = Mono.just(1)
                .doOnNext(integer -> {
                    log.info(
                            "No context propagation happens by default in Reactor - there will be no Observation in thread local here ["
                                    + registry.getCurrentObservation() + "]");
                })
                .tap(() -> new DefaultSignalListener<>() {
                    @Override
                    public void doFirst() {
                        log.info("We're using tap() -> there will be Observation in thread local here ["
                                + registry.getCurrentObservation() + "]");
                    }
                }).flatMap(integer -> Mono.just(integer).map(monoInteger -> monoInteger + 1))
                .transformDeferredContextual((integerMono, contextView) -> integerMono.doOnNext(integer -> {
                    try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(contextView)) {
                        log.info(
                                "We're retrieving thread locals from Reactor Context - there will be Observation in thread local here ["
                                        + registry.getCurrentObservation() + "]");
                    }
                }))
                // Example of having entries in thread local for <handle()> operator
                .handle((BiConsumer<Integer, SynchronousSink<Integer>>) (integer, synchronousSink) -> {
                    log.info("We're using handle() -> There will be Observation in thread local here ["
                            + registry.getCurrentObservation() + "]");
                    synchronousSink.next(integer);
                })
                //.doFinally(signalType -> child.stop())
                .contextWrite(context -> context.put(ObservationThreadLocalAccessor.KEY, child))
                .contextCapture();
//        parent.stop();
        return block;
    }

}