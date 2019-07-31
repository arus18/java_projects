package trie;

import java.util.LinkedList;
import java.util.List;

public class CompressedTrie {
    private class Trie<E> extends GeneralTree<E>{
        public List<Position<E>> removeChildren(Position<E> p){
            Node<E> n=(Node<E>)p;
            List<Position<E>> l=n.getChildren();
            n.setChildren(new LinkedList<>());
            return l;
        }
        private void addChildren(Position<E> p,List<Position<E>> l){
            Node<E> n=(Node)p;
            if(l!=null)
                for(Position<E> pos:l) {
                	Node<E> node=(Node<E>)pos;
                	node.setParent(n);
                }
            n.setChildren(l);
        }
    }
    Trie<LinkedList<Character>> trie=new Trie();
    Position<LinkedList<Character>> root=trie.addRoot(null);
    public CompressedTrie(){}
    private Position<LinkedList<Character>> find(char[] data,Position<LinkedList<Character>> p,int i,int[] mc){
        int k=0;
        for(Position<LinkedList<Character>> walk:trie.children(p)){
            LinkedList<Character> list=walk.getElement();
            System.out.print(list+"\n");
            for(char c:list){
                if(i<data.length&&c==data[i]){i++;k++;}
                else break;
            }
            mc[0]=k;
            if(i==data.length||(0<k&&k<list.size()))return walk;
            else if(k==list.size())return find(data,walk,i,mc);            
        }
        return p;
    }
    private Position<LinkedList<Character>> find(char[] data,Position<LinkedList<Character>> p,int[] mc1,int[] mc2){
        int i=0;
        while(trie.isInternal(p)){
            int j=trie.numChildren(p);
            for(Position<LinkedList<Character>> walk:trie.children(p)){
                int k=0;
                LinkedList<Character> list=walk.getElement();
                for(char c:list){
                    if(i<data.length&&c==data[i]){i++;k++;}
                    else break;
                }
                mc1[0]=k;mc2[0]=i;
                if(i==data.length||(0<k&&k<list.size()))return walk;
                else if(k==list.size()){p=walk;break;}
                else j--;
            }if(j==0)break;
        }
        return p;
    }
    private void insert(String s){
        int[] mc1=new int[1];
        int[] mc2=new int[1];
        Position<LinkedList<Character>> p=find(s.toCharArray(),root,mc1,mc2);
        if(trie.isRoot(p)){
            LinkedList<Character> list=new LinkedList<>();
            for(char c:s.toCharArray())
                list.add(c);
            trie.addChild(root, list);
        }
        else{
            LinkedList<Character> list1=new LinkedList<>();
            LinkedList<Character> list2=new LinkedList<>();
            LinkedList<Character> list3=p.getElement();
            char[] data=s.toCharArray();
            for(int k=mc2[0];k<data.length;k++)
                list1.addLast(data[k]);
            if(mc1[0]!=0){
                for(int j=list3.size();j>mc1[0];j--)
                    list2.addFirst(list3.removeLast());
                List<Position<LinkedList<Character>>> Children=trie.removeChildren(p);
                Position<LinkedList<Character>> temp=trie.addChild(p, list2);
                trie.addChildren(temp,Children); 
            }
            trie.addChild(p, list1);
        }
    }
    public Position<LinkedList<Character>> find(String s){
        return find(s.toCharArray(),root,new int[1],new int[1]);
    }
    
    public static void main(String[] args){
    	CompressedTrie trie=new CompressedTrie();
    	trie.insert("access");
    	trie.insert("accord");
        System.out.print(trie.trie.root.getChildren().get(0).getElement());
        Position<LinkedList<Character>> p=trie.find("access");
        Position<LinkedList<Character>> p1=trie.find("accord");
        System.out.print(p.getElement());
        System.out.print(p1.getElement());
    }
       
    
}
