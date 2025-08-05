package jp.igapyon.remindy.command;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.time.LocalTime;
import java.util.Random;

import jp.igapyon.remindy.core.MinuteCommand;

public class PikoMouseCommand implements MinuteCommand {
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
