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
import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.Space;
import tupla.VirtualMachine;

public class RemoveService {
	
	public static final long TEMPO_MAX_REMOCAO = 5_000; //5 seg.
	public static final long TEMPO_VIDA_TUPLA= Lease.FOREVER;

	public static void removeNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		if(!ValidateService.validaPartes(nome, ValidateService.PARTES_NUVEM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido. Ex.: nuvem1, nuvem2, ...");
			throw new FalhaException("Nome nuvem inválido");
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Space template = new Space();
		template.nuvem = nuvem;
		
		if(ValidateService.existeNuvem(template, space)) {
			List<Space> nuvemList = new ArrayList<Space>();
			
			Boolean existeHost = Boolean.TRUE; //Valida se existem hosts dentro da nuvem;
			
			while(existeHost) {
				Space result = (Space) space.take(template, null, TEMPO_MAX_REMOCAO);
				
				if(result != null)
					nuvemList.add(result);
				else
					existeHost = Boolean.FALSE;
			}
			
			if(!nuvemList.isEmpty() && nuvemList.size() == 1 && nuvemList.get(0).vm != null) {
				devolverTemplatesParaOEspaco(nuvemList, space);
					
				System.out.println("Ainda existe " + nuvemList.size() + " Host dentro da Nuvem. Favor, excluí-lo antes de tentar excluir a Nuvem.");
				JOptionPane.showMessageDialog(tela, "Ainda existe " + nuvemList.size() + " Host dentro da Nuvem. Favor, excluí-lo antes de tentar excluir a nuvem.");					
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");
				
			} else if(!nuvemList.isEmpty() && nuvemList.size() > 1) {
				devolverTemplatesParaOEspaco(nuvemList, space);
				
				System.out.println("Ainda existem " + nuvemList.size() + " Hosts dentro da Nuvem. Favor, excluir todos eles antes de tentar excluir a Nuvem.");
				JOptionPane.showMessageDialog(tela, "Ainda existem " + nuvemList.size() + " Hosts dentro da Nuvem. Favor, excluir todos eles antes de tentar excluir a Nuvem.");
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");
				
			} else {
				JOptionPane.showMessageDialog(tela, "Nuvem excluída com sucesso!");
			}
		
		} else {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			throw new FalhaException("Nuvem inexistente");
		}
	}
	
	public static void removeHost(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partes = caminhoComNome != null
				? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost']
				: new String[]{};

		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_HOST)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de Host válido, no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host2, nuvem2.host2, ...");
			throw new FalhaException("Nome host inválido");
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		
		if(ValidateService.existeHost(template, space)) {
			List<Space> hostList = new ArrayList<Space>();
			
			Boolean existeVM = Boolean.TRUE; //Valida se existem VM's dentro do host;
			
			while(existeVM) {
				Space result = (Space) space.take(template, null, TEMPO_MAX_REMOCAO);
				
				if(result != null)
					hostList.add(result);
				else
					existeVM = Boolean.FALSE;
			}
			
			if(!hostList.isEmpty() && hostList.size() == 1 && hostList.get(0).vm != null) {
				devolverTemplatesParaOEspaco(hostList, space);
					
				System.out.println("Ainda existe " + hostList.size() + " VM dentro do Host. Favor, excluí-la antes de tentar excluir o Host.");
				JOptionPane.showMessageDialog(tela, "Ainda existe " + hostList.size() + " VM dentro do Host. Favor, excluí-la antes de tentar excluir o Host.");					
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");
				
			} else if(!hostList.isEmpty() && hostList.size() > 1) {
				devolverTemplatesParaOEspaco(hostList, space);
				
				System.out.println("Ainda existem " + hostList.size() + " VM's dentro do Host. Favor, excluir todos eles antes de tentar excluir o Host.");
				JOptionPane.showMessageDialog(tela, "Ainda existem " + hostList.size() + " VM's dentro do Host. Favor, excluir todos eles antes de tentar excluir o Host.");
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");
				
			} else {
				//Devolve o template sem o host
				template.host = null;
				space.write(template, null, TEMPO_VIDA_TUPLA);
				
				JOptionPane.showMessageDialog(tela, "Host excluído com sucesso!");
			}
			
		} else {
			System.out.println("O Host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O Host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host inexistente");
		}
	}
	
	public static void removeVM(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partes = caminhoComNome != null
				? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM']
				: new String[]{};

		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_VM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de VM válido, no formato 'nuvem.host.vm' (sem aspas). Ex.: nuvem1.host2.vm3, nuvem2.host2.vm2, ...");
			throw new FalhaException("Nome VM inválido");
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		VirtualMachine vm = new VirtualMachine();
		vm.nome = partes[2];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		template.vm = vm;
		
		if(ValidateService.existeVM(template, space)) {
			List<Space> vmList = new ArrayList<Space>();
			
			Boolean existeProcesso = Boolean.TRUE; //Valida se existem Processos dentro da VM;
			
			while(existeProcesso) {
				Space result = (Space) space.take(template, null, TEMPO_MAX_REMOCAO);
				
				if(result != null)
					vmList.add(result);
				else
					existeProcesso = Boolean.FALSE;
			}
			
			if(!vmList.isEmpty() && vmList.size() == 1 && vmList.get(0).processo != null) {
				devolverTemplatesParaOEspaco(vmList, space);
					
				System.out.println("Ainda existe " + vmList.size() + " processo dentro da VM. Favor, excluí-lo antes de tentar excluir a VM.");
				JOptionPane.showMessageDialog(tela, "Ainda existe " + vmList.size() + " processo dentro da VM. Favor, excluí-lo antes de tentar excluir a VM.");					
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");

			} else if(!vmList.isEmpty() && vmList.size() > 1) {
				devolverTemplatesParaOEspaco(vmList, space);
				
				System.out.println("Ainda existem " + vmList.size() + " processos dentro da VM. Favor, excluir todos eles antes de tentar excluir a VM.");
				JOptionPane.showMessageDialog(tela, "Ainda existem " + vmList.size() + " processos dentro da VM. Favor, excluir todos eles antes de tentar excluir a VM.");
				throw new FalhaException("Ainda há dados a serem excluídos antes desta exclusão");
				
			} else {
				//Devolve o template sem a VM
				template.vm = null;
				space.write(template, null, TEMPO_VIDA_TUPLA);
				
				JOptionPane.showMessageDialog(tela, "VM excluída com sucesso!");
			}
			
		} else {
			System.out.println("A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("VM inexistente");
		}
	}
	
	public static void removeProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.")
						: new String[]{};
		
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_PROCESSO)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de processo válido, no formato 'nuvem.host.vm.processo' (sem aspas). Ex.: nuvemA.hostA.vmB.processoC, ...");
			throw new FalhaException("Nome processo inválido");
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		VirtualMachine vm = new VirtualMachine();
		vm.nome = partes[2];
		
		Processo processo = new Processo();
		processo.nome = partes[3];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		template.vm = vm;
		template.processo = processo;
		
		if(ValidateService.existeProcesso(template, space)) {
			space.take(template, null, TEMPO_MAX_REMOCAO);
			
			//Devolve o template sem o processo
			template.processo = null;
			space.write(template, null, TEMPO_VIDA_TUPLA);
			
			JOptionPane.showMessageDialog(tela, "Processo excluído com sucesso!");
		
		} else {
			System.out.println("Um processo com esse nome não foi encontrado dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Um processo com esse nome não foi encontrado dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Processo inexistente");
		}
	}
	
	private static void devolverTemplatesParaOEspaco(List<Space> tuplas, JavaSpace space) throws RemoteException, TransactionException {
		if(tuplas != null && !tuplas.isEmpty()) {
			for(Space t : tuplas) {
				space.write(t, null, TEMPO_VIDA_TUPLA);
			}
		}
	}
}
