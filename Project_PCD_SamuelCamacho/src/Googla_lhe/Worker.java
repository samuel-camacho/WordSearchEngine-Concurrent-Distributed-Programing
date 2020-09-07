package Googla_lhe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public class Worker implements Runnable{


	private ObjectInputStream in;
	private ObjectOutputStream out;



	public Worker() throws UnknownHostException, IOException{

		connectToServer();

	}


	@Override
	public void run() {
		while(true){
			try {
				sendRequest();	
				Object ob= in.readObject();

				if(ob instanceof Task ){
					Task task= (Task) ob;
					out.writeObject(numberOfFound(task.getNoticiaTask(), task.getTaskSearchWord()));
					out.flush();
					out.reset();
				}
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Falhou Envio de Index's");
				try {
					connectToServer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//break;
			}
		}
	}




	public void sendID() throws IOException{


		String id= "I'm a Worker";
		out.writeObject(id);
		out.flush();
		out.reset();
		
		

	}


	public void connectToServer() throws UnknownHostException, IOException{

		Socket socket = new Socket(InetAddress.getByName(null), Server.PORT);
		System.out.println("Endereço: " + InetAddress.getByName(null));

		try {
			System.out.println("Worker ligado!");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			sendID();

		} catch (IOException e) {
			System.out.println("Worker Falhou");

			socket.close();
			in.close();
			out.close();
		}
	}



	public void sendRequest() throws IOException{ //Worker is always asking for Tasks to Analyze

		String get= "Give me Tasks";

		out.writeObject(get);
		out.flush();
		out.reset();

	}




	public ArrayList<Integer> numberOfFound(Noticia noticia, String str){ // Returns an ArrayList with the Indexe's of Searched Wprd
		ArrayList<Integer> resultsList= new ArrayList<Integer>();
		int lastIndex = 0;

		while(lastIndex != -1){
			lastIndex = noticia.getTitle().indexOf(str,lastIndex);

			if(lastIndex != -1){
				resultsList.add(lastIndex);
				lastIndex += str.length();
			}
		}
		lastIndex=0;

		while(lastIndex != -1){
			lastIndex = noticia.getBody().indexOf(str,lastIndex);

			if(lastIndex != -1){
				resultsList.add(lastIndex);
				lastIndex += str.length();
			}
		}
		lastIndex=0;
		return resultsList;
	}



	public static void main(String[] args) throws UnknownHostException, IOException{

		Worker worker= new Worker();
		Thread t= new Thread(worker);
		t.start();

	}
}


