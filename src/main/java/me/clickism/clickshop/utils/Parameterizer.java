package me.clickism.clickshop.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Parameterizer<T extends Parameterizer<T>> {

    private static final String FORMAT = "{%s}";

    private boolean colorize = true;
    private String string = "";
    protected final Map<String, Object> params = new HashMap<>();

    public Parameterizer(String string) {
        this.string = string;
    }

    public Parameterizer() {
    }

    @SuppressWarnings("unchecked")
    public T setString(String string) {
        this.string = string;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(String key, @NotNull Object value) {
        this.params.put(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T putAll(Parameterizer<? extends Parameterizer<T>> parameterizer) {
        this.params.putAll(parameterizer.params);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setColorizeParameters(boolean colorize) {
        this.colorize = colorize;
        return (T) this;
    }

    @Override
    public String toString() {
        String result = Utils.colorize(string);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = String.format(FORMAT, entry.getKey());
            result = result.replace(placeholder, entry.getValue().toString());
        }
        if (colorize) result = Utils.colorize(result);
        return result;
    }
}
