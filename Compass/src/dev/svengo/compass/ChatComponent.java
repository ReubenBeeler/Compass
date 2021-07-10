package dev.svengo.compass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatComponent {

	private TextComponent textComponent = new TextComponent();
	
	public ChatComponent (String text, ChatColor color) {
		this.textComponent.setText(text);
		this.textComponent.setColor(color);
	}
	
	public ChatComponent (String text, ChatColor color, boolean underlined) {
		this.textComponent.setText(text);
		this.textComponent.setColor(color);
		this.textComponent.setUnderlined(underlined);
	}
	
	public TextComponent getTextComponent() {
		return textComponent;
	}
}
