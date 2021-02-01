package services;

import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Space;

public class ValidateService {
	
	public static final int PARTES_NUVEM = 1;
	public static final int PARTES_HOST = 2;
	public static final int PARTES_VM = 3;
	public static final int PARTES_PROCESSO = 4;
	
	private static final long TEMPO_MAX_ESPERA_LEITURA = 10_000; //10 seconds;

	public static boolean existeNuvem(Space template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space novoTemplate = new Space();
		novoTemplate.nuvem = template.nuvem;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeHost(Space template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space novoTemplate = new Space();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeVM(Space template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space novoTemplate = new Space();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		novoTemplate.vm = template.vm;
		
		return existeTemplate(novoTemplate, space);
	}
	
	public static boolean existeProcesso(Space template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space novoTemplate = new Space();
		novoTemplate.nuvem = template.nuvem;
		novoTemplate.host = template.host;
		novoTemplate.vm = template.vm;
		novoTemplate.processo = template.processo;
		
		return existeTemplate(novoTemplate, space);
	}
	
	private static boolean existeTemplate(Space template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space busca = (Space) space.read(template, null, TEMPO_MAX_ESPERA_LEITURA);
		return busca != null;
	}
	
	public static boolean validaPartes(String partes, int numeroPartesCaminho) {
		if(partes != null)
			return partes.split("\\.").length == numeroPartesCaminho;
		return false;
	}
}
