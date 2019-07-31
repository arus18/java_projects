import java.util.Random;




public class TicTacToe {
    public static final int X = 1, O = -1;
    public static final int EMPTY = 0;
    private int board[][] = new int[3][3];
    private int player;
    private GeneralTree<int[][]> gameTree = new GeneralTree<>();
    private Position<int[][]> root = gameTree.addRoot(board);
    private Position<int[][]> nextMove = root;
    private Position<int[][]> lastMove=null;

    public TicTacToe() {
        clearBoard();
    }

    public void gameConfiguration(Position<int[][]> p) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (p.getElement()[i][j] == 0) {
                    Position<int[][]> child = gameTree.addChild(p, arrayCopy(p.getElement()));
                    if(gameTree.depth(p)%2==0)
                        child.getElement()[i][j]=+player;
                    else
                        child.getElement()[i][j]=-player;
                }
            }
        }
        for (Position<int[][]> c : gameTree.children(p)) {
            gameConfiguration(c);
        }
    }

    public void createTree() {
        gameConfiguration(root);
    }

    private Position<int[][]> minPosition(Position<int[][]> p) {
        int min = 0;
        Position<int[][]> minPosition = null;
        for (Position<int[][]> c : gameTree.children(p)) {
            if (gameTree.depth(c) <= 1) {
                minPosition = random(p);
            } else {
                int[][] temp = c.getElement();
                if (min(temp) < min) {
                    min = min(temp);
                    minPosition = c;
                }
            }
        }
        return minPosition;
    }

    private int min(int[][] board) {
        int total;
        int min = 0;
        total = board[0][0] + board[0][1] + board[0][2];
        min = Math.min(min, total);
        total = board[1][0] + board[1][1] + board[1][2];
        min = Math.min(min, total);
        total = board[2][0] + board[2][1] + board[2][2];
        min = Math.min(min, total);
        total = board[0][0] + board[1][0] + board[2][0];
        min = Math.min(min, total);
        total = board[0][1] + board[1][1] + board[2][1];
        min = Math.min(min, total);
        total = board[0][2] + board[1][2] + board[2][2];
        min = Math.min(min, total);
        total = board[0][0] + board[1][1] + board[2][2];
        min = Math.min(min, total);
        total = board[2][0] + board[1][1] + board[0][2];
        min = Math.min(min, total);
        return min;
    }

    private Position<int[][]> random(Position<int[][]> p) {
        Random rand = new Random();
        int r = rand.nextInt(gameTree.numChildren(p));
        int temp = 0;
        for (Position<int[][]> c : gameTree.children(p)) {
            if (temp == r) {
                return c;
            }
            temp++;
        }
        return null;
    }
    private boolean isEqual(int[][] board,int[][] board1){
        int count=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(board[i][j]==board1[i][j])
                    count++;
        return count==9;
    }

    private Position<int[][]> findPosition(Position<int[][]> p) {
        for (Position<int[][]> c : gameTree.children(p)) {
            int count = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == c.getElement()[i][j]) {
                        count++;
                    }
                }
            }
            if (count == 9) {
                return c;
            }
        }
        return null;
    }

    public int[][] arrayCopy(int[][] array) {
        int[][] arrayCopy = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayCopy[i][j] = array[i][j];
            }
        }
        return arrayCopy;
    }

    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
        player = X;
    }

    private boolean hasWinningChance(int[][] board) {
        return (((board[0][0] == 1 && board[0][1] == 1 && board[0][2] == 0)
                || (board[0][0] == 1 && board[0][1] == 0 && board[0][2] == 1)
                || (board[0][0] == 0 && board[0][1] == 1 && board[0][2] == 1))
                || ((board[1][0] == 1 && board[1][1] == 1 && board[1][2] == 0)
                || (board[1][0] == 1 && board[1][1] == 0 && board[1][2] == 1)
                || (board[1][0] == 0 && board[1][1] == 1 && board[1][2] == 1))
                || ((board[2][0] == 1 && board[2][1] == 1 && board[2][2] == 0)
                || (board[2][0] == 1 && board[2][1] == 0 && board[2][2] == 1)
                || (board[2][0] == 0 && board[2][1] == 1 && board[2][2] == 1))
                || ((board[0][0] == 1 && board[1][0] == 1 && board[2][0] == 0)
                || (board[0][0] == 1 && board[1][0] == 0 && board[2][0] == 1)
                || (board[0][0] == 0 && board[1][0] == 1 && board[2][0] == 1))
                || ((board[0][1] == 1 && board[1][1] == 1 && board[2][1] == 0)
                || (board[0][1] == 1 && board[1][1] == 0 && board[2][1] == 1)
                || (board[0][1] == 0 && board[1][1] == 1 && board[2][1] == 1))
                || (((board[0][2] == 1) && (board[1][1] == 1) && (board[2][0] == 0))
                || (board[0][2] == 1 && board[1][2] == 0 && board[2][2] == 1)
                || (board[0][2] == 0 && board[1][2] == 1 && board[2][2] == 1))
                || ((board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 0)
                || (board[0][0] == 1 && board[1][1] == 0 && board[2][2] == 1)
                || (board[0][0] == 0 && board[1][1] == 1 && board[2][2] == 1))
                || ((board[2][0] == 1 && board[1][1] == 1 && board[0][2] == 0)
                || (board[2][0] == 1 && board[1][1] == 0 && board[0][2] == 1)
                || (board[2][0] == 0 && board[1][1] == 1 && board[0][2] == 1)));
    }

    public Position<int[][]> defend(Position<int[][]> p) {
        for (Position<int[][]> c : gameTree.children(p)) {
            int[][] temp = c.getElement();
            if (((temp[0][0] == 1 && temp[0][1] == 1 && temp[0][2] == -1)
                    || (temp[0][0] == 1 && temp[0][1] == -1 && temp[0][2] == 1)
                    || (temp[0][0] == -1 && temp[0][1] == 1 && temp[0][2] == 1))
                    || ((temp[1][0] == 1 && temp[1][1] == 1 && temp[1][2] == -1)
                    || (temp[1][0] == 1 && temp[1][1] == -1 && temp[1][2] == 1)
                    || (temp[1][0] == -1 && temp[1][1] == 1 && temp[1][2] == 1))
                    || ((temp[2][0] == 1 && temp[2][1] == 1 && temp[2][2] == -1)
                    || (temp[2][0] == 1 && temp[2][1] == -1 && temp[2][2] == 1)
                    || (temp[2][0] == -1 && temp[2][1] == 1 && temp[2][2] == 1))
                    || ((temp[0][0] == 1 && temp[1][0] == 1 && temp[2][0] == -1)
                    || (temp[0][0] == 1 && temp[1][0] == -1 && temp[2][0] == 1)
                    || (temp[0][0] == -1 && temp[1][0] == 1 && temp[2][0] == 1))
                    || ((temp[0][1] == 1 && temp[1][1] == 1 && temp[2][1] == -1)
                    || (temp[0][1] == 1 && temp[1][1] == -1 && temp[2][1] == 1)
                    || (temp[0][1] == -1 && temp[1][1] == 1 && temp[2][1] == 1))
                    || ((temp[0][2] == 1 && temp[1][1] == 1 && temp[2][0] == -1)
                    || (temp[0][2] == 1 && temp[1][2] == -1 && temp[2][2] == 1)
                    || (temp[0][2] == -1 && temp[1][2] == 1 && temp[2][2] == 1))
                    || ((temp[0][0] == 1 && temp[1][1] == 1 && temp[2][2] == -1)
                    || (temp[0][0] == 1 && temp[1][1] == -1 && temp[2][2] == 1)
                    || (temp[0][0] == -1 && temp[1][1] == 1 && temp[2][2] == 1))
                    || ((temp[2][0] == 1 && temp[1][1] == 1 && temp[0][2] == -1)
                    || (temp[2][0] == 1 && temp[1][1] == -1 && temp[0][2] == 1)
                    || (temp[2][0] == -1 && temp[1][1] == 1 && temp[0][2] == 1))) {
                return c;
            }
        }
        return null;
    }

    public void putMark(int i, int j) {
        if ((i < 0) || (i > 2) || (j < 0) || (j > 2)) {
            System.out.print("Invalid board position");
        }
        if (board[i][j] != EMPTY) {
            System.out.print("Board position occupied");
        }
        board[i][j] = player;
        nextMove = findPosition(nextMove);
        if(hasWinningChance(nextMove.getElement())){
            nextMove=defend(nextMove);
            board=nextMove.getElement();
        }
        else{
            nextMove = minPosition(nextMove);
            board = nextMove.getElement();
        }
        
    }

    public boolean isWin(int mark) {
        return ((board[0][0] + board[0][1] + board[0][2] == mark * 3)
                || (board[1][0] + board[1][1] + board[1][2] == mark * 3)
                || (board[2][0] + board[2][1] + board[2][2] == mark * 3)
                || (board[0][0] + board[1][0] + board[2][0] == mark * 3)
                || (board[0][1] + board[1][1] + board[2][1] == mark * 3)
                || (board[0][2] + board[1][2] + board[2][2] == mark * 3)
                || (board[0][0] + board[1][1] + board[2][2] == mark * 3)
                || (board[2][0] + board[1][1] + board[0][2] == mark * 3));
    }

    public int winner() {
        if (isWin(X)) {
            return (X);
        } else if (isWin(O)) {
            return (O);
        } else {
            return (O);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (board[i][j]) {
                    case X:
                        sb.append("X");
                        break;
                    case O:
                        sb.append("O");
                        break;
                    case EMPTY:
                        sb.append(" ");
                        break;
                }
                if (j < 2) {
                    sb.append("|");
                }
            }
            if (i < 2) {
                sb.append("\n-----\n");
            }
        }
        return sb.toString();
    }
   

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.createTree();
        game.putMark(0,0);
        game.putMark(1, 0);
        game.putMark(1, 1);
        System.out.print(game.toString());
    }
}

