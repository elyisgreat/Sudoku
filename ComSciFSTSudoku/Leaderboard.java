//Author: Ethan Orlander
//Date created: June 1, 2016
//Date modified: June 1, 2016
package ComSciFSTSudoku;
import java.sql.*;
import java.util.Arrays;
/**A utility class that stores and manages upload of the leaderboard.*/
public class Leaderboard implements Comparable<Leaderboard>{
  /**The SQL URL.*/
  public static String url = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5122097";
  /**The SQL username.*/
  public static String user = "sql5122097";
  /**The SQL password.*/
  public static String password = "9B7vypsJbv";
  /**The SQL command.*/
  public static String sql;
  /**The Leaderboard length.*/
  public static int length = 0;
  /**The Leaderboard array representing the Leaderboard.*/
  public static Leaderboard[] theLeaderboard=new Leaderboard[0];
  /**Leaderboard entry name.*/
  private String name;
  /**Leaderboard entry time.*/
  private long time;
  /**Leaderboard entry difficulty. Is inverted for sorting purposes.*/
  private int invertedDifficulty;
  /**Creates a new Leaderboard object.*/
  private Leaderboard(String nam, long tim, int invertedDiff){
    name=nam.substring(0,nam.length()<16?nam.length():16);
    time=tim;
    invertedDifficulty=invertedDiff;
  }
  /**Returns the name entry of this Leaderboard object.*/
  public String getName(){
    return name;
  }
  /**Returns the time entry of this Leaderboard object.*/
  public long getTime(){
    return time;
  }
  /**Returns the invertedDifficulty entry of this Leaderboard object.*/
  public int getInvertedDifficulty(){
    return invertedDifficulty;
  }
  /**Compares this Leaderboard object with l. Used for ranking.*/
  public int compareTo(Leaderboard l){
    if(this.time==l.time){
      if(this.invertedDifficulty==l.invertedDifficulty){return 0;}
      else if(this.invertedDifficulty>l.invertedDifficulty){return 1;}else{return -1;}
    }
    else if(this.time>l.time){return 1;}else{return -1;}
  }
  /**Pulls the Leaderboard from the SQL database.*/
  public static void getLeaderboard(){
    try{
      /**Connect to the MySQL database*/
      Connection myConn = DriverManager.getConnection(url, user, password); 
      /**Create a statement*/
      Statement myStmt = myConn.createStatement();
      /**Copy current leaderboard*/
      ResultSet myRs = myStmt.executeQuery("select * from Leaderboard");
      myRs.last();
      length = myRs.getRow();
      theLeaderboard = new Leaderboard[Math.min(length,18)];
      myRs.first();
      for(int c=0;c<length;c++){
        theLeaderboard[c] = new Leaderboard(myRs.getString("Name"),myRs.getLong("Score"),myRs.getInt("Difficulty")); 
        myRs.next();
      }
      myConn.close();
    }catch (Exception e){e.printStackTrace();}
    Arrays.sort(theLeaderboard);
  }
  /**Adds an entry to the Leaderboard.*/
  public static void addEntry(String nam, long tim, int invertedDiff){
    length++;
    theLeaderboard=Arrays.copyOf(theLeaderboard,length);
    theLeaderboard[length-1] = new Leaderboard(nam, tim, invertedDiff);
    Arrays.sort(theLeaderboard);
    if(length>18){
      length=18;
      theLeaderboard=Arrays.copyOf(theLeaderboard,length);
    }
  }
  
  /**Organizes the Leaderboard.*/
  public static void organizeLeaderboard(){Arrays.sort(theLeaderboard);}
  /**Uploads the Leaderboard to the SQL database.*/
  public static void updateSQLData(){
    try{
      /**Connect to the MySQL database*/
      Connection myConn = DriverManager.getConnection(url, user, password); 
      /**Create a statement*/
      Statement myStmt = myConn.createStatement();
      /**Execute SQL query*/
      sql = "delete from Leaderboard";
      
      myStmt.executeUpdate(sql);
      
      for(int i = 0; i < length; i++){
      /**Execute SQL query*/
      sql = "insert into Leaderboard "
          + " (Name, Score, Difficulty)"
          + " values ('"+ theLeaderboard[i].name + "', '" + theLeaderboard[i].time + "', '" + theLeaderboard[i].invertedDifficulty + "')";
      myStmt.executeUpdate(sql);
      }
      myConn.close();
      }catch (Exception e){e.printStackTrace();}
  }
  
}