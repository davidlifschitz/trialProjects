package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.InsufficientAssetsException;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;

public class BrokerageAccount extends Account {
    private Map<String,Integer> stocksToNumberOfShares = new HashMap<>();
    protected BrokerageAccount(long accountNumber) {
        super(accountNumber);
    }
    
    
    //COMPLETED
    protected int getNumberOfShares(String stock){
        Map<String,Integer> mapOfShares = stocksToNumberOfShares;
        int numberOfShares = mapOfShares.get(stock);
        return numberOfShares;
    }
    
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    //STILL NEED TO DO
    protected void buyShares(String stock, int shares) throws InsufficientAssetsException 
    {
        //first get the total price for how much this amount of shares of this stock will cost
        Map<String,Double> stockMarket = Bank.getBank().getStockToPriceMap();
        double priceOfSharesOfOneStock = stockMarket.get(stock) * shares;
        //if this amount is more than how much cash is in the account
            //throw an IAException
        //if enough cash
            //reduce that amount (and take off the transaction fee)
            //put that stock and #ofshares in the map in this account.
        //double getAvailableBalance = this.getAvailableBalance();
        if(priceOfSharesOfOneStock < this.getAvailableBalance())
        {
            this.stocksToNumberOfShares.put(stock, shares);
            this.withdrawCash(priceOfSharesOfOneStock);
        } else 
        {
            throw new InsufficientAssetsException();
        }
        
        
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    //COMPLETED
    protected void sellShares(String stock, int shares) throws InsufficientAssetsException
    {
        //first get the total price for how much this amount of shares of this stock will cost
        Map<String,Double> stockMarket = Bank.getBank().getStockToPriceMap();
        double priceOfSharesOfOneStock = stockMarket.get(stock) * shares;
        
        //if this amount is more than how much cash is in the account
            //throw an IAException
        //if enough shares to sell
            //add that amount (and take off the transaction fee)
            //reduce that stock and #ofshares in the map in this account.
        if(stocksToNumberOfShares.get(stock) > shares)
        {
            this.depositCash(priceOfSharesOfOneStock);
            int sharesOfStock = this.stocksToNumberOfShares.get(stock);
            sharesOfStock -= shares;
            this.stocksToNumberOfShares.put(stock, sharesOfStock);
        } else 
        {
            throw new InsufficientAssetsException();
        }
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    //COMPLETED
    protected double getTotalBalance()
    {
        double totalBalance = 0.0;
        double priceOfSharesOfStocks = 0.0;
        for (String stock : Bank.getBank().getStockToPriceMap().keySet()) {
            Map<String,Double> stockMarket = Bank.getBank().getStockToPriceMap();
            priceOfSharesOfStocks += stockMarket.get(stock) * stocksToNumberOfShares.get(stock);

        }
        totalBalance += super.getAvailableBalance() + priceOfSharesOfStocks;
        return totalBalance;
    }
}