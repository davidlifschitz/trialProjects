package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.AuthenticationException;
import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;
import org.junit.Assert;
import org.junit.Test;

public class BankSavingsTest {

    private Bank getBank() {
        Bank b = new Bank();
        Bank.INSTANCE = b;
        return b;
    }

    @Test
    public void testPatronCreation() {
        //test using just one patron
        Bank bank = getBank();
        long ssnum = 123456789;
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
        }
        catch (UnauthorizedActionException e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron incorrectly failed");
        }
        try {
            bank.createNewPatron("Judah", "Diament", 999999999, "diament", "1234");
        }
        catch (UnauthorizedActionException e) {
            e.printStackTrace();
            Assert.fail("creation of patron with same info but different SS# incorrectly failed");
        }
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
            Assert.fail("creation of patron with same exact SS# failed to throw an exception");
        }
        catch (UnauthorizedActionException e) {
        }
        //test using just multiple patrons
        bank = getBank();
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
            bank.createNewPatron("Judah", "Diament", ssnum + 2, "diament", "1234");
            bank.createNewPatron("Judah", "Diament", ssnum + 5, "diament", "1234");
            bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
            Assert.fail("creation of patron with same exact SS# failed to throw an exception");
        }
        catch (UnauthorizedActionException e) {
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
        }
        catch (UnauthorizedActionException e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron incorrectly failed");
        }
        try {
            bank.openSavingsAccount(ssnum, userName, password);
        }
        catch (UnauthorizedActionException e) {
            e.printStackTrace();
            Assert.fail("opening savings account incorrectly failed");
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            Assert.fail("opening savings account incorrectly failed");
        }
        try {
            bank.openSavingsAccount(ssnum, userName, password);
            Assert.fail("opening savings account should have thrown UnauthorizedActionException when opening with same ssnum, username, and password");
        }
        catch (Exception e) {
        }
    }

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
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
        }
        catch (UnauthorizedActionException e) {
            e.printStackTrace();
            Assert.fail("initial creation of patron or opening of savings account incorrectly failed");
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            Assert.fail("opening savings account incorrectly failed");
        }
        //deposit into 2 of them
        double amount = 1000.57;
        try {
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 2);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("deposit incorrectly failed");
        }
        //withdraw correctly twice, check balance at the end to make sure deposits and withdrawals actually happening
        try {
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 2);
            bank.withdrawCashFromSavings(ssnum1, userName, password, 1);
            Assert.assertEquals("Account should've had $1 left, but did not", 1, bank.checkBalanceSavings(ssnum1, userName, password), 0.5);
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
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 3);
            Assert.fail("withdrawal of more funds than were in the account should've failed but did not");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetBalances() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount * 2);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 3);
            //withdraw some cash
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 2);
            //check total remaining in ssnum3
            Assert.assertEquals("Incorrect total savings cash in SS# " + ssnum3, amount, bank.checkBalanceSavings(ssnum3, userName, password), 10);
            //check that the correct total remains in the bank
            Assert.assertEquals("Incorrect total savings cash in bank", amount * 4, bank.getTotalSavingsInBank(), 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

    @Test
    public void testGetNetWorth() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //do other things with other patrons
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount * 2);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            //deposit more cash
            bank.depositCashIntoSavings(ssnum1, userName, password, amount * 3);
            //withdraw some cash from a different patron
            bank.withdrawCashFromSavings(ssnum2, userName, password, amount * 2);
            //check that the correct total remains
            Assert.assertEquals("Incorrect Net Worth for SS# " + ssnum1, amount * 4, bank.getNetWorth(ssnum1, userName, password), 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

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
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 2);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            Transaction[] history = bank.getTransactionHistory(ssnum1,userName,password);
            int depositCount = 0;
            int withdrawalCount = 0;
            for(Transaction tx : history){
                if(tx == null){
                    break;
                }
                if(tx instanceof Deposit){
                    depositCount++;
                }
                if(tx instanceof Withdrawal){
                    withdrawalCount++;
                }
            }
            Assert.assertEquals("incorrect number of deposits in TX history",3,depositCount);
            Assert.assertEquals("incorrect number of withdrawals in TX history",2,withdrawalCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("testGetTransactionHistory failed");
        }
    }
}