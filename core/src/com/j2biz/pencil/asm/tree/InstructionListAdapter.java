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
package com.j2biz.pencil.asm.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class InstructionListAdapter implements List {

    private final List original;
    
    public InstructionListAdapter(final List originalList) {
        if( originalList == null ) {
            throw new NullPointerException( "parameter:originalList" );
        }
        this.original = originalList;
    }
    /**
     * @see java.util.List#size()
     */
    public int size() {
        return this.original.size();
    }

    /**
     * @see java.util.List#clear()
     */
    public void clear() {
        this.original.clear();
    }

    /**
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return this.original.isEmpty();
    }

    /**
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return this.original.toArray();
    }

    /**
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return this.original.get( index );
    }

    /**
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        return this.original.remove(index);
    }

    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, Object element) {
        this.original.add(index, element);
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return this.original.indexOf(o);
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return this.original.lastIndexOf(o);
    }

    /**
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Object o) {
        return this.original.add(o);
    }

    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return this.original.contains(o);
    }

    /**
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return this.original.remove(o);
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection c) {
        return this.original.addAll(index, c);
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        return this.original.addAll(c);
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        return this.original.containsAll(c);
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        return this.original.removeAll(c);
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        return this.original.retainAll(c);
    }

    /**
     * @see java.util.List#iterator()
     */
    public Iterator iterator() {
        return this.original.iterator();
    }

    /**
     * @see java.util.List#subList(int, int)
     */
    public List subList(int fromIndex, int toIndex) {
        return this.original.subList(fromIndex, toIndex);
    }

    /**
     * @see java.util.List#listIterator()
     */
    public ListIterator listIterator() {
        return this.original.listIterator();
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int index) {
        return this.original.listIterator(index);
    }

    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int index, Object element) {
        return this.original.set(index, element);
    }

    /**
     * @see java.util.List#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a) {
        return this.original.toArray(a);
    }

}
