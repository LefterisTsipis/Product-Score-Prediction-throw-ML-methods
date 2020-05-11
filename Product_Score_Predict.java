import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.RapidMiner.ExecutionMode;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;
import java.io.File;
import java.io.FileWriter;

public class Product_Score_Predict extends JFrame implements ActionListener {
    
    private JPanel row1;
    private JLabel product_Label;
    private JTextField product;


    private JPanel row2;
    private JTextArea Comment;
    private JLabel Comment_Label;
    
    private JPanel row3;
    private JLabel product_Label3;
    private JTextField score;

    private JPanel row4;
    private JButton predict;

  

    
    
    String productName,productComment,sFileName="C:\\Users\\LETERIS\\Desktop\\golf_test.csv";
    Double Score;

    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String queryString = "Delete from product-review";
    String serverName = "localhost:3306";
    String dbName = "reviews";
    String username = "root";
    String password = "";

    public Product_Score_Predict(){
        super("Predict product score");
        GUI();

    }
    public void actionPerformed(ActionEvent evt) {
 Object source = evt.getSource();
 if (source==predict) {
      
 try {
                       
            
            productName=product.getText();
            productComment=Comment.getText();
         
           
           /*
            //write csv
            FileWriter writer = new FileWriter(sFileName);
		 
	    writer.append("Outlook");
	    writer.append(',');
	    writer.append("Temperature");
	    writer.append(',');
            writer.append("Humidity");
	    writer.append(',');
            writer.append("Wind");
	    writer.append('\n');

	    writer.append(Outlook);
	    writer.append(',');
	    writer.append(Temperature.toString());
            writer.append(',');
	    writer.append(Humidity.toString());
            writer.append(',');
	    writer.append(Wind);
            writer.append('\n');
            
            writer.flush();
            writer.close();
            
            //end write csv
            
            */
           //  productName productComment
            String qString = "INSERT INTO product VALUES('"+productName+"','"+productComment+ "'"+" )";
           // String qString = "INSERT INTO product_review VALUES('"+productName+"',"+productComment+"')";
            createConnection();
            runInsertQuery(qString);
            closeConnection();
            
            RapidMiner.setExecutionMode(ExecutionMode.COMMAND_LINE);
            RapidMiner.init();
            File z=new File("C://Users//LETERIS//.RapidMiner//repositories//Local Repository//product_Score_final.rmp");
            Process process1 = new  Process(z);
            IOContainer ioResult1=process1.run();
            ExampleSet resultSet1=(ExampleSet)ioResult1.getElementAt(0);
            Example example1=resultSet1.getExample(0);
            Attribute Prediction=example1.getAttributes().get("prediction(Score)");
            String resultString1=example1.getValueAsString(Prediction);
            score.setText(resultString1);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OperatorException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     
     
     
 }

 }
void GUI(){

        row1 = new JPanel();
        product_Label = new JLabel("Enter product name :", JLabel.RIGHT);
        product = new JTextField(15);

        row2 = new JPanel();
        Comment_Label = new JLabel("Enter comment for the product :", JLabel.RIGHT);
        Comment = new JTextArea(10,20);
        
        row3 = new JPanel();
        product_Label3 = new JLabel("Score:", JLabel.RIGHT);
        score = new JTextField(5);

        row4 = new JPanel();
        predict = new JButton("predict");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        Container pane = getContentPane();
        GridLayout layout = new GridLayout(4,1);
        pane.setLayout(layout);



        //Prwti grammi
        FlowLayout layout1 = new FlowLayout();
        row1.setLayout(layout1);
        row1.add(product_Label);
        row1.add(product);
        pane.add(row1);


        //Deuteri grammi
        FlowLayout layout2 = new FlowLayout();
        row2.setLayout(layout2);
        row2.add(Comment_Label);
        row2.add(Comment);
        pane.add(row2);
        
        //triti grammi
        FlowLayout layout3 = new FlowLayout();
        row3.setLayout(layout3);
        row3.add(product_Label3);
        row3.add(score);
        pane.add(row3);

        //tetarti grammi
        FlowLayout layout4 = new FlowLayout();
        row4.setLayout(layout4);
        row4.add(predict);
        pane.add(row4);


        setContentPane(pane);
        pack();
        predict.addActionListener(this);




    }


    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver Registration Successful<br>");
        } catch (InstantiationException ie) {
            System.out.println("class instantiation exception: " + ie);
        } catch (ClassNotFoundException nf) {
            System.out.println("class not found exception: " + nf);
        } catch (IllegalAccessException iae) {
            System.out.println("illegal access exception: " + iae);
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + dbName + "?user=" + username + "&password=" + password);
            System.out.println("Connection Successful<br>");
        } catch (SQLException sq1) {
            System.out.println("Caught SQL exception " + sq1);
            System.out.println("Caught SQL exception " + sq1);
        }
    }

    private void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Caught SQL exception " + ex);
        }
    }
    
    
       private void runInsertQuery(String queryString) {
        try {
            statement = conn.createStatement();
            statement.execute(queryString);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    


    
}
