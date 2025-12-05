package br.com.pereiraeng.music.br.com.pereiraeng.music;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.xml.XMLserializable;

/**
 * Classe do objeto que representa uma {@link Note nota musical}, indicando a
 * sua altura
 * 
 * @author Philipe PEREIRA
 *
 */
public class Note implements Comparable<Note>, XMLserializable {

	public static final Note A4 = new Note((byte) 69);

	public static final Note MIDDLE_C = new Note((byte) 60);

	private byte midi;

	public Note(byte midi) {
		this.midi = midi;
	}

	/**
	 * 
	 * @param octaveScientific |[-2;10]|
	 *                         <ol start="-2">
	 *                         <li>Octocontra;</i>
	 *                         <li>Subsubcontra;</i>
	 *                         <li>Subcontra;</i>
	 *                         <li>Contra;</i>
	 *                         <li>Great;</i>
	 *                         <li>Small;</i>
	 *                         <li>One-lined;</i>
	 *                         <li>Two-lined;</i>
	 *                         <li>Three-lined;</i>
	 *                         <li>Four-lined;</i>
	 *                         <li>Five-lined;</i>
	 *                         <li>Six-lined;</i>
	 *                         <li>Seven-lined.</i>
	 *                         </ol>
	 * @param tone
	 *                         <ol start="0">
	 *                         <li>C;</i>
	 *                         <li>C# ou Db;</i>
	 *                         <li>D;</i>
	 *                         <li>D# ou Eb;</i>
	 *                         <li>E;</i>
	 *                         <li>F;</i>
	 *                         <li>F# ou Gb;</i>
	 *                         <li>G;</i>
	 *                         <li>G# ou Ab;</i>
	 *                         <li>A;</i>
	 *                         <li>A# ou Bb;</i>
	 *                         <li>B.</i>
	 *                         </ol>
	 * 
	 */
	public Note(int octaveScientific, int tone) {
		this((byte) ((octaveScientific + 1) * 12 + tone));
	}

	/**
	 * 
	 * @param octaveScientific |[-2;10]|
	 *                         <ol start="-2">
	 *                         <li>Octocontra;</i>
	 *                         <li>Subsubcontra</i>
	 *                         <li>Subcontra</i>
	 *                         <li>Contra</i>
	 *                         <li>Great</i>
	 *                         <li>Small</i>
	 *                         <li>One-lined</i>
	 *                         <li>Two-lined</i>
	 *                         <li>Three-lined</i>
	 *                         <li>Four-lined</i>
	 *                         <li>Five-lined</i>
	 *                         <li>Six-lined</i>
	 *                         <li>Seven-lined</i>
	 *                         </ol>
	 * @param solfege
	 * 
	 * @param accidental
	 */
	public Note(int octaveScientific, Solfege solfege, Accidental accidental) {
		this(octaveScientific, solfege.getTone() + (accidental == null ? 0 : accidental.getSemitone()));
	}

	/**
	 * 
	 * @return vetor com duas posições, sendo na que na primeira posição está a
	 *         indicação da nota, a segunda com o acidente
	 */
	public Object[] getNoteAcc() {
		return Solfege.getNoteAcc(this.midi % 12);
	}

	public int getOctave() {
		return this.midi / 12 - 1;
	}

	@Override
	public String getXML() {
		Object[] ns = getNoteAcc();
		Accidental acc = (Accidental) ns[1];
		return String.format("<pitch>\r\n<step>%c</step>\r\n%s<octave>%d</octave>\r\n</pitch>\r\n",
				((Solfege) ns[0]).getLetter(), acc.ordinal() == 2 ? "" : "<alter>" + acc.getSemitone() + "</alter>\r\n",
				getOctave());
	}

	// ---------------------------------------------------
	// REPRESENTAÇÃO ESCRITA

	public byte getMidi() {
		return midi;
	}

	public String getHelmholtz() {
		Object[] ns = getNoteAcc();
		int n = this.midi / 12;
		boolean s = n > 3;
		n = s ? n - 4 : 3 - n;
		char o = ((Solfege) ns[0]).getLetter();
		return String.format("%c%s%s", s ? Character.toLowerCase(o) : o, ((Accidental) ns[1]).toSimpleSymbol(),
				StringUtils.repeatChar(s ? '\'' : ',', n));
	}

	public String getScientific() {
		return String.format("%s%s", Solfege.getNoteAccString(this.midi % 12), StringUtils.getUnicodeSubscript(getOctave()));
	}

	public String getBilinear() {
		return String.format("%s%s", getBilinearOctave(this.midi / 12), Solfege.getNoteAccString(this.midi % 12));
	}

	private String getBilinearOctave(int n) {
		n -= 5;
		return (n < 0 ? "-" : "") + (char) (122 - Math.abs(n));
	}

	@Override
	public String toString() {
		return getScientific();
	}

	public static int parseNote(String string) {
		String s = string.substring(0, string.length() - 1);
		return Solfege.parseNoteAcc(s) + 12 * (StringUtils.parseSubSuperScript(string.substring(s.length())) + 1);
	}

	// ---------------------------------------------------
	// FREQUÊNCIA

	public double getF() {
		return 440. * Math.pow(2, (this.midi - 69.) / 12.);
	}

	// ---------------------------------------------------
	// DIFERENÇA DE ALTURA ENTRE DUAS NOTAS

	/**
	 * Função que retorna a distância deste nota a uma outra mais a frente, dada a
	 * partir do seu tom
	 * 
	 * @param solfege    próxima nota
	 * @param accidental próxima nota
	 * @return distância em semitons
	 */
	public int next(Solfege solfege, Accidental accidental) {
		int semitons = solfege.getTone() + accidental.getSemitone();
		return ExtendedMath.mod(semitons - this.getMidi(), 12);
	}

	private static final int[][] RATIOS = { { 1, 25, 9, 6, 5, 4, 45, 3, 8, 5, 9, 15 },
			{ 1, 24, 8, 5, 4, 3, 32, 2, 5, 3, 5, 8 } };

	/**
	 * 
	 * @param step número de semi-tons de distância
	 * @return vetor de inteiros com duas posições, a primeira com o numerador e a
	 *         segunda com o denominador da relação entre as frequências
	 */
	public static int[] getRatio(int step) {
		int d = step % 12;
		return new int[] { (step / 12) * RATIOS[1][d] + RATIOS[0][d], RATIOS[1][d] };
	}

	/**
	 * 
	 * @param step
	 * @return
	 */
	public static String getInterval(int step) {
		int d = step % 12;
		String out = null;
		switch (d) {
		case 0:
			out = "Tônica";
			break;
		case 1:
			out = "Segunda menor";
			break;
		case 2:
			out = "Segunda maior";
			break;
		case 3:
			out = "Terça menor";
			break;
		case 4:
			out = "Terça maior";
			break;
		case 5:
			out = "Quarta justa";
			break;
		case 6:
			out = "Trítono";
			break;
		case 7:
			out = "Quinta justa";
			break;
		case 8:
			out = "Sexta menor";
			break;
		case 9:
			out = "Sexta maior";
			break;
		case 10:
			out = "Sétima menor";
			break;
		case 11:
			out = "Sétima maior";
			break;
		}
		int n = step / 12;
		return (n == 0 ? out : String.format("%d oitava%s%s", n, n > 1 ? "s" : "", d == 0 ? "" : " e " + out));
	}

	public static String getShortInterval(int step) {
		String out = null;
		switch (step % 12) {
		case 0:
			out = "P1";
			break;
		case 1:
			out = "m2";
			break;
		case 2:
			out = "M2";
			break;
		case 3:
			out = "m3";
			break;
		case 4:
			out = "M3";
			break;
		case 5:
			out = "P4";
			break;
		case 6:
			out = "TT";
			break;
		case 7:
			out = "P5";
			break;
		case 8:
			out = "m6";
			break;
		case 9:
			out = "M6";
			break;
		case 10:
			out = "m7";
			break;
		case 11:
			out = "M7";
			break;
		}
		return out;
	}

	public static int getIntervalNumeric(int step) {
		switch (step % 12) {
		case 0:
			return 1;
		case 1:
		case 2:
			return 2;
		case 3:
		case 4:
			return 3;
		case 5:
			return 4;
		case 6: // eita...
			return 9;
		case 7:
			return 5;
		case 8:
		case 9:
			return 6;
		case 10:
		case 11:
			return 7;
		default:
			return -1;
		}
	}

	// ---------------------------------------------------
	// MUDAR NOTA

	public void apply(Accidental accidental) {
		this.midi += accidental.getSemitone();
	}

	public void apply(int av) {
		this.midi += av;
	}

	public void octave() {
		this.midi += 12;
	}

	// ---------------------------------------------------

	@Override
	public int compareTo(Note o) {
		return this.getMidi() - o.getMidi();
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Note)
			return this.getMidi() == ((Note) anObject).getMidi();
		return false;
	}

	@Override
	public int hashCode() {
		return this.midi;
	}

}
