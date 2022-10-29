package org.example.proxy;

import org.example.proxy.config.AppV1Config;
import org.example.proxy.config.AppV2Config;
import org.example.proxy.config.v1_proxy.ConcreteProxyConfig;
import org.example.proxy.config.v1_proxy.InterfaceProxyConfig;
import org.example.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import org.example.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import org.example.proxy.trace.logtrace.LogTrace;
import org.example.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * TODO :
 *  프록시는 두가지 일을한다.
 *  1. 접근제어 => 대신접근해주는 역할을 함으로써 좀 더 빨른 접근을 할 수 있도록한다.(B마트처럼 내가마트에없지만 대리자가 장봐주듯이)
 *  			접근제어 : 1) 권한에 따른 접근 차단
 *  					 2)	캐싱
 *  					 3) 지연 로딩
 *  2. 부가기능 => 클라이언트가 원하는 요청 외에 부가기능을 추가 할 수 있다.
 *  			부가기능 예 : 1) 요청 값이나, 응답 값을 중간에 변형
 *  						2) 실행 시간을 측정해서 추가 로그를 남김
 *  // GOF 디자인 패턴에서는 의도(intent) 에 따라 위의 두가지 기능을 나누어 본다.
 *  	프록시 패턴 : 접근제어가 목적
 *  	데코레이터 패턴 : 새로운 기능 추가가 목적
 *
 * TODO : 클래스 의존관계 : 서버와 프록시가 같은 인터페이스를 사용하고 클라이언트는 인터페이스에 의존해야한다.
 *  Client  -----요청----------------> ServerInterface
 *  										 |
 *  						  Proxy ------------------ Server
 */
 //@Import(AppV1Config.class)
//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
@Import(DynamicProxyFilterConfig.class)
@SpringBootApplication(scanBasePackages = "org.example.proxy.app") //주의 :  왜 app 을 지정했냐면 @Import 를 써서 수동등록을 보여주기위해
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
