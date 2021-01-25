package services;

import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Ceu;

public class ValidateService {
	
	private static final long TEMPO_MAX_ESPERA_LEITURA = 10_000; //10 seconds;

	public static boolean existeNuvem(Ceu template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Ceu novoTemplate = new Ceu();
		novoTemplate.nuvem = template.nuvem;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeHost(Ceu template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Ceu novoTemplate = new Ceu();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeVM(Ceu template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Ceu novoTemplate = new Ceu();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		novoTemplate.vm = template.vm;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeProcesso(Ceu template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Ceu novoTemplate = new Ceu();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		novoTemplate.vm = template.vm;
		novoTemplate.processo = template.processo;
		
		return existeTemplate(novoTemplate, space);
	}
	
	private static boolean existeTemplate(Ceu template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Ceu busca = (Ceu) space.read(template, null, TEMPO_MAX_ESPERA_LEITURA);
		return busca != null;
	}
	
	public static boolean validaPartes(String partes, int numeroPartesCaminho) {
		return partes.split("\\.").length == numeroPartesCaminho;
	}
}
