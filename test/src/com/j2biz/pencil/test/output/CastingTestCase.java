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

package com.j2biz.pencil.test.output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.j2biz.pencil.test.stuff.TestCastingCallerClass;
import com.j2biz.pencil.test.stuff.TestCastingInstance2;
import com.j2biz.pencil.test.stuff.TestCastingInstance3;


/**
 * @author andrej
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CastingTestCase extends TestCase {

    public void testCasting() throws Exception {
        PrintStream oldOut = System.err;
        FileOutputStream dropedOut = new FileOutputStream( "testCascadingVars.out" );
        PrintStream newOut = new PrintStream(dropedOut);
        System.setErr(  newOut );
        
        TestCastingCallerClass myVar = new TestCastingCallerClass();
        
        System.setErr(oldOut);
        newOut.flush();
        newOut.close();
        
        Properties props = new Properties();
        props.load(new FileInputStream("testCascadingVars.out"));
        
        assertEquals( props.getProperty( "parent.class.public.instance" ),  ((TestCastingInstance2) myVar.testVar).field3 );
        assertEquals(props.getProperty( "parent.interface.public.instance" ), ((TestCastingInstance3) myVar.testVar).field1 );
    }
}
