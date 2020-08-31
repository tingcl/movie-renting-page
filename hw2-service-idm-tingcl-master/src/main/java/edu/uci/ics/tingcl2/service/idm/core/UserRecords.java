package edu.uci.ics.tingcl2.service.idm.core;

import edu.uci.ics.tingcl2.service.idm.IDMService;
import edu.uci.ics.tingcl2.service.idm.configs.Configs;
import edu.uci.ics.tingcl2.service.idm.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.tingcl2.service.idm.security.Session;
import edu.uci.ics.tingcl2.service.idm.security.Token;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/*
 * IDM Database
 *
 * Includes are required functionality to check, insert, update data in IDM database
 */

public class UserRecords {
    public static final int MIN_PLEVEL = 2;
    public static final int MAX_PLEVEL = 5;
    public static final int PLEVEL_ADMIN = 2;
    public static final int PLEVEL_EMPLOYEE = 3;
    public static final int PLEVEL_SERVICE = 4;
    public static final int PLEVEL_USER = 5;
    public static final int PLEVEL_DEFAULT = PLEVEL_USER;

    public static final int MIN_USER_STATUS = 1;
    public static final int MAX_USER_STATUS = 4;
    public static final int USER_STATUS_ACTIVE = 1;
    public static final int USER_STATUS_CLOSED = 2;
    public static final int USER_STATUS_EXPIRED = 3;
    public static final int USER_STATUS_REVOKED = 4;
    public static final int USER_STATUS_DEFAULT = USER_STATUS_ACTIVE;

    public static boolean emailExists(String email){
        try{
            // Constructing email query
            String query = "SELECT email FROM users WHERE email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ServiceLogger.LOGGER.info("User email exists in database.");
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: exception occurred when querying database.");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("User email does not exist in database.");
        return false;
    }

    public static String getPassword(String email){
        // Validation checks should ensure password is never null
        String str = null;
        try {
            // Constructing query
            String query = "SELECT pword FROM users WHERE email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                str = rs.getString("pword");
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify email existence.");
            e.printStackTrace();
        }
        return str;
    }
    public static byte[] getSalt(String email){
        // Validation checks should ensure salt is never null
        byte[] salt = null;
        try {
            // Constructing query
            String query = "SELECT salt FROM users WHERE email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                salt = rs.getBytes("salt");
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify email existence.");
            e.printStackTrace();
        }
        return salt;
    }

    public static boolean addUser(RegisterRequestModel requestModel, byte[] salt, String password){
        ServiceLogger.LOGGER.info("Inserting valid user into database...");
        try{
            // Construct the query
            String query = "INSERT INTO users (email, status, plevel, salt, pword) VALUES (?, ?, ?, ?, ?);";

            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setString(1, requestModel.getEmail());
            ps.setInt(2, USER_STATUS_DEFAULT);
            ps.setInt(3, PLEVEL_DEFAULT);
            ps.setBytes(4, salt);
            ps.setString(5, password);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to insert user " + requestModel.getEmail());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean existSession(String email){
        ServiceLogger.LOGGER.info("Checking if session already exists");
        try{
            // Constructing email query
            String query = "SELECT * FROM sessions WHERE email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify email.");
            e.printStackTrace();
        }
        return false;
    }
    // create new active session
    public static boolean addSession(Session session){
        ServiceLogger.LOGGER.info("Adding new session into database");
        try{
            // Constructing email query
            String query = "INSERT INTO sessions (sessionID, email, status, timeCreated," +
                    "lastUsed, exprTime) VALUES (?, ?, ?, ?, ?, ?);";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setString(1, session.getSessionID().toString());
            ps.setString(2, session.getEmail());
            ps.setInt(3, USER_STATUS_DEFAULT);
            ps.setTimestamp(4, session.getTimeCreated());
            ps.setTimestamp(5, session.getLastUsed());
            ps.setTimestamp(6, session.getExprTime());
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            return true;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify email.");
            e.printStackTrace();
        }
        return false;
    }
    public static void revokeSessions(String email){
        try {
            ServiceLogger.LOGGER.info("Revoking existing sessions.");
            // Constructing email query
            String query = "UPDATE sessions SET status = ? WHERE email LIKE ? AND status like ? ";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setInt(1, USER_STATUS_REVOKED);
            ps.setString(2, email);
            ps.setInt(3, USER_STATUS_ACTIVE);
            // Execute query pray this works
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify email.");
            e.printStackTrace();
        }
    }
    public static int activeSessionUpdate(String sessionID){
        try {
            ServiceLogger.LOGGER.info("Retrieving active session");
            // Constructing session ID query
            String query = "SELECT * FROM sessions WHERE sessionID LIKE ? AND status like ? ";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setString(1, sessionID);
            ps.setInt(2, USER_STATUS_ACTIVE);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            Token token;
            Session session;
            if (rs.next()) {
                 ServiceLogger.LOGGER.info("Found active sessionID");
                 Configs configs = new Configs();
                 token = Token.rebuildToken(rs.getString("sessionID"));
                 session = Session.rebuildSession(
                        rs.getString("email"),
                        token,
                        rs.getTimestamp("timeCreated"),
                        rs.getTimestamp("lastUsed"),
                        rs.getTimestamp("exprTime")

                );
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                if((currentTime.getTime() - session.getLastUsed().getTime()) > Long.parseLong(configs.getTimeout())) {
                    revokeSessionsID(sessionID);
                    return 133;
                }
                if((currentTime.getTime() - session.getTimeCreated().getTime()) >= Long.parseLong(configs.getExpiration())){
                    expireSessionsID(sessionID);
                    return 131;
                }
                session.update();
                return 130;
            }
            return 134;
        }
        catch(SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to verify session status.");
            e.printStackTrace();
        }
        return 0;
    }
    public static void revokeSessionsID(String sessionID){
        try {
            ServiceLogger.LOGGER.info("Revoking existing sessions.");
            // Constructing email query
            String query = "UPDATE sessions SET status = ? WHERE sessionID LIKE ? AND status like ? ";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setInt(1, USER_STATUS_REVOKED);
            ps.setString(2, sessionID);
            ps.setInt(3, USER_STATUS_ACTIVE);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to revoke.");
            e.printStackTrace();
        }
    }
    public static void expireSessionsID(String sessionId){
        try {
            ServiceLogger.LOGGER.info("Revoking existing sessions.");
            // Constructing email query
            String query = "UPDATE sessions SET status = ? WHERE sessionID LIKE ? AND status like ?;";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setInt(1, USER_STATUS_EXPIRED);
            ps.setString(2, sessionId);
            ps.setInt(3, USER_STATUS_ACTIVE);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to expire.");
            e.printStackTrace();
        }
    }
    public static int getPlevel(String email){
        try {
            ServiceLogger.LOGGER.info("Getting plevel associated with email");
            // Constructing email query
            String query = "SELECT plevel FROM users WHERE email = ?";
            // Create the prepared statement
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setString(1, email);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("plevel");
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to expire.");
            e.printStackTrace();
        }
        return 0;
    }
    public static int returnSessionStatus(String sessionID){
        try{
            String query = "SELECT status FROM sessions WHERE sessionID LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, sessionID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("status");
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to expire.");
            e.printStackTrace();
        }
        return 0;
    }



}


