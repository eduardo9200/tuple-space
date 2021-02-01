package services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.Space;
import tupla.VirtualMachine;

public class MessagingService {

	public static void messagingBetweenProcess(String from, String to, String mensagem, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino'];
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_PROCESSO) && ValidateService.validaPartes(to, ValidateService.PARTES_PROCESSO)) {
			//Origem
			Nuvem nuvemOrigem = new Nuvem();
			nuvemOrigem.nome = partesOrigem[0];
			
			Host hostOrigem = new Host();
			hostOrigem.nome = partesOrigem[1];
			
			VirtualMachine vmOrigem = new VirtualMachine();
			vmOrigem.nome = partesOrigem[2];
			
			Processo processoOrigem = new Processo();
			processoOrigem.nome = partesOrigem[3];
			
			Space templateOrigem = new Space();
			templateOrigem.nuvem = nuvemOrigem;
			templateOrigem.host = hostOrigem;
			templateOrigem.vm = vmOrigem;
			templateOrigem.processo = processoOrigem;
			
			//Destino
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Host hostDestino = new Host();
			hostDestino.nome = partesDestino[1];
			
			VirtualMachine vmDestino = new VirtualMachine();
			vmDestino.nome = partesDestino[2];
			
			Processo processoDestino = new Processo();
			processoDestino.nome = partesDestino[3];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;
			templateDestino.host = hostDestino;
			templateDestino.vm = vmDestino;
			templateDestino.processo = processoDestino;
			
			if(ValidateService.existeProcesso(templateOrigem, space) && ValidateService.existeProcesso(templateDestino, space) && pertenceAMesmaVM(templateOrigem, templateDestino)) {
				//Space tuplaOrigem = (Space) space.take(templateOrigem, null, Lease.FOREVER);
				Space tuplaDestino = (Space) space.take(templateDestino, null, Lease.FOREVER);
								
				//tuplaDestino.processo.mensagem = tuplaOrigem.processo.mensagem;
				
				tuplaDestino.processo.mensagem = mensagem;
				space.write(tuplaDestino, null, 10_000);
			}
		}
	}
	
	private static boolean pertenceAMesmaVM(Space origem, Space destino) {
		return origem.nuvem.equals(destino.nuvem)
			&& origem.host.equals(destino.host)
			&& origem.vm.equals(destino.vm);
	}
}
