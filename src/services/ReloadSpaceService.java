package services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import tupla.Space;

public class ReloadSpaceService {

	public static List<Space> findAllTuples(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		List<Space> tuplas = new ArrayList<Space>();
		
		Space template = new Space();
		
		boolean existeNuvem = true;
		
		while(existeNuvem) {
			Space tupla = (Space) space.take(template, null, 5_000);
			
			if(tupla != null)
				tuplas.add(tupla);
			else
				existeNuvem = false;
		}
		
		for(Space tupla : tuplas) {
			space.write(tupla, null, Lease.FOREVER);
		}
		
		return tuplas;
	}
}
