package com.j2biz.pencil.eclipse.editor;

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.ui.IEditorPart;

public class CodeCompletionAssist extends CompletionRequestor {

	public void accept( CompletionProposal proposal ) {
		System.out.println( "complete it, baby");
		IEditorPart part = null;
//		part.get
		JavaSourceViewerConfiguration config = new JavaSourceViewerConfiguration(null, null);
		JavaTextTools tools = new JavaTextTools(null);
		
	}

}
