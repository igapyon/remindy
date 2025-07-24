package jp.igapyon.remindy.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class RemindScreenPopup2 {
	private static final int WIDTH = 640;
	private static final int HEIGHT = 320;
	private static final Color DARK_BG = Color.decode("#200010");
	private static final Color STRIPE_COLOR = Color.decode("#FF0055");
	private static final Font FONT_TITLE = new Font("MS Gothic", Font.BOLD, 48);
	private static final Font FONT_SUBTEXT = new Font("Meiryo UI", Font.PLAIN, 16);

	public static void show(String title, String subtext) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow();
			window.setSize(WIDTH, HEIGHT);
			window.setAlwaysOnTop(true);
			window.setLocationRelativeTo(null);

			JPanel root = new JPanel(new BorderLayout()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(DARK_BG);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};

			root.add(createStripePanel(), BorderLayout.NORTH);

			// 中央メッセージ
			JPanel center = new JPanel();
			center.setBackground(DARK_BG);
			JLabel label = new JLabel(title, SwingConstants.CENTER);
			label.setFont(FONT_TITLE);
			label.setForeground(STRIPE_COLOR);
			center.add(label);
			root.add(center, BorderLayout.CENTER);

			// 下部説明テキスト（白文字、改行対応）
			JTextArea subTextArea = new JTextArea(subtext);
			subTextArea.setFont(FONT_SUBTEXT);
			subTextArea.setForeground(Color.WHITE);
			subTextArea.setBackground(DARK_BG);
			subTextArea.setEditable(false);
			subTextArea.setFocusable(false);
			subTextArea.setOpaque(false);
			subTextArea.setLineWrap(true);
			subTextArea.setWrapStyleWord(true);
			subTextArea.setBorder(new EmptyBorder(10, 20, 10, 20));
			root.add(subTextArea, BorderLayout.SOUTH);

			root.add(createStripePanel(), BorderLayout.SOUTH);

			window.getContentPane().add(root);
			window.setVisible(true);

			new Timer(5000, e -> window.dispose()).start();
		});
	}

	private static JPanel createStripePanel() {
		return new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int panelHeight = getHeight();
				int stripeHeight = panelHeight - 8; // 上下に4pxの余白を確保
				int stripeY = 4;

				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(DARK_BG);
				g2d.fillRect(0, 0, getWidth(), panelHeight);

				// 斜めストライプ（右上がり ／）
				g2d.setColor(STRIPE_COLOR);
				int w = 30;
				for (int x = -getWidth(); x < getWidth() * 2; x += w * 2) {
					Polygon stripe = new Polygon();
					stripe.addPoint(x, stripeY);
					stripe.addPoint(x + w, stripeY);
					stripe.addPoint(x + w + stripeHeight, stripeY + stripeHeight);
					stripe.addPoint(x + stripeHeight, stripeY + stripeHeight);
					g2d.fillPolygon(stripe);
				}

				// 上下の赤線（4px）
				g2d.fillRect(0, 0, getWidth(), 4);
				g2d.fillRect(0, panelHeight - 4, getWidth(), 4);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 30); // 全体高さ
			}
		};
	}
}