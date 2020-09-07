package Googla_lhe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class Server extends Thread{

	public static int PORT=2911;
	private ServerSocket serverSocket;
	private ArrayList<Noticia> allNewsList = new ArrayList<Noticia>();
	private ArrayList<Task> taskList = new ArrayList<Task>();
	private ArrayList<Result> resultsList= new ArrayList<Result>();
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int ClientID=0;


	public Server() throws ClassNotFoundException, IOException {

		ReadNewsFile();
		buildServer();

	}

	public void ReadNewsFile() { //Read every ".txt" file in Folder and Transform it into a News!

		File news = new File("news29out");
		File[] filelist = news.listFiles();
		Scanner input;
		String title = "";
		String body = "";

		for (File file : filelist) {
			body="";
			try {
				input = new Scanner(file, "UTF-8");
				title = input.nextLine();

				while (input.hasNextLine()) {

					body += input.nextLine()+"/n";
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Noticia noticia = new Noticia(title, body+"/n");
			allNewsList.add(noticia);
		}
	}


	public void buildServer() throws IOException, ClassNotFoundException{ //Function that Builds The Server

		serverSocket= new ServerSocket(PORT);
		System.out.println("Lançou Server Socket: " + serverSocket);

		try{
			while(true){
				Socket socket= serverSocket.accept();
				in= new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				treatConnecter(in, out);
			}
		}catch(IOException e){
			System.out.println("Server Falhou");

		}finally{

			serverSocket.close();
			in.close();
			out.close();
		}
	}



	public void treatConnecter(ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException, IOException{

//If the Connecter is  Client, Creates a ClienHandler, else Creates a WorkerHandler
		Object ob= in.readObject();	
		if(ob instanceof String){
			String n= (String) ob;

			if(n.equals("I'm a Client")){
				ClientHandler clientHandler=  new ClientHandler(ClientID,this, out, in);
				ClientID++;
				Thread t= new Thread(clientHandler);
				t.start();
			}

			else if(n.equals("I'm a Worker")){	
				WorkerHandler workerHandler= new WorkerHandler(this, out, in);
				Thread t= new Thread(workerHandler);
				t.start();
			}
		}	
	}
	public synchronized void addTasks(Task task){

		taskList.add(task);
		notifyAll();
	}

	public synchronized Task getTask() throws InterruptedException{ //Used To WorkerHandler get Tasks, when there's at least One

		while(true){
			while(taskList.size()==0){
				System.out.println("Inicio do Wait do WorkerHandler Para Receber a Tarefa!");
				wait();
				System.out.println("Fim do Wait do WorkerHandler Para Receber a Tarefa!");
			}
			Task task= taskList.get(0);
			taskList.remove(0);

			return task;
		}
	}

	public synchronized ArrayList<Noticia> getNewsList(){ // returns the List of All News in the Folder

		return allNewsList;
	}


	public synchronized void addTaskResult(Result result){

		resultsList.add(result);
		notifyAll();

	}



	public synchronized int numberOfResultsToClient(ClientHandler c){ //Counts the Number of Results of the given Client in the List of All Results

		int count=0;

		ListIterator<Result> itr= resultsList.listIterator();

		while(itr.hasNext()){

			Result result= itr.next();

			if(result.getResultID()==c.getID()){
				count++;
			}
		}
		return count;
	}


	public synchronized ArrayList<Result> allResultsReady(ClientHandler c) throws InterruptedException{ //Only Returns when the all Results 
																										//of the given ClienHandler are Ready!			
		while(true){
			while(numberOfResultsToClient(c)<c.getTasksSize()){
				wait();
			}

			return resultsList;
		}
	}

	public synchronized void removeClientResults(ClientHandler c){//Removes all Results of the Given Client

		ListIterator<Result> itr= resultsList.listIterator();

		while(itr.hasNext()){

			Result result= itr.next();
			if(result.getResultID()==c.getID()){
				itr.remove();
			}
		}
	}


	public static void main(String[]args) throws ClassNotFoundException, IOException, InterruptedException{

		Server server= new Server();

	}
}
