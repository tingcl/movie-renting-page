package edu.uci.ics.tingcl2.service.basic.core;

import edu.uci.ics.tingcl2.service.basic.BasicService;
import edu.uci.ics.tingcl2.service.basic.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.basic.models.ValidateStringRequestModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StringRecords {
    public static boolean insertSentenceToDB(ValidateStringRequestModel requestModel) {
        try {
            // Construct the query
            String query =
                    "INSERT INTO valid_strings (sentence, length) VALUES (?, ?);";
            PreparedStatement ps = BasicService.getCon().prepareStatement(query);
            // Set the parameters
            ps.setString(1, requestModel.getInput());
            ps.setFloat(2, requestModel.getLen());
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to insert sentence " + requestModel.getInput());
            e.printStackTrace();
        }
        return false;
    }

    public static int retrieveRecordCount() {
        try {
            // Construct the query
            String query = "SELECT count(*) FROM valid_strings;";
            // Create the prepared statement
            PreparedStatement ps = BasicService.getCon().prepareStatement(query);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return rs.getInt("count(*)");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve number of string records.");
            e.printStackTrace();
            return -1;
        }

    }

    public static Record retrieveRecord(int id){
        try {
            // Construct the query
            String query = "SELECT id, sentence, length FROM valid_strings WHERE id LIKE ?;";
            // Create the prepared statement
            PreparedStatement ps = BasicService.getCon().prepareStatement(query);
            // Set arguments
            ps.setInt(1, id);
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            rs.next();

            Record record = new Record(
                    rs.getInt("id"),
                    rs.getString("sentence"),
                    rs.getInt("length")
            );

            ServiceLogger.LOGGER.info("Retrieved record " + record);
            return record;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve number of string records.");
            e.printStackTrace();
        }
        return null;
    }
}



