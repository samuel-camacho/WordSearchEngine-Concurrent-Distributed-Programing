package Googla_lhe;

import java.io.Serializable;

public class Task implements Serializable {


	private static final long serialVersionUID = 1L;
	private String searchWord;
	private Noticia noticia;
	private int ID;


	public Task(int ID, String searchWord, Noticia noticia){

		this.ID=ID;
		this.searchWord=searchWord;
		this.noticia=noticia;

	}


	public String getTaskSearchWord(){

		return searchWord;

	}

	public Noticia getNoticiaTask(){

		return noticia;

	}

	public synchronized int getTaskID(){

		return ID;
	}
}
