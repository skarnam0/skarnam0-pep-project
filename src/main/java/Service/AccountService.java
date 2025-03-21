package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /* 
     * register new account if username is valid & doesn't exist
     */

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            return null;
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            // if username already exists
            return null;
        }

        return accountDAO.createAccount(account);
    }

    /*
     * log in user if credentials match
     */

    public Account loginAccount(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }

        // otherwise invalid credentials return null
        return null;
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    
}
