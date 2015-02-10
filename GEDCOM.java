
	import java.io.*;
	import java.util.Iterator;
	import java.util.LinkedList;
	class Info
	{
	   String name;
	   String sex;
	   String birt;
	   String fams;
	   String famc;
	   String indi;

	   public Info(String n, String s, String b, String fs, String fc , String id)
	   {
	     name = n; 
	     sex = s; 
	     birt = b; 
	     fams = fs; 
	     famc=fc;
	     indi=id;
	   }
	}


	public class GEDCOM  {
		


		  @SuppressWarnings("deprecation")
		public static void main(String[] args) throws IOException {

		    
			  LinkedList<Info> linky = new LinkedList<Info>();
		    	FileInputStream  fis = new FileInputStream("E:/java/Gedcom/src/FamilyTree.ged");
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
	          int c=0;
	          /*   
	           
	           if (Tag.equals("INDI")|| (Tag.equals("NAME")) ||(Tag.equals("SEX"))||(Tag.equals("BIRT"))
	            		|| (Tag.equals("DEAT")) ||( Tag.equals("FAMC"))||( Tag.equals("FAMS")) ||(Tag.equals("FAM"))
	            		||( Tag.equals("MARR")) ||( Tag.equals("HUSB"))|| (Tag.equals("WIFE")) || (Tag.equals("CHIL"))|| 
	            				(Tag.equals("DIV") )|| (Tag.equals("DATE")) || (Tag.equals("TRLR"))|| (Tag.equals("NOTE")))
	            
	            System.out.println("Tag:"+Tag);
	 
	            else 
	            	System.out.println("Invalid Tag");
	          */  
	          
	          if(Tag.equals("INDI"))
	          {
	        	  flag=1;
	          }

	          if(flag==1)
	          {
	        if (Tag.equals("NAME")) 
	        {
	        	name=info[2];
	        }
	        else if (Tag.equals("SEX")) 
	        {
	        	sex=info[2];
	        }
		      
	        else if (Tag.equals("DATE")) 
	        {
	        	birt=info[2]+info[3]+info[4];
	        }
	        else if (Tag.equals("FAMS")) 
	        {
	        	fams=info[2];
	        }
	        else if (Tag.equals("FAMC")) 
	        {
	        	famc=info[2];
	        }
	        else if(Tag.equals("INDI"))
	        {
	        	id=info[1];
	            Info individualData = new Info(name,sex,birt,fams,famc,id);
	            linky.add(individualData);
	            name=null;
	            sex =null;
	            birt=null;
	            famc=null;
	            fams=null;
	        }
	            
	          }
	            }
		      fis.close();
		      bis.close();
		      dis.close();
		   
		    
		        
		  }
		  
	}
		  
	
