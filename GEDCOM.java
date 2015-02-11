package SOLUTION;


import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
/*class Info
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
     //System.out.println( indi + " " + name + " "+ sex + " " + birt + " " + fams + " " + famc  );
   }
   
   public Info(String famId, String fTag, String Inditag)
   {
	 famid = famId;
	 ftag = fTag;
	 IndiTag = Inditag;
     //System.out.println( "Family Begin" + famId + " " + fTag +  " " + Inditag);
   }
}*/


public class GEDCOM  {
	
    private static String[] linkIndviduals;
    private static String[] linkFamilies;

	  @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

	     
		    linkIndviduals = new String[5000]; 
		    linkFamilies = new String[1000]; 
		    
	    	FileInputStream  fis = new FileInputStream("/Users/hadoola/CS555-P03/src/SOLUTION/FamilyTree.ged");
    	    DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
    	    
            String Line;
            String name=null,sex = null,birt=null,famc=null,fams=null, id=null;
            String FamilyId=null, tag=null, IndiTag=null;
	        int indiFlag=0, famFlag=0, item=0, item1=0;
	        
            while (dis.available() != 0)
            {
            Line=dis.readLine();
	       
	        String[] info = Line.split(" ");
            String Level = info[0];
            String Tag=null;
            //System.out.println(Line);
            //System.out.println("Level:"+Level);
            for(int l=0; l<info.length; l++)
            {
                if(!(Character.isDigit(info[l].charAt(0))) && !(info[l].matches("^.*[^a-zA-Z0-9 ].*$")) )
                {
                  Tag=info[l];
                  //System.out.println("Tag Name   "+ info[l] + "\n");
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
            if (Tag.equals("NAME")) 
            {
            	name=info[2]+" "+info[3];
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
          
           

           if (Tag.equals("HUSB")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
      		    //Info FamiliesData = new Info(FamilyId, tag, IndiTag);
      		    linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
            else if (Tag.equals("WIFE")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	//Info FamiliesData = new Info(FamilyId, tag, IndiTag);
            	linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
    	      
            else if (Tag.equals("CHIL")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	//Info FamiliesData = new Info(FamilyId, tag, IndiTag);
                linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
          

            
          if(Tag.equals("INDI") || Tag.equals("FAM"))
          {
        	  if( id!=null && indiFlag!=0){
        		  //Info individualData = new Info(id, name,sex,birt,fams,famc);
        		  linkIndviduals[item] = id+" " +name+" " +sex+" " +birt+" " +fams+" " +famc;
        		  item++;
                  indiFlag = 0;
           	      id = null;
                  name=null;
                  sex =null;
                  birt=null;
                  famc=null;
                  fams=null;  
        	  } else item=0;
        	  id = info[1];
          }
          
          if(Tag.equals("FAM")){
        	  
       	    /* if( FamilyId!= null){
    		  Info FamiliesData = new Info(FamilyId, tag, IndiTag);
              linkFamilies.add(FamiliesData);
              famFlag = 0;
              FamilyId = null;
              tag=null;
              IndiTag=null;
             }*/
       	     FamilyId = info[1];
        	 /*if(famFlag==1) {
        		 famFlag=0;
        		 item1=0;
        	 }
        	 famFlag++;*/
          }
        }
	      fis.close();
	      dis.close();
	   	  
	      printIndividuals();
	      printFamilies();
	      
	  }
	  
	  private static void printIndividuals(){
		  //String[] Names = new String[5000];
	        for (int i=0; i<linkIndviduals.length; i++){
	          String[] info = linkIndviduals[i].split(" ");
	          if(linkIndviduals[i] != null)
	        	System.out.println(info[1] + " " + info[2]);
	          else break;
	        }
	  }
	  
	  private static void printFamilies(){
		    //String[] Husbands = new String[1000]; 
		    //String[] Wives = new String[1000]; 
	        for (int i=0; i<linkFamilies.length; i++){
	          String[] info = linkFamilies[i].split(" ");
	          System.out.println(linkFamilies[i]);
	          if(linkFamilies[i] != null && info[1] == "HUSB"){
	        	  for (int l=0; l<linkIndviduals.length; l++){
	        		  String[] info1 = linkIndviduals[l].split(" ");
	    	          if(info1[0] == info[3])
	    	        	System.out.println("The husband Name:" + info1[1] + " " + info1[2]);
	        	  }
	          } else if(linkFamilies[i] != null && info[1] == "WIFE"){
	        	  for (int l=0; l<linkIndviduals.length; l++){
	        		  String[] info1 = linkIndviduals[l].split(" ");
	    	          if(info1[0] == info[3])
	    	        	System.out.println("The wife Name:" + info1[1] + " " + info1[2]);
	        	  }
	          }
	          else break;
	        }
	  }
	  
}
	  
