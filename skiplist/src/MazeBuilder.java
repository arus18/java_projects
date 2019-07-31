import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MazeBuilder {
    public static String mazeBuilder(int n){
        Vertex<Integer>[][] matrix=(Vertex<Integer>[][])new Vertex[n][n];
        Graph<Integer,Integer> graph=new AdjacencyMapGraph<>(false);
        Random rand=new Random();
        int index=0;
        List<Edge<Integer>> spTree=new LinkedList<>();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                matrix[i][j]=graph.insertVertex(++index);
        for(int i=0;i<n;i++)
            for(int j=0;j<n-1;j++){
                graph.insertEdge(matrix[i][j], matrix[i][j+1], rand.nextInt(100));
                graph.insertEdge(matrix[j][i], matrix[j+1][i], rand.nextInt(100));
            }
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                sb.append("|");
                sb.append("_");
                if(j==n-1)sb.append("|");
            }
            sb.append("\n");
        }
        PositionalList<Edge<Integer>> tempList=AdjacencyMapGraph.MST(graph);
        for(Position<Edge<Integer>> pos:tempList.positions())
        	spTree.add(pos.getElement());
        for(int i=0;i<n;i++){
            for(int j=0;j<n-1;j++){
                Vertex<Integer> u=matrix[i][j];
                Vertex<Integer> v=matrix[i][j+1];
                Edge<Integer> e=graph.getEdge(u, v);
                Vertex<Integer> u1=matrix[j][i];
                Vertex<Integer> v1=matrix[j+1][i];
                Edge<Integer> e1=graph.getEdge(u1, v1);
                if(spTree.contains(e)){
                    int value=u.getElement();
                    int temp=value-1+value+i*2+1;
                    sb.deleteCharAt(temp);
                    sb.insert(temp," ");
                }
                if(spTree.contains(e1)){
                    int value=u1.getElement();
                    int temp=value-1+value+j*2;
                    sb.deleteCharAt(temp);
                    sb.insert(temp," ");
                   
                }
            }
        }
        return sb.toString();
    }
    public static void main(String[] args) {
       System.out.print(MazeBuilder.mazeBuilder(10));
    }
    
    
    
}