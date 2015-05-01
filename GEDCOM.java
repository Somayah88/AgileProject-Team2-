import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

class IndividualRecord {

	String SEX, Id, BirthDate, FamS, FamC, Marr, husb, wife, Fam, Name;
	String DeathDate, MarrDate;
	int age;
boolean isDead;
	public IndividualRecord(String id, String name, String sex,
			String birthdate, String Fams, String Famc, String deathDate, boolean isDead) {
		this.Id = id;
		this.Name = name;
		this.SEX = sex;
		this.BirthDate = birthdate;
		this.FamS = Fams;
		this.FamC = Famc;
		this.DeathDate=deathDate;
		this.isDead=isDead;
		try {
			age=setage();
		} catch (ParseException e) {

			e.printStackTrace();
		}

	}
	public int setage() throws ParseException
	{ 

		SimpleDateFormat formatter1 = new SimpleDateFormat("d MMM yyyy");
		Date Bdate = formatter1.parse(BirthDate);
		Calendar cal = Calendar.getInstance();
		Date today = new Date();
		Calendar cal1 = Calendar.getInstance();
		cal.setTime(Bdate);
		cal1.setTime(today);
		age = cal1.get(Calendar.YEAR)-cal.get(Calendar.YEAR);
		int currMonth = cal1.get(Calendar.MONTH) + 1;
		int birthMonth = cal.get(Calendar.MONTH) + 1;
		int months = currMonth - birthMonth;
		if (months < 0)
			age--;
		//else if (months == 0 && cal1.get(Calendar.DATE) < cal.get(Calendar.DATE))
		//age--;
		else if (months == 12)
			age++;
		System.out.println(age);
		return age;
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
	int age;
}
/**************************************************/
public class GEDCOM {

	private static IndividualRecord[] indRecords;
	private static FamilyInfo[] Family;
	private static int indiFlag = 0, item = 0, item1 = -1;
	private static String IndiTag = null;
	private static String name = null, sex = null, famc = null, fams = null,
			id = null, birt = null, death=null, marr=null;
	private static boolean DeathDateFlag=false, MarriageFlag=false, BirtDateFlag=false, Divorseflag=false, isDead;
	static SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		indRecords = new IndividualRecord[5000];
		Family = new FamilyInfo[1000];

		FileInputStream fis = new FileInputStream("My-Family-S04.ged");
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
							birt, fams, famc, death, isDead);
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
					isDead=false;
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
				if (info[2].equals("Y"))
					isDead=true;
					

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
	SortByAge();
		CheckAgeLimit();

		CheckMarriageBeforeBirth();
		PrintFamiliesChildren();
		ListSiblingAgeDiff();
	CheckSiblingMarriage();
		below14Marriage();
		deathNotAfterCurrDate ();
	ListUnmarried();
		CheckMarriageAfterToday();
              CheckMotherOver60();
		CheckBirthBeforeToday();
		ageDifferOfSpouse ();
		moreThan10Children ();
		ageDiffBetweecChildParent ();
		ListDead ();
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
	{ 	System.out.println("********** Divorce Before Marriage*********");

	for (int j=0;j<Family.length && Family[j]!=null  ;j++)
	{ 
		if (Family[j].MarriageDate!=null && Family[j].DivorseDate!=null )
			try {

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
			//FamilyChildrenIds.clear();

			for (int c = 0; c < ChildrenBirthDates.size(); c++) {
				if (ChildrenBirthDates.get(c).compareTo(HusbandBirthDate) < 0
						|| ChildrenBirthDates.get(c).compareTo(WifeBirthDate) < 0)
					System.out.println("Error in file: There is a child in family " + Family[i].FamilyId + " who has a birthdate which is greater than the parents'");
			}		
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


		Date Dat1;

		int Month1;

		System.out.println("********** Print Based On BirthMonth*********");
		String [] months={"January", "February", "March", "Aprl","May", "June", "July","Augest","September","October","November","December"};
		int flag=0;
		for (int i=0;i<11;i++)
		{System.out.println ("Individuals who were born on "+months[i]);
		flag=0;
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
			System.out.println("None");
		}
		System.out.println("*****************************");
	}
	/*IndividualRecord temp;
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
	}*/

	public static void SortByAge()
	{
		int i,j,n, count=0;
		IndividualRecord swap;
		n=indRecords.length;
		System.out.println("\n Individuals sorted according to Age");
		for (i=0; i<n && indRecords[i]!=null ; i++)
		{
			count++;
		}

		for (i=0;indRecords[i]!=null ; i++) {
			for (j = 0; j < count - i - 1; j++) {
				if (indRecords[j].age > indRecords[j+1].age) 
				{
					swap       = indRecords[j];
					indRecords[j]   = indRecords[j+1];
					indRecords[j+1] = swap;
				}
			}
		} 

		for (i=0; i<n && indRecords[i]!=null ; i++)
		{
			System.out.println("Name : "+indRecords[i].Name+"\n Age : "+ indRecords[i].age+"\n");
		}

	}
	public static void CheckAgeLimit()
	{ 
		int i;
		System.out.println("\n Check Age");
		for (i=0; i<indRecords.length && indRecords[i]!=null ; i++)
		{
			if(indRecords[i].age>150 && indRecords[i].DeathDate==null)
			{
				System.out.println("The individual "+indRecords[i].Name+"has age beyond 150");
			}
		}
	}

	private static void CheckMarriageBeforeBirth() {
		System.out.println("\n*****************Check Parent's Marriage not Before their Birth Dates***************");
		Date HusbandBirth = new Date();
		Date WifeBirth = new Date();
		Date MarriageDate = new Date();
		for (int j=0; j< Family.length && Family[j]!= null; j++) { 
			if (Family[j].MarriageDate != null ){
				try {
					for (int i = 0; i < indRecords.length	&& indRecords[i] != null; i++) {
						if (indRecords[i].Id.equalsIgnoreCase(Family[j].HusbandId))
							HusbandBirth = formatter
							.parse(indRecords[i].BirthDate);
						else if (indRecords[i].Id.equalsIgnoreCase(Family[j].WifeId))
							WifeBirth = formatter.parse(indRecords[i].BirthDate);
					}
					MarriageDate = formatter.parse(Family[j].MarriageDate);

					if (HusbandBirth.compareTo(MarriageDate) > 0)
						System.out.println("Parents in the Family "+ Family[j].FamilyId + "  has marriage date("+ Family[j].MarriageDate+ ") before their Birth date. ");
					else if (WifeBirth.compareTo(MarriageDate) > 0)
						System.out.println("Parents in the Family "+ Family[j].FamilyId + "  has marriage date("	+ Family[j].MarriageDate+ ") before their Birth date. ");

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void PrintFamiliesChildren() {
		System.out.println("\n*****************Print Families' Children***************");
		for (int i = 0; i < Family.length && Family[i] != null; i++){
			if(Family[i].ChlidrenIds.size() > 0)
				System.out.println("Family's " + Family[i].FamilyId + " Children: ");
			for (int c = 0; c < Family[i].ChlidrenIds.size(); c++) 
				System.out.println(Family[i].ChlidrenIds.get(c));
		}
	}
	private static void ListSiblingAgeDiff()
	{
		ChildInfo[] Children = new ChildInfo[indRecords.length];
		String[] SiblingsAge; 
		int count = 0, ChildCount = 0;

		System.out.println("\n*****************List age difference of siblings *****************");
		for(int i=0; i< Family.length  && i<= item1; i++){
			for (int j=0; j<Family[i].ChlidrenIds.size() ; j++) {
				Children[count] = new ChildInfo();
				Children[count].individualsID = Family[i].ChlidrenIds.get(j);
				Children[count].FamilyID = Family[i].FamilyId;
				for(int l=0; l< indRecords.length; l++)
					if(indRecords[l].Id.equals(Family[i].ChlidrenIds.get(j))){
						Children[count].name = indRecords[l].Name;
						Children[count].age = indRecords[l].age;
						break;
					}
				count++;
				ChildCount++;
			}
		}
		int agediff;
		
		SiblingsAge = new String[ChildCount];
		for(int l=0; l<Children.length && l <ChildCount; l++)
		{
			for(int v=l+1; v< Children.length && v < ChildCount; v++)
			{
				if(Children[l].FamilyID.equals(Children[v].FamilyID)){
					agediff= Math.abs(Children[l].age-Children[v].age);
					System.out.println(" Family "+Children[l].FamilyID+" : Age difference between " +Children[l].individualsID+" "+ Children[l].name +" and "+Children[v].individualsID+" "+ Children[v].name +"is " +agediff );
				} 
			}

		}
			}

	private static void CheckSiblingMarriage()
	{
		String Husfamc="husfc", Wifefamc="wifefc", famid1="fam1", famid2="fam2" ;
		String husname=null, wifename =null;
		int m=0, n=0;
		String spouse;
		String[] child2=new String[100];
		System.out.println("\n*****************Sibling Marriage***************");
		for (int f = 0; f < Family.length && Family[f] != null; f++)
		{
			for (int i = 0; i < Family.length && Family[i] != null; i++)
			{
				for(int k=i+1;k < Family.length && Family[k] != null; k++)
				{ 
					if(Family[i].HusbandId.equals(Family[k].HusbandId)||Family[i].WifeId.equals(Family[k].WifeId))
					{
						famid1=Family[i].FamilyId;
						famid2=Family[k].FamilyId;
						m=1;
						break;
					}
				}
				if(m==1)
					break;
			}
			for (int  j= 0; j < indRecords.length	&& indRecords[j] != null; j++)
			{
				if(indRecords[j].FamC!=null)
				{
					if(indRecords[j].FamC.equals(famid1))
					{
						spouse=indRecords[j].FamS;
						for (int  z= 0; z < indRecords.length	&& indRecords[z] != null; z++)
						{
							if(spouse!=null && indRecords[z].FamS!=null)
							{
								if(indRecords[z].FamS.equals(spouse))
									if(indRecords[z].FamC!=null)
										if(indRecords[z].FamC.equals(famid2))
										{
											System.out.println("The spouses "+indRecords[j].Id+" "+indRecords[j].Name+"and " +indRecords[z].Id+" "+indRecords[z].Name+" of the Family "
													+indRecords[z].FamS+" are half siblings");
											n=1;
											break;
										}
							}
						}
					}
				}
				if(n==1)
					break;
			}
		}
		for (int i = 0; i < Family.length && Family[i] != null; i++)
		{
			for (int  j= 0; j < indRecords.length	&& indRecords[j] != null; j++)
			{
				//System.out.println(Family[i].HusbandId +indRecords[j].Id );
				if(Family[i].HusbandId.equals(indRecords[j].Id))
				{
					Husfamc=indRecords[j].FamC;
					husname=indRecords[j].Name;
				}
				else if(Family[i].WifeId.equals(indRecords[j].Id))
				{

					Wifefamc=indRecords[j].FamC;
					wifename=indRecords[j].Name;
				}

			}
			if(Husfamc!=null && Wifefamc!=null)
			{
				if(Husfamc.equals(Wifefamc))
				{
					System.out.println("The spouses "+Family[i].HusbandId+" "+husname+" and " +Family[i].WifeId+" "+wifename+" of the Family "+indRecords[i].FamS+" are siblings");
					Husfamc="Husfamc";
					Husfamc="Wifefamc";
				}
			}
		}
		System.out.println("\n");
	}

	public static void below14Marriage() {
		System.out.println("***************Marraige age not below 14************");
		for (int j=0;j<Family.length && Family[j]!=null  ;j++){
			for (int i=0; i<indRecords.length && indRecords[i]!=null; i++)
				if (indRecords[i].BirthDate!=null && Family[j].MarriageDate!=null){
					if (indRecords[i].Id.equals(Family[j].HusbandId)|| (indRecords[i].Id.equals(Family[j].WifeId)))
					{
						Date Mdate, Bdate;
						//LocalDate MD,BD;
						try {
							Mdate = formatter.parse(Family[j].MarriageDate);
							Bdate= formatter.parse(indRecords[i].BirthDate);

							int years= Mdate.getYear()-Bdate.getYear();
							int months=Mdate.getMonth()-Bdate.getMonth();
							if (months<0)
								years--;
								if (years<0)
								System.out.println("For individual  "+indRecords[i].Id+"  " +indRecords[i].Name+ "the Marraige date and birth dates are wrong**** Marraige date before birthdate");
							if (years<=14 && years>0)
								System.out.println("Individual "+indRecords[i].Name+"  with Id:  "+indRecords[i].Id +"  got married below 14 ");
						
						} catch (ParseException e) {

							e.printStackTrace();
						}

					}
				}

		}

	}
	public static void deathNotAfterCurrDate ()
	{
		System.out.println("***********Death Should not be after Current date***************");
		Date Ddate;
		Date Curr=new Date();
		for ( int i=0;i<indRecords.length && indRecords[i]!=null; i++)
		{
			if (indRecords[i].DeathDate!=null)
			{ 
				try {
					Ddate=formatter.parse(indRecords[i].DeathDate);
					if (Ddate.after(Curr))
						System.out.println("Individual Named "+indRecords[i].Name+"   with Id: "+indRecords[i].Id+
								" has death date "+indRecords[i].DeathDate+" which is after the current date  "+ Curr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}	
	}
	public static void ListUnmarried()
	{
		System.out.println("***********Unmarried people in the family***************");
		for ( int i=0;i<indRecords.length && indRecords[i]!=null; i++)
		{
		if (indRecords[i].FamS==null)
		{
			System.out.println(indRecords[i].Id+" "+indRecords[i].Name);
		}
		}
	}
	public static void CheckMarriageAfterToday()
	{
		int i;
		System.out.println("\n********************Check for Marriage date after current date******************");
		Date today = new Date();
		for(i=0;i<Family.length && Family[i]!=null;i++)
		{
			try
			{
				if(Family[i].MarriageDate!=null)
				{
					Date Mrgdate = formatter.parse(Family[i].MarriageDate);
					if(Mrgdate.after(today))
					{
						System.out.println("Error in file:The marriage date in Family " +Family[i].FamilyId+ " is not valid. The date is a future date");
					}

				}
			}
			catch(ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public static void CheckMotherOver60() {
		System.out.println("*********** Mothers Who Gave Birth at the Age of 60 ***************");
		Date MotherBirth = new Date();
		ArrayList<Date> SavedChlidrenBirths = new ArrayList<Date>();
		for (int i=0; i<indRecords.length && indRecords[i]!=null ; i++) {
		  try{
		    if (indRecords[i].SEX.equalsIgnoreCase("F") && indRecords[i].FamS != null){
		    	MotherBirth = formatter.parse(indRecords[i].BirthDate);
		    	for(int j=0; j<Family.length && Family[j]!=null; j++) {
		    	  if(Family[j].FamilyId.equalsIgnoreCase(indRecords[i].FamS)){
		    		for(int c=0; c<Family[j].ChlidrenIds.size(); c++) {
		    		  for (int l=0; l<indRecords.length && indRecords[l]!=null ; l++) 
		    			if(indRecords[l].Id.equalsIgnoreCase(Family[j].ChlidrenIds.get(c))) {
		    			  SavedChlidrenBirths.add(formatter.parse(indRecords[l].BirthDate));
		    			}
		    		}
		    	  }
		    	 }
		     }
		    for(int d=0; d<SavedChlidrenBirths.size(); d++){
		    	int years = SavedChlidrenBirths.get(d).getYear() - MotherBirth.getYear();
		    	if(years >= 60) {
		    		System.out.println("Mother with an Id " + indRecords[i].Id +
		    				" has given birth at the age of 60.");
		    		break;
		    	}
		    }
	    	MotherBirth = new Date();
	    	SavedChlidrenBirths = new ArrayList<Date>();
		  } catch (ParseException e) {
			  e.printStackTrace();
		  }
		}
	}
	
	public static void CheckBirthBeforeToday() {
		System.out.println("*********** Individuals who's Birthdates after Today ***************"); 
		Date Today = new Date();
		for (int i=0; i<indRecords.length && indRecords[i]!=null ; i++) {
			try {
				Date IndiBirth = formatter.parse(indRecords[i].BirthDate);
				if (IndiBirth.compareTo(Today) > 0) {
					System.out.println("There is an individual with an Id "+indRecords[i].Id
							  +" who has a wrong birthdate.");
				}				
			} catch (ParseException e) {
				  e.printStackTrace();
			}			
		}
	}
	
	public static void ageDifferOfSpouse ()
	{
		System.out.println("***********Age Difference between Spouse***************");
		int wifeAge=0, HusbAge=0, ageDiff=-1;
		for (int i=0;i<Family.length &&Family[i]!=null;i++)
		{
			if (Family[i].HusbandId!=null && Family[i].WifeId!=null)
			{
				for ( int j=0; j<indRecords.length && indRecords[j]!=null;j++)
				{	if (indRecords[j].Id.equals(Family[i].WifeId))
					wifeAge=indRecords[j].age;
				if (indRecords[j].Id.equals(Family[i].HusbandId))
					HusbAge=indRecords[j].age;

				}

				ageDiff=Math.abs(HusbAge-wifeAge);
			}
			//System.out.println(wifeAge+"  "+HusbAge);

			if (ageDiff!=-1)
				System.out.println("The age difference between spouse in Family "+Family[i].FamilyId+" is "+ ageDiff);
		}
	}

	public static void moreThan10Children ()
	{
		System.out.println("************ More Than 10 Children**************");
		for ( int i=0;i<Family.length && Family[i]!=null;i++)
		{
			if (  Family[i].ChlidrenIds.size()>10	)
				System.out.println(" Family ("+Family[i].FamilyId+") has more than 10 children");

		}
	}


	public static void ageDiffBetweecChildParent (){
		
		System.out.println("************ Age Difference between Child and Parents**************");
		
			int wifeAge=0, HusbAge=0, ageDiff=-1, ageDiff2=-1;
			for (int i=0;i<Family.length &&Family[i]!=null;i++)
			{
				if (Family[i].HusbandId!=null && Family[i].WifeId!=null)
				{
					for ( int j=0; j<indRecords.length && indRecords[j]!=null;j++)
					{	if (indRecords[j].Id.equals(Family[i].WifeId))
						wifeAge=indRecords[j].age;
					if (indRecords[j].Id.equals(Family[i].HusbandId))
						HusbAge=indRecords[j].age;

					}
					for (int k=0;k<Family[i].ChlidrenIds.size()&& Family[i].ChlidrenIds!=null;k++)
						for (int j=0;j<indRecords.length && indRecords[j]!=null;j++)
							if (indRecords[j].Id.equals(Family[i].ChlidrenIds.get(k))){
								ageDiff=HusbAge-indRecords[j].age;
								ageDiff2=wifeAge-indRecords[j].age;
								if (ageDiff >0)
									System.out.println("Age difference between child "+indRecords[j].Id+ " and his father is "+ageDiff);
								if(ageDiff2>0)
									System.out.println("Age difference between child "+indRecords[j].Id+ " and his mother is "+ageDiff2);
}
					
				}
		}
	}

		
	
	public static void ListDead ()
	{ 
		System.out.println("********List dead people*******");
	        System.out.println("Dead individuals are");
		for (int i=0;i<indRecords.length && indRecords[i]!=null;i++)
		{
			if (indRecords[i].isDead==true)
			System.out.println(indRecords[i].Name+" "+indRecords[i].Id);
		}
			
	}
		
		
		
	
	
	
	
	
	
	
	
	
}
