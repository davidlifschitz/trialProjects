package edu.yu.cs.intro.simpleBank;
import edu.yu.cs.intro.simpleBank.Transaction.TRANSACTION_TYPE;
import edu.yu.cs.intro.simpleBank.*;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;







public class Bank 
{
    //creating the static INSTANCE of Bank to be used when getBank is called (PART 1)
    private static Bank INSTANCE;
    
    private Set<Patron> bankPatrons;
    private Map<String,Double> stocksSymbolToPrice;
    private Map<Long,Patron> socialSecurityAndPatron;
    /**
     * transaction history is no longer stored in each patron object. 
     * Instead, the bank maintains a Map of transactions, 
     * mapping each Patron to the List of transactions that the given patron has executed.
     */
    private Map<Patron,List<Transaction>> txHistoryByPatron;
    private List<Transaction> listOfTransaction;
    
    
    protected final double transactionFee;
    private long accountID;
    private long brokerageAccountID;
    
    public Bank(double transactionFee){
        this.transactionFee = transactionFee;
        //creating the static INSTANCE of Bank to be used when getBank is called (PART 2)
        INSTANCE = this;
        //initialize your collections here
        this.txHistoryByPatron = new HashMap<Patron, List<Transaction>>();
        this.bankPatrons = new HashSet<Patron>(); 
        this.stocksSymbolToPrice = new HashMap<String,Double>();
        this.socialSecurityAndPatron = new HashMap<Long,Patron>();
        this.listOfTransaction = new ArrayList<Transaction>();
        this.accountID = 0;
        this.brokerageAccountID = -1;
    }
    public static Bank getBank(){
        return INSTANCE;
    }

    protected Set<Patron> getBankPatrons(){return this.bankPatrons;}
    protected Map<String,Double> getStockToPriceMap() {return this.stocksSymbolToPrice;}
    protected Map<Long,Patron> getPatronBySSN(){return this.socialSecurityAndPatron;}

    /**
     * Creates a new Patron in the bank.
     */
    //COMPLETED
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password)
    {
        Patron newPatron = new Patron(firstName, lastName, socialSecurityNumber, userName, password);
        System.out.println("created a patron.");
        Set<Patron> patronSet = this.bankPatrons;
        //if(patronSet.isEmpty()){
        //    System.out.println("the set is empty");
        //}
        System.out.println("created a set of patrons.");
        patronSet.add(newPatron);
        socialSecurityAndPatron.put(newPatron.getSocialSecurityNumber(), newPatron);
        List<Transaction> transList = new ArrayList<Transaction>(this.listOfTransaction);
        txHistoryByPatron.put(newPatron, transList);
        

        System.out.println("added the newly created patron to the newly created set of Patrons.");
    }
    /**
     * @return a set the stock ticker symbols listed in this bank
     */
    //COMPLETED
    public Set<String> getAllStockTickerSymbols(){
        return this.stocksSymbolToPrice.keySet();
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    //COMPLETED
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
    {
        double balanceBrokerage = 0.0; 
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            Patron newPatron = this.socialSecurityAndPatron.get(socialSecurityNumber);
            if(newPatron.getBrokerageAccount() != null)
            {
                    balanceBrokerage = newPatron.getBrokerageAccount().getAvailableBalance();
            }else{
                    throw new UnauthorizedActionException();
                }
        }else{
            throw new AuthenticationException();
            }
        
        
        return balanceBrokerage;
        
    }
    /**
     * @return all the cash in all savings accounts added up
     */
    //COMPLETED
    public double getTotalSavingsInBank(){
        Set<Patron> newSetOfPatrons = this.bankPatrons;
        double totalSavingsInBank = 0.0;
        for (Patron patron : newSetOfPatrons) {
            totalSavingsInBank += patron.getSavingsAccount().getAvailableBalance();
        }
        
        return totalSavingsInBank;
    }
    /**
     * Lists a new stock with the given symbol at the given price
     * @return false if the stock was previously listed, true if it was added as a result of this call
     */
    //COMPLETED
    protected boolean addNewStockToMarket(String tickerSymbol, double sharePrice){
        //if the stock is already listed, return false
        //otherwise, add the key-value pair to the stocksSymbolToPrice map and return true;
        boolean bools = false;
        if(this.stocksSymbolToPrice.size() == 0)
        {
            this.stocksSymbolToPrice.put(tickerSymbol, sharePrice);
            System.out.println("added stock to the market.");
            bools = true;
        } else 
        {
            if((this.stocksSymbolToPrice.containsKey(tickerSymbol)))
            {
                System.out.println("Stock already exists.");
                bools = false;
            } else
            {
                this.stocksSymbolToPrice.put(tickerSymbol, sharePrice);
                System.out.println("added stock to the market.");
                bools = true;
            }
        }

        return bools;
        
    }
    /**
     * @return the account number of the opened account
     */
    //COMPLETED
    public long openSavingsAccount( long socialSecurityNumber, String userName, String password) {
        for (Patron patron : this.bankPatrons) {
            if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
            {
                if(this.socialSecurityAndPatron.get(socialSecurityNumber).equals(patron))
                {
                accountID += 2;
                Account newSavingsAccount = new Account(accountID);
                patron.addAccount(newSavingsAccount);
                break;
                }
                
            }
            
        }
        return accountID;
    }
    /**
     * @return the account number of the opened account
     */
    //COMPLETED
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) {
        for (Patron patron : this.bankPatrons) {
            if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
            {
                if(this.socialSecurityAndPatron.get(socialSecurityNumber).equals(patron))
                {
                    brokerageAccountID += 2;
                    BrokerageAccount newBrokerageAccount = new BrokerageAccount(brokerageAccountID);
                    patron.addAccount(newBrokerageAccount);
                    break;
                }
            }    
        }
        
        return accountID;
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    //COMPLETED
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        double balanceSavings = 0.0;  
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            Patron newPatron = this.socialSecurityAndPatron.get(socialSecurityNumber);
            {
                if(newPatron.getSavingsAccount() != null)
                {
                    balanceSavings = newPatron.getSavingsAccount().getAvailableBalance();
                } else 
                {
                    throw new UnauthorizedActionException();
                }
            }
        } else 
        {
            throw new AuthenticationException();
        }
        return balanceSavings;
        
    }
    /**
     * Deposit cash into the given savings account
     */
    //COMPLETE
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount)
    {
        try {
            if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
            {
                Transaction deposit = new Transaction(this.socialSecurityAndPatron.get(socialSecurityNumber), Transaction.TRANSACTION_TYPE.DEPOSIT, this.socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount(), amount);
                deposit.execute();
                Patron newPatron = this.socialSecurityAndPatron.get(socialSecurityNumber);
                List<Transaction> newTrans = txHistoryByPatron.get(newPatron);
                newTrans.add(deposit);
                txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
            }
        } catch (Exception e) {
            
        }
        
                 
            
        
    }
    /**
    * withdraw cash from the patron's savings account
    * throw AuthenticationException if the SS#, username, and password don't match a bank patron
    * throw UnauthorizedActionException if the given patron does not have a savings account
    * throw InsufficientAssetsException if that amount of money is not present the savings account
    */
    //COMPLETED
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if(socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount() != null)
            {
                if(socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount().getAvailableBalance() >= amount + transactionFee)
                {
                    Transaction withdrawFromSavings = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.WITHDRAW, socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount(), amount);
                    withdrawFromSavings.execute();
                    List<Transaction> newTrans = txHistoryByPatron.get(this.socialSecurityAndPatron.get(socialSecurityNumber));
                    newTrans.add(withdrawFromSavings);
                    txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
                } else {
                    throw new InsufficientAssetsException();
                } 
            } else 
            {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    //COMPLETED
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if(socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null)
            {
                if(socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getAvailableBalance() > amount + transactionFee)
                {
                    Transaction withdrawFromBrokerage = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.WITHDRAW, socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount(), amount);
                    withdrawFromBrokerage.execute();
                    List<Transaction> newTrans = txHistoryByPatron.get(this.socialSecurityAndPatron.get(socialSecurityNumber));
                    newTrans.add(withdrawFromBrokerage);
                    txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
                } else {throw new InsufficientAssetsException();}
            } else 
            {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    }
    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    //COMPLETED
    public double checkCashInBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        double cashInBrokerage = 0.0;
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if(socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null)
            {
                cashInBrokerage =+ socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getAvailableBalance();
            } else 
            {
                throw new UnauthorizedActionException();
            }
        }else
        {
            throw new AuthenticationException();
        }
        return cashInBrokerage;
    }
    /**
     * @return all the cash in all brokerage accounts added up
     */
    //COMPLETED
    public double getTotalBrokerageCashInBank(){
        double allTheBrokeCash = 0.0;
        try
        {
            Set<Patron> bankPatrons = getBank().getBankPatrons();
            for (Patron patron : bankPatrons) 
            {
                allTheBrokeCash += patron.getBrokerageAccount().getAvailableBalance();
            }
        
        } catch(Exception e)
        {
            
        }
        return allTheBrokeCash;
    } 
    /**
     * @return the stock price for the given stock ticker symbol. Return 0 if there is no such stock.
     */
    //COMPLETED
    public double getStockPrice(String symbol){
        double stockPrice = 0.0;
        if(!(this.stocksSymbolToPrice.get(symbol) == null))
        {
            stockPrice = this.stocksSymbolToPrice.get(symbol);
        }
        return stockPrice;
    } 
    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    //COMPLETED
    public int getNumberOfOutstandingShares(String tickerSymbol){
        int nOfOutShares = 0;
        for (Patron patron : bankPatrons) {
            nOfOutShares += patron.getBrokerageAccount().getNumberOfShares(tickerSymbol);
        }
        return nOfOutShares;
    }
    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    //COMPLETED
    public int getMarketCapitalization(String tickerSymbol)
    {   
        return (int) (getNumberOfOutstandingShares(tickerSymbol) * this.stocksSymbolToPrice.get(tickerSymbol));
    }
    /**
    * transfer cash from the patron's savings account to his brokerage account
    * throws AuthenticationException if the SS#, username, and password don't match a bank patron
    * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
    * throws InsufficientAssetsException if that amount of money is not present in the savings account
    */
    //COMPLETED
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException 
    {
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if ((socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null) && (socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount() != null)) 
            {
                if(socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount().getAvailableBalance() >= amount + (2*transactionFee))
                {
                    Transaction withdraw = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.WITHDRAW, socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount(), amount);
                    Transaction deposit  = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.DEPOSIT, socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount(), amount);
                    withdraw.execute();
                    deposit.execute();
                    List<Transaction> newTrans = txHistoryByPatron.get(this.socialSecurityAndPatron.get(socialSecurityNumber));
                    newTrans.add(deposit);
                    newTrans.add(withdraw);
                    txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
                }    
            } else {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    } 
    /**
    * transfer cash from the patron's savings account to his brokerage account
    * throws AuthenticationException if the SS#, username, and password don't match a bank patron
    * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
    * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
    */
    //COMPLETED
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException
    {
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if ((socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null) && (socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount() != null)) 
            {
                if(socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getAvailableBalance() >= amount + (2*transactionFee))
                {
                    //Transaction withdraw = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.WITHDRAW, socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount(), amount);
                    //Transaction deposit  = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.DEPOSIT, socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount(), amount);
                    //withdraw.execute();
                    //deposit.execute();
                    withdrawCashFromBrokerage(socialSecurityNumber, userName, password, amount);
                    depositCashIntoSavings(socialSecurityNumber, userName, password, amount);
                }    
            } else {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    }
    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    //COMPLETED
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if (socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null) 
            {
                Map<String,Double> stockToPrice = this.stocksSymbolToPrice;
                double sharePrice = stockToPrice.get(tickerSymbol);
                if (socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getAvailableBalance() > shares*sharePrice + transactionFee) 
                {
                    Transaction buyStock = new Transaction(socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.BUYSTOCK, socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount(), shares, tickerSymbol);
                    buyStock.execute();
                    List<Transaction> newTrans = txHistoryByPatron.get(this.socialSecurityAndPatron.get(socialSecurityNumber));
                    newTrans.add(buyStock);
                    txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
                } else {
                    throw new InsufficientAssetsException();
                }    
            } else {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    }
    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patron does not have the given number of shares of the given stock
     */
    //COMPLETED
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if(this.socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null)
            {
                if(this.socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getNumberOfShares(tickerSymbol) > shares)
                {
                    Transaction sellStock = new Transaction(this.socialSecurityAndPatron.get(socialSecurityNumber), TRANSACTION_TYPE.SELLSTOCK, this.socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount(), shares, tickerSymbol);
                    sellStock.execute();
                    List<Transaction> newTrans = txHistoryByPatron.get(this.socialSecurityAndPatron.get(socialSecurityNumber));
                    newTrans.add(sellStock);
                    txHistoryByPatron.put(this.socialSecurityAndPatron.get(socialSecurityNumber), newTrans);
                    System.out.println("added to history!");
                } else 
                {
                    throw new InsufficientAssetsException();
                }

            } else 
            {
                throw new UnauthorizedActionException();
            }
        } else 
        {
            throw new AuthenticationException();
        }
    }
    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    //COMPLETED
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        double netWorth = 0.0;
        if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
        {
            if(socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount() != null && socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount() != null)
            {
                netWorth += this.socialSecurityAndPatron.get(socialSecurityNumber).getBrokerageAccount().getTotalBalance() + this.socialSecurityAndPatron.get(socialSecurityNumber).getSavingsAccount().getAvailableBalance();
            } 
        } else 
        {
            throw new AuthenticationException();
        }
        return netWorth;
    }
    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    //COMPLETED
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException
    {
        Transaction[] transactionHistory = new Transaction[0];
        Map<Patron,List<Transaction>> transMap = this.txHistoryByPatron;
            if(this.socialSecurityAndPatron.containsKey(socialSecurityNumber) && this.socialSecurityAndPatron.get(socialSecurityNumber).getUserName().equals(userName) && this.socialSecurityAndPatron.get(socialSecurityNumber).getPassword().equals(password))
            {   
                ArrayList<Transaction> transList = (ArrayList<Transaction>)(transMap.get(socialSecurityAndPatron.get(socialSecurityNumber)));
                if(transList == null) 
                {
                    return transactionHistory;
                } 
                else
                {
                    transactionHistory = new Transaction[10];
                    for (int i = 0; i < transList.size(); i++) {
                        if(transactionHistory[transactionHistory.length-1] != null)
                            {
                                transactionHistory = new Transaction[transactionHistory.length*2];
                            }
                         Transaction newTrans = transList.get(i);
                         transactionHistory[i] = newTrans;
                         
                    }
                }
                //Quick for() loop to get rid of all the rest of the null elements in the array if it was smaller than size 10.
                Transaction[] transArrayWithNoNulls = new Transaction[transList.size()];
                for(int j = 0; j < transArrayWithNoNulls.length; j++)
                {
                    transArrayWithNoNulls[j] = transactionHistory[j];
                }
                transactionHistory = transArrayWithNoNulls;
            } else 
            {
                throw new AuthenticationException();
            }
        
                
        
        return transactionHistory;
    }


    
    
    public static void main(String[] args) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException 
    {
        String IBM = "IBM";
        String AMAZON = "AMZN";
        String GOOGLE = "GOOG";
        long ssnum = 123456789;
        String userName = "diament";
        String password = "1234";

        Bank bank = new Bank(0);
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
        try {
            double amount = 5000;
            bank.depositCashIntoSavings(ssnum, userName, password, amount);
            //transfer from savings to brokerage
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 2000);
            double balanceSavings = bank.checkBalanceSavings(ssnum, userName, password);
            double brokerageSavings = bank.checkCashInBrokerage(ssnum, userName, password);
            //transfer from brokerage to savings
            bank.transferFromBrokerageToSavings(ssnum, userName, password, 1000);
            double balanceSavings1 = bank.checkBalanceSavings(ssnum, userName, password);
            double brokerageSavings1 = bank.checkCashInBrokerage(ssnum, userName, password);
            //transfer from savings to brokerage
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 4000);
            double balanceSavings2 = bank.checkBalanceSavings(ssnum, userName, password);
            double brokerageSavings2 = bank.checkCashInBrokerage(ssnum, userName, password);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

