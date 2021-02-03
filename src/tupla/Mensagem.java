package tupla;

import net.jini.core.entry.Entry;

public class Mensagem implements Entry {

	private static final long serialVersionUID = -7859766344840381026L;
	
	public Space remetente;
	public Space destinatario;
	public String mensagem;
	
	public Mensagem() { }
}
