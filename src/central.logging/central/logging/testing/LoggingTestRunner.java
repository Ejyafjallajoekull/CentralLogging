package central.logging.testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import central.logging.functionality.Logging;
import central.logging.functionality.LoggingFailureException;
import central.logging.functionality.LoggingHandler;

/**
 * The LoggingTestRunner class can perform some basic tests to check 
 * the logging process for errors.
 * 
 * @author Planters
 *
 */
public class LoggingTestRunner {

	public static void main(String[] args) {
		System.out.println("Start running tests");
		// tests for static logging
		LoggingTestRunner.testSetNumberLogFiles();
		LoggingTestRunner.testSetLogFileName();
		LoggingTestRunner.testSetLoggingFolder();
		LoggingTestRunner.testGetLogFiles();
		LoggingTestRunner.testLogWriting();
		// tests for class based logging
		LoggingTestRunner.testSetNumberLogFilesClass();
		LoggingTestRunner.testSetLogFileNameClass();
		LoggingTestRunner.testSetLoggingFolderClass();
		LoggingTestRunner.testGetLogFilesClass();
		LoggingTestRunner.testLogWritingClass();
		LoggingTestRunner.testConstructorsClass();
		System.out.println("All tests passed");
	}

	private static void testSetNumberLogFiles() {
		int num = 4;
		int rep = 10000;
		Random r = new Random();
		for (int i = 0; i < rep; i++) { // test all numbers greater than zero
			num = r.nextInt(Integer.MAX_VALUE) + 1;
			LoggingHandler.setNumberLogFiles(num);
			if (LoggingHandler.getNumberLogFiles() != num) {
				throw new IllegalArgumentException("The number of logging files set (" + 
						num + ") is not equal to the returned one (" + 
						LoggingHandler.getNumberLogFiles() + ")");
			}
		}
		for (int i = 0; i < rep; i++) { // test negative numbers and zero
			num = r.nextInt(Integer.MAX_VALUE) * (-1);
			try {
				LoggingHandler.setNumberLogFiles(num);
				throw new IllegalArgumentException(String.format("Setting the log file number to "
						+ "%s should fail, but resulted in setting the log file number to %s.", 
						num, LoggingHandler.getNumberLogFiles()));
			} catch (IllegalArgumentException e) {
				// Do nothing as this is expected behaviour.
			}
		}
		System.out.println("Test LoggingHandler.setNumberLogFiles passed");
	}
	
	private static void testSetLogFileName() {
		byte[] arr = null;
		String testName = "";
		int rep = 10000;
		Random r = new Random();
		for (int i = 0; i < rep; i++) { // test all existent strings
			arr = new byte[r.nextInt(5000) + 1];
			r.nextBytes(arr);
			testName = new String(arr);
			LoggingHandler.setLogFileName(testName);
			if (LoggingHandler.getLogFileName() != testName) {
				throw new IllegalArgumentException("The name of the logging files set (" + 
						testName + ") is not equal to the returned one (" + 
						LoggingHandler.getLogFileName() + ")");
			}
		}
		String emptyName = "";
		try {
			LoggingHandler.setLogFileName(emptyName); // check empty strings
			throw new IllegalArgumentException(String.format("Setting the log file name to "
					+ "\"%s\" should fail, but resulted in setting the log file name to \"%s\".", 
					emptyName, LoggingHandler.getLogFileName()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		String nullName = null;
		try {
			LoggingHandler.setLogFileName(nullName); // check empty strings
			throw new IllegalArgumentException(String.format("Setting the log file name to "
					+ "\"%s\" should fail, but resulted in setting the log file name to \"%s\".", 
					nullName, LoggingHandler.getLogFileName()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		System.out.println("Test LoggingHandler.setLogFileName passed");
	}
	
	private static void testSetLoggingFolder() {
		String folderPath = "ABCLog"; // test relative paths ; String
		File folder = new File(folderPath);
		LoggingHandler.setLoggingFolder(folderPath);
		if (!LoggingHandler.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					LoggingHandler.getLoggingFolder() + ")");
		}
		folderPath = "ABDLog"; // test relative paths ; File
		folder = new File(folderPath);
		LoggingHandler.setLoggingFolder(folder);
		if (!LoggingHandler.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					LoggingHandler.getLoggingFolder() + ")");
		}
		folderPath = "ABELog"; // test absolute paths ; String
		folder = new File(folderPath);
		folderPath = folder.getAbsolutePath();
		folder = new File(folderPath);
		LoggingHandler.setLoggingFolder(folderPath);
		if (!LoggingHandler.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					LoggingHandler.getLoggingFolder() + ")");
		}
		folderPath = "ABFLog"; // test absolute paths ; File
		folder = new File(folderPath);
		folderPath = folder.getAbsolutePath();
		folder = new File(folderPath);
		LoggingHandler.setLoggingFolder(folder);
		if (!LoggingHandler.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					LoggingHandler.getLoggingFolder() + ")");
		}
		folder = null;
		try {
			LoggingHandler.setLoggingFolder(folder); // test null File
			throw new IllegalArgumentException(String.format("Setting the log folder to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folder, LoggingHandler.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		folderPath = null;
		try {
			LoggingHandler.setLoggingFolder(folderPath); // test null folder path
			throw new IllegalArgumentException(String.format("Setting the log folder path to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folderPath, LoggingHandler.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		folderPath = "";
		try {
			LoggingHandler.setLoggingFolder(folderPath); // test empty folder path
			throw new IllegalArgumentException(String.format("Setting the log folder path to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folderPath, LoggingHandler.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		System.out.println("Test LoggingHandler.setLoggingFolder passed");
	}
	
	private static void testGetLogFiles() {
		String folder = "TestLogging_" + System.nanoTime();
		String name = "TestLog";
		LoggingHandler.setLoggingFolder(folder);
		LoggingHandler.setLogFileName(name);
		LoggingHandler.getLoggingFolder().mkdirs();
		ArrayList<File> logFiles = new ArrayList<File>();
		// test empty list
		if (!Arrays.equals(LoggingHandler.getLogFiles(), logFiles.toArray())) {
			throw new IllegalArgumentException("The list of tested files ("
					+ logFiles + "is not equal to the returned ones ("
					+ Arrays.toString(LoggingHandler.getLogFiles()) + ").");
		}
		// test single file
		logFiles.add(LoggingHandler.getLoggingFolder().toPath().resolve(name + LoggingHandler.LOG_FILE_EXTENSION).toFile());
		try {
			logFiles.get(0).createNewFile();
			if (!Arrays.equals(LoggingHandler.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(LoggingHandler.getLogFiles()) + ").");
			}
			// test multiple files
			for (int i = 0; i < 100; i++) {
				logFiles.add(LoggingHandler.getLoggingFolder().toPath().resolve(name + i + LoggingHandler.LOG_FILE_EXTENSION).toFile());
				logFiles.get(i+1).createNewFile();
			}
			Collections.sort(logFiles); // need as getLogFiles returns the files sorted in alphabetical order
			if (!Arrays.equals(LoggingHandler.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(LoggingHandler.getLogFiles()) + ").");
			}
			// test deletion
			Random r = new Random();
			logFiles.remove(r.nextInt(logFiles.size())).delete();
			if (!Arrays.equals(LoggingHandler.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(LoggingHandler.getLogFiles()) + ").");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Test file ("
					+ LoggingHandler.getLoggingFolder().toPath().resolve(name).toFile()
					+ "could not be accessed.");
		} finally { // delete all the files created at the end of the testing
			for (File f : logFiles) {
				f.delete();
			}
			LoggingHandler.getLoggingFolder().delete();
		}
		System.out.println("Test LoggingHandler.getLogFiles passed");
	}
	
	private static void testLogWriting() {
		String folder = "TestLogging_" + System.nanoTime();
		String name = "TestLog";
		int num = 6;
		LoggingHandler.setLoggingFolder(folder);
		LoggingHandler.setLogFileName(name);
		LoggingHandler.setNumberLogFiles(num);
		// stopping a non running logging process should not cause any trouble
		try {
			LoggingHandler.stopLogWriting();
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Stopping a non running logging process "
					+ "caused a problem.");
		}
		// start log writing
		try {
			LoggingHandler.startLogWriting();
			// start log writing again, should not cause any trouble
			LoggingHandler.startLogWriting();
			if (LoggingHandler.getLogFiles().length != 1) {
				throw new IllegalArgumentException("There should be one logging file, "
						+ "but " + LoggingHandler.getLogFiles().length + " are present.");
			}
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Starting a non running logging process "
					+ "caused a problem.");
		}
		for (int i = 2; i < 100; i++) { // stop and restart a few times
			try {
				LoggingHandler.stopLogWriting();
				Thread.sleep(1);
			} catch (LoggingFailureException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Stopping a running logging process "
						+ "caused a problem.");
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Could not put this thread to sleep, "
						+ "but thats necessary for this test to work properly.");
			}
			try {
				LoggingHandler.startLogWriting();
			} catch (LoggingFailureException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Starting a non running logging process "
						+ "caused a problem.");
			}
			if (i < num) {
				if (LoggingHandler.getLogFiles().length != i) {
					throw new IllegalArgumentException("Expected " + i + " log files, but found "
							+ "only " + LoggingHandler.getLogFiles().length + ".");
				}
			} else {
				if (LoggingHandler.getLogFiles().length > num) {
					throw new IllegalArgumentException("There are " + LoggingHandler.getLogFiles().length
							+ " log files, but the maximum number is set to " + num + ".");
				}
			}
		}
		// stop logging and delete the test files
		try {
			LoggingHandler.stopLogWriting();
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Stopping a running logging process "
					+ "caused a problem.");
		}
		for (File f : LoggingHandler.getLogFiles()) {
			f.delete();
		}
		LoggingHandler.getLoggingFolder().delete();
		System.out.println("Test LoggingHandler.startLogWriting passed");
	}
	
	private static void testSetNumberLogFilesClass() {
		int num = 4;
		int rep = 10000;
		Random r = new Random();
		for (int i = 0; i < rep; i++) { // test all numbers greater than zero
			Logging testLogger = new Logging();
			num = r.nextInt(Integer.MAX_VALUE) + 1;
			testLogger.setNumberLogFiles(num);
			if (testLogger.getNumberLogFiles() != num) {
				throw new IllegalArgumentException("The number of logging files set (" + 
						num + ") is not equal to the returned one (" + 
						testLogger.getNumberLogFiles() + ")");
			}
		}
		for (int i = 0; i < rep; i++) { // test negative numbers and zero
			Logging testLogger = new Logging();
			num = r.nextInt(Integer.MAX_VALUE) * (-1);
			try {
				testLogger.setNumberLogFiles(num);
				throw new IllegalArgumentException(String.format("Setting the log file number to "
						+ "%s should fail, but resulted in setting the log file number to %s.", 
						num, testLogger.getNumberLogFiles()));
			} catch (IllegalArgumentException e) {
				// Do nothing as this is expected behaviour.
			}
		}
		System.out.println("Test (new Logging()).setNumberLogFiles passed");
	}
	
	private static void testSetLogFileNameClass() {
		byte[] arr = null;
		String testName = "";
		int rep = 10000;
		Random r = new Random();
		Logging testLogger = new Logging();
		for (int i = 0; i < rep; i++) { // test all existent strings
			arr = new byte[r.nextInt(5000) + 1];
			r.nextBytes(arr);
			testName = new String(arr);
			testLogger.setLogFileName(testName);
			if (testLogger.getLogFileName() != testName) {
				throw new IllegalArgumentException("The name of the logging files set (" + 
						testName + ") is not equal to the returned one (" + 
						testLogger.getLogFileName() + ")");
			}
		}
		String emptyName = "";
		try {
			testLogger.setLogFileName(emptyName); // check empty strings
			throw new IllegalArgumentException(String.format("Setting the log file name to "
					+ "\"%s\" should fail, but resulted in setting the log file name to \"%s\".", 
					emptyName, testLogger.getLogFileName()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		String nullName = null;
		try {
			testLogger.setLogFileName(nullName); // check empty strings
			throw new IllegalArgumentException(String.format("Setting the log file name to "
					+ "\"%s\" should fail, but resulted in setting the log file name to \"%s\".", 
					nullName, testLogger.getLogFileName()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		System.out.println("Test (new Logging()).setLogFileName passed");
	}
	
	private static void testSetLoggingFolderClass() {
		String folderPath = "ABCLog"; // test relative paths ; String
		File folder = new File(folderPath);
		Logging testLogger = new Logging();
		testLogger.setLoggingFolder(folderPath);
		if (!testLogger.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					testLogger.getLoggingFolder() + ")");
		}
		folderPath = "ABDLog"; // test relative paths ; File
		folder = new File(folderPath);
		testLogger.setLoggingFolder(folder);
		if (!testLogger.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					testLogger.getLoggingFolder() + ")");
		}
		folderPath = "ABELog"; // test absolute paths ; String
		folder = new File(folderPath);
		folderPath = folder.getAbsolutePath();
		folder = new File(folderPath);
		testLogger.setLoggingFolder(folderPath);
		if (!testLogger.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					testLogger.getLoggingFolder() + ")");
		}
		folderPath = "ABFLog"; // test absolute paths ; File
		folder = new File(folderPath);
		folderPath = folder.getAbsolutePath();
		folder = new File(folderPath);
		testLogger.setLoggingFolder(folder);
		if (!testLogger.getLoggingFolder().equals(folder)) {
			throw new IllegalArgumentException("The logging folder path set (" +
					folder + ") is different to the returned one (" +
					testLogger.getLoggingFolder() + ")");
		}
		folder = null;
		try {
			testLogger.setLoggingFolder(folder); // test null File
			throw new IllegalArgumentException(String.format("Setting the log folder to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folder, testLogger.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		folderPath = null;
		try {
			testLogger.setLoggingFolder(folderPath); // test null folder path
			throw new IllegalArgumentException(String.format("Setting the log folder path to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folderPath, testLogger.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		folderPath = "";
		try {
			testLogger.setLoggingFolder(folderPath); // test empty folder path
			throw new IllegalArgumentException(String.format("Setting the log folder path to "
					+ "\"%s\" should fail, but resulted in setting the log folder to \"%s\".", 
					folderPath, testLogger.getLoggingFolder()));
		} catch (IllegalArgumentException e) {
			// Do nothing as this is expected behaviour.
		}
		System.out.println("Test LoggingHandler.setLoggingFolder passed");
	}
	
	private static void testGetLogFilesClass() {
		String folder = "TestLogging_" + System.nanoTime();
		String name = "TestLog";
		Logging testLogger = new Logging();
		testLogger.setLoggingFolder(folder);
		testLogger.setLogFileName(name);
		testLogger.getLoggingFolder().mkdirs();
		ArrayList<File> logFiles = new ArrayList<File>();
		// test empty list
		if (!Arrays.equals(testLogger.getLogFiles(), logFiles.toArray())) {
			throw new IllegalArgumentException("The list of tested files ("
					+ logFiles + "is not equal to the returned ones ("
					+ Arrays.toString(testLogger.getLogFiles()) + ").");
		}
		// test single file
		logFiles.add(testLogger.getLoggingFolder().toPath().resolve(name + LoggingHandler.LOG_FILE_EXTENSION).toFile());
		try {
			logFiles.get(0).createNewFile();
			if (!Arrays.equals(testLogger.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(testLogger.getLogFiles()) + ").");
			}
			// test multiple files
			for (int i = 0; i < 100; i++) {
				logFiles.add(testLogger.getLoggingFolder().toPath().resolve(name + i + LoggingHandler.LOG_FILE_EXTENSION).toFile());
				logFiles.get(i+1).createNewFile();
			}
			Collections.sort(logFiles); // need as getLogFiles returns the files sorted in alphabetical order
			if (!Arrays.equals(testLogger.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(testLogger.getLogFiles()) + ").");
			}
			// test deletion
			Random r = new Random();
			logFiles.remove(r.nextInt(logFiles.size())).delete();
			if (!Arrays.equals(testLogger.getLogFiles(), logFiles.toArray())) {
				throw new IllegalArgumentException("The list of tested files ("
						+ logFiles + "is not equal to the returned ones ("
						+ Arrays.toString(testLogger.getLogFiles()) + ").");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Test file ("
					+ testLogger.getLoggingFolder().toPath().resolve(name).toFile()
					+ "could not be accessed.");
		} finally { // delete all the files created at the end of the testing
			for (File f : logFiles) {
				f.delete();
			}
			testLogger.getLoggingFolder().delete();
		}
		System.out.println("Test (new Logging()).getLogFiles passed");
	}
	
	private static void testLogWritingClass() {
		String folder = "TestLogging_" + System.nanoTime();
		String name = "TestLog";
		int num = 6;
		Logging testLogger = new Logging();
		testLogger.setLoggingFolder(folder);
		testLogger.setLogFileName(name);
		testLogger.setNumberLogFiles(num);
		// stopping a non running logging process should not cause any trouble
		try {
			testLogger.stopLogWriting();
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Stopping a non running logging process "
					+ "caused a problem.");
		}
		// start log writing
		try {
			testLogger.startLogWriting();
			// start log writing again, should not cause any trouble
			testLogger.startLogWriting();
			if (testLogger.getLogFiles().length != 1) {
				throw new IllegalArgumentException("There should be one logging file, "
						+ "but " + testLogger.getLogFiles().length + " are present.");
			}
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Starting a non running logging process "
					+ "caused a problem.");
		}
		for (int i = 2; i < 100; i++) { // stop and restart a few times
			try {
				testLogger.stopLogWriting();
				Thread.sleep(1);
			} catch (LoggingFailureException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Stopping a running logging process "
						+ "caused a problem.");
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Could not put this thread to sleep, "
						+ "but thats necessary for this test to work properly.");
			}
			try {
				testLogger.startLogWriting();
			} catch (LoggingFailureException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Starting a non running logging process "
						+ "caused a problem.");
			}
			if (i < num) {
				if (testLogger.getLogFiles().length != i) {
					throw new IllegalArgumentException("Expected " + i + " log files, but found "
							+ "only " + testLogger.getLogFiles().length + ".");
				}
			} else {
				if (testLogger.getLogFiles().length > num) {
					throw new IllegalArgumentException("There are " + testLogger.getLogFiles().length
							+ " log files, but the maximum number is set to " + num + ".");
				}
			}
		}
		// stop logging and delete the test files
		try {
			testLogger.stopLogWriting();
		} catch (LoggingFailureException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Stopping a running logging process "
					+ "caused a problem.");
		}
		for (File f : testLogger.getLogFiles()) {
			f.delete();
		}
		testLogger.getLoggingFolder().delete();
		System.out.println("Test (new Logging()).startLogWriting passed");
	}
	
	private static void testConstructorsClass() {
		Logging firstLog = new Logging();
		Logging secondLog = null;
		if (firstLog.equals(secondLog)) {
			throw new IllegalArgumentException(String.format("%s and %s should not be equal.", 
					firstLog, secondLog));
		}
		secondLog = new Logging();
		if (!firstLog.equals(secondLog)) {
			throw new IllegalArgumentException(String.format("%s and %s should be equal.", 
					firstLog, secondLog));
		}
	}
	
}
