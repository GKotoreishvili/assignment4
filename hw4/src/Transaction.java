
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public final class Transaction {

    private final int WithdrawID;
    private final int DepositID;
    private final int Amount;

    public Transaction(int ID1, int ID2, int amount) {

        WithdrawID = ID1;
        DepositID = ID2;
        Amount = amount;
    }

    public int getWithID() {

        return WithdrawID;
    }

    public int getDepID() {

        return DepositID;
    }

    public int getAmount() {

        return Amount;
    }

}
