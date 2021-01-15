package commons;

import java.rmi.RemoteException;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Ceu;
import tupla.Host;
import tupla.Nuvem;

public class Service {

	public static void createNuvem(String comando, JavaSpace space) throws RemoteException, TransactionException {
		Comando com = quebraComando(comando);
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = Comando.getNome(com.getLocalizadorOrigem());
		
		Ceu ceu = new Ceu();
		ceu.nuvem = nuvem;
		ceu.host = null;
		ceu.vm = null;
		ceu.processo = null;
		
		space.write(ceu, null, 60 * 1000);
		return;
	}
	
	public static void createHost(String comando, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = comando.split(" "); //['insert', 'host', 'nomeHost', 'in', 'nomeNuvem']
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[4];
		
		Host host = new Host();
		host.nome = partes[2];
		
		Ceu template = new Ceu();
		template.nuvem = nuvem;
		template.host = host;

		Ceu buscaTemplate = (Ceu) space.read(template, null, 10 * 1000);
		
		if(buscaTemplate != null) {
			System.out.println("O host já existe");
			
		} else {
			Ceu template2 = new Ceu();
			template2.nuvem = nuvem;
			
			Ceu buscaTemplate2 = (Ceu) space.read(template2, null, 10 * 1000);
			
			if(buscaTemplate2 == null) {
				System.out.println("A nuvem informada não existe");
			
			} else {
				space.write(template, null, 60 * 1000);							
			}
		}
		return;
	}
	
	public static void createVM(String comando, JavaSpace space) {
		
	}
	
	public static void createProcesso(String comando, JavaSpace space) {
		
	}
	
	private static Comando quebraComando(String comando) {
		return Comando.quebraComando(comando);
	}
}
