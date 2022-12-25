package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.EmployeeService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    private final ObservationRegistry registry;

    private final Tracer tracer;

    public EmployeeController(EmployeeService employeeService, ObservationRegistry registry, Tracer tracer) {
        this.employeeService = employeeService;
        this.registry = registry;
        this.tracer = tracer;
    }

    @GetMapping("/employees")
    public Flux<Employee> getEmployees() {
        //https://stackoverflow.com/questions/74601706/spring-boot-3-webflux-application-with-micrometer-tracing-not-showing-traceid-an
        //Use handle or tap
        //.tap(() -> Micrometer.observation(registry));
        //FIXME Giving same traceId for new requests also, how to close this
        //FIXME  just call tap() or handle() and then you'll get the MDC correlated for you.
        return Flux.deferContextual(contextView -> {
            ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY);
            String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
            log.info("In controller traceId {}", traceId);
            return employeeService.getEmployees()
                    .handle((t, u) -> {
                    log.info("Inside handle {}", t);
                    u.next(t);
                });
        });
    }

}
