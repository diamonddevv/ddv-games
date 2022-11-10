package net.diamonddev.ddvgames.minigame.setting;


import net.minecraft.text.Text;

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

}
