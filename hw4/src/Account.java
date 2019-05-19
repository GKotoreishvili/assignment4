
import java.util.*;

/*
 *
 */
public class Account {

	private int ID;
	private int Amount;
	private int NumberOfTransactions;

	public Account(int id, int amount) {

		ID = id;
		NumberOfTransactions = 0;
		Amount = amount;
	}

	public int getID() {

		return ID;
	}

	public int getAmount() {

		return Amount;
	}

	public int getNumbOfTrans() {

		return NumberOfTransactions;
	}

	public synchronized boolean Withdraw(int amount) {

		if (Amount >= amount) {
			Amount -= amount;
			NumberOfTransactions++;
			return true;
		}
		return false;
	}

	public synchronized void Deposit(int amount) {

		Amount += amount;
		NumberOfTransactions++;
	}

	@Override
	public synchronized String toString() {

		String toString = new String("");
		toString = "acct:" + Integer.toString(ID);
		toString += " bal " + Integer.toString(Amount);
		toString += " trans" + Integer.toString(NumberOfTransactions);
		return toString;
	}
}
