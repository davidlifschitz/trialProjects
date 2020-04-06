package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.*;
import edu.yu.cs.intro.bank.Bank.Stock;

public class BrokerageAccount extends Account {
    private double transactionFee = 1.0;
    /**an array of sticks owned*/
    private Bank.Stock[] stocksOwned = new Bank.Stock[10];
    /**the number of shares of each stock owned by this account*/
    private int[] numberOfShares = new int[10];

    protected Bank.Stock[] getStockArray(){
        return this.stocksOwned;
    }
    protected int[] getSharesArray(){
        return this.numberOfShares;
    }
    

    protected BrokerageAccount(long accountNumber, Patron patron) 
            {
                super(accountNumber,patron);
            }


    protected int getNumberOfShares(Bank.Stock stock){
        int numberOS = 0;
        for (int i = 0; i < stocksOwned.length; i++ ) {
            if(stocksOwned[i] == null)
            {
                continue;
            } else if(stocksOwned[i].getTickerSymbol().equals(stock.getTickerSymbol()))
            {
                numberOS += numberOfShares[i];
                break;
                
            }
        }
        return numberOS;
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(Bank.Stock stock, int shares) throws InsufficientAssetsException 
    {   try
        {   
        for (int i = 0; i < numberOfShares.length; i++) {
            Stock[] stockArray = Bank.getBank().getStockArray();
            Stock newStock = stockArray[i];
            if(newStock.equals(stock))
            {

            } else 
            {
                continue;
            }
            if(this.stocksOwned[i] == null)
            {
                double allTheCash = (shares * newStock.getSharePrice()) + transactionFee;
                if( allTheCash > this.cash)
                {
                    throw new InsufficientAssetsException();
                } else 
                {
                    this.cash -= (shares * stockArray[i].getSharePrice()) + transactionFee; 
                    this.stocksOwned[i] = stock;
                    this.numberOfShares[i] = shares;
                    break;
                }
                
            } else 
            {
                continue;
            }
        }
        //StockPurchase purchasingStock = new StockPurchase(shares, this, this.getPatron(), stock);
        //purchasingStock.execute();    
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(Bank.Stock stock, int shares) throws InsufficientAssetsException
    {   
       try
       {
        Bank.Stock[] stockMarket = getStockArray();
        int[] sharesArray = getSharesArray();
        for (int i = 0; i < stockMarket.length; i ++) {
            if(stockMarket[i].equals(stock))
            {
                if(sharesArray[i] < shares)
                {
                    throw new InsufficientAssetsException();
                } else 
                {
                    sharesArray[i] -= shares;
                    this.cash += shares * stockMarket[i].getSharePrice(); 
                             
                }
            }
        }
       } catch (Exception e)
       {

       }

        
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    @Override
    protected double getTotalBalance(){
        int[] shareArray = getSharesArray();
        Bank.Stock[] stockArray = getStockArray();
        int numberOfShares = 0;
        double sharePrice = 0;
        double allTheMoneyFromStocks = 0.0;
        for(int i = 0; i < stockArray.length; i++)
        {
            if(stockArray[i] == null) 
            {
                continue;
            }
            numberOfShares = shareArray[i];
            sharePrice = stockArray[i].getSharePrice();
            allTheMoneyFromStocks += numberOfShares * sharePrice;
        }
        
        return this.cash + allTheMoneyFromStocks;
    }

    /**
     * this method must return total cash
     * @return
     */
    @Override
    protected double getAvailableBalance() {
        return this.cash;
    }
}