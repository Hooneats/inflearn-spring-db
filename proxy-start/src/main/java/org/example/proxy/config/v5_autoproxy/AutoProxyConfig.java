package org.example.proxy.config.v5_autoproxy;


import lombok.extern.slf4j.Slf4j;
import org.example.proxy.config.AppV1Config;
import org.example.proxy.config.AppV2Config;
import org.example.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import org.example.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * TODO : 컴포넌트 스캔 대상 및 따로 빈등록시 프록시 등록을 안해주고 한번에 적용가능하게 만들었지만
     *  생각해보면 프록시 적용은 포인트컷을 사용하는 것이 좋아보인다. 때문에 스프링 AOP 는 포인트컷을 사용해서 프록시 적용 대상 여부를 체크한다.
     *  참고로 어드바이저는 포인트컷을 가지고있다.
     *
     *  결과적으로 포인트컷은 다음 두 곳에 사용된다.
     *  1) 프록시 적용 대상 여부를 체크해서 꼭 필요한 곳에만 프록시를 적용한다.(빈 후처리기 에서 - 자동 프록시 생성)
     *      ㄴ 만약 프록시를 적용할 메서드가 없으면 생성할 필요가 없기에 이것을 @Pointcut 으로 판단
     *  2) 프록시의 어떤 메서드가 호출 되었을 때 어드바이스를 적용할 지 판단한다.(프록시 내부에서 - 어드바이스 적용)
     *
     *  TODO : 스프링 부트는 자동 프록시 생성기 - 'AutoProxyCreator'
     *    AnnotationAwareAspectJAutoProxyCreator (PostProcessor) 라는 빈 후처리기를 사용한다.
     *      ㄴ 이는 스프링 빈으로 등록된 'Advisor' 를 자동으로 찾아서 프록시가 필요한 곳에 자동으로 프록시를 적용해준다. (Advisor 안에 Pointcut 과 Advice 전부 있기에)
     *          ㄴ 뿐만아니라 @Aspect 도 자동으로 인식해서 프록시를 만들고 AOP 를 적용해준다.
     *      ㄴ 'Advisor' 를 조회 -> 'Pointcut' 조건 확인 -> 적용 대상이면 프록시 생성해서 반환, 아니면 그냥 빈 반환  -> 반환 객체를 스프링 빈으로 등록
     *
     */

    /**
     * 자동 생성 프로세서에 (AnnotationAwareAspectJAutoProxyCreator)의해 Advisor 만 빈으로 등록하면 된다.
     */
//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }


    /**
     * 하지만  포인트컷의 "request*", "order*", "save*" 조건은 우리가 등록한것 외에도
     * 기타 다른 빈에서도 사용할경우 포인트컷이 등록되는 단점이 있다.
     * TODO : 때문에 결론적으로 패키지에 메서드 이름까지 함께 지정할 수 있는 매우 정밀한 포인트컷이 필요하다
     *  ㄴ 이를 위해 'AspectJExpressionPointCut' 이 존재하고 실무에서는 이것만 사용한다.
     *                  ㄴ AOP 에 특화된 포인트컷 표현식을 적용할 수 있다.
     */
//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* org.example.proxy.app..*(..))");
        // execution(* hello.proxy.app..*(..))
        //  ㄴ-->
        //      * 은 모든 반환타입
        //      org.example.proxy.app.. 은 해당 패키지와 그 하위 전부
        //      *(..) 여기서 * 는 모든 메서드 이름, (..) 는 어떤 파라미터든 상관없다

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* org.example.proxy.app..*(..)) && !execution(* org.example.proxy.app..noLog(..))");
        //  ㄴ-->
        //      && 는 두 조건을 모두 만족해야 한다
        //      !execution 는 부정 조건

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
