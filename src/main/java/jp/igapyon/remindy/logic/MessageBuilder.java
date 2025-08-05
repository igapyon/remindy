package jp.igapyon.remindy.logic;

import java.time.LocalTime;

public class MessageBuilder {
	public String build(LocalTime now) {
		return "現在時刻: " + now.toString(); // 仮のメッセージ
	}
}
