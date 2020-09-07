package Googla_lhe;

import java.io.Serializable;


public class SearchMessage  implements Serializable{


	private static final long serialVersionUID = 1L;
	private String mensagem;

	public SearchMessage(String msg) {
		this.mensagem=msg;
	}

	public String getMessage(){
		return mensagem;
	}
}
