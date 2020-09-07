package Googla_lhe;

import java.io.Serializable;

public class Noticia implements Serializable{


	private static final long serialVersionUID = 1L;
	private String title;
	private String body;

	public Noticia(String title, String body){

		this.title=title;
		this.body=body;
	}


	public void setTitle(String title){

		this.title=title;


	}

	public String getTitle(){

		return title;

	}

	public void setBody(String body){

		this.body=body;

	}

	public String getBody(){

		return body;

	}


	public String toString(){

		return "Título: " + title + " / Body: " + body ;
	}
}
