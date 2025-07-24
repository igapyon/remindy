package jp.igapyon.remindy.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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

			root.add(createStripePanel(), BorderLayout.SOUTH);

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
				g.setColor(DARK_BG);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(STRIPE_COLOR);
				int w = 30;
				int h = getHeight();
				for (int x = -getWidth(); x < getWidth() * 2; x += w * 2) {
					Polygon p = new Polygon();
					p.addPoint(x, 0);
					p.addPoint(x + w, 0);
					p.addPoint(x + w + h, h);
					p.addPoint(x + h, h);
					g.fillPolygon(p);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 30);
			}
		};
	}
}