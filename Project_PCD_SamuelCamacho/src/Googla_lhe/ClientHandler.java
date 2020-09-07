package Googla_lhe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class ClientHandler implements Runnable {



	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Server server;
	private ArrayList<String> resultsToClient= new ArrayList<String>();
	private int tasksSize=0;
	private boolean isReadMessage=false;
	private ReadMessage readMessage=null;
	private int ID;


	public ClientHandler(int ID, Server server, ObjectOutputStream out, ObjectInputStream in){

		this.ID=ID;
		this.server=server;
		this.in=in;
		this.out=out;

	}

	public int getID(){

		return ID;

	}


	public void clientRequest() throws ClassNotFoundException, IOException{ //See's The Type of Request from Client

		Object ob = in.readObject();

		if(ob instanceof SearchMessage){
			SearchMessage msg= (SearchMessage) ob;
			addTasksToServer(msg.getMessage());
		}
		else if( ob instanceof ReadMessage){
			ReadMessage msg= (ReadMessage) ob;
			isReadMessage=true;
			readMessage=msg;
		}
	}


	public void addTasksToServer(String search){ //For every News on Folder, creates a new Task with the Search Word with this ClientHandler ID
		int count=0;	
		for(Noticia noticia: server.getNewsList()){

			Task task= new Task(this.ID,search, noticia);
			server.addTasks(task);
			count++;
		}

		tasksSize=count;
	}


	public synchronized void addResults() throws InterruptedException{ //Add all Final Results to Client, when they're Ready!

		for(Result result: server.allResultsReady(this)){
			if(result.getResultID()==this.ID){
				if(result.getNumberOfFound()>0){
					resultsToClient.add(result.toString());
				}
			}
		}
		server.removeClientResults(this);
	}


	public int getTasksSize(){

		return tasksSize;
	}



	public void sendResultsToClient() throws IOException{

		Collections.sort(resultsToClient);
		Collections.reverse(resultsToClient);
		out.writeObject(resultsToClient);
		out.flush();
		out.reset();
		resultsToClient.clear();

	}

	public String selectedTitle(String str){ //This function will get the News with the given Title and Returns the News Body!

		String body="";
		for(Noticia news: server.getNewsList()){
			if(str.equals(news.getTitle())){
				body= news.getBody();
			}
		}
		return body;
	}

	public void sendNewsBody() throws IOException{

		String body=selectedTitle(readMessage.getTitle());
		out.writeObject(body);
		out.flush();
		out.reset();
	}

	public  void coordinateResultType() throws IOException, InterruptedException{//Coordinate the Result Type to Client
																				 //based on the Type of Message Received	
		if(isReadMessage==true){
			sendNewsBody();
			isReadMessage=false;

		}
		else{
			addResults();
			sendResultsToClient();
		}
	}

	@Override
	public void run() {

		while(true){	
			try {
				clientRequest();
				coordinateResultType();

			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				System.out.println("Erro no Run do ClientHandler");
				break;	
			}
		}
	}
}