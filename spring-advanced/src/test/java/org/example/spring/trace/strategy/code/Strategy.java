package org.example.spring.trace.strategy.code;

/**
 * 변하는 로직
 *
 * TODO : 전략 패턴은
 *  변하지 않는 부분을 context 에 두고 변하는 부분을 strategy 에 넣는다
 *  이것은 선조립 후 실행 방식에서 유용하다.
 *   ㄴ 이 방식의 단점은 context 와 strategy 를 조립한 이후에는 Setter 를 사용해 변경하는 등 변경이 번거롭다.
 *      만약 context 를 싱글톤으로 사용시에는 동시성 이슈까지 고려해야한다.
 *      때문에 그냥 예제처럼 새로 만들어 각각 따로 가져가는게 좋을 수도 있다.
 *       ㄴ ContextV2 방식이 제일 나아보인다.(이를 스프링에서 템플릿 콜백 패턴이라고도 한다. -> xxxTemplate 이 있다면 보통 이게 적용됨)
 *          ㄴ ContextV2 처럼 다른 코드의 인수로서 넘겨주는 '실행 가능한 코드' 를 콜백(callback) 이라고 한다.
 *                      ㄴ call(코드가 호출 되는데) back(에프터함수 즉, 넘겨준 곳에서 실행한다) -자바에서는 익명내부클래스, 람다로 가능
 *                          ㄴ context -> template / strategy -> callback
 */
public interface Strategy {
    void call();
}
