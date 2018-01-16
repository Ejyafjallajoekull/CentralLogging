package central.logging.functionality;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LoggingHandler class provides a centralised way of logging.
 * 
 * @author Planters
 *
 */
public class LoggingHandler {
	private static final Logger LOG = Logger.getLogger(LoggingHandler.class.getName());
	/**
	 * The file extension used for log files.
	 */
	public static final String LOG_FILE_EXTENSION = ".xml";
	private static Handler logHandler = null;
	private static File logFolder = new File("Logs"); // the folder containing all log files
	private static String logFileName = "Log";
	private static int numberLogFiles = 5;
	
	/*
	 * Start the log writing procedure.
	 */
	public static void startLogWriting() throws LoggingFailureException {
		if (logHandler == null) {
			if (!logFolder.exists()) {
				logFolder.mkdirs(); // create directory if necessary
			} else if (!logFolder.isDirectory()) {
				throw new LoggingFailureException("The specified logging folder exists, but is not a directory.");
			}
			// delete the oldest files
			File[] currentLogFiles = LoggingHandler.getLogFiles();
			for (int i = 0; i <= currentLogFiles.length - LoggingHandler.numberLogFiles; i++) {
				if (!currentLogFiles[i].delete()) {
					LoggingHandler.getLog().warning("The old  log file " + currentLogFiles[i] + " could not be deleted.");
				}
			}
			try {
				LocalDateTime dateTimeNow = LocalDateTime.now();
				String startingTime = String.format("%d_%d_%d_%d_%d_%d_%d", dateTimeNow.getYear(), 
						dateTimeNow.getMonthValue(), dateTimeNow.getDayOfMonth(), dateTimeNow.getHour(), 
						dateTimeNow.getMinute(), dateTimeNow.getSecond(), dateTimeNow.getNano());
				logHandler = new FileHandler((logFolder.toPath().resolve(logFileName + "_" 
						 + startingTime + LOG_FILE_EXTENSION)).toString()); // always write to first log file
				LOG.addHandler(logHandler);
			} catch (SecurityException e) {
				LOG.log(Level.SEVERE, "Security problem accessing log file.", e);
				e.printStackTrace();
				throw new LoggingFailureException("Security problem accessing log file.", e);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "The log file could not be written to.", e);
				e.printStackTrace();
				throw new LoggingFailureException("The log file could not be written to.", e);
			}
		} else {
			LOG.warning("Logging has already been started.");
		}
	}
	
	/**
	 * Stop the log writing procedure.
	 */
	public static void stopLogWriting() throws LoggingFailureException {
		if(logHandler != null) {
			try {
				logHandler.close();
				LOG.removeHandler(logHandler);
				logHandler = null;
			} catch (SecurityException e) { // false if logging could not be stopped
				e.printStackTrace();
				LOG.log(Level.WARNING, "Logging could not be stopped.", e);
				throw new LoggingFailureException( "Logging could not be stopped.", e);
			}
		} else {
			LOG.warning("No logging is currently performed and can thereby not be stopped.");
		}
	}

	/**
	 * Get all logging files for the current settings.
	 * 
	 * @return an array of all log files
	 */
	public static File[] getLogFiles() {
		// list all log files created with the current settings
		return logFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				String name = arg0.getName();
				return name.startsWith(logFileName) && name.endsWith(LOG_FILE_EXTENSION);
			}
		});
	}

	/**
	 * Get the current logger in order to access logging functionality.
	 * 
	 * @return the current Logger
	 */
	public static Logger getLog() {
		return LOG;
	}

	/**
	 * Get the folder where all logging files should be saved.
	 * 
	 * @return the logging folder as File
	 */
	public static File getLoggingFolder() {
		return logFolder;
	}

	/** 
	 * Set the folder to which all logging files should be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolder - the folder to save logging files to
	 */
	public static void setLoggingFolder(File logFolder) {
		if (logFolder != null) {
			LoggingHandler.logFolder = logFolder;
		} else {
			LoggingHandler.getLog().warning("The logging folder cannot be set to null.");
		}
	}
	
	/** 
	 * Set the folder to which all logging files should be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolderPath - the path to the folder to save logging files to
	 */
	public static void setLoggingFolder(String logFolderPath) {
		if (logFolderPath != null) {
			LoggingHandler.logFolder = new File(logFolderPath);
		} else {
			LoggingHandler.getLog().warning("The logging folder cannot be set to null.");
		}
	}

	/**
	 * Get the base name of logging files.
	 * 
	 * @return the name of logging files
	 */
	public static String getLogFileName() {
		return logFileName;
	}

	/**
	 * Set the base name to use as template for all logging files.
	 * 
	 * @param logFileName - the name of the logging files
	 */
	public static void setLogFileName(String logFileName) {
		if (logFileName != null && logFileName.length() > 0) {
			LoggingHandler.logFileName = logFileName;
		} else {
			LoggingHandler.getLog().warning("The logging file name cannot be set to null.");
		}
	}

	/**
	 * Get the number of logging files that will be saved.
	 * If there are more files than specified the oldest ones
	 * are deleted.
	 * 
	 * @return the number of logging files
	 */
	public static int getNumberLogFiles() {
		return numberLogFiles;
	}

	/**
	 * Set the number of logging files that will be saved before deleting
	 * the oldest.
	 * 
	 * @param numberLogFiles - the number of logging files to set
	 */
	public static void setNumberLogFiles(int numberLogFiles) {
		if (numberLogFiles > 0) {
			LoggingHandler.numberLogFiles = numberLogFiles;
		} else {
			LoggingHandler.getLog().warning("The number of logging files cannot be zero or less.");
		}
	}
}
