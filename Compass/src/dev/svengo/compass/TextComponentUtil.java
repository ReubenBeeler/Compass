package dev.svengo.compass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public interface TextComponentUtil {
	
	public static TextComponent getTextComponent(String text, ChatColor color) {
		final TextComponent tc = new TextComponent();
		tc.setText(text);
		tc.setColor(color);
		return tc;
	}
	
	public static TextComponent getTextComponent(String text, ChatColor color, boolean underlined) {
		final TextComponent tc = getTextComponent(text, color);
		tc.setUnderlined(underlined);
		return tc;
	}
}
