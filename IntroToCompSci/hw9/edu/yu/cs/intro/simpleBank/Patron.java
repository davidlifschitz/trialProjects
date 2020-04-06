package edu.yu.cs.intro.simpleBank;





import edu.yu.cs.intro.simpleBank.InsufficientAssetsException;


public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private Account savingsAccount;

    

    protected Patron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.userName = userName;
        this.password = password;
    }

    
    protected String getFirstName(){return this.firstName;}
    protected String getLastName(){return this.lastName;}
    protected String getUserName(){return this.userName;}
    protected String getPassword(){return this.password;}
    protected long getSocialSecurityNumber(){return this.socialSecurityNumber;}
    protected void addAccount(Account acct){
        if(!(acct instanceof BrokerageAccount))
        {
            this.savingsAccount = acct; 
        } else if(acct instanceof BrokerageAccount)
        {
            this.brokerageAccount = (BrokerageAccount)(acct);
        }
    }
    protected Account getAccount(long accountNumber){
        Account newAccount = new Account(accountNumber);
        
        if (!(newAccount instanceof BrokerageAccount)) 
        {
            this.savingsAccount = newAccount;
        } else {
            this.brokerageAccount = (BrokerageAccount)(newAccount);
        }
        return newAccount;
    }
    protected void setBrokerageAccount(BrokerageAccount account) {this.brokerageAccount = account;}
    protected void setSavingsAccount(Account account) {this.savingsAccount = account;}
    protected BrokerageAccount getBrokerageAccount() {return this.brokerageAccount;}
    protected Account getSavingsAccount() {return this.savingsAccount;}

    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth()
    {
        double netWorth = 0.0;
        netWorth += this.savingsAccount.getAvailableBalance() + this.brokerageAccount.getTotalBalance();
        return netWorth;
    }
}
