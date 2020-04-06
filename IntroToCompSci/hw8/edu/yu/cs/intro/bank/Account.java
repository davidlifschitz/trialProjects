package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;

import java.util.Arrays;

public abstract class Account {
    private final long accountNumber;
    private Patron patron;
    private Transaction[] txHistory = new Transaction[1];
    protected double cash;

    protected Account(long accountNumber, Patron patron){
        this.accountNumber = accountNumber;
        this.patron = patron;
    }
    protected long getAccountNumber() {
        return this.accountNumber;
    }
    protected Patron getPatron() {
        return this.patron;
    }
    protected double getCash(){return this.cash;}
    /**
     * returns a copy of the txHistory array
     * why do you think we return a copy and not the original array?
     */
    protected Transaction[] getTransactionHistory(){
        return Arrays.copyOf(this.txHistory,this.txHistory.length);
    }
    protected void depositCash(double amount){
        if(amount < 0){
            throw new IllegalArgumentException("can't deposit negative cash");
        }
        this.cash += amount;
    }
    //*************************************************
    //below are methods you must complete in this class
    //*************************************************
    /**add a tx to the tx history of this account*/
    protected void addTransactionToHistory(Transaction tx)
    {
        Transaction[] transactionArray = getTransactionHistory();
        for(int i = 0; i < transactionArray.length; i++){
            if (transactionArray[transactionArray.length -1] == null) {
                if(transactionArray[i] != null) {
                    continue;
                } else {
                    transactionArray[i] = tx;
                    break;
                }
            } else {
                Transaction[] newTransactionArray = new Transaction[transactionArray.length+1];
                
                for(int j = 0; j < transactionArray.length; j++)
                {
                    newTransactionArray[j] = transactionArray[j];
                }
                transactionArray = newTransactionArray;
                
            }
            
        }
        this.txHistory = transactionArray;
    }
    protected void withdrawCash(double amount) throws InsufficientAssetsException
    {
        if(amount < this.cash)
        {
            this.cash -= amount;
        } else
        {
            throw new InsufficientAssetsException();
        }
    }

    //*********************************************************
    //below are abstract methods that subclasses must implement
    //*********************************************************
    protected abstract double getTotalBalance();
    protected abstract double getAvailableBalance();
}