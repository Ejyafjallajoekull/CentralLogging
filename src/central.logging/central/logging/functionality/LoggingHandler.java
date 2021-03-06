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
	
	/**
	 * The version number of this program.
	 */
	public static final String VERSION = "1.2.0.1";
	/**
	 * The centralised logger to be used.
	 */
	private static final Logger LOG = Logger.getLogger(LoggingHandler.class.getName());
	static { // disable logging to console via global logger
		LoggingHandler.LOG.setUseParentHandlers(false);
	}
	/**
	 * The file extension used for log files.
	 */
	public static final String LOG_FILE_EXTENSION = ".xml";
	private static Handler logHandler = null;
	private static File logFolder = new File("Logs"); // the folder containing all log files
	private static String logFileName = "Log";
	private static int numberLogFiles = 5;
	
	/**
	 * Start the log writing procedure.
	 */
	public static void startLogWriting() throws LoggingFailureException {
		if (LoggingHandler.logHandler == null) {
			if (!LoggingHandler.getLoggingFolder().exists()) {
				LoggingHandler.getLoggingFolder().mkdirs(); // create directory if necessary
			} else if (!LoggingHandler.getLoggingFolder().isDirectory()) {
				throw new LoggingFailureException("The specified logging folder exists, but is not a directory.");
			}
			// delete the oldest files
			File[] currentLogFiles = LoggingHandler.getLogFiles();
			for (int i = 0; i <= currentLogFiles.length - LoggingHandler.getNumberLogFiles(); i++) {
				if (!currentLogFiles[i].delete()) {
					LoggingHandler.getLog().warning("The old  log file " + currentLogFiles[i] + " could not be deleted.");
				}
			}
			try {
				LocalDateTime dateTimeNow = LocalDateTime.now();
				String startingTime = String.format("%d_%d_%d_%d_%d_%d_%d", dateTimeNow.getYear(), 
						dateTimeNow.getMonthValue(), dateTimeNow.getDayOfMonth(), dateTimeNow.getHour(), 
						dateTimeNow.getMinute(), dateTimeNow.getSecond(), dateTimeNow.getNano());
				LoggingHandler.logHandler = new FileHandler((LoggingHandler.logFolder.toPath().resolve(
						LoggingHandler.logFileName + "_" + startingTime 
						+ LoggingHandler.LOG_FILE_EXTENSION)).toString()); // always write to first log file
				LoggingHandler.getLog().addHandler(LoggingHandler.logHandler);
			} catch (SecurityException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Security problem accessing log file.", e);
				e.printStackTrace();
				throw new LoggingFailureException("Security problem accessing log file.", e);
			} catch (IOException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "The log file could not be written to.", e);
				e.printStackTrace();
				throw new LoggingFailureException("The log file could not be written to.", e);
			}
		} else {
			LoggingHandler.getLog().warning("Logging has already been started.");
		}
	}
	
	/**
	 * Stop the log writing procedure.
	 */
	public static void stopLogWriting() throws LoggingFailureException {
		if(LoggingHandler.logHandler != null) {
			try {
				LoggingHandler.logHandler.close();
				LoggingHandler.getLog().removeHandler(logHandler);
				LoggingHandler.logHandler = null;
			} catch (SecurityException e) { // false if logging could not be stopped
				e.printStackTrace();
				LoggingHandler.getLog().log(Level.WARNING, "Logging could not be stopped.", e);
				throw new LoggingFailureException( "Logging could not be stopped.", e);
			}
		} else {
			LoggingHandler.getLog().warning("No logging is currently performed "
					+ "and can thereby not be stopped.");
		}
	}

	/**
	 * Get all logging files for the current settings.
	 * 
	 * @return an array of all log files
	 */
	public static File[] getLogFiles() {
		// list all log files created with the current settings
		return LoggingHandler.getLoggingFolder().listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				String name = arg0.getName();
				return name.startsWith(LoggingHandler.logFileName) 
						&& name.endsWith(LoggingHandler.LOG_FILE_EXTENSION);
			}
		});
	}

	/**
	 * Get the current logger in order to access logging functionality.
	 * 
	 * @return the current Logger
	 */
	public static Logger getLog() {
		return LoggingHandler.LOG;
	}

	/**
	 * Get the folder where all logging files should be saved.
	 * 
	 * @return the logging folder as File
	 */
	public static File getLoggingFolder() {
		return LoggingHandler.logFolder;
	}

	/** 
	 * Set the folder to which all logging files should be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolder - the folder to save logging files to
	 * @throws IllegalArgumentException if the folder is null
	 */
	public static void setLoggingFolder(File logFolder) {
		if (logFolder != null) {
			LoggingHandler.logFolder = logFolder;
		} else {
			throw new IllegalArgumentException("The logging folder cannot be null.");
		}
	}
	
	/** 
	 * Set the folder to which all logging files should be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolderPath - the path to the folder to save logging files to
	 * @throws IllegalArgumentException if the folder path is null
	 */
	public static void setLoggingFolder(String logFolderPath) {
		if (logFolderPath != null) {
			LoggingHandler.logFolder = new File(logFolderPath);
		} else {
			throw new IllegalArgumentException("The logging folder cannot be null.");
		}
	}

	/**
	 * Get the base name of logging files.
	 * 
	 * @return the name of logging files
	 */
	public static String getLogFileName() {
		return LoggingHandler.logFileName;
	}

	/**
	 * Set the base name to use as template for all logging files.
	 * 
	 * @param logFileName - the name of the logging files
	 * @throws IllegalArgumentException if the log file name is null or empty
	 */
	public static void setLogFileName(String logFileName) {
		if (logFileName != null && logFileName.length() > 0) {
			LoggingHandler.logFileName = logFileName;
		} else {
			throw new IllegalArgumentException("The log file name cannot be null or empty.");
		}
	}

	/**
	 * Get the number of logging files that will be saved.
	 * If there are more files than specified, the oldest ones
	 * are deleted.
	 * 
	 * @return the number of logging files
	 */
	public static int getNumberLogFiles() {
		return LoggingHandler.numberLogFiles;
	}

	/**
	 * Set the number of logging files that will be saved before deleting
	 * the oldest.
	 * 
	 * @param numberLogFiles - the number of logging files to set
	 * @throws IllegalArgumentException if the number of log files is less than 1
	 */
	public static void setNumberLogFiles(int numberLogFiles) throws IllegalArgumentException {
		if (numberLogFiles > 0) {
			LoggingHandler.numberLogFiles = numberLogFiles;
		} else {
			throw new IllegalArgumentException("The number of log files cannot be zero or less.");
		}
	}
	
	/**
	 * Log the specified message and print it to the console. Depending on the log level the 
	 * message will be printed to err or out.
	 * 
	 * @param level - the log level
	 * @param msg - the message to log
	 */
	public static void logAndPrint(Level level, String msg) {
		LoggingHandler.getLog().log(level, msg);
		String formattedMsg = String.format("[%s] %s", level.getName(), msg);
		if (Level.SEVERE.equals(level) || Level.WARNING.equals(level)) {
			System.err.println(formattedMsg);
		} else {
			System.out.println(formattedMsg);
		}
	}
	
	/**
	 * Log the specified message and print it to the console. Depending on the log level the 
	 * message will be printed to err or out.
	 * 
	 * @param level - the log level
	 * @param msg - the message to log
	 * @param thrown - the exception raised
	 */
	public static void logAndPrint(Level level, String msg, Throwable thrown) {
		LoggingHandler.getLog().log(level, msg, thrown);
		String formattedMsg = String.format("[%s] %s", level.getName(), msg);
		if (Level.SEVERE.equals(level) || Level.WARNING.equals(level)) {
			System.err.println(formattedMsg);
		} else {
			System.out.println(formattedMsg);
		}
	}
	
}
