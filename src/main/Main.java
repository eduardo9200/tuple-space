package main;

import java.util.Scanner;

import net.jini.space.JavaSpace;
import tupla.Ceu;
import tupla.Host;
import tupla.Nuvem;

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
					String[] partes = message.split(" "); //['insert', 'cloud', 'nomeNuvem']
					
					Nuvem nuvem = new Nuvem();
					nuvem.nome = partes[2];
					
					Ceu ceu = new Ceu();
					ceu.nuvem = nuvem;
					ceu.host = null;
					ceu.vm = null;
					ceu.processo = null;
					
					space.write(ceu, null, 60 * 1000);
				
				} else if(message.contains("insert host")) {
					String[] partes = message.split(" "); //['insert', 'host', 'nomeHost', 'in', 'nomeNuvem']
					
					Nuvem nuvem = new Nuvem();
					nuvem.nome = partes[4];
					
					Host host = new Host();
					host.nome = partes[2];
					
					Ceu template = new Ceu();
					template.nuvem = nuvem;
					template.host = host;

					Ceu ceu = (Ceu) space.read(template, null, 10 * 1000);
					
					if(ceu != null) {
						System.out.println("O host já existe");
					} else {
						Ceu ceu1 = new Ceu();
						ceu1.nuvem = nuvem;
						
						Ceu ceu2 = (Ceu) space.read(ceu1, null, 10 * 1000);
						
						if(ceu2 == null) {
							System.out.println("A nuvem informada não existe");
						
						} else {
							space.write(template, null, 60 * 1000);							
						}
					}
					
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}
