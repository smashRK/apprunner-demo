package com.turtle.portal.customer.api;
import  org.springframework.http.HttpStatus;
import  org.springframework.http.ResponseEntity;
import com.turtle.portal.customer.model.Customer;
import com.turtle.portal.customer.repository.CustomerRepository;
import com.turtle.portal.customer.service.EntraIdUserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customers")
public class CustomerController
{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntraIdUserAccess entraIdUserAccess;

    @PostMapping("/update")
    public String updateCustomer(@RequestBody Customer customer)
    {
        //Updating the mutable fields  AccountNumber,ContactNumber,Role of the customer
        try
        {
            // Validate required fields
            if ( customer.getId() <= 0 ||customer.getAccountNumber() == null ||
                    customer.getContactNumber() == null || customer.getRole() == null ||
                    customer.getReject() ==null  ||customer.getPending()==null

            )
            {
                return "Error: All fields are required";
            }

            // Check if customer with id already exists
            Customer existingCustomer = customerRepository.findById(String.valueOf(customer.getId()));
            System.out.print(existingCustomer);
            if (existingCustomer != null)
            {
                // Check if all fields are identical
                if (areCustomersIdentical(existingCustomer, customer))
                {
                    return "Record already exists";
                }

                // Update existing customer with new values
                customer.setId(existingCustomer.getId());
                int result = customerRepository.update(customer);
                return result == 1 ? "Customer updated successfully" : "Error updating customer";
            }


            return "Update api status:200 but fields are not updated";


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String>  addCustomer(@RequestBody Customer customer)
    {
        //Insert attributes data in the  database of the customer
        try {
            // Validate required fields
            if (customer.getFirstName() == null || customer.getLastName() == null ||
                    customer.getEmailId() == null || customer.getAccountNumber() == null ||
                    customer.getContactNumber() == null || customer.getRole() == null) {
                return ResponseEntity.badRequest().body("Error: All fields are required");
//                return "Error: All fields are required";
            }
            // Set default values for new customer
            customer.setPending(true);
            customer.setApprove(false);
            customer.setReject(false);
            // If no existing customer, perform insert
            int customer_saved = customerRepository.save(customer);
            // Return response based on the result
            if (customer_saved > 0) {
                return ResponseEntity.ok("Customer added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding customer");
            }
//            return customer_saved == 1 ? "Customer added successfully" : "Error adding customer";


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());

//            return "Error:" + e.getMessage();
        }
    }
    @PostMapping("/approveUser")
    public String EntraIdapproval(@RequestBody Customer customer)
    {
    //Approving user by creating a new entra id with the attribute details of the customer
    try{
        // Validate required fields
        if (  customer.getFirstName() == null || customer.getLastName() == null ||
                customer.getEmailId() == null || customer.getAccountNumber()==null ||
                customer.getContactNumber()==null || customer.getRole()==null ||  customer.getApprove() == null ||
                customer.getReject() ==null ||customer.getPending() ==null)
        {
            return "Error: All fields are required and approve should be true";
        }
        //Initiate  the attributes
        String firstName=customer.getFirstName();
        String lastName=customer.getLastName();
        String emailId=customer.getEmailId();
        String contactNumber=customer.getContactNumber();
        String accountNumber=customer.getAccountNumber();

        //Check the Approval status
        if (customer.getApprove() &&
                !customer.getPending() && !customer.getReject())
        {
        String result=entraIdUserAccess.entraIdconfig(firstName,lastName,emailId,contactNumber,accountNumber);
        return result;
        }
        else
        {
            return "User not yet approved";
        }


    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }





    private boolean areCustomersIdentical(Customer c1, Customer c2)
    {
        //Checking  whether the attributes already exists
        return c1.getFirstName().equals(c2.getFirstName()) &&
                c1.getLastName().equals(c2.getLastName()) &&
                c1.getEmailId().equals(c2.getEmailId()) &&
                c1.getAccountNumber().equals(c2.getAccountNumber()) &&
                c1.getContactNumber().equals(c2.getContactNumber()) &&
                c1.getRole().equals(c2.getRole()) &&
                c1.getApprove() == c2.getApprove() &&
                c1.getReject() == c2.getReject() &&
                c1.getPending() == c2.getPending();
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers()
    {
        //Get all attributes from database of the customer
        return customerRepository.findAll();
    }
//    @GetMapping("/all")
//    public Map<String, Object>  getAllCustomers(@RequestParam(defaultValue = "1") int page)
//    {
//        int pageSize = 10;
//        if (page < 1) {
//            throw new IllegalArgumentException("Page number must be 1 or greater.");
//        }
//        else
//        if(pageSize <1)
//        {
//            throw new IllegalArgumentException("Page size must be 1 or greater.");
//        }
//        int totalRecords = customerRepository.totalCustomers();
//        int totalPages = (totalRecords==0) ? 1:(int) Math.ceil((double)totalRecords / pageSize);
//
//
//        int offset=(page-1)*pageSize;
//        //Get all attributes from database of the customer
//        List<Customer>  customers=customerRepository.findAllPagewise(pageSize,offset);
//        Boolean  isLastPage=(page >= totalPages);
//        Map<String, Object> response = new HashMap<>();
//        response.put("customers",customers);
//        response.put("totalPages",totalPages);
//        response.put("currentPage",page);
//        response.put("isLastPage",isLastPage);
//        response.put("totalRecords",totalRecords);
//        System.out.println("response "+response);
//
//
//        return response;
//    }
}