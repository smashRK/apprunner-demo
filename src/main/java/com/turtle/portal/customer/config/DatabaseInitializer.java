package com.turtle.portal.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Drop existing table if exists
        // jdbcTemplate.execute("DROP TABLE IF EXISTS customers");

        // Create table with new structure
        String sql = "CREATE TABLE IF NOT EXISTS customers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(255) NOT NULL, " +
                "last_name VARCHAR(255) NOT NULL, " +
                "email_id VARCHAR(255) NOT NULL, " +
                "account_number VARCHAR(255) NOT NULL, " +
                "contact_number VARCHAR(20) NOT NULL, " +
                // "department VARCHAR(50) NOT NULL,"+
                "role VARCHAR(50) NOT NULL, " +
                "approve BOOLEAN DEFAULT FALSE, " +
                "reject BOOLEAN DEFAULT FALSE, " +
                "pending BOOLEAN DEFAULT TRUE)";
                // "approve_timestamp TIMESTAMP NULL," +
                // "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                // "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";

        //create sample data

        // String data = "INSERT INTO customers (first_name, last_name, email_id, account_number, contact_number, department, role, approve, reject, pending, approve_timestamp) VALUES " +
        //         "('vitchco', 'vitchco', 'vitchcor6196@gmail.com', '78451296322', '98765432122', 'Sales', 'admin', 0, 0, 1, NULL), " +
        //         "('vitchco', 'raj', 'vitchco@gmail.com', '78451296322', '98765432122', 'Customer Support', 'admin', 0, 0, 1, NULL), " +
        //         "('Alice', 'Brown', 'alice.brown@email.com', '100001', '9876543211', 'Marketing', 'user', 0, 0, 1, NULL), " +
        //         "('Bob', 'Green', 'bob.green@email.com', '100002', '9876543212', 'Inventory Management', 'admin', 0, 0, 1, NULL), " +
        //         "('Charlie', 'Davis', 'charlie.davis@email.com', '100003', '9876543213', 'IT', 'user', 0, 0, 1, NULL), " +
        //         "('Daniel', 'Evans', 'daniel.evans@email.com', '100004', '9876543214', 'HR', 'admin', 0, 0, 1, NULL), " +
        //         "('Emma', 'Wilson', 'emma.wilson@email.com', '100005', '9876543215', 'Finance', 'user', 0, 0, 1, NULL), " +
        //         "('Frank', 'Harris', 'frank.harris@email.com', '100006', '9876543216', 'Customer Support', 'admin', 0, 0, 1, NULL), " +
        //         "('Grace', 'Anderson', 'grace.anderson@email.com', '100007', '9876543217', 'Marketing', 'user', 0, 0, 1, NULL), " +
        //         "('Henry', 'Martinez', 'henry.martinez@email.com', '100008', '9876543218', 'Sales', 'admin', 0, 0, 1, NULL), " +
        //         "('Isabella', 'Lopez', 'isabella.lopez@email.com', '100009', '9876543219', 'IT', 'user', 0, 0, 1, NULL), " +
        //         "('Jack', 'Gonzalez', 'jack.gonzalez@email.com', '100010', '9876543220', 'Inventory Management', 'admin', 0, 0, 1, NULL), " +
        //         "('Katherine', 'Perez', 'katherine.perez@email.com', '100011', '9876543221', 'HR', 'user', 0, 0, 1, NULL), " +
        //         "('Leo', 'Thomas', 'leo.thomas@email.com', '100012', '9876543222', 'Finance', 'admin', 0, 0, 1, NULL), " +
        //         "('Mia', 'Taylor', 'mia.taylor@email.com', '100013', '9876543223', 'Customer Support', 'user', 0, 0, 1, NULL), " +
        //         "('Noah', 'Moore', 'noah.moore@email.com', '100014', '9876543224', 'Marketing', 'admin', 0, 0, 1, NULL), " +
        //         "('Olivia', 'Jackson', 'olivia.jackson@email.com', '100015', '9876543225', 'Sales', 'user', 0, 0, 1, NULL), " +
        //         "('Paul', 'White', 'paul.white@email.com', '100016', '9876543226', 'IT', 'admin', 0, 0, 1, NULL), " +
        //         "('Quinn', 'Hernandez', 'quinn.hernandez@email.com', '100017', '9876543227', 'Inventory Management', 'user', 0, 0, 1, NULL), " +
        //         "('Rachel', 'Young', 'rachel.young@email.com', '100018', '9876543228', 'HR', 'admin', 0, 0, 1, NULL), " +
        //         "('Samuel', 'King', 'samuel.king@email.com', '100019', '9876543229', 'Finance', 'user', 0, 0, 1, NULL), " +
        //         "('Taylor', 'Scott', 'taylor.scott@email.com', '100020', '9876543230', 'Customer Support', 'admin', 0, 0, 1, NULL), " +
        //         "('Ursula', 'Adams', 'ursula.adams@email.com', '100021', '9876543231', 'Marketing', 'user', 0, 0, 1, NULL), " +
        //         "('Victor', 'Baker', 'victor.baker@email.com', '100022', '9876543232', 'Sales', 'admin', 0, 0, 1, NULL), " +
        //         "('Wendy', 'Gonzalez', 'wendy.gonzalez@email.com', '100023', '9876543233', 'IT', 'user', 0, 0, 1, NULL), " +
        //         "('Xander', 'Nelson', 'xander.nelson@email.com', '100024', '9876543234', 'Inventory Management', 'admin', 0, 0, 1, NULL), " +
        //         "('Yasmine', 'Carter', 'yasmine.carter@email.com', '100025', '9876543235', 'HR', 'user', 0, 0, 1, NULL), " +
        //         "('Zachary', 'Mitchell', 'zachary.mitchell@email.com', '100026', '9876543236', 'Finance', 'admin', 0, 0, 1, NULL), " +
        //         "('Ava', 'Rodriguez', 'ava.rodriguez@email.com', '100027', '9876543237', 'Customer Support', 'user', 0, 0, 1, NULL);";



//        String data = "INSERT INTO customers (first_name, last_name, email_id, account_number, contact_number, department, role, approve, reject, pending) " +
//                "VALUES " +
//                "('vitchco', 'vitchco', 'vitchcor6196@gmail.com', '78451296322', '98765432122', 'Sales', 'admin', 0, 1, 0), " +
//                "('vitchco', 'raj', 'vitchco@gmail.com', '78451296322', '98765432122', 'Customer Support', 'admin', 0, 1, 0), " +
//                "('Alice', 'Brown', 'alice.brown@email.com', '100001', '9876543211', 'Marketing', 'user', 1, 0, 0), " +
//                "('Bob', 'Green', 'bob.green@email.com', '100002', '9876543212', 'Inventory Management', 'admin', 1, 0, 0), " +
//                "('Charlie', 'Davis', 'charlie.davis@email.com', '100003', '9876543213', 'Finance & Billing', 'user', 0, 1, 0), " +
//                "('David', 'Miller', 'david.miller@email.com', '100004', '9876543214', 'Loyalty & Rewards', 'admin', 1, 0, 0), " +
//                "('Ella', 'Smith', 'ella.smith@email.com', '100005', '9876543215', 'Returns & Refunds', 'user', 0, 0, 1), " +
//                "('Frank', 'Taylor', 'frank.taylor@email.com', '100006', '9876543216', 'E-commerce Operations', 'user', 1, 0, 0), " +
//                "('Grace', 'Wilson', 'grace.wilson@email.com', '100007', '9876543217', 'Store Management', 'admin', 0, 1, 0), " +
//                "('Harry', 'Anderson', 'harry.anderson@email.com', '100008', '9876543218', 'Delivery & Logistics', 'user', 1, 0, 0), " +
//                "('Isla', 'Thomas', 'isla.thomas@email.com', '100009', '9876543219', 'Customer Support', 'admin', 1, 0, 0), " +
//                "('Jack', 'White', 'jack.white@email.com', '100010', '9876543220', 'Inventory Management', 'user', 0, 1, 0), " +
//                "('Kate', 'Harris', 'kate.harris@email.com', '100011', '9876543221', 'Finance & Billing', 'admin', 1, 0, 0), " +
//                "('Leo', 'Clark', 'leo.clark@email.com', '100012', '9876543222', 'Sales', 'user', 0, 1, 0), " +
//                "('Mia', 'Walker', 'mia.walker@email.com', '100013', '9876543223', 'Returns & Refunds', 'admin', 1, 0, 0), " +
//                "('Noah', 'Hall', 'noah.hall@email.com', '100014', '9876543224', 'E-commerce Operations', 'user', 0, 1, 0), " +
//                "('Olivia', 'Allen', 'olivia.allen@email.com', '100015', '9876543225', 'Marketing', 'admin', 1, 0, 0), " +
//                "('Paul', 'Young', 'paul.young@email.com', '100016', '9876543226', 'Finance & Billing', 'user', 0, 1, 0), " +
//                "('Quinn', 'King', 'quinn.king@email.com', '100017', '9876543227', 'Customer Support', 'admin', 1, 0, 0), " +
//                "('Rachel', 'Scott', 'rachel.scott@email.com', '100018', '9876543228', 'Loyalty & Rewards', 'user', 0, 1, 0), " +
//                "('Steve', 'Adams', 'steve.adams@email.com', '100019', '9876543229', 'Inventory Management', 'admin', 1, 0, 0), " +
//                "('Tina', 'Nelson', 'tina.nelson@email.com', '100020', '9876543230', 'Store Management', 'user', 0, 1, 0), " +
//                "('Uma', 'Carter', 'uma.carter@email.com', '100021', '9876543231', 'E-commerce Operations', 'admin', 1, 0, 0), " +
//                "('Victor', 'Mitchell', 'victor.mitchell@email.com', '100022', '9876543232', 'Delivery & Logistics', 'user', 0, 1, 0), " +
//                "('Wendy', 'Perez', 'wendy.perez@email.com', '100023', '9876543233', 'Marketing', 'admin', 1, 0, 0), " +
//                "('Xander', 'Roberts', 'xander.roberts@email.com', '100024', '9876543234', 'Returns & Refunds', 'user', 0, 1, 0), " +
//                "('Yara', 'Phillips', 'yara.phillips@email.com', '100025', '9876543235', 'Customer Support', 'admin', 1, 0, 0), " +
//                "('Zane', 'Evans', 'zane.evans@email.com', '100026', '9876543236', 'Sales', 'user', 0, 1, 0), " +
//                "('Amy', 'Garcia', 'amy.garcia@email.com', '100027', '9876543237', 'Store Management', 'admin', 1, 0, 0), " +
//                "('Brian', 'Martinez', 'brian.martinez@email.com', '100028', '9876543238', 'Finance & Billing', 'user', 0, 1, 0)";

// Execute query using JDBC Template
//        jdbcTemplate.update(query);



        jdbcTemplate.execute(sql);
        // jdbcTemplate.execute(data);
        System.out.println("Customers table created successfully with new structure.");
    }
}
