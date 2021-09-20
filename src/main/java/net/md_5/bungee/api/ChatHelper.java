package net.md_5.bungee.api;

import net.md_5.bungee.api.chat.TextComponent;

public class ChatHelper {
    public static TextComponent getTextComponent(String text) {
        return new TextComponent(text);
    }
}