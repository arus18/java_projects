package huff;



public class Huffman {
    public static Map<Character,Integer> computeFrequency(String s){
        Map<Character,Integer> map=new ProbeHashMap<>();
        char[] data=s.toCharArray();
        for(char c:data)
            map.put(c,1);
        for(int i=0;i<data.length;i++)
            for(int j=0;j<data.length;j++)
                if(j!=i&&data[i]==data[j]){
                    int v=map.get(data[i])+1;
                    map.put(data[i],v);
                    data[i]=Character.MAX_VALUE;
                }
        return map;
    }
    public static LinkedBinaryTree<Character> huffman(String s){
        Map<Character,Integer> map=computeFrequency(s);
        HeapPriorityQueue<Integer,LinkedBinaryTree<Character>> pq=new HeapPriorityQueue<>();
        for(Entry<Character,Integer> e:map.entrySet()){
            LinkedBinaryTree<Character> l=new LinkedBinaryTree<>();
            l.addRoot(e.getKey());
            pq.insert(e.getValue(),l);
        }
        while(pq.size()>1){
            Entry<Integer,LinkedBinaryTree<Character>> e1=pq.removeMin();
            Entry<Integer,LinkedBinaryTree<Character>> e2=pq.removeMin();
            LinkedBinaryTree<Character> l=new LinkedBinaryTree<>();
            Position<Character> Root=l.addRoot(null);
            l.attach(Root,e1.getValue(),e2.getValue());
            pq.insert(e1.getKey()+e2.getKey(),l);
        }
        Entry<Integer,LinkedBinaryTree<Character>> e=pq.removeMin();
        return e.getValue();
    }
    public static void main(String[] args){
        String s="a fast runner need never be afraid of the dark";
        Huffman.huffman(s);
    }
    
}