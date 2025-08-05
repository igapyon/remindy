package jp.igapyon.remindy.command;

import java.awt.TrayIcon;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;

public class NotifyCommand implements MinuteCommand {
	private final TrayIcon trayIcon;
	private final MessageBuilder messageBuilder;
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	public NotifyCommand(TrayIcon trayIcon, MessageBuilder messageBuilder) {
		this.trayIcon = trayIcon;
		this.messageBuilder = messageBuilder;
	}

	@Override
	public void execute(LocalTime now) {
		String message = messageBuilder.build(now);
		if (trayIcon != null && message != null && !message.isEmpty()) {
			trayIcon.displayMessage("Remindy", message, TrayIcon.MessageType.INFO);
			System.err.println("【通知】" + now.format(TIME_FORMATTER) + "\n" + message);
		}
	}
}
