/*
 * Copyright 2004 Andreas Siebert (j2biz community)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.j2biz.pencil.ant;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.util.FileUtils;

import com.j2biz.pencil.Starter;

public class PencilTask extends Task {

	final static FileUtils	FILE_UTIL	= FileUtils.newFileUtils();

	private String			configName;

	private File			copyOfConfigFile;

	private File			configFile;

	private String			copyOfConfigName;

	private boolean			verbose		= false;

	public String getConfig( ) {
		return configName;
	}

	public void setConfig( String configName ) {
		this.configName = configName;
	}

	public void setStoredConfig( String configName ) {
		this.copyOfConfigName = configName;
	}

	public String getStoredConfig( ) {
		return copyOfConfigName;
	}

	public boolean getVerbose( ) {
		return this.verbose;
	}

	public void setVerbose( boolean verbose ) {
		this.verbose = verbose;
	}

	@Override
	public void execute( ) throws BuildException {
		try {
			final File configFile = checkAttributes();

			if ( getVerbose() )
				log("Pencil use the following config-file : "
						+ this.configFile.getAbsolutePath());

			replaceTokensInConfigFile();

			final File tmpConfigFile = getCopyOfConfig(configFile);

			Starter enhancer = new Starter(tmpConfigFile);
		} catch ( final Throwable x ) {
			log( "Pencil failed. a problem occured: " + x, Project.MSG_ERR);
			x.printStackTrace();
			
			throw new BuildException(x);
		}
	}

	private void replaceTokensInConfigFile( ) {
		final Replace replace = createReplace();
		fillTokensWithProps(replace);
		replace.execute();
	}

	private void fillTokensWithProps( Replace r ) {
		for ( Enumeration e = getProject().getProperties().keys(); e
				.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			createReplaceFilter(r, key);
		}
	}

	private void createReplaceFilter( Replace r, String key ) {
		final Replace.Replacefilter filter = r.createReplacefilter();
		
		final String token = "${" + key + "}";
		filter.setToken( token );
		
		final String value = getProject().getProperty(key);
		filter.setValue(value);
		
		if( getVerbose())
			log("set " + token + " in the pencil config-file to the value: " + value, Project.MSG_DEBUG);
	}

	private File checkAttributes( ) {
		if ( this.configName == null )
			throw new BuildException(MsgKeys.getERR_CONFIG_ATTR_REQUIRED());

		this.configFile = FILE_UTIL.resolveFile(getBaseDir(), this.configName);

		if ( !this.configFile.exists() )
			throw new BuildException(MsgKeys.getERR_CONFIG_FILE_NOT_EXIST(
					this.configName, this.configFile));

		if ( this.configFile.isDirectory() )
			throw new BuildException(MsgKeys.getERR_CONFIG_FILE_IS_A_DIRECTORY(
					this.configName, this.configFile));

		return configFile;
	}

	private File getBaseDir( ) {
		return getProject().getBaseDir();
	}

	private Replace createReplace( ) {
		final Replace r = new Replace();
		r.setProject(getProject());
		r.setFile(getCopyOfConfig(this.configFile));

		return r;
	}

	private File getCopyOfConfig( final File configFile ) {
		return (this.copyOfConfigFile == null) ? createCopyOfConfig()
				: copyOfConfigFile;
	}

	private File createCopyOfConfig( ) {
		final File rval;

		if ( this.copyOfConfigName != null ) {
			rval = FILE_UTIL.resolveFile(configFile.getParentFile(),
					this.copyOfConfigName);
		} else {
			rval = new File(configFile.getParentFile(),
					createTempName(configFile));
		}

		try {
			FILE_UTIL.copyFile(configFile, rval);
		} catch ( final IOException x ) {
			throw new BuildException(MsgKeys
					.getERR_CANT_CREATE_TEMP_CONFIG_FILE(x, this.configName,
							this.configFile), x);
		}

		this.copyOfConfigFile = rval;

		if ( getVerbose() ) {
			log("Create a following temporary config-file filled with known ant-property-values.");
			log("Name of temporary created config file is: "
					+ this.copyOfConfigFile.getAbsolutePath());
		}

		return rval;
	}

	private String createTempName( final File configFile ) {
		return configFile.getName() + ".t_" + System.currentTimeMillis();
	}

}
