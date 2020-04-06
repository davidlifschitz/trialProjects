package edu.yu.cs.intro.simpleBank;

import java.util.HashSet;

import edu.yu.cs.intro.simpleBank.Transaction.TRANSACTION_TYPE;
import edu.yu.cs.intro.simpleBank.InsufficientAssetsException;


public class Account {
    private final long accountNumber;
    private double cash;

    protected Account(final long accountNumber){this.accountNumber = accountNumber;}
    protected long getAccountNumber() {return this.accountNumber;}
    protected double getAvailableBalance(){return this.cash;}
    protected Patron getPatron() {
        Patron newPatron = new Patron("firstName", "lastName", 12345, "userName", "password");
        for (Patron patron : Bank.getBank().getBankPatrons()) {
            if(patron.getAccount(this.accountNumber) != null)
            {
                newPatron = patron;
                break;
            }  
        }
        return newPatron;
    }
    //*************************************************
    //below are methods you must complete inside this class
    //*************************************************
    //COMPLETED
    protected void depositCash(double amount)
    {
        
            //this method should take the parameter of "amount" and add it to the this.cash already existing
            this.cash += amount - Bank.getBank().transactionFee;
            // it will do this by creating a transaction object with the tx type of deposit    
        
        
    }
    //COMPLETED
    protected void withdrawCash(double amount) throws InsufficientAssetsException
    {
        //this method should take the parameter of "amount" and add it to the this.cash already existing
        // it will do this by creating a transaction object with the tx type of withdraw
        if(this.cash >= amount + Bank.getBank().transactionFee)
        {
            this.cash -= amount;
        } else 
        {
            throw new InsufficientAssetsException();
        }
        
    }
}