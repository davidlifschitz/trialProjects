package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class StockSale extends StockTransaction {
    /**
     * check that there is enough of the given stock (as indicated by amount) in the given account to sell.
     * If not, throw InsufficientAssetsException
     */
    public StockSale(double amount, BrokerageAccount target, Patron patron, Bank.Stock stock) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron, stock);
    }

    
    @Override
    protected double getAmount() {
        return 0;
    }

    @Override
    protected Account getTarget() {
        return null;
    }

    @Override
    protected Patron getPatron() {
        return null;
    }

    /**
     * Check that there is enough of the given stock (as indicated by amount) in the given account to sell. If not, throw InsufficientAssetsException.
     * Buy the stock, i.e. use the methods on the patron's brokerage account to sell the stock.
     * Set the time of transaction execution.
     */
    @Override
    protected void execute() throws InsufficientAssetsException {
        this.time = getTime();
        for(Patron patron: Bank.getBank().getPatronArray())
        {
            if(this.patron.equals(patron))
            {
                if(this.amount < this.patron.getBrokerageAccount().getNumberOfShares(stock))
                {
                    patron.getBrokerageAccount().sellShares(stock, (int)(amount));
                    this.target.addTransactionToHistory(this);
                    
                    
                }
                else 
                {
                    throw new InsufficientAssetsException();
                }
            }
            
        }
        
    }
}
