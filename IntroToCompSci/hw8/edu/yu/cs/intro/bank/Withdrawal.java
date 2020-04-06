package edu.yu.cs.intro.bank;

import java.sql.Time;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class Withdrawal extends Transaction {

    protected Withdrawal(double amount, Account target, Patron patron) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron);
        System.out.println("creating the Withdrawel object worked");
    }

   

    @Override
    protected double getAmount() {
        return amount;
    }

    @Override
    protected Account getTarget() {
        return this.target;
    }

    @Override
    protected Patron getPatron() {
        return this.patron;
    }

    /**
     * if the account does not have enough cash to withdraw the amount, throw an InsufficientAssetsException.
     * if it does have enough, decrease the cash by the given amount
     */
    @Override
    protected void execute() throws InsufficientAssetsException {
        try {
            
            if(amount > this.target.cash)
            {
                throw new InsufficientAssetsException();
            } else 
            {
                this.target.cash -= amount;
                this.time = this.getTime();
                this.target.addTransactionToHistory(this);
            }
        } catch (Exception e) {
            
        }
    }
}
