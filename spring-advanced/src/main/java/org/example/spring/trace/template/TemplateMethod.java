package org.example.spring.trace.template;

/**
 * TODO : 템플릿 메서드 패턴은 = 변하는 부분과 변하지 않는부분을 분리할 수 있게 해준다.
 *  ㄴ ex) TemplateMethodTest, AbstractTemplate , FunctionTemplate
 *   ㄴ 알고리즘의 골격을 정의하고 일부 단계를 하위 클래스로 연기한다.
 *   템플릿 메서드를 사용하면 하위 클래스가 알고리즘의 구조를 변경하지 않고도 특정 단계를 재정의 할 수 있다.
 *   하지만, 템플릿 메서드는 상속을 사용하기에 상속의 단점을 그대로 가져간다. --> 전략패턴으로 해결가능(변하지 않는부분과 변하는부분을 완전 분리)
 */
public class TemplateMethod {
}
