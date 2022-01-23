package ac.liv.csc.comp201.control;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Coin implements Comparable <Coin> {
	public Coin(String coinCode, int coinWorth,String coinName) {
		this.coinCode = coinCode;
		this.coinWorth = coinWorth;
		this.coinName=coinName;
	}
	private String coinCode;
	private String coinName;
	private int coinWorth=0;
	/**
	 * @return the coinCode
	 */
	public final String getCoinCode() {
		return coinCode;
	}
	/**
	 * @return the coinWorth
	 */
	public final int getCoinWorth() {
		return coinWorth;
	}
	
	private static void setRMB() {
		coinImageNames=rmbCoinImageNames;
		// 1 Yuan
		// 5 Jiao
		// 1 Jiao
		// 5 fen
		// 2 fen
		// 1 fen
		allCoins=new Coin[] {
				new Coin("z1",100,"1 Yuan"),
				new Coin("z2",50,"5 Jiao"),
				new Coin("z7",10,"1 Jiao"),
				new Coin("z9",5,"5 Fen"),
				new Coin("bc",2,"2 Fen"),
				new Coin("bd",1,"1 Fen")	
		};
	}
	
	private static void setUSD() {
		// 25 C
		// 10 C
		// 5 C
		// 1 C
		coinImageNames=usdCoinImageNames;
		
		allCoins=new Coin[] {
				new Coin("us1",25,"25 c"),
				new Coin("us2",10,"10 c"),
				new Coin("us3",5,"5 c"),
				new Coin("us4",1,"1 cent"),
		};
	}
	
	private static String coinCodes[]=null;
	
	private static String coinNames[]=null;
	
	
	
	private static void setCoinCodes() {
		coinCodes=new String[allCoins.length];
		coinNames=new String[allCoins.length];
		int count=allCoins.length;
		Arrays.sort(allCoins, Collections.reverseOrder());
		for (int idx=0;idx<allCoins.length;idx++) {
			coinCodes[idx]=allCoins[idx].getCoinCode();
			coinNames[idx]=allCoins[idx].getName();
		}
	}
	
	private String getName() {
		return(coinName);
	}
	public static void setCurrencyCode(String currency) {
		
		switch (currency) {
		case "GBP" :
			setGBP();break;
		case "RMB" :
				setRMB();break;
		case "USD" :
				setUSD();break;

		}
		setCoinCodes();
	}
	
	private static void setGBP() {
		coinImageNames=gbpCoinImageNames;
		allCoins=new Coin[] {
		new Coin("ef",100,"100p"),
		new Coin("ab",1,"1p"),
		new Coin("ac",5,"5p"),
		new Coin("ba",10,"10p"),
		new Coin("bc",20,"20p"),
		new Coin("bd",50,"50p")				
		};		
	}
	/**
	 * Coin's defined for coin handler to handle incoming codes
	 */
	private static Coin allCoins[]={
			new Coin("ef",100,"100p"),
			new Coin("ab",1,"1p"),
			new Coin("ac",5,"5p"),
			new Coin("ba",10,"10p"),
			new Coin("bc",20,"20p"),
			new Coin("bd",50,"50p")				

	};
	
	public static Coin getCoinFromCode(String code) {
		for (int index=0;index<allCoins.length;index++) {
			if (allCoins[index].coinCode.equals(code)) {
				return(allCoins[index]);
			}
		}
		return(null);
	}
	/**
	 * @return the allcoins
	 */
	public static final Coin[] getAllcoins() {
		return allCoins;
	}
	
	private static String coinImageNames[]=null;
	
	private static final String gbpCoinImageNames[]={"coin1p.jpg","coin5p.jpg","coin10p.jpg","coin20p.jpg","coin50p.jpg","pound.jpg"};

	private static final String usdCoinImageNames[]={"coinusd1.jpg","coinusd5.jpg","coinusd10.jpg","coinusd25.jpg" };

	private static final String rmbCoinImageNames[]={"rmb1.jpg","rmb2.jpg","rmb5.jpg","rmb10.jpg","rmb50.jpg","rmb100.jpg"};
	
	public static final String [] getCoinImageNames() {
		
		return(coinImageNames);
	}
	
	public static String[] getCoinCodes() {
		return coinCodes;
	}
	public static String[] getCoinNames() {
		return coinNames;
	}
	@Override
	public int compareTo(Coin o) {
		// TODO Auto-generated method stub
				if (o.getCoinWorth()<getCoinWorth()) {
					return(-1);
				}
				if (o.getCoinWorth()==getCoinWorth()) {
					return(0);
				}
				return(1);	}

}
