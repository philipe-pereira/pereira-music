package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import br.com.pereiraeng.swing.spinner.CyclingSpinnerNumberModel;

/**
 * Classe do objeto gráfico
 * 
 * @author Philipe PEREIRA
 *
 */
public class ToneInput extends JSpinner {
	private static final long serialVersionUID = 1L;

	public ToneInput(int initial) {
		setModel(new CyclingSpinnerNumberModel(initial, 0, 11, 1));
		setEditor(new ToneEditor(this));
	}

	public class ToneEditor extends DefaultEditor {
		private static final long serialVersionUID = 1L;

		public ToneEditor(JSpinner spinner) {
			super(spinner);
			JFormattedTextField ftf = super.getTextField();
			ftf.setFormatterFactory(new DefaultFormatterFactory(new ToneFormatter()));
			ftf.setColumns(3);
			ftf.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}

	private class ToneFormatter extends AbstractFormatter {
		private static final long serialVersionUID = 1L;

		@Override
		public Object stringToValue(String string) throws ParseException {
			return Solfege.parseNoteAcc(string);
		}

		@Override
		public String valueToString(Object object) throws ParseException {
			return Solfege.getNoteAccString((int) object, true);
		}
	}
}
