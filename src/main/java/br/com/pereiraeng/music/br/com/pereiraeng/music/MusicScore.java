package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.math.Scale2DiOff;
import br.com.pereiraeng.swing.interfaces.DesG;
import br.com.pereiraeng.xml.XMLserializable;

/**
 * Classe dos objetos que representam uma partitura
 * 
 * @author Philipe PEREIRA
 *
 */
public class MusicScore extends ArrayList<Measure> implements DesG, XMLserializable {
	private static final long serialVersionUID = 1L;

	private int numObra = -1;

	private String name;

	private int numMov = -1;

	private String titleMov;

	private String composer, lyricist;

	/**
	 * divisão vertical
	 */
	private List<Part> parts;

	// ============================= DRAWER =============================

	@Override
	public void setDrawable(boolean drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDrawable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void drawObject(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGrade(Scale2DiOff grade) {
		// TODO Auto-generated method stub

	}

	// ============================= XML =============================

	@Override
	public String getXML() {
		String out = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!DOCTYPE score-partwise PUBLIC\r\n\"-//Recordare//DTD MusicXML 3.0 Partwise//EN\"\r\n\"http://www.musicxml.org/dtds/partwise.dtd\">\r\n<score-partwise version=\"3.0\">\r\n";

		// obra e seu número
		if (numObra > 0 || name != null) {
			out += "<work>\r\n";
			if (numObra > 0)
				out += String.format("<work-number>Op. %d</work-number>\r\n", numObra);
			if (name != null)
				out += String.format("<work-title>%s</work-title>\r\n", name);
			out += "</work>\r\n";
		}

		// movimento e seu número
		if (numMov > 0)
			out += String.format("<movement-number>%d</movement-number>\r\n", numMov);
		if (titleMov != null)
			out += String.format("<movement-title>%s</movement-title>\r\n", titleMov);

		// compositor e lirista
		if (composer != null || lyricist != null) {
			out += "<identification>\r\n";
			if (composer != null)
				out += String.format("<creator type=\"composer\">%s</creator>\r\n");
			if (lyricist != null)
				out += String.format("<creator type=\"lyricist\">%s</creator>\r\n");
			out += "</identification>\r\n";
		}

		// listar partes
		out += "<part-list>\r\n";
		for (int i = 0; i < parts.size(); i++) {
			Part p = parts.get(i);

			// número e nome do instrumento
			out += String.format("<score-part id=\"P%d\">\r\n<part-name>%s</part-name>\r\n", i + 1, p);

			// nome abreviado do instrumento
			String abb = p.getNameAbbr();
			if (abb != null)
				out += String.format("<part-abbreviation>%s</part-abbreviation>\r\n", abb);

			out += "</score-part>\r\n";
		}
		out += "</part-list>\r\n";

		int div = getDivision();

		// detalhar partes
		for (int p = 0; p < parts.size(); p++) {
			out += String.format("<part id=\"P%d\">\r\n", p + 1);

			Part part = parts.get(p);

			for (int m = 0; m < this.size(); m++) {
				Measure measure = this.get(m);
				out += String.format("<measure number=\"%d\">", m + 1);

				if (m == 0) {
					// se for o primeiro
					out += String.format("<attributes>\r\n<divisions>%d</divisions>\r\n%s</attributes>\r\n", div,
							part.getXML());
				} else {
					// TODO se houver mudança
				}

				for (int s = 0; s < part.size(); s++) {
					MusicalBar bar = measure.get(p).get(s);

					String st = "";
					if (part.size() > 1)
						st = String.format("<staff>%d</staff>\r\n", s + 1);

					for (int n = 0; n < bar.size(); n++) {
						MusicalNote mn = bar.get(n);
						out += String.format("<note>\r\n%s<duration>%d</duration>\r\n%s\r\n%s</note>\r\n", mn.getXML(),
								mn.getDuration(), StringUtils.repeatString("<dot/>", mn.getDots()), st);
					}
				}
				out += "</measure>";
			}
			out += "</part>\r\n";
		}

		return out + "</score-partwise>";
	}

	/**
	 * 
	 * @return
	 */
	private int getDivision() {
		byte out = 0;
		for (int m = 0; m < this.size(); m++) {
			Measure me = this.get(m);
			for (int b1 = 0; b1 < me.size(); b1++) {
				ArrayList<MusicalBar> bs = me.get(b1);
				for (int b2 = 0; b2 < bs.size(); b2++) {
					MusicalBar bar = bs.get(b2);
					for (int n = 0; n < bar.size(); n++) {
						MusicalNote mn = bar.get(n);
						byte d = mn.getDuration();
						if (d > out)
							out = d;
					}
				}
			}
		}
		if (out < 3)
			return 1;
		else
			return (int) Math.pow(2, out - 2);
	}
}
