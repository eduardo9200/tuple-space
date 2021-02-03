package services;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import exception.FalhaException;
import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import services.CreateService.TipoInsercao;
import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.Space;
import tupla.VirtualMachine;

/*
 * Classe respons�vel por migrar hosts, VM's e processos
 * */
public class MoveService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 5_000; //5 seg.
	
	/**
	 * Move um host para uma nuvem destino
	 * 
	 * @param from caminho do host o qual se quer mover, no formato nomeNuvem.nomeHost
	 * @param to nome da nuvem que receber� o host movido, no formato nomeNuvem
	 * @param space espa�o onde est�o as tuplas
	 * @param tela refer�ncia � tela do sistema
	 */
	public static void moveHost(String from, String to, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra os nomes de from e to
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeHost = ""; //Novo nome do host que vem do destino para a origem, caso seja necess�rio alterar.
		
		//Faz a valida��o dos nomes do host de origem e nuvem de destino, para saber se est�o dentro do padr�o
		if(ValidateService.validaPartes(from, ValidateService.PARTES_HOST) && ValidateService.validaPartes(to, ValidateService.PARTES_NUVEM)) {
			//Cria��o do template de origem
			Nuvem nuvemOrigem = new Nuvem();
			nuvemOrigem.nome = partesOrigem[0];
			
			Host hostOrigem = new Host();
			hostOrigem.nome = partesOrigem[1];
			
			Space templateOrigem = new Space();
			templateOrigem.nuvem = nuvemOrigem;
			templateOrigem.host = hostOrigem;
			
			//Cria��o do template de destino para valida��o
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;

			//Verifica se o host de origem e a nuvem de destino desse host existem no espa�o
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
				
				//Atualiza o nome do host, caso tenha o mesmo nome de um host existente dentro da nuvem
				if(ValidateService.existeHost(templateAux, space)) {
					String mensagem = "A nuvem " + templateAux.nuvem.nome + " j� possui um host denominado " + templateAux.host.nome + ". Modifique o host name.";
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
					
					CreateService.insereTuplaNoEspaco(novoTemplate, space, TipoInsercao.HOST);
					//space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
				
				//Salva os dados da origem sem o Host
				Space tuplaOrigem = new Space();
				tuplaOrigem.nuvem = nuvemOrigem;
				
				if(!ValidateService.existeNuvem(tuplaOrigem, space)) {
					space.write(tuplaOrigem, null, TEMPO_VIDA_TUPLA);
				}
			
				JOptionPane.showMessageDialog(tela, "Host transferido com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "O host de origem ou a nuvem de destino n�o foi encontrado ou n�o existe. Verifique os nomes e tente novamente.");
				throw new FalhaException("Host e/ou Nuvem n�o existente");
			}
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome v�lido para o host e/ou nuvem. Ex.: nuvem->nuvem; host->nuvem.host");
			throw new FalhaException("Nome do Host e/ou da Nuvem inv�lido");
		}
	}
	
	/*
	 * Move uma VM para um host destino
	 * 
	 * @param from caminho do host o qual se quer mover, no formato nomeNuvem.nomeHost
	 * @param to nome da nuvem que receber� o host movido, no formato nomeNuvem
	 * @param space espa�o onde est�o as tuplas
	 * @param tela refer�ncia � tela do sistema
	 * */
	public static void moveVM(String from, String to, JavaSpace space, Tela tela) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException, FalhaException {
		//Quebra os nomes de from e to
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeVM = ""; //Novo nome do host que vem do destino para a origem, caso seja necess�rio alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_VM) && ValidateService.validaPartes(to, ValidateService.PARTES_HOST)) {
			//Cria��o do template de Origem
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
			
			//Cria��o do template de destino para valida��o
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Host hostDestino = new Host();
			hostDestino.nome = partesDestino[1];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;
			templateDestino.host = hostDestino;

			//Verifica se o host de origem e a nuvem de destino desse host existem no espa�o
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
				
				//Atualiza o nome da VM, caso seja necess�rio
				if(ValidateService.existeVM(templateAux, space)) {
					String mensagem = "O host " + templateAux.host.nome + " j� possui uma VM denominada " + templateAux.vm.nome + ". Modifique o nome da VM.";
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
					
					CreateService.insereTuplaNoEspaco(novoTemplate, space, TipoInsercao.VM);
					//space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
				
				//Salva os dados da origem sem a VM
				Space tuplaOrigem = new Space();
				tuplaOrigem.nuvem = nuvemOrigem;
				tuplaOrigem.host = hostOrigem;
				
				if(!ValidateService.existeHost(tuplaOrigem, space)) {
					space.write(tuplaOrigem, null, TEMPO_VIDA_TUPLA);	
				}
			
				JOptionPane.showMessageDialog(tela, "VM transferida com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "A VM de origem ou o host de destino n�o foi encontrado ou n�o existe. Verifique os nomes e tente novamente.");
				throw new FalhaException("VM e/ou Host n�o existente");
			}
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome v�lido para o host e/ou VM. Ex.: Host->nuvem.host; VM->nuvem.host.vm");
			throw new FalhaException("Nome da VM e/ou do Host inv�lido");
		}
	}

	/*
	 * Move um processo para uma VM destino
	 * 
	 * @param from caminho do host o qual se quer mover, no formato nomeNuvem.nomeHost
	 * @param to nome da nuvem que receber� o host movido, no formato nomeNuvem
	 * @param space espa�o onde est�o as tuplas
	 * @param tela refer�ncia � tela do sistema
	 * */
	public static void moveProcesso(String from, String to, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra os nomes de from e to
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=>'nomeVMOrigem', 3=>'nomeProcessoOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino', 2=>'nomeVMDestino'];
		
		List<Space> templateListOrig = new ArrayList<Space>();
		String novoNomeProcesso = ""; //Novo nome do host que vem do destino para a origem, caso seja necess�rio alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_PROCESSO) && ValidateService.validaPartes(to, ValidateService.PARTES_VM)) {
			//Cria��o do template de Origem
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
			
			//Cria��o do template de destino para valida��o
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

			//Verifica se o processo de origem e a nuvem de destino desse host existem no espa�o
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
				
				//Atualiza o nome do processo, caso seja necess�rio
				if(ValidateService.existeProcesso(templateAux, space)) {
					String mensagem = "A VM " + templateAux.vm.nome + " j� possui um processo denominado " + templateAux.processo.nome + ". Modifique o nome do processo.";
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
				
					CreateService.insereTuplaNoEspaco(novoTemplate, space, TipoInsercao.PROCESSO);
					//space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
				}
				
				//Salva os dados da origem sem o Processo
				Space tuplaOrigem = new Space();
				tuplaOrigem.nuvem = nuvemOrigem;
				tuplaOrigem.host = hostOrigem;
				tuplaOrigem.vm = vmOrigem;
				
				if(!ValidateService.existeVM(tuplaOrigem, space)) {
					space.write(tuplaOrigem, null, TEMPO_VIDA_TUPLA);
				}
			
				JOptionPane.showMessageDialog(tela, "Processo transferido com sucesso!");
				
			} else {
				JOptionPane.showMessageDialog(tela, "O processo de origem ou a VM de destino n�o foi encontrado ou n�o existe. Verifique os nomes e tente novamente.");
				throw new FalhaException("Processo e/ou VM n�o existente");
			}
		
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome v�lido para o processo e/ou VM. Ex.: Processo->nuvem.host.vm.processo; VM->nuvem.host.vm");
			throw new FalhaException("Nome do Processo e/ou da VM inv�lido");
		}
	}
}
