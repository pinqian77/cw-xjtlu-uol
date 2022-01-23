package ac.liv.csc.comp201.control;

import java.util.Hashtable;


public class Strings {
	
	private static Hashtable <Integer,String>allStrings=new Hashtable <Integer,String>();
	
	public static final int INVALID=1;
	public static final int UNAVAILABLE=2;
	public static final int CASHLOW=3;
	public static final int BALANCE=4;
	public static final int CALIBRATING=5;
	public static final int DRINKREADY=6;
	public static final int OUTOFSERVICE = 7;
	public static final int SHUTDOWN =8;


	
	public static void setLanguage(String lang) throws Exception {
		switch(lang) {
		case "en" :
			setEnglishStrings();
			break;
		case "cn" :
			setChineseStrings();
			break;
		default :
			throw new Exception("Unknown language exception");
		}
	}

	
	private static void put(int key,String string) {
		allStrings.put(new Integer(key),string);
	}
	
	/**
	 * Google translated, needs proof reading
	 */
	private static void setChineseStrings() {
		put(INVALID,"饮料代码无效");
		put(UNAVAILABLE,"喝不用，请选择另一个");
		put(CASHLOW,"请输入更多钱");
		put(BALANCE,"平衡");
		put(CALIBRATING,"Calibrating"); // TO DO needs translation
		put(DRINKREADY,"Drink ready");// TO DO needs translation
		put(OUTOFSERVICE,"Out of service");// TO DO needs translation
		put(SHUTDOWN,"Fatal hardware error shutting down");// TO DO needs translation
		
	}


	private static void setEnglishStrings() {
		put(INVALID,"Invalid drink code");
		put(UNAVAILABLE,"Drink unavaible, please pick another");
		put(CASHLOW,"Please enter more money");
		put(BALANCE,"Balance");
		put(CALIBRATING,"Calibrating");
		put(DRINKREADY,"Drink ready");
		put(OUTOFSERVICE,"Out of service");
		put(SHUTDOWN,"Fatal hardware error shutting down");
		
		
	}


	private static String lang="";

	public static String getString(int key) {
		if (lang.equals("")) {
		   try {
			setLanguage("en");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		Integer keyi=new Integer(key);
		if (allStrings.containsKey(key)) {
			return(allStrings.get(keyi));
		}
		return("Undefined "+key);
	}
	
	
	
}
