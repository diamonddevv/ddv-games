package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.DDVGamesMod;

public class Setting {
    private final String simpleName;
    private double val;

    public Setting(double defaultVal, String simpleName) { // todo: maybe expand setting value types?
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
}
