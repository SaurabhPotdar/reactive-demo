package com.tce.service;

import com.tce.model.Employee;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@Configuration
public class TestService {

    private final Tracer tracer;

    private final String serverUrl;

    private final WebClient webClient = WebClient.builder().build();

    public TestService(Tracer tracer, @Value("${server.url}") String serverUrl) {
        this.tracer = tracer;
        this.serverUrl = serverUrl;
    }

    public Flux<Employee> get() {
        log.info("Get employees");
        return webClient.get()
                .uri(serverUrl + "/employees")
                .header("X-B3-TraceId", tracer.currentSpan().context().traceId())
                .header("X-B3-SpanId", tracer.currentSpan().context().spanId())
                .header("traceId", tracer.currentSpan().context().traceId())
                .header("spanId", tracer.currentSpan().context().spanId())
                .retrieve()
                .bodyToFlux(Employee.class)
                .doOnComplete(() -> log.info(tracer.currentTraceContext().context().traceId() + " " + tracer.currentSpan().context().traceId()))
                .doOnComplete(() -> log.info("Received response from server"));
    }

//    public OtelTracer otelTracer() {
//
//        SpanExporter spanExporter = new ZipkinSpanExporterBuilder()
//                .setSender(URLConnectionSender.create("http://localhost:9411/api/v2/spans")).build();
//
//// [OTel component] SdkTracerProvider is a SDK implementation for TracerProvider
//        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder().setSampler(alwaysOn())
//                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build()).build();
//
//// [OTel component] The SDK implementation of OpenTelemetry
//        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
//                .setPropagators(ContextPropagators.create(B3Propagator.injectingMultiHeaders())).build();
//
//// [OTel component] Tracer is a component that handles the life-cycle of a span
//        io.opentelemetry.api.trace.Tracer otelTracer = openTelemetrySdk.getTracerProvider()
//                .get("io.micrometer.micrometer-tracing");
//
//// [Micrometer Tracing component] A Micrometer Tracing wrapper for OTel
//        OtelCurrentTraceContext otelCurrentTraceContext = new OtelCurrentTraceContext();
//
//// [Micrometer Tracing component] A Micrometer Tracing listener for setting up MDC
//        Slf4JEventListener slf4JEventListener = new Slf4JEventListener();
//
//// [Micrometer Tracing component] A Micrometer Tracing listener for setting
//// Baggage in MDC. Customizable
//// with correlation fields (currently we're setting empty list)
//        Slf4JBaggageEventListener slf4JBaggageEventListener = new Slf4JBaggageEventListener(Collections.emptyList());
//
//// [Micrometer Tracing component] A Micrometer Tracing wrapper for OTel's Tracer.
//// You can consider
//// customizing the baggage manager with correlation and remote fields (currently
//// we're setting empty lists)
//        return new OtelTracer(otelTracer, otelCurrentTraceContext, event -> {
//            slf4JEventListener.onEvent(event);
//            slf4JBaggageEventListener.onEvent(event);
//        }, new OtelBaggageManager(otelCurrentTraceContext, Collections.emptyList(), Collections.emptyList()));
//
//    }

}
