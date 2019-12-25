package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.smoothstack.lms.entity.BookLoan;

public class BookLoanDAO extends BaseDAO<BookLoan> {
    
    public BookLoanDAO(Connection connection) {
        super(connection);
    }

    public Integer checkoutBook(int bookId, int branchId, int cardNumber) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOutObject = LocalDate.now();
        LocalDate dueDateObject = dateOutObject.plusWeeks(1);
        String dateOut = dateOutObject.format(formatter);
        String dueDate = dueDateObject.format(formatter);
        String sql = "";
        Object[] parameters = null;
        
        sql = "UPDATE  tbl_book_copies SET noOfCopies=noOfCopies-1 WHERE bookId = ? AND branchId = ?";
        parameters = new Object[] {bookId, branchId};
        super.modifyData(sql, parameters);
        sql = "INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (?, ?, ?, ?, ?);";
        parameters = new Object[] {bookId, branchId, cardNumber, dateOut, dueDate};
        return super.insertData(sql, parameters);
    }
    
    public List<BookLoan> getBookLoans() throws SQLException {
        String sql = "SELECT * FROM tbl_book_loans;";
        return super.getData(sql);
    }
    
    public List<BookLoan> getBookLoansForBorrower(int branchId, int cardNumber) throws SQLException {
        String sql = "SELECT * FROM tbl_book_loans WHERE branchId = ? AND cardNo = ?;";
        Object[] parameters = new Object[] {branchId, cardNumber};
        return super.getData(sql, parameters);
    }
    
    public BookLoan getBookLoan(int bookId, int branchId, int cardNumber) throws SQLException {
        String sql = "SELECT * FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ?;";
        Object[] parameters = new Object[] {bookId, branchId, cardNumber};
        List<BookLoan> bookLoans = super.getData(sql, parameters);
        BookLoan bookLoan = null;
        
        if (bookLoans.size() != 0) {
            bookLoan = bookLoans.get(0);
        }
        
        return bookLoan;
    }
    
    public void updateBookLoan(int bookId, int branchId, int amount) throws SQLException {
        String sql = "UPDATE tbl_book_loans SET noOfCopies=? WHERE bookId = ? AND branchId = ?;";
        Object[] parameters = new Object[] {amount, bookId, branchId};
        super.modifyData(sql, parameters);
    }
    
    public void checkinBook(int bookId, int branchId, int cardNumber) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateInObject = LocalDate.now();
        String dateIn = dateInObject.format(formatter);
        String sql = "";
        Object[] parameters = null;
        
        sql = "UPDATE  tbl_book_copies SET noOfCopies=noOfCopies+1 WHERE bookId = ? AND branchId = ?";
        parameters = new Object[] {bookId, branchId};
        super.modifyData(sql, parameters);
        sql = "UPDATE tbl_book_loans SET dateIn=? WHERE bookId = ? AND branchId = ? AND cardNo = ?;";
        parameters = new Object[] {dateIn, bookId, branchId, cardNumber};
        super.modifyData(sql, parameters);
    }
    
    public void overrideDueDate(int bookId, int branchId, int cardNumber, LocalDate dueDateObject) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dueDate = dueDateObject.format(formatter);
        String sql = "UPDATE  tbl_book_loans SET dueDate=? WHERE bookId = ? AND branchId = ? AND cardNo = ?";
        Object[] parameters = new Object[] {dueDate, bookId, branchId, cardNumber};
        super.modifyData(sql, parameters);
    }
    
    @Override
    protected void processData(ResultSet resultSet, List<BookLoan> list) throws SQLException {
        while (resultSet.next()) {
            BookLoan bookLoan = new BookLoan();
            bookLoan.setBookId(resultSet.getInt("bookId"));
            bookLoan.setBranchId(resultSet.getInt("branchId"));
            bookLoan.setCardNumber(resultSet.getInt("cardNo"));
            bookLoan.setDateOut(LocalDate.parse(resultSet.getString("dateOut")));
            bookLoan.setDueDate(LocalDate.parse(resultSet.getString("dueDate")));
            bookLoan.setDateIn(LocalDate.parse(resultSet.getString("dateIn")));
            list.add(bookLoan);
        }
    }

}
