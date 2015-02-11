package SOLUTION;


import java.io.*;

public class GEDCOM  {
	
    private static String[] linkIndviduals;
    private static String[] linkFamilies;

	  @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

	     
		    linkIndviduals = new String[5000]; 
		    linkFamilies = new String[1000]; 
		    
	    	FileInputStream  fis = new FileInputStream("FamilyTreeP.ged");
    	    DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
    	    
            String Line;
            String name=null,sex = null,birt=null,famc=null,fams=null, id=null;
            String FamilyId=null, tag=null, IndiTag=null;
	        int indiFlag=0, item=0, item1=0;
	        
            while (dis.available() != 0)
            {
            Line=dis.readLine();
	       
	        String[] info = Line.split(" ");
            //String Level = info[0];
            String Tag=null;
            for(int l=0; l<info.length; l++)
            {
                if(!(Character.isDigit(info[l].charAt(0))) && !(info[l].matches("^.*[^a-zA-Z0-9 ].*$")) )
                {
                  Tag=info[l];
                  break;
                }
            }
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
      		    linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
            else if (Tag.equals("WIFE")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
            	linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
    	      
            else if (Tag.equals("CHIL")) 
            {
            	tag=info[1];
            	IndiTag=info[2];
                linkFamilies[item1] = FamilyId+" "+tag+" "+IndiTag;
                item1++;
            }
          

            
          if(Tag.equals("INDI") || Tag.equals("FAM"))
          {
        	  if( id!=null && indiFlag!=0){
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
       	     FamilyId = info[1];
          }
        }
	      fis.close();
	      dis.close();
	   	  
	      printIndividuals();
	      System.out.println("###########################");
	      printFamilies();
	      
	  }
	  
	  private static void printIndividuals(){
	        for (int i=0; i<linkIndviduals.length && linkIndviduals[i] != null; i++){
	          String[] info = linkIndviduals[i].split(" ");
	          System.out.println(info[1] + " " + info[2]);
	        }
	  }
	  
	  private static void printFamilies(){
	        for (int i=0; i<linkFamilies.length && linkFamilies[i] != null; i++){
	          String[] info = linkFamilies[i].split(" ");
	          if(info[1].equalsIgnoreCase("HUSB")){
	        	  for (int l=0; l<linkIndviduals.length && linkIndviduals[l] != null; l++){
	        		  String[] info1 = linkIndviduals[l].split(" ");
	    	          if(info[2].equals(info1[0]) )
	    	        	System.out.println("The husband Name:" + info1[1] + " " + info1[2]);
	        	  }
	          } else if(info[1].equalsIgnoreCase("WIFE") ){
	        	  for (int l=0; l<linkIndviduals.length && linkIndviduals[l] != null; l++){
	        		  String[] info1 = linkIndviduals[l].split(" ");
	        		  if(info[2].equals(info1[0]) )
	    	        	System.out.println("The wife Name:" + info1[1] + " " + info1[2]);
	        	  }
	          }
	        }
	  }
	  
}
	  
