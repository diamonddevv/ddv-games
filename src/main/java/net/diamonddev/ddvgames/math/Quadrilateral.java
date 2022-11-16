package net.diamonddev.ddvgames.math;

import net.minecraft.util.math.Vec2f;

public class Quadrilateral {

    private final Vec2f a;
    private final Vec2f b;

    public Quadrilateral(Vec2f A, Vec2f B) {
        this.a = A;
        this.b = B;
    }

    public Vec2f getCornerA() {
        return a;
    }

    public Vec2f getCornerB() {
        return b;
    }
}
