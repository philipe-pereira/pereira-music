package br.com.pereiraeng.music.br.com.pereiraeng.music;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

public class NoteInput extends JSpinner {
	private static final long serialVersionUID = 1L;

	public NoteInput(int initial) {
		setModel(new SpinnerNumberModel(initial, 12, 116, 1));
		setEditor(new NoteEditor(this));
	}

	@Override
	public void setForeground(Color color) {
		super.setForeground(color);
		((DefaultEditor) getEditor()).getTextField().setForeground(color);
	}

	public class NoteEditor extends DefaultEditor {
		private static final long serialVersionUID = 1L;

		public NoteEditor(JSpinner spinner) {
			super(spinner);
			JFormattedTextField ftf = super.getTextField();
			ftf.setFormatterFactory(new DefaultFormatterFactory(new NoteFormatter()));
			ftf.setColumns(3);
			ftf.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}

	private class NoteFormatter extends AbstractFormatter {
		private static final long serialVersionUID = 1L;

		@Override
		public Object stringToValue(String string) throws ParseException {
			return Note.parseNote(string);
		}

		@Override
		public String valueToString(Object object) throws ParseException {
			return new Note(((Integer) object).byteValue()).toString();
		}
	}
}
