package SOLUTION;


import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
class Info
{


   String indi, name, sex, birt, fams, famc;
   String famid, ftag, IndiTag;
   
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
   
   public Info(String famId, String fTag, String Inditag)
   {
	 famid = famId;
	 ftag = fTag;
	 IndiTag = Inditag;
     System.out.println( famId + " " + fTag +  " " + Inditag);
   }
}


public class GEDCOM  {
	
    private static LinkedList<Info> linkIndviduals;
    private static LinkedList<Info> linkFamilies;

	  @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

	     
		    linkIndviduals = new LinkedList<Info>(); 
		    linkFamilies = new LinkedList<Info>(); 
		    
	    	FileInputStream  fis = new FileInputStream("/Users/hadoola/CS555-P03/src/SOLUTION/FamilyTree.ged");
    	    DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
    	    
            String Line;
            String name=null,sex = null,birt=null,famc=null,fams=null, id=null;
            String FamilyId=null, tag=null, IndiTag=null;
	        int indiFlag=0, famFlag=0;
	        
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
            if(indiFlag!=0)
            {
            if (Tag.equals("NAME")) 
            {
            	name=info[2];
            	indiFlag++;
            }
            else if (Tag.equals("SEX")) 
            {
            	sex=info[2];
            	indiFlag++;
            }
    	      
            else if (Tag.equals("DATE")) 
            {
            	birt=info[2]+info[3]+info[4];
            	indiFlag++;
            }
            else if (Tag.equals("FAMS")) 
            {
            	fams=info[2];
            	indiFlag++;
            }
            else if (Tag.equals("FAMC")) 
            {
            	famc=info[2];
            	indiFlag++;
            }
          
           }
           
            if(famFlag!=0)
            {
            if (Tag.equals("HUSB")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	famFlag++;
            	System.out.print(tag+ " " + IndiTag);
            }
            else if (Tag.equals("WIFE")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	famFlag++;
            	System.out.print(tag+ " " + IndiTag);
            }
    	      
            else if (Tag.equals("CHIL")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	famFlag++;
            	System.out.print(tag+ " " + IndiTag);
            }
          
           }
            
          if(Tag.equals("INDI") || Tag.equals("FAM"))
          {
        	  if( id!=null){
        		  Info individualData = new Info(id, name,sex,birt,fams,famc);
                  linkIndviduals.add(individualData);
                  indiFlag = 0;
           	      id = null;
                  name=null;
                  sex =null;
                  birt=null;
                  famc=null;
                  fams=null;  
        	  }
        	  id = info[1];
        	  indiFlag++;
          }
          
          if(Tag.equals("FAM")){
        	  
       	     if( FamilyId!= null){
    		  Info FamiliesData = new Info(FamilyId, tag, IndiTag);
              linkFamilies.add(FamiliesData);
              famFlag = 0;
              FamilyId = null;
              tag=null;
              IndiTag=null;
             }
       	     FamilyId = info[1];
        	 famFlag++;
          }
        }
	      fis.close();
	      dis.close();
	   	   
	      
	  }
	  
	  private static void StoreFamilies(){
		  
	  }
	  
}
	  
