package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.*;
import edu.yu.cs.intro.bank.*;
import org.junit.Assert;
import org.junit.Test;

public class demoTesting {
    private Bank getBank() {
        Bank b = new Bank();
        Bank.INSTANCE = b;
        return b;
    }

    private static final String IBM = "IBM";
    private static final String AMAZON = "AMZN";
    private static final String GOOGLE = "GOOG";
    private final long ssnum = 123456789;
    private final String userName = "diament";
    private final String password = "1234";

    private void purchaseStocks() {
        Bank bank = getBank();
        try {
            //add stocks to market
            bank.addNewStockToMarket(IBM, 100);
            bank.addNewStockToMarket(AMAZON, 1500);
            bank.addNewStockToMarket(GOOGLE, 2000);
            //create accounts and deposit money
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
            bank.depositCashIntoSavings(ssnum, userName, password, 75000);
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 50000);
            //buy stocks
            //buy 100 shares of IBM
            bank.purchaseStock(ssnum, userName, password, IBM, 100);
            //buy 10 shares of Google
            bank.purchaseStock(ssnum, userName, password, GOOGLE, 10);
            //cash left in brokerage: 20000
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to purchase stocks: ");
        }
    }

    @Test
    public void testGetMarketCapitalizationZero() {
        Bank bank = getBank();
        try {
            double shares = bank.getMarketCapitalization(AMAZON);
            Assert.assertEquals("incorrect market cap when total = 0", 0, shares, 0.1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to properly get market cap of zero when total there are no shares of the stock on the market ");
        }
    }
    }















