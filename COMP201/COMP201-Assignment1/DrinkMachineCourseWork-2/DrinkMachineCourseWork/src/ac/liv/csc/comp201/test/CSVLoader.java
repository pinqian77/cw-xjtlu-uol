package ac.liv.csc.comp201.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;


public class CSVLoader {
	private Hashtable <String,String[]> list=new <String,String[]>Hashtable();
	private ArrayList <String []>fields=new ArrayList<String []>();
	private String fileName;
	private int keyIndex=-1;
	private boolean loaded=false;
	private static final int COLS=20;
	
	
	public CSVLoader(String fileName,int keyIndex) {
		this.fileName=fileName;
		this.keyIndex=keyIndex;
		load();
	}
	
	
	public String [] getAllStrings(String input) {
		int count=0;
		boolean inquotes=false;
		for (int idx=0;idx<input.length();idx++) {
			char next=input.charAt(idx);
			if (next=='"') {
				inquotes=!(inquotes);
			}
			if (inquotes) continue;
			if (next==',') {
				count++;
			}
		}
		count++;
		String ret[]=new String[count];
		count=0;
		StringBuffer sb=new StringBuffer();
		for (int idx=0;idx<input.length();idx++) {
			char next=input.charAt(idx);
			if (next!=',') {
			   sb.append(next);
			}
			if (next=='"') {
				inquotes=!(inquotes);
			}
			if (inquotes) continue;
			if (next==',') {
				ret[count++]=sb.toString().trim();
				sb=new StringBuffer();
			}
			
		}
		ret[count++]=sb.toString().trim();
		return(ret);
	}
	
	public void save() {
		try {
			FileWriter writer=new FileWriter(fileName);
		Collection <String []> values=list.values();
		
		for (int idx=0;idx<fields.size();idx++) {
			String [] line=fields.get(idx);
			if (idx==0) {
				//System.out.println("Len of line is "+line.length);
			}
			for (int col=0;col<line.length;col++) {
				if (col>0) {
					writer.write(",");					
				}
				writer.write(line[col]);
				
			}
			writer.write("\n");
		}
		writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateField(String key,String value,int col) {
		if (list.containsKey(key)) {
			String line[]=list.get(key);
			line[col]=value;
			save();
		} else {
			System.out.println("Error could not find .."+key);
		}
	}
	
	private void load() {
		if (loaded) return;
		loaded=true;
		String line;
		long index=0;
		boolean first=true;
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader((InputStream)(new FileInputStream(fileName))));
			
			line = reader.readLine();
			//System.out.println("First line is "+line);
			
		while (line!=null) {
			line=line.trim();
			String data[]=getAllStrings(line);
			String splitLine[]=new String[COLS];
			for (int idx=0;idx<data.length;idx++) {
				splitLine[idx]=data[idx];
			}
			for (int idx=data.length;idx<COLS;idx++) {
				splitLine[idx]="";
			}
			
			if (keyIndex<data.length) {
				if (first) {
					//System.err.println("fields are "+splitLine[13]);
					first=false;
				}
				
			    list.put(splitLine[keyIndex].trim(),splitLine);
			    fields.add(splitLine);
			   // System.out.println("Key is "+splitLine[this.keyIndex]);
			} else {
				list.put(""+index++,splitLine);
			    fields.add(splitLine);
				System.err.println("No idx line is "+line);
			}
			line = reader.readLine();
			
		}
		reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getItem(String key,int index) {
		load();
		key=key.trim();
		if (list.containsKey(key)) {
		   return(list.get(key)[index]);
		} else {
			System.err.println("Failed to find key "+key+" in file "+this.fileName);
			return("");
		}
		
	}
	
	public Enumeration getAllKeys() {
		return(list.keys());
	}
	
	
	
	public static void main(String argv[]) {
		CSVLoader loader=new CSVLoader("C:\\comp201_2018_2019\\cw1.2\\marks2.csv",0);
//		System.out.println("Item is "+loader.getItem("sgszho12",3));
	}

}
