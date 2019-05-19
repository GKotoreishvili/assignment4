
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;
import java.lang.*;


public class Bank{
    // Constant variables
    private static final int AccNumber = 20;
    private static final int AccAmount = 1000;

    // Instance variables
    private BlockingQueue<Transaction> threads;
    private Account[] accounts;

        public Worker getWorkerInstance(int i) {
            return new Worker(i);
        }
        private class Worker implements Runnable{

            private int ID;

            public Worker(int id) {

                ID = id;
            }

            @Override
            public void run() {

                while (true) {
                    try {

                        Transaction tran = threads.take();
                        synchronized (tran) {

                            if (tran.getWithID() == -1) {
                                threads.put(tran);
                                break;
                            }

                            Account withAcc = accounts[tran.getWithID()];
                            Account depAcc = accounts[tran.getDepID()];
                            int amount = tran.getAmount();

                            if (withAcc.getAmount() >= amount) {
                                withAcc.Withdraw(amount);
                                depAcc.Deposit(amount);
                            }
                        }
                    } catch (InterruptedException ies) {
                        ies.printStackTrace();
                    }
                }
            }
        }


    public Bank() {

        accounts = new Account[AccNumber];
        for (int i=0; i<accounts.length; i++) {
            accounts[i] = new Account(i, AccAmount);

        }

        threads = new ArrayBlockingQueue<>(AccNumber);
    }


    public void putElem(Transaction elem) {

        try {
            threads.put(elem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Account[] getAccounts() {

        return this.accounts;
    }


    public static void main(String args[]) {

        int threadsCount = Integer.parseInt(args[1]);
        Bank bank = new Bank();

        Thread[] th = new Thread[threadsCount];
        for (int i=0; i<threadsCount; i++) {
            th[i] = new Thread(bank.getWorkerInstance(i));
            th[i].start();
        }

        int id1=0, id2=0, money=0;
        try {

            Scanner scanner = new Scanner(new File(args[0]));
            while(scanner.hasNextInt())
            {
                id1 = scanner.nextInt();
                id2 = scanner.nextInt();
                money = scanner.nextInt();
                bank.putElem(new Transaction(id1, id2, money));
            }
            bank.putElem(new Transaction(-1, -1, -1));
        }
        catch (IOException e) {
            System.out.println("Error reading file '" +
                    args[0] + "'");
        }

        for (int i=0; i<threadsCount; i++) {
            try {
                th[i].join();
            } catch (InterruptedException ies) {
                ies.printStackTrace();
            }
        }

        Account[] acc = bank.getAccounts();
        for (int i=0; i<acc.length; i++) {
            System.out.println(acc[i] + " " + i);
        }
    }

}
