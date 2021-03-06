//Author: Zachary Minuk, Ely Golden
//Date created: March 26, 2016
//Date modified: June 1, 2016
package ComSciFSTSudoku;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Arrays;
import ComSciFSTSudoku.org.json.*;
import ComSciFSTSudoku.ReadWrite.FileIO;
import ComSciFSTSudoku.SudokuClass.Sudoku;
/**The main class of sudoku game, runs methods from other classes. */
public class MainClass {
  /**The main method.*/
  public static void main (String [] args) throws Exception {
    MethodsGUI.intro();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      /**Runs whenever the game is quit.*/
      public void run() {
        MethodsGUI.fileData=new JSONObject();
        MethodsGUI.fileData.put("moves",MethodsGUI.moves);
        MethodsGUI.fileData.put("solvedGame",MethodsGUI.solvedGame.toString());
        MethodsGUI.fileData.put("game",MethodsGUI.game.toString());
        MethodsGUI.fileData.put("originalGame",MethodsGUI.originalGame.toString());
        MethodsGUI.fileData.put("gameOver",MethodsGUI.gameOver);
        MethodsGUI.fileData.put("timeKeeper",MethodsGUI.timeKeeper);
        MethodsGUI.fileData.put("invalidTilesIndex",MethodsGUI.invalidTilesIndex);
        MethodsGUI.fileData.put("actualDifficulty",MethodsGUI.actualDifficulty);
        MethodsGUI.fileData.put("difficultyIndex",MethodsGUI.difficultyIndex);
        try{
          FileIO.writeBinary(MethodsGUI.fileData.toString().getBytes("UTF-8"),new RandomAccessFile("data/gamestate.json","rw"));
        }catch(Exception e){};
      }
    }));
  }//end of main
  
  //Below are action listeners that run certain methods when certain buttons are pressed
  
  /**Action Listener for play button.*/
  static class play implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.load.setText("Loading...");
      MethodsGUI.playButton.setEnabled(false);
    }}//displays the sudoku board and info screen when play button is pressed
  
  /**Action Listener for resume button.*/
  static class resume implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.gridReveal();
    }}//runs the grid reveal method when you press resume game button
  
  /**Action Listener for help button.*/
  static class help implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.helpMethod();
    }}//runs the help method when help button is pressed
  
  /**Action Listener for leaderboard button.*/
  static class leaderboard implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.leaderboardMethod();
    }}//runs the leaderboard method when leaderboard button is pressed
  
  /**Action Listener for options button.*/
  static class option implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.optionMethod();
    }}//runs the option method when option button is pressed
  
  /**Action Listener for back button.*/
  static class back implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.invalidTilesIndex = MethodsGUI.hintBox.getSelectedIndex();
      MethodsGUI.mainScreen();
    }}//runs the main screen method when the 'return home' button is pressed
  
  /**Action Listener for show solution button.*/
  static class check implements ActionListener {
    /**ActionPerformed method.*/
    public void actionPerformed (ActionEvent e) {
      MethodsGUI.solutionDisplay();
    }}//runs the solution display method when you press show solution button
  
  /**Window focus Listener for pause function.*/
  static class pause implements WindowFocusListener{
    /**WindowGainedFocus method.*/
    public void windowGainedFocus (WindowEvent e) {}
    /**WindowLostFocus method.*/
    public void windowLostFocus (WindowEvent e) {if(MethodsGUI.undoPossible){MethodsGUI.mainScreen();}}
  }//runs the main screen method when the window loses focus
  
  /**Focus Listener that determines when a JTextArea is pressed and determines which of the 81 it is*/
  static class locate implements FocusListener{
    /**FocusGained method.*/
    public void focusGained (FocusEvent e) {
      if (MethodsGUI.gameOver==false) {
        MethodsGUI.selectedField=0;
        Component theField=e.getComponent();
        while(MethodsGUI.selectedField<81){
          if(MethodsGUI.arrayFields[MethodsGUI.selectedField]==theField){break;}
          MethodsGUI.selectedField++;
        }//end of if
        MethodsGUI.backupText=MethodsGUI.arrayFields[MethodsGUI.selectedField].getText().replaceAll("[\u200b]","");//replaces with a zero width space
        MethodsGUI.arrayFields[MethodsGUI.selectedField].selectAll();
      }//end of if
    }//end of focus gained
    /**FocusLost method.*/
    public void focusLost (FocusEvent e) {
      if (MethodsGUI.gameOver==false) {
        if(MethodsGUI.game.equals(MethodsGUI.solvedGame)){
          MethodsGUI.gameOver();
        }//end of if
      }//end of if
    }//end of focus lost
  }//end of locate
  
  /**Key Listener that handles Undo function.*/
  static class undo implements KeyListener {
    /**KeyTyped method.*/
    public void keyTyped (KeyEvent e) {
      if(e.getKeyChar()=='Z'&&MethodsGUI.moves.length>0&&MethodsGUI.undoPossible){
        MethodsGUI.frame1.requestFocusInWindow();
        byte undoTile=MethodsGUI.getInit(MethodsGUI.moves[MethodsGUI.moves.length-1]);
        int undoPos=MethodsGUI.getPos(MethodsGUI.moves[MethodsGUI.moves.length-1]);
        MethodsGUI.game.setTile(undoPos,undoTile);
        MethodsGUI.arrayFields[undoPos].setText(undoTile==-1?" ":""+(undoTile+1));
        MethodsGUI.moves=Arrays.copyOf(MethodsGUI.moves,MethodsGUI.moves.length-1);
        MethodsGUI.checkInvalidTiles();
      }//end of if statement
    }//end of key typed
    /**KeyPressed method.*/
    public void keyPressed (KeyEvent e) {}//must be here to run
    /**KeyReleased method.*/
    public void keyReleased (KeyEvent e) {}//must be here to run
  }//end of undo key listener
  
  /**Key Listner that finds out which key is typed in JTextArea and if not 1-9 does not display it. 
    * Number will turn red if there is a conflicting number in same sub-grid, row, or column*/
  static class key implements KeyListener {
    /**KeyTyped method.*/
    public void keyTyped (KeyEvent e) {
      if((""+e.getKeyChar()).matches("[1-9\\ ]")||e.getKeyChar()==8){
        if(e.getKeyChar()==8){MethodsGUI.arrayFields[MethodsGUI.selectedField].setText(" ");}
        else{MethodsGUI.arrayFields[MethodsGUI.selectedField].setText("");}
        byte tileBeingSet=MethodsGUI.game.getTile(MethodsGUI.selectedField);
        byte tileToSet=(byte)(e.getKeyChar()==' '||e.getKeyChar()==8?-1:Character.getNumericValue(e.getKeyChar())-1);
        MethodsGUI.game.setTile(MethodsGUI.selectedField,tileToSet);
        int moveToRecord=MethodsGUI.recordMove(MethodsGUI.selectedField,tileBeingSet,tileToSet);
        if(MethodsGUI.getInit(moveToRecord)!=MethodsGUI.getFin(moveToRecord)){
          MethodsGUI.moves=Arrays.copyOf(MethodsGUI.moves,MethodsGUI.moves.length+1);
          MethodsGUI.moves[MethodsGUI.moves.length-1]=moveToRecord;
        }//only records moves where the value of the tile actually changes
        MethodsGUI.checkInvalidTiles();
        MethodsGUI.frame1.requestFocusInWindow();
      }//end of if
      else{
        e.setKeyChar('\u200b');
        MethodsGUI.arrayFields[MethodsGUI.selectedField].setText(MethodsGUI.backupText);
        MethodsGUI.frame1.requestFocusInWindow();
      }//end of if
    }//end of key typed
    /**KeyPressed method.*/
    public void keyPressed (KeyEvent e) {}//must be here to run
    /**KeyReleased method.*/
    public void keyReleased (KeyEvent e) {}//must be here to run
  }//end of key listener
}//end of class