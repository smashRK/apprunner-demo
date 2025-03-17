package com.turtle.portal.customer.repository;

import com.turtle.portal.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Customer findByEmail(String emailId) {
        String sql = "SELECT * FROM customers WHERE email_id = ?";
        List<Customer> customers = jdbcTemplate.query(sql, new CustomerRowMapper(), emailId);
        return customers.isEmpty() ? null : customers.get(0);
    }
    public Customer findById(String Id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<Customer> customers = jdbcTemplate.query(sql, new CustomerRowMapper(), Id);
        return customers.isEmpty() ? null : customers.get(0);
    }

    public int update(Customer customer) {
        String sql = "UPDATE customers SET    account_number = ?," +
                " contact_number = ?, role = ?, " +
                " reject = ?, pending = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                customer.getAccountNumber(),
                customer.getContactNumber(),
                customer.getRole(),
//                customer.getApprove(),
                customer.getReject(),
                customer.getPending(),
                customer.getId());
    }

    public int updateApprovalStatus(Customer customer)
    {
        String sql = "UPDATE customers SET approve = ?,reject=?,pending=?" +
                " WHERE email_id = ?";
        return jdbcTemplate.update(sql,
                customer.getApprove(),
                customer.getReject(),
                customer.getPending(),
                customer.getEmailId());
    }

    public int save(Customer customer) {
        String sql = "INSERT INTO customers (  first_name, last_name, email_id, account_number, contact_number, " +
                "role, approve, reject, pending) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
//                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmailId(),
                customer.getAccountNumber(),
                customer.getContactNumber(),
                customer.getRole(),
                customer.getApprove(),
                customer.getReject(),
                customer.getPending());
    }

    public List<Customer> findAll() {
        //select all customers
        String sql = "SELECT * FROM customers";
        System.out.println(sql);
        return jdbcTemplate.query(sql, new CustomerRowMapper());
    }
    public int totalCustomers()
    {
        String sql="SELECT COUNT(*) FROM customers";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
    public List<Customer>  findAllPagewise(int pageSize,int offset)
    {
        //select all customers who belong from the page number(page) and page size in default is 10
        String sql="SELECT * FROM customers LIMIT ? OFFSET ? ";

         return jdbcTemplate.query(sql,new Object[]{pageSize,offset},new CustomerRowMapper());
    }
    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmailId(rs.getString("email_id"));
            customer.setAccountNumber(rs.getString("account_number"));
            customer.setContactNumber(rs.getString("contact_number"));
            customer.setRole(rs.getString("role"));
            customer.setApprove(rs.getBoolean("approve"));
            customer.setReject(rs.getBoolean("reject"));
            customer.setPending(rs.getBoolean("pending"));
            return customer;
        }
    }
}