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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * This class is haevy based on the code from the ClassReader of the ASM-Project
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class ClassNameReader {

    /**
     * The type of CONSTANT_Class constant pool items.
     */

    final static int CLASS = 7;

    /**
     * The type of CONSTANT_Fieldref constant pool items.
     */

    final static int FIELD = 9;

    /**
     * The type of CONSTANT_Methodref constant pool items.
     */

    final static int METH = 10;

    /**
     * The type of CONSTANT_InterfaceMethodref constant pool items.
     */

    final static int IMETH = 11;

    /**
     * The type of CONSTANT_String constant pool items.
     */

    final static int STR = 8;

    /**
     * The type of CONSTANT_Integer constant pool items.
     */

    final static int INT = 3;

    /**
     * The type of CONSTANT_Float constant pool items.
     */

    final static int FLOAT = 4;

    /**
     * The type of CONSTANT_Long constant pool items.
     */

    final static int LONG = 5;

    /**
     * The type of CONSTANT_Double constant pool items.
     */

    final static int DOUBLE = 6;

    /**
     * The type of CONSTANT_NameAndType constant pool items.
     */

    final static int NAME_TYPE = 12;

    /**
     * The type of CONSTANT_Utf8 constant pool items.
     */

    final static int UTF8 = 1;

    /**
     * @param b
     */
    public ClassNameReader() {
        ;
    }

    public String readClassName(final InputStream in) throws IOException {
		byte[] buffer = IOUtils.toByteArray(in);
        return readClassName( buffer );
    }

	public int readClassVersion(final byte[] byteBuffer) {
		return readInt(byteBuffer, 4);
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see org.objectweb.asm.ClassReader#accept(org.objectweb.asm.ClassVisitor,
     *      org.objectweb.asm.Attribute[], boolean)
     */
    public String readClassName(final byte[] byteBuffer) {
        int sizeOfConstantPool = readUnsignedShort(byteBuffer, 8);

        int index = 10;
        int max = 0;
        int[] itemIdxs = new int[sizeOfConstantPool];

        for (int i = 1; i < sizeOfConstantPool; ++i) {
            itemIdxs[i] = index + 1;

            int tag = byteBuffer[index];
            int size;
            switch (tag) {
            case FIELD:
            case METH:
            case IMETH:
            case INT:
            case FLOAT:
            case NAME_TYPE:
                size = 5;
                break;
            case LONG:
            case DOUBLE:
                size = 9;
                ++i;
                break;
            case UTF8:
                size = 3 + readUnsignedShort(byteBuffer, index + 1);
                max = (size > max ? size : max);
                break;
            case CLASS:
                size = 3;
                break;
            default:
                size = 3;
                break;
            }

            index += size;
        }

        final char[] stringChars = new char[max];
        final int classInfoIdx = index + 2;

        final String className = readClassName(byteBuffer, itemIdxs,
                classInfoIdx, stringChars);
        
        return className;
    }

    public String readClassName(byte[] byteCode, int[] items, int classInfoIdx,
            final char[] buf) {
        int classNameIdx = items[ readUnsignedShort(byteCode, classInfoIdx) ];
        int utfIdx = items[readUnsignedShort(byteCode, classNameIdx)];

        // reads the length of the string (in bytes, not characters)
        int utfLen = readUnsignedShort(byteCode, utfIdx);
        utfIdx += 2;

        // parses the string bytes
        int endIndex = utfIdx + utfLen;

        int strLen = 0;
        int c, d, e;
        while (utfIdx < endIndex) {
            c = byteCode[utfIdx++] & 0xFF;
            switch (c >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                // 0xxxxxxx
                buf[strLen++] = (char) c;
                break;
            case 12:
            case 13:
                // 110x xxxx 10xx xxxx
                d = byteCode[utfIdx++];
                buf[strLen++] = (char) (((c & 0x1F) << 6) | (d & 0x3F));
                break;
            default:
                // 1110 xxxx 10xx xxxx 10xx xxxx
                d = byteCode[utfIdx++];
                e = byteCode[utfIdx++];
                buf[strLen++] = (char) (((c & 0x0F) << 12) | ((d & 0x3F) << 6) | (e & 0x3F));
                break;
            }
        }

        final String className = new String(buf, 0, strLen);
        return className;
    }

    private int readUnsignedShort(final byte[] byteCode, final int index) {
        return ((byteCode[index] & 0xFF) << 8) | (byteCode[index + 1] & 0xFF);
    }
	
	  public int readInt (final byte[] byteCode, final int index) {
	    return ((byteCode[index] & 0xFF) << 24) |
	           ((byteCode[index + 1] & 0xFF) << 16) |
	           ((byteCode[index + 2] & 0xFF) << 8) |
	           (byteCode[index + 3] & 0xFF);
	  }
}