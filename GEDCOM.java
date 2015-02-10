package SOLUTION;


import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
class Info
{


   String indi;
   String name;
   String sex;
   String birt;
   String fams;
   String famc;

   public Info(String id, String n, String s, String b, String fs, String fc)
   {


	 indi=id; 
     name = n; 
     sex = s; 
     birt = b; 
     fams = fs; 
     famc=fc;
     System.out.println( indi + " " + name + " "+ sex + " " + birt + " " + fams + " " + famc  );
   }
}


public class GEDCOM  {
	
    private static LinkedList<Info> linkIndviduals;
    private static LinkedList<Info> linkFamilies;

	  @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

	     
		    linkIndviduals = new LinkedList<Info>(); 
	    	FileInputStream  fis = new FileInputStream("/Users/hadoola/CS555-P03/src/SOLUTION/FamilyTree.ged");
            BufferedInputStream bis = new BufferedInputStream(fis);
    	    DataInputStream dis = new DataInputStream(bis);
            String Line;
            String name=null,sex = null,birt=null,famc=null,fams=null, id=null;
	        int flag=0;
	        
            while (dis.available() != 0)
            {
            Line=dis.readLine();
	       
	        String[] info = Line.split(" ");
            String Level = info[0];
            String Tag=null;
            System.out.println(Line);
            System.out.println("Level:"+Level);
            for(int l=0; l<info.length; l++)
            {
                if(!(Character.isDigit(info[l].charAt(0))) && !(info[l].matches("^.*[^a-zA-Z0-9 ].*$")) )
                {
                  Tag=info[l];
                  System.out.println("Tag Name   "+ info[l] + "\n");
                  break;
                }
            }
          /*   
           
           if (Tag.equals("INDI")|| (Tag.equals("NAME")) ||(Tag.equals("SEX"))||(Tag.equals("BIRT"))
            		|| (Tag.equals("DEAT")) ||( Tag.equals("FAMC"))||( Tag.equals("FAMS")) ||(Tag.equals("FAM"))
            		||( Tag.equals("MARR")) ||( Tag.equals("HUSB"))|| (Tag.equals("WIFE")) || (Tag.equals("CHIL"))|| 
            				(Tag.equals("DIV") )|| (Tag.equals("DATE")) || (Tag.equals("TRLR"))|| (Tag.equals("NOTE")))
            
            System.out.println("Tag:"+Tag);
 
            else 
            	System.out.println("Invalid Tag");
          */  
            if(flag!=0)
            {
            if (Tag.equals("NAME")) 
            {
            	name=info[2];
            	flag++;
            }
            else if (Tag.equals("SEX")) 
            {
            	sex=info[2];
            	flag++;
            }
    	      
            else if (Tag.equals("DATE")) 
            {
            	birt=info[2]+info[3]+info[4];
            	flag++;
            }
            else if (Tag.equals("FAMS")) 
            {
            	fams=info[2];
            	flag++;
            }
            else if (Tag.equals("FAMC")) 
            {
            	famc=info[2];
            	flag++;
            }
          
           }
            
          if(Tag.equals("INDI"))
          {
        	  if( id!=null){
        		  Info individualData = new Info(id, name,sex,birt,fams,famc);
                  linkIndviduals.add(individualData);
                  flag = 0;
           	      id = null;
                  name=null;
                  sex =null;
                  birt=null;
                  famc=null;
                  fams=null;  
        	  }
        	  id = info[1];
        	  flag++;
          }
        }
	      fis.close();
	      bis.close();
	      dis.close();
	   	   
	      
	  }
	  
	  private static void StoreFamilies(){
		  
	  }
	  
}
	  
