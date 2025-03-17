package com.turtle.portal.customer.service;
import com.turtle.portal.customer.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.turtle.portal.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class EntraIdUserAccess {
    @Autowired
    private CustomerRepository customerRepository;
    public  String entraIdconfig(String firstName,String lastName,String userMail,
                                       String contactNumber,String accountNumber)
    {
        try {
//            CustomerRepository customerRepository=new CustomerRepository();


            //Initialising credentials
            String tokenUrl="https://login.microsoftonline.com/48698181-4c0b-4789-ae7c-a3bbb389183b/oauth2/v2.0/token";
            System.out.println("tokenUrl:"+tokenUrl);
            String content_type = "application/x-www-form-urlencoded";
            String grant_type = "client_credentials";
            String client_id = "4f423205-366e-4fd8-8a56-bbcd5d054576";
            String client_secret = "Wyh8Q~jYM_TbkjShw3PE2Cbi_60I2ZuzvTL19aYB";
            String scope = "https://graph.microsoft.com/.default";
            String graph_url = "https://graph.microsoft.com/v1.0/";
            // String userMail="saleem.farook@d2railabs.com";

            // Mapping each body credentials as key-value pair
            Map<String, String> getTokenBody = new HashMap<>();
            getTokenBody.put("grant_type", grant_type);
            getTokenBody.put("client_id", client_id);
            getTokenBody.put("client_secret", client_secret);
            getTokenBody.put("scope", scope);

            String displayName=firstName+ " " +lastName;
            // Mapping each setJsonBody credentials as key-value pair
            Map<String,Object> setJsonBody=new HashMap<>();
            setJsonBody.put("accountEnabled",true);
            setJsonBody.put("displayName",displayName);
            setJsonBody.put("givenName",firstName);
//            setJsonBody.put("lastName",lastName);
            setJsonBody.put("mail",userMail);
            setJsonBody.put("mobilePhone",contactNumber);
//            setJsonBody.put("accountNumber",accountNumber);

            List<Map<String,Object>> identities=new ArrayList<>();
            Map<String,Object> identityObject = new HashMap<>();
            identityObject.put("signInType","emailAddress");
            identityObject.put("issuer","d2railabsltd.onmicrosoft.com");
            identityObject.put("issuerAssignedId",userMail);
            identities.add(identityObject);
            setJsonBody.put("identities",identities);
            Map<String,Object>  passwordProfile= new HashMap<>();
            setJsonBody.put("passwordProfile",passwordProfile);
            passwordProfile.put("password","TemporaryPass123!");
            passwordProfile.put("forceChangePasswordNextSignIn",true);
            setJsonBody.put("passwordPolicies","DisablePasswordExpiration");
            setJsonBody.put("extension_143a64bf7bfb44b8aeeab6fd2cba1361_Role", "Admin");
            setJsonBody.put("extension_143a64bf7bfb44b8aeeab6fd2cba1361_accountNumber",accountNumber);
            //Passing the arguments to the getAccessToken method
            String tokenJson = getAccessToken(tokenUrl, content_type, getTokenBody);
            //Post-processing token
            ObjectMapper objectMapper =new ObjectMapper();
            JsonNode JsonNode= objectMapper.readTree(tokenJson);
            String token=JsonNode.get("access_token").asText();
            String user=getUser(token,graph_url);
            String checkstatus=Usercheck(user,userMail);
            JsonNode JsonBody=objectMapper.valueToTree(setJsonBody);
            if (checkstatus.equals("false"))
            {
            try {
                System.out.println("Creating user in Entra Id:    "+userMail);
                String createUser = setUser(token, graph_url, JsonBody);
                System.out.println("User created:   " + createUser);
                Customer cus=customerRepository.findByEmail(userMail);
                 if (cus!=null)
                 {
                     cus.setApprove(true);
                     cus.setPending(false);
                     cus.setReject(false);
                     customerRepository.updateApprovalStatus(cus);
                     System.out.println("User approved:   " + cus.getFirstName());
                     return "User created in EntraId and approved";
                 }
                 else
                 {
                     System.out.println("User not found in database:   " + userMail);
                     return "User not found in database";
                 }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            }
            else
            {
                return "User already exists in Entra Id";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static String getAccessToken(String tokenUrl, String content_type, Map<String, String> body)
    {
        //Getting token by passing tokenUrl,headers,params
        //Referencing Client
        try
        {
            //Creating client
            HttpClient client = HttpClient.newHttpClient();
            // Convert map to form-urlencoded string
            // collectors are used to  delimit each key-value pair
            String requestBody = body.entrySet().stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            // Building request
            HttpRequest requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", content_type)
                    .build();

            //Sending the request
            HttpResponse<String> response = client.send(requestBuilder, HttpResponse.BodyHandlers.ofString());

            // Print the response details
            System.out.println("Response Code: " + response.statusCode());
//            System.out.println("Response Body: " + response.body());
            return response.body();
        }
        catch (Exception e)
        {
            System.out.println("Detailed error:" + e.getMessage());
            e.printStackTrace();

        }
        return "An unexpected error occurred. Please try again later.";
    }

    public static String getUser(String token, String graph_url)
    {
        try
        {
            //Creating client
            HttpClient client = HttpClient.newHttpClient();
            String selectedFields = "id,displayName,givenName,mail,mobilePhone,otherMails,identities,extension_143a64bf7bfb44b8aeeab6fd2cba1361_Role,extension_143a64bf7bfb44b8aeeab6fd2cba1361_accountNumber";
            String params = selectedFields.isEmpty() ? "" : "?$select=" + URLEncoder.encode(selectedFields, StandardCharsets.UTF_8);
            String url = graph_url + "users" + params;
            // Building request
            HttpRequest requestBuilder = HttpRequest.newBuilder(URI.create(url))
                    .header("Authorization", "Bearer" + token)
                    .GET()
                    .build();
            //Sending the request
            HttpResponse<String> response = client.send(requestBuilder, HttpResponse.BodyHandlers.ofString());
            // Print the response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response.body();
        }
        catch (Exception e)
        {
            System.out.println("Detailed error:" + e.getMessage());
            e.printStackTrace();
        }
        return "An unexpected error occurred. Please try again later.";

    }
    public static  String Usercheck(String user,String userMail)
    {
        try
        {   //objectMapper intialisation
            ObjectMapper objectMapper =new ObjectMapper();
            //creating Node
            JsonNode userNode =objectMapper.readTree(user);
            JsonNode valueNode=userNode.path("value");
            for ( JsonNode value:valueNode)
            {
                String mail=value.path("mail").asText();
                JsonNode othermails=value.path("othermails");
                JsonNode identities=value.path("identities");
                if (mail.equals(userMail))
                {
                    System.out.println("User checked and already exists in Entra Id:    "+mail);
                    return user;
                }
                else if (Arrays.asList(othermails).contains(userMail))
                {
                    return user;

                }
                else
                {
                    for( JsonNode identity:identities)
                    {
                        String signInType=identity.path("signInType").asText();
                        String issuerAssignedId=identity.path("issuerAssignedId").asText();
                        if (signInType.equals("emailAddress") && issuerAssignedId.equals(userMail))
                        {
                            System.out.println("User checked and already exists in Entra Id:    "+mail);
                            return user;
                        }
                    }
                }
            }
            System.out.println("User checked and doesn't exists in Entra Id:    "+userMail);
            return "false";
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }



    public static String  setUser(String token,String graph_url,JsonNode setJsonBody)
    {
        try
        {
            //Creating client
            HttpClient client=HttpClient.newHttpClient();
            // Cast setJsonBody from JsonNode to String
            ObjectMapper objectMapper=new ObjectMapper();
            String body=objectMapper.writeValueAsString(setJsonBody);

            String url=graph_url+"users";
            // Building request
            HttpRequest requestBuilder= HttpRequest.newBuilder(URI.create(url))
                    .header("Authorization","Bearer"+token)
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body,StandardCharsets.UTF_8))
                    .build();
            //Sending the request
            HttpResponse<String> response = client.send(requestBuilder, HttpResponse.BodyHandlers.ofString());
            // Print the response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response.body();


        }
        catch (Exception e)
        {
            System.out.println("Detailed error:" + e.getMessage());
            e.printStackTrace();
        }
        return "An unexpected error occurred. Please try again later.";
    }
}