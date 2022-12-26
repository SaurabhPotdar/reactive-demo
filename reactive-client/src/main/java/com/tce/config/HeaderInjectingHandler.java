package com.tce.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequestObservationContext;

@Slf4j
public class HeaderInjectingHandler implements ObservationHandler<ClientRequestObservationContext> {

        @Override
        public void onStart(ClientRequestObservationContext context) {
            log.info(context.getRemoteServiceName());
            context.getSetter().set(context.getCarrier(), "traceId", "1000");
            context.getSetter().set(context.getCarrier(), "X-B3-TraceId", "2000");
        }

        @Override
        public boolean supportsContext(Observation.Context context) {
            return context instanceof ClientRequestObservationContext;
        }

}
