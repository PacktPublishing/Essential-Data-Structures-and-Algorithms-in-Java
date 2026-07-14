public class FixedAccountTransfer {
    public void transfer(Account from, Account to, int amount) {
        // Always lock in consistent order (using object hash codes)
        Account first = from.hashCode() < to.hashCode() ? from : to;
        Account second = from.hashCode() < to.hashCode() ? to : from;
        
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}

