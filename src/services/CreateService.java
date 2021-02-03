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

/*
 * Classe respons�vel por gerenciar as cria��es das tuplas no espa�o
 * */
public class CreateService {
	
	private static final long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	private static final long TEMPO_MAX_LEITURA = 10_000; //10 seg.

	/*
	 * Cria uma nuvem no espa�o
	 * 
	 * @param nome o nome da nuvem a ser inserida
	 * @param space	o espa�o onde a nuvem ser� criada
	 * @param tela a refer�ncia da tela onde o sistema est� sendo executado
	 * */
	public static void createNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, TransactionException, HeadlessException, UnusableEntryException, InterruptedException, FalhaException {
		if(!ValidateService.validaPartes(nome, ValidateService.PARTES_NUVEM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome v�lido. Ex.: nuvem1, nuvem2, ...");
			throw new FalhaException("Nome nuvem inv�lido");
		}
		
		//Cria template da nuvem
		Nuvem nuvem = new Nuvem();
		nuvem.nome = nome;
		
		Space template = new Space();
		template.nuvem = nuvem;
		
		//Verifica se a nuvem j� existe no espa�o e a adiciona, caso ainda n�o exista
		if(ValidateService.existeNuvem(template, space)) {
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " j� existe no espa�o.");
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
	 * Cria um host no espa�o
	 * 
	 * @param caminhoComNome nome do host no formato nomeNuvem.nomeHost
	 * @param space o espa�o onde o host ser� criado
	 * @param tela refer�ncia da tela onde o sistema est� sendo executado
	 */
	public static void createHost(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o caminho para separar os nomes da nuvem e do host
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost']
						: new String[]{};
		
		//Valida se o caminho recebido atende ao pad�o de nome de host
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_HOST)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de host v�lido no formato 'nuvem.host' (sem aspas). Ex.: nuvem1.host1, nuvemA.hostB, ...");
			throw new FalhaException("Nome host inv�lido");
		}
		
		//Cria o template do host
		Nuvem nuvem = new Nuvem();
		nuvem.nome = partes[0];
		
		Host host = new Host();
		host.nome = partes[1];
		
		Space template = new Space();
		template.nuvem = nuvem;
		template.host = host;
		
		//Faz as devidas valida��es de exist�ncia de nuvem e host e insere este, caso ainda n�o exista na nuvem
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			throw new FalhaException("Nuvem inexistente");
		
		} else if(ValidateService.existeHost(template, space)) {
			System.out.println("J� existe um host com esse nome dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "J� existe um host com esse nome dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host duplicado");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.HOST);
			JOptionPane.showMessageDialog(tela, "Host criado com sucesso!");
		}
		return;
	}
	
	/*
	 * Cria uma VM no espa�o
	 * 
	 * @param caminhoComNome nome da VM no formato nomeNuvem.nomeHost.nomeVM
	 * @param space espa�o onde a VM ser� criada
	 * @param tela refer�ncia � tela onde o sistema est� sendo executado
	 * */
	public static void createVM(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o caminho para separar os nomes da nuvem, host e VM
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM']
						: new String[]{};
		
		//Valida se o caminho recebido atende ao pad�o de nome de VM
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_VM)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de VM v�lido, no formato 'nuvem.host.vm' (sem aspas). Ex.: nuvem1.host2.vm3, nuvem2.host2.vm2, ...");
			throw new FalhaException("Nome VM inv�lido");
		}
		
		//Cria o template da VM
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
		
		//Faz as devidas valida��es de exist�ncia de nuvem, host e VM e insere esta, caso ainda n�o exista no host
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			throw new FalhaException("Nuvem inexistente");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " n�o foi encontrado ou n�o existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " n�o foi encontrado ou n�o existe dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host inexistente");
		
		} else if(ValidateService.existeVM(template, space)) {
			System.out.println("Uma VM com esse nome j� existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Uma VM com esse nome j� existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("VM duplicada");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.VM);
			JOptionPane.showMessageDialog(tela, "VM criada com sucesso!");
		}
		return;
	}
	
	/*
	 * Cria um processo no espa�o
	 * 
	 * @param caminhoComNome nome do processo no formato nomeNuvem.nomeHost.nomeVM.nomeProcesso
	 * @param space espa�o onde o processo ser� criado
	 * @param tela refer�ncia � tela onde o sistema est� sendo executado
	 * */
	public static void createProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		//Quebra o caminho para separar os nomes da nuvem, host, vm e processo
		String[] partes = caminhoComNome != null
						? caminhoComNome.split("\\.") //[0=>'nomeNuvem', 1=>'nomeHost', 2=>'nomeVM', 3=>'nomeProcesso']
						: new String[]{};
		
		//Valida se o caminho recebido atende ao pad�o de nome de processo
		if(!ValidateService.validaPartes(caminhoComNome, ValidateService.PARTES_PROCESSO)) {
			JOptionPane.showMessageDialog(tela, "Digite um nome de processo v�lido, no formato 'nuvem.host.vm.processo' (sem aspas). Ex.: nuvemA.hostA.vmB.processoC, ...");
			throw new FalhaException("Nome processo inv�lido");
		}
		
		//Cria o template do processo
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
		
		//Faz as devidas valida��es de exist�ncia de nuvem, host, VM e processo e insere este, caso ainda n�o exista na VM
		if(!ValidateService.existeNuvem(template, space)) {
			System.out.println("A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			JOptionPane.showMessageDialog(tela, "A nuvem " + nuvem.nome + " n�o foi encontrada ou n�o existe");
			throw new FalhaException("Nuvem inexistente");
		
		} else if(!ValidateService.existeHost(template, space)) {
			System.out.println("O host " + host.nome + " n�o foi encontrado ou n�o existe dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "O host " + host.nome + " n�o foi encontrado ou n�o existe dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Host inexistente");
		
		} else if(!ValidateService.existeVM(template, space)) {
			System.out.println("A VM " + vm.nome + " n�o foi encontrada ou n�o existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "A VM " + vm.nome + " n�o foi encontrada ou n�o existe dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("VM inexistente");
		
		} else if(ValidateService.existeProcesso(template, space)) {
			System.out.println("Um processo com esse nome j� existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			JOptionPane.showMessageDialog(tela, "Um processo com esse nome j� existe dentro da VM " + vm.nome + " dentro do host " + host.nome + " dentro da nuvem " + nuvem.nome);
			throw new FalhaException("Processo duplicado");
		
		} else {
			insereTuplaNoEspaco(template, space, TipoInsercao.PROCESSO);
			JOptionPane.showMessageDialog(tela, "Processo criado com sucesso!");
		}
	}
	
	/*
	 * M�todo respons�vel por adicionar uma tupla no espa�o, ap�s passar por todas as valida��es
	 * 
	 * Antes de se salvar uma nova tupla, � feita uma verifica��o que garante que n�o fiquem lixos armazenados no espa�o de tupla.
	 * 
	 * Ex.: ao tentar criar um host dentro de uma nuvem sem host, a tupla original � do tipo (nuvemX, null, null, null).
	 * � necess�rio que essa tupla seja removida e inserida uma nova para ficar (nuvemX, hostX, null, null);
	 * Ao tentar inserir um segundo host dentro dessa mesma nuvem, ao ser dado um take, ser� verificado que essa nuvem j� possui
	 * pelo menos um host, ent�o preciso coloc�-la de volta ao espa�o e logo depois ser� escrita a nova tupla que ser� salva.
	 * 
	 * Se essas verifica��es n�o fossem feitas, as novas tuplas seriam inseridas normalmente, mas estaria armazenada
	 * uma tupla do tipo (nuvemX, null, null, null) como lixo.
	 * 
	 * As mesmas ideias acontecem ao tentar inserir VM's e Processos.
	 * 
	 * @param template template a ser adicionado no espa�o
	 * @param space espa�o onde a tupla ser� adicionada
	 * @param tipoInsercao valor enumerado indicando se est� sendo salvo uma nuvem, um host, uma VM ou um processo
	 * */
	public static void insereTuplaNoEspaco(Space template, JavaSpace space, TipoInsercao tipo) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Space templateAux = new Space();
		
		switch(tipo) {
			case HOST:
				//Cria o template
				templateAux.nuvem = template.nuvem;
				
				//Busca e remove o template criado
				Space result1 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				//Verifica se a tupla removida ser� devolvida ao espa�o ou n�o
				if(result1.host != null)
					space.write(result1, null, TEMPO_VIDA_TUPLA);
				
				//Insere a nova tupla no espa�o
				space.write(template, null, TEMPO_VIDA_TUPLA);
			
				break;
			
			case VM:
				//Cria o template
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				
				//Busca e remove o template criado
				Space result2 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				//Verifica se a tupla removida ser� devolvida ao espa�o ou n�o
				if(result2.vm != null)
					space.write(result2, null, TEMPO_VIDA_TUPLA);
				
				//Insere a nova tupla no espa�o
				space.write(template, null, TEMPO_VIDA_TUPLA);
			
				break;
			
			case PROCESSO:
				//Cria o template
				templateAux.nuvem = template.nuvem;
				templateAux.host = template.host;
				templateAux.vm = template.vm;
				
				//Busca e remove o template criado
				Space result3 = (Space) space.take(templateAux, null, TEMPO_MAX_LEITURA);
				
				//Verifica se a tupla removida ser� devolvida ao espa�o ou n�o
				if(result3.processo != null) {					
					space.write(result3, null, TEMPO_VIDA_TUPLA); //Devolve a tupla removida para o espa�o, caso par�metro != null. Se par�metro = null, � descartada.
					criaThread(result3, space); //Cria uma thread para o processo conseguir ler as mensagens
				}
				
				//Insere a nova tupla no espa�o
				space.write(template, null, TEMPO_VIDA_TUPLA);
				criaThread(template, space); //Cria uma thread para o processo conseguir ler as mensagens
				
				break;
			
			case NUVEM:
				//A rotina de salvar a nuvem j� est� sendo executada dentro do m�todo createNuvem()
				break;
		}
	}
	
	/*
	 * Cria uma thread para o processo ler as mensagens que forem enviadas a ele
	 * 
	 * @param template template onde est�o os dados da mensagem
	 * @param space espa�o a ser consultado pelo processo
	 * */
	private static void criaThread(Space template, JavaSpace space) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					//Cria o template
					Mensagem mensagem = new Mensagem();
					mensagem.destinatario = template;
					
					try {
						//Busca a mensagem e a remove do espa�o, caso encontre
						Mensagem result = (Mensagem) space.take(mensagem, null, Long.MAX_VALUE);
						
						//Caso n�o encontre, finaliza a thread
						if(result == null) {
							System.out.println("Sem mensagens recebidas");
							return;
						}
						
						Space remetente = result.remetente;
						
						//Valida para saber se o remetente e destinat�rio pertencem � mesma VM
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
