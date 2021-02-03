package services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Space;

/*
 * Classe auxiliar para consultar o espaço de tuplas e retornar todas as tuplas existentes
 * para atualizar os dados da tabela da tela do sistema.
 * */
public class ReloadSpaceService {

	/*
	 * Busca todas as tuplas do espaço, exceto as tuplas de mensagem entre processos
	 * 
	 * @param space espaço onde as tuplas estão armazenadas
	 * @return todas as tuplas do espaço, exceto as tuplas de mensagens entre processos
	 * */
	public static List<Space> findAllTuples(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		//Lista das tuplas do espaço
		List<Space> tuplas = new ArrayList<Space>();
		
		//Criação do template
		//Ao não setar valores, ele buscará todas as tuplas
		Space template = new Space();
		
		boolean existeNuvem = true;
		
		//Rotina para remover todas as tuplas e adicioná-las em uma lista
		while(existeNuvem) {
			Space tupla = (Space) space.take(template, null, 5_000);
			
			if(tupla != null)
				tuplas.add(tupla);
			else
				existeNuvem = false;
		}
		
		//Após a rotina anterior, as tuplas são devolvidas ao espaço
		for(Space tupla : tuplas) {
			space.write(tupla, null, Lease.FOREVER);
		}
		
		return tuplas;
	}
}
