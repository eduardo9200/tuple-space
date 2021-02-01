package services;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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

public class MoveService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 10_000; //10 seg.
	
	/**
	 * 
	 * @param from caminho do host o qual se quer mover, no formato nomeNuvem.nomeHost
	 * @param to nome da nuvem que receberá o host movido
	 * @param space 
	 * @param tela
	 */
	public static void moveHost(String from, String to, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeHost = ""; //Novo nome do host que vem do destino para a origem, caso seja necessário alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_HOST) && ValidateService.validaPartes(to, ValidateService.PARTES_NUVEM)) {
			//Criação do template de Origem
			Nuvem nuvemOrigem = new Nuvem();
			nuvemOrigem.nome = partesOrigem[0];
			
			Host hostOrigem = new Host();
			hostOrigem.nome = partesOrigem[1];
			
			Space templateOrigem = new Space();
			templateOrigem.nuvem = nuvemOrigem;
			templateOrigem.host = hostOrigem;
			
			//Criação do template de destino para validação
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;

			//Verifica se o host de origem e a nuvem de destino desse host existem no espaço
			if(ValidateService.existeHost(templateOrigem, space) && ValidateService.existeNuvem(templateDestino, space)) {
				
				Boolean existeTemplateOrigem = Boolean.TRUE;
				
				//Remove todos os templates da <origem> e armazena-os em uma lista
				while(existeTemplateOrigem) {
					Space result = (Space) space.take(templateOrigem, null, TEMPO_MAX_LEITURA);
					
					if(result != null)
						templateListOrig.add(result);
					else
						existeTemplateOrigem = Boolean.FALSE;
				}
				
				//Valida nome do host a ser transferido
				Space templateAux = new Space();
				templateAux.nuvem = nuvemDestino;
				templateAux.host = hostOrigem;
				
				if(ValidateService.existeHost(templateAux, space)) {
					String mensagem = "A nuvem " + templateAux.nuvem.nome + " já possui um host denominado " + templateAux.host.nome + ". Modifique o host name.";
					novoNomeHost = JOptionPane.showInputDialog(tela, mensagem, "Host", JOptionPane.WARNING_MESSAGE);	
				}
				
				//Transfere o host para a nuvem de destino
				for(Space item : templateListOrig) {
					Host hostDestino = new Host();
					hostDestino.nome = novoNomeHost.isEmpty()
							   		 ? item.host.nome
							   		 : novoNomeHost;
					
					Space novoTemplate = new Space();
					novoTemplate.nuvem = nuvemDestino;
					novoTemplate.host = hostDestino;
					novoTemplate.vm = item.vm;
					novoTemplate.processo = item.processo;
					
					space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
			
				JOptionPane.showMessageDialog(tela, "Host transferido com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "O host de origem ou a nuvem de destino não foi encontrada ou não existe. Verifique os nomes e tente novamente.");
			}
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido para o host e/ou nuvem. Ex.: nuvem->nuvem; host->nuvem.host");
		}
	}
	
	public static void moveVM(String from, String to, JavaSpace space, Tela tela) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeVM = ""; //Novo nome do host que vem do destino para a origem, caso seja necessário alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_VM) && ValidateService.validaPartes(to, ValidateService.PARTES_HOST)) {
			//Criação do template de Origem
			Nuvem nuvemOrigem = new Nuvem();
			nuvemOrigem.nome = partesOrigem[0];
			
			Host hostOrigem = new Host();
			hostOrigem.nome = partesOrigem[1];
			
			VirtualMachine vmOrigem = new VirtualMachine();
			vmOrigem.nome = partesOrigem[2];
			
			Space templateOrigem = new Space();
			templateOrigem.nuvem = nuvemOrigem;
			templateOrigem.host = hostOrigem;
			templateOrigem.vm = vmOrigem;
			
			//Criação do template de destino para validação
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Host hostDestino = new Host();
			hostDestino.nome = partesDestino[1];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;
			templateDestino.host = hostDestino;

			//Verifica se o host de origem e a nuvem de destino desse host existem no espaço
			if(ValidateService.existeVM(templateOrigem, space) && ValidateService.existeHost(templateDestino, space)) {
				
				Boolean existeTemplateOrigem = Boolean.TRUE;
				
				//Remove todos os templates da <origem> e armazena-os em uma lista
				while(existeTemplateOrigem) {
					Space result = (Space) space.take(templateOrigem, null, TEMPO_MAX_LEITURA);
					
					if(result != null)
						templateListOrig.add(result);
					else
						existeTemplateOrigem = Boolean.FALSE;
				}
				
				//Valida o nome da VM a ser transferida
				Space templateAux = new Space();
				templateAux.nuvem = nuvemDestino;
				templateAux.host = hostDestino;
				templateAux.vm = vmOrigem;
				
				if(ValidateService.existeVM(templateAux, space)) {
					String mensagem = "O host " + templateAux.host.nome + " já possui uma VM denominada " + templateAux.vm.nome + ". Modifique o nome da VM.";
					novoNomeVM = JOptionPane.showInputDialog(tela, mensagem, "Virtual Machine", JOptionPane.WARNING_MESSAGE);	
				}
				
				//Transfere a VM para o host de destino
				for(Space item : templateListOrig) {
					VirtualMachine vmDestino = new VirtualMachine();
					vmDestino.nome = novoNomeVM.isEmpty()
							   		 ? item.vm.nome
							   		 : novoNomeVM;
					
					Space novoTemplate = new Space();
					novoTemplate.nuvem = nuvemDestino;
					novoTemplate.host = hostDestino;
					novoTemplate.vm = vmDestino;
					novoTemplate.processo = item.processo;
					
					space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
			
				JOptionPane.showMessageDialog(tela, "VM transferida com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "A VM de origem ou o host de destino não foi encontrado ou não existe. Verifique os nomes e tente novamente.");
			}
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido para o host e/ou VM. Ex.: Host->nuvem.host; VM->nuvem.host.vm");
		}
	}

	public static void moveProcesso(String from, String to, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem', 3=>'nomeProcessoOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino', 2=>'nomeVMDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeProcesso = ""; //Novo nome do host que vem do destino para a origem, caso seja necessário alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_PROCESSO) && ValidateService.validaPartes(to, ValidateService.PARTES_VM)) {
			//Criação do template de Origem
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
			
			//Criação do template de destino para validação
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Host hostDestino = new Host();
			hostDestino.nome = partesDestino[1];
			
			VirtualMachine vmDestino = new VirtualMachine();
			vmDestino.nome = partesDestino[2];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;
			templateDestino.host = hostDestino;
			templateDestino.vm = vmDestino;

			//Verifica se o processo de origem e a nuvem de destino desse host existem no espaço
			if(ValidateService.existeProcesso(templateOrigem, space) && ValidateService.existeVM(templateDestino, space)) {
				
				Boolean existeTemplateOrigem = Boolean.TRUE;
				
				//Remove todos os templates da <origem> e armazena-os em uma lista
				while(existeTemplateOrigem) {
					Space result = (Space) space.take(templateOrigem, null, TEMPO_MAX_LEITURA);
					
					if(result != null)
						templateListOrig.add(result);
					else
						existeTemplateOrigem = Boolean.FALSE;
				}
				
				//Valida o nome do processo a ser transferido
				Space templateAux = new Space();
				templateAux.nuvem = nuvemDestino;
				templateAux.host = hostDestino;
				templateAux.vm = vmDestino;
				templateAux.processo = processoOrigem;
				
				if(ValidateService.existeProcesso(templateAux, space)) {
					String mensagem = "A VM " + templateAux.vm.nome + " já possui um processo denominado " + templateAux.processo.nome + ". Modifique o nome do processo.";
					novoNomeProcesso = JOptionPane.showInputDialog(tela, mensagem, "Processo", JOptionPane.WARNING_MESSAGE);	
				}
				
				//Transfere o processo para a VM de destino
				for(Space item : templateListOrig) {
					Processo processoDestino = new Processo();
					processoDestino.nome = novoNomeProcesso.isEmpty()
							   		 	 ? item.processo.nome
							   			 : novoNomeProcesso;
					
					Space novoTemplate = new Space();
					novoTemplate.nuvem = nuvemDestino;
					novoTemplate.host = hostDestino;
					novoTemplate.vm = vmDestino;
					novoTemplate.processo = processoDestino;
					
					space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
			
				JOptionPane.showMessageDialog(tela, "Processo transferido com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "O processo de origem ou a VM de destino não foi encontrado ou não existe. Verifique os nomes e tente novamente.");
			}
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido para o processo e/ou VM. Ex.: Processo->nuvem.host.vm.processo; VM->nuvem.host.vm");
		}
	}
}
