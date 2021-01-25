package main;

import java.util.Scanner;

import net.jini.space.JavaSpace;
import services.Service;

public class Main {

	public static void main(String[] args) {

		try {
			System.out.println("Procurando pelo serviço JavaSpace...");
			Lookup finder = new Lookup(JavaSpace.class);
			JavaSpace space = (JavaSpace) finder.getService();
			
			if(space == null) {
				System.out.println("O serviço JavaSpace não foi encontrado. Encerrando...");
				System.exit(-1);
			}
			
			System.out.println("O serviço JavaSpace foi encontrado.");
			System.out.println(space);
			
			Scanner scanner = new Scanner(System.in);
			
			while(true) {
				System.out.println("Entre com o texto da mensagem (ENTER para sair): ");
				String message = scanner.nextLine();
				
				if(message == null || message.equals("")) {
					System.exit(0);
				}
				
				if(message.contains("insert cloud")) {
					Service.createNuvem(message, space, null);
				
				} else if(message.contains("insert host")) {
					Service.createHost(message, space, null);
				
				} else if(message.contains("insert vm")) {
					Service.createVM(message, space, null);
				
				} else if(message.contains("insert process")) {
					Service.createProcesso(message, space, null);
				
				} else if(message.contains("remove cloud")) {
					
				} else if(message.contains("remove host")) {
					
				} else if(message.contains("remove vm")) {
					
				} else if(message.contains("remove process")) {
					
				} else if(message.contains("migrate host")) {
					
				} else if(message.contains("migrate vm")) {
					
				} else if(message.contains("migrate process")) {
					
				} else if(message.contains("message")) {
					
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}
