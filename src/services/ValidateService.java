package services;

import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Space;

/*
 * Classe auxiliar para validação de existência de nuvens, hosts, VM's e processos, além de validar os padrões dos nomes desses parâmetros
 * */
public class ValidateService {
	
	//Número de partes que o nome de cada item abaixo possuirá ao tentar executar uma ação
	public static final int PARTES_NUVEM = 1; //['nomeNuvem']
	public static final int PARTES_HOST = 2; //['nomeNuvem', 'nomeHost']
	public static final int PARTES_VM = 3; //['nomeNuvem', 'nomeHost', 'nomeVM']
	public static final int PARTES_PROCESSO = 4; //['nomeNuvem', 'nomeHost', 'nomeVM', 'nomeProcesso']
	
	private static final long TEMPO_MAX_ESPERA_LEITURA = 15_000; //15 seconds;

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
