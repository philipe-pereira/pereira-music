package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.util.ArrayList;

import br.com.pereiraeng.xml.XMLserializable;

/**
 * Classe dos objetos que representam um período de tempo de uma partitura
 * 
 * @author Philipe PEREIRA
 *
 */
public class Measure extends ArrayList<ArrayList<MusicalBar>> implements XMLserializable {
	private static final long serialVersionUID = 1L;

	private byte compassNum, compassDem;

	@Override
	public String getXML() {
		return String.format("<time>\r\n<beats>%d</beats>\r\n<beat-type>%d</beat-type>\r\n</time>\r\n", compassNum,
				compassDem);
	}
}
