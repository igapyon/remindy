package jp.igapyon.remindy.command;

import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.ui.JyuWarningPopup;

public class StartupCommand implements MinuteCommand {
	private final String version;
	private boolean executed = false;

	public StartupCommand(String version) {
		this.version = version;
	}

	@Override
	public void execute(LocalTime now) {
		if (!executed) {
			String message = "Remindy (" + version + ")\nリマインドと名言を毎分通知します";
			JyuWarningPopup.showPopup(message);
			System.err.println("【起動】" + message); // ← この行を追加
			executed = true;
		}
	}
}
