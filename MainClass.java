//Author: Zachary Minuk, Ely Golden, Ethan Orlander
//Purpose: The main class of sudoku game, runs methods from other classes
//Date created: March 26, 2016
//Date modified: April 26, 2016
import java.awt.event.*;
import java.awt.Component;
import SudokuClass.Sudoku;

public class MainClass { 
  public static void main (String [] args) throws Exception {
    MethodsGUI.intro();
  }//end of main
  
  //Below are action listeners that run certain methods when certain buttons are pressed
  static class play implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.infoWindow();
    }}//displays the sudoku board and info screen when play button is pressed
  
  static class help implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.helpMethod();
    }}//runs the help method when help button is pressed
  
  static class leaderboard implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.leaderboardMethod();
    }}//runs the leaderboard method when leaderboard button is pressed
  
  static class option implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.optionMethod();
    }}//runs the option method when option button is pressed
  
  static class back implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.mainScreen();
    }}//runs the main screen method when the 'return home' button is pressed
  
  static class quit implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      System.exit(0);
    }}//quits the game when quit button is pressed
  
  static class check implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.solutionDisplay();
    }}
  
  static class locate implements FocusListener{
    public void focusGained (FocusEvent e) {
      if (MethodsGUI.gameOver==false) {
        int el=0;
        Component theField=e.getComponent();
        while(el<81){
          if(MethodsGUI.arrayFields[el]==theField){break;}
          el++;
        }//end of if
        MethodsGUI.arrayFields[el].selectAll();
        MethodsGUI.backupText=MethodsGUI.arrayFields[el].getText();
        //while(MethodsGUI.arrayFields[el].getText().equals(MethodsGUI.backupText)){
        //  if(MethodsGUI.arrayFields[el].getText().length()>1){MethodsGUI.arrayFields[el].setText(MethodsGUI.backupText);}
        //  if(MethodsGUI.arrayFields[el].getText().matches("[^1-9\\ ]")==false){MethodsGUI.arrayFields[el].setText(MethodsGUI.backupText);}
        //  if(MethodsGUI.arrayFields[el].getText().equals(" ")){MethodsGUI.arrayFields[el].setText("");}
        //}//end of while loop
        System.out.println("Success");
      }//end of if
    }//end of focus gained
    public void focusLost (FocusEvent e) {
      if (MethodsGUI.gameOver==false) {
      }//end of if
    }//end of focus lost
  }//end of locate
  
  static class key implements KeyListener {
    public void keyTyped (KeyEvent e) {

    }
    public void keyPressed (KeyEvent e) {}
    public void keyReleased (KeyEvent e) {}
  }
  
}//end of class