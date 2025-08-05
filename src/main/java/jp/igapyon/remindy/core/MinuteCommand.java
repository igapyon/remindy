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
package jp.igapyon.remindy.core;

import java.time.LocalTime;

/**
 * 毎分実行されるコマンドのインターフェースです。
 * <p>
 * {@link MinuteTicker} から毎分呼び出されることを想定しており、 実装クラスは
 * {@code execute(LocalTime now)} を通じて、 時刻に応じた処理を行うことができます。
 * </p>
 *
 * <p>
 * 例: 通知表示、ポップアップ、マウス移動など
 * </p>
 *
 * @author Toshiki Iga
 */
public interface MinuteCommand {
	/**
	 * 毎分呼び出されるメソッドです。
	 * 
	 * @param now 現在時刻（分単位での実行に相当する {@link LocalTime}）
	 */
	void execute(LocalTime now);
}
