package main;

import java.util.Scanner;

import net.jini.space.JavaSpace;

/*
 * Classe depreciada. N�o est� sendo utilizada.
 * */
@Deprecated
public class WriteMessage {

	public static void main(String[] args) {
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
			
			Scanner scanner = new Scanner(System.in);
			
			while(true) {
				System.out.println("Entre com o texto da mensagem (ENTER para sair): ");
				String message = scanner.nextLine();
				
				if(message == null || message.equals("")) {
					System.exit(0);
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
