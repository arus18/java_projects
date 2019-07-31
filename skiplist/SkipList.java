package slist;

import java.util.Random;

public class SkipList<V> {
    protected static class Node<Integer,V>{
        private MapEntry<Integer,V> element;
        private Node<Integer,V> prev;
        private Node<Integer,V> next;
        private Node<Integer,V> above;
        private Node<Integer,V> below;
        public Node(MapEntry<Integer,V> e,Node n,Node p,Node a,Node b){
            element=e;
            next=n;
            prev=p;
            above=a;
            below=b;
        }
        public MapEntry<Integer,V> getElement(){return element;}
        public Node<Integer,V> getPrev(){return prev;}
        public Node<Integer,V> getNext(){return next;}
        public Node<Integer,V> getAbove(){return above;}
        public Node<Integer,V> getBelow(){return below;}
        public void setElement(MapEntry n){
            element=n;
        }
        public void setPrev(Node<Integer,V> n){
            prev=n;
        }
        public void setNext(Node<Integer,V> n){
            next=n;
        }
        public void setAbove(Node<Integer,V> n){
            above=n;
        }
        public void setBelow(Node<Integer,V> n){
            below=n;
        }
        
    }
    int height=0;
    int size=0;
    Node<Integer,V> head;
    Node<Integer,V> tail;
    public Node<Integer,V> next(Node n){
        return n.getNext();
    }
    public Node<Integer,V> prev(Node n){
        return n.getPrev();
    }
    public Node<Integer,V> above(Node n){
        return n.getAbove();
    }
    public Node<Integer,V> below(Node n){
        return n.getBelow();
    }
    public SkipList(){
        head=new Node<>(new MapEntry(Integer.MIN_VALUE,null),null,null,null,null);
        tail=new Node<>(new MapEntry(Integer.MAX_VALUE,null),null,null,null,null);
        head.setNext(tail);
        tail.setPrev(head);
    }
    protected Node<Integer,V> skipSearch(Integer key){
        Node<Integer,V> node=head;
        while(below(node)!=null){
            node=node.getBelow();
            while(next(node).getElement().getKey()<=key){
                node=node.getNext();
            }
        }
        return node;
    }
    protected Node<Integer,V> insertAfterAbove(Node n,Node m,Integer key,V v){
        Node<Integer,V> newest=new Node(new MapEntry(key,v),null,null,null,null);
        Node<Integer,V> next=null;
        if(n!=null){
            next=n.getNext();
            n.setNext(newest);
            if(next!=null)
                next.setPrev(newest);
        }
        if(m!=null){
            m.setAbove(newest);
            newest.setBelow(m);
        }
        newest.setPrev(n);
        newest.setNext(next);
        return newest;
    }
    protected Node<Integer,V> skipInsert(Integer key,V value){
        Random rand=new Random();
        Node p=skipSearch(key);
        Node q=null;
        int i=-1;
        do{
            i=i+1;
            if(i>=height){
                height=height+1;
                Node t=head.getNext();
                head=insertAfterAbove(null,head,Integer.MIN_VALUE,null);
                insertAfterAbove(head,t,Integer.MAX_VALUE,null);
            }
            q=insertAfterAbove(p,q,key,value);
            while(p.getAbove()==null)
                p=p.getPrev();
            p=p.getAbove();
        }while(rand.nextBoolean());
        size=size+1;
        return q;
    }
    protected Node<Integer,V> remove(int key){
        Node<Integer,V> p=skipSearch(key);
        if(p.getElement().getKey()!=key)
            return null;
        else
            do{
                p.getPrev().setNext(p.getNext());
                p.getNext().setPrev(p.getPrev());
                p=above(p);
            }while(p!=null);
        size--;
        return p;
    }
    public static void main(String[] args){
       SkipList<Integer> skiplist=new SkipList<>();
       for(int i=0;i<1000;i++) {
    	   skiplist.skipInsert(i, i);
       }
       skiplist.remove(560);
    }
}
