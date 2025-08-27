/*
 * Copyright 2025 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.remindy.logic;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.remindy.vo.Reminder;

/**
 * {@code MessageBuilder} は、現在時刻に基づいて通知メッセージを構築するユーティリティクラスです。
 *
 * <p>
 * メッセージは以下の構成要素を含みます:
 * </p>
 * <ul>
 * <li>🔔 現在時刻と一致するリマインダー</li>
 * <li>🗓 現在時刻以降の予定（分または時間後）</li>
 * <li>💡 順番に表示される格言</li>
 * </ul>
 *
 * <p>
 * 各メッセージは最大24文字までに切り詰められます。
 * </p>
 */
public class MessageBuilder {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static final int MAX_MESSAGE_LENGTH = 24;

	private final List<Reminder> reminders;
	private final List<String> proverbs;
	private int proverbIndex = 0;

	/**
	 * 指定されたリマインダーと格言リストで {@code MessageBuilder} を初期化します。
	 *
	 * @param reminders リマインダーのリスト（null の場合は空リストとして扱われます）
	 * @param proverbs  格言のリスト（null の場合は空リストとして扱われます）
	 */
	public MessageBuilder(List<Reminder> reminders, List<String> proverbs) {
		this.reminders = reminders != null ? reminders : Collections.emptyList();
		this.proverbs = proverbs != null ? proverbs : Collections.emptyList();
	}

	/**
	 * 指定された現在時刻に基づいて、通知メッセージを構築します。
	 *
	 * @param now 現在時刻
	 * @return 構築された通知メッセージ（複数行、改行区切り）
	 */
	public String build(LocalTime now) {
		String nowStr = now.format(TIME_FORMATTER);
		List<String> lines = new ArrayList<>();

		// 🔔 現在時刻に一致するリマインダー
		for (Reminder r : reminders) {
			if (nowStr.equals(r.time)) {
				lines.add(truncate("🔔時間🔔 " + r.message));
			}
		}

		// 🗓 これからの予定
		for (Reminder r : reminders) {
			LocalTime rTime = LocalTime.parse(r.time, TIME_FORMATTER);
			if (rTime.isAfter(now)) {
				long minutes = Math.round(Duration.between(now, rTime).getSeconds() / 60.0);
				String future;
				if (minutes > 60) {
					double hours = Math.floor(minutes / 6.0) / 10.0;
					future = truncate(String.format("%s (%.1f時間後) ", r.time, hours) + r.message);
				} else {
					future = truncate(String.format("%s (%d分後) ", r.time, minutes) + r.message);
				}
				lines.add("🗓 " + future);
			}
		}

		// 💡 格言（順送り）
		if (!proverbs.isEmpty()) {
			lines.add("💡 " + proverbs.get(proverbIndex));
			proverbIndex = (proverbIndex + 1) % proverbs.size();
		}

		return String.join("\n", lines);
	}

	/**
	 * 現在時刻に一致する 🔔 リマインダーだけを返します（副作用なし）。
	 *
	 * @param now 現在時刻
	 * @return 「🔔時間🔔 …」の行を要素とするリスト（0件の場合あり）
	 */
	public List<String> buildNowOnly(LocalTime now) {
		String nowStr = now.format(TIME_FORMATTER);
		List<String> lines = new ArrayList<>();
		for (Reminder r : reminders) {
			if (nowStr.equals(r.time)) {
				lines.add(truncate("🔔時間🔔 " + r.message));
			}
		}
		return lines;
	}

	/**
	 * 指定された文字列を最大長までに切り詰めます。
	 *
	 * @param msg 対象の文字列
	 * @return 最大長に切り詰めた文字列
	 */
	private String truncate(String msg) {
		if (msg == null)
			return "";
		if (msg.length() <= MAX_MESSAGE_LENGTH)
			return msg;
		return msg.substring(0, MAX_MESSAGE_LENGTH);
	}
}
