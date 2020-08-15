/* Name: Connor Rinaldo
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 26, 2020
*/


import java.io.File;
import javax.swing.*;
import java.util.Date;
import java.awt.event.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;

public class Frame {
	static double totalamt = 0.0; //Total amount for order and number of items(noi)
	static int noi = 1;
	public static void main(String[] args){
		
		ArrayList<item> books = new ArrayList<item>(); //Full inventory
		ArrayList<String> order = new ArrayList<String>(); //Complete order format for view
		ArrayList<String> transOrder = new ArrayList<String>(); //Complete order for transaction

		try { //Gets inventory and makes an object for each book
			File file = new File("inventory.txt");
			Scanner reader = new Scanner(file);
			while(reader.hasNextLine()) {
				String info = reader.nextLine();
				String[] parts = info.split(",");
				item book = new item(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]));
				books.add(book); //Puts each item in Alist
			}
			reader.close();
		}catch(Exception e) {
			System.out.print("oops");
		}
		
		JFrame f = new JFrame("Ye Old Shop of Books"); //Main frame
		JFrame err = new JFrame(); //Pop up messages
		JPanel panel = new JPanel();
		JLabel one = new JLabel("Enter number of items in this order: "); //Spaces for formatting
		JLabel two = new JLabel("                   Enter Book ID for Item #1: ");
		JLabel three = new JLabel("                   Enter quantity for item #1: ");
		JLabel four = new JLabel("                                            Item #1 info: ");
		JLabel five = new JLabel("                Order subtotal for 0 item(s)");
		
		JTextField amountOfItems = new JTextField(), 
				id = new JTextField(), 
				quantity = new JTextField(),
				info= new JTextField(), 
				total = new JTextField(); 
		
		JButton process = new JButton("Proces Item #" + noi); 
		JButton confirm = new JButton("Confirm Item #" + noi);
		JButton view = new JButton("View Order");
		JButton finish = new JButton("Finish Order");
		JButton newOrder = new JButton("New Order");
		JButton exit = new JButton("Exit");
		
		amountOfItems.setColumns(40); //Formatting
		id.setColumns(40);
		quantity.setColumns(40);
		info.setColumns(40);
		total.setColumns(40);
		
		confirm.setEnabled(false); //Initial state
		view.setEnabled(false);
		finish.setEnabled(false);
		
		info.setEditable(false);
		total.setEditable(false);
		
		process.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try { 
					//For loop, size of inventory, id of book
					int i , amt = books.size(), number = Integer.parseInt(id.getText());  

					for(i = 0; i <  amt; i++) {
						if(books.get(i).id == number) {
							int discount = 0, qut = Integer.parseInt(quantity.getText()); //Quantity
							double price = 0.0, disPoint = 0.0; //Price after discount and decimal value for discount
	
							if(qut >= 10) { //Finds discount
								discount = 15;
								disPoint = .15;
								price = books.get(i).price * 0.85 * qut;
							}else {
								if(qut <= 9 && qut >= 5) {
									discount = 10;
									disPoint = .1;
									price = books.get(i).price * 0.9 * qut;
								}else
									price = books.get(i).price * qut;
							}
							
							price = (double) Math.round(price * 100)/100; //Formats to two places
							totalamt = totalamt + price;
							
							order.add(books.get(i).id + " " + books.get(i).quote + " " + books.get(i).price + " "
									+ discount + "% " + price);
							
							transOrder.add(books.get(i).id + ", " + books.get(i).quote + ", " + books.get(i).price + ", "
									+ qut + ", " + disPoint + ", " + price);
							
							info.setText(books.get(i).id + " " + books.get(i).quote + " " + books.get(i).price + " " + discount + "% " + price);
							four.setText("                                            Item #" + noi + " info: ");
							
							process.setEnabled(false);
							confirm.setEnabled(true);
							break;
						}
					}
					
					if(i == amt) { //Error message if not found
						JOptionPane.showMessageDialog(err, "Book ID " + number + " not found");
					}
				}catch(Exception i) {
					JOptionPane.showMessageDialog(err, "Book ID " + id.getText() + " not found");
				}
			}
		});
		
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {				
				if(noi == Integer.parseInt(amountOfItems.getText())) { //Checks if theres another item
					confirm.setEnabled(false);
					
					totalamt = (double) Math.round(totalamt * 100)/100; //Formating 
					
					id.setText("");
					quantity.setText("");
					total.setText(""+totalamt);
					process.setText("Proces");
					confirm.setText("Confirm");
					two.setText("                                                                    ");
					three.setText("                                                                    ");
					
					view.setEnabled(true);
					finish.setEnabled(true);
					
					JOptionPane.showMessageDialog(err, "Item #" + noi + " accepted");
				}else {
					
					JOptionPane.showMessageDialog(err, "Item #" + noi + " accepted");
					noi++;
					id.setText("");
					quantity.setText("");
					process.setText("Proces Item #" + noi);
					confirm.setText("Confirm Item #" + noi);
					view.setEnabled(true);
					finish.setEnabled(true);
					process.setEnabled(true);
					confirm.setEnabled(false);
					totalamt = (double) Math.round(totalamt * 100)/100; //Formating 
					total.setText(""+totalamt);	
				}
			}
		});
		
		view.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int i, amt = order.size();
				String viewAll = "";
				for(i = 0; i < amt; i++) { //Displays order at the current time
					viewAll = viewAll + (i+1) + ". " + order.get(i) + "\n";
				}
				JOptionPane.showMessageDialog(err, viewAll);
			}
		});
		
		finish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DateFormat  dateTimeForm = new SimpleDateFormat("MM/DD/YY, hh:mm:ss aa"); //Date formats
				DateFormat  dateTimeFormFinal = new SimpleDateFormat("DDMMYYYYHHmm");
                String formattedDate = dateTimeForm.format(new Date()).toString();
                String formattedDateFinal = dateTimeFormFinal.format(new Date()).toString();
                
                double finalamt = 0.0;
                String finalOrder = "";
                int i, amt = order.size();
                
                //This is the final order message 
                finalOrder = "Date: " + formattedDate + " EST\n\n"
                		+ "Number of items: " + noi +"\n\n"
                		+ "Item# / ID / Title / Price / Qty / Disc % / Subtotal\n\n";
                
				for(i = 0; i < amt; i++) {
					finalOrder = finalOrder + (i+1) + ". " + order.get(i) + "\n";
				}
				
				finalamt = totalamt  * 1.06;
				finalamt = (double) Math.round(finalamt * 100)/100;
				finalOrder = finalOrder + "\n\nOrder subtoal: $"+ totalamt + "\n\n"
						+"Tax rate:      6%\n\n"
						+ "Order total: $" + finalamt + "\n\n"
					    + "Thanks for shopping at Ye Old Shop of Books";
				
                JOptionPane.showMessageDialog(err, finalOrder);
                
               try{ //Appends to the end of file or makes it 
            	   File trans = new File("transactions.txt");
            	   if(!trans.exists()) {
            	   		trans.createNewFile();
            	   	   }
            	   FileWriter fw = new FileWriter(trans, true); 
            	   BufferedWriter bw = new BufferedWriter(fw);
            	   for(i = 0; i < amt; i++) {
            		  bw.write(formattedDateFinal + ", " + transOrder.get(i) +", " + formattedDate + " EST\n"); 
            	   }
            	   bw.close();
                }catch (IOException e1) {
						}
                              
			}
		});
		
		newOrder.addActionListener(new ActionListener(){ //Resets all variables, lists, and frames
			public void actionPerformed(ActionEvent e) {
				totalamt = 0.0;
				noi = 1;
				
				order.clear();
				transOrder.clear();
				
				process.setEnabled(true);
				confirm.setEnabled(false);
				view.setEnabled(false);
				finish.setEnabled(false);
				
				amountOfItems.setText("");
				id.setText("");
				quantity.setText("");
				info.setText("");
				total.setText("");
				
				process.setText("Proces Item #1");
				confirm.setText("Confirm Item #1");
				two.setText("                   Enter Book ID for Item #1: ");
				three.setText("                   Enter quantity for item #1: ");
				four.setText("                                            Item #1 info: ");
			}
		});
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0); //Kills program
			}
		});
		
		//Adds all elements
		panel.add(one);
		panel.add(amountOfItems);
		panel.add(two);
		panel.add(id);
		panel.add(three);
		panel.add(quantity);
		panel.add(four);
		panel.add(info);
		panel.add(five);
		panel.add(total);
		
		panel.add(process);
		panel.add(confirm);
		panel.add(view);
		panel.add(finish);
		panel.add(newOrder);
		panel.add(exit);
		
		f.add(panel);
		f.setSize(750, 215);
		f.setVisible(true);
	}
}
