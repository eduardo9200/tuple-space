package services;

import java.awt.HeadlessException;
import java.rmi.RemoteException;

import exception.FalhaException;
import main.Tela;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class Service {
	
	//CREATE METHODS
	public static void createNuvem(String nome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, TransactionException, UnusableEntryException, InterruptedException, FalhaException {
		CreateService.createNuvem(nome, space, tela);
		return;
	}

	public static void createHost(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		CreateService.createHost(caminhoComNome, space, tela);
		return;
	}
	
	public static void createVM(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		CreateService.createVM(caminhoComNome, space, tela);
		return;
	}
	
	public static void createProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		CreateService.createProcesso(caminhoComNome, space, tela);
		return;
	}
	
	//REMOVE METHODS
	public static void removeNuvem(String nome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		RemoveService.removeNuvem(nome, space, tela);
		return;
	}
	
	public static void removeHost(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		RemoveService.removeHost(caminhoComNome, space, tela);
		return;
	}
	
	public static void removeVM(String caminhoComNome, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		RemoveService.removeVM(caminhoComNome, space, tela);
		return;
	}
	
	public static void removeProcesso(String caminhoComNome, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		RemoveService.removeProcesso(caminhoComNome, space, tela);
		return;
	}
	
	//MOVE METHODS
	public static void moveHost(String from, String to, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		MoveService.moveHost(from, to, space, tela);
		return;
	}
	
	public static void moveVM(String from, String to, JavaSpace space, Tela tela) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException, FalhaException {
		MoveService.moveVM(from, to, space, tela);
		return;
	}

	public static void moveProcesso(String from, String to, JavaSpace space, Tela tela) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		MoveService.moveProcesso(from, to, space, tela);
		return;
	}
	
	//MESSAGING METHOD
	public static void sendMessage(String from, String to, String mensagem, JavaSpace space, Tela tela) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException, FalhaException {
		MessagingService.sendMessage(from, to, mensagem, space, tela);
		return;
	}
}
