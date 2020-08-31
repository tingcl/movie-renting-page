package edu.uci.ics.tingcl2.service.api_gateway.core;

import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.gateway.GatewayReportResponseModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    // Singular insert in this endpoint to insert into responses table
    public static void insertResponse(String transactionid, String email, String sessionid, String response, int httpstatus){
        try{
            String query = "INSERT responses(transactionid, email, sessionid, response, httpstatus) " +
                    "VALUES (?, ?, ?, ?, ?);";
            ServiceLogger.LOGGER.info("Calling connection...");
            Connection con = GatewayService.getConPool().requestCon();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,transactionid);
            ps.setString(2, email);
            ps.setString(3, sessionid);
            ps.setString(4, response);
            ps.setInt(5, httpstatus);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query successful.");
            ServiceLogger.LOGGER.info("Releasing connection...");
            GatewayService.getConPool().releaseCon(con);
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Exception thrown: Query failed.");
            e.printStackTrace();
        }
    }
    public static GatewayReportResponseModel requestGatewayReport(String transactionid){
        // Response to return
        Connection con = GatewayService.getConPool().requestCon();
        try{
            String query = "SELECT response, httpstatus FROM responses WHERE transactionid = ?;";
            ServiceLogger.LOGGER.info("Calling connection...");
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,transactionid);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ServiceLogger.LOGGER.info("Found transactionid");
                GatewayService.getConPool().releaseCon(con);
                return new GatewayReportResponseModel(rs.getString("response"), rs.getInt("httpstatus"));
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Exception thrown: Query failed.");
            e.printStackTrace();
        }
        GatewayService.getConPool().releaseCon(con);
        return null;
    }
    public static void removeCompletedRequest(String transactionID){
        try{
            String query = "DELETE FROM responses WHERE transactionID = ?";
            ServiceLogger.LOGGER.info("Calling connection...");
            Connection con = GatewayService.getConPool().requestCon();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, transactionID);
            ServiceLogger.LOGGER.info("Preparing to query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Releasing connection...");
            GatewayService.getConPool().releaseCon(con);
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Exception thrown: Query failed.");
            e.printStackTrace();
        }
    }
}
