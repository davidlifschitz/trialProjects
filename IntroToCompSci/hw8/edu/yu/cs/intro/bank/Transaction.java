package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;


public abstract class Transaction {
    protected final double amount;
    protected final Account target;
    protected final Patron patron;
    protected long time;
    
    /**
     * Creates a transaction.
     * Must check the following:
     * 1) that amount is positive (if not, throw IllegalArgumentException)
     * 2) that the Account actually exists in the bank (if not, throw NoSuchAccountException)
     * 3) that the Patron actually exists in the bank (if not, throw UnauthorizedActionException)
     * 4) that the Patron is an owner of the given account (if not, throw UnauthorizedActionException)
     */
    protected Transaction(double amount, Account target, Patron patron) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        this.amount = amount;
        this.target = target;
        int validityCounterForAccount = 0;
        int validityCounterForPatron = 0;
        for(int i = 0; i < Bank.getBank().getAccountArray().length; i ++){
            if(this.target.equals(Bank.getBank().getAccountArray()[i])){
                validityCounterForAccount = 1;
            } else {
                continue;
            }
        }
        
        if(validityCounterForAccount == 0)
        {
            throw new NoSuchAccountException();
        }
        this.patron = patron;
        
        for(int i = 0; i < Bank.getBank().getAccountArray().length; i ++){
            if(this.patron.equals(Bank.getBank().getPatronArray()[i])){
                validityCounterForPatron = 1;
            } else {
                continue;
            }
        }
        
        if(this.target instanceof BrokerageAccount)
            {
                if(this.patron.getBrokerageAccount() == this.target)
                {

                } else 
                {
                    validityCounterForPatron = 0;
                }
            }
        if(this.target instanceof SavingsAccount)
        {
            if(this.patron.getSavingsAccount() == this.target)
            {

            } else 
            {
                validityCounterForPatron = 0;
            }
        } 
        
        if(validityCounterForPatron == 0)
        {
            throw new UnauthorizedActionException();
        }
        this.time = getTime();
        
    }

    /**time the tx took place, from System.currentTimeMillis()*/
    protected long getTime(){return System.currentTimeMillis();}
    protected abstract double getAmount();
    protected abstract Account getTarget();
    protected abstract Patron getPatron();
    /**executes the transaction, and sets the transaction time*/
    protected abstract void execute() throws InsufficientAssetsException;
}