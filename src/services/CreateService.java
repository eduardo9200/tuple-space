package services;

import java.awt.HeadlessException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Space;
import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.VirtualMachine;

public class CreateService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 10_000; //10 seg.

	public static void createNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, TransactionException, HeadlessException, UnusableEntryException, InterruptedException {
		if(!ValidateService.validaPartes(nome, ValidateService.PARTES_NUVEM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido. Ex.: nuvem1, nuvem2, ...");
			return;
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Space template = new Space();
		template.nuvem = nuvem;
		
		if(ValidateService.existeNuvem(template, space)) {
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " já existe no espaço.");
		
		} else {
			template.host = null;
			template.vm = null;
			template.processo = null;
			
			space.write(template, null, TEMPO_VIDA_TUPLA);	
		}
		return;
	}
	
	public static void createHost(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost']
						: new String[]{};
		
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_HOST)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de host válido no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host1, nuvemA.hostB, ...");
			return;
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
		
		} else if(ValidateService.existeHost(template, space)) {
			System.out.println("Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.NUVEM);
		}
		return;
	}
	
	public static void createVM(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM']
						: new String[]{};
		
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_VM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de VM válido, no formato 'nuvem.host.vm' (sem aspas). Ex.: nuvem1.host2.vm3, nuvem2.host2.vm2, ...");
			return;
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
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
		
		} else if(ValidateService.existeVM(template, space)) {
			System.out.println("Uma VM com esse nome já existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Uma VM com esse nome já existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.VM);
		}
		return;
	}
	
	public static void createProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM', 3=>'nomeProcesso']
						: new String[]{};
		
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_PROCESSO)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de processo válido, no formato 'nuvem.host.vm.processo' (sem aspas). Ex.: nuvemA.hostA.vmB.processoC, ...");
			return;
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
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
		
		} else if(!ValidateService.existeVM(template, space)) {
			System.out.println("A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
		
		} else if(ValidateService.existeProcesso(template, space)) {
			System.out.println("Um processo com esse nome já existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Um processo com esse nome já existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.PROCESSO);
		}
	}
	
	/*
	 * Antes de se salvar uma nova tupla, é feita uma verificação que garante que não fiquem lixos armazenados no espaço de tupla.
	 * 
	 * Ex.: ao tentar criar um host dentro de uma nuvem sem host, a tupla original é (nuvemX, null, null, null).
	 * É necessário que essa tupla seja removida e inserida uma nova para ficar (nuvemX, hostX, null, null);
	 * Ao tentar inserir um segundo host dentro dessa mesma nuvem, ao ser dado um take, será verificado que essa nuvem já possui
	 * pelo menos um host, então preciso colocá-la de volta ao espaço e logo depois será escrita a nova tupla que será salva.
	 * 
	 * Se essas verificações não fossem feitas, as novas tuplas seriam inseridas normalmente, mas estaria armazenada
	 * uma tupla do tipo (nuvemX, null, null, null) como lixo.
	 * 
	 * As mesmas ideias acontecem ao tentar inserir VM's e Processos.
	 * */
	private static void insereTuplaNoEspaco(Space template, JavaSpace space, TipoInsercao tipo) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space templateAux = new Space();
		
		switch(tipo) {
			case HOST:
			
				templateAux.nuvem = template.nuvem;
				
				write(template, templateAux, space);
			
				break;
			
			case VM:
			
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				
				write(template, templateAux, space);
			
				break;
			
			case PROCESSO:
			
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				templateAux.processo = template.processo;
				
				write(template, templateAux, space);
			
				break;
			
			case NUVEM:
				break;
		}
	}
	
	private static void write(Space template, Space templateAux, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space result = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
		
		if(result.processo != null)
			space.write(result, null, TEMPO_VIDA_TUPLA); //Devolve a tupla para o espaço
		space.write(template, null, TEMPO_VIDA_TUPLA); //Insere a nova tupla no espaço;
	}
	
	private enum TipoInsercao {
		NUVEM, HOST, VM, PROCESSO;
	}
}
