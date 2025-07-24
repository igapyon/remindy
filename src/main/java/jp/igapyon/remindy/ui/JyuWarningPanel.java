package jp.igapyon.remindy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class JyuWarningPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	// カスタマイズポイント
	private static final Color BG_COLOR = Color.BLACK;
	private static final Color ACCENT_COLOR = new Color(0xFF0055);
	private static final int LINE_THICKNESS = 6;
	private static final int GAP_LINE_TO_STRIPE = 8;
	private static final int STRIPE_BAND_HEIGHT = 36;
	private static final int STRIPE_UNIT_WIDTH = 36;
	private static final Font MAIN_FONT = new Font("MS Gothic", Font.BOLD, 28);

	private final String message;

	public JyuWarningPanel(String message) {
		this.message = message;
		setOpaque(true);
		setBackground(BG_COLOR);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		final int w = getWidth();
		final int h = getHeight();

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// 背景
			g2.setColor(BG_COLOR);
			g2.fillRect(0, 0, w, h);

			// ===== 上部帯 =====
			int y = 0;
			g2.setColor(ACCENT_COLOR);
			g2.fillRect(0, y, w, LINE_THICKNESS); // 上線
			y += LINE_THICKNESS + GAP_LINE_TO_STRIPE;
			drawStripeBand(g2, 0, y, w, STRIPE_BAND_HEIGHT); // ストライプ
			y += STRIPE_BAND_HEIGHT + GAP_LINE_TO_STRIPE;

			int contentTop = y;

			// ===== 下部帯 =====
			int bottomBandTop = h - (LINE_THICKNESS + GAP_LINE_TO_STRIPE + STRIPE_BAND_HEIGHT + GAP_LINE_TO_STRIPE);
			int contentBottom = bottomBandTop;

			drawStripeBand(g2, 0, bottomBandTop + GAP_LINE_TO_STRIPE, w, STRIPE_BAND_HEIGHT); // ストライプ
			g2.fillRect(0, h - LINE_THICKNESS, w, LINE_THICKNESS); // 下線

			// ===== テキスト描画 =====
			g2.setFont(MAIN_FONT);
			g2.setColor(ACCENT_COLOR);
			FontMetrics fm = g2.getFontMetrics();
			String[] lines = message.split("\\n");
			int lineHeight = fm.getHeight();
			int textBlockHeight = lineHeight * lines.length;

			int usableHeight = contentBottom - contentTop;
			int textStartY = contentTop + (usableHeight - textBlockHeight) / 2 + fm.getAscent();

			for (int i = 0; i < lines.length; i++) {
				int tw = fm.stringWidth(lines[i]);
				int textStartX = (w - tw) / 2;
				g2.drawString(lines[i], textStartX, textStartY + i * lineHeight);
			}
		} finally {
			g2.dispose();
		}
	}

	private void drawStripeBand(Graphics2D g2, int startX, int startY, int width, int height) {
		g2.setColor(ACCENT_COLOR);
		for (int x = startX - width; x < width * 2; x += STRIPE_UNIT_WIDTH * 2) {
			Polygon p = new Polygon();
			p.addPoint(x, startY + height); // 左上
			p.addPoint(x + STRIPE_UNIT_WIDTH, startY + height); // 右上
			p.addPoint(x + STRIPE_UNIT_WIDTH + height, startY); // 右下
			p.addPoint(x + height, startY); // 左下
			g2.fillPolygon(p);
		}
	}
}