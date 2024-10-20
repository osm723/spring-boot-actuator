package hello.order.gauge;

import hello.order.OrderService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockConfigV1 {

    @Bean
    public MyStockMetric myStockMetric(OrderService service, MeterRegistry registry) {
        return new MyStockMetric(service, registry);
    }

    @Slf4j
    static class MyStockMetric {
        private OrderService service;
        private MeterRegistry registry;

        public MyStockMetric(OrderService service, MeterRegistry registry) {
            this.service = service;
            this.registry = registry;
        }

        @PostConstruct
        public void init() {
            Gauge.builder("my.stock", service, service -> {
                log.info("stock gauge call");
                return service.getStock().get();
            }).register(registry);
        }
    }
}
