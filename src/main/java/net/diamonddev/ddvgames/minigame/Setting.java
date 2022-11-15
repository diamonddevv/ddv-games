package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.DDVGamesMod;

public class Setting {
    private final String simpleName;
    private double val;

    public Setting(double defaultVal, String simpleName) {
        this.val = defaultVal;
        this.simpleName = simpleName;
    }

    public double getValue() {
        return this.val;
    }
    public void setValue(double v) {
        this.val = v;
    }

    public String getSimpleName() {
        return simpleName;
    }


    public static boolean parseAsBoolean(String simpleName) {
        return DDVGamesMod.gameManager.getSetting(simpleName) > 0.0;
    }
    public static double parseAsDouble(String simpleName) {
        return DDVGamesMod.gameManager.getSetting(simpleName);
    }
    public static int parseAsInt(String simpleName) {
        return (int) parseAsDouble(simpleName);
    }


    @Deprecated // I may come back to this.
    public static abstract class Key<T> {

        public double parse(T val) {
            if (val instanceof Boolean bl) {
                return bl ? 1.0 : 0.0;
            }
            return (double) val;
        }
    }

    public static class DoubleKey extends Key<Double> { }
    public static class IntKey extends Key<Integer> {}
    public static class BoolKey extends Key<Boolean> {}
}
