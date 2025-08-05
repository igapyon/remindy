package jp.igapyon.remindy.command;

import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;

public class LogCommand implements MinuteCommand {
	@Override
	public void execute(LocalTime now) {
		System.out.println("🔔 Tick at: " + now);
	}
}
