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

	/*
	 * Escreve uma mensagem no espaço para ser lida por um processo
	 * 
	 * @param from caminho do remetente no formato nomeNuvem.nomeHost.nomeVM.nomeProcesso
	 * @param to caminho do destinatário no formato nomeNuvem.nomeHost.nomeVM.nomeProcesso
	 * @param mensagem mensagem enviada do remetente ao destinatário
	 * @param space espaço onde a tupla de mensagem será escrita
	 * @param tela referência à tela onde o sistema está sendo executado
	 * */
	public static void sendMessage(String from, String to, String mensagem, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra from e to para separar os nomes da nuvem, host, VM e processo
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem', 3=>'nomeProcessoOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino', 2=>'nomeVMDestino', 3=>'nomeProcessoDestino'];
		
		//Valida from e to para saber se estão de acordo com o padrão do nome de processo
		if(ValidateService.validaPartes(from, ValidateService.PARTES_PROCESSO) && ValidateService.validaPartes(to, ValidateService.PARTES_PROCESSO)) {
			//Cria o template do remetente
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
			
			//Cria o template do destinatário
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
			
			//Valida se o remetente existe e se pertence à mesma VM do destinatário e escreve a tupla de mensagem no espaço.
			//Obs.: não é necessário o destinatário existir para ser enviada uma mensagem, pois quando existir, ele receberá essa mensagem.
			if(ValidateService.existeProcesso(templateOrigem, space) && pertenceAMesmaVM(templateOrigem, templateDestino)) {
				//Cria o template da tupla de mensagem
				Mensagem templateMensagem = new Mensagem();
				templateMensagem.remetente = templateOrigem;
				templateMensagem.destinatario = templateDestino;
				templateMensagem.mensagem = mensagem;
			
				//Insere a mensagem no espaço de tuplas
				space.write(templateMensagem, null, Lease.FOREVER);
			
			} else {
				JOptionPane.showMessageDialog(tela, "Processo remetente não existente ou o remetente e destinatário não pertencem à mesma VM.");
				throw new FalhaException("Remetente não existe ou os processos não estão na mesma VM.");
			}
		}
	}
	
	/*
	 * Valida para saber se dois processos estão dentro da mesma VM
	 * */
	public static boolean pertenceAMesmaVM(Space origem, Space destino) {
		return origem.nuvem.nome.equals(destino.nuvem.nome)
			&& origem.host.nome.equals(destino.host.nome)
			&& origem.vm.nome.equals(destino.vm.nome);
	}
}
