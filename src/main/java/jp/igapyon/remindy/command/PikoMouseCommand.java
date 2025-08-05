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

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.time.LocalTime;
import java.util.Random;

import jp.igapyon.remindy.core.MinuteCommand;

/**
 * 毎分、マウスポインタをわずかに動かすコマンドです。
 * <p>
 * 操作対象がアイドル状態になることを防ぐ目的で、マウスポインタをランダムな方向に1ピクセル動かします。
 * </p>
 * 実行例:
 * 
 * <pre>
 * マウス座標 (500, 300) → (501, 300)
 * </pre>
 * 
 * この処理は {@link Robot} クラスを使用しており、例外が発生した場合は標準エラー出力に記録します。
 * 
 * @author Toshiki Iga
 */
public class PikoMouseCommand implements MinuteCommand {
	/**
	 * マウスポインタを現在の位置からランダムに 1 ピクセルだけ移動させます。
	 *
	 * @param now 現在の時刻（使用しませんが、インターフェース仕様上必須）
	 */
	@Override
	public void execute(LocalTime now) {
		try {
			Robot robot = new Robot();
			Point loc = MouseInfo.getPointerInfo().getLocation();
			int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
			int[] move = directions[new Random().nextInt(directions.length)];
			robot.mouseMove(loc.x + move[0], loc.y + move[1]);
		} catch (Exception e) {
			System.err.println("マウス移動に失敗: " + e.getMessage());
		}
	}
}
