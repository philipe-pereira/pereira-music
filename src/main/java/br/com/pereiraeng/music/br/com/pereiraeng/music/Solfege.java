package br.com.pereiraeng.music.br.com.pereiraeng.music;

/**
 * Enumeração que permite representar as diferentes notas musicais
 * 
 * @author Philipe PEREIRA
 *
 */
public enum Solfege {
	DO, RE, MI, FA, SOL, LA, SI;

	public char getLetter() {
		return (char) (65 + ((2 + this.ordinal()) % 7));
	}

	public static Solfege getSolfege(char c) {
		return Solfege.values()[(c - 60) % 7];
	}

	/**
	 * É a função inversa de {@link Solfege#getNote(int)}.
	 * 
	 * @return |[0;11]|
	 */
	public int getTone() {
		return 2 * this.ordinal() + (this.ordinal() < 3 ? 0 : -1);
	}

	/**
	 * É a função inversa de {@link Solfege#getTone()}.
	 * 
	 * @param tone |[0;11]|
	 * @return nota
	 */
	public static Solfege getNote(int tone) {
		return Solfege.values()[(int) Math.ceil(tone / 2.)];
	}

	public boolean hasSharp() {
		switch (this) {
		case DO:
		case RE:
		case FA:
		case SOL:
		case LA:
			return true;
		case MI:
		case SI:
			return false;
		}
		return false;
	}

	public boolean hasFlat() {
		switch (this) {
		case RE:
		case MI:
		case SOL:
		case LA:
		case SI:
			return true;
		case DO:
		case FA:
			return false;
		}
		return false;
	}

	/**
	 * Função que retorna a nota que seria utilizada para se representar o mesmo
	 * tom, porém com o acidente oposto (e.g., se esta nota for {@link #DO Dó} e
	 * esta função receber o acidente {@link Accidental#SHARP diese}, a função
	 * retorna {@link #RE Ré}, pois Ré {@link Accidental#FLAT bemol} é o mesmo tom
	 * de Dó diesel).
	 * 
	 * @param ac acidente
	 * @return nota de mesmo tom quando com o acidente oposto
	 */
	private Solfege getSameTone(Accidental ac) {
		return getNote(this.getTone() + 2 * ac.getSemitone());
	}

	/**
	 * 
	 * @param tone |[0;11]|
	 * @return vetor com duas posições, sendo na que na primeira posição está a
	 *         indicação da nota, a segunda com o acidente
	 */
	public static Object[] getNoteAcc(int tone) {
		Solfege s = getNote(tone);
		Accidental st = Accidental.get(tone - s.getTone());
		return new Object[] { s, st };
	}

	/**
	 * 
	 * @param tone |[0;11]|
	 * @return
	 */
	public static String getNoteAccString(int tone) {
		return getNoteAccString(tone, false);
	}

	/**
	 * Função que retorna a sequência de caracteres que designa a nota em função do
	 * tom
	 * 
	 * @param tone tone |[0;11]|
	 * @param two  <code>true</code> para retornar, quando há acidente, a nota com
	 *             um acidente e a nota que com o acidente oposto resulta no mesmo
	 *             tom
	 * @return
	 */
	public static String getNoteAccString(int tone, boolean two) {
		Object[] ns = getNoteAcc(tone);
		Accidental ac = (Accidental) ns[1];
		if (ac != Accidental.NATURAL && two) {
			Solfege s = (Solfege) ns[0];
			return String.format("%c%s/%c%s", s.getLetter(), ac.toSimpleSymbol(), s.getSameTone(ac).getLetter(),
					ac.oppose().toSimpleSymbol());
		} else
			return String.format("%c%s", ((Solfege) ns[0]).getLetter(), ac.toSimpleSymbol());
	}

	/**
	 * Função que transforma uma sequência de caracteres no tom da nota
	 * 
	 * @param string uma letra ou duas (a primeira sendo uma letra de A a G, a
	 *               eventual segunda a designação do
	 *               {@link Accidental#toSimpleSymbol() acidente} )
	 * @return tone |[0;11]|
	 */
	public static int parseNoteAcc(String string) {
		int out = getSolfege(string.charAt(0)).getTone();
		if (string.length() > 1)
			out += Accidental.getAccidental(string.charAt(1)).getSemitone();
		return out;
	}
}
