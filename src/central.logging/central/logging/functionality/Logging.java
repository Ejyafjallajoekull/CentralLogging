package central.logging.functionality;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Logging class provides a centralised way of logging while not being restricted to 
 * a single static logger.
 * 
 * @author Planters
 *
 */
public class Logging {

	/**
	 * An incremental suffix for the logger names.
	 */
	private static long logIdentifier = Long.MIN_VALUE;
	
	/**
	 * The centralised logger to be used.
	 */
	private final Logger log = Logger.getLogger(Logging.class.getName() + "." + Logging.logIdentifier++);

	private Handler logHandler = null;
	private File logFolder = new File("Logs"); // the folder containing all log files
	private String logFileName = "Log";
	private int numberLogFiles = 5;
	
	/**
	 * Create a Logging facility with default values.
	 */
	public Logging() {
		// disable logging to console via global logger
		this.getLog().setUseParentHandlers(false);
	}
	
	/**
	 * Create a logging facility with the specified logging folder and log file name.
	 * 
	 * @param loggingFolder - the folder to write the log files to
	 * @param logFileName - the basic filename for all the log files
	 */
	public Logging(File loggingFolder, String logFileName) {
		this.setLoggingFolder(loggingFolder);
		this.setLogFileName(logFileName);
		// disable logging to console via global logger
		this.getLog().setUseParentHandlers(false);
	}
	
	/**
	 * Start the log writing procedure.
	 */
	public void startLogWriting() throws LoggingFailureException {
		if (this.logHandler == null) {
			if (!this.getLoggingFolder().exists()) {
				this.getLoggingFolder().mkdirs(); // create directory if necessary
			} else if (!this.getLoggingFolder().isDirectory()) {
				throw new LoggingFailureException("The specified logging folder exists, but is not a directory.");
			}
			// delete the oldest files
			File[] currentLogFiles = this.getLogFiles();
			for (int i = 0; i <= currentLogFiles.length - this.getNumberLogFiles(); i++) {
				if (!currentLogFiles[i].delete()) {
					this.getLog().warning("The old  log file " + currentLogFiles[i] + " could not be deleted.");
				}
			}
			try {
				LocalDateTime dateTimeNow = LocalDateTime.now();
				String startingTime = String.format("%d_%d_%d_%d_%d_%d_%d", dateTimeNow.getYear(), 
						dateTimeNow.getMonthValue(), dateTimeNow.getDayOfMonth(), dateTimeNow.getHour(), 
						dateTimeNow.getMinute(), dateTimeNow.getSecond(), dateTimeNow.getNano());
				this.logHandler = new FileHandler((this.getLoggingFolder().toPath().resolve(
						this.getLogFileName() + "_" + startingTime 
						+ LoggingHandler.LOG_FILE_EXTENSION)).toString()); // always write to first log file
				this.getLog().addHandler(this.logHandler);
			} catch (SecurityException e) {
				this.getLog().log(Level.SEVERE, "Security problem accessing log file.", e);
				e.printStackTrace();
				throw new LoggingFailureException("Security problem accessing log file.", e);
			} catch (IOException e) {
				this.getLog().log(Level.SEVERE, "The log file could not be written to.", e);
				e.printStackTrace();
				throw new LoggingFailureException("The log file could not be written to.", e);
			}
		} else {
			this.getLog().warning("Logging has already been started.");
		}
	}
	
	/**
	 * Stop the log writing procedure.
	 */
	public void stopLogWriting() throws LoggingFailureException {
		if(this.logHandler != null) {
			try {
				this.logHandler.close();
				this.getLog().removeHandler(logHandler);
				this.logHandler = null;
			} catch (SecurityException e) { // false if logging could not be stopped
				e.printStackTrace();
				this.getLog().log(Level.WARNING, "Logging could not be stopped.", e);
				throw new LoggingFailureException( "Logging could not be stopped.", e);
			}
		} else {
			this.getLog().warning("No logging is currently performed "
					+ "and can thereby not be stopped.");
		}
	}

	/**
	 * Get all logging files for the current settings.
	 * 
	 * @return an array of all log files
	 */
	public File[] getLogFiles() {
		// list all log files created with the current settings
		return this.getLoggingFolder().listFiles(file -> {
			String name = file.getName();
			return name.startsWith(this.getLogFileName()) 
					&& name.endsWith(LoggingHandler.LOG_FILE_EXTENSION);
		});
	}

	/**
	 * Get the current logger in order to access logging functionality.
	 * 
	 * @return the current Logger
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Get the folder where all logging files are saved.
	 * 
	 * @return the logging folder as File
	 */
	public File getLoggingFolder() {
		return this.logFolder;
	}

	/** 
	 * Set the folder to which the logging files will be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolder - the folder to save logging files to
	 * @throws IllegalArgumentException if the folder is null
	 */
	public void setLoggingFolder(File logFolder) {
		if (logFolder != null) {
			this.logFolder = logFolder;
		} else {
			throw new IllegalArgumentException("The logging folder cannot be null.");
		}
	}
	
	/** 
	 * Set the folder to which the logging files will be saved. 
	 * It must be set before logging is started.
	 * 
	 * @param logFolderPath - the path to the folder to save logging files to
	 * @throws IllegalArgumentException if the folder path is null
	 */
	public void setLoggingFolder(String logFolderPath) {
		if (logFolderPath != null) {
			this.logFolder = new File(logFolderPath);
		} else {
			throw new IllegalArgumentException("The logging folder cannot be null.");
		}
	}

	/**
	 * Get the base name of the corresponding logging files.
	 * 
	 * @return the name of the logging files
	 */
	public String getLogFileName() {
		return this.logFileName;
	}

	/**
	 * Set the base name to use as template for the logging files.
	 * 
	 * @param logFileName - the name of the logging files
	 * @throws IllegalArgumentException if the log file name is null or empty
	 */
	public void setLogFileName(String logFileName) {
		if (logFileName != null && logFileName.length() > 0) {
			this.logFileName = logFileName;
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
	public int getNumberLogFiles() {
		return this.numberLogFiles;
	}

	/**
	 * Set the number of logging files that will be saved before deleting
	 * the oldest.
	 * 
	 * @param numberLogFiles - the number of logging files to set
	 * @throws IllegalArgumentException if the number of log files is less than 1
	 */
	public void setNumberLogFiles(int numberLogFiles) {
		if (numberLogFiles > 0) {
			this.numberLogFiles = numberLogFiles;
		}  else {
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
	public  void logAndPrint(Level level, String msg) {
		this.getLog().log(level, msg);
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
	public void logAndPrint(Level level, String msg, Throwable thrown) {
		this.getLog().log(level, msg, thrown);
		String formattedMsg = String.format("[%s] %s", level.getName(), msg);
		if (Level.SEVERE.equals(level) || Level.WARNING.equals(level)) {
			System.err.println(formattedMsg);
		} else {
			System.out.println(formattedMsg);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logFileName == null) ? 0 : logFileName.hashCode());
		result = prime * result + ((logFolder == null) ? 0 : logFolder.hashCode());
		result = prime * result + numberLogFiles;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Logging other = (Logging) obj;
		if (logFileName == null) {
			if (other.logFileName != null)
				return false;
		} else if (!logFileName.equals(other.logFileName))
			return false;
		if (logFolder == null) {
			if (other.logFolder != null)
				return false;
		} else if (!logFolder.equals(other.logFolder))
			return false;
		if (numberLogFiles != other.numberLogFiles)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Log [%s:%s; %s]", Logging.logIdentifier, 
				this.getLoggingFolder().toPath().resolve(this.getLogFileName()) , this.getNumberLogFiles());
	}
}
