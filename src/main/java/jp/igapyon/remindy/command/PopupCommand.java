package jp.igapyon.remindy.command;

import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;
import jp.igapyon.remindy.ui.JyuWarningPopup;

public class PopupCommand implements MinuteCommand {
	private final MessageBuilder messageBuilder;

	public PopupCommand(MessageBuilder messageBuilder) {
		this.messageBuilder = messageBuilder;
	}

	@Override
	public void execute(LocalTime now) {
		String message = messageBuilder.build(now);
		if (message != null && message.contains("🔔時間🔔")) {
			// 🔔 を含むメッセージだけポップアップ表示
			String[] lines = message.split("\\R");
			String popupMessage = String.join("\n", limit(lines, 3)); // 最初の3行だけ
			JyuWarningPopup.showPopup(popupMessage);
		}
	}

	private String[] limit(String[] lines, int max) {
		int limit = Math.min(lines.length, max);
		String[] result = new String[limit];
		System.arraycopy(lines, 0, result, 0, limit);
		return result;
	}
}
