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
package jp.igapyon.remindy.command;

import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;

/**
 * 毎分のタイミングで現在時刻をコンソールに出力するコマンド。
 * <p>
 * 主にデバッグや動作確認の目的で使用されます。
 * </p>
 * 
 * <pre>
 * 🔔 Tick at: 12:34
 * </pre>
 * 
 * @author Toshiki Iga
 */
public class LogCommand implements MinuteCommand {
	/**
	 * 毎分実行され、現在時刻を標準出力に表示します。
	 *
	 * @param now 現在の時刻（HH:mm 形式などで使用可能）
	 */
	@Override
	public void execute(LocalTime now) {
		System.out.println("🔔 Tick at: " + now);
	}
}
