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

/*
 * Classe responsável por remover tuplas existentes do espaço
 * */
public class RemoveService {
	
	public static final long TEMPO_MAX_REMOCAO = 10_000; //10 seg.
	public static final long TEMPO_VIDA_TUPLA= Lease.FOREVER;

	/*
	 * Remove uma nuvem do espaço
	 * 
	 * @param nome nome da nuvem no formato nomeNuvem
	 * @param space espaço onde estão as tuplas
	 * @param tela referência à tela do sistema
	 * */
	public static void removeNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Valida o nome da nuvem
		if(!ValidateService.validaPartes(nome, ValidateService.PARTES_NUVEM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido. Ex.: nuvem1, nuvem2, ...");
			throw new FalhaException("Nome nuvem inválido");
		}
		
		//Cria template
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Space template = new Space();
		template.nuvem = nuvem;
		
		//Se a nuvem existe e ela não possui hosts, então poderá ser removida do espaço. Caso contrário, será lançado erro.
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
			
			//Se ainda existirem hosts na nuvem, então ela é devolvida para o espaço, já que havia sido removida através do take
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
	
	/*
	 * Remove um host de uma nuvem
	 * 
	 * @param caminhoComNome nome do host no formato nomeNuvem.nomeHost
	 * @param space espaço onde estão as tuplas
	 * @param tela referência à tela do sistema
	 * */
	public static void removeHost(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o nome para separar os nomes da nuvem e do host
		String[] partes = caminhoComNome != null
				? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost']
				: new String[]{};

		//Valida o nome do host para saber se atende ao padrão de nome de host
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_HOST)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de Host válido, no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host2, nuvem2.host2, ...");
			throw new FalhaException("Nome host inválido");
		}
		
		//Cria o template
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		
		//Se o host existe e não possui VM's, então ele é removido do espaço. Caso contrário, será lançado um erro.
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
			
			//Caso ainda hajam VM's, os hosts são devolvidos para o espaço, já que haviam sido removidos com o take
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
	
	/*
	 * Remove uma VM do espaço
	 * 
	 * @param caminhoComNome nome da VM no formato nomeNuvem.nomeHost.nomeVM
	 * @param space espaço onde estão as tuplas
	 * @param tela referência à tela do sistema
	 * */
	public static void removeVM(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o nome da VM
		String[] partes = caminhoComNome != null
				? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM']
				: new String[]{};

		//Valida o nome da VM para saber se ela atende ao padrão do nome de VM
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_VM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de VM válido, no formato 'nuvem.host.vm' (sem aspas). Ex.: nuvem1.host2.vm3, nuvem2.host2.vm2, ...");
			throw new FalhaException("Nome VM inválido");
		}
		
		//Cria o template
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
		
		//Se existir VM e ela não possuir processos, então será removida do espaço. Caso contrário, será lançado um erro.
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
			
			//Se existirem processos na VM, então ela será devolvida ao espaço, já que havia sido removida com o take
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
	
	/*
	 * Remove um processo do espaço de tupla
	 * 
	 * @param caminhoComNome nome do processo no formato nomeNuvem.nomeHost.nomeVM.nomeProcesso
	 * @param space espaço onde estão as tuplas
	 * @param tela referência à tela do sistema
	 * */
	public static void removeProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o nome do processo
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.")
						: new String[]{};
		
		//Valida o nome do processo para saber se atende ao padrão do nome do processo
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_PROCESSO)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de processo válido, no formato 'nuvem.host.vm.processo' (sem aspas). Ex.: nuvemA.hostA.vmB.processoC, ...");
			throw new FalhaException("Nome processo inválido");
		}
		
		//Cria o template
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
		
		//Se existe processo, então ele será removido. Caso contrário, é lançado erro.
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
	
	/*
	 * Devolve uma lista de templates previamente removidos para o espaço de tuplas.
	 * 
	 * @param tuplas a lista de tuplas do tipo Space a ser devolvida para o espaço
	 * @param space o espaço de tuplas que receberá a lista de tuplas
	 * */
	private static void devolverTemplatesParaOEspaco(List<Space> tuplas, JavaSpace space) throws RemoteException, TransactionException {
		if(tuplas != null && !tuplas.isEmpty()) {
			for(Space t : tuplas) {
				space.write(t, null, TEMPO_VIDA_TUPLA);
			}
		}
	}
}
