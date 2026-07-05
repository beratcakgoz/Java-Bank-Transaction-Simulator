import java.util.Random;

class BankException extends Exception {
    public BankException(String nameofclass, String operation, String reason) {
        super(nameofclass + ": Cannot " + operation + "; " + reason);
    }
}

abstract class BankAccount {
    protected String id;
    protected float balance;

    public BankAccount(String id, float balance) {
        this.id = id;
        this.balance = balance;
    }

    public void deposit(float number) throws BankException {
        if (number < 0) {
            throw new BankException("BankAccount", "deposit", "number is negative.");
        }
        this.balance += number;
    }

    public void withdraw(float number) throws BankException {
        if (!this.canWithdraw(number)) {
            throw new BankException("BankAccount", "withdraw", "there is not enough money.");
        }
        this.balance -= number;
    }

    public boolean canWithdraw(float number) {
        return this.balance >= number;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + ", Balance: " + this.balance;
    }
}

class CheckingAccount extends BankAccount {
    private float overdraftLimit;

    public CheckingAccount(String id, float balance, float overdraftLimit) {
        super(id, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(float number) throws BankException {
        if (!this.canWithdraw(number)) {
            throw new BankException("CheckingAccount", "withdraw", "there is not enough money.");
        }
        super.withdraw(number);
    }

    @Override
    public boolean canWithdraw(float number) {
        return (this.balance + this.overdraftLimit) >= number;
    }

    @Override
    public String toString() {
        return super.toString() + ", Overdraft Limit: " + this.overdraftLimit;
    }
}

class FXAccount extends BankAccount {
    private float exchangeRate;

    public FXAccount(String id, float balance, float exchangeRate) {
        super(id, balance);
        this.exchangeRate = exchangeRate;
    }

    @Override
    public void deposit(float number) throws BankException {
        if (number < 0) {
            throw new BankException("FXAccount", "deposit", "the number is negative.");
        }
        super.deposit(number / this.exchangeRate);
    }

    @Override
    public void withdraw(float number) throws BankException {
        float convertedMoney = number / this.exchangeRate;
        if (!this.canWithdraw(convertedMoney)) {
            throw new BankException("FXAccount", "withdraw", "not enough money.");
        }
        super.withdraw(convertedMoney);
    }

    @Override
    public String toString() {
        return super.toString() + ", Exchange Rate: " + this.exchangeRate;
    }
}

abstract class Bank {
    public Bank() {}

    public abstract String toString();

    public void transfer(BankAccount first, BankAccount second, float number) throws BankException {
        first.withdraw(number);
        try {
            second.deposit(number);
        } catch (BankException a) {
            first.deposit(number); // İade (Rollback) işlemi
            throw a;
        }
    }
}

class CommissionBank extends Bank {
    protected float commissionRate;
    protected float balance;

    public CommissionBank(float commissionRate) {
        this.commissionRate = commissionRate;
        this.balance = 0;
    }

    protected float calculateCommission(float number) {
        return number * this.commissionRate;
    }

    @Override
    public String toString() {
        return "Bank Balance: " + this.balance + ", Commission rate: " + this.commissionRate;
    }

    @Override
    public void transfer(BankAccount first, BankAccount second, float number) throws BankException {
        float commission = calculateCommission(number);
        float totalToWithdraw = number + commission;
        
        try {
            first.withdraw(totalToWithdraw);
        } catch (BankException e) {
            throw new BankException("CommissionBank", "transfer from " + first.id + " to " + second.id, "sender doesn't have enough money.");
        }
        
        try {
            second.deposit(number);
        } catch (BankException e) {
            first.deposit(totalToWithdraw); // İade (Rollback) işlemi
            throw e;
        }
        
        this.balance += commission; // Komisyonu bankaya ekle
    }
}

class MinCommissionBank extends CommissionBank {
    private float minCommission;

    public MinCommissionBank(float commissionRate, float minCommission) {
        super(commissionRate);
        this.minCommission = minCommission;
    }

    @Override
    protected float calculateCommission(float number) {
        float calculated = super.calculateCommission(number);
        return (calculated < minCommission) ? minCommission : calculated;
    }

    @Override
    public String toString() {
        return "Bank balance: " + this.balance + ", Commission rate: " + this.commissionRate + ", Minimum commission: " + this.minCommission;
    }
}

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        BankAccount[] accounts = new BankAccount[20];
        
       
        for (int i = 0; i < accounts.length; i++) {
            int a = rand.nextInt(3);
            String id = "account" + (i + 1);
            
            if (a == 0) {
                accounts[i] = new BankAccount(id, 0) {};
            } else if (a == 1) {
                float overdraft = 50 + rand.nextInt(151);
                accounts[i] = new CheckingAccount(id, 0, overdraft);
            } else {
                float rate = 0.5f + rand.nextFloat() * 1.5f;
                accounts[i] = new FXAccount(id, 0, rate);
            }
            
            // Hesaplara başlangıç bakiyesi yatırma
            try {
                float depositAmount = 100 + rand.nextInt(400);
                accounts[i].deposit(depositAmount);
            } catch (BankException e) {
                System.out.println("Deposit error: " + e);
            }
        }
        
        
        Bank[] banks = new Bank[3];
        banks[0] = new Bank() {
            @Override
            public String toString() {
                return "This bank is a no commission bank.";
            }
        };
        banks[1] = new CommissionBank(0.05f);
        banks[2] = new MinCommissionBank(0.02f, 2.0f);
        
        
        for (int i = 0; i < 100; i++) {
            int from = rand.nextInt(accounts.length);
            int to = rand.nextInt(accounts.length);
            
            
            while (to == from) {
                to = rand.nextInt(accounts.length);
            }
            
            float num = 20 + rand.nextInt(30);
            int bank = rand.nextInt(banks.length);
            
            try {
                banks[bank].transfer(accounts[from], accounts[to], num);
            } catch (BankException e) {
                System.out.println("Transfer failed: " + e);
            }
        }
        
        
        System.out.println("\n--- Accounts ---");
        for (BankAccount acc : accounts) {
            System.out.println(acc);
        }
        
        System.out.println("\n--- Banks ---");
        for (Bank b : banks) {
            System.out.println(b);
        }
    }
}