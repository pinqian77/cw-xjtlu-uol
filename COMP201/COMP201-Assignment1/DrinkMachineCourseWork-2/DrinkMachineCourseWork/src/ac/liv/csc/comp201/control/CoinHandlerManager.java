package ac.liv.csc.comp201.control;

import java.util.ArrayList;
import java.util.Collections;

import ac.liv.csc.comp201.model.IMachine;

public class CoinHandlerManager {
	public void handleCoinsIn(IMachine machine) {
		String code = machine.getCoinHandler().getCoinKeyCode();

		// add the current inserted coin value to machine balance
		if (code != null){
			System.out.println("Code is "+ code);
			int currentWorth = Coin.getCoinFromCode(code).getCoinWorth();
			machine.setBalance(machine.getBalance() + currentWorth);
		}
	}
	
	/**
	 * Pay's the current balance in coins if it can be paid, this is a best try function
	 * if not enough coins then it pays the best it can do
	 */
	public void returnChange(IMachine machine) {
		machine.getDisplay().setTextString("Trying to pay change back");
		System.out.println("PAYING CHANGE!!!! balance is "+ machine.getBalance());

		// initialize expected return value
		int returnValue = machine.getBalance();

		// sort the coins list
		Coin[] coins = Coin.getAllcoins();
		ArrayList<Coin> coinList = new ArrayList<>();
		for (Coin c: coins){
			coinList.add(c);
		}
		Collections.sort(coinList);

		// start to return coins from large value to small value
		for (int i = 0; i < coinList.size(); i ++){
			// break if expected return value is achieved
			if (returnValue == 0){
				break;
			}
			// keep returning coins
			int currentWorth = coinList.get(i).getCoinWorth();
			String currentCode = coinList.get(i).getCoinCode();
			while (currentWorth <= returnValue && machine.getCoinHandler().coinAvailable(currentCode)){
				// dispense coin
				machine.getCoinHandler().dispenseCoin(currentCode);
				// update expected return value
				returnValue -= currentWorth;
				// update balance after dispensing
				machine.setBalance(machine.getBalance() - currentWorth);
				// System.out.println("Return: " + currentWorth + "; Remain: " + returnValue + "; Balance: " + machine.getBalance());
			}
		}
	}

}
