package org.example.code.class_extends;

public class JavaPenguin extends JavaAnimal implements JavaSwimable, JavaFlyable {

    private final int wingCount;

    public JavaPenguin(String species, int legCount) {
        super(species, 2);
        this.wingCount = 2;
    }

    @Override
    public void move() {
        System.out.println("팽귄이 움직입니다.");
    }

    @Override
    public int getLegCount() {
        return super.getLegCount() + this.wingCount;
    }

    @Override
    public void act() {
        JavaSwimable.super.act();
        JavaFlyable.super.act();
    }
}
