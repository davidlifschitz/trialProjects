package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.*;
import java.util.Comparator;
/**
 * class for modeling deposits, withdrawals, buying & selling stock
 */


public class Transaction 
{
    private final double amount;
    private final Account account;
    private final Patron patron;
    private final TRANSACTION_TYPE txType;
    private long time;
    private String stockSymbol;
    protected enum TRANSACTION_TYPE{DEPOSIT,WITHDRAW,BUYSTOCK,SELLSTOCK}
    /*
     *   protected void setAmount(double amount)
     *   {
     *       this.amount = amount;
     *   }
     */
    /**
     * @param patron the Patron who wishes to execute the transaction
     * @param type the type of transaction the patron wishes to execute
     * @param account the account on which the Patron wishes to execute the transaction
     * @throws UnauthorizedActionException if the patron does not own the account, OR if given transaction type
     * can not be executed on the given type of account (e.g. you can't sell stocks from a savings account)
     * @throws IllegalArgumentException if:
     * 1) if the TRANSACTION_TYPE is buying or selling stock and the amount is not a whole number 
     * 2) if the amount is not >0
     */
    //STILL NEED TO DO
    protected Transaction(Patron patron, TRANSACTION_TYPE type, Account account, double amount) throws UnauthorizedActionException{
        this.patron = patron;
        this.txType = type;
        Account inputAccount = account;
        double amountOfTransaction = amount;
        if (!(inputAccount instanceof BrokerageAccount)) 
        {
            if(patron.getSavingsAccount().getAccountNumber() == inputAccount.getAccountNumber())
            {
                if(amount > 0)
                {
                    this.account = inputAccount; 
                    this.amount = amountOfTransaction;
                } else 
                {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            if(patron.getBrokerageAccount().getAccountNumber() == inputAccount.getAccountNumber())
            {
                if(amount > 0)
                {
                    this.account = inputAccount; 
                    this.amount = amountOfTransaction;
                } else 
                {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new UnauthorizedActionException();
            }
        }
            
    }
    protected Transaction(Patron patron, TRANSACTION_TYPE type, Account account, double amount, String stock) throws UnauthorizedActionException
    {
        this.patron = patron;
        this.txType = type;
        Account inputAccount = account;
        int amountOfTransaction = (int)(amount);
		if(patron.getBrokerageAccount().getAccountNumber() == account.getAccountNumber())
            {
                this.account = inputAccount;
                if((int)amountOfTransaction == amountOfTransaction)
                {
                    this.amount = amountOfTransaction;
                    this.stockSymbol = stock;
                } 
                else 
                {
                    throw new IllegalArgumentException();
                }                                 
            } else {
                throw new UnauthorizedActionException();
            } 
        
    }
    
    /**time the tx took place, from System.currentTimeMillis()*/
    public long getTime(){
        return this.time;
    }
    protected void setStockSymbol(String symbol){this.stockSymbol = symbol;}
    public double getAmount() {return this.amount;}
    public Account getAccount(){return this.account;}
    public Patron getPatron(){return this.patron;}
    /**
     * Most do the following:
     * 1) based on the value of txType, check that all the instance variables that are required are set to valid values
     *      a) buying or selling stock requires that stockSymbol be set, and that amount be a whole number
     *      b) deposit or withdrawal requires amount be set
     * 2) executes the transaction, and sets the transaction time
     * @throws InsufficientAssetsException if there is not enough cash in the account to execute the transaction,
     *    OR if the Patron does not own as many shares in the given stock as he is attempting to sell
     */
    //STARTED WORKING ON
    protected void execute() throws InsufficientAssetsException
    {
        switch (this.txType){
            case WITHDRAW:
                if(this.account.getAvailableBalance() >= (this.amount + Bank.getBank().transactionFee)){
                    this.account.withdrawCash(this.amount + Bank.getBank().transactionFee);
                    this.time = System.currentTimeMillis();
                    //do the withdrawing
                    //set the tx time
                    break; 
                    
                } else 
                {
                    throw new InsufficientAssetsException();       
                }
            case DEPOSIT:
                //do the depositing
                //set the tx time
                    this.account.depositCash(this.amount);
                    //setAmount(this.account.getAvailableBalance());
                    this.time = System.currentTimeMillis();
                break;
            case BUYSTOCK:
                //buy a stock
                //set the tx time
                if(this.account.getAvailableBalance() > (this.amount + Bank.getBank().transactionFee))
                {
                    this.setStockSymbol(this.stockSymbol);
                    BrokerageAccount newBroke = (BrokerageAccount) (this.account);
                    newBroke.buyShares(this.stockSymbol, (int)(this.amount));
                    this.time = System.currentTimeMillis();
                    break;
                } else 
                {
                    throw new InsufficientAssetsException();
                }
                
            case SELLSTOCK:
                //sell a stock
                    //get the amount of shares per stock from brokerageaccount
                    //if there are enough shares to sell
                        //reduce the number of shares
                        //get the value for how much each is worth * the number of shares sold
                            //add that value to brokerageaccount.cash
                //set the tx time
                //Map<String,Double> stockToPrice = Bank.getBank().getStockToPriceMap();
                this.setStockSymbol(this.stockSymbol);
                BrokerageAccount newBroke = (BrokerageAccount) (this.account);
                newBroke.sellShares(this.stockSymbol, (int) (this.amount));
                this.time = System.currentTimeMillis();
                break;
            default:
                break;
        }





    }
}

