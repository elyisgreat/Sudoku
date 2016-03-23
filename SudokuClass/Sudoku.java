import java.util.*;
public class Sudoku{
  
  public static final int MIN_SIZE=2;
  public static final int MAX_SIZE=11;
  
  private byte[] tiles;
  
    private static int[] appendToArray(int[] a,int[] b){
    int oldLength=a.length;
    a=Arrays.copyOf(a,a.length+b.length);
    for(int c=0;c<b.length;c++){
      a[oldLength+c]=b[c];
    }
    return a;
  }
  
  private static byte[] stringArrayToByteArray(String[] arr){
    byte[] b=new byte[arr.length];
    for(int c=0;c<arr.length;c++){
      b[c]=Byte.parseByte(arr[c]);
    }
    return b;
  }
  
    private static byte[] removeElementVal(byte[] a,byte elementVal){
    byte[] b=new byte[0];
    for(int c=0;c<a.length;c++){
      if(a[c]!=elementVal){
        b=Arrays.copyOf(b,b.length+1);
        b[b.length-1]=a[c];
      }
    }
    return b;
  }
    
    private static int[] removeElement(int[] arr,int element){
      int[] b=new int[arr.length-1];
      int subcount=0;
      for(int c=0;c<arr.length;c++){
        if(c!=element){b[subcount]=arr[c];subcount++;}
      }
      return b;
    }
  
    private static boolean allEmementsUnique(byte[] b){
     if(b.length>256){return false;}
      for(int i=0;i<b.length-1;i++){
        for(int j=i+1;j<b.length;j++){
          if(b[i]==b[j]){return false;}
         }
        }              
    return true;   
  }
    
   private static int[] removeDuplicates(int[] a){
      Arrays.sort(a);
      int[] b=new int[] {a[0]};
      for(int c=1;c<a.length;c++){
        if(a[c]!=a[c-1]){b=Arrays.copyOf(b,b.length+1);b[b.length-1]=a[c];}
      }
      return b;
   }
  
    private static boolean arrayContains(int[] array,int val){
      for(int c=0;c<array.length;c++){
        if(array[c]==val){return true;}
      }
      return false;
    }
    
    private static int[] naturalNumbersUntil(int num){
      int[] nums=new int[num];
      for(int c=1;c<num;c++){
        nums[c]=nums[c-1];
        nums[c]++;
      }
      return nums;
    }
    
    public Sudoku(byte[] tileset){
      int size=(int)Math.pow(tileset.length,1d/4d);
      if((int)Math.pow(size,4)!=tileset.length||size<MIN_SIZE||size>MAX_SIZE){throw new IllegalArgumentException(tileset.length+" is an invalid number of tiles");}
      for(int c=0;c<tileset.length;c++){
        if(tileset[c]<-1||tileset[c]>=(size*size)){throw new IllegalArgumentException("Tile "+c+" has an invalid value");}
         }
      tiles=new byte[tileset.length];
      for(int c=0;c<tileset.length;c++){
        tiles[c]=tileset[c];
      }
    }
    
    public Sudoku(int size){
      if(size<MIN_SIZE||size>MAX_SIZE){throw new IllegalArgumentException(size+" is an invalid sudoku size");}
      tiles=new byte[(int)Math.pow(size,4)];
      for(int c=0;c<tiles.length;c++){
        tiles[c]=-1;
      }
    }
    
    public Sudoku(int size,int missingTiles,Random random){
      this(size,missingTiles,random,random);
    }
    
    //TODO: Random Sudoku Generator
    public Sudoku(int size,int missingTiles,Random random1,Random random2){
      this(size);
    }

  public Sudoku(byte[] tileset,int missingTiles,Random random){
    Sudoku s=new Sudoku(tileset);
    if(s.tiles.length<=missingTiles){s=new Sudoku(s.getSize());}
    else{
    int[] potentialMissingTiles=naturalNumbersUntil(tileset.length);
    for(int c=0;c<missingTiles;c++){
         int rnd=random.nextInt(potentialMissingTiles.length);
         s.tiles[potentialMissingTiles[rnd]]=-1;
         potentialMissingTiles=removeElement(potentialMissingTiles,rnd);
        }
       }
     tiles=s.tiles;
  }
  
  public String toString(){
    String str="";
    for(int c=0;c<tiles.length;c++){
      str=str+tiles[c];
      if(c!=tiles.length-1){str=str+"|";}
    }
    return str;
  }
  
  public static Sudoku constructFromString(String stringForm){
    return new Sudoku(stringArrayToByteArray(stringForm.split("\\|")));
  }
  
  public String toTechnicalString(){
    String str="";
    for(int c=0;c<tiles.length;c++){
      str=str+tiles[c];
      if(c!=tiles.length-1){str=str+"\u001f";}
    }
    return str;
  }
  
  public static Sudoku constructFromTechnicalString(String stringForm){
    return new Sudoku(stringArrayToByteArray(stringForm.split("\u001f")));
  }
  
  public static Sudoku copy(final Sudoku obj){
    return new Sudoku(obj.tiles);
  }
  
  public boolean equals(Object obj){
    return Arrays.equals(tiles,((Sudoku)obj).tiles);
  }
  
  public int hashCode(){
    return Arrays.hashCode(tiles);
  }
  
  public byte[] getTiles(){
    byte[] tileset=new byte[tiles.length];
    for(int c=0;c<tiles.length;c++){
      tileset[c]=tiles[c];
    }
    return tileset;
  }
  
  public int getSize(){
    return (int)Math.pow(tiles.length,1d/4d);
  }
  
  public byte getTile(int tilePosition){
    return tiles[tilePosition];
  }
  
  public byte getTile(int row,int column){
    return tiles[getTilePosition(row,column)];
  }
  
  public byte getTileAtBox(int box,int pos){
    return tiles[getTilePositionAtBox(box,pos)];
  }
  
  public void setTile(int tileNumber,byte tileValue){
    if(tileValue<-1||tileValue>=(getSize()*getSize())){throw new IllegalArgumentException("Tile "+tileValue+" has an invalid value");}
    tiles[tileNumber]=tileValue;
  }
  
  public void setTile(int row,int column,byte tileValue){
    if(tileValue<-1||tileValue>=(getSize()*getSize())){throw new IllegalArgumentException("Tile "+tileValue+" has an invalid value");}
    tiles[getTilePosition(row,column)]=tileValue;
  }
  
  public void setTileAtBox(int box,int pos,byte tileValue){
   if(tileValue<-1||tileValue>=(getSize()*getSize())){throw new IllegalArgumentException("Tile "+tileValue+" has an invalid value");}
   tiles[getTilePositionAtBox(box,pos)]=tileValue;
  }
  
    public int getTilePosition(int row,int column){
    return getSize()*getSize()*row+column;
  }
  
  public int getTilePositionAtBox(int box,int pos){
    return getSize()*(box/getSize())*getSize()*getSize()+getSize()*(box%getSize())+getSize()*getSize()*(pos/getSize())+(pos%getSize());
  }
  
  public int rowOf(int tilePosition){
    return tilePosition/(getSize()*getSize());
  }
  
  public int columnOf(int tilePosition){
    return tilePosition%(getSize()*getSize());
  }
  
  public int boxOf(int tilePosition){
    return (tilePosition/(getSize()*getSize()*getSize()))*getSize()+(tilePosition%(getSize()*getSize()))/getSize();
  }
  
  public int boxPositionOf(int tilePosition){
    return ((tilePosition/(getSize()*getSize()))%getSize())*getSize()+(tilePosition%getSize());
  }
  
  public byte[] getRow(int row){
    byte[] rowTiles=new byte[getSize()*getSize()];
    for(int c=0;c<rowTiles.length;c++){
      rowTiles[c]=tiles[getTilePosition(row,c)];
    }
    return rowTiles;
  }
  
  public byte[] getColumn(int column){
    byte[] columnTiles=new byte[getSize()*getSize()];
    for(int c=0;c<columnTiles.length;c++){
      columnTiles[c]=tiles[getTilePosition(c,column)];
    }
    return columnTiles;
  }
  
  public byte[] getBox(int box){
    byte[] boxTiles=new byte[getSize()*getSize()];
    for(int c=0;c<boxTiles.length;c++){
      boxTiles[c]=tiles[getTilePositionAtBox(box,c)];
    }
    return boxTiles;
  }
  
  public boolean isSolvedSudoku(){
    return tiles.length==removeElementVal(tiles,(byte)-1).length;
  }
  
  public boolean isValidSudoku(){
    for(int c=0;c<getSize()*getSize();c++){
      if((allEmementsUnique(getRow(c))&&allEmementsUnique(getColumn(c))&&allEmementsUnique(getBox(c)))==false){return false;}
    }
    return true;
  }
  
  public boolean isValidSolvedSudoku(){
    return isSolvedSudoku()&&isValidSudoku();
  }
  
  public int[] unsolvedTilePositions(){
    int[] tilepos=new int[0];
    for(int c=0;c<tiles.length;c++){
      if(tiles[c]==-1){tilepos=appendToArray(tilepos,new int[] {c});}
    }
    return tilepos;
  }
  
  public int[] invalidTilePositions(){
    int[] tilepos=new int[0];
    byte[] currentRow;
    byte[] currentColumn;
    byte[] currentBox;
    for(int c=0;c<getSize()*getSize();c++){
      currentRow=getRow(c);
      currentColumn=getColumn(c);
      currentBox=getBox(c);
      for(int d=0;d<getSize()*getSize()-1;d++){
        for(int e=d+1;e<getSize()*getSize();e++){
          if(currentRow[d]==currentRow[e]&&currentRow[d]!=-1&&currentRow[e]!=-1){
            int app1=getTilePosition(c,d);
            int app2=getTilePosition(c,e);
            if(arrayContains(tilepos,app1)==false){tilepos=appendToArray(tilepos,new int[] {app1});}
            if(arrayContains(tilepos,app2)==false){tilepos=appendToArray(tilepos,new int[] {app2});}
            }
          if(currentColumn[d]==currentColumn[e]&&currentColumn[d]!=-1&&currentColumn[e]!=-1){
            int app1=getTilePosition(d,c);
            int app2=getTilePosition(e,c);
            if(arrayContains(tilepos,app1)==false){tilepos=appendToArray(tilepos,new int[] {app1});}
            if(arrayContains(tilepos,app2)==false){tilepos=appendToArray(tilepos,new int[] {app2});}
            }
          if(currentBox[d]==currentBox[e]&&currentBox[d]!=-1&&currentBox[e]!=-1){
            int app1=getTilePositionAtBox(c,d);
            int app2=getTilePositionAtBox(c,e);
            if(arrayContains(tilepos,app1)==false){tilepos=appendToArray(tilepos,new int[] {app1});}
            if(arrayContains(tilepos,app2)==false){tilepos=appendToArray(tilepos,new int[] {app2});}
            }
        }
      }
    }
    Arrays.sort(tilepos);
    return tilepos;
  }
  
  public int[] conflictingTilePositions(int tilePosition){
    if(tiles[tilePosition]==-1){return new int[0];}
    int[] conflicts=new int[0];
    for(int c=0;c<getSize()*getSize();c++){
      if(getTile(rowOf(tilePosition),c)==tiles[tilePosition]&&getTilePosition(rowOf(tilePosition),c)!=tilePosition){
        int app=getTilePosition(rowOf(tilePosition),c);
        if(arrayContains(conflicts,app)==false){conflicts=appendToArray(conflicts,new int[] {app});}
      }
      if(getTile(c,columnOf(tilePosition))==tiles[tilePosition]&&getTilePosition(c,columnOf(tilePosition))!=tilePosition){
        int app=getTilePosition(c,columnOf(tilePosition));
        if(arrayContains(conflicts,app)==false){conflicts=appendToArray(conflicts,new int[] {app});}
      }
      if(getTileAtBox(boxOf(tilePosition),c)==tiles[tilePosition]&&getTilePositionAtBox(boxOf(tilePosition),c)!=tilePosition){
        int app=getTilePositionAtBox(boxOf(tilePosition),c);
        if(arrayContains(conflicts,app)==false){conflicts=appendToArray(conflicts,new int[] {app});}
      }
    }
    Arrays.sort(conflicts);
    return conflicts;
  }
  
  public int[] conflictingTilePositions(int row,int column){
    return conflictingTilePositions(getTilePosition(row,column));
  }
  public int[] conflictingTilePositionsAtBox(int box,int pos){
    return conflictingTilePositions(getTilePositionAtBox(box,pos));
  }
  
  public static void main(String[] args){
  }
}