package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import br.com.pereiraeng.swing.Leaf;

/**
 * Classe do objeto gráfico que representa uma pauta musical, porém somente para
 * a representação das notas, sem levar em conta sua posição ou duração
 * 
 * @author Philipe PEREIRA
 *
 */
public class StaticStaff extends Leaf {
	private static final long serialVersionUID = 1L;

	private static final float BORDER_H = 0.1f, BORDER_V = 0.3f;

	private Clef clef = Clef.TREBLE;

	private Chord chord;

	public StaticStaff(int width, int height) {
		super(Color.WHITE, width, height, true);
		Object[] fs = { StaticStaff.class.getResourceAsStream("Bravura.otf"), new File("files/font/Bravura.otf") };
		for (int i = 0; i < fs.length; i++) {
			Object f = fs[i];
			if (f instanceof File) {
				try {
					// http://www.smufl.org/fonts/
					// http://www.unicode.org/charts/PDF/U1D100.pdf
					this.fm = Font.createFont(Font.TRUETYPE_FONT, (File) f);
					break;
				} catch (FontFormatException | IOException e) {
					System.err.println("Arquivo não encontrado: " + f);
				}
			} else {
				try {
					// http://www.smufl.org/fonts/
					// http://www.unicode.org/charts/PDF/U1D100.pdf
					this.fm = Font.createFont(Font.TRUETYPE_FONT, (InputStream) f);
					break;
				} catch (FontFormatException | IOException e) {
					System.err.println("Arquivo não encontrado: " + f);
				}
			}
		}
	}

	public void setClef(Clef clef) {
		this.clef = clef;
	}

	public Chord getChord() {
		return chord;
	}

	public void setChord(Chord chord) {
		this.chord = chord;
		if (chord != null) {
			float f = chord.getMean();
			if (f > 63) // Eb4
				setClef(Clef.TREBLE);
			else if (f < 58f) // Bb3
				setClef(Clef.BASS);
			else
				setClef(Clef.ALTO);
		}
		repaint();
	}

	// ----------------------------------------------------------------

	private transient int y0, step, xp;

	private Font fm;

	@Override
	protected void draw(Graphics2D g) {
		Dimension d = getSize();

		int x0 = (int) (BORDER_H * d.getWidth());
		int xf = (int) (d.getWidth() * (1 - BORDER_H));
		this.xp = (2 * xf + x0) / 3;

		this.y0 = (int) (BORDER_V * d.getHeight());
		float h = (int) (d.getHeight() * (1 - 2f * BORDER_V));
		this.step = (int) (h / 5);

		for (int i = 0; i < 5; i++) {
			// 5 linhas da pauta
			int y = y0 + i * step;
			g.drawLine(x0, y0 + i * step, xf, y);

			if (i == 5 - clef.getLine()) {
				if (fm != null) {
					Font f = g.getFont();
					g.setFont(fm.deriveFont(.75f * h));

					switch (clef) {
					case BASS:
						// 1D122 = D834|DD22
						g.drawString("\uD834\uDD22", x0, y);// Fclef
						break;
					case ALTO:
						// 1D121 = D834|DD21
						g.drawString("\uD834\uDD21", x0, y);// Cclef
						break;
					case TREBLE:
						// 1D11E = D834|DD1E
						g.drawString("\uD834\uDD1E", x0, y);// Gclef
						break;
					}
					g.setFont(f);
				} // TODO roubar os pontos da fonte bravura
			}
		}

		if (chord != null) {
			int min = -9, max = 0;
			for (int i = 0; i < chord.getChordLength(); i++) {
				Note note = chord.getChordNote(i);
				int octave = note.getOctave();
				Object[] sa = note.getNoteAcc();
				Solfege s = (Solfege) sa[0];
				Accidental a = (Accidental) sa[1];

				int pos = 0;
				switch (clef) {
				case BASS:
					pos = (octave - 3) * 7 + s.ordinal() - 5; // o zero fica em G3
					break;
				case ALTO:
					pos = (octave - 4) * 7 + s.ordinal() - 4; // o zero fica em F4
					break;
				case TREBLE:
					pos = (octave - 5) * 7 + s.ordinal() - 3; // o zero fica em E5
					break;
				}

				int y = y0 - pos * step / 2;
				min = Math.min(min, pos);
				max = Math.max(max, pos);
				g.drawOval(xp, y - step / 2, step * 5 / 4, step);
				g.drawString(a.toSimpleSymbol(), xp - 15, y + 5);
			}

			// linhas extras (se necessário)
			if (min < -9) {
				for (int i = -9; i > min; i -= 2) {
					int y = y0 - (i - 1) * step / 2;
					g.drawLine(xp - 15, y, xp + step * 5 / 4 + 15, y);
				}
			}

			if (max > 2) {
				for (int i = 2; i <= max; i += 2) {
					int y = y0 - i * step / 2;
					g.drawLine(xp - 15, y, xp + step * 5 / 4 + 15, y);
				}
			}
		}
	}
}
