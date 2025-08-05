package jp.igapyon.remindy.command;

import java.time.LocalTime;

import javax.swing.SwingUtilities;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.ui.JyuWarningPopup;

public class StartupCommand implements MinuteCommand {
	private final String version;

	public StartupCommand(String version) {
		this.version = version;
	}

	@Override
	public void execute(LocalTime now) {
		String message = "Remindy (" + version + ")" + "\nリマインドと名言を毎分通知します";
		System.err.println("🔔 StartupCommand 実行: " + now + "\n" + message);
		SwingUtilities.invokeLater(() -> {
			JyuWarningPopup.showPopup(message);
		});
	}
}
