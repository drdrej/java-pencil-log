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

package com.j2biz.log;
 

/** 
 * This class is a simple logger-wrapper.
 * It contains no code, cause its nothing do. 
 * A programmer should use this class to mark a logger-call.
 * An enhancer replace this marker with logging-code.
 * 
 * Example:
 * <code>
 *     LOG.debug( "this is a test" );
 * </code>  
 * will be translated into a snipet like this:
 * <code>
 *     if( LOGGER.isDebugEnabled() ) {
 *        try {
 *           LOGGER.debug( "this is a test" );
 *        } catch (Throwable x) {
 * 			;
 *        } 
 *     }
 * </code>
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 * 
 * @author Andreas Siebert 
 * 
 */
/**
 * @author andrej
 *
 */
/**
 * @author andrej
 *
 */
public final class LOG {

    public static final void debug(String message) {
        ;
    }
    
    public static final void fatal(String message) {
        ;
    }
    
    public static final void error(String message) {
        ;
    }
    
    public static final void info(String message) {
        ;
    }
    
    public static final void trace(String message) {
        ;
    }
    
}
