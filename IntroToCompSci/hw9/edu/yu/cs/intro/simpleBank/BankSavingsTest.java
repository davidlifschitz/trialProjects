package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.*;
import org.junit.Assert;
import org.junit.Test;

public class BankSavingsTest {

    private Bank getBank() {
        return new Bank(0);
    }

    @Test
    public void testPatronCreation() {
        //test using just one patron
        Bank bank = getBank();
        long ssnum = 123456789;
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
            bank.getNetWorth(ssnum,"diament","1234");
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron incorrectly failed");
        }
        try {
            bank.createNewPatron("Judah", "Diament", 999999999, "diament", "1234");
            bank.getNetWorth(999999999,"diament","1234");
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("creation of patron with same info but different SS# incorrectly failed");
        }
        //test using just multiple patrons
        bank = getBank();
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
            bank.getNetWorth(ssnum,"diament","1234");
            bank.createNewPatron("Judah", "Diament", ssnum + 2, "diament", "1234");
            bank.getNetWorth(ssnum+2,"diament","1234");
            bank.createNewPatron("Judah", "Diament", ssnum + 5, "diament", "1234");
            bank.getNetWorth(ssnum+5,"diament","1234");
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testOpenSavingsAccount() {
        Bank bank = getBank();
        long ssnum = 123456789;
        String userName = "diament";
        String password = "1234";
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.getNetWorth(ssnum,"diament","1234");
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron incorrectly failed");
        }
        try {
            bank.openSavingsAccount(ssnum, userName, password);
            Assert.assertEquals("balance should be zero",0,bank.getNetWorth(ssnum,"diament","1234"),0.0001);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("opening savings account incorrectly failed");
        }
    }
    //This didn't work because the withdraw.execute would only run 
    //if the amount in the savings account was greater than the amount withdrawn, 
    //it should have been greater than OR EQUAL TO.
    //This was also the problem in the withdrawCashFromSavings Method in Bank.
    
    //The actual problem was that i forgot to add an else statement so right 
    //after each time it did the transaction correctly it still threw the exception
    @Test
    public void testDepositAndWithdrawCashFromSavings() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.getNetWorth(ssnum2,"diament","1234");
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.getNetWorth(ssnum1,"diament","1234");
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.getNetWorth(ssnum3,"diament","1234");
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron or opening of savings account incorrectly failed");
        }
        //deposit into 2 of them
        double amount = 1000.57;
        try {
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 2d);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("deposit incorrectly failed");
        }
        //withdraw correctly twice, check balance at the end to make sure deposits and withdrawals actually happening
        try {
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 2d);
            bank.withdrawCashFromSavings(ssnum1, userName, password, 1d);
            Assert.assertEquals("Account should've had $1 left, but did not", 1d, bank.checkBalanceSavings(ssnum1, userName, password), 0.5);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("first withdrawal incorrectly failed");
        }
        //withdraw from someone who has no savings account
        try {
            bank.withdrawCashFromSavings(ssnum2, userName, password, amount);
            Assert.fail("withdrawal from someone who has no savings account should have failed but did not");
        }
        catch (Exception e) {
        }
        //withdraw too much money from someone who has a savings account
        try {
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 3d);
            Assert.fail("withdrawal of more funds than were in the account should've failed but did not");
        } catch (Exception e) {
        }
    }


    //Same problem with forgetting the else statement.
    @Test
    public void testGetBalances() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000d;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount * 2d);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 3d);
            //withdraw some cash
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 2d);
            //check total remaining in ssnum3
            Assert.assertEquals("Incorrect total savings cash in SS# " + ssnum3, amount, bank.checkBalanceSavings(ssnum3, userName, password), 10);
            //check that the correct total remains in the bank
            Assert.assertEquals("Incorrect total savings cash in bank", amount * 4d, bank.getTotalSavingsInBank(), 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

    //Problem was that I required both the savings and brokerage accounts to exist. What i thought was happy code.
    //There was nothing in the api about only having one of the accounts. only if there were none.
    @Test
    public void testGetNetWorth() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000d;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //do other things with other patrons
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount * 2d);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            //deposit more cash
            bank.depositCashIntoSavings(ssnum1, userName, password, amount * 3d);
            //withdraw some cash from a different patron
            bank.withdrawCashFromSavings(ssnum2, userName, password, amount * 2d);
            //check that the correct total remains
            Assert.assertEquals("Incorrect Net Worth for SS# " + ssnum1, amount * 4d, bank.getNetWorth(ssnum1, userName, password), 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

    //Had the same problem with the else statement.
    //When i fixed the else statement it passed the test.
    //test getTransactionHistory
    @Test
    public void testGetTransactionHistory() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            //deposit into 2 of them
            double amount = 1000.57;
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //withdraw correctly twice, check balance at the end to make sure deposits and withdrawals actually happening
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 2d);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            Transaction[] history = bank.getTransactionHistory(ssnum1,userName,password);
            int txCount = 0;
            for(Transaction tx : history){
                if(tx == null){
                    break;
                }
                txCount++;
            }
            Assert.assertEquals("incorrect number of TXs in TX history",5,txCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("testGetTransactionHistory failed");
        }
    }
}