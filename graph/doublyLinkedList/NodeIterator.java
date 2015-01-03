package graph.doublyLinkedList;

/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* For the latest version of this library please visit: https://github.com/amirbawab/GraphADT
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public interface NodeIterator <E> {
	public E next();
	public boolean hasNext();
	public NodeIterator<E> concatenate(NodeIterator<E> secondIter);
	public int size();
}
