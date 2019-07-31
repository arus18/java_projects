package trie;
import java.util.LinkedList;
import java.util.List;


public class GeneralTree<E> {
	   protected static class Node<E> implements Position<E> {
		   private E element;
		   private Node<E> parent;
		   private List<Position<E>> children;
		   public Node(E e,Node<E> above) {
			   element=e;
			   parent=above;
			   children=new LinkedList<>();
		   }
		   public List<Position<E>> getChildren(){return children;}
		   public void setChildren(List<Position<E>> l) {children=l;}
		   public void setParent(Node<E> n) {parent=n;}
		@Override
		public E getElement() throws IllegalStateException {
			// TODO Auto-generated method stub
			return element;
		}
		
	   }
	   public GeneralTree() {}
	   protected Node<E> root=null;
	   public Position<E> root(){return root;}
	   public boolean isRoot(Position<E> p) {return p==root();}
	   public Position<E> parent(Position<E> p){
		   Node<E> node=(Node<E>)p;
		   return node.parent;
	   }
	   public List<Position<E>> children(Position<E> p){
		   Node<E> node=(Node<E>)p;
		   return node.children;
	   }
	   public Position<E> addRoot(E e){
		   root=new Node(e,null);
		   return root;
	   }
	   public Position<E> addChild(Position<E> p,E e){
		   Node<E> node=(Node<E>)p;
		   Node<E> child=new Node(e,node);
		   node.children.add(child);
		   return child;
	   }
	   public int depth(Position<E> p) {
		   if(isRoot(p))return 0;
		   else return 1+ depth(parent(p));
	   }
	   public int numChildren(Position<E> p) {
		   Node<E> node=(Node<E>)p;
		   return node.children.size();
	   }
	   public boolean isInternal(Position<E> p) {
		   Node<E> node=(Node<E>)p;
		   return !node.children.isEmpty();
	   }
	   public static void main(String[] args) {
		   GeneralTree<Integer> tree=new GeneralTree();
		   tree.addRoot(8);
	   }
	   
	}


