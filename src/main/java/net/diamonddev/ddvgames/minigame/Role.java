package net.diamonddev.ddvgames.minigame;

public class Role {

    public static final Role EMPTY = new Role("");

    private final String name;

    public Role(String simpleName) {
        this.name = simpleName;
    }

    public String getName() {
        return name;
    }

    public static Role fromName(String name) {
        return new Role(name);
    }
}
