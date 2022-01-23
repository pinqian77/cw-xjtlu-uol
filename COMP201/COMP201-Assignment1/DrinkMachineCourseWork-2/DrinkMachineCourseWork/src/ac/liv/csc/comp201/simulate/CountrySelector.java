package ac.liv.csc.comp201.simulate;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import ac.liv.csc.comp201.control.Coin;
import ac.liv.csc.comp201.model.IMachine;

public class CountrySelector  extends JFrame implements ActionListener {
	private JComboBox currList =null; 
	/**
	 * 
	 */
	
	IMachine machine;
	
	private static final long serialVersionUID = 1L;
	
	public CountrySelector(IMachine machine) {
		String [] currencies= {"GBP","RMB","USD"};
		this.machine=machine;
		currList =new JComboBox(currencies);
		this.add(currList);
		currList.setVisible(true);
		this.setSize(120, 40);
		this.setBounds(new Rectangle(120,120,120,40));
		this.setVisible(true);
		this.validate();
		currList.addActionListener(this);
		int width=250;
		int height=600;
		
		double screenWidth=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double screenHeight=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		
		this.setBounds((int)(screenWidth/2)-120,40, width,height);
		
		this.setSize(this.getPreferredSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
        String currency = (String)cb.getSelectedItem();
        Coin.setCurrencyCode(currency);
        machine.resetCoinHandler();
	}

}
