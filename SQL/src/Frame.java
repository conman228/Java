/* Name: Connor Rinaldo
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 3 – Event-driven Enterprise Simulation
 Date: Sunday January 26, 2020
*/

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	
	Connection connection;

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Frame frame = new Frame();
			frame.setVisible(true);
		}

	public Frame() throws ClassNotFoundException, SQLException {
			
		//Grid layout used window builder to form frame
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{79, 72, 179, 89, 109, 135, 189, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 46, 28, 16, 0, 217, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		
		JPanel contentPane = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		
		contentPane.setLayout(gbl_contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 550);
		setContentPane(contentPane);
		
		//LABELS
		JLabel userLabel = new JLabel("Username");
		JLabel passLabel = new JLabel("Password");
		JLabel urlLabel = new JLabel("Database URL");
		JLabel driverLabel = new JLabel("JBDC Driver");
		JLabel easc = new JLabel("Enter A SQL Command");
		JLabel edi = new JLabel("Enter Database Information");		
		JLabel connectLabel = new JLabel("No Connection Now");
		JLabel window = new JLabel("SQL Execution Result window");
		
		//BUTTONS
		JButton clearSQLBtn = new JButton("Clear SQL Command");
		JButton executeBtn = new JButton("Execute SQL Command");
		JButton clearResultBtn = new JButton("Clear Result Window");
		JButton connectBtn = new JButton("Connect to Database");
		
		//TEXT FIELDS
		JTextField userText = new JTextField("");
		JPasswordField passText = new JPasswordField ("");
		JTextArea sqlText = new JTextArea("");
		
		//COLORS 
		userLabel.setOpaque(true);
		passLabel.setOpaque(true);
		urlLabel.setOpaque(true);
		driverLabel.setOpaque(true);
		connectLabel.setOpaque(true);
		
		driverLabel.setBackground(Color.LIGHT_GRAY);
		userLabel.setBackground(Color.LIGHT_GRAY);
		passLabel.setBackground(Color.LIGHT_GRAY);
		urlLabel.setBackground(Color.LIGHT_GRAY);
		clearResultBtn.setBackground(Color.YELLOW);
		connectLabel.setBackground(Color.BLACK);
		clearSQLBtn.setBackground(Color.WHITE);
		executeBtn.setBackground(Color.GREEN);
		connectBtn.setBackground(Color.BLUE);
		
		edi.setForeground(Color.BLUE);
		easc.setForeground(Color.BLUE);
		clearSQLBtn.setForeground(Color.RED);
		connectLabel.setForeground(Color.RED);
		connectBtn.setForeground(Color.YELLOW);
		
		sqlText.setWrapStyleWord(true);
		sqlText.setLineWrap(true);
		
		//GETS LIST OF DRIVERS AND THEN SCHEMAS 
		ArrayList<String> result = new ArrayList<>();
		String sql = "show databases", temp;
		int loc = 0, last, size;
		String[] drivers, urls;
		
		JComboBox driverBox;
		JComboBox urlBox;
	
		//DRIVERS LIST
		for (Enumeration<Driver> driverEnumeration = DriverManager.getDrivers();
			driverEnumeration.hasMoreElements(); ) {
				temp = driverEnumeration.nextElement()+"";
				result.add(temp.substring(0, temp.indexOf('@')));
		  	}
		
		size = result.size();
		drivers = new String[size]; 
		
		//ARRAYLIST TO ARRAY FOR COMBO
		for(int i = 0; i < size; i++) {
			drivers[i] = result.get(i);
		}
		
		//OPENS CONNECT TO GET SCHEMAS(DONT USER ROOT)
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/","client" ,"client");
		Statement statement = connection.createStatement();
		ResultSet set = statement.executeQuery(sql);
		
		set.last();
		last = set.getRow();
		set.first();
		
		urls = new String[last];
		urls[loc++] = "jdbc:mysql://localhost:3312/" + set.getString(1);
		while(set.next()) 
		  urls[loc++] = "jdbc:mysql://localhost:3312/" + set.getString(1);
		
		connection.close();
		
		driverBox = new JComboBox(drivers);
		urlBox = new JComboBox(urls);
		
		//CONNECTS TO DATABASE
		connectBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					connection = DriverManager.getConnection(urlBox.getSelectedItem()+"", userText.getText(), passText.getText());
					connectLabel.setText("Connected to " + urlBox.getSelectedItem()+"");
					  
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(scrollPane, "Can Not Connect To Database","Database Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		
		//CLEAR SQL
		clearSQLBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				sqlText.setText("");
				}
			});
		
		//GETS INFO AND MAKES TABLE
		executeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					ResultSet resultSet = connection.createStatement().executeQuery(sqlText.getText());
					ResultSetMetaData metaData = resultSet.getMetaData();
					
					JTable table;
					String[] colName;
					String[][] values;
					int colAmt = metaData.getColumnCount(), rowAmt;
				
					resultSet.last();
					rowAmt = resultSet.getRow();
					
					colName = new String[colAmt];
					values = new String[rowAmt][colAmt];
					
					//FILLS TABLE
					for(int i = 0; i < colAmt; i++) {
						colName[i] = metaData.getColumnName(i+1);
					}
					
					for(int i = 0; i < rowAmt; i++) {
						for(int j = 0; j < colAmt; j++) {
							resultSet.absolute(i+1);
							values[i][j] = resultSet.getString(j+1);
						}
					}
					
					//ADDS TABLE
					table = new JTable(values, colName);
					scrollPane.setViewportView(table);
					
					}catch(Exception e1) {//CATCHES ERROS 
						try {
							//IF STATEMENT IS A UPDATE
							int result = connection.createStatement().executeUpdate(sqlText.getText());
							JOptionPane.showMessageDialog(scrollPane, result + " Rows Affected.");
						}catch(Exception e2){//CATCHES ERROS 
							JOptionPane.showMessageDialog(scrollPane, e2.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		
		//CLEAR TABLE
		clearResultBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JTable temp = new JTable();
				scrollPane.setViewportView(temp);
				}
			});
		
		//Used windowsBuilder to develop the frame and set up
		GridBagConstraints gbc_edi = new GridBagConstraints();
		gbc_edi.anchor = GridBagConstraints.WEST;
		gbc_edi.gridwidth = 4;
		gbc_edi.insets = new Insets(0, 0, 5, 5);
		gbc_edi.gridx = 0;
		gbc_edi.gridy = 0;
		contentPane.add(edi, gbc_edi);
		
		GridBagConstraints gbc_easc = new GridBagConstraints();
		gbc_easc.gridwidth = 2;
		gbc_easc.fill = GridBagConstraints.HORIZONTAL;
		gbc_easc.insets = new Insets(0, 0, 5, 5);
		gbc_easc.gridx = 4;
		gbc_easc.gridy = 0;
		contentPane.add(easc, gbc_easc);
		
		GridBagConstraints gbc_driverLabel = new GridBagConstraints();
		gbc_driverLabel.fill = GridBagConstraints.BOTH;
		gbc_driverLabel.insets = new Insets(0, 0, 5, 5);
		gbc_driverLabel.gridx = 0;
		gbc_driverLabel.gridy = 1;
		contentPane.add(driverLabel, gbc_driverLabel);
		
		GridBagConstraints gbc_driverBox = new GridBagConstraints();
		gbc_driverBox.gridwidth = 3;
		gbc_driverBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_driverBox.insets = new Insets(0, 0, 5, 5);
		gbc_driverBox.gridx = 1;
		gbc_driverBox.gridy = 1;
		contentPane.add(driverBox, gbc_driverBox);
		
		GridBagConstraints gbc_sqlText = new GridBagConstraints();
		gbc_sqlText.insets = new Insets(0, 0, 5, 0);
		gbc_sqlText.gridwidth = 3;
		gbc_sqlText.gridheight = 4;
		gbc_sqlText.fill = GridBagConstraints.BOTH;
		gbc_sqlText.gridx = 4;
		gbc_sqlText.gridy = 1;
		contentPane.add(sqlText, gbc_sqlText);
	
		GridBagConstraints gbc_urlLabel = new GridBagConstraints();
		gbc_urlLabel.fill = GridBagConstraints.BOTH;
		gbc_urlLabel.insets = new Insets(0, 0, 5, 5);
		gbc_urlLabel.gridx = 0;
		gbc_urlLabel.gridy = 2;
		contentPane.add(urlLabel, gbc_urlLabel);
		
		GridBagConstraints gbc_urlBox = new GridBagConstraints();
		gbc_urlBox.gridwidth = 3;
		gbc_urlBox.insets = new Insets(0, 0, 5, 5);
		gbc_urlBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_urlBox.gridx = 1;
		gbc_urlBox.gridy = 2;
		contentPane.add(urlBox, gbc_urlBox);
		
		GridBagConstraints gbc_userLabel = new GridBagConstraints();
		gbc_userLabel.fill = GridBagConstraints.BOTH;
		gbc_userLabel.insets = new Insets(0, 0, 5, 5);
		gbc_userLabel.gridx = 0;
		gbc_userLabel.gridy = 3;
		contentPane.add(userLabel, gbc_userLabel);
		
		GridBagConstraints gbc_userText = new GridBagConstraints();
		gbc_userText.gridwidth = 3;
		gbc_userText.insets = new Insets(0, 0, 5, 5);
		gbc_userText.fill = GridBagConstraints.HORIZONTAL;
		gbc_userText.gridx = 1;
		gbc_userText.gridy = 3;
		contentPane.add(userText, gbc_userText);
		userText.setColumns(10);
		
		GridBagConstraints gbc_passLabel = new GridBagConstraints();
		gbc_passLabel.fill = GridBagConstraints.BOTH;
		gbc_passLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passLabel.gridx = 0;
		gbc_passLabel.gridy = 4;
		contentPane.add(passLabel, gbc_passLabel);
		
		GridBagConstraints gbc_passText = new GridBagConstraints();
		gbc_passText.gridwidth = 3;
		gbc_passText.insets = new Insets(0, 0, 5, 5);
		gbc_passText.fill = GridBagConstraints.HORIZONTAL;
		gbc_passText.gridx = 1;
		gbc_passText.gridy = 4;
		contentPane.add(passText, gbc_passText);
		passText.setColumns(10);
		
		GridBagConstraints gbc_connectLabel = new GridBagConstraints();
		gbc_connectLabel.fill = GridBagConstraints.BOTH;
		gbc_connectLabel.gridwidth = 3;
		gbc_connectLabel.insets = new Insets(0, 0, 5, 5);
		gbc_connectLabel.gridx = 0;
		gbc_connectLabel.gridy = 6;
		contentPane.add(connectLabel, gbc_connectLabel);
		
		GridBagConstraints gbc_connectBtn = new GridBagConstraints();
		gbc_connectBtn.gridwidth = 2;
		gbc_connectBtn.fill = GridBagConstraints.BOTH;
		gbc_connectBtn.insets = new Insets(0, 0, 5, 5);
		gbc_connectBtn.gridx = 3;
		gbc_connectBtn.gridy = 6;
		contentPane.add(connectBtn, gbc_connectBtn);
		
		GridBagConstraints gbc_clearSQLBtn = new GridBagConstraints();
		gbc_clearSQLBtn.fill = GridBagConstraints.BOTH;
		gbc_clearSQLBtn.insets = new Insets(0, 0, 5, 5);
		gbc_clearSQLBtn.gridx = 5;
		gbc_clearSQLBtn.gridy = 6;
		contentPane.add(clearSQLBtn, gbc_clearSQLBtn);
		
		GridBagConstraints gbc_executeBtn = new GridBagConstraints();
		gbc_executeBtn.insets = new Insets(0, 0, 5, 0);
		gbc_executeBtn.fill = GridBagConstraints.BOTH;
		gbc_executeBtn.gridx = 6;
		gbc_executeBtn.gridy = 6;
		contentPane.add(executeBtn, gbc_executeBtn);
		
		GridBagConstraints gbc_window = new GridBagConstraints();
		gbc_window.anchor = GridBagConstraints.WEST;
		gbc_window.gridwidth = 4;
		gbc_window.insets = new Insets(0, 0, 5, 5);
		gbc_window.gridx = 0;
		gbc_window.gridy = 8;
		contentPane.add(window, gbc_window);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 9;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		GridBagConstraints gbc_clearResultBtn = new GridBagConstraints();
		gbc_clearResultBtn.anchor = GridBagConstraints.WEST;
		gbc_clearResultBtn.gridwidth = 4;
		gbc_clearResultBtn.insets = new Insets(0, 0, 0, 5);
		gbc_clearResultBtn.gridx = 0;
		gbc_clearResultBtn.gridy = 10;
		contentPane.add(clearResultBtn, gbc_clearResultBtn);
	}
}
