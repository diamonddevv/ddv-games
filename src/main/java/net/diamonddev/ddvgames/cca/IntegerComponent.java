package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class IntegerComponent implements Component {

    private final String componentId;

    public IntegerComponent(String name) {
        this.componentId = name;
    }

    private int integer;



    @Override
    public void readFromNbt(NbtCompound tag) {
        setInteger(tag.getInt(componentId));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt(componentId, getInteger());
    }


    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }
}
