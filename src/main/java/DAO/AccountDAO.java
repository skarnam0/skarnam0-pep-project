package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    /* 
        Create Account
        @param account object with username, password to insert
        @return inserted account w/ account_id
    */

    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO Account(username, password) VALUES (?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1)); // account id
                }
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* 
        Retrieve Account
        @param username
        @return account object if exists, null otherwise
    */

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Account WHERE username = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Account getAccountById(int accountId) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    
}
