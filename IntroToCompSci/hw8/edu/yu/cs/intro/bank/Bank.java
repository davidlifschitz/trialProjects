package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.*;

public class Bank 
{
    public static Bank INSTANCE = new Bank();
    public Bank(){}
    public static Bank getBank() {return INSTANCE;}
    
    private Patron[] bankPatrons = new Patron[10];
    private Account[] bankAccounts = new Account[10];
    private Stock[] stocksOnMarket = new Stock[10];
    private long accountID = 0;
    private long brokeAccountID = -1;
   
    protected Patron[] getPatronArray(){return this.bankPatrons;}
    protected Account[] getAccountArray(){return this.bankAccounts;}
    protected Stock[] getStockArray(){return this.stocksOnMarket;}
    
    public static class Stock 
    {
        private final String tickerSymbol;
        private final double sharePrice;
        /**
         * Note that because this constructor is private, the Bank class is the only class that can create instances of Stock.
         * All other classes may refer to, i.e. have variables pointing to, Stock objects, but only Bank can create new Stock Objects.
         */
        private Stock(String tickerSymbol, double sharePrice)
        {
            this.tickerSymbol = tickerSymbol;
            this.sharePrice = sharePrice;
        }
        public double getSharePrice(){
            return this.sharePrice;
        }
        public String getTickerSymbol(){
            return this.tickerSymbol;
        
        }
    }
    /**
     * lists a new stock with the given symbol at the given price
     */
    //  
    protected void addNewStockToMarket(String tickerSymbol, double sharePrice)
    {
        //Given: Bank with Stock array.
        //What this code does:
        //Adds a new stock to the stocksOnMarket array with a tickersymbol and a sharePrice (each inputted).
            //if there is an empty spot in the array to add the stock, add the stock.
            if(this.stocksOnMarket[this.stocksOnMarket.length-1] == null)
            {
                for (int i = 0; i < stocksOnMarket.length; i++) {
                    if(stocksOnMarket[i] == null)
                    {
                        this.stocksOnMarket[i] = new Stock(tickerSymbol, sharePrice);
                        break;
                    }    
                }
                
            } else {
                //in the situation that the array is full
                Stock[] newArray = new Stock[this.stocksOnMarket.length +1];
                for (int j = 0; j < getBank().getStockArray().length -1 ; j++) 
                {
                       newArray[j] = stocksOnMarket[j]; 
                }
                this.stocksOnMarket = newArray;
            }
        
    }
    /**
     * return the stock object for the given stock ticker symbol
     */
    // 
    public Stock getStockBySymbol(String symbol) {
        //Given: stock symbol.
        //What this code does: 
        //Searches through array of stocks and finds the stock that exists with the symbol given. returns the stock with the symbol and the sharePrice
            int stockIndex = 0;
            for(int i = 0; i < this.stocksOnMarket.length; i ++){
            if(this.stocksOnMarket[i].tickerSymbol.equals(symbol)){
                stockIndex = i;
                break;
            }
        }
        return this.stocksOnMarket[stockIndex];
    }
    /**
     * @return an array of all the stock ticker symbols of this bank
     */
    // 
    public String[] getListOfAllStockTickerSymbols()
    {
        String[] arrayOfTickerSymbols = new String[this.stocksOnMarket.length];
        for(int i = 0; i < arrayOfTickerSymbols.length; i++)
        {
            if(this.stocksOnMarket[i] == null)
            {
                break;
            }   else 
                {
                    arrayOfTickerSymbols[i] = this.stocksOnMarket[i].tickerSymbol;
                }
        }
        return arrayOfTickerSymbols;
    }
    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    //
    public int getNumberOfOutstandingShares(String tickerSymbol){
        int totalSharesOfAStock = 0;
        //Account[] arrayOfAccounts = getBank().getAccountArray();
        for(Patron patron: bankPatrons)
        {
            if(patron == null)
            {
                continue;
            }
            Stock[] arrayOfStocks = patron.getBrokerageAccount().getStockArray();
            for (int i = 0; i < arrayOfStocks.length; i++) {
                int[] arrayOfPatronsShares = patron.getBrokerageAccount().getSharesArray();
                if(arrayOfStocks[i].tickerSymbol.equals(tickerSymbol))
                {
                    totalSharesOfAStock += arrayOfPatronsShares[i];
                    break;
                } else{
                    continue;
                }
            }
            
        }
        return totalSharesOfAStock;
    }
    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    //
    public int getMarketCapitalization(String tickerSymbol){
        double totalCapital = 0;
        int amountOfShares = getNumberOfOutstandingShares(tickerSymbol);
        double sharePrice = 0.0;
        for(int i = 0; i < this.stocksOnMarket.length; i ++){
            if(this.stocksOnMarket[i] == null)
            {
                continue;
            }
            if(this.stocksOnMarket[i].tickerSymbol.equals(tickerSymbol)){
                sharePrice += this.stocksOnMarket[i].sharePrice;
                break;
            }
        }
        totalCapital =  amountOfShares * sharePrice;
        return (int) (totalCapital);
    }
    /**
     * @return all the cash in all savings accounts added up
     */
    // 
    public double getTotalSavingsInBank()
    {
        {
            double totalMoneyInSavings = 0.0;
            for(int i = 0; i < this.bankAccounts.length; i ++)
            {   
                Account newAccount = this.bankAccounts[i];
                if(newAccount instanceof SavingsAccount){
                    totalMoneyInSavings += newAccount.cash;
                }
            }
            return totalMoneyInSavings;
        }
        /*
        double totalMoneyInSavings = 0.0;
        Patron[] arrayOfPatrons = getBank().getPatronArray();
        for(int i = 0; i < arrayOfPatrons.length; i ++){
            if(arrayOfPatrons[i].getSavingsAccount().cash == 0){
                continue;
            } else{
                totalMoneyInSavings += arrayOfPatrons[i].getSavingsAccount().cash;
            }
        }
        return totalMoneyInSavings;
        */
    }
    
    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    //
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getSavingsAccount() == null)
            {
                throw new UnauthorizedActionException();
            }else 
            {
                if(amount > newPatron.getSavingsAccount().cash)
                {
                    throw new InsufficientAssetsException();
                } else 
                {
                    try {
                        Withdrawal withdrawObj = new Withdrawal(amount, newPatron.getSavingsAccount(), newPatron);  
                        withdrawObj.execute(); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                 
            } 
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    //
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        try 
        {
            for (Patron patron : bankPatrons) {
                if(patron.getSocialSecurityNumber() == socialSecurityNumber && patron.getUserName().equals(userName) && patron.getPassword().equals(password))
                {
                    if(patron.getBrokerageAccount() == null)
                    {
                        throw new UnauthorizedActionException();
                    } else 
                    {
                        if(amount > patron.getBrokerageAccount().cash){
                            throw new InsufficientAssetsException();
                        } else 
                        {
                            Withdrawal withdraw = new Withdrawal(amount, patron.getBrokerageAccount(), patron);
                            withdraw.execute();
                        }
                    }
                } else {
                    throw new AuthenticationException();
                }
            }
            


        } catch (Exception e) {
            
        }
    }
    
    /**
     * @return all the cash in all brokerage accounts added up
     */
    // 
    
    public double getTotalBrokerageCashInBank()
    {
        
        double totalMoneyInBrokerage = 0.0;
        for(int i = 0; i < this.bankAccounts.length; i ++)
        {   
            if(this.bankAccounts[i] instanceof BrokerageAccount){
                totalMoneyInBrokerage += this.bankAccounts[i].cash;
            }
        }
        /*
        double totalMoneyInBrokerage = 0.0;
        Patron[] arrayOfPatrons = getBank().getPatronArray();
        for(int i = 0; i < arrayOfPatrons.length; i ++){
            if(arrayOfPatrons[i].getBrokerageAccount().cash == 0){
                continue;
            } else{
                totalMoneyInBrokerage += arrayOfPatrons[i].getBrokerageAccount().cash;
            }
        }
        */
        return totalMoneyInBrokerage;
    }
    /**
     * Creates a new Patron in the bank.
     * Throws an UnauthorizedActionException if a Patron already exists with that social security number.
     */
    // 
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password) throws UnauthorizedActionException 
    {
        Patron newPatron = new Patron(firstName, lastName, socialSecurityNumber, userName, password);
        for (int i= 0; i < this.bankPatrons.length - 1; i++ ) 
        {
            if (this.bankPatrons[this.bankPatrons.length-1] != null) 
            {
                Patron[] newArray = new Patron[this.bankPatrons.length+1];
                    for(int j = 0; j < this.bankPatrons.length; j ++ )
                    {
                        newArray[j] = this.bankPatrons[j];
                    }
                    this.bankPatrons = newArray;
            } else 
            {
                if (this.bankPatrons[i] != null) 
                {
                    if (newPatron.getSocialSecurityNumber() == this.bankPatrons[i].getSocialSecurityNumber()) 
                    {
                        throw new UnauthorizedActionException();
                    } else 
                    {
                        continue;
                    }
                
                } else 
                {
                    this.bankPatrons[i] = newPatron;
                    break;
                }
            }     
        }
            
       

        
    }
    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a savings account
     * @return the account number of the opened account
     */
    //
     public long openSavingsAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
    {
        Account[] accountArray = getAccountArray();
        Patron newPatron = patronValidation(socialSecurityNumber,userName,password);
        if (newPatron != null)
        {
            accountID += 2;
            SavingsAccount newSavingsAccount = new SavingsAccount(accountID, newPatron);
            for(int i = 0; i < accountArray.length-1; i++)
            {
                if(accountArray[i] != null)
                {
                    if(accountArray[i].getPatron().equals(newPatron) && accountArray[i] instanceof SavingsAccount)
                    {
                        throw new UnauthorizedActionException();
                    } else 
                    {
                        continue;
                    }
                } else 
                {
                    addToAccountArray(newSavingsAccount);
                    newPatron.setSavingsAccount(newSavingsAccount);
                    break;
                }
                
            }
        } else {
            throw new AuthenticationException();
        }
        return accountID;
    }
    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a Brokerage account
     * @return the account number of the opened account
     */
    //
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
    {   
        
        Patron newPatron = patronValidation(socialSecurityNumber,userName,password);
        if (newPatron != null)
        {
            brokeAccountID += 2;
            BrokerageAccount newBrokerageAccount = new BrokerageAccount(brokeAccountID, newPatron);
                if(newPatron.getBrokerageAccount() != null)
                {
                        throw new UnauthorizedActionException();
                } else 
                {
                    addToAccountArray(newBrokerageAccount);
                    newPatron.setBrokerageAccount(newBrokerageAccount);
                }
        } else {
            throw new AuthenticationException();
        }
        return brokeAccountID;
        
    }
    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    //
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException
    {
        
        double netWorth = 0.0;
        Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            

                try {
                    netWorth += newPatron.getNetWorth();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            
        return netWorth;
    }
    
    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    //
    public double checkAvailableBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
   {  
    double totalCash = 0.0;

        Patron newPatron = new Patron("a","a",1,"a","a");
            
        try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getBrokerageAccount() == null)
            {
                throw new UnauthorizedActionException();
            } else
            {
                totalCash += newPatron.getBrokerageAccount().cash;
            }
        return totalCash;
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    //
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
    {
        int totalSharesPerStock = 0;
        double totalMoneyPerStock = 0.0;
        double totalCash = 0.0;
        double totalMoneyFromStocks = 0.0;
        double sumOfCashandStocks = 0.0;
        
        Patron newPatron = new Patron("a","a",1,"a","a");
            
        try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getBrokerageAccount() == null)
            {
                throw new UnauthorizedActionException();
            } else
            {
                totalCash = newPatron.getBrokerageAccount().cash;
                Stock[] stockArray = getStockArray();
                Stock[] patronsStocks = newPatron.getBrokerageAccount().getStockArray();
                for(int i = 0; i < stockArray.length; i ++)
                {
                    for (Stock stock : stockArray) 
                    {
                        if(stock == null)
                        {
                            continue;
                        }
                        if(patronsStocks[i] == null)
                        {
                            continue;
                        }
                        if(patronsStocks[i].equals(stock))
                        {
                            totalMoneyPerStock = stock.sharePrice;
                            totalSharesPerStock = newPatron.getBrokerageAccount().getNumberOfShares(stock);
                            double multSharesandStocks = totalSharesPerStock * totalMoneyPerStock;
                            totalMoneyFromStocks += multSharesandStocks;
                            continue;
                            
                        } else 
                        {
                            continue;
                        }
                    }
                    
                    
                }
                sumOfCashandStocks = totalCash + totalMoneyFromStocks;    
            }
        return sumOfCashandStocks;
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    //
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException
    {
        double totalCash = 0.0;

        Patron newPatron = new Patron("a","a",1,"a","a");
            
        try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getSavingsAccount() == null)
            {
                throw new UnauthorizedActionException();
            } else
            {
                totalCash += newPatron.getSavingsAccount().cash;
            }
        return totalCash;
    }
    /**
     * Deposit cash into the given savings account
     * @throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * @throws UnauthorizedActionException if the given patron does not have a savings account
     */
    //
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException
    {   Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getSavingsAccount() == null)
            {
                throw new UnauthorizedActionException();
            }else 
            {
                try {
                    Deposit depositObj = new Deposit(amount, newPatron.getSavingsAccount(), newPatron);  
                    depositObj.execute(); 
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            } 
        
        }
        /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in the savings account
     */
    //
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException 
        {
            Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
                    throw new AuthenticationException();
            }
            
            if(newPatron.getSavingsAccount() == null || newPatron.getBrokerageAccount() == null)
            {
                throw new UnauthorizedActionException();
            }else 
            {
                if(amount > newPatron.getSavingsAccount().cash)
                {
                    throw new InsufficientAssetsException();
                } else 
                {
                    try {
                        Transfer transferObj = new Transfer(amount, newPatron.getSavingsAccount(), newPatron.getBrokerageAccount(), newPatron);  
                        transferObj.execute(); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                 
            } 
            
            
        }
    /**
     * transfer cash from the patron's brokerage account to his savings account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
     */
    //
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException
    {
        Patron newPatron = new Patron("a","a",1,"a","a");
        try {
            newPatron = patronValidation(socialSecurityNumber, userName, password);    
            } catch (Exception e) {
            throw new AuthenticationException();
            }            
        if(newPatron.getSavingsAccount() == null || newPatron.getBrokerageAccount() == null)
        {
            throw new UnauthorizedActionException();
        }else 
        {
            if(amount < newPatron.getBrokerageAccount().cash)
            {
                try 
                {
                    Transfer transfer = new Transfer(amount, newPatron.getBrokerageAccount(), newPatron.getSavingsAccount(), newPatron);
                    transfer.execute(); 
                } catch (Exception e) 
                {
                    e.printStackTrace();
                }
            } else 
            {
                throw new InsufficientAssetsException();
            }
                             
        } 
    }
    
    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    //
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        

            Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
                } catch (Exception e) {
                throw new AuthenticationException();
                }            
            if(newPatron.getSavingsAccount() == null || newPatron.getBrokerageAccount() == null)
            {
                throw new UnauthorizedActionException();
            }else 
            {
                Stock[] stockArray = stocksOnMarket;
                for (int i = 0; i < stockArray.length; i ++)
                {
                    if(stockArray[i].tickerSymbol.equals(tickerSymbol))
                    {
                        if ((shares * stockArray[i].sharePrice < newPatron.getBrokerageAccount().cash)) 
                    {
                        try 
                        {
                            
                            double amountOfShares = (double)(shares);
                            BrokerageAccount bAccount = newPatron.getBrokerageAccount();
                            
                            StockPurchase sPurchase = new StockPurchase(amountOfShares, bAccount, newPatron, stockArray[i]);
                            sPurchase.execute();
                            break;
                        } catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    } else 
                    {
                        throw new InsufficientAssetsException();
                    }
                    } else 
                    {
                        continue;
                    }
                    
                }
                
                                 
            } 
        
        
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patrong does not have the given number of shares of the given stock
     */
    //
     public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException
    {
        Patron newPatron = new Patron("a","a",1,"a","a");
            try {
                newPatron = patronValidation(socialSecurityNumber, userName, password);    
                } catch (Exception e) {
                    throw new AuthenticationException();
                }            
                if(newPatron.getSavingsAccount() == null || newPatron.getBrokerageAccount() == null)
                {
                    throw new UnauthorizedActionException();
                }else 
                {
                    if(shares < newPatron.getBrokerageAccount().getNumberOfShares(getStockBySymbol(tickerSymbol)));
                    {
                        try 
                        {
                            StockSale newSale = new StockSale(shares, newPatron.getBrokerageAccount(), newPatron, getStockBySymbol(tickerSymbol));
                            newSale.execute();
                        } catch (Exception e) 
                        {
                            e.printStackTrace();
                            throw new InsufficientAssetsException();
                        }
                    }
                                             
                }
    
}

    

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    //
     public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException
    {
        
        Transaction[] arrayOfTransactions = new Transaction[5];
        for(int i = 0; i < arrayOfTransactions.length; i++)
        {
            if(arrayOfTransactions[arrayOfTransactions.length-1] == null)
            {
                if(bankAccounts[i] == null)
                {
                    arrayOfTransactions = new Transaction[1];
                    continue;
                } else 
                {
                    Transaction[] bankAccountTransactions = bankAccounts[i].getTransactionHistory();
                    for(int j = 0; j < bankAccountTransactions.length; j++)
                    {
                        arrayOfTransactions[j] = bankAccountTransactions[j];
                    }
                    break;
                }

            } else 
            {
                Transaction[] newArray = new Transaction[arrayOfTransactions.length + 1];
                for (int j = 0; j < newArray.length -1 ; j++) 
                {
                       newArray[j] = arrayOfTransactions[j]; 
                }
                arrayOfTransactions = newArray;
            }
        }
        
        return arrayOfTransactions;
    }


    protected Account[] addToAccountArray(Account account)
    {
        Account[] arrayOfAccounts = getAccountArray();
        for(int i = 0; i< arrayOfAccounts.length-1; i++)
        {
            if(arrayOfAccounts[i] == null)
            {
                arrayOfAccounts[i] = account;
                break;
            }
            }
        return arrayOfAccounts;
        }


    protected Patron patronValidation(long ssn, String userName, String password) throws AuthenticationException
    {
        Patron[] patronArray = getPatronArray();
        Patron newPatron = null;
        for(int i = 0; i < patronArray.length-1;i++)
        {
            if(patronArray[i].getSocialSecurityNumber() == ssn)
            {
                if(patronArray[i].getUserName().equals(userName))
                {
                    if(patronArray[i].getPassword().equals(password))
                    {
                        newPatron = patronArray[i];
                        break;
                    } else
                    {
                        continue;
                    }

                } else
                    {
                        continue;
                    }
            } else
                {
                    continue;
                }
        }
        if(newPatron == null)
        {
            throw new AuthenticationException();
        }
        return newPatron;
    }


    public static void main(String[] args)
    {
    String IBM = "IBM";
    String AMAZON = "AMZN";
    String GOOGLE = "GOOG";
    long ssnum = 123456789;
    String userName = "diament";
    String password = "1234";

    Bank bank = getBank();
        try {
            //bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            //bank.openBrokerageAccount(ssnum, userName, password);
            //bank.openSavingsAccount(ssnum, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
           
        try {
            //add stocks to market
            //bank.addNewStockToMarket(IBM, 100);
            //bank.addNewStockToMarket(AMAZON, 1500);
            //bank.addNewStockToMarket(GOOGLE, 2000);
            //create accounts and deposit money
            //bank.depositCashIntoSavings(ssnum, userName, password, 75000);
            //bank.transferFromSavingsToBrokerage(ssnum, userName, password, 50000);
            //buy stocks
            //buy 100 shares of IBM
            //bank.purchaseStock(ssnum, userName, password, IBM, 100);
            //buy 10 shares of Google
            //bank.purchaseStock(ssnum, userName, password, GOOGLE, 10);
            //cash left in brokerage: 20000
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            double shares = bank.getMarketCapitalization(AMAZON);
            System.out.println(shares);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    

}