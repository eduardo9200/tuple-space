package commons;

public class Comando {

	public static String INSERT = "insert";
	public static String REMOVE = "remove";
	public static String MIGRATE = "migrate";
	public static String MESSAGE = "message";
	
	private String comando;		//insert, remove, migrate, message;
	private String complemento;	//cloud, host, vm, process, message content
	private String localizadorOrigem; //nuvem.host.vm.processo
	private String localizadorDestino;
	
	public static Comando quebraComando(String commandString) {
		String[] partes = commandString.split(" ");
		
		Comando comando = new Comando();
		comando.setComando(partes[0]);
		comando.setComplemento(partes[1]);
		comando.setLocalizadorOrigem(partes[2]);
		
		if(comando.getComando().equals(MIGRATE) || comando.getComando().equals(MESSAGE)) {
			comando.setLocalizadorDestino(partes[4]);
		}
		
		return comando;
	}
	
	public static String getNome(String caminho) {
		String[] path = caminho.split(".");
		return path[path.length-1];
	}
	
	//Getters and Setters
	public String getComando() {
		return comando;
	}
	
	public void setComando(String comando) {
		this.comando = comando;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getLocalizadorOrigem() {
		return localizadorOrigem;
	}

	public void setLocalizadorOrigem(String localizadorOrigem) {
		this.localizadorOrigem = localizadorOrigem;
	}

	public String getLocalizadorDestino() {
		return localizadorDestino;
	}

	public void setLocalizadorDestino(String localizadorDestino) {
		this.localizadorDestino = localizadorDestino;
	}
}
