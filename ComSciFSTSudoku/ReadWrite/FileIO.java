package ComSciFSTSudoku.ReadWrite;
import java.io.*;
/** A utility class used for reading/writing files into/out of memory. */
public class FileIO{
  private static long ceilDiv(long x, long y) {
    long r = x / y;
    if ((x ^ y) >= 0 && (r * y != x)) {
      r++;
    }
    return r;
  }
  /**Reads the contents of fileToRead into a String. */
  public static String readFile(FileReader fileToRead) throws Exception{
    BufferedReader br=new BufferedReader(fileToRead);
    String importedFile="";
    String line="";
    do{
      line=br.readLine();
      if(line!=null){importedFile=importedFile+line+"\n";}
    }while(line!=null);
    return importedFile;
  }
  /**Writes fileToWrite to the where2WriteIt. This completely overwrites the contents of where2WriteIt. */
  public static void writeFile(String fileToWrite,FileWriter where2WriteIt) throws Exception{
    PrintWriter pw=new PrintWriter(where2WriteIt);
    pw.print(fileToWrite);
    pw.close();
  }
  /**Reads the contents of data into a 2-dimensional byte array. Each byte array represents a gigabyte of data. */
  public static byte[][] readExtendedBinary(RandomAccessFile data)throws Exception{
    data.seek(0);
    long gigabytes=ceilDiv(data.length(),1073741824);
    long subbytes=(data.length()==0)?0:(((data.length()-1)%1073741824)+1);
    byte[][] imported=new byte[(int)gigabytes][(int)subbytes];
    for(int count=0;count<imported.length;count++){
      data.read(imported[count]);
    }
    return imported;
  }
  /**Reads the contents of data into a byte array.*/
  public static byte[] readBinary(RandomAccessFile data)throws Exception{
    data.seek(0);
    int fileLength=((data.length()>2147483647)?2147483647:(int)data.length());
    byte[] imported=new byte[fileLength];
    data.read(imported);
    return imported;
  }
  /**Writes data into the RandomAccessFile writeTo. This completely overwrites the contents of writeTo.*/
  public static void writeExtendedBinary(byte[][] data,RandomAccessFile writeTo)throws Exception{
    writeTo.setLength(0);
    for(int count=0;count<data.length;count++){
      writeTo.write(data[count]);
    }
  }
  /**Writes data into the RandomAccessFile writeTo. This completely overwrites the contents of writeTo.*/
  public static void writeBinary(byte[] data, RandomAccessFile writeTo)throws Exception{
    writeTo.setLength(0);
    writeTo.write(data);
  }
  public static void main(String[] args) throws Exception{
  }
}