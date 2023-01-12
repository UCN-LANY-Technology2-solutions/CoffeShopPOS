package ui;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class GuiHelpers {
	
	public static <T> ListModel<T> mapToListModel(List<T> list){
		
		if(list == null)
			throw new IllegalArgumentException("list cannot be null");
		
		DefaultListModel<T> listModel = new DefaultListModel<>();
		for (int idx = 0; idx < list.size(); idx++) {
			listModel.addElement(list.get(idx));
		}
		return listModel;		
	}
	
	public static <T> ComboBoxModel<T> mapToComboBoxModel(List<T> list){
		
		if(list == null)
			throw new IllegalArgumentException("list cannot be null");
		
		DefaultComboBoxModel<T> comboBoxModel = new DefaultComboBoxModel<>();
		for(int idx = 0; idx < list.size(); idx++) {
			comboBoxModel.addElement(list.get(idx));
		}
		return comboBoxModel;
	}
}
