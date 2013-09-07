package com.j2biz.pencil.eclipse.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;

public class QuickAssistTest implements IQuickAssistProcessor {

	public boolean hasAssists( IInvocationContext context ) throws CoreException {
		System.out.println("gogogo");
		return true;
	}

	public IJavaCompletionProposal[] getAssists( IInvocationContext context, IProblemLocation[] locations ) throws CoreException {
		System.out.println("gugugu");
		return new IJavaCompletionProposal[0];
	}

}
