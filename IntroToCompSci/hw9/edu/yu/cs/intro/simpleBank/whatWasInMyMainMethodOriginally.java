/*Bank theBank = new Bank(0);
        System.out.println(theBank.transactionFee);
        
        //Create 2 Patrons
        theBank.createNewPatron("David", "Lifschitz", 12345, "userName", "password");
        theBank.createNewPatron("firstName", "lastName", 123456, "userName2", "password2");
        Patron newPatron = getBank().getPatronBySSN().get((long)(123456));
        
        //Accounts
        //Account newAccount = new Account(1);
        //theBank.bankPatrons.iterator().next().addAccount(newAccount);
        //System.out.println(theBank.bankPatrons.iterator().next().getAccount(1));

        //Opening BrokerageAccounts for the two patrons;
        theBank.openBrokerageAccount(newPatron.getSocialSecurityNumber(), newPatron.getUserName(), newPatron.getPassword());
        theBank.openBrokerageAccount(12345, "userName", "password");

        
        
        //Opening SavingsAccounts for the two patrons;
        theBank.openSavingsAccount(newPatron.getSocialSecurityNumber(), newPatron.getUserName(), newPatron.getPassword());
        theBank.openSavingsAccount(12345, "userName", "password");
        
        //Depositing Cash into Savings
        theBank.depositCashIntoSavings(12345, "userName", "password", 1000);
        theBank.depositCashIntoSavings(123456, "userName2", "password2", 1000);

        //WithdrawCashFromSavings
        theBank.withdrawCashFromSavings(12345, "userName", "password", 1000 - 2);
        theBank.withdrawCashFromSavings(12345, "userName", "password", 1);
        
        //Transfering Cash from Savings into Brokerage
        theBank.transferFromSavingsToBrokerage(12345, "userName","password", 200);
        theBank.transferFromSavingsToBrokerage(123456, "userName2","password2", 200);
        //Checking: Depositing Cash into Savings
        System.out.println(theBank.checkBalanceSavings(12345, "userName", "password"));
        
        
        //Checking: Total Cash from BrokerageAccounts in Bank
        System.out.println(theBank.getTotalBrokerageCashInBank());
        
        
        //Checking: Total Cash from SavingsAccounts in Bank
        System.out.println(theBank.getTotalSavingsInBank());
        
        //Creating new Stocks
        theBank.addNewStockToMarket("GOOG", 2.0);
        theBank.addNewStockToMarket("APPL", 10.0);
        
        //Buying Shares
        newPatron.getBrokerageAccount().buyShares("GOOG", 5);
        newPatron.getBrokerageAccount().buyShares("APPL", 5);
        theBank.purchaseStock(12345, "userName", "password", "GOOG", 5);
        theBank.purchaseStock(12345, "userName", "password", "APPL", 5);
        System.out.println(newPatron.getBrokerageAccount().getTotalBalance());
        System.out.println(getBank().getPatronBySSN().get((long)(12345)).getBrokerageAccount().getTotalBalance());
        
        //Selling Shares
        newPatron.getBrokerageAccount().sellShares("GOOG", 2);
        newPatron.getBrokerageAccount().sellShares("APPL", 2);
        theBank.sellStock(12345, "userName", "password", "GOOG", 2);
        theBank.sellStock(12345, "userName", "password", "APPL", 2);
        

        //Getting Market Capital
        //I need to add stocks to all brokerageAccounts before I can run this method. 
        //(or add a try catch and then it just returns 0.)
        System.out.println(theBank.getMarketCapitalization("GOOG"));
        System.out.println(theBank.getMarketCapitalization("APPL"));
        
        
        //Getting the Transaction History per Patron
        Transaction[] transHistory1 = theBank.getTransactionHistory(12345, "userName", "password");
        System.out.println("got the transaction history for first patron.");
        for(int i = 0; i < transHistory1.length; i ++)
        {
            System.out.println(transHistory1[i].getTime());
            System.out.println("^ this transaction time was.");
        }
        Transaction[] transHistory2 =theBank.getTransactionHistory(123456, "userName2", "password2");
        System.out.println("got the transaction history for second patron.");
        for(int i = 0; i < transHistory2.length; i ++)
        {
            System.out.println(transHistory1[i].getTime());
            System.out.println("^ this transaction time was.");
        }
       
        //Getting netWorth per Patron
        double firstPatronsNetWorth = theBank.getNetWorth(12345, "userName", "password");
        double secondPatronsNetWorth = theBank.getNetWorth(123456, "userName2", "password2");
        System.out.println(firstPatronsNetWorth);
        System.out.println(secondPatronsNetWorth);
        */
        