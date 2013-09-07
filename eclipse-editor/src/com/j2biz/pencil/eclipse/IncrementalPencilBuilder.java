package com.j2biz.pencil.eclipse;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;



// Dieser code muss in ein Projekt-File integriert werden, damit
// der Builder aufgerufen werden kann.
// ---------------------------------------------------------------------
//
//<buildCommand>
//<name>com.j2biz.pencil.eclipse.editor.pencilBuilder</name>
//<arguments>
//</arguments>
//</buildCommand>
// ---------------------------------------------------------------------


public class IncrementalPencilBuilder extends org.eclipse.core.resources.IncrementalProjectBuilder {

	protected IProject[] build( final int kind, final Map args, final IProgressMonitor monitor ) throws CoreException {
		System.out.println(">>> start my pencil-builder." );
		
		for( Iterator i = args.values().iterator(); i.hasNext(); ) {
			System.out.println(" >>>>>>> argument: " + i.next() );
		}
		
		return null;
	}

     protected void startupOnInitialize() {
    	System.out.println("Pencil-Builderis initialized ... " );
     }
    
     protected void clean(IProgressMonitor monitor) {
    	 System.out.println("Pecnil_builder cleans the project files");
     }

}


