package net.diamonddev.ddvgames.math;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Cube extends Box {
    private final Vec3d b;
    private final Vec3d a;

    public Cube(Vec3d pos1, Vec3d pos2) {
        super(pos1, pos2);
        this.a = pos1;
        this.b = pos2;
    }

    public Vec3d getCornerA() {
        return a;
    }
    public Vec3d getCornerB() {
        return b;
    }
}
