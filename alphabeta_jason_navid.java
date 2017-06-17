import java.util.*;

/**
 * Jason Zhang 		999230758
 * Navid Al Nadvi 	912309925
 */
public class alphabeta_jason_navid extends AIModule {
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
        } else {
            my_opponent = 1;
        }

        alpha_beta_search(current, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public void alpha_beta_search(final GameStateModule game, int depth, int alpha, int beta) {
        ArrayList<Integer>legalMoves = new ArrayList<Integer>();
        int bestColumn = -1;

        int aux;
        int score = Integer.MIN_VALUE;

        for(int i = 0; i < game.getWidth(); i++){
            if(game.getHeightAt(i) < 6){
                legalMoves.add(i);
            }
        }

        for (int moves : legalMoves) {
            game.makeMove(moves);
            aux = min_value(game, depth - 1, alpha, beta) + middleBias(game, moves);
            // System.out.println("aux: " + aux + " column: " + moves);

            if (aux >= score) {
                bestColumn = moves;
                score = aux;
            }
            game.unMakeMove();
        }
        printArray(currentBoard);
        System.out.println("score: " + score + " chosen move: " + bestColumn + " player: " + my_player + " opponent: " + my_opponent);
        chosenMove = bestColumn;
    }

    public int max_value(final GameStateModule game, int depth, int parentAlpha, int parentBeta) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        int score = Integer.MIN_VALUE;
        int aux = 0;

        if (depth <= 0 || game.isGameOver()) {
            return evaluation(game, my_player);
        }

        for (int i = 0; i < game.getWidth(); i++) {
            if (game.getHeightAt(i) < 6) {
                legalMoves.add(i);
            }
        }

        for (int moves : legalMoves) {
            game.makeMove(moves);
            aux = min_value(game, depth - 1, parentAlpha, parentBeta);
            if (aux >= score) {
                score = aux;
            }
            game.unMakeMove();
            if (score >= parentBeta) {
                return score;
            }
            if (parentAlpha <= score) {
                parentAlpha = score;
            }
        }

        return score;
    }

    public int min_value(final GameStateModule game, int depth, int parentAlpha, int parentBeta) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        int score = Integer.MAX_VALUE;
        int aux = 0;

        if (depth <= 0 || game.isGameOver()) {
            return evaluation(game, my_opponent);
        }
        for (int i = 0; i < game.getWidth(); i++) {
            if (game.getHeightAt(i) < 6) {
                legalMoves.add(i);
            }
        }

        for (int moves : legalMoves) {
            game.makeMove(moves);
            aux = max_value(game, depth - 1, parentAlpha, parentBeta);
            if (aux <= score) {
                score = aux;
            }
            game.unMakeMove();
            if (score <= parentAlpha) {
                return score;
            }
            if (parentBeta >= score) {
                parentBeta = score;
            }
        }

        return score;
    }



    public int middleBias(final GameStateModule game, int col) {
        int[] widthBias = {3, 5, 7, 9, 7, 5, 3};
        int[] heightBias = {2, 3, 4, 5, 2, 1};
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
        else{
            if (game.getWinner() == my_player)
                result = 1000;
            else if (game.getWinner() == my_opponent)
                result = -1000;
        }
        // System.out.println("result: " + result+ " player: "+ player);

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

