package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.*;
import org.junit.Assert;
import org.junit.Test;

public class DemoTest 
{
    private static final String IBM = "IBM";
    private static final String AMAZON = "AMZN";
    private static final String GOOGLE = "GOOG";
    private final long ssnum = 123456789;
    private final String userName = "diament";
    private final String password = "1234";

    private Bank getBank() 
    {
        return new Bank(0);
    }
    Bank bank = new Bank(0);
    //test purchaseStock
    @Test
    public void testPurchaseStock() {
        try {
            
            //add stocks to market
            bank.addNewStockToMarket(IBM, 100d);
            bank.addNewStockToMarket(AMAZON, 1500d);
            bank.addNewStockToMarket(GOOGLE, 2000d);
            //create accounts and deposit money
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
            bank.depositCashIntoSavings(ssnum, userName, password, 75000d);
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 50000d);
            //buy stocks
            //buy 100 shares of IBM
            bank.purchaseStock(ssnum, userName, password, IBM, 100);
            //buy 10 shares of Google
            bank.purchaseStock(ssnum, userName, password, GOOGLE, 10);
            //cash left in brokerage: 20000
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to purchase stocks: ");
        }
        try {
            Bank bank = new Bank(0);
            //check cash in brokerage - should be 20000
            Assert.assertEquals("incorrect amount of cash in brokerage account", 20000d, bank.checkCashInBrokerage(ssnum, userName, password), 100);
            //check total value of brokerage - should be 50000
            Assert.assertEquals("incorrect amount of cash in brokerage account", 50000d, bank.checkTotalBalanceBrokerage(ssnum, userName, password), 100);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("incorrect balance in brokerage account after purchase of stocks: ");
        }
    }
    
    
}