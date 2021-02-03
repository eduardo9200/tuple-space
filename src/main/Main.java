package main;

import java.awt.EventQueue;
import java.util.Scanner;

import net.jini.space.JavaSpace;
import services.Service;

public class Main {

	public static void main(String[] args) {
		Tela frame = new Tela();
		frame.setVisible(true);
		frame.instanciaEspacoTupla();
		//frame.atualizarTabela();
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

}
