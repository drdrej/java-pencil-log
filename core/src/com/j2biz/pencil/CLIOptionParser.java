package com.j2biz.pencil;

import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.grlea.log.SimpleLogger;

import com.j2biz.pencil.config.ConfigManager;

public class CLIOptionParser {

	public static SimpleLogger				LOG			= new SimpleLogger(
																CLIOptionParser.class);

	private final Option							configOption;

	private final Option							sourceDirectoryOption;

	private final Option							allowedPackagesOption;
	
	public final CommandLineParser	parser	= new GnuParser();

	private final Options					options		= new Options();

	
	/**
	 * singleton
	 */
	private static final CLIOptionParser	INSTANCE	= new CLIOptionParser();

	private CLIOptionParser( ) {
		LOG.entry("<init>()");
		addHelpOption();
		this.configOption = addConfigOption();
		this.sourceDirectoryOption = addSourceOption();
		this.allowedPackagesOption = addPackagesOption();
		addClasspathOption();

		ConfigManager.initialize();
		LOG.exit("<init>()");
	}

	public static CLIOptionParser getInstance( ) {
		return INSTANCE;
	}

	public final Option addHelpOption( ) {
		LOG.entry("addHelpOption()");

		OptionBuilder.withArgName("help");
		OptionBuilder.withDescription("to display the help of pencil.");
		
		final Option rval = OptionBuilder.create('h');

		options.addOption(rval);
		
		LOG.exit("addHelpOption()");
		return rval;
	}

	public final Option addConfigOption( ) {
		LOG.entry("addConfigOption( )");
		
		OptionBuilder.withArgName("config");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("to select the configuration-file.");
		OptionBuilder.isRequired(false);
		OptionBuilder.withLongOpt("config");
		final Option rval = OptionBuilder.create('c');

		options.addOption(rval);
		
		LOG.exit("addConfigOption( )");
		return rval;
	}

	public final Option addSourceOption( ) {
		LOG.entry("addSourceOption( )");
		
		OptionBuilder.withArgName("src-path");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("to select the directory with the classes, which should be precompiled.");
		OptionBuilder.isRequired(false);
		OptionBuilder.withLongOpt("src-path");

		final Option rval = OptionBuilder.create('s');
		options.addOption(rval);

		LOG.exit("addSourceOption( )");
		return rval;
	}

	public final Option addPackagesOption( ) {
		LOG.entry("addPackagesOption( )");
		OptionBuilder.withArgName("packages");
		OptionBuilder.hasArgs();
		OptionBuilder
				.withDescription("to select the packages, which should be precompiled.");
		OptionBuilder.isRequired(false);
		OptionBuilder.withLongOpt("packages");
		OptionBuilder.withValueSeparator(',');
		final Option rval = OptionBuilder.create('p');

		options.addOption(rval);
		LOG.exit("addPackagesOption( )");
		
		return rval;
	}

	public void addClasspathOption( ) {
		LOG.entry("addClasspathOption( )");

		OptionBuilder.withArgName("classpath");
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription("to select set the project-classpath.");
		OptionBuilder.isRequired(false);
		OptionBuilder.withLongOpt("classpath");
		OptionBuilder.withValueSeparator(',');
		final Option classpath = OptionBuilder.create("cp");

		options.addOption(classpath);
		LOG.exit("addClasspathOption( )");
	}

	public String getSourceOptionString( ) {
		return sourceDirectoryOption.getOpt();
	}

	public String getConfigOptionString( ) {
		return this.configOption.getOpt();
	}

	public String getPackagesOption( ) {
		return this.allowedPackagesOption.getOpt();
	}

	public void printHelpMessage( final PrintWriter output ) {
		final HelpFormatter formatter = new HelpFormatter();

		formatter.printUsage(output, 80, "ls -src-path [-package]");
		output.println();

		formatter.printWrapped(output, HelpFormatter.DEFAULT_WIDTH,
				"  (c) 2004 by Andreas Siebert at j2biz.com");
		formatter.printWrapped(output, HelpFormatter.DEFAULT_WIDTH,
				"  Pencil - log-message-compiler for Java.");

		formatter
				.printWrapped(
						output,
						HelpFormatter.DEFAULT_WIDTH,
						2,
						"  Description: Compiles log messages in the classes in the source directory defined by -src-pah option.");
		output.println();

		formatter.printOptions(output, HelpFormatter.DEFAULT_WIDTH, options, 2,
				HelpFormatter.DEFAULT_LEFT_PAD);

		output.flush();
		output.close();
	}

	public CommandLine parse( final String[] arguments )
			throws MissingArgumentException, MissingOptionException,
			ParseException {
		
		return parser.parse(options, arguments);
	}
}
