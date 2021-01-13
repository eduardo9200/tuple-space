package tupla;

import net.jini.core.entry.Entry;

public class Ceu implements Entry {

	private static final long serialVersionUID = 1L;
	
	public Nuvem nuvem;
	public Host host;
	public VirtualMachine vm;
	public Processo processo;
	
	public Ceu() { }
}
