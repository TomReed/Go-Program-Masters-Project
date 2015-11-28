package go;

public class BoardEvaluator{

	public static boolean stop(GoBoard board){
		//stop if there have been two consecutive passes or capture stones are not on board
		if (board.getPass()>1){
			return true;
		}
		else if (board.getlifeChar()!='x'&&board.getlifeChar()!='o'){   //'-' indicates empty space in life group location
			return true;
		}
		else{
			return false;
		}
	}

	//returns a score based on the strength of the board from perspective of player
	public static int evaluate(GoBoard board,char player){
		char lifePlayer = board.getLifePlayer(); //the colour of the special group 

		//if life group capture return -1000 if player was captured, or +1000 if player's enemy 

		if ((board.getlifeChar()!='x')&&(board.getlifeChar()!='o')){
			if (lifePlayer!=player){
				return 100000;
			}
			else{
				return -1000000;
			}
		}
		else {
			return heuristicValue(board,player);
		}


	}

	public static int heuristicValue(GoBoard board,char player){
		
		char enemy;
		if(player=='x'){enemy='o';}else {enemy='x';}
		char defender = board.getLifePlayer();
		char attacker;
		if (defender=='o'){attacker='x';}else{attacker='o';}

		int score = 0;
		char[][] goban = board.getArray();
		
		/*
		 * 1. CHECKS BOARD FOR REMOVAL OF LIBERTIES OR HANES PLAYED BY ATTACKING SIDE
		 */
		for(int row=0;row<19;row++){
			for(int column=0;column<19;column++){
				if (goban[row][column]==defender){

					if (goban[row][column]==defender){
						char north,east,south,west;
						if (row<18){north = goban[row+1][column];}else{north='B';}
						if (column<18){east = goban[row][column+1];}else{east='B';}  //B for border
						if (row>0){south = goban[row-1][column];}else{south='B';}
						if (column>0){west = goban[row][column-1];}else{west='B';}

						//check for liberties taken to N,E,S & W
						if (north==attacker){score+=30;}
						if (east==attacker){score+=30;}
						if (south==attacker){score+=30;}
						if (west==attacker){score+=30;}

						//check possible hane combinations N&W and N&W
						if(north==attacker){
							if (west==attacker){score+=50;}
							if (east==attacker){score+=50;}
						}
						//check possible hane combinations S&W and S&E
						if (south==attacker){
							if (west==attacker){score+=50;}
							if (east==attacker){score+=50;}
						}

						
					}
				}
			}
		}
		// 2. check for attackers empty triangles
		score +=(countEmptyTriangles(goban,attacker))*75;
		
		
		//Scores so far have been from the attackers perspective if the player is a defender:
		//Make score negative
		if (player==defender){score = score*-1;}

		
		//3. CHECK BOARD FOR MOUTHS - good shape - for each score plus 100 for player & -100 for enemy 
		score+=(countMouths(goban,player))*100;
		score+=(countMouths(goban,enemy))*-100;
		
		return score;
	}

	/*
	 * takes the board array as arguments, and a player char as arguments 
	 * returns the number of empty triangles of that player
	 * 
	 */
	public static int countEmptyTriangles(char[][] b,char player){
		// Rotations of empty triangle
		// A OO B OO C O D  O                                         //
		//    O   O    OO  OO                                         //
		int count=0;
		
		//inspect stones relative to top left stone, column 0 to 17, row 1 to 18
		for(int row=1;row<19;row++){
			for(int column=0;column<18;column++){
			//if top left is empty a player might by D	
				if(b[row][column]=='-'){
					if(b[row][column+1]==player&&b[row-1][column]==player&&b[row-1][column+1]==player){
						count++;
					}
				}
				//if top left is player might be A,B or C
				else if(b[row][column]==player){
					//if top right is a player 
					if(b[row][column+1]==player){
						//if one of bottom row is a space and the other is a player might be A or B
						if((b[row-1][column]==player&&b[row-1][column+1]=='-')||(b[row-1][column]=='-'&&b[row-1][column+1]==player)){
							count++;
						}
					}
					//if top right is a space, might be C
					else if(b[row][column+1]=='-'){
						if (b[row-1][column]==player&&b[row-1][column+1]==player){
							count++;
						}
					}
				}
			}
		}
		return count;
	}
	

	public static int countMouths(char[][] b, char player){
		/* Representations of the 4 rotations of the mouth shape
		 * 
		 * A O O 	B  OO  C OO    D O  O
		 *   O        O        O        O    
		 *    OO      O O    O O     O O                 
		 */
		int count = 0;

		//scan every point apart from the edges of the board 
		for (int row=1;row<18;row++){
			for (int column=1;column<18;column++){

				// Check around every point that is not a player, it might be a centre of a mouth
				if (b[row][column]!=player){
					//check North is player and south is not (characteristics of B and C)
					if ((b[row+1][column]==player)&&(b[row-1][column]!=player)){  //COULD BE B OR C
						//check South East and South West
						if (b[row-1][column-1]==player && b[row-1][column+1]==player){  //COULD Still be B or C
							char nw = b[row+1][column-1];
							char ne = b[row+1][column+1];
							char w = b[row][column-1];
							char e = b[row][column+1];
							//check NW==player NE!=player E==player W!=player   //TYPE C
							if(nw==player && ne!=player && e ==player && w!=player){
								count++;
							}
							//check TYPE B
							else if(nw!=player && ne==player && e!=player && w==player){
								count++;
							}
						}
					}
					//if not B or C check for A or D
					//check if South is player and north is not a player
					else if ((b[row-1][column]==player)&&(b[row+1][column]!=player)){  //COULD BE A OR D
						//check ne and nw
						if (b[row+1][column-1]==player && b[row+1][column+1]==player){  //COULD Still be A or D
							char sw = b[row-1][column-1];
							char se = b[row-1][column+1];
							char w = b[row][column-1];
							char e = b[row][column+1];
							//check NW==player NE!=player E==player W!=player   //TYPE D
							if(sw==player && se!=player && e ==player && w!=player){
								count++;
							}
							//check TYPE A
							else if(sw!=player && se==player && e!=player && w==player){
								count++;
							}
						}
					}
				}
			}
		}
		return count;
	}
}

