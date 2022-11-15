package net.diamonddev.ddvgames.math;

import net.diamonddev.ddvgames.util.SharedUtil;
import net.minecraft.util.math.Vec2f;

public class Quadrilateral {

    private final Vec2f a;
    private final Vec2f b;

    public Quadrilateral(Vec2f A, Vec2f B) {
        this.a = A;
        this.b = B;
    }

    public Quadrilateral multiply(double d) {
        return new Quadrilateral(this.a.multiply((float)d),this.b.multiply((float)d));
    }

    public Quadrilateral square() {
        return new Quadrilateral(SharedUtil.vec2fPow(this.a, 2), SharedUtil.vec2fPow(this.b, 2));
    }

    public Vec2f getCornerA() {
        return a;
    }

    public Vec2f getCornerB() {
        return b;
    }
}
