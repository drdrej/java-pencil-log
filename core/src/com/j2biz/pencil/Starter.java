/* 
 * "Pencil - Log message compiler" is (c) 2004 Andreas Siebert (j2biz community)
 *
 * Author: Andreas Siebert.
 *  
 * This file is part of "Pencil - Log message compiler".
 *
 * "Pencil - Log message compiler" is free software; 
 * you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version.
 *
 * "Pencil - Log message compiler" is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Pencil - Logger message compiler"; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package com.j2biz.pencil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.grlea.log.SimpleLogger;

import com.j2biz.info.ConsoleErrorStatusLogger;
import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.config.ConfigManager;

public class Starter {

	public static SimpleLogger LOG = new SimpleLogger( Starter.class);
	
	private final boolean					sourceOptionEntered;

	private final boolean					configOptionEntered;
	
	private final CommandLine			line;
	
	Starter( final CommandLine line ) {
		assert line != null;
	
		final CLIOptionParser cliParser = CLIOptionParser.getInstance();
		
		
		if ( line.hasOption("h") )
			executeHelp(cliParser);
		
		this.line = line;
		
		this.sourceOptionEntered = line.hasOption(cliParser.getSourceOptionString());
		this.configOptionEntered = line.hasOption(cliParser.getConfigOptionString());

		if (configOptionEntered && sourceOptionEntered) 
			exitCauseScrAndConfigAreEntered();
		
		if ( configOptionEntered ) {
			executeWithConfig(line);
		} else {
			final File srcPathFile;
			if ( sourceOptionEntered ) {
				final String fileName = line.getOptionValue(cliParser.getSourceOptionString());
				final File file = new File(fileName);
				if ( file.exists() ) {
					srcPathFile = file;
				} else {
					srcPathFile = new File(new File("").getAbsoluteFile(),
							fileName);
				}
			} else {
				srcPathFile = new File("");
			}

			if ( !srcPathFile.exists() ) {
				exitCauseSrcPathNotExist(srcPathFile);
			} else {
				final ErrorStatusLogger logger = new ConsoleErrorStatusLogger();
				final Enhancer enhancer;
				if ( line.hasOption(cliParser.getPackagesOption()) ) {
					final String[] packageNames = line
							.getOptionValues(cliParser.getPackagesOption());
					final Source source = new Source(srcPathFile, packageNames);
					final ClassManager manager = new ClassManager(new File(""),
							srcPathFile, new Source[] { source }, null, logger);

					enhancer = new Enhancer(manager, logger);
				} else {
					enhancer = new Enhancer(srcPathFile, logger);
				}

				enhancer.enhance();
			}
		}
	}

	private void executeHelp( final CLIOptionParser cliParser ) {
		cliParser.printHelpMessage(  new PrintWriter(System.out) );
		System.exit(0);
	}
	
	public Starter(final File configFile ) {
		configOptionEntered = false;
		sourceOptionEntered = false;
		line = null;
		
		// wegen der initialisierung des Config-Parsers.
		CLIOptionParser.getInstance();
		executeWithConfig(configFile);
	}

	private void executeWithConfig( final CommandLine line ) {
		File configFile = getConfigFile();

		final CLIOptionParser cliParser = CLIOptionParser.getInstance();
		
		if ( !configFile.exists() ) 
			exitCauseConfigPathNotExist(cliParser.getConfigOptionString(), line);
		
		executeWithConfig(configFile);
	}

	private void executeWithConfig( final File configFile ) {
		final ClassManager manager = initManagerWithConfig(configFile);
		
		final Enhancer enhancer = new Enhancer(manager,
				new ConsoleErrorStatusLogger());

		enhancer.enhance();
	}

	private File getConfigFile() {
		final CLIOptionParser cliParser = CLIOptionParser.getInstance();
		
		final String configPath = line.getOptionValue(cliParser.getConfigOptionString());

		File configFile = new File(configPath);
		
		return configFile;
	}

	private ClassManager initManagerWithConfig( final File configFile) {
		final ClassManager manager;
		try {
			return ConfigManager.readConfiguration(configFile);
		} catch ( FileNotFoundException x ) {
			final CLIOptionParser cliParser = CLIOptionParser.getInstance();
			exitCauseConfigPathNotExist(cliParser.getConfigOptionString(), line, x);
			
			return null;
		}
	}

	public static void main( String[] args ) {
		try {
			final CLIOptionParser cliParser = CLIOptionParser.getInstance();
			final CommandLine line = cliParser.parse(args);
			new Starter(line);
		} catch ( MissingArgumentException e ) {
			exitCauseMissingArgument(e);
		} catch ( MissingOptionException e ) {
			exitCauseMissingOption(e);
		} catch ( ParseException e ) {
			exitCauseParseException(e);
		}
	}

	private static void exitCauseParseException( ParseException e ) {
		System.err.println("Parsing failed.  Reason: " + e.getMessage());
		System.exit(-1);
	}

	private static void exitCauseMissingOption( MissingOptionException e ) {
		System.err.println("Missing Option: " + e.getMessage());
		System.exit(-1);
	}

	private static void exitCauseMissingArgument( MissingArgumentException e ) {
		System.err.println("Missing Argument-Value. " + e.getMessage());
		System.exit(-1);
	}

	private static void exitCauseSrcPathNotExist( final File srcPathFile ) {
		System.out
				.println("WARNING: src-path doesn't exists. please select an existing path.");
		System.out.println("WARNING: the selected src-path is: "
				+ srcPathFile.getAbsolutePath());
		System.exit(-1);
	}

	private static void exitCauseConfigPathNotExist(
		final String config,
		CommandLine line,
		FileNotFoundException x ) {
		System.out
				.println("WARNING: the entered config-path does not exist. path == "
						+ line.getOptionValue(config));
		x.printStackTrace();
		System.exit(-1);
	}

	private static void exitCauseConfigPathNotExist(
		final String config,
		CommandLine line ) {
		
		System.out.println("WARNING: the entered config-path does not exist. path");
		System.out.println("WARNING: entered path: " + line.getOptionValue(config));
		System.exit(-1);
	}

	private static void exitCauseScrAndConfigAreEntered( ) {
		System.out.println("WARNING: src-path option is allowed only if the config-option is not used.");
		System.exit(-1);
	}

}
