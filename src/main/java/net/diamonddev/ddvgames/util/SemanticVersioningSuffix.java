package net.diamonddev.ddvgames.util;

public enum SemanticVersioningSuffix {

    NONE("none"),
    BETA("beta"),
    ALPHA("alpha"),
    SNAPSHOT("snapshot"),
    TEST("test");


    private final String key;
    SemanticVersioningSuffix(String stringifiedSuffixForTranslation) {
        this.key = stringifiedSuffixForTranslation;
    }

    public String getTranslationKey() {
        return "ddv.semanticVersioning.suffixes." + this.key;
    }
}
