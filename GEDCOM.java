import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class IndividualRecord {

	String SEX, Id, BirthDate, FamS, FamC, Marr, husb, wife, Fam, Name;
	String DeathDate;

	public IndividualRecord(String id, String name, String sex,
			String birthdate, String Fams, String Famc, String deathDate) {
		this.Id = id;
		this.Name = name;
		this.SEX = sex;
		this.BirthDate = birthdate;
		this.FamS = Fams;
		this.FamC = Famc;
		this.DeathDate=deathDate;

	}

}

class FamilyInfo {
	String FamilyId;
	String HusbandId;
	String MarriageDate;
	ArrayList<String> WifeId = new ArrayList<String>();
	ArrayList<String> ChlidrenIds = new ArrayList<String>();
}

class ChildInfo {
	String individualsID;
	String FamilyID;
	String name;
	Date Birth = new Date();
}

public class GEDCOM {

	private static IndividualRecord[] indRecords;
	private static FamilyInfo[] Family;
	private static int indiFlag = 0, item = 0, item1 = -1;
	private static String IndiTag = null;
	private static String name = null, sex = null, famc = null, fams = null,
			id = null, birt = null, death=null;
	private static boolean DeathDateFlag=false, MarriageFlag=false, BirtDateFlag=false;
	static SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		indRecords = new IndividualRecord[5000];
		Family = new FamilyInfo[1000];

		FileInputStream fis = new FileInputStream("My-Family.ged");
		DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
		String Line, FamilyId = null;
//*************** Reading the GEDCOM File****************************
		while (dis.available() != 0) {
			Line = dis.readLine();

			String[] info = Line.split(" ");
			String Tag = null;
			for (int l = 0; l < info.length; l++) {
				if (!(Character.isDigit(info[l].charAt(0)))
						&& !(info[l].matches("^.*[^a-zA-Z0-9 ].*$"))) {
					Tag = info[l];
					break;
				}
			}

			if (Tag.startsWith("N", 0) || Tag.startsWith("S", 0)
					|| Tag.startsWith("D", 0) || Tag.startsWith("F", 0) ||Tag.startsWith("B", 0)) {
				CheckIndivduals(Tag, info);
			}

			if (Tag.startsWith("H", 0) || Tag.startsWith("W", 0)
					|| Tag.startsWith("C", 0)|| Tag.startsWith("M",0)) {
				CheckFamilyMember(Tag, info);
			}
	 

			if (Tag.equals("INDI") || Tag.equals("FAM")) {
				if (id != null && indiFlag != 0) {
					indRecords[item] = new IndividualRecord(id, name, sex,
							birt, fams, famc, death);
					item++;
					indiFlag = 0;
					id = null;
					name = null;
					sex = null;
					birt = null;
					famc = null;
					fams = null;
					death=null;
					DeathDateFlag=false;
					MarriageFlag=false;
				} else
					item = 0;
				id = info[1];
			}

			if (Tag.equals("FAM")) {
				IndiTag = null;
				FamilyId = info[1];
				item1++;
				Family[item1] = new FamilyInfo();
				Family[item1].FamilyId = FamilyId;

			}
			
			
		}
		fis.close();
		dis.close();
//**********End Of Reading ******************************//
		printIndividuals();
		System.out.println("###########################");
		printFamilies();
		System.out.println("###########################");
		
		CheckDeathbeforeBirth ();

                try {
			printOrderSiblingsLsit();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
                CheckHusbandIsMale();
	}

	private static void CheckIndivduals(String Tag, String[] info) {
		if (Tag.equals("NAME")) {
			name = info[2] + " " + info[3];
			indiFlag++;
		} else if (Tag.equals("SEX")) {
			sex = info[2];
			indiFlag++;
		} else if (Tag.equals("DATE") && BirtDateFlag) {
			birt = info[2]+" " + info[3] +" "+ info[4];
			indiFlag++;
			BirtDateFlag=false;
		} else if (Tag.equals("FAMS")) {
			fams = info[2];
			indiFlag++;
		} else if (Tag.equals("FAMC")) {
			famc = info[2];
			indiFlag++;
		}
		if (Tag.equals("DATE") && DeathDateFlag)
		{
			death=info[2]+" "+info[3]+" "+info[4];
			DeathDateFlag=false;
			
		}
		if (Tag.equals("DEAT"))
		{
			 DeathDateFlag=true;
			
		}
		if (Tag.equals("BIRT"))
		{
			BirtDateFlag=true;
		}
	
		
		
	}

	private static void CheckFamilyMember(String Tag, String[] info) {
		if (Tag.equals("HUSB")) {
			IndiTag = info[2];
			Family[item1].HusbandId = IndiTag;
		} else if (Tag.equals("WIFE")) {
			IndiTag = info[2];
			Family[item1].WifeId.add(IndiTag);
		}

		else if (Tag.equals("CHIL")) {
			IndiTag = info[2];
			Family[item1].ChlidrenIds.add(IndiTag);
		}
		else if (Tag.equals("MARR"))
			MarriageFlag=true;
			
		
			
	}

	private static void printIndividuals() {
		for (int i = 0; i < indRecords.length && indRecords[i] != null; i++) {

			System.out.println(indRecords[i].Name + " " + indRecords[i].Id+ " Birth Date= "+indRecords[i].BirthDate+"  Death Date="+ indRecords[i].DeathDate);
		}
	}

	private static void printFamilies() {
		for (int count = 0; count <= item1; count++)
			for (int i = 0; i < Family[count].WifeId.size(); i++) {
				System.out.println(Family[count].FamilyId + ", husband "
						+ Family[count].HusbandId + ", wife "
						+ Family[count].WifeId.get(i)+ "Marraige Date"+ Family[count].MarriageDate);
				for (int c = 0; c < Family[count].ChlidrenIds.size(); c++) {
					System.out.println(", with child "
							+ Family[count].ChlidrenIds.get(c));
				}
			}
	}
	
	private static void CheckDeathbeforeBirth (){
		for (int i=0; i<indRecords.length ; i++)
		{ if (indRecords[i].BirthDate != null && indRecords[i].DeathDate!=null)
			try {
				 
				Date Bdate = formatter.parse(indRecords[i].BirthDate);
				Date Ddate= formatter.parse(indRecords[i].DeathDate);
			
				if (Bdate.compareTo(Ddate)>0)
				System.out.println(" Individual "+ indRecords[i].Id+" ("+indRecords[i].Name+") has death date("+ indRecords[i].DeathDate+") before birth date ("+ indRecords[i].BirthDate+") " );
				
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void printOrderSiblingsLsit() throws ParseException{
		ChildInfo[] Children = new ChildInfo[indRecords.length];
		String[] OrderSiblings;
		DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date(); 
		int count = 0, ChildCount = 0;
	    
		System.out.println("Siblings are listed in order of their age in every family: ");
		for(int i=0; i< Family.length  && i<= item1; i++){
		  for (int j=0; j<Family[i].ChlidrenIds.size() ; j++) {
			Children[count] = new ChildInfo();
			Children[count].individualsID = Family[i].ChlidrenIds.get(j);
			Children[count].FamilyID = Family[i].FamilyId;
		    for(int l=0; l< indRecords.length; l++)
		    	if(indRecords[l].Id.equals(Family[i].ChlidrenIds.get(j))){
		    		date = formatter.parse(indRecords[l].BirthDate);
		    		Children[count].name = indRecords[l].Name;
		    		break;
		    	}
		    Children[count].Birth = date;
			count++;
			ChildCount++;
		  }
	   }
	   
	   OrderSiblings = new String[ChildCount];
	   for(int l=0; l< Children.length && l < ChildCount; l++){
		 OrderSiblings[l] =  "Family " + Children[l].FamilyID + " has a child whose name is "+ Children[l].name;
		 int num = l;
		 if(num >= 1)
		   if(Children[l].FamilyID.equals(Children[--num].FamilyID)){
			  if(Children[l].Birth.before(Children[num].Birth)){
				  String Name = "Family " + Children[l].FamilyID + " has a child whose name is "+ Children[l].name;
				  OrderSiblings[num] =  Name;
				  OrderSiblings[l] = "Family " + Children[num].FamilyID + " has a child whose name is "+ Children[num].name;
			  } 
		   } 
	   }
	   for(int o=0; o<ChildCount; o++)
		   System.out.println(OrderSiblings[o]);
	}
	
	private static void CheckHusbandIsMale() {
		String HusbandId;
		for(int i=0; i< Family.length && i<= item1; i++){
			HusbandId = Family[i].HusbandId;
			for(int l=0; l< indRecords.length; l++){
		       if(indRecords[l].Id.equals(HusbandId)){
		    	   if(! indRecords[l].SEX.equalsIgnoreCase("M"))
		    		   System.out.println("Family with "+Family[i].FamilyId+" has a husband "
		    	                     +HusbandId+" , "+indRecords[l].Name+" , who isn't a male.");
		    	   break;
		       }
		    }
		}
	}
	
}
