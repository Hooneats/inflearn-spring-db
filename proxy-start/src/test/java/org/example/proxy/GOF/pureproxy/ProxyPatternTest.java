package org.example.proxy.GOF.pureproxy;

import org.example.proxy.GOF.pureproxy.proxy.code.ProxyPatternClient;
import org.example.proxy.GOF.pureproxy.proxy.code.RealSubject;
import org.example.proxy.GOF.pureproxy.proxy.code.Subject;
import org.example.proxy.GOF.pureproxy.proxy.code.cache.CacheProxy;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        client.execute();
        client.execute();
        client.execute();
        // 총 3초의 시간 걸림
    }

    /**
     * RealSubject, Subject 와 ProxyPatternClient 코드를 전혀 건드리지 않고 접근제어 적용이 가능했다.(CacheProxy 적용해봄)
     */
    @Test
    void cacheProxyTest() {
        Subject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);

        client.execute();
        client.execute();
        client.execute();
        // 시간이 굉장히 빨리 걸린다. (결과가 처음만 1초걸리고 나머지는 바로바로 나옴)
    }
}
