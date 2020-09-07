package Googla_lhe;

import java.io.Serializable;

public class ReadMessage implements Serializable {


	private static final long serialVersionUID = 1L;
	private String title;

	public ReadMessage(String title){

		this.title=title;
	}

	public String getTitle(){

		return title;

	}
}
