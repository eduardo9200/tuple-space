package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private JLabel lblInserir;
	private JLabel lblRemover;
	private JLabel lblMover;
	
	private JButton btnNewCloud;
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
	
	private JScrollPane scrollPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
	}
	
	private void initComponents() {
		btnNewCloud = new JButton("Nuvem");
		btnNewCloud.setBounds(46, 67, 89, 23);
		contentPane.add(btnNewCloud);
		
		btnNewHost = new JButton("Host");
		btnNewHost.setBounds(46, 101, 89, 23);
		contentPane.add(btnNewHost);
		
		btnNewVM = new JButton("VM");
		btnNewVM.setBounds(46, 135, 89, 23);
		contentPane.add(btnNewVM);
		
		btnNewProcesso = new JButton("Processo");
		btnNewProcesso.setBounds(46, 169, 89, 23);
		contentPane.add(btnNewProcesso);
		
		lblInserir = new JLabel("Inserir");
		lblInserir.setBounds(46, 42, 46, 14);
		contentPane.add(lblInserir);
		
		lblRemover = new JLabel("Remover");
		lblRemover.setBounds(171, 42, 61, 14);
		contentPane.add(lblRemover);
		
		btnRemoveNuvem = new JButton("Nuvem");
		btnRemoveNuvem.setBounds(171, 67, 89, 23);
		contentPane.add(btnRemoveNuvem);
		
		btnRemoveHost = new JButton("Host");
		btnRemoveHost.setBounds(171, 101, 89, 23);
		contentPane.add(btnRemoveHost);
		
		btnRemoveVM = new JButton("VM");
		btnRemoveVM.setBounds(171, 135, 89, 23);
		contentPane.add(btnRemoveVM);
		
		btnRemoveProcesso = new JButton("Processo");
		btnRemoveProcesso.setBounds(171, 169, 89, 23);
		contentPane.add(btnRemoveProcesso);
		
		lblMover = new JLabel("Mover");
		lblMover.setBounds(294, 42, 46, 14);
		contentPane.add(lblMover);
		
		btnMoverHost = new JButton("Host");
		btnMoverHost.setBounds(294, 82, 89, 23);
		contentPane.add(btnMoverHost);
		
		btnMoverVM = new JButton("VM");
		btnMoverVM.setBounds(294, 116, 89, 23);
		contentPane.add(btnMoverVM);
		
		btnMoverProcesso = new JButton("Processo");
		btnMoverProcesso.setBounds(294, 150, 89, 23);
		contentPane.add(btnMoverProcesso);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 203, 414, 247);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
	}
	
	private void initActions() {
		btnNewCloud.addActionListener(new ActionListener() {
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
	}
	
	private void btnNewCloudActionPerformed(ActionEvent e) {
		System.out.println("Insere Nuvem");
		JOptionPane.showInputDialog(this, "Nome da Nuvem", "Nuvem", JOptionPane.QUESTION_MESSAGE);
	}
	
	private void btnNewHostActionPerformed(ActionEvent e) {
		System.out.println("Insere Host");
		JOptionPane.showInputDialog(this, "Nome do Host", "Host", JOptionPane.QUESTION_MESSAGE);
	}
	
	private void btnNewVMActionPerformed(ActionEvent e) {
		System.out.println("Insere VM");
		JOptionPane.showInputDialog(this, "Nome da VM", "VM", JOptionPane.QUESTION_MESSAGE);
	}
	
	private void btnNewProcessoActionPerformed(ActionEvent e) {
		System.out.println("Insere Processo");
		JOptionPane.showInputDialog(this, "Nome do Processo", "Processo", JOptionPane.QUESTION_MESSAGE);
	}
	
	private void btnRemoveNuvemActionPerformed(ActionEvent e) {
		System.out.println("Remove Nuvem");
	}
	
	private void btnRemoveHostActionPerformed(ActionEvent e) {
		System.out.println("Remove Host");
	}
	
	private void btnRemoveVMActionPerformed(ActionEvent e) {
		System.out.println("Remove VM");
	}
	
	private void btnRemoveProcessoActionPerformed(ActionEvent e) {
		System.out.println("Remove Processo");
	}
	
	private void btnMoverHostActionPerformed(ActionEvent e) {
		System.out.println("Mover Host");
	}
	
	private void btnMoverVMActionPerformed(ActionEvent e) {
		System.out.println("Mover VM");
	}
	
	private void btnMoverProcessoActionPerformed(ActionEvent e) {
		System.out.println("Mover Processo");
	}
}
