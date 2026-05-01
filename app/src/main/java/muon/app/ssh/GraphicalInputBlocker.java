/**
 *
 */
package muon.app.ssh;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Friendly modal "connecting" indicator shown while an SSH session is being
 * established. Replaces the previous empty 400x300 placeholder dialog.
 */
public class GraphicalInputBlocker extends JDialog implements InputBlocker {
	private static final long serialVersionUID = 1L;

	private final JFrame window;
	private final JLabel statusLabel;

	public GraphicalInputBlocker(JFrame window) {
		super(window);
		this.window = window;

		setUndecorated(true);
		setModal(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);

		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(Color.WHITE);
		root.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(0xC8, 0xC8, 0xC8), 1),
				BorderFactory.createEmptyBorder(22, 28, 22, 28)));

		JLabel title = new JLabel("正在连接 SSH 服务器", SwingConstants.LEFT);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
		title.setForeground(new Color(0x22, 0x22, 0x22));

		statusLabel = new JLabel("正在建立安全连接，请稍候…");
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 12f));
		statusLabel.setForeground(new Color(0x55, 0x55, 0x55));
		statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 12, 0));

		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setBorderPainted(false);
		bar.setPreferredSize(new Dimension(320, 6));

		JPanel center = new JPanel();
		center.setOpaque(false);
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		title.setAlignmentX(LEFT_ALIGNMENT);
		statusLabel.setAlignmentX(LEFT_ALIGNMENT);
		bar.setAlignmentX(LEFT_ALIGNMENT);
		center.add(title);
		center.add(statusLabel);
		center.add(bar);
		center.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel hint = new JLabel("OpenSFTP");
		hint.setFont(hint.getFont().deriveFont(Font.PLAIN, 11f));
		hint.setForeground(new Color(0x99, 0x99, 0x99));
		hint.setAlignmentX(LEFT_ALIGNMENT);
		center.add(hint);

		root.add(center, BorderLayout.CENTER);

		// Allow drag to move (since undecorated)
		final java.awt.Point[] drag = new java.awt.Point[1];
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { drag[0] = e.getPoint(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				if (drag[0] != null) {
					java.awt.Point p = e.getLocationOnScreen();
					setLocation(p.x - drag[0].x, p.y - drag[0].y);
				}
			}
		};
		root.addMouseListener(ma);
		root.addMouseMotionListener(ma);
		root.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

		setContentPane(root);
		pack();
	}

	@Override
	public void blockInput() {
		SwingUtilities.invokeLater(() -> {
			statusLabel.setText("正在建立安全连接，请稍候…");
			pack();
			setLocationRelativeTo(window);
			setVisible(true);
		});
	}

	@Override
	public void unblockInput() {
		SwingUtilities.invokeLater(() -> setVisible(false));
	}
}
