package br.com.pereiraeng.music.br.com.pereiraeng.music;

/**
 * Classe do objeto que representa uma {@link Note nota musical} em seu contexto
 * (ou seja, com sua {@link #position posição} e {@link #getDuration() duração})
 * 
 * @author Philipe PEREIRA
 *
 */
public class MusicalNote extends Note {

	/**
	 * Posição temporal do início da nota, indicado por um número misto que é
	 * representado por um vetor com três posições:
	 * 
	 * <ol start="0">
	 * <li>unidades;</i>
	 * <li>numerador;</i>
	 * <li>logaritmo na base 2 do denominador.</i>
	 * </ol>
	 */
	private byte[] position;

	/**
	 * Logarítmo na base 2 do inverso da duração da nota (ver
	 * {@link #getDuration()})
	 */
	private byte duration;

	/**
	 * Pontos de aumento da duração
	 */
	private byte dot;

	public MusicalNote(int octave, Solfege solfege, Accidental accidental, byte[] position, byte duration, byte dot) {
		super(octave, solfege, accidental);
		this.position = position;
		this.duration = duration;
		this.dot = dot;
	}

	// duração da nota

	public int getDots() {
		return dot;
	}

	/**
	 * 
	 * @return
	 *         <ol start="0">
	 *         <li>Semibreve;</i>
	 *         <li>Mínima;</i>
	 *         <li>Semimínima;</i>
	 *         <li>Colcheia;</i>
	 *         <li>Semicolcheia;</i>
	 *         <li>Fusa;</i>
	 *         </ol>
	 */
	public byte getDuration() {
		return duration;
	}

	/**
	 * Função que retorna a posição da nota na forma de uma fração
	 * 
	 * @return vetor que representa uma fração (duas posições: numerador e logaritmo
	 *         na base 2 do denominador)
	 */
	public short[] getDurationFr() {
		return new short[] { (short) (Math.pow(2, dot + 1) - 1), (short) (duration + dot) };
	}

	/**
	 * Função que retorna a duração da nota na forma de um número decimal
	 * 
	 * @return número decimal
	 */
	public float getDurationF() {
		float f = (float) (1f / Math.pow(2f, duration));
		if (dot == 0)
			return f;
		else
			return f * (2f - (float) (1f / Math.pow(2f, dot)));
	}

	/**
	 * Função que retorna a duração da nota na forma de um número decimal
	 * 
	 * @return número decimal
	 */
	public double getDurationD() {
		double d = 1. / Math.pow(2., duration);
		if (dot == 0)
			return d;
		else
			return d * (2. - 1. / Math.pow(2., dot));
	}

	// ---------------------------------------------------

	@Override
	public int compareTo(Note o) {
		int c = 0;
		if (o instanceof MusicalNote) {
			MusicalNote mn = (MusicalNote) o;
			c = this.position[0] - mn.position[0];
			if (c == 0)
				c = (int) (this.position[1] * Math.pow(2, mn.position[2])
						- mn.position[1] * Math.pow(2, this.position[2]));
		}
		if (c == 0) // se for na mesma posição, ver a altura
			c = super.compareTo(o);
		return c;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof MusicalNote) {
			MusicalNote c = (MusicalNote) anObject;
			return super.equals(c) && this.position[0] == c.position[0] && this.position[1] == c.position[1]
					&& this.position[2] == c.position[2];
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + position[0] + duration;
	}
}
