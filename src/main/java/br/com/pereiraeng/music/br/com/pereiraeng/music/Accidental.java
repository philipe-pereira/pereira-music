package br.com.pereiraeng.music.br.com.pereiraeng.music;

public enum Accidental {
	DOUBLE_FLATS(0x1D12B, 'B'), FLAT(0x266D, 'b'), NATURAL(0x266E, ' '), SHARP(0x266F, '#'), DOUBLE_SHARP(0x1D12A, 'x');

	private int fullSymbol;
	private char simpleSymbol;

	private Accidental(int c, char simple) {
		this.fullSymbol = c;
		this.simpleSymbol = simple;
	}

	public int getSemitone() {
		return this.ordinal() - 2;
	}

	public static Accidental getAccidental(char c) {
		for (int i = 0; i < Accidental.values().length; i++) {
			Accidental ac = Accidental.values()[i];
			if (ac.simpleSymbol == c)
				return ac;
		}
		return null;
	}

	public String toSymbol() {
		return String.valueOf(Character.toChars(this.fullSymbol));
	}

	public String toSimpleSymbol() {
		return this.simpleSymbol == ' ' ? "" : String.format("%c", this.simpleSymbol);
	}

	public static Accidental get(int i) {
		return values()[i + 2];
	}

	public Accidental oppose() {
		switch (this) {
		case DOUBLE_FLATS:
			return DOUBLE_SHARP;
		case FLAT:
			return SHARP;
		case NATURAL:
			return NATURAL;
		case SHARP:
			return FLAT;
		case DOUBLE_SHARP:
			return DOUBLE_FLATS;
		default:
			return null;
		}
	}
}
