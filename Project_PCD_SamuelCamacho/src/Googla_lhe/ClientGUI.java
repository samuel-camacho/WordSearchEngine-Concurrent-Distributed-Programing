package Googla_lhe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

public class ClientGUI extends Thread{

	private JFrame frame;
	private JPanel centerPanel;
	private JPanel searchPanel;
	private JButton searchButton;
	private JTextField textField;
	private JTextArea textArea;
	private DefaultListModel<String> allNews= new DefaultListModel<String>();
	private JList<String> newsList= new JList<String>(allNews);
	private JScrollPane scrollPane;
	private JScrollPane textScroll;
	private Client client;



	public ClientGUI(Client client){

		buildInterface();
		this.client=client;

	}


	public void buildInterface(){ // Builds The Interface for the User

		frame= new JFrame("Googla-lhe");
		searchButton= new JButton("Search");
		textField= new JTextField("",20);
		centerPanel= new JPanel();
		searchPanel= new JPanel();
		textArea= new JTextArea();

		frame.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(1,2));
		searchPanel.setLayout(new FlowLayout());

		searchButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){


				try {

					client.getInterFaceText(textField.getText()); //Button is Clicked, Client transform the String into SearchMessage and send it to Server
					getSearchedNews(); //Function of SwingWorker / Read all Function bellow 

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("Falhou Texto");
					try {
						client.connectToServer();
					} catch (ClassNotFoundException |IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				allNews.removeAllElements();
			}
		});

		scrollPane= new JScrollPane(newsList);

		newsList.addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent e){

				String title= (String) newsList.getSelectedValue();
				String title2= title.substring(4);

				try {
					client.sendTitleToServer(title2); //Client Transforms into ReadMessage and send it to Server
					getSelectedTitle();               //Function of SwingWorker / Read all Function bellow 

				} catch (IOException e1) {

					System.out.println("Falhou Titulo de Notícia");
					try {
						client.connectToServer();
					} catch (ClassNotFoundException | IOException e2 ) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}}
			});

		textScroll= new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		searchPanel.add(textField);
		searchPanel.add(searchButton);
		centerPanel.add(scrollPane);
		centerPanel.add(textScroll);

		frame.add(searchPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(600,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void getSearchedNews(){

		SwingWorker<ArrayList<String>, Void> worker= new SwingWorker<ArrayList<String>, Void>(){

			@Override
			protected ArrayList<String> doInBackground() throws Exception{

				ArrayList<String> results= client.getResultsFromServer();

				return results;

			}

			@Override
			protected void done(){

				try {
					ArrayList<String> results=get();

					for(String str: results){
						allNews.addElement(str);

					}

				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};worker.execute();
	}


	private void getSelectedTitle(){

		SwingWorker<String,Void> worker= new SwingWorker<String, Void>(){

			@Override
			protected String doInBackground() throws Exception{

				String body= client.getNewsBody();

				return body;


			}

			@Override
			protected void done(){

				String body="";
				try {
					body = get();
					textArea.setText(body);

				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};worker.execute();
	}
}
