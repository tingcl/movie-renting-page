package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.BillingService;
import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.*;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class BillingRecords {

    public static boolean insertedDuplicate(InsertRequestModel requestModel){
        try{
            String query = "SELECT id FROM carts WHERE email LIKE ? AND movieId LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getEmail());
            ps.setString(2, requestModel.getMovieId());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ServiceLogger.LOGGER.info("Inserted duplicated cart item.");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not query 'CARTS' table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("No duplicate items.");
        return false;
    }

    public static boolean insertIntoCart(InsertRequestModel requestModel){
        try{
            String query = "INSERT INTO carts (email, movieId, quantity) VALUES (?, ?, ?);";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getEmail());
            ps.setString(2, requestModel.getMovieId());
            ps.setInt(3, requestModel.getQuantity());
            ServiceLogger.LOGGER.warning("Preparing to insert: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.warning("Inserted unique cart item.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into 'CARTS' table...");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCart(InsertRequestModel requestModel){
        try{
            String query = "UPDATE carts SET quantity = ? WHERE email LIKE ? AND movieId like ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setInt(1, requestModel.getQuantity());
            ps.setString(2, requestModel.getEmail());
            ps.setString(3, requestModel.getMovieId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Update successful.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not update 'CARTS' table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("Update failed.");
        return false;
    }

    public static boolean itemExist(DeleteRequestModel rm){
        try{
            String query = "SELECT id FROM carts WHERE email LIKE ? AND movieId LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(2, rm.getMovieId());
            ps.setString(1,rm.getEmail());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ServiceLogger.LOGGER.info("Item exists cart item.");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not query 'CARTS' table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Item does not exist.");
        return false;
    }

    public static boolean deleteCartItem(DeleteRequestModel rm){
        try {
            String query = "DELETE FROM carts WHERE email = ? AND movieId = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, rm.getEmail());
            ps.setString(2, rm.getMovieId());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Deleted item successfully.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not delete 'CARTS' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could not delete item...");
        return false;
    }

    public static boolean emailHasCartItems(String email){
        try{
            String query = "SELECT id FROM carts WHERE email LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ServiceLogger.LOGGER.info("User cart has items");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not query 'CARTS' table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("User cart has no items.");
        return false;
    }

    public static CartModel[] listUserCart(String email){
        try{
            String query = "SELECT email, movieId, quantity FROM carts WHERE email LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<CartModel> list = new ArrayList<>();
            while(rs.next()){
                CartModel item = new CartModel(
                        rs.getString("email"),
                        rs.getString("movieId"),
                        rs.getInt("quantity")
                );
                ServiceLogger.LOGGER.info("Retrieved record " + item);
                list.add(item);
            }
            CartModel[] cart = list.toArray(new CartModel[list.size()]);
            return cart;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could retrieve user cart...");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean clearCart(String email) {
        try {
            String query = "DELETE FROM carts WHERE email LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Deleted items successfully.");
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not clear...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could not clear...");
        return false;
    }

    public static boolean duplicateCardEntry(String id){
        try {
            String query = "SELECT id FROM creditcards WHERE id LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ServiceLogger.LOGGER.info("Duplicate credit card entry.");
                return true;
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not detect...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("No duplicate credit card entry.");
        return false;
    }

    public static boolean enterCard(InsertCardRequestModel requestModel){
        try{
            String query = "INSERT INTO creditcards (id, firstName, lastName, expiration) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getId());
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setDate(4, new java.sql.Date(requestModel.getExpiration().getTime()));
            ServiceLogger.LOGGER.warning("Preparing to insert: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.warning("Inserted unique card item.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into 'creditcards' table...");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCard(InsertCardRequestModel requestModel){
        try{
            String query = "UPDATE creditcards SET firstName = ?, lastName = ?, expiration = ? " +
                    "WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            java.sql.Date sqlDate = new java.sql.Date(requestModel.getExpiration().getTime());
            ps.setDate(3, sqlDate);
            ps.setString(4, requestModel.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Updated card successfully.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not update 'CREDITCARDS' table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("Update failed.");
        return false;
    }

    public static boolean deleteCard(DeleteCardRequestModel requestModel){
        try {
            String query = "DELETE FROM creditcards WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Deleted card successfully.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not delete 'creditcards' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could not delete card...");
        return false;
    }

    public static CardModel getCardInfo(DeleteCardRequestModel requestModel){
        try {
            String query = "SELECT * FROM creditcards WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ServiceLogger.LOGGER.info("Found card information.");
                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //String expiration = dateFormat.format(rs.getDate("expiration"));
                CardModel info = new CardModel(
                        rs.getString("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("expiration")
                );
                return info;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not find 'creditcards' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could find card...");
        return null;
    }

    public static boolean customerAlready(String email){
        try {
            String query = "SELECT email FROM customers WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ServiceLogger.LOGGER.warning("Duplicate insertion customer...");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not find 'customer' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("New customer...");
        return false;
    }

    public static boolean insertCustomer(InsertCustomerRequestModel requestModel){
        try{
            String query = "INSERT INTO customers (email, firstName, lastName, ccId, address) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setString(4, requestModel.getCcId());
            ps.setString(5, requestModel.getAddress());
            ServiceLogger.LOGGER.info("Preparing to insert: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Inserted customer card item.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into customer table...");
            e.printStackTrace();
        }

        ServiceLogger.LOGGER.warning("Failed to insert");

        return false;
    }

    public static boolean updateCustomer(InsertCustomerRequestModel requestModel){
        try{
            String query = "UPDATE customers SET firstName = ?, lastName = ?, ccId = ?, address = ? " +
                    "WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            ps.setString(3, requestModel.getCcId());
            ps.setString(4, requestModel.getAddress());
            ps.setString(5, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Preparing to insert: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Updated customer info.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into customer table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Failed to update");
        return false;
    }
    public static CustomerModel getCustomerInfo(RetrieveCustomerRequestModel requestModel) {
        try {
            String query = "SELECT * FROM customers WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ServiceLogger.LOGGER.info("Found customer information.");
                CustomerModel c = new CustomerModel(
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("ccId"),
                        rs.getString("address")
                );
                return c;
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not find 'creditcards' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could find card...");
        return null;
    }
    public static boolean clearUserCart(String email){
        try {
            String query = "DELETE FROM carts WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Deleted all of cart successfully.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not delete 'CARTS' table item...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could not delete cart...");
        return false;
    }

    public static boolean addToSalesAndTransactions(CartModel item, String token){
        try{
            CallableStatement cs = BillingService.getCon().prepareCall(
                    "{call insert_sales_transactions(?, ?, ?, ?, ?, ?)}");
            cs.setString(1, item.getEmail());
            cs.setString(2, item.getMovieId());
            cs.setInt(3, item.getQuantity());
            Date date = new Date(System.currentTimeMillis());
            cs.setDate(4, date);
            cs.setInt(5, getCurrentSId() + 1);
            cs.setString(6, token);
            ServiceLogger.LOGGER.info("Preparing to insert: " + cs.toString());
            cs.execute();
            ServiceLogger.LOGGER.info("table insertion true.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into Sales and Transactions tables...");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkoutCart(OrderRequestModel requestModel, String token){
        CartModel[] items = listUserCart(requestModel.getEmail());
        for(int i = 0; i < items.length; i++){
            addToSalesAndTransactions(items[i], token);
        }
        clearUserCart(requestModel.getEmail());
        return true;
    }

    public static BillingModel[] listUserOrders(String email, String transactionId){
        try{
            String query = "SELECT email, sales.movieId, quantity, saleDate, unit_price, discount FROM sales, transactions, movie_prices WHERE email " +
                    "LIKE ? AND transactionId = ? AND sales.id = transactions.sId AND sales.movieId = movie_prices.movieId;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, transactionId);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<BillingModel> list = new ArrayList<>();
            while(rs.next()){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(rs.getDate("saleDate"));
                BillingModel item = new BillingModel(
                        rs.getString("email"),
                        rs.getString("movieId"),
                        rs.getInt("quantity"),
                        rs.getFloat("unit_price"),
                        rs.getFloat("discount"),
                        date
                );
                ServiceLogger.LOGGER.info("Retrieved order " + item);
                list.add(item);
            }
            BillingModel[] items = list.toArray(new BillingModel[list.size()]);
            return items;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could retrieve user cart...");
            e.printStackTrace();
        }
        return null;
    }
    public static float getSum(String email){
        float sum = 0;
        try{
            String query = "SELECT quantity, unit_price, discount FROM carts, movie_prices WHERE " +
                    "carts.movieId = movie_prices.movieId AND carts.email LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int quantity = rs.getInt("quantity");
                float unit_price = rs.getFloat("unit_price");
                float discount = rs.getFloat("discount");

                ServiceLogger.LOGGER.info("quantity: " + quantity + ", unit_price: " + unit_price +
                        ", discount: " + discount);

                sum += (unit_price * discount *quantity);
                ServiceLogger.LOGGER.info("total price: " + sum);

            }
            BigDecimal bd = new BigDecimal(Float.toString(sum));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            sum = bd.floatValue();
            ServiceLogger.LOGGER.info("total price: " + sum);
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could retrieve total sum of cart items... this is bad!");
            e.printStackTrace();
        }
        // No sum will ever be 0
        return sum;
    }
    public static int getCurrentSId(){
        // No sales thus current sId is 0
        int sId = 0;
        try{
            String query = "SELECT id FROM sales ORDER BY id DESC;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                sId = rs.getInt("id");
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could retrieve total sum of cart items...");
            e.printStackTrace();
        }
        return sId;
    }
    public static boolean tokenFound(String token){
        try{
            String query = "SELECT token FROM transactions WHERE token LIKE ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, token);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ServiceLogger.LOGGER.info("Valid token in transactions table.");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could retrieve total sum of cart items...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("Invalid token not in transactions table.");
        return false;
    }
    public static boolean updateTransactionId(String transactionId, String token){
        try{
            String query = "UPDATE transactions SET transactionId = ? WHERE token = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, transactionId);
            ps.setString(2, token);
            ServiceLogger.LOGGER.info("Preparing to insert: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Updated customer info.");
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into customer table...");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Failed to update");
        return false;
    }
    public static List<String> getTransactionId(String email){
        List<String> transactionIds = new ArrayList<>();
        try{
            String query = "select distinct transactionId from sales, transactions where sales.id = transactions.sId " +
                    "and email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Preparing to execute: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String Id = rs.getString("transactionId");
                transactionIds.add(Id);
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not insert into customer table...");
            e.printStackTrace();
        }
        return transactionIds;
    }
}
