package Googla_lhe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class WorkerHandler implements Runnable {


	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Server server;
	private Task currentTask;



	public WorkerHandler(Server server, ObjectOutputStream out, ObjectInputStream in){

		this.server=server;
		this.in=in;
		this.out=out;

	}

	@Override
	public void run() {

		while(true){	
			try {
				WorkerRequest();
				getResultFromWorker();
			} catch (InterruptedException | IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Erro no Run do WorkerHandler");
				break;
			}
		}
	}



	public void sendTaskToWorker() throws InterruptedException, IOException{ //Send task to Worker // Current Task is updated here!

		Task task= getWorkerTaskFromServer();
		currentTask=task;
		out.writeObject(task);
		out.flush();
		out.reset();

	}

	public void WorkerRequest() throws IOException, ClassNotFoundException, InterruptedException{ //Responds To Worker with a Task
		//When Task list have Tasks ready	
		Object ob= in.readObject();

		if(ob instanceof String){
			String r= (String) ob;

			if(r.equals("Give me Tasks")){
				sendTaskToWorker();
			}
		}
	}




	public Task getWorkerTaskFromServer() throws InterruptedException, IOException{ // Used to get Tasks from Server

		Task taskToWorker= server.getTask();
		return taskToWorker;
	}


	public void getResultFromWorker() throws ClassNotFoundException, IOException{ // get the results from worker and Add in Server Results List

		Object ob= in.readObject();
		Result result=null;

		if(ob instanceof ArrayList){
			ArrayList<Integer> results = (ArrayList<Integer>) ob;
			result= new Result(currentTask.getTaskID(), results.size(), currentTask.getNoticiaTask().getTitle());
			server.addTaskResult(result);
		}
	}
}

