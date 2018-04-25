/**
 * Define a logging API with a static, centralised logger.
 * 
 * @author Planters
 */
module central.logging {

	exports central.logging.functionality;
	
	requires java.base;
	requires transitive java.logging;
	
	
}