package Googla_lhe;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	private int numberOfFound;
	private String title;
	private int ID;

	public Result(int ID,int numberOfFound, String title){

		this.ID=ID;
		this.numberOfFound=numberOfFound;
		this.title=title;
	}


	public int getNumberOfFound(){

		return numberOfFound;
	}

	public String getTitle(){

		return title;
	}

	public synchronized int getResultID(){

		return ID;
	}


	@Override
	public String toString(){

		return numberOfFound + " - " + title;
	}

}
