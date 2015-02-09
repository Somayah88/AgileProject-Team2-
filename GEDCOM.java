
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class GEDCOM  {
	


	  @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

	    
	   
	    	FileInputStream  fis = new FileInputStream("FamilyTreeP.ged");
            BufferedInputStream bis = new BufferedInputStream(fis);
    	    DataInputStream dis = new DataInputStream(bis);
            String Line;
	     
            
            while (dis.available() != 0) {
             Line=dis.readLine();
	       
	        String[] info = Line.split(" ");
            String Level = info[0];
            String Tag = info[1];
          
            System.out.println(Line);
            System.out.println("Level:"+Level);
           
            if (Tag.equals("INDI")|| (Tag.equals("NAME")) ||(Tag.equals("SEX"))||(Tag.equals("BIRT"))
            		|| (Tag.equals("DEAT")) ||( Tag.equals("FAMC"))||( Tag.equals("FAMS")) ||(Tag.equals("FAM"))
            		||( Tag.equals("MARR")) ||( Tag.equals("HUSB"))|| (Tag.equals("WIFE")) || (Tag.equals("CHIL"))|| 
            				(Tag.equals("DIV") )|| (Tag.equals("DATE")) || (Tag.equals("TRLR"))|| (Tag.equals("NOTE")))
            
            System.out.println("Tag:"+Tag);
 
            else 
            	System.out.println("Invalid Tag");

        
	      }

	    
	      fis.close();
	      bis.close();
	      dis.close();

	   
	  }
	}


