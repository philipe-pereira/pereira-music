package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.util.ArrayList;

import br.com.pereiraeng.xml.XMLserializable;

public class Part extends ArrayList<Staff> implements XMLserializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private String nameAbbr;

	/**
	 * Key signature. No flats or sharps, so the fifths element is 0. If we were in
	 * the key of D major with 2 sharps, fifths would be set to 2. If we were in the
	 * key of F major with 1 flat, fifths would be set to -1. The name “fifths”
	 * comes from the representation of a key signature along the circle of fifths.
	 * It lets us represent standard key signatures with one element, instead of
	 * separate elements for sharps and flats.
	 */
	private byte keySignature;

	public Part(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	public String getNameAbbr() {
		return nameAbbr;
	}

	public byte getKeySignature() {
		return keySignature;
	}

	public void setKeySignature(byte keySignature) {
		this.keySignature = keySignature;
	}

	@Override
	public String getXML() {
		String out = String.format("<key>\r\n<fifths>%d</fifths>\r\n</key>\r\n", keySignature);

		if (this.size() > 1)
			out += String.format("<staves>%d</staves>\r\n", this.size());

		for (int i = 0; i < this.size(); i++) {
			String n = "";
			if (this.size() > 1)
				n = String.format(" number=\"%d\"", i + 1);
			out += String.format("<clef%s>\r\n%s</clef>\r\n", n, this.get(i).getClef().getXML());
		}
		return out;
	}
}
