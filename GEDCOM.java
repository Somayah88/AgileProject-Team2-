import java.io.*;
import java.util.ArrayList;
import java.util.Date;

class IndividualRecord {

	String SEX, Id, BirthDate, FamS, FamC, Marr, husb, wife, Fam, Name;
	Date DeathDate;

	public IndividualRecord(String id, String name, String sex,
			String birthdate, String Fams, String Famc) {
		this.Id = id;
		this.Name = name;
		this.SEX = sex;
		this.BirthDate = birthdate;
		this.FamS = Fams;
		this.FamC = Famc;

	}

}

class FamilyInfo {
	String FamilyId;
	String HusbandId;
	ArrayList<String> WifeId = new ArrayList<String>();
	ArrayList<String> ChlidrenIds = new ArrayList<String>();
}

public class P03 {

	private static IndividualRecord[] indRecords;
	private static FamilyInfo[] Family;
	private static int indiFlag = 0, item = 0, item1 = -1;
	private static String IndiTag = null;
	private static String name = null, sex = null, famc = null, fams = null,
			id = null, birt = null;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		indRecords = new IndividualRecord[5000];
		Family = new FamilyInfo[1000];

		FileInputStream fis = new FileInputStream("FamilyTreeP.ged");
		DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
		String Line, FamilyId = null;

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
					|| Tag.startsWith("D", 0) || Tag.startsWith("F", 0)) {
				CheckIndivduals(Tag, info);
			}

			if (Tag.startsWith("H", 0) || Tag.startsWith("W", 0)
					|| Tag.startsWith("C", 0)) {
				CheckFamilyMember(Tag, info);
			}

			if (Tag.equals("INDI") || Tag.equals("FAM")) {
				if (id != null && indiFlag != 0) {
					indRecords[item] = new IndividualRecord(id, name, sex,
							birt, fams, famc);
					item++;
					indiFlag = 0;
					id = null;
					name = null;
					sex = null;
					birt = null;
					famc = null;
					fams = null;
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

		printIndividuals();
		System.out.println("###########################");
		printFamilies();

	}

	private static void CheckIndivduals(String Tag, String[] info) {
		if (Tag.equals("NAME")) {
			name = info[2] + " " + info[3];
			indiFlag++;
		} else if (Tag.equals("SEX")) {
			sex = info[2];
			indiFlag++;
		} else if (Tag.equals("DATE")) {
			birt = info[2] + info[3] + info[4];
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
			Family[item1].WifeId.add(IndiTag);
		}

		else if (Tag.equals("CHIL")) {
			IndiTag = info[2];
			Family[item1].ChlidrenIds.add(IndiTag);
		}
	}

	private static void printIndividuals() {
		for (int i = 0; i < indRecords.length && indRecords[i] != null; i++) {

			System.out.println(indRecords[i].Name + " " + indRecords[i].Id);
		}
	}

	private static void printFamilies() {
		for (int count = 0; count <= item1; count++)
			for (int i = 0; i < Family[count].WifeId.size(); i++) {
				System.out.println(Family[count].FamilyId + ", husband "
						+ Family[count].HusbandId + ", wife "
						+ Family[count].WifeId.get(i));
				for (int c = 0; c < Family[count].ChlidrenIds.size(); c++) {
					System.out.println(", with child "
							+ Family[count].ChlidrenIds.get(c));
				}
			}
	}
}
