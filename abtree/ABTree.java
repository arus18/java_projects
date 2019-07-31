package abtree;

import java.util.LinkedList;
import java.util.Queue;

public class ABTree<K,V> {
    protected static class Node<K,V> {
        private SortedTableMap<K,V> element;
        private Node<K,V> parent;
        Position<Node<K,V>> pos;
        private LinkedPositionalList<Node<K,V>> children=new LinkedPositionalList<>();
        public Node(SortedTableMap<K,V> e,Node<K,V> above){
            element=e;
            parent=above;
        }
        public Position<Node<K,V>> getPosition(){return pos;}
        public void setPosition(Position<Node<K,V>> p){pos=p;}
        public SortedTableMap<K,V> getElement(){return element;}
        public Node<K,V> getParent(){return parent;}
        public void setElement(SortedTableMap<K,V> s){element=s;}
        public void setParent(Node<K,V> n){parent=n;}
        public Position<Node<K,V>> addFirst(Node<K,V> n){
            return children.addFirst(n);
        }
        public Position<Node<K,V>> addLast(Node<K,V> n){
            return children.addLast(n);
        }
        public Position<Node<K,V>> addAfter(Position<Node<K,V>> p,Node<K,V> n){
            return children.addAfter(p, n);
        }
        public Position<Node<K,V>> addBefore(Position<Node<K,V>> p,Node<K,V> n){
            return children.addBefore(p, n);
        }
        public Position<Node<K,V>> after(Position<Node<K,V>> p){
            return children.after(p);
        }
        public Position<Node<K,V>> before(Position<Node<K,V>> p){
            return children.before(p);
        }
        public Position<Node<K,V>> first(){return children.first();}
        public Position<Node<K,V>> last(){return children.last();}
        public Node<K,V> remove(Position<Node<K,V>> p){return children.remove(p);}
        public Iterable<Position<Node<K,V>>> positions(){return children.positions();}
        public int size(){return children.size();}
        public boolean isInternal(){return size()>0;}
    }
    public ABTree(int a,int b){
        this.a=a;
        this.b=b;        
    }
    private Node<K,V> root= new Node(new SortedTableMap<>(),null);
    private double a;
    private double b;
    private int size;
    public void splitNode(Node<K,V> n){
        if(root==n){
            n.setParent(new Node(new SortedTableMap<>(),null));
            root=n.getParent();
            Position<Node<K,V>> p=root.addFirst(n);
            n.setPosition(p);
        }
        SortedTableMap<K,V> map=n.getElement();
        Node<K,V> parent=n.getParent();
        Node<K,V> newNode=new Node(new SortedTableMap<>(),parent);
        int median=(int) Math.ceil((b+1)/2);
        if(n.isInternal()){
            for(int i=median;i<=b;i++){
                Node<K,V> child=n.remove(n.last());
                child.setParent(newNode);
                Position<Node<K,V>> pos=newNode.addFirst(child);
                child.setPosition(pos);
            }
        }
        for(int i=median;i<b;i++){
            Entry<K,V> e=map.lastEntry();
            map.remove(e.getKey());
            newNode.getElement().put(e.getKey(), e.getValue());
        }
        Entry<K,V> pe=map.lastEntry();
        map.remove(pe.getKey());
        parent.getElement().put(pe.getKey(), pe.getValue());
        newNode.setParent(parent);
        Position<Node<K,V>> pos=parent.addAfter(n.getPosition(), newNode);
        newNode.setPosition(pos);
    }
    public boolean transfer(Position<Node<K,V>> p, K key,int t){
        Node<K,V> parent=p.getElement().getParent();
        SortedTableMap<K,V> map=p.getElement().getElement();
        SortedTableMap<K,V> parentMap=parent.getElement();
        Position<Node<K,V>> before=parent.before(p);
        Position<Node<K,V>> after=parent.after(p);
        if(before==null){
            Node<K,V> afterNode=after.getElement();
            SortedTableMap<K,V> siblingMap=afterNode.getElement();
            if(siblingMap.size()>(a-1)){
                if(p.getElement().isInternal()){
                    Node<K,V> child=afterNode.remove(afterNode.first());
                    child.setParent(p.getElement());
                    Position<Node<K,V>> pos=p.getElement().addLast(child);
                    child.setPosition(pos);
                }
                Entry<K,V> e=siblingMap.ceilingEntry(key);
                siblingMap.remove(e.getKey());
                Entry<K,V> pe=parentMap.ceilingEntry(key);
                parentMap.remove(pe.getKey());
                map.put(pe.getKey(), pe.getValue());
                parentMap.put(e.getKey(), e.getValue());
                return true;
            }
        }
        else if(after==null){
            Node<K,V> beforeNode=before.getElement();
            SortedTableMap<K,V> siblingMap=beforeNode.getElement();
            if(siblingMap.size()>(a-1)){
                if(p.getElement().isInternal()){
                    Node<K,V> child=beforeNode.remove(beforeNode.last());
                    child.setParent(p.getElement());
                    Position<Node<K,V>> pos=p.getElement().addFirst(child);
                    child.setPosition(pos);
                }
                Entry<K,V> e=siblingMap.lowerEntry(key);
                siblingMap.remove(e.getKey());
                Entry<K,V> pe=parentMap.lowerEntry(key);
                parentMap.remove(pe.getKey());
                map.put(pe.getKey(), pe.getValue());
                parentMap.put(e.getKey(), e.getValue());
                return true;
            }    
        }
        else{
            Node<K,V> beforeNode=before.getElement();
            Node<K,V> afterNode=after.getElement();
            SortedTableMap<K,V> leftSiblingMap=beforeNode.getElement();
            SortedTableMap<K,V> rightSiblingMap=afterNode.getElement();
            if(leftSiblingMap.size()>(a-1)){
                if(p.getElement().isInternal()){
                    Node<K,V> child=beforeNode.remove(beforeNode.last());
                    child.setParent(p.getElement());
                    Position<Node<K,V>> pos=p.getElement().addFirst(child);
                    child.setPosition(pos);
                }
                Entry<K,V> e=leftSiblingMap.lowerEntry(key);
                
                leftSiblingMap.remove(e.getKey());
                Entry<K,V> pe=parentMap.lowerEntry(key);
                if(t==67)System.out.print(e.getKey()+" "+pe.getKey()+"\n");
                parentMap.remove(pe.getKey());
                if(t==67)System.out.print(parentMap.size());
                map.put(pe.getKey(), pe.getValue());
                parentMap.put(e.getKey(), e.getValue());
                return true;
            }
            else if(rightSiblingMap.size()>(a-1)){
                
                if(p.getElement().isInternal()){
                    Node<K,V> child=afterNode.remove(afterNode.first());
                    child.setParent(p.getElement());
                    Position<Node<K,V>> pos=p.getElement().addLast(child);
                    child.setPosition(pos);
                }
                Entry<K,V> e=rightSiblingMap.ceilingEntry(key);
                rightSiblingMap.remove(e.getKey());
                Entry<K,V> pe=parentMap.ceilingEntry(key);
                parentMap.remove(pe.getKey());
                map.put(pe.getKey(), pe.getValue());
                parentMap.put(e.getKey(), e.getValue());
                return true;
            }
        }
        return false;
    }
    public void fusion(Position<Node<K,V>> p,K key,int t){
        SortedTableMap<K,V> map=p.getElement().getElement();
        Node<K,V> parent=p.getElement().getParent();
        Position<Node<K,V>> before=parent.before(p);
        Position<Node<K,V>> after=parent.after(p);
        if(before==null){
            Node<K,V> afterNode=after.getElement();
            SortedTableMap<K,V> siblingMap=afterNode.getElement();
            for(Entry<K,V> e:siblingMap.entrySet()){
                map.put(e.getKey(), e.getValue());
            }
            for(Position<Node<K,V>> pos:afterNode.positions()){
                Position<Node<K,V>> position=p.getElement().addLast(pos.getElement());
                pos.getElement().setParent(p.getElement());
                pos.getElement().setPosition(position);
            }
            parent.remove(after);
            Entry<K,V> pe=parent.getElement().ceilingEntry(key);
            parent.getElement().remove(pe.getKey());
            map.put(pe.getKey(), pe.getValue());
        }else{
            if(t==82)System.out.print("f");
            Node<K,V> beforeNode=before.getElement();
            SortedTableMap<K,V> siblingMap=beforeNode.getElement();
            for(Entry<K,V> e:siblingMap.entrySet()){
                map.put(e.getKey(), e.getValue());
            }
            Position<Node<K,V>> q=p.getElement().first();
            for(Position<Node<K,V>> pos:beforeNode.positions()){
                Position<Node<K,V>> position=p.getElement().addBefore(q,pos.getElement());
                pos.getElement().setParent(p.getElement());
                pos.getElement().setPosition(position);
            }
            parent.remove(before);
            Entry<K,V> pe=parent.getElement().lowerEntry(key);
            parent.getElement().remove(pe.getKey());
            map.put(pe.getKey(), pe.getValue());
        }
    }
    private Node<K,V> treeSearch(K key){
        Node<K,V> walk=root;
        while(walk.isInternal()){
            if(walk.getElement().get(key)!=null)
                return walk;
            for(Position<Node<K,V>> p:walk.positions()){
                SortedTableMap<K,V> map=p.getElement().getElement();
                if(map.ceilingEntry(key)!=null){
                    walk=p.getElement();
                    break;
                }else{
                    Node<K,V> n=p.getElement();
                    if(n.isInternal()){
                        Node<K,V> last=n.last().getElement();
                        if(last.getElement().ceilingEntry(key)!=null){
                            walk=last;
                            break;
                        }
                    }
                    walk=p.getElement();
                }
            }
        }
        return walk;
    }
    private Node<K,V> predecessor(Node<K,V> n,K key,int t){
        Node<K,V> walk=n;
        while(walk.isInternal()){
            SortedTableMap<K,Node<K,V>> temp=new SortedTableMap<>();
            for(Position<Node<K,V>> q:walk.positions()){
                for(Entry<K,V> e:q.getElement().getElement().entrySet()){
                    if(t==82)System.out.print(e.getKey()+" ");
                    temp.put(e.getKey(),q.getElement());
                }
            }
            walk=temp.lowerEntry(key).getValue();
        }
        return walk;
    }
    public V put(K key,V value){
        Node<K,V> n=treeSearch(key);
        SortedTableMap<K,V> map=n.getElement();
        V v=map.put(key, value);
        while(map.size()>(b-1)){
            splitNode(n);
            n=n.getParent();
            map=n.getElement();
        }
        size++;
        return v;
    }
    public V remove(K key,int t){
        Node<K,V> n=treeSearch(key);
        SortedTableMap<K, V> map;
        V v;
        if(n.isInternal()){
            Node<K,V> predecessor=predecessor(n,key,0);
            V value=n.getElement().remove(key);
            Entry<K,V> e=predecessor.getElement().lowerEntry(key);
            predecessor.getElement().remove(e.getKey());
            predecessor.getElement().put(key, value);
            n.getElement().put(e.getKey(), e.getValue());
            map=predecessor.getElement();
            v=map.remove(key);
            key=e.getKey();
            n=predecessor;
        }else{
            map=n.getElement();
            v=map.remove(key);
        }
        while(n!=root&&map.size()<(a-1)){
            if(!transfer(n.getPosition(),key,t)){
                fusion(n.getPosition(), key,t);
                n=n.getParent();
                map=n.getElement();
              
            }
            if(root.getElement().isEmpty()){
                Node<K,V> child=n.first().getElement();
                root=child;
                child.setParent(null);
                break;
            }
        }
        size--;
        return v;
    }
    public void printTree(){
        Queue<LinkedList<Node<K,V>>> q=new LinkedList();
        LinkedList<Node<K,V>> list=new LinkedList();
        list.add(root);
        q.add(list);
        while(!q.isEmpty()){
            LinkedList<Node<K,V>> l=q.remove();
            LinkedList<Node<K,V>> temp=new LinkedList<>();
            for(Node<K,V> n:l){
                for(Entry<K,V> e:n.getElement().entrySet())
                    System.out.print(e.getKey()+",");
                System.out.print("  ");
                for(Position<Node<K,V>> p:n.positions())
                    temp.add(p.getElement());
            }
            System.out.print("\n");
            if(!temp.isEmpty())q.add(temp);
        }
    }

    
    public static void main(String[] args){
        ABTree<Integer,Integer> ab=new ABTree<>(7,15);
        for(int i=1;i<1000;i++)
            ab.put(i, i);
        for(int i=500;i>10;i--)
            ab.remove(i, 0);
        ab.printTree();
    }
    
    
    
}