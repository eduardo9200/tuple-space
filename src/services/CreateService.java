package services;

import java.awt.HeadlessException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Ceu;
import tupla.Host;
import tupla.Nuvem;
import tupla.Processo;
import tupla.VirtualMachine;

public class CreateService {
	
	private static final long TEMPO_MAX_ESPERA_ESCRITA = 60_000; //60 seconds;

	public static void createNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, TransactionException, HeadlessException, UnusableEntryException, InterruptedException {
		if(!ValidateService.validaPartes(nome, 1)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido. Ex.: nuvem1, nuvem2, ...");
			return;
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Ceu template = new Ceu();
		template.nuvem = nuvem;
		
		if(ValidateService.existeNuvem(template, space)) {
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " já existe no espaço.");
		
		} else {
			template.host = null;
			template.vm = null;
			template.processo = null;
			
			space.write(template, null, TEMPO_MAX_ESPERA_ESCRITA);	
		}
		return;
	}
	
	public static void createHost(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome.split("\\."); //[0=>'nomeNuvem', 1=>'nomeHost']
		
		if(!ValidateService.validaPartes(caminhoComNome, 2)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de host válido no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host1, nuvemA.hostB, ...");
			return;
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		Ceu template = new Ceu();
		template.nuvem = nuvem;
		template.host = host;
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
		
		} else if(ValidateService.existeHost(template, space)) {
			System.out.println("Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
		
		} else {
			space.write(template, null, TEMPO_MAX_ESPERA_ESCRITA);
		}
		return;
	}
	
	public static void createVM(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome.split("\\."); //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM']
		
		if(!ValidateService.validaPartes(caminhoComNome, 3)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de VM válido, no formato 'nuvem.host.vm' (sem aspas). Ex.: nuvem1.host2.vm3, nuvem2.host2.vm2, ...");
			return;
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		VirtualMachine vm = new VirtualMachine();
		vm.nome = partes[2];
		
		Ceu template = new Ceu();
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
			space.write(template, null, TEMPO_MAX_ESPERA_ESCRITA);
		}
		return;
	}
	
	public static void createProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String[] partes = caminhoComNome.split("\\."); //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM', 3=>'nomeProcesso']
		
		if(!ValidateService.validaPartes(caminhoComNome, 4)) {
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
		
		Ceu template = new Ceu();
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
			space.write(template, null, TEMPO_MAX_ESPERA_ESCRITA);
		}
	}
}
