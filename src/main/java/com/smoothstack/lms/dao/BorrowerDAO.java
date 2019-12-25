package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> {

    public BorrowerDAO(Connection connection) {
        super(connection);
    }

    public Integer createBorrower(String name, String address, String phone) throws SQLException {
        String sql = "INSERT INTO tbl_borrower (name, address, phone) VALUES (?, ?, ?);";
        Object[] parameters = new Object[] {name, address, phone};
        return super.insertData(sql, parameters);
    }
    
    public List<Borrower> getBorrowers() throws SQLException {
        String sql = "SELECT * FROM tbl_borrower;";
        return super.getData(sql);
    }
    
    public Borrower getBorrower(int cardNo) throws SQLException {
        String sql = "SELECT * FROM tbl_borrower WHERE cardNo = ?;";
        Object[] parameters = new Object[] {cardNo};
        List<Borrower> borrowers = super.getData(sql, parameters);
        Borrower borrower = null;
        
        if (borrowers.size() != 0) {
            borrower = borrowers.get(0);
        }
        
        return borrower;
    }
    
    public List<Borrower> getBorrowerBooks(int cardNumber, int branchId) throws SQLException {
        String sql = "SELECT * FROM tbl_borrower "
                + "NATURAL JOIN TBL_BOOK_LOANS NATURAL JOIN tbl_book NATURAL JOIN tbl_library_branch "
                + "WHERE cardNo = ? AND branchId = ?;";
        Object[] parameters = new Object[] {cardNumber, branchId};
        return super.getData(sql, parameters);
    }
    
    public void updateBorrower(int cardNo, String name, String address, String phone) throws SQLException {
        String sql = "UPDATE tbl_borrower SET name=?, address=?, phone=? WHERE cardNo = ?;";
        Object[] parameters = new Object[] {cardNo, name, address, phone};
        super.modifyData(sql, parameters);
    }
    
    public void deleteAuthor(int cardNo) throws SQLException {
        String sql = "DELETE FROM tbl_borrower WHERE cardNo = ?;";
        Object[] parameters = new Object[] {cardNo};
        super.modifyData(sql, parameters);
    }
    
    public boolean borrowerExists(int cardNumber) throws SQLException {
        String sql = "SELECT * FROM tbl_borrower "
                + "WHERE cardNo = ?;";
        Object[] parameters = new Object[] {cardNumber};
        return super.getData(sql, parameters).size() == 1;         
    }
    
    @Override
    protected void processData(ResultSet resultSet, List<Borrower> list) throws SQLException {
        while (resultSet.next()) {
            Borrower borrower = new Borrower();
            borrower.setCardNumber(resultSet.getInt("borrowerId"));
            borrower.setName(resultSet.getString("name"));
            borrower.setAddress(resultSet.getString("address"));
            borrower.setPhoneNumber(resultSet.getString("phone"));
            list.add(borrower);
        }
    }
    
}
