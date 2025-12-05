package br.com.pereiraeng.music.br.com.pereiraeng.music;

public enum ChordType {
	MAJOR("maior", "", 4, 7), MINOR("menor", "m", 3, 7), SEV_DOMINANT("de sétima dominante", "7", -2, 4, 7),
	SEV_DIM("de sétima diminuta", "7dm", -3, 3, 6), AUG("aumentada", "+", 4, 8), DIM("diminuta", "0", 3, 6),
	SEV_MAJOR("de sétima maior", "7M", -1, 4, 7), SEV_HALF_DIM("de sétima meio diminuto", "o7", -2, 3, 6),
	SEV_MINOR("de sétima menor", "7m", -2, 3, 7), SEV_MAJOR_MINOR("de sétima maior menor", "7Mm", -1, 3, 7),
	SEV_AUG("de sétima aumentada", "7+", -2, 4, 8), SEV_AUG_MAJOR("de sétima aumentada maior", "7M+", -1, 4, 8);

	private String name, symbol;

	private final int[] shift;

	private ChordType(String name, String symbol, int... shift) {
		this.name = name;
		this.symbol = symbol;
		this.shift = shift;
	}

	public int getChordLength() {
		return this.shift.length + 1;
	}

	public int getShift(int i) {
		if (i == 0)
			return 0;
		else
			return shift[i - 1];
	}

	@Override
	public String toString() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public static ChordType getChord(Note[] notes) {
		for (int i = 0; i < ChordType.values().length; i++) {
			ChordType ct = ChordType.values()[i];
			if (isChordType(notes, ct))
				return ct;
		}
		return null;
	}

	private static boolean isChordType(Note[] notes, ChordType ct) {
		if (ct.getChordLength() == notes.length) {
			Note root = notes[0];
			for (int j = 1; j < notes.length; j++) {
				Note note = notes[j];
				int diff = note.compareTo(root);
				if (!ct.contains(diff))
					return false;
			}
			return true;
		}
		return false;
	}

	public boolean contains(int diff) {
		if (diff == 0)
			return true;
		if (diff < 0)
			diff += 12;
		for (int i = 0; i < shift.length; i++) {
			int s = shift[i];
			if (s < 0)
				s += 12;
			if (diff == s)
				return true;
		}
		return false;
	}

	public boolean belongs(Note note, Solfege root, Accidental acRoot) {
		return contains((note.getMidi() - root.getTone() - acRoot.getSemitone()) % 12);
	}
}
