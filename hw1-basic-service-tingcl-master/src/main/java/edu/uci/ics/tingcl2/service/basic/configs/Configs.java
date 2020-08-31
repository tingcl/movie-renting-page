package edu.uci.ics.tingcl2.service.basic.configs;

import edu.uci.ics.tingcl2.service.basic.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.basic.models.ConfigsModel;

public class Configs {
    // Default service configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 8216;
    private final String DEFAULT_PATH = "/api/basicService";

    // Default logger configs
    private final String DEFAULT_OUTPUTDIR = "./logs/";
    private final String DEFAULT_OUTPUTFILE = "basicService.log";

    // Null database configs
    private final String DEFAULT_DBUSERNAME = null;
    private final String DEFAULT_DBPASSWORD = null;
    private final String DEFAULT_DBHOSTNAME = null;
    private final int    DEFAULT_DBPORT = 0;
    private final String DEFAULT_DBDRIVER = null;
    private final String DEFAULT_DBNAME = null;
    private final String DEFAULT_DBSETTINGS = null;

    // Service configs
    private String scheme;
    private String hostName;
    private int    port;
    private String path;

    // Logger configs
    private String outputDir;
    private String outputFile;

    // Database configs
    private String dbUsername;
    private String dbPassword;
    private String dbHostname;
    private int    dbPort;
    private String dbDriver;
    private String dbName;
    private String dbSettings;


    public Configs() {
        scheme = DEFAULT_SCHEME;
        hostName = DEFAULT_HOSTNAME;
        port = DEFAULT_PORT;
        path = DEFAULT_PATH;
        outputDir = DEFAULT_OUTPUTDIR;
        outputFile = DEFAULT_OUTPUTFILE;
        dbUsername = DEFAULT_DBUSERNAME;
        dbPassword = DEFAULT_DBPASSWORD;
        dbHostname = DEFAULT_DBHOSTNAME;
        dbPort =  DEFAULT_DBPORT;
        dbDriver = DEFAULT_DBDRIVER;
        dbName = DEFAULT_DBNAME;
        dbSettings = DEFAULT_DBSETTINGS;
    }



    public Configs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            throw new NullPointerException("Unable to create Configs from ConfigsModel.");
        } else {
            // Set service configs
            scheme = cm.getServiceConfig().get("scheme");
            if (scheme == null) {
                scheme = DEFAULT_SCHEME;
                System.err.println("Scheme not found in configuration file. Using default.");
            } else {
                System.err.println("Scheme: " + scheme);
            }

            hostName = cm.getServiceConfig().get("hostName");
            if (hostName == null) {
                hostName = DEFAULT_HOSTNAME;
                System.err.println("Hostname not found in configuration file. Using default.");
            } else {
                System.err.println("Hostname: " + hostName);
            }

            port = Integer.parseInt(cm.getServiceConfig().get("port"));
            if (port == 0) {
                port = DEFAULT_PORT;
                System.err.println("Port not found in configuration file. Using default.");
            } else if (port < 1024 || port > 65536) {
                port = DEFAULT_PORT;
                System.err.println("Port is not within valid range. Using default.");
            } else {
                System.err.println("Port: " + port);
            }

            path = cm.getServiceConfig().get("path");
            if (path == null) {
                path = DEFAULT_PATH;
                System.err.println("Path not found in configuration file. Using default.");
            } else {
                System.err.println("Path: " + path);
            }

            // Set logger configs
            outputDir = cm.getLoggerConfig().get("outputDir");
            if (outputDir == null) {
                outputDir = DEFAULT_OUTPUTDIR;
                System.err.println("Logging output directory not found in configuration file. Using default.");
            } else {
                System.err.println("Logging output directory: " + outputDir);
            }

            outputFile = cm.getLoggerConfig().get("outputFile");
            if (outputFile == null) {
                outputFile = DEFAULT_OUTPUTFILE;
                System.err.println("Logging output file not found in configuration file. Using default.");
            } else {
                System.err.println("Logging output file: " + outputFile);
            }

            // Set database configs
            dbUsername = cm.getDatabaseConfig().get("dbUsername");
            if (dbUsername == null) {
                dbUsername = DEFAULT_DBUSERNAME;
                System.err.println("Database username not found in configuration file. Using default.");
            } else {
                System.err.println("Database username: " + dbUsername);
            }

            dbPassword = cm.getDatabaseConfig().get("dbPassword");
            if (dbPassword == null) {
                dbPassword = DEFAULT_DBPASSWORD;
                System.err.println("Database password not found in configuration file. Using default.");
            } else {
                System.err.println("Database password found in configuration file.");

            }

            dbHostname = cm.getDatabaseConfig().get("dbHostname");
            if (dbHostname == null) {
                dbHostname = DEFAULT_DBHOSTNAME;
                System.err.println("Database hostname not found in configuration file. Using default.");
            } else {
                System.err.println("Database hostname: " + dbHostname);
            }

            dbPort = Integer.parseInt(cm.getDatabaseConfig().get("dbPort"));
            if (dbPort == 0) {
                dbPort = DEFAULT_DBPORT;
                System.err.println("Database port not found in configuration file. Using default.");
            } else if (dbPort < 1024 || dbPort > 65536) {
                dbPort = DEFAULT_DBPORT;
                System.err.println("Database port is not within valid range. Using default.");
            } else {
                System.err.println("Database port: " + dbPort);
            }

            dbDriver = cm.getDatabaseConfig().get("dbDriver");
            if (dbHostname == null) {
                dbHostname = DEFAULT_DBDRIVER;
                System.err.println("Database driver not found in configuration file. Using default.");
            } else {
                System.err.println("Database driver: " + dbDriver);
            }

            dbName = cm.getDatabaseConfig().get("dbName");
            if (dbName == null) {
                dbName = DEFAULT_DBNAME;
                System.err.println("Database name not found in configuration file. Using default.");
            } else {
                System.err.println("Database name: " + dbName);
            }

            dbSettings = cm.getDatabaseConfig().get("dbSettings");
            if (dbSettings == null) {
                dbSettings = DEFAULT_DBSETTINGS;
                System.err.println("Database settings not found in configuration file. Using default.");
            }
        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Port: " + port);
        ServiceLogger.LOGGER.config("Path: " + path);
        ServiceLogger.LOGGER.config("Logger output directory: " + outputDir);
        ServiceLogger.LOGGER.config("Logger output file: " + outputFile);
        ServiceLogger.LOGGER.config("Database username: " + dbUsername);
        ServiceLogger.LOGGER.config("Database password provided? " + ((dbPassword != null) ? "true" : "false") + " " + dbPassword);
        ServiceLogger.LOGGER.config("Database hostname: " + dbHostname);
        ServiceLogger.LOGGER.config("Database port: " + dbPort);
        ServiceLogger.LOGGER.config("Database driver: " + dbDriver);
        ServiceLogger.LOGGER.config("Database name: " + dbName);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getDatabaseUsername() {
        return dbUsername;
    }

    public String getDatabasePassword() {
        return dbPassword;
    }

    public String getDatabaseHostname() {
        return dbHostname;
    }

    public int getDatabasePort() {
        return dbPort;
    }

    public String getDatabaseDriver() {
        return dbDriver;
    }

    public String getDatabaseName() {
        return dbName;
    }

    public String getDatabaseSettings() {
        return dbSettings;
    }
}