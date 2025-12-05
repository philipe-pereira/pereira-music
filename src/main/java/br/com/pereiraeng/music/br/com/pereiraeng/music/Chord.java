package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.util.TreeSet;

import br.com.pereiraeng.core.StringUtils;


/**
 * Classe dos objetos que representam
 * 
 * @author Philipe PEREIRA
 *
 */
public class Chord {

	private final Note[] notes;

	private final String notation;

	public Chord(Note root, ChordType chordType) {
		this(getNotes(root, chordType));
	}

	public Chord(Solfege root, Accidental ac, ChordType chordType, Note cut) {
		this(getNotes(root, ac, chordType, cut));
	}

	public Chord(Note[] notes) {
		this.notes = notes;
		this.notation = writeNotation();
	}

	public Note getRoot() {
		return this.notes[0];
	}

	public Note getInf() {
		Note out = getRoot();
		for (int i = 1; i < notes.length; i++)
			if (notes[i].compareTo(out) < 0)
				out = notes[i];
		return out;
	}

	public Note getSup() {
		Note out = getRoot();
		for (int i = 1; i < notes.length; i++)
			if (notes[i].compareTo(out) > 0)
				out = notes[i];
		return out;
	}

	public float getMean() {
		float out = 0f;
		for (int i = 0; i < notes.length; i++)
			out += notes[i].getMidi();
		return out / notes.length;
	}

	private String writeNotation() {
		String out = getRoot().getScientific();

		ChordType ct = ChordType.getChord(notes);
		if (ct == null)
			return out;
		else
			out += ct.getSymbol();
		boolean sym = ct == ChordType.SEV_DIM || ct == ChordType.AUG;

		// pega a menor nota
		Note minor = getInf();

		if (!sym) {
			// se não for simétrica...

			// calcula a distância (intervalo) de todas as notas para essa nota menor
			TreeSet<Integer> pos = new TreeSet<>();
			for (int i = 0; i < notes.length; i++)
				if (notes[i] != minor)
					pos.add(Note.getIntervalNumeric(notes[i].compareTo(minor)));

			if (pos.size() == 2)
				out = StringUtils.getUnicodeSuperscript(pos.last()) + StringUtils.getUnicodeSubscript(pos.first()) + out;
			else if (pos.size() == 3) {
				if (pos.contains(9)) {
					// 2,T,6 ou 3,T,6
					out = StringUtils.getUnicodeSuperscript(pos.first()) + (char) (0x2093) + out;
				} else {
					int last = pos.last();
					if (last == 7) // 3,5,7
						out = StringUtils.getUnicodeSuperscript(last) + out;
					else {
						int first = pos.first();
						if (first == 2) // 2,4,6
							out = StringUtils.getUnicodeSuperscript(first) + out;
						else {
							if (pos.contains(5)) // 3,5,6
								out = StringUtils.getUnicodeSuperscript(last) + StringUtils.getUnicodeSubscript(5) + out;
							else // 3,4,6
								out = StringUtils.getUnicodeSuperscript(4) + StringUtils.getUnicodeSubscript(first) + out;
						}
					}
				}
			}
		} else {
			// se for simétrica... não faz sentido calcular a distância entre as notas (pois
			// elas são todas iguais). Neste caso, calcula-se a distância (intervalo) para a
			// raiz
			int diff = Note.getIntervalNumeric(getRoot().compareTo(minor));
			if (diff != 1) {
				if (diff == 9)
					out = (char) (0x2093) + out;
				else
					out = StringUtils.getUnicodeSuperscript(diff) + out;
			}
		}

		return out;
	}

	public int getChordLength() {
		return this.notes.length;
	}

	public Note getChordNote(int pos) {
		return this.notes[pos];
	}

	public boolean contains(int semitones) {
		for (int i = 0; i < notes.length; i++)
			if (notes[i].getMidi() == semitones)
				return true;
		return false;
	}

	@Override
	public String toString() {
		return notation;
	}

	// -------------------------------------------------------

	public static Note[] getNotes(Note root, ChordType chordType) {
		Note[] out = new Note[chordType.getChordLength()];

		out[0] = new Note(root.getMidi());
		getNotes(out, chordType);

		return out;
	}

	public static Note[] getNotes(Solfege root, Accidental acRoot, ChordType chordType, Note cut) {
		Note[] out = new Note[chordType.getChordLength()];

		out[0] = new Note(cut.getOctave() - 1, root, acRoot);
		getNotes(out, chordType);

		for (int i = 0; i < out.length; i++)
			while (out[i].compareTo(cut) < 0)
				out[i].octave();

		return out;
	}

	private static void getNotes(Note[] notes, ChordType chordType) {
		for (int i = 1; i < notes.length; i++)
			notes[i] = new Note((byte) (notes[0].getMidi() + chordType.getShift(i)));
	}
}
