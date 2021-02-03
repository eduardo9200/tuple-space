package services;

import java.awt.HeadlessException;
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

public class CreateService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 10_000; //10 seg.

	public static void createNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, TransactionException, HeadlessException, UnusableEntryException, InterruptedException, FalhaException {
		if(!ValidateService.validaPartes(nome, ValidateService.PARTES_NUVEM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome válido. Ex.: nuvem1, nuvem2, ...");
			throw new FalhaException("Nome nuvem inválido");
		}
		
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Space template = new Space();
		template.nuvem = nuvem;
		
		if(ValidateService.existeNuvem(template, space)) {
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " já existe no espaço.");
			throw new FalhaException("Nuvem duplicada");
		
		} else {
			template.host = null;
			template.vm = null;
			template.processo = null;
			
			space.write(template, null, TEMPO_VIDA_TUPLA);
			JOptionPane.showMessageDialog(tela, "Nuvem criada com sucesso!");
		}
		return;
	}
	
	/**
	 * 
	 * @param caminhoComNome
	 * @param space
	 * @param tela
	 */
	public static void createHost(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost']
						: new String[]{};
		
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_HOST)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de host válido no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host1, nuvemA.hostB, ...");
			throw new FalhaException("Nome host inválido");
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
			throw new FalhaException("Nuvem inexistente");
		
		} else if(ValidateService.existeHost(template, space)) {
			System.out.println("Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Já existe um host com esse nome dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host duplicado");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.HOST);
			JOptionPane.showMessageDialog(tela, "Host criado com sucesso!");
		}
		return;
	}
	
	public static void createVM(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
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
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			throw new FalhaException("Nuvem inexistente");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host inexistente");
		
		} else if(ValidateService.existeVM(template, space)) {
			System.out.println("Uma VM com esse nome já existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Uma VM com esse nome já existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("VM duplicada");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.VM);
			JOptionPane.showMessageDialog(tela, "VM criada com sucesso!");
		}
		return;
	}
	
	public static void createProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM', 3=>'nomeProcesso']
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
		
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " não foi encontrada ou não existe");
			throw new FalhaException("Nuvem inexistente");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " não foi encontrado ou não existe dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host inexistente");
		
		} else if(!ValidateService.existeVM(template, space)) {
			System.out.println("A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "A VM " + vm.nome + " não foi encontrada ou não existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("VM inexistente");
		
		} else if(ValidateService.existeProcesso(template, space)) {
			System.out.println("Um processo com esse nome já existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Um processo com esse nome já existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Processo duplicado");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.PROCESSO);
			JOptionPane.showMessageDialog(tela, "Processo criado com sucesso!");
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
	public static void insereTuplaNoEspaco(Space template, JavaSpace space, TipoInsercao tipo) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space templateAux = new Space();
		
		switch(tipo) {
			case HOST:
				templateAux.nuvem = template.nuvem;
				
				Space result1 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				if(result1.host != null)
					space.write(result1, null, TEMPO_VIDA_TUPLA); //Devolve a tupla removida para o espaço, caso parâmetro != null. Se parâmetro = null, é descartada.
				space.write(template, null, TEMPO_VIDA_TUPLA); //Insere a nova tupla no espaço;
			
				break;
			
			case VM:
			
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				
				Space result2 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				if(result2.vm != null)
					space.write(result2, null, TEMPO_VIDA_TUPLA); //Devolve a tupla removida para o espaço, caso parâmetro != null. Se parâmetro = null, é descartada.
				space.write(template, null, TEMPO_VIDA_TUPLA); //Insere a nova tupla no espaço;
			
				break;
			
			case PROCESSO:
			
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				templateAux.vm = template.vm;
				
				Space result3 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				if(result3.processo != null) {					
					space.write(result3, null, TEMPO_VIDA_TUPLA); //Devolve a tupla removida para o espaço, caso parâmetro != null. Se parâmetro = null, é descartada.
					criaThread(result3, space);
				}
				
				space.write(template, null, TEMPO_VIDA_TUPLA); //Insere a nova tupla no espaço;
				criaThread(template, space);
				
				break;
			
			case NUVEM:
				break;
		}
	}
	
	private static void criaThread(Space template, JavaSpace space) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					Mensagem mensagem = new Mensagem();
					mensagem.destinatario = template;
					
					try {
						Mensagem result = (Mensagem) space.take(mensagem, null, Long.MAX_VALUE);
						
						if(result == null) {
							System.out.println("Sem mensagens recebidas");
							return;
						}
						
						Space remetente = result.remetente;
						
						if(MessagingService.pertenceAMesmaVM(remetente, mensagem.destinatario)) {
							System.out.println("Mensagem recebida: " + result.mensagem);
							
							//Remove para atualizar o campo mensagem do processo.
							space.take(template, null, TEMPO_MAX_LEITURA);
							
							//Atualiza o campo mensagem
							Processo processo = new Processo();
							processo.nome = template.processo.nome;
							processo.mensagem = result.mensagem;
							
							template.processo = processo;
							
							//Reinsere a tupla removida com a mensagem atualizada
							space.write(template, null, TEMPO_VIDA_TUPLA);
						}
						
					} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public enum TipoInsercao {
		NUVEM, HOST, VM, PROCESSO;
	}
}
