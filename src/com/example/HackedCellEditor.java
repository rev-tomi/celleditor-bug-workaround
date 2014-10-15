package com.example;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;

@SuppressWarnings("serial")
public abstract class HackedCellEditor extends DefaultCellEditor {
	
	private final TableEditorListenerHelper editorListenerHelper;

	public HackedCellEditor(JTextField textField) {
		super(textField);
		editorListenerHelper = new TableEditorListenerHelper(this, textField);
	}
	
	@Override public void addCellEditorListener(CellEditorListener l) {
		editorListenerHelper.addCellEditorListener(l);
	}
	
	@Override public void removeCellEditorListener(CellEditorListener l) {
		editorListenerHelper.removeCellEditorListener(l);
	}
	
	@Override public CellEditorListener[] getCellEditorListeners() {
		return editorListenerHelper.getCellEditorListeners();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int row, int column) {
		editorListenerHelper.setTable(table);
		Component defaultResult = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		return getTableCellEditorComponentImpl(table, value, isSelected, row, column, defaultResult);
	}
	
	/** Don't invoke super.getTableCellEditorComponent() because that's patched here. Use the defaultResult instead. */
	protected abstract Component getTableCellEditorComponentImpl(JTable table, Object value,
            boolean isSelected,
            int row, int column, Component defaultResult);

}
