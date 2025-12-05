package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import br.com.pereiraeng.swing.Leaf;

public class ChromaticCircle extends Leaf {
	private static final long serialVersionUID = 1L;

	private static final int BORDER = 20;

	private static final int DIST = 50;

	public ChromaticCircle(int width) {
		super(Color.WHITE, width, width, true);
	}

	// ----------------------------------------------------------

	private transient int show = 0;

	private transient int shiftTone = 0;

	private transient ChordType chordType;

	public void setShow(int show) {
		this.show = show;
		repaint();
	}

	public void setShiftTone(int shiftTone) {
		this.shiftTone = shiftTone;
		repaint();
	}

	public void setChord(ChordType chordType) {
		this.chordType = chordType;
		repaint();
	}

	@Override
	protected void draw(Graphics2D g) {
		Dimension d = getSize();
		int width = Math.min(d.width, d.height);

		g.drawOval(BORDER, BORDER, width - 2 * BORDER, width - 2 * BORDER);
		g.drawOval(BORDER + DIST, BORDER + DIST, width - 2 * (BORDER + DIST), width - 2 * (BORDER + DIST));

		int xc = width / 2, yc = width / 2;
		int ro = (width - 2 * BORDER) / 2, ri = (width - 2 * (BORDER + DIST)) / 2, rm = (width - 2 * BORDER - DIST) / 2;
		for (int i = 0; i < 12; i++) {
			double phi = i * 0.52359877559829887307710723054658 + 0.26179938779914943653855361527329; // 30*i+15
			int x1 = (int) (xc + ro * Math.cos(phi)), y1 = (int) (yc + ro * Math.sin(phi)),
					x2 = (int) (xc + ri * Math.cos(phi)), y2 = (int) (yc + ri * Math.sin(phi));
			g.drawLine(x1, y1, x2, y2);

			phi -= 1.832595714594046055769875306913; // - 90 - 15
			int x3 = (int) (xc + rm * Math.cos(phi)) - 10, y3 = (int) (yc + rm * Math.sin(phi));

			switch (show) {
			case 0:
				// Solfege
				g.drawString(Solfege.getNoteAccString((i + shiftTone) % 12, true), x3, y3);
				break;
			case 1:
				// relações
				int[] r = Note.getRatio((i + shiftTone) % 12);
				g.drawString(String.format("%d:%d", r[0], r[1]), x3, y3);
				break;
			case 2:
				// intervalos
				g.drawString(Note.getInterval((i + shiftTone) % 12), x3, y3);
				break;
			}
		}

		if (chordType != null) {
			int rc = (int) (ri * .8f);
			for (int i = 0; i < chordType.getChordLength(); i++) {
				double phi = chordType.getShift(i) * 0.52359877559829887307710723054658
						- 1.5707963267948966192313216916398; // 30*i-90
				int x3 = (int) (xc + rc * Math.cos(phi)), y3 = (int) (yc + rc * Math.sin(phi));
				g.drawLine(xc, yc, x3, y3);
			}
		}
	}
}
