package me.clickism.clickshop.utils;

import me.clickism.clickshop.data.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public class HoverableMessageParametizer extends MessageParametizer {

    private static final String HOVER_FORMAT = "[%s]";

    private final Map<String, BaseComponent[]> hoverParams = new HashMap<>();

    public HoverableMessageParametizer(Message message) {
        super(message);
    }

    public HoverableMessageParametizer putHover(String key, BaseComponent[] components) {
        this.hoverParams.put(key, components);
        return this;
    }

    public HoverableMessageParametizer putAllHover(Map<String, BaseComponent[]> hoverParams) {
        this.hoverParams.putAll(hoverParams);
        return this;
    }

    @Override
    public HoverableMessageParametizer setMessage(Message message) {
        super.setMessage(message);
        return this;
    }

    @Override
    protected void send(Player player, boolean sound) {
        message.getType().sendSpigot(player, toComponents(), sound);
    }

    public BaseComponent[] toComponents() {
        List<BaseComponent> parameterizedComponents = new LinkedList<>();
        for (BaseComponent component : TextComponent.fromLegacyText(toString())) {
            insertComponent(parameterizedComponents, component);
        }
        return parameterizedComponents.toArray(new BaseComponent[0]);
    }

    private void insertComponent(List<BaseComponent> list, BaseComponent component) {
        String text = component.toLegacyText();
        for (Map.Entry<String, BaseComponent[]> entry : hoverParams.entrySet()) {
            String placeholder = String.format(HOVER_FORMAT, entry.getKey());
            if (text.contains(placeholder)) {
                replaceComponent(list, text, placeholder, entry.getValue());
                return;
            }
        }
        list.add(component);
    }

    private void replaceComponent(List<BaseComponent> list, String text, String placeholder, BaseComponent[] components) {
        String[] parts = text.split(Pattern.quote(placeholder));
        for (int i = 0; i < parts.length; i++) {
            list.addAll(Arrays.asList(TextComponent.fromLegacyText(parts[i])));
            if (i == 0 || i < parts.length - 1) {
                list.addAll(Arrays.asList(components));
            }
        }
    }
}
