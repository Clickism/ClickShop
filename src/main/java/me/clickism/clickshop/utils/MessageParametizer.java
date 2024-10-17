package me.clickism.clickshop.utils;

import me.clickism.clickshop.data.Message;
import org.bukkit.entity.Player;

public class MessageParametizer extends Parameterizer<MessageParametizer> {

    protected Message message;

    public MessageParametizer(Message message) {
        super(message.toString());
        this.message = message;
    }

    public MessageParametizer() {
        super();
    }

    public MessageParametizer setMessage(Message message) {
        this.message = message;
        setString(message.toString());
        return this;
    }

    public HoverableMessageParametizer hoverable() {
        HoverableMessageParametizer hoverable = new HoverableMessageParametizer(message);
        return (HoverableMessageParametizer) hoverable.putAll(this);
    }

    public void send(Player player) {
        send(player, true);
    }

    public void sendSilently(Player player) {
        send(player, false);
    }

    protected void send(Player player, boolean sound) {
        message.getType().send(player, toString(), sound);
    }
}
