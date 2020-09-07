package Googla_lhe;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client{


	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ArrayList<String> interfaceResults= new ArrayList<String>();



	public Client() throws ClassNotFoundException, IOException {

		ClientGUI clientGUI = new ClientGUI(this);

		connectToServer();

	}


	public void getInterFaceText(String str) throws IOException { //When Button Search is Clicked, sends a SearchMessage to Server(Client Handler)
		SearchMessage searchWord = new SearchMessage(str);
		out.writeObject(searchWord);
		out.flush();
		out.reset();
	}



	public void sendTitleToServer(String str) throws IOException {//When Button Search is Clicked, sends a ReadMessage to Server(Client Handler)

		ReadMessage readMessage= new ReadMessage(str);
		out.writeObject(readMessage);
		out.flush();
		out.reset();

	}


	public void connectToServer() throws IOException, ClassNotFoundException { //Function to Connect to Server

		Socket socket = new Socket(InetAddress.getByName(null), Server.PORT);
		System.out.println("Endereço: " + InetAddress.getByName(null));
		try {
			System.out.println("Client ligado!");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			sendID();
			

		} catch (IOException e) {
			System.out.println("Client Falhou");
			
			socket.close();
			out.close();
			in.close();
			connectToServer();
		}
	}


	public ArrayList<String> getResultsFromServer() throws ClassNotFoundException, IOException {

		Object ob= in.readObject();

		if(ob instanceof ArrayList){	

			interfaceResults = (ArrayList<String>) ob;

		}
		return interfaceResults;

	}

	public String getNewsBody() throws ClassNotFoundException, IOException{

		Object ob=in.readObject();
		String body="";

		if(ob instanceof String){
			body= (String) ob;
		}
		return body;

	} 

	public void sendID() throws IOException, ClassNotFoundException{

		String id= "I'm a Client";
		out.writeObject(id);
		out.flush();
		out.reset();
	}





	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {

		Client client = new Client();

	}
}
