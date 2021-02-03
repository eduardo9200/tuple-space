package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.Space;
import tupla.VirtualMachine;

public class TuplasTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private List<Space> dados = new ArrayList<Space>(); //Linhas
	private String[] colunas = {"#", "Nuvem", "Host", "VM", "Processo", "Mensagem"}; //Colunas

	@Override
	public int getRowCount() {
		return dados.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				Nuvem nuvem = dados.get(rowIndex).nuvem;
				return nuvem == null ? "-" : nuvem.nome;
			case 2:
				Host host = dados.get(rowIndex).host;
				return host == null ? "-" : host.nome;
			case 3:
				VirtualMachine vm = dados.get(rowIndex).vm;
				return vm == null ? "-" : vm.nome;
			case 4:
				Processo processo = dados.get(rowIndex).processo;
				return processo == null ? "-" : processo.nome;
			case 5:
				Processo p = dados.get(rowIndex).processo;
				return p == null ? "-" : ((p.mensagem == null || p.mensagem.isEmpty()) ? "-" : p.mensagem);
			default:
				return null;
		}
	}
	
	public void addRow(Space space) {
		dados.add(space);
		this.fireTableDataChanged();
	}

	public void clearAllRows() {
		dados.clear();
	}
	
}
