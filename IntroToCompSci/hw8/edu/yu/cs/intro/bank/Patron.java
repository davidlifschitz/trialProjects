package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;

public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private SavingsAccount savingsAccount;

    protected Patron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.userName = userName;
        this.password = password;
    }
    protected String getFirstName(){return this.firstName;}
    protected String getLastName(){return this.lastName;}
    protected long getSocialSecurityNumber(){return this.socialSecurityNumber;}
    protected String getUserName(){return this.userName;}
    protected String getPassword(){return this.password;}
    //NEED TO GET ANSWER ABOUT EXISTENCE OF THIS METHOD FROM PIAZZA
    protected void addAccount(Account acct){}
    //NEED TO GET ANSWER ABOUT EXISTENCE OF THIS METHOD FROM PIAZZA
    protected Account getAccount(long accountNumber) throws NoSuchAccountException{return null;}
    protected void setBrokerageAccount(BrokerageAccount account) {this.brokerageAccount = account;}
    protected void setSavingsAccount(SavingsAccount account) {this.savingsAccount = account;}
    protected BrokerageAccount getBrokerageAccount() {return this.brokerageAccount;}
    protected SavingsAccount getSavingsAccount() {return this.savingsAccount;} 
    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth(){
        double netWorth = 0.0;
        if(this.brokerageAccount != null)
        {
            netWorth += this.brokerageAccount.getTotalBalance();
        }
        if(this.savingsAccount != null)
        {
            netWorth += this.savingsAccount.cash;
        }
        return netWorth;
    }
}
