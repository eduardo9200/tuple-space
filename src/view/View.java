package view;

import javax.swing.JFrame;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;

	public static String menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("ESPA�O DE TUPLAS").append('\n');
		builder.append("1 - Adicionar Nuvem").append('\n');
		builder.append("2 - Adicionar Host").append('\n');
		builder.append("3 - Adicionar VM").append('\n');
		builder.append("4 - Adicionar Processo").append('\n');
		builder.append("Digite a op��o selecionada: ");
		return builder.toString();
	}
}
