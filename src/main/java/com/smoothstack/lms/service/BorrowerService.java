package com.smoothstack.lms.service;

import java.util.List;

import com.smoothstack.lms.entity.BookLoan;
import com.smoothstack.lms.entity.LibraryBranch;

public class BorrowerService {
    
    public void checkoutBook(int bookId, int branchId, int cardNumber) {
        return "";
    }
    
    public void checkinBook(int bookId, int branchId, int cardNumber) {
        return "";
    }
    
    public List<LibraryBranch> getLibraryBranches() {
        return null;
    }
    
    public List<Book> getAvailableBooksNotCheckedOut(int cardNumber, int libraryBranchId) {
        return null;
    }
    
    public List<BookLoan> getBookLoans(int branchId, int cardNo) {
        return null;
    }

}
