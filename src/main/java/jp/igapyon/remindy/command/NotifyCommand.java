package jp.igapyon.remindy.command;

import java.awt.TrayIcon;
import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;

public class NotifyCommand implements MinuteCommand {
	private final TrayIcon trayIcon;
	private final MessageBuilder messageBuilder;

	public NotifyCommand(TrayIcon trayIcon, MessageBuilder messageBuilder) {
		this.trayIcon = trayIcon;
		this.messageBuilder = messageBuilder;
	}

	@Override
	public void execute(LocalTime now) {
		String message = messageBuilder.build(now);
		if (trayIcon != null && message != null && !message.isEmpty()) {
			trayIcon.displayMessage("Remindy", message, TrayIcon.MessageType.INFO);
			System.err.println("【通知】" + now + "\n" + message);
		}
	}
}
