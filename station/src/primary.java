import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

/*
Name: Connor Rinaldo
Course: CNT 4714 Spring 2020
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 9, 2020
Class: CNT 4714 – Spring 2020
*/

public class primary {
	public static void main(String args[]) {
		int size;
		station stations[];
		conveyors convey[];
		ArrayList<Integer> workload = new ArrayList<Integer>();
		
		try { //Reads File
			File file = new File("config.txt");
			Scanner reader = new Scanner(file);
			while(reader.hasNextLine()) {
				workload.add(Integer.parseInt(reader.nextLine()));
				
			}
			reader.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		size = workload.get(0); //Amount of stations
		stations = new station[size];
		convey = new conveyors[size];
		
		for(int i = 0; i < size; i++) //Makes all conveyor's
			convey[i] = new conveyors(i);
		
		ExecutorService loop = Executors.newFixedThreadPool(size);//Thread pool
		
		for(int i = 0; i < size; i++) {//Makes stations and sets connections
			stations[i] = new station(i, workload.get(i+1), size-1);
			stations[i].setInputCon(convey[stations[i].getInputNum()]);
			stations[i].setOutputCon(convey[stations[i].getOutputNum()]);
		}
		
		for(int i = 0; i < size; i++) {//Runs all threads
			try {
				loop.execute(stations[i]);
			}catch(Exception e){}
		}
		loop.shutdown();//Ends thread
	}
}
