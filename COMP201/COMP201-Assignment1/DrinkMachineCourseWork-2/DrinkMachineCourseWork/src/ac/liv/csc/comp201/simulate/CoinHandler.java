package ac.liv.csc.comp201.simulate;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import ac.liv.csc.comp201.control.Coin;
import ac.liv.csc.comp201.model.ICoinHandler;
import ac.liv.csc.comp201.model.IMachine;

public class CoinHandler extends JFrame implements ICoinHandler, Temp {
	
	
	
	private static final String fileName="coinHandlerState.txt";

	private static final int MAX_COINS = 20;
	
	private String coinTray="";
	
	public synchronized void  insertCoin(String code) {
		if (!coinHandlerOpen) return;
		coinCodeBuffer.addElement(code);
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinTray()
	 */
	@Override
	public String getCoinTray() {
		return(coinTray);
	}
	
	private Vector <String> coinCodeBuffer=new Vector <String> ();
	
	private int coinHopperLevels[]=new int[MAX_COINS];
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#coinAvailable(java.lang.String)
	 */
	@Override
	public boolean coinAvailable(String coinCode) {
		for (int index=0;index<Coin.getCoinCodes().length;index++) {
			if (coinCode.equals(Coin.getCoinCodes()[index])) {
				if (coinHopperLevels[index]==0) {
					return(false);
				}
				return(true);
			}		}
		return(false);		
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#setHopperLevel(java.lang.String, int)
	 */
	@Override
	public void setHopperLevel(String coinCode,int level) {
		for (int index=0;index<Coin.getCoinCodes().length;index++) {
			if (coinCode.equals(Coin.getCoinCodes()[index])) {
				coinHopperLevels[index]=level;
				//save();
			}
		}
	}
	
	private void save() {
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			return;
		}
		pw.println(coinTray);
		for (int index=0;index<coinHopperLevels.length;index++) {
			pw.println(""+coinHopperLevels[index]);
		}
		pw.close();		
	}
	
	private void load() {
		BufferedReader reader=null;
		String tray=null;
		try {
			reader=new BufferedReader(new FileReader(fileName));
			tray = reader.readLine();
		} catch (IOException e) {
			tray=null;
		}
		if (tray==null) {
			System.out.println("Creating new state file...");
			this.coinTray=tray;
			for (int index=0;index<coinHopperLevels.length;index++) {
				coinHopperLevels[index]=10;
			}
			save();		// create state if not run before	
			return;
		}
		System.out.println("Tray is "+tray);
		try {
		String coinLevel=reader.readLine();
		int index=0;
		while (coinLevel!=null) {
			int level=Integer.parseInt(coinLevel);
			this.coinHopperLevels[index]=level;
			coinLevel=reader.readLine();
			index++;
			if (index==coinHopperLevels.length) break;
		}
		reader.close();
		}
		catch (IOException e) {
			tray=null;
		} 	}
	
	private void addCoin(String coinCode) {
		for (int index=0;index<Coin.getCoinCodes().length;index++) {
			if (coinCode.equals(Coin.getCoinCodes()[index])) {
				coinHopperLevels[index]++;
				save();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinHopperLevel(java.lang.String)
	 */
	@Override
	public int getCoinHopperLevel(String coinCode) {
		for (int index=0;index<Coin.getCoinCodes().length;index++) {
			if (coinCode.equals(Coin.getCoinCodes()[index])) {
				return (coinHopperLevels[index]);
			}
		}
		return(0);
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#dispenseCoin(java.lang.String)
	 */
	@Override
	public boolean dispenseCoin(String coinCode) {
		for (int index=0;index<Coin.getCoinCodes().length;index++) {
			if (coinCode.equals(Coin.getCoinCodes()[index])) {
				if (coinHopperLevels[index]==0) {
					return(false);
				}
				coinTray=coinTray+Coin.getCoinNames()[index]+" ";
				coinHopperLevels[index]-=1;
				save();
				return(true);
			}
		}
		return(false);
	}
	
	private int coinWidth=120;
	
	private boolean coinHandlerOpen=true;
	
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.ICoinHandler#getCoinKeyCode()
	 */
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinKeyCode()
	 */
	@Override
	public synchronized String getCoinKeyCode() {
		if (coinCodeBuffer.size()>0) {
			String code=coinCodeBuffer.get(0);
			coinCodeBuffer.remove(0);
			System.out.println("GOT CODE "+code);
			return(code);
		}	
		return(null);
	}
	
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#lockCoinHandler()
	 */
	@Override
	public void lockCoinHandler() {
		coinHandlerOpen=false;
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#unlockCoinHandler()
	 */
	@Override
	public void unlockCoinHandler() {
		coinHandlerOpen=true;
	}
	
	
	public static void main(String args[]) {
		System.out.println("starting...");
		CoinHandler instance=new CoinHandler(null);
		instance.setVisible(true);		
	}
	
	
	
	
	
	
	public static BufferedImage scale(BufferedImage sbi, int imageType, float dWidth, float dHeight) {
	    BufferedImage dbi = null;
	    
	    float fWidth=dWidth/sbi.getWidth();
	    float fHeight=dHeight/sbi.getHeight();
	    if(sbi != null) {
	        dbi = new BufferedImage((int)dWidth, (int)dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
	
	public CoinHandler(IMachine machine) {
		load();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setBounds(10,screenSize.height-coinWidth-30,coinWidth*6+20,coinWidth+30);
		//this.setUndecorated(true);
		setTitle("Coin Handler");
		setAlwaysOnTop(true);
	//	System.out.println("starting..");
		setLayout(new GridLayout(1, 6, 0, 0));
		setCoinHandler();
		this.setVisible(true);
	}

	private void setCoinHandler() {
		
		for (int index=0;index<Coin.getCoinImageNames().length;index++) {
			JButton button = new JButton("");
			try {
				System.out.println("Image is "+"/images/"+Coin.getCoinImageNames()[index]);
				InputStream is=getClass().getResourceAsStream("/images/"+Coin.getCoinImageNames()[index]);
				
			    BufferedImage img = ImageIO.read(is);
			    BufferedImage i=scale(img,img.getType(),coinWidth,coinWidth);
			    ImageIcon icon=new ImageIcon(i);
			    
			    button.setIcon(icon);
			    
			  } catch (Exception ex) {
			    ex.printStackTrace();
			  }
			//buttons[index]=button;
			button.setActionCommand(Coin.getCoinCodes()[index]);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (coinHandlerOpen) {
					   addCoin(actionEvent.getActionCommand());
					   coinCodeBuffer.add(actionEvent.getActionCommand());				}
				    }
				
			});
			this.getContentPane().add(button);
		}
	}
	
	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void clearCoinTry() {
		coinTray="";
		save();
	}

	/**
	 * @return the coinHandlerOpen
	 */
	public final boolean isCoinHandlerOpen() {
		return coinHandlerOpen;
	}

}
