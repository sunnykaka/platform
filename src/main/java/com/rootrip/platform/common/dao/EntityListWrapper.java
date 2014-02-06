package com.rootrip.platform.common.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author liubin
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class EntityListWrapper<E extends Entity> implements List<E>{

	// 被包装的列表对象
	private List<E> list;
	
	/**
	 * 构造方法传入被包装的列表对象
	 * @param list 列表对象
	 */
	public EntityListWrapper(List<E> list){
		this.list = list;
	}
	
	public Object[] getEntitiesId(){
		Object[] idArray = new Object[this.list.size()];
		for(int i = 0; i < this.size(); i++){
			idArray[i] = this.get(i).getId();
		}
		return idArray;
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(E o) {
		return list.add(o);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, E element) {
		list.add(index, element);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		return list.addAll(index, c);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return list.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return list.equals(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public E get(int index) {
		return list.get(index);
	}

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		return list.hashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<E> iterator() {
		return list.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public E remove(int index) {
		return list.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return list.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public E set(int index, E element) {
		return list.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return list.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return list.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	
}
