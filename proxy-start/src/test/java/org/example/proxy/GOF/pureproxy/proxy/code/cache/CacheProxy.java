package org.example.proxy.GOF.pureproxy.proxy.code.cache;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.GOF.pureproxy.proxy.code.Subject;

// TODO : 프록시를 통해 캐시 적용
// TODO : Cache Proxy 적용 전 = Client ----> RealSubject
// TODO : Cache Proxy 적용 후 = Client ----> Proxy ------> RealSubject
@Slf4j
public class CacheProxy implements Subject {

    private final Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
