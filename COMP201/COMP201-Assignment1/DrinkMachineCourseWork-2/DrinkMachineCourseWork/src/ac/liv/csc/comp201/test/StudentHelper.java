package ac.liv.csc.comp201.test;

import java.util.Enumeration;

public class StudentHelper {
	private CSVLoader shortList;
	private CSVLoader shortListEmail;
	
	private CSVLoader longList;
	private CSVLoader longListEmail;
	
	
	public StudentHelper() {
        shortList=new CSVLoader("C:\\comp201_2018_2019\\cw1.2\\comp201vital.csv",0);
		shortListEmail=new CSVLoader("C:\\comp201_2018_2019\\cw1.2\\comp201vital.csv",3);
		System.out.println("Loading long list id");
		longList=new CSVLoader("C:\\comp201_2018_2019\\cw1.2\\marks2.csv",0);
		System.out.println("Loading long list email");
		longListEmail=new CSVLoader("C:\\comp201_2018_2019\\cw1.2\\marks2.csv",2);

	}
	
	public String shortToLongId(String shortId) {
		String email=shortList.getItem(shortId,3);
		String id="";
		if (email.trim().length()>0) {
		   id=longListEmail.getItem(email,0);
		}
		id=id.trim();
		return(id);
	}
	
	public String longToShortId(String longId) {
		String email=longList.getItem(longId,2);
		String id="";
		if (email.trim().length()>0) {
		   id=shortListEmail.getItem(email,0);
		}
		id=id.trim();
		return(id);
	}
	
	public void updateMark(String shortID,int mark) {
		String longID=shortToLongId(shortID);
		System.out.println("Long id is "+longID);
		if (longID.length()>0) {
			longList.updateField(longID,""+mark,14);
		} else {
			System.out.println("Could not find "+shortID);
		}
	}
	
	
	public static void main(String argv[]) {
		StudentHelper helper=new StudentHelper();
		//System.out.println("Long id "+helper.shortToLongId("sgszho12"));
		Enumeration keyList=helper.longList.getAllKeys();
		int count=0;
		while (keyList.hasMoreElements()) {
			
			String key=(String)keyList.nextElement();
			key=key.trim();
			/*if (key.trim().equals("201317721")) {
				continue;
			}
			if (key.trim().equals("201317521")) {
				continue;
			}
			if (!key.startsWith("201")) {
				continue;
			}
			*/
			String longId=helper.longToShortId(key);
			
			if (longId.length()==0) {
				System.err.println("Problem could not find id "+key);
				System.out.println("Email is "+helper.longList.getItem(longId,3));
				System.exit(1);
				
			} else {
				count++;
				System.out.println("Id "+key+" translates to "+longId);
			}
		}
		System.out.println("count is "+count);
	}


}
