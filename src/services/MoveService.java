package services;

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
import tupla.Space;
import tupla.VirtualMachine;

public class MoveService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 10_000; //10 seg.
	
	public static void moveHost(String from, String to, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino'];
		
		List<Space> hostListOrig = new ArrayList<Space>();
		List<Space> hostListDest = new ArrayList<Space>();
		
		String novoNomeHostOrigem = ""; //Novo nome do host que vem do destino para a origem, caso seja necessário alterar.
		String novoNomeHostDestino = ""; //Novo nome do host que vem da origem para o destino, caso seja necessário alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_HOST) && ValidateService.validaPartes(to, ValidateService.PARTES_HOST)) {
			//Origem
			Nuvem nuvemOrigem = new Nuvem();
			nuvemOrigem.nome = partesOrigem[0];
			
			Host hostOrigem = new Host();
			hostOrigem.nome = partesOrigem[1];
			
			Space templateOrigem = new Space();
			templateOrigem.nuvem = nuvemOrigem;
			templateOrigem.host = hostOrigem;
			
			//Destino
			Nuvem nuvemDestino = new Nuvem();
			nuvemDestino.nome = partesDestino[0];
			
			Host hostDestino = new Host();
			hostDestino.nome = partesDestino[1];
			
			Space templateDestino = new Space();
			templateDestino.nuvem = nuvemDestino;
			templateDestino.host = hostDestino;
			
			Boolean existeHostOrigem = Boolean.TRUE;
			Boolean existeHostDestino = Boolean.TRUE;
			
			//Remove todos os templates de origem
			while(existeHostOrigem) {
				Space result = (Space) space.take(templateOrigem, null, TEMPO_MAX_LEITURA);
				
				if(result != null)
					hostListOrig.add(result);
				else
					existeHostOrigem = Boolean.FALSE;
			}
			
			//Remove todos os templates de destino
			while(existeHostDestino) {
				Space result = (Space) space.take(templateDestino, null, TEMPO_MAX_LEITURA);
				
				if(result != null)
					hostListDest.add(result);
				else
					existeHostDestino = Boolean.FALSE;
			}
			
			//Valida nome dos hosts a serem permutados entre as nuvens
			Space templateAux1 = new Space();
			templateAux1.nuvem = nuvemOrigem;
			templateAux1.host = hostDestino;
			
			Space templateAux2 = new Space();
			templateAux2.nuvem = nuvemDestino;
			templateAux2.host = hostOrigem;
			
			if(ValidateService.existeHost(templateAux1, space)) {
				String mensagem = "A nuvem " + templateAux1.nuvem.nome + " já possui um host denominado " + templateAux1.host.nome + ". Modifique o host name.";
				novoNomeHostOrigem = JOptionPane.showInputDialog(tela, mensagem, "Host", JOptionPane.WARNING_MESSAGE);	
			}
			
			if(ValidateService.existeHost(templateAux2, space)) {
				String mensagem = "A nuvem " + templateAux1.nuvem.nome + " já possui um host denominado " + templateAux1.host.nome + ". Modifique o host name.";
				novoNomeHostDestino = JOptionPane.showInputDialog(tela, mensagem, "Host", JOptionPane.WARNING_MESSAGE);
			}
			
			//Permuta os hosts
			for(Space item : hostListDest) {
				
				item.host.nome = novoNomeHostDestino.isEmpty()
							   ? item.host.nome
							   : novoNomeHostDestino;
				
				Space novoTemplate = new Space();
				novoTemplate.nuvem = nuvemOrigem;
				novoTemplate.host = item.host;
				novoTemplate.vm = item.vm;
				novoTemplate.processo = item.processo;
				
				space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
			}
			
			for(Space item : hostListOrig) {
				item.host.nome = novoNomeHostOrigem.isEmpty()
							   ? item.host.nome
							   : novoNomeHostOrigem;
				
				Space novoTemplate = new Space();
				novoTemplate.nuvem = nuvemDestino;
				novoTemplate.host = item.host;
				novoTemplate.vm = item.vm;
				novoTemplate.processo = item.processo;
				
				space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
			}
			
			JOptionPane.showMessageDialog(tela, "Hosts permutados entre as nuvens com sucesso!");
			
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido para o host no formato 'nuvem.host' (sem aspas). Ex.: nuvemA.host1;");
		}
	}
	
	public static void moveVM(String from, String to, JavaSpace space, Tela tela) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
		String[] partesOrigem = from.split("\\."); //[0=>'nomeNuvemOrigem', 1=>'nomeHostOrigem', 2=> 'nomeVMOrigem'];
		String[] partesDestino = to.split("\\."); //[0=>'nomeNuvemDestino', 1=>'nomeHostDestino', 3=> 'nomeVMDestino'];
		
		List<Space> vmListOrig = new ArrayList<Space>();
		List<Space> vmListDest = new ArrayList<Space>();
		
		String novoNomeVmOrigem = ""; //Novo nome da VM que vem do destino para a origem, caso seja necessário alterar.
		//String novoNomeVmDestino = ""; //Novo nome da VM que vem da origem para o destino, caso seja necessário alterar.
		
		if(ValidateService.validaPartes(from, ValidateService.PARTES_VM) && ValidateService.validaPartes(to, ValidateService.PARTES_VM)) {
			//Origem
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
			
			//Destino
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
			
			Boolean existeHostOrigem = Boolean.TRUE;
			Boolean existeHostDestino = Boolean.TRUE;
			
			//Remove todos os templates de origem
			while(existeHostOrigem) {
				Space result = (Space) space.take(templateOrigem, null, TEMPO_MAX_LEITURA);
				
				if(result != null)
					vmListOrig.add(result);
				else
					existeHostOrigem = Boolean.FALSE;
			}
			
			//Remove todos os templates de destino
			/*while(existeHostDestino) {
				Space result = (Space) space.take(templateDestino, null, TEMPO_MAX_LEITURA);
				
				if(result != null)
					vmListDest.add(result);
				else
					existeHostDestino = Boolean.FALSE;
			}*/
			
			//Valida nome dos hosts a serem permutados entre as nuvens
			Space templateAux = new Space();
			templateAux.nuvem = nuvemDestino;
			templateAux.host = hostDestino;
			templateAux.vm = vmOrigem;
			
			/*Space templateAux2 = new Space();
			templateAux2.nuvem = nuvemOrigem;
			templateAux2.host = hostDestino;*/
			
			if(ValidateService.existeHost(templateAux, space)) {
				String mensagem = "A VM " + templateAux.vm.nome + " já existe dentro do host " + templateAux.host.nome + ". Modifique seu nome.";
				novoNomeVmOrigem = JOptionPane.showInputDialog(tela, mensagem, "VM", JOptionPane.WARNING_MESSAGE);	
			}
			
			/*if(ValidateService.existeHost(templateAux2, space)) {
				String mensagem = "A nuvem " + templateAux1.nuvem.nome + " já possui um host denominado " + templateAux1.host.nome + ". Modifique o host name.";
				novoNomeHostDestino = JOptionPane.showInputDialog(tela, mensagem, "Host", JOptionPane.WARNING_MESSAGE);
			}*/
			
			//Permuta os hosts
			for(Space item : vmListOrig) {
				item.vm.nome = novoNomeVmOrigem.isEmpty()
						   	 ? item.vm.nome
						     : novoNomeVmOrigem;
				
				Space novoTemplate = new Space();
				novoTemplate.nuvem = nuvemDestino;
				novoTemplate.host = hostDestino;
				novoTemplate.vm = item.vm;
				novoTemplate.processo = item.processo;
				
				space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
			}
			
			for(Space item : vmListOrig) {
				Space novoTemplate = new Space();
				novoTemplate.nuvem = nuvemOrigem;
				novoTemplate.host = hostOrigem;
				
				space.write(novoTemplate, null, TEMPO_VIDA_TUPLA);
			}
			
			JOptionPane.showMessageDialog(tela, "Hosts permutados entre as nuvens com sucesso!");
			
		} else {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido para o host no formato 'nuvem.host' (sem aspas). Ex.: nuvemA.host1;");
		}
	}

	public static void moveProcesso(String from, String to, JavaSpace space, Tela tela) {
		
	}
}
