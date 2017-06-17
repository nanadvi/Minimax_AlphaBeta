import java.util.*;

/**
 * Jason Zhang 		999230758
 * Navid Al Nadvi 	912309925
 */
public class minimax_jason_navid extends AIModule {
    private int my_player;
    private int my_opponent;
    private int[][] currentBoard;

    @Override
    public void getNextMove(final GameStateModule state){
        int depth = 6;
        final GameStateModule current = state.copy();
        this.currentBoard = makeArray(current);
        this.my_player = current.getActivePlayer();

        if(my_player == 1) {
            my_opponent = 2;
        }
        else {
            my_opponent = 1;
        }

        minimax(current, depth);
    }
    public void minimax(final GameStateModule game, int depth){
        ArrayList<Integer>legalMoves = new ArrayList<Integer>();
        int bestCall = 0;
        int utility = Integer.MIN_VALUE;
        int utility_aux;
        for(int i = 0; i < game.getWidth(); i++){
            if(game.canMakeMove(i)){
                legalMoves.add(i);
            }
        }

        for (int moves : legalMoves) {
            game.makeMove(moves);
            utility_aux = minvalue(game, depth-1) + middleBias(game,moves);

            // System.out.println("utility_aux: " + utility_aux + " column: " + moves);
            if(utility_aux >= utility){
                bestCall = moves;
                utility = utility_aux;
            }
            game.unMakeMove();
        }
        // printArray(currentBoard);
        // System.out.println("utility: " + utility+ " chosen move: " + bestCall + " player: " + my_player + " opponent: " + my_opponent);
        chosenMove = bestCall;
    }

    public int minvalue(final GameStateModule game, int depth){
        ArrayList<Integer>legalMoves = new ArrayList<Integer>();
        int min = Integer.MAX_VALUE;
        int min_aux;

        if(depth <= 0 || game.isGameOver()){
            return  evaluation(game, my_opponent);
            //return 0;
        }
        for(int i = 0; i < game.getWidth(); i++){
            if(game.canMakeMove(i)){
                legalMoves.add(i);
            }
        }
        for (int moves : legalMoves) {
            game.makeMove(moves);
            min_aux = maxvalue(game, depth-1);
            if(min_aux <= min){
                min = min_aux;

            }
            game.unMakeMove();
        }
        return min;
    }

    public int maxvalue(final GameStateModule game, int depth){
        ArrayList<Integer>legalMoves = new ArrayList<Integer>();
        int max = Integer.MIN_VALUE;
        int max_aux = 0;

        if(depth <= 0 || game.isGameOver()){
            // eval function when game is over
            return evaluation(game, my_player);
        }
        for(int i = 0; i < game.getWidth(); i++){
            if(game.canMakeMove(i)){
                legalMoves.add(i);
            }
        }
        for (int moves : legalMoves) {
            game.makeMove(moves);
            max_aux = minvalue(game, depth-1);
            if(max_aux >= max){
                max = max_aux;
            }
            game.unMakeMove();
        }
        return max;
    }

    public int middleBias(final GameStateModule game, int col) {
        int[] widthBias = {3, 4, 7, 9, 7, 4, 3};
        int[] heightBias = {2, 3, 4, 4, 2, 1};
        int index = game.getHeightAt(col);

        int index2 = Math.abs(4 - index - 1);
        return (heightBias[index2] + widthBias[col]);
    }


    public int evaluation (final GameStateModule game, int player){
        int result = 0;

        if(!game.isGameOver()){
            for(int row = currentBoard.length-1; row >= 0; row--){
                for(int col = 0; col < currentBoard[row].length-1; col++){
                    for(int dx = -1; dx <= 1; dx++) {
                        for(int dy = -1; dy <= 1; dy++) {
                            //game out of bounds
                            if (!((row + dy < 0) || (row + dy >= currentBoard.length-1) || col + dx < 0 || (col + dx >= currentBoard[row].length-1)
                                    || (row - dy >= currentBoard.length-1) || (col - dx >= currentBoard[row].length-1) || (row-dy < 0) || (col-dx < 0) )) {
                                // (row + dy < 0) never go below board
                                // (row + dy >= currentBoard.length-1) never go below board
                                // col + dx < 0 never go left of board
                                // (col + dx >= currentBoard[row].length-1) never go right of board
                                // (row - dy >= currentBoard.length-1)        never go diagonal
                                // (col - dx >= currentBoard[row].length-1)
                                // (row-dy < 0) || (col-dx < 0) never go diagonal bottom left

                                // check diagonal / or \
                                if (currentBoard[row + dy][col + dx] == player) {
                                    if (currentBoard[row - dy][col - dx] == player) {
                                        result = 90;
                                    } else {
                                        result = 40;
                                    }
                                } // only checks adjacents
                                else {
                                    if (currentBoard[row - dy][col - dx] != player) {
                                        result = -10;
                                    } else {
                                        result = -5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            if (game.getWinner() == my_player)
                result = 1000;
            else if (game.getWinner() == my_opponent)
                result = -1000;
        }

        return result;
    }



    public int[][] makeArray(final GameStateModule game) {
        // game board columns
        int[][] temp = new int[game.getHeight()][game.getWidth()];
        int k = 0;
        for (int i = game.getHeight()-1; i >= 0; i--) {
            // game board height
            for (int j = 0; j < game.getWidth() ; j++) {
                // turn game board into 2d array
                // 0 is empty 1 is red coin 2 is yellow coin
                temp[i][j] = game.getAt(j, k);
            }
            k++;
        }
        return temp;
    }

    // print test
    public void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            // game board height
            for (int j = 0; j < array[0].length; j++) {
                // turn game board into 2d array
                System.out.print(currentBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
