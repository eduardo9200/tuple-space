package services;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import exception.FalhaException;
import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Host;
import tupla.Mensagem;
import tupla.Nuvem;
import tupla.Processo;
import tupla.Space;
import tupla.VirtualMachine;

public class MessagingService {

	public static void sendMessage(String from, String to, String mensagem, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem', 3=>'nomeProcessoOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino', 2=>'nomeVMDestino', 3=>'nomeProcessoDestino'];
		
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
			
			if(ValidateService.existeProcesso(templateOrigem, space) && pertenceAMesmaVM(templateOrigem, templateDestino)) {
				Mensagem templateMensagem = new Mensagem();
				templateMensagem.remetente = templateOrigem;
				templateMensagem.destinatario = templateDestino;
				templateMensagem.mensagem = mensagem;
			
				space.write(templateMensagem, null, Lease.FOREVER);
			
			} else {
				JOptionPane.showMessageDialog(tela, "Processo remetente não existente ou o remetente e destinatário não pertencem à mesma VM.");
				throw new FalhaException("Remetente não existe ou os processos não estão na mesma VM.");
			}
		}
	}
	
	public static boolean pertenceAMesmaVM(Space origem, Space destino) {
		return origem.nuvem.nome.equals(destino.nuvem.nome)
			&& origem.host.nome.equals(destino.host.nome)
			&& origem.vm.nome.equals(destino.vm.nome);
	}
}
