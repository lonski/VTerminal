package resetting_a_screen;

import com.valkryst.VTerminal.component.VFrame;
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExampleC {
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(VTerminalLookAndFeel.getInstance(24));
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			final var frame = new VFrame(40, 20);
			frame.setVisible(true);
			frame.pack();
			frame.setLocationRelativeTo(null);

			final var panel = frame.getContentPane();
			for (int y = 0 ; y < panel.getHeightInTiles() ; y++) {
				for (int x = 0 ; x < panel.getWidthInTiles() ; x++) {
					panel.setBackgroundAt(x, y, getRandomColor());
				}
			}

			Executors.newSingleThreadScheduledExecutor().schedule(() -> {
				panel.resetBackgroundColors();
				SwingUtilities.invokeLater(panel::repaint);
			}, 2, TimeUnit.SECONDS);
		});
	}

	private static Color getRandomColor() {
		switch (ThreadLocalRandom.current().nextInt(0, 6)) {
			case 0: { return Color.MAGENTA; }
			case 1: { return Color.GREEN; }
			case 2: { return Color.YELLOW; }
			case 3: { return Color.BLUE; }
			case 4: { return Color.RED; }
			case 5: { return Color.ORANGE; }
			default: { return Color.WHITE; }
		}
	}
}
