package resetting_a_screen;

import com.jhlabs.image.GaussianFilter;
import com.valkryst.VTerminal.component.VFrame;
import com.valkryst.VTerminal.image.SequentialOp;
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExampleF {
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
					panel.setCodePointAt(x, y, getRandomCodePoint());
					panel.setBackgroundAt(x, y, getRandomColor());
					panel.setForegroundAt(x, y, getRandomColor());
					panel.setSequentialImageOpAt(x, y, new SequentialOp(new GaussianFilter(3f)));
				}
			}

			Executors.newSingleThreadScheduledExecutor().schedule(() -> {
				panel.reset();
				SwingUtilities.invokeLater(panel::repaint);
			}, 2, TimeUnit.SECONDS);
		});
	}

	private static int getRandomCodePoint() {
		return ThreadLocalRandom.current().nextInt(33, 127);
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
