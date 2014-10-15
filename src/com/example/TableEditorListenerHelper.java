package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * This class is a workaround for a java bug: http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6788481
 * CellEditorListener.editorCanceled() doesn't get invoked in JTable-s.
 * JTable can have CellEditors with different input fields. This
 * workaround works only with JTextField inputs.
 */
public class TableEditorListenerHelper {
	
	// dealing with events
	private final EventListenerList listeners = new EventListenerList();
	private ChangeEvent changeEvent;
	
	// cell editor that we're helping
	private CellEditor editor;
	
	// transient state
	private boolean editing = false;
	private JTable table;
	
	public TableEditorListenerHelper(CellEditor editor, JTextField field) {
		this.editor = editor;
		field.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		field.addFocusListener(new FocusListener() {

			@Override public void focusGained(FocusEvent e) {
				editing = true;
			}

			@Override public void focusLost(FocusEvent e) {
				JTable table = TableEditorListenerHelper.this.table;
				if (editing && isEditing(table)) {
					fireEditingCanceled();
				}
			}
			
			private boolean isEditing(JTable table) { // a hack necessary to deal with focuslist vs table repaint
				return table != null && table.isEditing();
			}
			
		});
	}
	
	public void addCellEditorListener(CellEditorListener l) {
		listeners.add(CellEditorListener.class, l);
	}
	
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(CellEditorListener.class, l);
	}
	
	public CellEditorListener[] getCellEditorListeners() {
		return listeners.getListeners(CellEditorListener.class);
	}
	
	protected void fireEditingCanceled() {
		for (CellEditorListener l : getCellEditorListeners()) {
			l.editingCanceled(getOrCreateEvent());
		}
		resetEditingState();
	}
	
	protected void fireEditingStopped() {
		for (CellEditorListener l : getCellEditorListeners()) {
			l.editingStopped(getOrCreateEvent());
		}
		resetEditingState();
	}
	
	private void resetEditingState() {
		table = null;
		editing = false;
	}
	
	private ChangeEvent getOrCreateEvent() {
		return changeEvent = changeEvent == null ? new ChangeEvent(editor) : changeEvent;
	}

}
