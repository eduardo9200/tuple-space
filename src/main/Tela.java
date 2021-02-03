package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exception.FalhaException;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import services.ReloadSpaceService;
import services.Service;
import tupla.Space;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	
	TuplasTableModel tableModel = new TuplasTableModel();
	
	private JPanel contentPane;
	private JPanel panelTabela;
	
	private JLabel lblInserir;
	private JLabel lblRemover;
	private JLabel lblMover;
	private JLabel lblLog;
	private JLabel lblNewLabel;
	private JLabel lblAtualizaTabela;
	private JLabel lblInfo;
	private JLabel lblEntreProcessos;
	
	private JButton btnNewNuvem;
	private JButton btnNewHost;
	private JButton btnNewVM;
	private JButton btnNewProcesso;
	private JButton btnRemoveNuvem;
	private JButton btnRemoveHost;
	private JButton btnRemoveVM;
	private JButton btnRemoveProcesso;
	private JButton btnMoverHost;
	private JButton btnMoverVM;
	private JButton btnMoverProcesso;
	private JButton btnAtualizar;
	private JButton btnNewMessage;
	
	private JScrollPane scrollPane_1;
	private JTable table;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		Tela frame = new Tela();
		frame.setVisible(true);
		frame.instanciaEspacoTupla();
		frame.atualizarTabela();
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
		
		this.habilitaBotoes(false);
	}
	
	private void initComponents() {
		btnNewNuvem = new JButton("Nuvem");
		btnNewNuvem.setBounds(44, 92, 89, 23);
		contentPane.add(btnNewNuvem);
		
		btnNewHost = new JButton("Host");
		btnNewHost.setBounds(44, 126, 89, 23);
		contentPane.add(btnNewHost);
		
		btnNewVM = new JButton("VM");
		btnNewVM.setBounds(44, 160, 89, 23);
		contentPane.add(btnNewVM);
		
		btnNewProcesso = new JButton("Processo");
		btnNewProcesso.setBounds(44, 194, 89, 23);
		contentPane.add(btnNewProcesso);
		
		lblInserir = new JLabel("Inserir");
		lblInserir.setBounds(44, 67, 46, 14);
		contentPane.add(lblInserir);
		
		lblRemover = new JLabel("Remover");
		lblRemover.setBounds(169, 67, 61, 14);
		contentPane.add(lblRemover);
		
		btnRemoveNuvem = new JButton("Nuvem");
		btnRemoveNuvem.setBounds(169, 92, 89, 23);
		contentPane.add(btnRemoveNuvem);
		
		btnRemoveHost = new JButton("Host");
		btnRemoveHost.setBounds(169, 126, 89, 23);
		contentPane.add(btnRemoveHost);
		
		btnRemoveVM = new JButton("VM");
		btnRemoveVM.setBounds(169, 160, 89, 23);
		contentPane.add(btnRemoveVM);
		
		btnRemoveProcesso = new JButton("Processo");
		btnRemoveProcesso.setBounds(169, 194, 89, 23);
		contentPane.add(btnRemoveProcesso);
		
		lblMover = new JLabel("Mover");
		lblMover.setBounds(292, 67, 46, 14);
		contentPane.add(lblMover);
		
		btnMoverHost = new JButton("Host");
		btnMoverHost.setBounds(292, 107, 89, 23);
		contentPane.add(btnMoverHost);
		
		btnMoverVM = new JButton("VM");
		btnMoverVM.setBounds(292, 141, 89, 23);
		contentPane.add(btnMoverVM);
		
		btnMoverProcesso = new JButton("Processo");
		btnMoverProcesso.setBounds(292, 175, 89, 23);
		contentPane.add(btnMoverProcesso);
		
		lblLog = new JLabel("");
		lblLog.setForeground(Color.MAGENTA);
		lblLog.setBounds(10, 41, 414, 20);
		contentPane.add(lblLog);
		
		lblNewLabel = new JLabel("Espa\u00E7o de Tuplas");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 133, 19);
		contentPane.add(lblNewLabel);
		
		btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setBounds(408, 475, 89, 23);
		contentPane.add(btnAtualizar);
		
		panelTabela = new JPanel();
		panelTabela.setBounds(10, 228, 498, 247);
		contentPane.add(panelTabela);
		panelTabela.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 478, 225);
		panelTabela.add(scrollPane_1);
		
		table = new JTable();
		scrollPane_1.setViewportView(table);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		table.setModel(tableModel);
		table.setAutoCreateRowSorter(true);
		
		lblAtualizaTabela = new JLabel("");
		lblAtualizaTabela.setForeground(Color.MAGENTA);
		lblAtualizaTabela.setBounds(20, 475, 193, 23);
		contentPane.add(lblAtualizaTabela);
		
		lblInfo = new JLabel("");
		lblInfo.setBounds(231, 475, 167, 23);
		contentPane.add(lblInfo);
		
		btnNewMessage = new JButton("Nova Msg.");
		btnNewMessage.setBounds(402, 92, 106, 23);
		contentPane.add(btnNewMessage);
		
		lblEntreProcessos = new JLabel("Entre Processos");
		lblEntreProcessos.setBounds(402, 67, 106, 14);
		contentPane.add(lblEntreProcessos);
	}
	
	private void initActions() {
		btnNewNuvem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewCloudActionPerformed(e);
			}
		});
		
		btnNewHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewHostActionPerformed(e);
			}
		});
		
		btnNewVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewVMActionPerformed(e);
			}
		});
		
		btnNewProcesso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewProcessoActionPerformed(e);
			}
		});
		
		btnRemoveNuvem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveNuvemActionPerformed(e);
			}
		});
		
		btnRemoveHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveHostActionPerformed(e);
			}
		});
		
		btnRemoveVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveVMActionPerformed(e);
			}
		});
		
		btnRemoveProcesso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveProcessoActionPerformed(e);
			}
		});
		
		btnMoverHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMoverHostActionPerformed(e);
			}
		});
		
		btnMoverVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMoverVMActionPerformed(e);
			}
		});
		
		btnMoverProcesso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMoverProcessoActionPerformed(e);
			}
		});
		
		btnNewMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewMessageActionPerformed(e);
			}
		});
		
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAtualizarActionPerformed(e);
			}
		});
	}
	
	/*
	 * Busca um espaço de tupla que esteja executando
	 * */
	public void instanciaEspacoTupla() {
		try {
			this.lblLog.setText("Procurando pelo serviço JavaSpace...");
			Lookup finder = new Lookup(JavaSpace.class);
			space = (JavaSpace) finder.getService();
			
			if(space == null) {
				this.lblLog.setText("O serviço JavaSpace não foi encontrado. Encerrando...");
				System.exit(-1);
			}
			
			this.lblLog.setText("O serviço JavaSpace foi encontrado.");
			System.out.println(space);
			this.habilitaBotoes(true);
			
			/*Scanner scanner = new Scanner(System.in);
			
			while(true) {
				System.out.println("Entre com o texto da mensagem (ENTER para sair): ");
				String message = scanner.nextLine();
				
				if(message == null || message.equals("")) {
					System.exit(0);
				}
				
				
			}*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que cria a nuvem no espaço
	 * */
	private void btnNewCloudActionPerformed(ActionEvent e) {
		System.out.println("Inserindo Nuvem");
		String nomeNuvem = JOptionPane.showInputDialog(this, "Nome da Nuvem", "Nuvem", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeNuvem == null || nomeNuvem.isEmpty() || nomeNuvem.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Criando Nuvem...");
			
			Service.createNuvem(nomeNuvem, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Nuvem criada: " + nomeNuvem);
		
		} catch (RemoteException | TransactionException | HeadlessException | UnusableEntryException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha na criação da nuvem");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que cria o host no espaço
	 * */
	private void btnNewHostActionPerformed(ActionEvent e) {
		System.out.println("Insere Host");
		String nomeHost = JOptionPane.showInputDialog(this, "Nome do Host", "Host", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHost == null || nomeHost.isEmpty() || nomeHost.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Criando Host...");
			
			Service.createHost(nomeHost, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Host criado: " + nomeHost);
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha na criação do Host");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que cria a VM no espaço
	 * */
	private void btnNewVMActionPerformed(ActionEvent e) {
		System.out.println("Insere VM");
		String nomeVM = JOptionPane.showInputDialog(this, "Nome da VM", "VM", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVM == null || nomeVM.isEmpty() || nomeVM.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Criando VM...");
			
			Service.createVM(nomeVM, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("VM criada: " + nomeVM);
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha na criação da VM");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que cria o processo no espaço
	 * */
	private void btnNewProcessoActionPerformed(ActionEvent e) {
		System.out.println("Insere Processo");
		String nomeProcesso = JOptionPane.showInputDialog(this, "Nome do Processo", "Processo", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcesso == null || nomeProcesso.isEmpty() || nomeProcesso.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Criando Processo...");
			
			Service.createProcesso(nomeProcesso, space, this);
		
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Processo criado: " + nomeProcesso);
		
		} catch (HeadlessException | RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha na criação do Processo");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que remove a nuvem do espaço
	 * */
	private void btnRemoveNuvemActionPerformed(ActionEvent e) {
		System.out.println("Remove Nuvem");
		
		String nomeNuvem = JOptionPane.showInputDialog(this, "Nome da Nuvem", "Nuvem", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeNuvem == null || nomeNuvem.isEmpty() || nomeNuvem.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Removendo Nuvem...");
			
			Service.removeNuvem(nomeNuvem, space, this);
		
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Nuvem removida: " + nomeNuvem);
		
		} catch (RemoteException | TransactionException | HeadlessException | UnusableEntryException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha ao remover Nuvem...");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que remove o host do espaço
	 * */
	private void btnRemoveHostActionPerformed(ActionEvent e) {
		System.out.println("Remove Host");
		String nomeHost = JOptionPane.showInputDialog(this, "Nome do Host", "Host", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHost == null || nomeHost.isEmpty() || nomeHost.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Removendo Host...");
			
			Service.removeHost(nomeHost, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Host removido: " + nomeHost);
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | HeadlessException | FalhaException e1) {
			this.lblInfo.setText("Falha ao remover Host...");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que remove a VM do espaço
	 * */
	private void btnRemoveVMActionPerformed(ActionEvent e) {
		System.out.println("Remove VM");
		String nomeVM = JOptionPane.showInputDialog(this, "Nome da VM", "VM", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVM == null || nomeVM.isEmpty() || nomeVM.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Removendo VM...");
			
			Service.removeVM(nomeVM, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("VM removida: " + nomeVM);
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | HeadlessException | FalhaException e1) {
			this.lblInfo.setText("Falha ao remover VM");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que remove o processo do espaço
	 * */
	private void btnRemoveProcessoActionPerformed(ActionEvent e) {
		System.out.println("Remove Processo");
		String nomeProcesso = JOptionPane.showInputDialog(this, "Nome do Processo", "Processo", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcesso == null || nomeProcesso.isEmpty() || nomeProcesso.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Removendo Processo...");
			
			Service.removeProcesso(nomeProcesso, space, this);
		
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Processo removido: " + nomeProcesso);
		} catch (HeadlessException | RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha ao remover Processo");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que migra hosts entre nuvens
	 * */
	private void btnMoverHostActionPerformed(ActionEvent e) {
		System.out.println("Mover Host");
		String nomeHostOrigem = JOptionPane.showInputDialog(this, "Nome do host de origem (nomeNuvem.nomeHost)", "Host origem", JOptionPane.QUESTION_MESSAGE);
		String nomeHostDestino = JOptionPane.showInputDialog(this, "Nome da nuvem de destino (nomeNuvem)", "Nuvem destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHostOrigem == null || nomeHostOrigem.isEmpty() || nomeHostOrigem.isBlank() || nomeHostDestino == null || nomeHostDestino.isEmpty() || nomeHostDestino.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Movendo Host...");
			
			Service.moveHost(nomeHostOrigem, nomeHostDestino, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Host movido com sucesso!");
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha ao mover Host");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que migra VM's entre hosts
	 * */
	private void btnMoverVMActionPerformed(ActionEvent e) {
		System.out.println("Mover VM");
		String nomeVMOrigem = JOptionPane.showInputDialog(this, "Nome da VM de origem (nomeNuvem.nomeHost.nomeVM)", "VM origem", JOptionPane.QUESTION_MESSAGE);
		String nomeVMDestino = JOptionPane.showInputDialog(this, "Nome do Host de destino (nomeNuvem.nomeHost)", "Host destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVMOrigem == null || nomeVMOrigem.isEmpty() || nomeVMOrigem.isBlank() || nomeVMDestino == null || nomeVMDestino.isEmpty() || nomeVMDestino.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Movendo VM...");
			
			Service.moveVM(nomeVMOrigem, nomeVMDestino, space, this);
		
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("VM movida com sucesso!");
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha ao mover VM");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que migra processos entre VM's
	 * */
	private void btnMoverProcessoActionPerformed(ActionEvent e) {
		System.out.println("Mover Processo");
		String nomeProcessoOrigem = JOptionPane.showInputDialog(this, "Nome do processo de origem (nomeNuvem.nomeHost.nomeVM.nomeProcesso)", "Processo origem", JOptionPane.QUESTION_MESSAGE);
		String nomeProcessoDestino = JOptionPane.showInputDialog(this, "Nome da VM de destino (nomeNuvem.nomeHost.nomeVM)", "VM destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcessoOrigem == null || nomeProcessoOrigem.isEmpty() || nomeProcessoOrigem.isBlank() || nomeProcessoDestino == null || nomeProcessoDestino.isEmpty() || nomeProcessoDestino.isBlank()) {
			return;
		}
		
		try {
			this.lblInfo.setText("Movendo Processo...");
			
			Service.moveProcesso(nomeProcessoOrigem, nomeProcessoDestino, space, this);
		
			this.lblInfo.setText("");
			this.atualizarTabela();
			System.out.println("Processo movido com sucesso!");
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | HeadlessException | FalhaException e1) {
			this.lblInfo.setText("Falha ao mover Processo");
			e1.printStackTrace();
		}
	}
	
	/*
	 * Realiza a chamada do serviço que troca mensagens entre processos da mesma VM
	 * */
	private void btnNewMessageActionPerformed(ActionEvent e) {
		System.out.println("Nova mensagem entre processos");
		String remetente = JOptionPane.showInputDialog(this, "Processo remetente (nomeNuvem.nomeHost.nomeVM.nomeProcesso)", "Remetente", JOptionPane.QUESTION_MESSAGE);
		String destinatario = JOptionPane.showInputDialog(this, "Processo destinatário (nomeNuvem.nomeHost.nomeVM)", "Destinatário", JOptionPane.QUESTION_MESSAGE);
		String mensagem = JOptionPane.showInputDialog(this, "Mensagem", "Mensagem", JOptionPane.INFORMATION_MESSAGE);
		
		if(remetente == null || remetente.isEmpty() || remetente.isBlank() ||
		   destinatario == null || destinatario.isEmpty() || destinatario.isBlank() ||
		   mensagem == null || mensagem.isEmpty() || mensagem.isBlank()
		) {
			return;
		}
		
		try {
			this.lblInfo.setText("Enviando mensagem...");
			
			Service.sendMessage(remetente, destinatario, mensagem, space, this);
			
			this.lblInfo.setText("");
			this.atualizarTabela();
		
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | FalhaException e1) {
			this.lblInfo.setText("Falha ao enviar mensagem");
			e1.printStackTrace();
		}
	}
	
	private void btnAtualizarActionPerformed(ActionEvent e) {
		this.atualizarTabela();
	}
	
	/*
	 * Atualiza os dados da tabela de acordo com as tuplas existentes no espaço, exceto as tuplas de troca de mensagens
	 * */
	public void atualizarTabela() {
		try {
			this.lblAtualizaTabela.setText("AGUARDE! Atualizando dados...");
			
			tableModel.clearAllRows();
			
			List<Space> todasTuplas = ReloadSpaceService.findAllTuples(space);
			
			if(todasTuplas == null || todasTuplas.isEmpty())
				return;
			
			for(Space tupla : todasTuplas) {
				tableModel.addRow(tupla);
			}
			this.lblAtualizaTabela.setText("Dados atualizados");
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			this.lblAtualizaTabela.setText("Falha ao atualizar dados: " + e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	private void habilitaBotoes(boolean b) {
		this.btnNewNuvem.setEnabled(b);
		this.btnNewHost.setEnabled(b);
		this.btnNewVM.setEnabled(b);
		this.btnNewProcesso.setEnabled(b);
		
		this.btnRemoveNuvem.setEnabled(b);
		this.btnRemoveHost.setEnabled(b);
		this.btnRemoveVM.setEnabled(b);
		this.btnRemoveProcesso.setEnabled(b);
		
		this.btnMoverHost.setEnabled(b);
		this.btnMoverVM.setEnabled(b);
		this.btnMoverProcesso.setEnabled(b);
		
		this.btnNewMessage.setEnabled(b);
		
		this.btnAtualizar.setEnabled(b);
	}
}
