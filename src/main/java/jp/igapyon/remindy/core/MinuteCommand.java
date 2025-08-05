package jp.igapyon.remindy.core;

import java.time.LocalTime;

public interface MinuteCommand {
	void execute(LocalTime now);
}
