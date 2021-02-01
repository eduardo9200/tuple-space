package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import services.ReloadSpaceService;
import services.Service;
import tupla.Nuvem;
import tupla.Space;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	private List<Nuvem> nuvens;
	
	private JPanel contentPane;
	
	private JLabel lblInserir;
	private JLabel lblRemover;
	private JLabel lblMover;
	private JLabel lblLog;
	
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
	
	private JScrollPane scrollPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Tela frame = new Tela();
		frame.setVisible(true);
		frame.instanciaEspacoTupla();
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
	}
	
	private void initComponents() {
		btnNewNuvem = new JButton("Nuvem");
		btnNewNuvem.setEnabled(false);
		btnNewNuvem.setBounds(44, 92, 89, 23);
		contentPane.add(btnNewNuvem);
		
		btnNewHost = new JButton("Host");
		btnNewHost.setEnabled(false);
		btnNewHost.setBounds(44, 126, 89, 23);
		contentPane.add(btnNewHost);
		
		btnNewVM = new JButton("VM");
		btnNewVM.setEnabled(false);
		btnNewVM.setBounds(44, 160, 89, 23);
		contentPane.add(btnNewVM);
		
		btnNewProcesso = new JButton("Processo");
		btnNewProcesso.setEnabled(false);
		btnNewProcesso.setBounds(44, 194, 89, 23);
		contentPane.add(btnNewProcesso);
		
		lblInserir = new JLabel("Inserir");
		lblInserir.setBounds(44, 67, 46, 14);
		contentPane.add(lblInserir);
		
		lblRemover = new JLabel("Remover");
		lblRemover.setBounds(169, 67, 61, 14);
		contentPane.add(lblRemover);
		
		btnRemoveNuvem = new JButton("Nuvem");
		btnRemoveNuvem.setEnabled(false);
		btnRemoveNuvem.setBounds(169, 92, 89, 23);
		contentPane.add(btnRemoveNuvem);
		
		btnRemoveHost = new JButton("Host");
		btnRemoveHost.setEnabled(false);
		btnRemoveHost.setBounds(169, 126, 89, 23);
		contentPane.add(btnRemoveHost);
		
		btnRemoveVM = new JButton("VM");
		btnRemoveVM.setEnabled(false);
		btnRemoveVM.setBounds(169, 160, 89, 23);
		contentPane.add(btnRemoveVM);
		
		btnRemoveProcesso = new JButton("Processo");
		btnRemoveProcesso.setEnabled(false);
		btnRemoveProcesso.setBounds(169, 194, 89, 23);
		contentPane.add(btnRemoveProcesso);
		
		lblMover = new JLabel("Mover");
		lblMover.setBounds(292, 67, 46, 14);
		contentPane.add(lblMover);
		
		btnMoverHost = new JButton("Host");
		btnMoverHost.setEnabled(false);
		btnMoverHost.setBounds(292, 107, 89, 23);
		contentPane.add(btnMoverHost);
		
		btnMoverVM = new JButton("VM");
		btnMoverVM.setEnabled(false);
		btnMoverVM.setBounds(292, 141, 89, 23);
		contentPane.add(btnMoverVM);
		
		btnMoverProcesso = new JButton("Processo");
		btnMoverProcesso.setEnabled(false);
		btnMoverProcesso.setBounds(292, 175, 89, 23);
		contentPane.add(btnMoverProcesso);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 228, 414, 247);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		lblLog = new JLabel("");
		lblLog.setForeground(Color.MAGENTA);
		lblLog.setBounds(10, 41, 414, 20);
		contentPane.add(lblLog);
		
		JLabel lblNewLabel = new JLabel("Espa\u00E7o de Tuplas");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 133, 19);
		contentPane.add(lblNewLabel);
		
		btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setBounds(335, 486, 89, 23);
		contentPane.add(btnAtualizar);
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
		
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAtualizarActionPerformed(e);
			}
		});
	}
	
	private void instanciaEspacoTupla() {
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
			this.habilitaBotoes();
			
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
	
	private void btnNewCloudActionPerformed(ActionEvent e) {
		System.out.println("Inserindo Nuvem");
		String nomeNuvem = JOptionPane.showInputDialog(this, "Nome da Nuvem", "Nuvem", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeNuvem == null || nomeNuvem.isEmpty() || nomeNuvem.isBlank()) {
			return;
		}
		
		try {
			Service.createNuvem(nomeNuvem, space, this);
			this.nuvens.add(new Nuvem(nomeNuvem));
			System.out.println("Nuvem criada: " + nomeNuvem);
		} catch (RemoteException | TransactionException | HeadlessException | UnusableEntryException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnNewHostActionPerformed(ActionEvent e) {
		System.out.println("Insere Host");
		String nomeHost = JOptionPane.showInputDialog(this, "Nome do Host", "Host", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHost == null || nomeHost.isEmpty() || nomeHost.isBlank()) {
			return;
		}
		
		try {
			Service.createHost(nomeHost, space, this);
			System.out.println("Host criado: " + nomeHost);
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnNewVMActionPerformed(ActionEvent e) {
		System.out.println("Insere VM");
		String nomeVM = JOptionPane.showInputDialog(this, "Nome da VM", "VM", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVM == null || nomeVM.isEmpty() || nomeVM.isBlank()) {
			return;
		}
		
		try {
			Service.createVM(nomeVM, space, this);
			System.out.println("VM criada: " + nomeVM);
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnNewProcessoActionPerformed(ActionEvent e) {
		System.out.println("Insere Processo");
		String nomeProcesso = JOptionPane.showInputDialog(this, "Nome do Processo", "Processo", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcesso == null || nomeProcesso.isEmpty() || nomeProcesso.isBlank()) {
			return;
		}
		
		try {
			Service.createProcesso(nomeProcesso, space, this);
			System.out.println("Processo criado: " + nomeProcesso);
		} catch (HeadlessException | RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnRemoveNuvemActionPerformed(ActionEvent e) {
		System.out.println("Remove Nuvem");
		
		String nomeNuvem = JOptionPane.showInputDialog(this, "Nome da Nuvem", "Nuvem", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeNuvem == null || nomeNuvem.isEmpty() || nomeNuvem.isBlank()) {
			return;
		}
		
		try {
			Service.removeNuvem(nomeNuvem, space, this);
			this.nuvens.remove(new Nuvem(nomeNuvem));
			System.out.println("Nuvem removida: " + nomeNuvem);
		} catch (RemoteException | TransactionException | HeadlessException | UnusableEntryException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnRemoveHostActionPerformed(ActionEvent e) {
		System.out.println("Remove Host");
		String nomeHost = JOptionPane.showInputDialog(this, "Nome do Host", "Host", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHost == null || nomeHost.isEmpty() || nomeHost.isBlank()) {
			return;
		}
		
		try {
			Service.removeHost(nomeHost, space, this);
			System.out.println("Host removido: " + nomeHost);
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnRemoveVMActionPerformed(ActionEvent e) {
		System.out.println("Remove VM");
		String nomeVM = JOptionPane.showInputDialog(this, "Nome da VM", "VM", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVM == null || nomeVM.isEmpty() || nomeVM.isBlank()) {
			return;
		}
		
		try {
			Service.removeVM(nomeVM, space, this);
			System.out.println("VM removida: " + nomeVM);
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnRemoveProcessoActionPerformed(ActionEvent e) {
		System.out.println("Remove Processo");
		String nomeProcesso = JOptionPane.showInputDialog(this, "Nome do Processo", "Processo", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcesso == null || nomeProcesso.isEmpty() || nomeProcesso.isBlank()) {
			return;
		}
		
		try {
			Service.removeProcesso(nomeProcesso, space, this);
			System.out.println("Processo removido: " + nomeProcesso);
		} catch (HeadlessException | RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnMoverHostActionPerformed(ActionEvent e) {
		System.out.println("Mover Host");
		String nomeHostOrigem = JOptionPane.showInputDialog(this, "Nome do host de origem", "Host origem", JOptionPane.QUESTION_MESSAGE);
		String nomeHostDestino = JOptionPane.showInputDialog(this, "Nome do host de destino", "Host destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeHostOrigem == null || nomeHostOrigem.isEmpty() || nomeHostOrigem.isBlank() || nomeHostDestino == null || nomeHostDestino.isEmpty() || nomeHostDestino.isBlank()) {
			return;
		}
		
		try {
			Service.moveHost(nomeHostOrigem, nomeHostDestino, space, this);
			System.out.println("Host movido com sucesso!");
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnMoverVMActionPerformed(ActionEvent e) {
		System.out.println("Mover VM");
		String nomeVMOrigem = JOptionPane.showInputDialog(this, "Nome da VM de origem", "VM origem", JOptionPane.QUESTION_MESSAGE);
		String nomeVMDestino = JOptionPane.showInputDialog(this, "Nome da VM de destino", "VM destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeVMOrigem == null || nomeVMOrigem.isEmpty() || nomeVMOrigem.isBlank() || nomeVMDestino == null || nomeVMDestino.isEmpty() || nomeVMDestino.isBlank()) {
			return;
		}
		
		try {
			Service.moveHost(nomeVMOrigem, nomeVMDestino, space, this);
			System.out.println("VM movida com sucesso!");
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnMoverProcessoActionPerformed(ActionEvent e) {
		System.out.println("Mover Processo");
		String nomeProcessoOrigem = JOptionPane.showInputDialog(this, "Nome do processo de origem", "Processo origem", JOptionPane.QUESTION_MESSAGE);
		String nomeProcessoDestino = JOptionPane.showInputDialog(this, "Nome do processo de destino", "Processo destino", JOptionPane.QUESTION_MESSAGE);
		
		if(nomeProcessoOrigem == null || nomeProcessoOrigem.isEmpty() || nomeProcessoOrigem.isBlank() || nomeProcessoDestino == null || nomeProcessoDestino.isEmpty() || nomeProcessoDestino.isBlank()) {
			return;
		}
		
		try {
			Service.moveHost(nomeProcessoOrigem, nomeProcessoDestino, space, this);
			System.out.println("Processo movido com sucesso!");
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void btnAtualizarActionPerformed(ActionEvent e) {
		try {
			List<Space> todasTuplas = ReloadSpaceService.findAllTuples(nuvens, space);
			
			//todasTuplas.sort();
			
			for(Space tupla : todasTuplas) {
				String modelo =
						new StringBuilder()
						.append("(")
						.append(tupla.nuvem.nome).append(":")
						.append(tupla.host.nome).append(":")
						.append(tupla.vm.nome).append(":")
						.append(tupla.processo.nome)
						.append("<").append(tupla.processo.mensagem).append(">")
						.append(")")
						.toString();
				
				this.textArea.append(modelo + "\n");
			}
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void habilitaBotoes() {
		this.btnNewNuvem.setEnabled(true);
		this.btnNewHost.setEnabled(true);
		this.btnNewVM.setEnabled(true);
		this.btnNewProcesso.setEnabled(true);
		
		this.btnRemoveNuvem.setEnabled(true);
		this.btnRemoveHost.setEnabled(true);
		this.btnRemoveVM.setEnabled(true);
		this.btnRemoveProcesso.setEnabled(true);
		
		this.btnMoverHost.setEnabled(true);
		this.btnMoverVM.setEnabled(true);
		this.btnMoverProcesso.setEnabled(true);
	}
}
