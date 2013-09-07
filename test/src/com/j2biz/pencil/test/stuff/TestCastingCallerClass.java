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
package com.j2biz.pencil.test.stuff;

import com.j2biz.log.LOG;


public class TestCastingCallerClass {

       
    final public TestCascadeInstance1 testVar = new TestCascadeInstance1();
    
    public TestCastingCallerClass() {
         
        LOG.debug( "this.interface.public.instance = ${testVar.field1}" );
        LOG.debug( "this.interface.default.instance = ${testVar.field2}" );
        LOG.debug( "this.class.public.instance = ${testVar.field3}");
        LOG.debug( "this.class.default.instance = ${testVar.field4}" );
        
        // >>> Direktes Cascading:
        LOG.debug( "parent.interface.public.instance = ${(com.j2biz.pencil.test.stuff.TestCastingInstance3):testVar.field1}" );
        LOG.debug( "parent.class.public.instance = ${(com.j2biz.pencil.test.stuff.TestCastingInstance2):testVar.field3}" );
        
        // statische Variablen werden eigentlich ueber direct-caller abgeholt.. sollte diese Variante dennoch funktionieren ?
//          new StringBuffer ().append( ((com.j2biz.pencil.test.stuff.TestCastingInstance3) testVar).field1 );
           
         // direct in der casting-klasse nachshauen..
          // voraussetzung die casting-klasse ist in der vererbungshierarchie der aktuellen klasse vorhnaden
//          new StringBuffer ().append( ((TestCastingInstance2) testVar).field3 );
    }
}
