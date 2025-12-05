package br.com.pereiraeng.music.br.com.pereiraeng.music;

import br.com.pereiraeng.xml.XMLserializable;

public enum Clef implements XMLserializable {
	TREBLE(Solfege.SOL, (byte) 2), ALTO(Solfege.DO, (byte) 3), BASS(Solfege.FA, (byte) 4);

	private Solfege note;
	private byte line;

	private Clef(Solfege note, byte line) {
		this.note = note;
		this.line = line;
	}

	public Solfege getNote() {
		return note;
	}

	public byte getLine() {
		return line;
	}

	@Override
	public String getXML() {
		return String.format("<sign>%c</sign>\r\n<line>%d</line>\r\n", note.getLetter(), line);
	}
}
