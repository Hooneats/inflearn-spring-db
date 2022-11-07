package org.example.proxy.config.v6_aop;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.config.AppV1Config;
import org.example.proxy.config.AppV2Config;
import org.example.proxy.config.v6_aop.aspect.LogTraceAspect;
import org.example.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
