
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class IndividualRecord {

	String SEX, Id, BirthDate, FamS, FamC, Marr, husb, wife, Fam, Name;
	String DeathDate, MarrDate;

	public IndividualRecord(String id, String name, String sex,
			String birthdate, String Fams, String Famc, String deathDate) {
		this.Id = id;
		this.Name = name;
		this.SEX = sex;
		this.BirthDate = birthdate;
		this.FamS = Fams;
		this.FamC = Famc;
		this.DeathDate=deathDate;
		//this.MarrDate=MarrD;

	}

}

class FamilyInfo {
	String FamilyId;
	String HusbandId;
	String MarriageDate;
	String DivorseDate;
	String WifeId;
	//ArrayList<String> WifeId = new ArrayList<String>();
	ArrayList<String> ChlidrenIds = new ArrayList<String>();
}
class ChildInfo {
	String individualsID;
	String FamilyID;
	String name;
	Date Birth = new Date();
}
/**************************************************/
public class GEDCOM {

	private static IndividualRecord[] indRecords;
	private static FamilyInfo[] Family;
	private static int indiFlag = 0, item = 0, item1 = -1;
	private static String IndiTag = null;
	private static String name = null, sex = null, famc = null, fams = null,
			id = null, birt = null, death=null, marr=null;
	private static boolean DeathDateFlag=false, MarriageFlag=false, BirtDateFlag=false, Divorseflag=false;
	static SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		indRecords = new IndividualRecord[5000];
		Family = new FamilyInfo[1000];

		FileInputStream fis = new FileInputStream("My-Family-11-Mar-2015-3.ged");
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
//|| Tag.startsWith("D", 0)
			if (Tag.startsWith("N", 0) || Tag.startsWith("S", 0)
					 || Tag.startsWith("F", 0) ||Tag.startsWith("B", 0)) {
				CheckIndivduals(Tag, info);
			}

			if (Tag.startsWith("H", 0) || Tag.startsWith("W", 0)
					|| Tag.startsWith("C", 0)) {
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
					Divorseflag=false;
					marr=null;
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
			 if (Tag.equals("MARR"))
				MarriageFlag=true;
			if (Tag.equals("DEAT"))
			{
				 DeathDateFlag=true;
				
			}
			if (Tag.equals("BIRT"))
			{
				BirtDateFlag=true;
			}
			if (Tag.equals("DIV"))
			{
				 Divorseflag=true;
				
			}
			if (Tag.equals("DATE"))	
			{
				parseDtates(Tag, info);
			}
			
			
		}
		fis.close();
		dis.close();
//**********End Of Reading ******************************//
		printIndividuals();
		System.out.println("###########################");
		//printFamilies();
		System.out.println("###########################");

		CheckDeathbeforeBirth ();
		CheckDeathbeforeMarriage();
		 try {
				printOrderSiblingsLsit();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
	        CheckHusbandIsMale();
	        CheckWifeFemale();
	        CheckDivorseAfterToday();
	        CheckDivorceBeforeMarriage ();
		
		CheckChildBirthBeforeParent();
		PrintMalesAndFemales();
		try {
			PrintBasedOnBirthMonth();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

	}

	private static void CheckIndivduals(String Tag, String[] info) {
		if (Tag.equals("NAME")) {
			name = info[2] + " " + info[3];
			indiFlag++;
		} else if (Tag.equals("SEX")) {
			sex = info[2];
			indiFlag++;
		
		} else if (Tag.equals("FAMS")) {
			fams = info[2];
			indiFlag++;
		} else if (Tag.equals("FAMC")) {
			famc = info[2];
			indiFlag++;
		}
		
		
		
	}

	private static void CheckFamilyMember(String Tag, String[] info) {
		if (Tag.equals("HUSB")) {
			IndiTag = info[2];
			Family[item1].HusbandId = IndiTag;
		} else if (Tag.equals("WIFE")) {
			IndiTag = info[2];
			//Family[item1].WifeId.add(IndiTag);
			Family[item1].WifeId=IndiTag;

		}

		else if (Tag.equals("CHIL")) {
			IndiTag = info[2];
			Family[item1].ChlidrenIds.add(IndiTag);
		}
		
		
			
	}//Tag.equals("DATE") &&
	private static void parseDtates (String Tag, String[] info){
		 if ( BirtDateFlag) {
				birt = info[2]+" " + info[3] +" "+ info[4];
				indiFlag++;
				BirtDateFlag=false;
		 }
		if ( MarriageFlag)
		{
			marr=info[2]+" "+info[3]+" "+info[4];
			Family[item1].MarriageDate=marr;
			MarriageFlag=false;
			
		}
		if ( DeathDateFlag)
		{
			death=info[2]+" "+info[3]+" "+info[4];
			DeathDateFlag=false;
			
		}
		if ( Divorseflag)
		{
			Family[item1].DivorseDate=info[2]+" "+info[3]+" "+info[4];
			Divorseflag=false;
			
		}
		
	}

	private static void printIndividuals() {
		for (int i = 0; i < indRecords.length && indRecords[i] != null; i++) {

			System.out.println(indRecords[i].Name + " " + indRecords[i].Id+ " Birth Date= "+indRecords[i].BirthDate+"  Death Date="+ indRecords[i].DeathDate+" ");
		}
	}

	/*private static void printFamilies() {
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
	}*/
	
	private static void CheckDeathbeforeBirth (){
		for (int i=0; i<indRecords.length && indRecords[i]!=null ; i++)
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
	private static void CheckDeathbeforeMarriage ()
	{
		for (int i=0; i<indRecords.length && indRecords[i]!=null; i++)
		{ 
			if ( indRecords[i].DeathDate!=null)
		{
				for (int j=0;j<Family.length && Family[j]!=null  ;j++)
				{ 
			if (indRecords[i].Id.compareTo(Family[j].HusbandId)==0 || indRecords[i].Id.compareTo(Family[j].WifeId)==0 )
			try {
				System.out.println("***************");
				Date Mdate = formatter.parse(Family[j].MarriageDate);
				Date Ddate= formatter.parse(indRecords[i].DeathDate);
				//System.out.print (Mdate +"  "+Ddate);
			
				if (Mdate.compareTo(Ddate)>0)
				System.out.println(" Individual "+ indRecords[i].Id+" ("+indRecords[i].Name+") has death date("+ indRecords[i].DeathDate+") before Marriage date ("+ Family[j].MarriageDate+") " );
				
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}}

	}
			
		}
	
}
	
	private static void printOrderSiblingsLsit() throws ParseException{
		ChildInfo[] Children = new ChildInfo[indRecords.length];
		String[] OrderSiblings;
		//DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
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
	
	private static void CheckDivorseAfterToday ()
	{
		int i;
		System.out.println("\n********************Check for Divorse date after current date******************");
		//DateFormat today = new SimpleDateFormat("dd/MMM/yyyy");
		Date today = new Date();
		//dateFormat.format(date);
		for(i=0;i<Family.length && Family[i]!=null;i++)
		{
			try
			{
				if(Family[i].DivorseDate!=null)
				{
				Date Divdate = formatter.parse(Family[i].DivorseDate);
					if(Divdate.after(today))
					{
						System.out.println("Error in file:The divorce date in Family " +Family[i].FamilyId+ " is not valid. The date is a future date");
					}
					
				}
			}
			catch(ParseException e) {
					e.printStackTrace();
			}
		}
		
	}	
	//Check Wife is female
	
	public static void CheckWifeFemale(){
		int i, j;
		System.out.println("\n********************Check for Wife Gender******************");
		for(i=0;i<Family.length && Family[i]!=null;i++)
		{
			for(j=0;j<indRecords.length && indRecords[j]!=null;j++)
			{
				//System.out.println (indRecords[j].Id+ Family[i].WifeId);
				if(Family[i].WifeId.equals(indRecords[j].Id))
				{
					//System.out.println(indRecords[i].SEX);
					if(!indRecords[i].SEX.equalsIgnoreCase("F"))
					{
						
						System.out.println("Error in file: The wife in Family" +Family[i].FamilyId+ " , "+indRecords[j].Name+"is not female");
					}
				}
			}
		}
		
	}
	
	private static void CheckDivorceBeforeMarriage ()
	{
		
				for (int j=0;j<Family.length && Family[j]!=null  ;j++)
				{ 
			if (Family[j].MarriageDate!=null && Family[j].DivorseDate!=null )
			try {
				System.out.println("***************");
				Date Mdate = formatter.parse(Family[j].MarriageDate);
				Date Ddate= formatter.parse(Family[j].DivorseDate);
				//System.out.print (Mdate +"  "+Ddate);
			
				if (Mdate.compareTo(Ddate)>0)
				
				System.out.println(" Family "+ Family[j].FamilyId +"  has divorce date("+ Family[j].DivorseDate+") before Marriage date ("+ Family[j].MarriageDate+") " );
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}}

	}
	
	private static void CheckChildBirthBeforeParent() {
		ArrayList<Date> ChildrenBirthDates = new ArrayList<Date>();
		Date HusbandBirthDate = new Date();
		Date WifeBirthDate = new Date();

		System.out.println("\n*****************Check for Children Birthdates < Parents' Birthdates***************");
		for (int i = 0; i < Family.length && Family[i] != null; i++) {
			String HusbId = Family[i].HusbandId;
			String WifeId = Family[i].WifeId;
			ArrayList<String> FamilyChildrenIds = Family[i].ChlidrenIds;
			
			for (int j = 0; j < indRecords.length && indRecords[j] != null; j++) {
					if (indRecords[j].Id.equalsIgnoreCase(HusbId)) {
					   try { 
							HusbandBirthDate = formatter
									.parse(indRecords[j].BirthDate);
							HusbId = "";
						}catch (ParseException e) {
								e.printStackTrace();
						}						
					} else if (indRecords[j].Id.equalsIgnoreCase(WifeId)) {
					  try { 
						WifeBirthDate = formatter
								.parse(indRecords[j].BirthDate);
						WifeId = "";
					  }catch (ParseException e) {
							e.printStackTrace();
					  }
				    } 
			 }
			
			for (int c = 0; c < FamilyChildrenIds.size(); c++)
				for (int k = 0; k < indRecords.length && indRecords[k] != null; k++) {
					try {
						if (indRecords[k].Id.equalsIgnoreCase(FamilyChildrenIds.get(c))) {
							ChildrenBirthDates.add(c, formatter
									.parse(indRecords[k].BirthDate));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			FamilyChildrenIds.clear();
			
            System.out.println(HusbandBirthDate.toString());
            System.out.println(WifeBirthDate.toString() );   
            System.out.println("ChildrenBirthDates  " + ChildrenBirthDates.size()); 
            
			for (int c = 0; c < ChildrenBirthDates.size(); c++) {
				System.out.println(ChildrenBirthDates.get(c));
				if (ChildrenBirthDates.get(c).compareTo(HusbandBirthDate) < 0
						|| ChildrenBirthDates.get(c).compareTo(WifeBirthDate) < 0)
					System.out.println("Error in file: There is a child who has a birthdate which is greater than the parents'");
			}
			System.out.println("\n");			
		}
	}
	
	private static void PrintMalesAndFemales() {
		ArrayList<String> Males = new ArrayList<String>();
		ArrayList<String> Females = new ArrayList<String>();		
		System.out.println("\n*****************Print Females and Males***************");
		
		for (int i = 0; i < indRecords.length && indRecords[i] != null; i++) {
			if(indRecords[i].SEX.equalsIgnoreCase("M"))
				Males.add(indRecords[i].Name);
			else if(indRecords[i].SEX.equalsIgnoreCase("F"))
				Females.add(indRecords[i].Name);
		}
		
		System.out.println("\nThe Males in this family are: ");
		for (int j = 0; j < Males.size() ; j++) 
			System.out.println(Males.get(j));
		
		System.out.println("\nThe Females in this family are: ");
		for (int k = 0; k < Females.size() ; k++) 
			System.out.println(Females.get(k));		
	}
	private static void PrintBasedOnBirthMonth() throws ParseException  {

		System.out.println("********** Print Based On BirthMonth*********");
		Date Dat1;
		Date Dat2 ;
		int Month1, Month2;
		System.out.println("********** Print Based On BirthMonth*********");
		Date Dat1;
		Date Dat2 ;
		int Month1, Month2;
		String [] months={"January", "February", "March", "Aprl","May", "June", "July","Augest","September","October","November","December"};
		int flag=0;
		for (int i=0;i<11;i++)
			{System.out.println ("Individuals who were born on "+months[i]);
			for (int j=0;j<indRecords.length && indRecords[j]!=null;j++)
			{
				Dat1=formatter.parse(indRecords[j].BirthDate);
				Month1=Dat1.getMonth();
				if(Month1==i)
					{System.out.println(indRecords[j].Name);
					flag=1;
					}
			}	
			if (flag==0)
				System.out.println("No individuals were born in this month");
			}
		System.out.println("*****************************");
		IndividualRecord temp;
		IndividualRecord[] indRec= new IndividualRecord[indRecords.length];
		for (int i=0; i<indRecords.length && indRecords[i]!=null ; i++)
		{
			indRec[i]=indRecords[i];
		}

		int C=0;

		while (C<indRec.length){

			if (indRec[C]!=null){
				if ( indRec[C].BirthDate!=null)
				{
					for (int j=C+1; j<indRec.length &&indRec[j]!=null ;j++)
					{if (indRec[j].BirthDate!=null)
					{
						Dat1=formatter.parse(indRec[C].BirthDate);
						Month1=Dat1.getMonth();

						Dat2=formatter.parse(indRec[j].BirthDate);
						Month2=Dat2.getMonth();


						if (Month1>Month2)
						{
							temp=indRec[j];
							indRec[j]=indRec[C];
							indRec[C]=temp;

						}

					}}


				}}
			C++;}

		for (int k=0; k<indRec.length&& indRec[k]!=null; k++)

			System.out.println(indRec[k].Name);
	}
}


