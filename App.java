
import java.sql.* ;
import java.io.*;
import java.util.*;
import java.lang.Math.*;


// global variables



 class dataBase_Connection {

    // we will define the connection for the data base and will make the useful tables here in this class

    // let us first define the final variables for our data base 
    // by making them private we have used encapsulation 
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private  static final String DATABASE_URL = "jdbc:mysql://localhost:3306/food_ordering?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private  static final String USERNAME = "udit"; // username
    private  static final String PASSWORD = "*Ukmagic@35*"; // password
    private Connection connection  ; 
    private Properties properties;
 

// function for making the connection 




private Properties getCredentials() {
    if (properties == null) {
        properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
    }
    return properties;
}


    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getCredentials());
                System.out.println("Connected:");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



public Connection reterive_Connection ()
{

    return connection ;

}



}



// class for Cart 
class my_Cart{


    // to view the cart

    public void display_Cart(Statement st){
    
        try{
            System.out.println("See Your Cart items :");
            String que  = "Select * from cart" ;
            ResultSet  rs= st.executeQuery(que) ;
            while(rs.next()){
                System.out.print("User Email: "+rs.getString(1)+", ");
                System.out.print("Restaurant Name: "+rs.getString(2)+", ");
                System.out.print("Food Item Name : "+rs.getString(3)+", ");
                System.out.print("Food Item Quantity: "+rs.getInt(4)+", ");
                System.out.print("Food Item Price: "+rs.getInt(5));
                System.out.println("");
            }
        }
        
        catch (SQLException e)
        {
            
            e.printStackTrace();
        }
    
    }


public void order_Confirmation(){
System.out.println(" ");
System.out.println("Your Item has been placed :), Now go for the Payments !") ;
}

// to discard the order !
public void discard_Cart(Statement st, String User_Mail){


    try{
     
        String que  = "Delete from cart" ;
         st.executeUpdate(que);
        
        System.out.println("Your cart is empty Now !") ;
       
    }
    
    catch (SQLException e)
    {
        
        e.printStackTrace();
    }
}


// to show the menu of the restaurant picked by the user
public void show_Menu(Statement st, int num){
        
        
    try{
        System.out.println("Menu for the Restaurant id " + num + " is:" );
        String que  = "Select * from menu where res_Id =" + "\"" + num + "\"";
        ResultSet  rs= st.executeQuery(que) ;
        while(rs.next()){
            System.out.print("Menu ID: "+rs.getString(1)+", ");
            System.out.print("Restaurant ID: "+rs.getString(2)+", ");
            System.out.print("Restaurant Name: "+rs.getString(3)+", ");
            System.out.print("Food Item Name: "+rs.getString(4)+", ");
            System.out.print("Food Item Price: "+rs.getString(5));
            System.out.println("");
        }
    }
    
    catch (SQLException e)
    {
        
        e.printStackTrace();
    }
    
    }

// adding to the cart everytime it ask user which item to add in cart using while loop logic
public void add_To_Cart(Statement st,int res_Id, Connection con,String user_mail){
Scanner scn = new Scanner (System.in) ; 
System.out.println("") ; 
System.out.println("Lets add some food items in your Cart ! ") ;
System.out.println("If anytime you feel, You are done selecting the food items, Press 0") ;
show_Menu(st,res_Id);
int flag = 1 ;
while (flag ==1){
    System.out.println("Enter the menu ID of the food item you wanna add !") ;
    int check_Menu  = scn.nextInt();
    if (check_Menu !=0 ){
        System.out.println("How many items do you want ? Enter the number") ; 
        int user_quant = scn.nextInt();
        // quanitity of the selected food item
        show_Menu(st, res_Id); // for showing the menu again and again
       
        // logic for entering the details in the cart table 
        String res_name = "" ;
        String item_name = "" ;
        int price = 0 ;
        
        
        try{
            String que  = "Select * from menu where menu_Id =" + "\"" + check_Menu + "\"";
            //String que2 = "Select quantity from cart where user_Email =" + "\"" + user_mail + "\"";
            Statement st2 = con.createStatement() ;
            ResultSet  rs= st.executeQuery(que) ;
          //  ResultSet rs2 = st2.executeQuery(que2) ;
           
           // prev_quant = rs2.getInt(1);
            rs.next() ; 
            res_name = rs.getString(3);
            item_name = rs.getString(4);
            price = rs.getInt(5); 
            
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }

        // now we have to add these values of order in the cart 
        
        try{
            String cart_Query = "INSERT INTO cart (user_Email, res_Name, item_Name, quantity, price)" + "VALUES (?, ?, ?, ?, ?)";
            //Creating a PreparedStatement object
            PreparedStatement pst = con.prepareStatement(cart_Query);
            
            pst.setString(1,user_mail);
            pst.setString(2, res_name);
            pst.setString(3, item_name);
            pst.setInt(4, user_quant);
            pst.setInt(5,price);
            pst.execute();
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }

        


       



    }

    else{
        // for breaking the while loop 
        flag = 0 ; 
    }
   


}
}

// deletion of the particular item 
public void delete_From_Cart(Statement st,int res_Id, Connection con,String user_mail){
    Scanner scn = new Scanner (System.in) ; 
    System.out.println("") ; 
    System.out.println("Lets delete  some food items from your Cart ! ") ;
    System.out.println("If anytime you feel, You are done deleting the food items, Press 0") ;
    


    int flag  = 1  ;

    while(flag == 1){

        System.out.println("Your cart is here !") ;
        display_Cart(st);
        System.out.println("Enter the food item you want to delete from the cart !");
        String food_Item = scn.nextLine();

        try{
           
            String que  = "Delete  from cart where item_Name =" + "\"" + food_Item + "\"";
            st.executeUpdate(que);
        }


        catch (SQLException e)
        {
            
            e.printStackTrace();

        }
        
        System.out.println("Your cart after deleting the items ");
        display_Cart(st);

        System.out.println("1.Want to further delete some item ,Enter 1");
        System.out.println("2.Don't want to delete the food items anymore, Enter 2");

        int response = scn.nextInt() ;

        if (response ==1){
         flag =1 ; 
        }

        else if (response == 2){
        flag = 0 ; 
        }

        else{
            System.out.println("Invalid input !") ; 
            flag = 0 ; 
        }



    }

     



}



}

// for visualization on the screen 

// class to write all the query statements to get the data : 

class retrieve_Data extends my_Cart{
    // it helps to retrieve the data 
    // implemnts function overloading.
    
    // function overloading
    public void fetch_User(Statement st) throws SQLException{


        String query = "Select Username from Customer" ;
        //Statement st = connection.createStatement() ;
        ResultSet rs = st.executeQuery(query) ;
 
        // fetching the data 
        // before fetching the value you should write 
        rs.next() ; 
        String user_Name = rs.getString(1);
        System.out.println("WELCOME " + user_Name + ", Use Our App to order the food ! ");
 }


 
// function overloading : 
 public void fetch_User(Statement st,boolean yes) throws SQLException{
    // it will print the all details of the user 
    if (yes){
        String query = "Select * from Customer" ;
        //Statement st = connection.createStatement() ;
        ResultSet  res= st.executeQuery(query) ;
    
        
        System.out.println("Details of the Customer");
        while(res.next()) {
           System.out.print("Name of the User: "+res.getString(1)+", ");
           System.out.print("Gender: "+res.getString(2));
           System.out.print(", Address: "+res.getString(4));
           System.out.print(", Email: "+res.getString(5));
           System.out.println("");
        }
    }
   
}
// fetching user email
public String fetch_UserMail(Statement st){

  String user_Mail = "" ; 
    try{
        
    String query = "Select email from Customer" ;
    //Statement st = connection.createStatement() ;
    ResultSet rs = st.executeQuery(query) ;

    // fetching the data 
    // before fetching the value you should write 
    rs.next() ; 
     user_Mail = rs.getString(1);
   

    }

    catch (SQLException e)
{
    
    e.printStackTrace();
}

return user_Mail ; 

}

// fetching names of all restaurants : 
public void fetch_All_Restaurants(Statement st,Connection connection) throws SQLException{
    System.out.println();
    Statement st2 = connection.createStatement() ;
    String places = "Select res_Name from restaurant" ;
    String city = "Select ADDRESS from Customer" ; 
    //Statement st = connection.createStatement() ;
    ResultSet  pl= st2.executeQuery(places) ;
    ResultSet  cy = st.executeQuery(city) ; 
    cy.next() ; 
    System.out.println("******List of 5 famous Restaurants in your city : " + cy.getString(1) + "*******" ); 
    int i =101 ; 
    System.out.println();
    while(pl.next()) {
       System.out.print("Restaurant id " + i + ":"+ " " + pl.getString(1)+" ");
       i++;
       System.out.println("");
    }
}

// fetching the coordiantes of all restaurants
// we have used the try catch in finding the coordinates 

public double [] fetch_latitude(Statement st){
double [] arr_lat = new double [5];

try{

    String lat  = "Select latitude from restaurant" ;
    ResultSet  rs= st.executeQuery(lat) ;
    int ptr = 0 ; 
    while(rs.next()){
    arr_lat [ptr] = rs.getDouble(1) ;
    ptr++ ; 
    }
}

catch (SQLException e)
{
    
    e.printStackTrace();
}

return arr_lat ; 






}

public double []  fetch_longitude(Statement st){
    double [] arr_lon = new double [5];


    try{
        String lon = "Select longitude from restaurant" ;
        ResultSet  rs= st.executeQuery(lon) ;
        int ptr = 0 ; 
        while(rs.next()){
        arr_lon [ptr] = rs.getDouble(1) ;
        ptr++ ; 
        }
    
    }

    catch (SQLException e)
{
    // do something appropriate with the exception, *at least*:
    e.printStackTrace();
}


   
    
return arr_lon; 
    }



    public void fetch_Menu(Statement st, int num){
        
        
        try{
            System.out.println("Menu for the Restaurant id " + num + " is:" );
            String que  = "Select * from menu where res_Id =" + "\"" + num + "\"";
            ResultSet  rs= st.executeQuery(que) ;
            while(rs.next()){
                System.out.print("Menu ID: "+rs.getString(1)+", ");
                System.out.print("Restaurant ID: "+rs.getString(2)+", ");
                System.out.print("Restaurant Name: "+rs.getString(3)+", ");
                System.out.print("Food Item Name: "+rs.getString(4)+", ");
                System.out.print("Food Item Price: "+rs.getString(5));
                System.out.println("");
            }
        }
        
        catch (SQLException e)
        {
            
            e.printStackTrace();
        }
        
        }



        
}

// 2. order class 
class your_Order extends retrieve_Data {


// encapsulation performed
private double [] latitude = new double [5] ;
private double [] longitude = new double [5] ;
private double[] estimated_Times = new double [5] ;



 
// haversine logic 
// estimated time logic : 

public double  cal_estimatedTime_Haversine(double lat1,
double lat2, double lon1,
             double lon2 ){

             
                

                lon1 = Math.toRadians(lon1);
                lon2 = Math.toRadians(lon2);
                lat1 = Math.toRadians(lat1);
                lat2 = Math.toRadians(lat2);

                double dlon = lon2 - lon1;
                double dlat = lat2 - lat1;
                double aim = Math.pow(Math.sin(dlat / 2), 2)
                         + Math.cos(lat1) * Math.cos(lat2)
                         * Math.pow(Math.sin(dlon / 2),2);
                     
                double hw = 2 * Math.asin(Math.sqrt(aim));
         
                
                double rad = 6371;
         
                // result will be in minutes
                double res = (hw*rad)/4 ; 
                 res = (double) Math.round(res * 100) / 100;
                return res ; 

}

public void cal_EstimatedTime(Statement st){
    System.out.println();
    System.out.println("The Estimated time in minutes for the above restaurants are : ");
    latitude = fetch_latitude(st)  ; // accessing the latitude of restaurants 

    longitude = fetch_longitude(st) ; // fetching the longitude data .

    // user latitude and longitude assumption :
    double user_Lat = 35.3 ;
    double user_Long = 62.8 ; 

    int ptr = 1 ;
    int res_Id = 101 ;  
    while (ptr<=5){
    
    // calling the cal_Estimated_Haversine

    double time = cal_estimatedTime_Haversine(user_Lat, latitude[ptr-1], user_Long, longitude[ptr-1]);
    System.out.println("The estimated time for Restaurant id " + res_Id +" : " + time + " minutes");
    estimated_Times[ptr-1] = time ; 
    res_Id++;
    ptr++ ; 

    

    }
    System.out.println(" ");


     

}

public double [] give_estimated_Times(){
return this.estimated_Times ; 
}

public void show_Menu (Statement st,int num){
    fetch_Menu(st, num); // for output of the menu
    }


// placing the order
public void place_Order(Statement st, int res_Id,Connection con){
Scanner scn = new Scanner (System.in) ; 
System.out.println("") ; 
System.out.println("Let us help you to place your Order :) ") ;
System.out.println("If anytime you feel, You are done selecting the food items, Press 0") ;

int flag = 1 ;
while (flag ==1){
    System.out.println("Enter the menu ID of the food item you wanna eat !") ;
    int check_Menu  = scn.nextInt(); 
    if (check_Menu !=0 ){
        System.out.println("How many items do you want ? Enter the number") ; 
        int user_quant = scn.nextInt();
        // quanitity of the selected food item
        show_Menu(st, res_Id); // for showing the menu again and again
       
        // logic for entering the details in the cart table 
        String user_mail = fetch_UserMail(st);
        String res_name = "" ;
        String item_name = "" ;
        int price = 0 ;
        
        try{
            String que  = "Select * from menu where menu_Id =" + "\"" + check_Menu + "\"";
            
            ResultSet  rs= st.executeQuery(que) ;
            rs.next() ; 
            res_name = rs.getString(3);
            item_name = rs.getString(4);
            price = rs.getInt(5); 
            
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }

        // now we have to add these values of order in the cart 
        try{
            String cart_Query = "INSERT INTO cart (user_Email, res_Name, item_Name, quantity, price)" + "VALUES (?, ?, ?, ?, ?)";
            //Creating a PreparedStatement object
            PreparedStatement pst = con.prepareStatement(cart_Query);
            
            pst.setString(1,user_mail);
            pst.setString(2, res_name);
            pst.setString(3, item_name);
            pst.setInt(4, user_quant);
            pst.setInt(5,price);
            pst.execute();
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }
    }

    else{
        // for breaking the while loop 
        flag = 0 ; 
    }
   
}

}


public void alter_Cart(Statement st,int res_Id,Connection con){

    // to alter the cart using the Cart Methods

    System.out.println("") ;
int stopper = 1 ; 
   while(stopper ==1){
       System.out.println();
    System.out.println("What you want to do with the order , you have to choose 5th option for payments: ") ;
    System.out.println("1.View the order Cart");
    System.out.println("2.Add item to the Cart");
    System.out.println("3.Delete item from the Cart");
    System.out.println("4.Discard my Order ! ");
    System.out.println("5.Confirm my Order :) ");

Scanner scn = new Scanner(System.in) ; 
 int user_Response = scn.nextInt() ; 
 if (user_Response == 1){
 display_Cart(st) ; 

 }

 else if(user_Response == 2){
    String mail = fetch_UserMail(st);
     add_To_Cart(st,res_Id,con,mail);
     System.out.println("Your cart after adding the items !");
     display_Cart(st);
     
 }

 else if(user_Response == 3){

    String user_Mail = fetch_UserMail(st);
    delete_From_Cart(st, res_Id, con, user_Mail);
    System.out.println("Your cart after deleting  the items !");
    display_Cart(st);
    
     }

else if(user_Response == 4){
    String user_mail = fetch_UserMail(st);
    discard_Cart(st, user_mail);
    System.out.println("Your cart after discarding the items");
    display_Cart(st);
    
 }
 else if(user_Response == 5){
  stopper = 13 ; // to break the loop 
   order_Confirmation();
 }

 else{
     System.out.println("Invalid Input !")  ;

 }
   }
    
}

}



/* class Wishlist : 
 Inheritence 
 user_Wishlist is the child class of your_Order class */
class user_Wishlist extends your_Order{

// for adding the food item selected from the selected restaurant in the wishlist table in data base 
public void add_To_WishList(Statement st, int res_Id, Connection con){

Scanner scn = new Scanner (System.in) ; 
System.out.println("") ; 
System.out.println("Let us help you to fill your Wishlist :) ") ;
System.out.println("If anytime you feel, You are done selecting the food items, Press 0") ;

int flag = 1 ;
while (flag ==1){
    System.out.println("Enter the menu ID of the food item you wanna add to wishlist !") ;
    int check_Menu  = scn.nextInt(); 
    if (check_Menu !=0 ){
        
        
        show_Menu(st, res_Id); // for showing the menu again and again
       
        // logic for entering the details in the cart table 
        String user_mail = fetch_UserMail(st);
        String res_name = "" ;
        String item_name = "" ;
        int price = 0 ;
        
        try{
            String que  = "Select * from menu where menu_Id =" + "\"" + check_Menu + "\"";
            
            ResultSet  rs= st.executeQuery(que) ;
            rs.next() ; 
            res_name = rs.getString(3);
            item_name = rs.getString(4);
            price = rs.getInt(5); 
            
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }

        // now we have to add these values of order in the cart 
        try{
            String wish_Query = "INSERT INTO wishlist (user_Email, res_Name, item_Name, price)" + "VALUES (?, ?, ?, ?)";
            //Creating a PreparedStatement object
            PreparedStatement pst = con.prepareStatement(wish_Query);
            
            pst.setString(1,user_mail);
            pst.setString(2, res_name);
            pst.setString(3, item_name);
            pst.setInt(4,price);
            pst.execute();
        }

        catch (SQLException e)
        {
            
            e.printStackTrace();

        }

    
    }

    else{
        // for breaking the while loop 
        flag = 0 ; 
    }
   
}

}


public void display_Wishlist(Statement st){
    
    try{
        System.out.println("See Your Wishlist items :");
        String que  = "Select * from wishlist" ;
        ResultSet  rs= st.executeQuery(que) ;
        while(rs.next()){
            System.out.print("User Email: "+rs.getString(1)+", ");
            System.out.print("Restaurant Name: "+rs.getString(2)+", ");
            System.out.print("Food Item Name : "+rs.getString(3)+", ");
            System.out.print("Food Item Price: "+rs.getInt(4));
            System.out.println("");
           
        }
        System.out.println(" ");
        System.out.println("Thankyou for using Our Food App :) ");
    }
    
    catch (SQLException e)
    {
        
        e.printStackTrace();
    }

}

}

// wishlist class ends here 


// 3.Payment class for calculating the bills and using promo code for further payments



class CalculateBill extends user_Wishlist{

    //bill amount from 2nd part
    // we have to calculat the base bill from  the cart data base 
    public int cal_Base_Bill(Statement st){

      int base_Bill = 0 ; 
    
    
            try{
                String que  = "Select * from cart" ;
                ResultSet  rs= st.executeQuery(que) ;
                while(rs.next()){
                    int quantity =rs.getInt(4) ;
                    int price = rs.getInt(5) ;
                    int mul = quantity * price ; 
                    base_Bill += mul ; 
                }
            }
            
            catch (SQLException e)
            {
                
                e.printStackTrace();
            }
        
        return base_Bill ; 
    }

    // checking the conditio of the minimum amount 
    // if not we will add add_To_Cart option here 
        public int  minimumOrder(int amount,Statement st, int res_Id, Connection con) {
            System.out.println(" ");
            if (amount < 100) {
                
                System.out.println("Your base bill order is less than minimum order price \n Please add more items to your cart");
                show_Menu(st, res_Id);
                place_Order(st, res_Id, con);
                // now we will calculate again the bill 
                int base_Bill = cal_Base_Bill(st) ; 
                System.out.println("Your Base bill is : " +base_Bill);
                return base_Bill ; 
            }

            else{

                System.out.println("Your Base bill is : " +amount);
                return amount  ;

            }

        }


        //fetch latitute and longitude
        //pass into haversine
        //Now calculate delivery charges based on Distance
        //let the base price be 100 then according to distance
      

        //Calculate delivery based on distance between user and restro
        public int add_delivery_charges(int base_Bill, int res_Id){

            // beacuse we have calculated the estimated time which depends on the estimated distance between user 
            // loaction and restaurant coordinates , w can hardcode it directly because these values have been calculated once.
            double []  est_Distance = {22.82,19.59,7.36,22.99,27.44} ;// by haversine formula in ablove methods
           double distance = 0 ; 
            if(res_Id == 101){
             distance = est_Distance[0] ; 
            }
            else if(res_Id == 102){
            distance = est_Distance[1] ; 
            }

            else if(res_Id == 103){
                distance = est_Distance[2] ; 
                }
            else if(res_Id == 104){
                distance = est_Distance[3] ; 
                }

            else{
                distance = est_Distance[4] ; 
                }

            int basePrice = 100;
            int charges = (int) (basePrice + (distance * 0.5));// assumption 
            int bill_with_Charges = charges + base_Bill;
            System.out.println("Cost of your order with Delivery charges according the distance from your restaurant is : " + bill_with_Charges) ;
            return bill_with_Charges ; 
            
        }
         // adding the promo code !
        public double promo_code(int amount){
            System.out.println("Do you want to use Promo Codes");
            System.out.println("Choose out of the following options\n 1. SAVE50 \n 2. SAVE20 \n 3. NO");
            Scanner scn = new Scanner(System.in);
            double amt = amount; 
            int y = scn.nextInt();
            if (y == 1) {
                System.out.println("Promo Code SAVE50 has been applied");
                amt = amount*0.5;
            } else if (y == 2) {
                System.out.println("Promo Code SAVE20 has been applied");
                amt= amount*0.2;
            } else {
                System.out.println("Oops! No promo code has been applied");
            }

            System.out.println("Final bill after adding the promo code is : " + amt) ; 

            return amt ; 
            
        }
    }


// Payment mode 
// Here we have used the abstraction . 


abstract class PaymentMode{

    //There different modes of payment mode - UPI, NetBanking, Credit/Debit Card.
    abstract void UPI(double amount);
    abstract void NetBanking(double amount);
    abstract void Card(double  amount);

    }

class Payment extends PaymentMode {
    @Override
    public void UPI(double amount) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter UPI Id");
        String login=scn.nextLine();
        System.out.println("Enter UPI Pin");
        String pin=scn.nextLine();

        //
        System.out.println("--------------Connecting Securily-----------------");
        //
        System.out.println("Payment of Rs. " + amount + " through " + login +" is successfull ");
    }


    @Override
    public void NetBanking(double amount) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter bank name");
        String bank_name=scn.nextLine();
        System.out.println("Enter login id");
        String login=scn.nextLine();
        System.out.println("Enter password");
        String password=scn.nextLine();
        System.out.println("Enter 6 digit OTP Card");
        int otp=scn.nextInt();
        //
        System.out.println("--------------Connecting Securily-----------------");
        //
        System.out.println("Payment of Rs. " + amount + " through " + bank_name + " is successfull ");
    }

    @Override
    public void Card(double amount) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter Name on card");
        String card_name = scn.nextLine();
        System.out.println("Enter your 12 digit Card Number");
        long num=scn.nextLong();
        System.out.println("Enter Expiry Month & year ");
        int expiry_m = scn.nextInt();
        int expiry_y=scn.nextInt();
        System.out.println("Enter your 3 digit Security Code");
        int sc=scn.nextInt();
        //
        System.out.println("--------------Connecting Securily-----------------");
        //
        System.out.println("Payment of Rs. "+ amount + " by " + card_name + " through card number " + num + " is successfull ");

    }
}
class Check_Out {
    public void checkout(double amount)
    {
        String mode = null;
        Scanner scn = new Scanner(System.in);
        System.out.println("Please select mode of payment");
        System.out.println("1. New Credit/ Debit card \n" +
                "2. Pay with Net Banking \n" +
                "3. Pay with UPI");
        int y = scn.nextInt();
        if (y == 1) {
            mode = "Credit/Debit Card";
        } else if (y == 2) {
            mode = "NetBanking";
        } else {
            mode = "UPI";
        }

        Payment pm= new Payment();
        if(mode.equals("Credit/Debit Card")){

            //pass bill amount
            pm.Card(amount);
        }
        else if(mode.equals("NetBanking")){

            //pass bill amount
            pm.NetBanking(amount);
        }
        else{

            //pass bill amount
            pm.UPI(amount);
        }

    }
}


// 4. Tracking, Here we will do the 4th part of the project, to track the order 

class Calculate_Timing extends CalculateBill{


/*
It calculate the time constraint ,which says if the order time exceeds the estimated time by 10% 

*/

public int []  cal_Time_Constraint(Statement st,int res_Id){
    int []  time_Constraint  = new int [2]  ; 
    String  your_Restaurant = "" ; 
    try{
        String que  = "Select * from cart" ; 
        
        ResultSet  rs= st.executeQuery(que) ;

        while(rs.next()){
            your_Restaurant=rs.getString(2);
        }

        System.out.println("Your restaurant from where you have ordered is : " + your_Restaurant) ;
        

        // calculating the current time of system 

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        


        // calculating the estimated time for the chosen restaurant 
        double est_Time = cal_estimated_TIme(st, res_Id) ;  // uses haversine logic 

      // current time exceeds the estimated time by 10%,
       int temp = (int)(est_Time * 1.1)  ;
       temp = temp % 60 ; 
       int new_Minutes = (int) (minutes + temp ) ; 


       int t  = (int)( (est_Time * 1.1) / 60) ;  
       int new_Hour = hours + t ; 
       time_Constraint[0] = new_Hour ;
       time_Constraint[1] = new_Minutes ; 
       

    return time_Constraint ; 

        
    }

    catch (SQLException e)
    {
        
        e.printStackTrace();

    }

    return time_Constraint ; 

}

public double  cal_estimated_TIme(Statement st,int res_Id){
    

    double [] latitude = new double [5];
    double [] longitude = new double [5];
    double estimated_Time = 0 ; 

    
    latitude = fetch_latitude(st)  ; // accessing the latitude of restaurants 

    longitude = fetch_longitude(st) ; // fetching the longitude data .

    // user latitude and longitude assumption :
    double user_Lat = 35.3 ;
    double user_Long = 62.8 ; 

    int ptr = 0;
    if(res_Id == 101){
        ptr = 0 ; 
       }
       else if(res_Id == 102){
       ptr = 1; 
       }

       else if(res_Id == 103){
        ptr = 2; 
           }
       else if(res_Id == 104){
        ptr = 3;
           }

       else{
        ptr = 4;
           }
  
    
    // calling the cal_Estimated_Haversine

    double time = cal_estimatedTime_Haversine(user_Lat, latitude[ptr], user_Long, longitude[ptr]);
    estimated_Time = time ; 
   

    

  return estimated_Time ; 

}


}


class Track_Order {

   public void  track_Order(int [] time){
   
   System.out.println("Estimated  Time for delivery  in  HH:MM is : "+ time[0]+":"+time[1]);
   Scanner scn = new Scanner(System.in);
   Calendar calendar = Calendar.getInstance();
   int curr_hour = calendar.get(Calendar.HOUR_OF_DAY);
   int curr_minutes = calendar.get(Calendar.MINUTE);

if (time[0] < curr_hour && time[1]<curr_minutes){

    // that means user can cancel it

    System.out.println("So Sorry, Due to traffic issues we are not able to deliever in time, Do you want to Cancel the order?");
        	System.out.println("1. Yes");
        	System.out.println("2. No");
        	
        	int choice=scn.nextInt();
        	if(choice==1)
        	{
        		System.out.println("Your Order has been  Cancelled !");
        		System.exit(0);
        	}
        	else if(choice==2) {
        		System.out.println("Thankyou very much  for your patience. We will try to reach you soon !");


        	}

            else {
                System.out.println("Invalid input") ;
            }


}

else{
    // order may be reach within time 
    System.out.println("Have you got your order?");
        		System.out.println("1. Yes");
        		System.out.println("2. No");
        		System.out.println("3. Want to Call Customer Support");
        		
        		int choice=scn.nextInt();

                if (choice == 1){
                // give the choice to user to rate 
                rate_App() ;  

                }
// recursion will happen here 
                else if(choice == 2){
                
                System.out.println("Our delievery guy is on his way, Please be patient, He will reach soon !");
                System.out.println("Do you want to track your order again !" ) ;
                System.out.println("1.Yes" ) ;
                System.out.println("2.No" ) ;
                
                int track = scn.nextInt();
                if (track == 2){
                System.out.println("Please wait ! ,Order will be delievered soon ") ; 
                System.out.println("Thankyou  for using our App . Happy Eating :) ");
                }

                else{
                    track_Order(time); //  recursive call to track order 
                }


                }

                else{
                    System.out.println("Call on , Customer Care Service Number: 1-800-419-0157.");
                    System.out.println("Thankyou  for using our App . Happy Eating :) ");
                }
}

   

   }


   public void rate_App(){
    System.out.println("Do you want to Rate us?");
    System.out.println("1. Yes");
    System.out.println("2. No");
    
    Scanner scn=new Scanner(System.in);
    int rate =scn.nextInt();
    if(rate==2)
    {
        System.out.println("Thankyou  for using our App . Happy Eating :) ");
        
    }

    else{
        System.out.println("Rate us by giving any digit between 1 to 5" );
        System.out.println("1:Not Satisfied 2:Somewhat Satisfied 3:Happy  4:Very Happy :) and 5:Excellent Service!");
        System.out.println("give Rating Here :");
        int user_rate=scn.nextInt();
        System.out.println("Thankyou  for using our App . Happy Eating :) ");
    }
    
   }
}


public class App {
   

    
  // Main funtion 
    public static void main(String[] args) throws Exception {
    // objects of different classes      
     dataBase_Connection my_Sql = new dataBase_Connection();
     retrieve_Data my_Data = new retrieve_Data() ; 

     Connection connection=  my_Sql.getConnection();      // connecting the data base
     Statement st = connection.createStatement() ;
     // Login form starts from here 
     
     new LoginForm((connection));

     
     my_Data.fetch_User(st);
   
     /*
     for function overloading
     if you wanna see whole details of customer.
      boolean yes = true;
     my_Data.fetch_User(st,yes);

     */
     // let us make the tables now in the data base 
     System.out.println("1. Craving for food ! , Want to Order something right Now, Enter 1 ");
     System.out.println("2. Just want to explore Restaurants and Order Later, Enter 2");

     System.out.println();
     Scanner scn = new Scanner(System.in);
     int user_Ip = scn.nextInt() ; // user Input for Order or Wishlist :
     // Object of Visualization 
     if (user_Ip ==1) {
     // Order 
     System.out.println("Lets order something special for you :) ");

     // Order for a user  
     your_Order order = new your_Order();
     order.fetch_All_Restaurants(st,connection);
     order.cal_EstimatedTime(st);
     System.out.println("From where you want to order,Enter the Restaurant Id Number:");
     int choose_Restaurant =scn.nextInt();
     order.show_Menu(st,choose_Restaurant);
     order.place_Order(st,choose_Restaurant,connection) ; 
     order.alter_Cart(st,choose_Restaurant,connection);

     // payments 
     // making an object of CalculateBill
     CalculateBill bill = new CalculateBill();
    int base_Amount = bill.cal_Base_Bill(st); 
    int base_Bill = bill.minimumOrder(base_Amount,st,choose_Restaurant,connection); // updation in bill if less than 100
    int updated_Bill = bill.add_delivery_charges(base_Bill,choose_Restaurant) ;  // adding delivery charges with the distance
     
    double final_Bill = bill.promo_code(updated_Bill) ; // adding the promo code

    // implementing various payments methods
    Check_Out check = new Check_Out() ; // making an object of Check_Out

    check.checkout(final_Bill);

    Calculate_Timing time = new Calculate_Timing() ;

    int [] timings = time.cal_Time_Constraint(st, choose_Restaurant); // return new hours and new minutes 
    Track_Order user_tracking = new Track_Order() ;  
    user_tracking.track_Order(timings) ; 


     // tracking 


     }

     else{
     // Generate wishlist
     System.out.println("Lets explore the different restaurants !");

     user_Wishlist user_Wish = new user_Wishlist();

     user_Wish.fetch_All_Restaurants(st, connection); 
     user_Wish.cal_EstimatedTime(st);
     System.out.println("From where you want to add items in your Wishlist, Enter the Restaurant Id Number:");
     int choose_Restaurant =scn.nextInt();
     user_Wish.show_Menu(st, choose_Restaurant);
     user_Wish.add_To_WishList(st,choose_Restaurant,connection);
     // to display the wishlist 
     user_Wish.display_Wishlist(st);
    
    
     }

     
     my_Sql.disconnect();

     
    }
}


