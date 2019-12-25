package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.LibraryBranch;

public class LibraryBranchDAO extends BaseDAO<LibraryBranch> {

    public LibraryBranchDAO(Connection connection) {
        super(connection);
    }
    
    public Integer createBranch(String branchName, String branchAddress) throws SQLException {
        String sql = "INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?, ?);";
        Object[] parameters = new Object[] {branchName, branchAddress};
        return super.insertData(sql, parameters);
    }
    
    public List<LibraryBranch> getBranchs() throws SQLException {
        String sql = "SELECT * FROM tbl_library_branch;";
        return super.getData(sql);
    }
    
    public LibraryBranch getBranch(int branchId) throws SQLException {
        String sql = "SELECT * FROM tbl_library_branch WHERE branchId = ?;";
        Object[] parameters = new Object[] {branchId};
        List<LibraryBranch> branches = super.getData(sql, parameters);
        LibraryBranch branch = null;
        
        if (branches.size() != 0) {
            branch = branches.get(0);
        }
        
        return branch;
    }
    
    public void updateBranch(int branchId, String branchName, String branchAddress) throws SQLException {
        String sql = "UPDATE tbl_library_branch SET branchName=?, branchAddress=? WHERE branchId = ?;";
        Object[] parameters = new Object[] {branchName, branchAddress, branchId};
        super.modifyData(sql, parameters);
    }
    
    public void deleteBranch(int branchId) throws SQLException {
        String sql = "DELETE FROM tbl_library_branch WHERE branchId = ?;";
        Object[] parameters = new Object[] {branchId};
        super.getData(sql, parameters);
    }

    @Override
    protected void processData(ResultSet resultSet, List<LibraryBranch> list) throws SQLException {
        while (resultSet.next()) {
            LibraryBranch branch = new LibraryBranch();
            branch.setBranchId(resultSet.getInt("branchId"));
            branch.setName(resultSet.getString("branchName"));
            branch.setAddress(resultSet.getString("branchAddress"));
            list.add(branch);
        }
    }

}
