package br.com.pereiraeng.music.br.com.pereiraeng.music;

public class Staff {

	private Clef clef;

	public Staff(Clef clef) {
		this.clef = clef;
	}

	public Clef getClef() {
		return clef;
	}

	@Override
	public String toString() {
		return this.clef.name();
	}
}
