package main;

import net.jini.space.JavaSpace;
import tupla.Space;
import view.View;

public class ReadMessage {

	public static void main(String[] args) {
		System.out.println(View.menu());
		try {
			System.out.println("Procurando pelo servi�o JavaSpace...");
			Lookup finder = new Lookup(JavaSpace.class);
			JavaSpace space = (JavaSpace) finder.getService();
			
			if(space == null) {
				System.out.println("O servi�o JavaSpace n�o foi encontrado. Encerrando...");
				System.exit(-1);
			}
			
			System.out.println("O servi�o JavaSpace foi encontrado.");
			System.out.println(space);
			
			while(true) {
				Space template = new Space();
				Space ceu = (Space) space.take(template, null, 60 * 1000);
				
				if(ceu == null) {
					System.out.println("Tempo de espera esgotado. Encerrando...");
					System.exit(0);
				}
				
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
