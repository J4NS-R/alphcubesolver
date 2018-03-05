package aplhcubes;

public class AlphCubesSolver {
	
	public static void main(String[] args){
		
		new AlphCubesSolver().solve();
		
	}
	
	private Cube[] cubes;
	
	public void solve(){
		
		//all cubes must start with primRot T & secRot 0
		cubes = new Cube[]{
				new Cube('C', 'B', 'C', 'D', 'A', 'D', 'T', 0),
				new Cube('B', 'B', 'B', 'C', 'A', 'D', 'T', 0),
				new Cube('A', 'D', 'B', 'B', 'A', 'C', 'T', 0),
				new Cube('A', 'C', 'D', 'B', 'A', 'C', 'T', 0)
		};
		
		int perms = 0;
		while (!isSolved()){
			perms++;
			
			nextCube(0, false);
			
		}
		
		printSolution(perms);
		
	}
	
	//moves to the next permutation. 
	//Default: first cube, secondary rotation
	//if necessary, the func is used recursively for rotating more cubes
	private void nextCube(int cubeIndex, boolean prim){
		Cube cube = cubes[cubeIndex];
		if (prim){
			cube.rotatePrim();
			if (cube.primRot == 'T' && cubeIndex < cubes.length-1)
				nextCube(cubeIndex+1, false);
		}else{
			cube.rotateSec();
			if (cube.secRot == 0)
				nextCube(cubeIndex, true);
		}
		if (cubeIndex == cubes.length-1 && cube.secRot==0 && cube.primRot=='T'){
			System.out.println("No solution found.");
			System.exit(0);
		}
	}
	
	private void printSolution(int perms){
		System.out.println("Solution found after "+perms+" permutations");
		System.out.println("Solution:");
		for (int i = 0; i < cubes.length; i++){
			System.out.println(
					"Cube "+i+": primRot: "+cubes[i].primRot+", secRot: "+cubes[i].secRot
			);
		}
		System.out.println("---");
		String row = "North row: ";
		for (Cube cube : cubes){
			row += cube.north +", ";
		}
		System.out.println(row);
		row = "Top row: ";
		for (Cube cube : cubes){
			row += cube.top +", ";
		}
		System.out.println(row);
		row = "South row: ";
		for (Cube cube : cubes){
			row += cube.south +", ";
		}
		System.out.println(row);
		row = "Bottom row: ";
		for (Cube cube : cubes){
			row += cube.bottom +", ";
		}
		System.out.println(row);
	}
	
	//check whether the current cube state is a solved state
	//That is, the north, top, south and bottom sides all respectively contain
	//distinct elements
	private boolean isSolved(){
		
		for (int side = 0; side < 4; side++){
		
			char[] sideArr = new char[cubes.length];
			for (int i = 0; i < cubes.length; i++){
				switch (side){
				case 0:
					sideArr[i] = cubes[i].north;
					break;
				case 1:
					sideArr[i] = cubes[i].top;
					break;
				case 2:
					sideArr[i] = cubes[i].south;
					break;
				case 3:
					sideArr[i] = cubes[i].bottom;
					break;
				}
			}
				
			if (!isUnique(sideArr))
				return false;
		
		}
		
		return true;
		
	}
	
	//checks if every el in arr is unique
	private boolean isUnique(char[] arr){
		
		boolean unique = true;
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr.length; j++){
				if (i != j)
					unique = unique && (arr[i] != arr[j]);
			}
			if (!unique)
				break;
		}
		
		return unique;
		
	}

	private class Cube{
		
		public char top;
		public char north;
		public char south;
		public char east;
		public char west;
		public char bottom;
		private char primRot; //primary rotation. Where the primary face is pointing
		private int secRot; //secondary rotation. How many clockwise turns from the
							//perspective of the primRot have been made (0-3)
		
		public Cube(char north, char east, char south, char west, char top, char bottom, char primRot, int secRot){
			
			setVals(north, east, south, west, top, bottom, primRot, secRot);
			
		}
		//Separate from constructor, because the object needs to rewrite itself, and java
		//doesn't like it when we use constructors for this purpose.
		private void setVals(char north, char east, char south, char west, char top, char bottom, char primRot, int secRot){
			
			this.north = north;
			this.east = east;
			this.south = south;
			this.west = west;
			this.top = top;
			this.bottom = bottom;
			this.primRot = primRot;
			this.secRot = secRot;
			
		}
		
		//private char[] primRotOrder = {'N', 'E', 'S', 'W', 'T', 'B'};
		//rotate primary face to the next position (and all other faces with it)
		public void rotatePrim(){
			
			switch (primRot){
			case 'B':
				setVals(bottom, east, top, west, north, south, 'N', secRot);
				break;
			case 'N':
				setVals(west, north, east, south, top, bottom, 'E', secRot);
				break;
			case 'E':
				setVals(west, north, east, south, top, bottom, 'S', secRot);
				break;
			case 'S':
				setVals(west, north, east, south, top, bottom, 'W', secRot);
				break;
			case 'W':
				setVals(bottom, north, top, south, west, east, 'T', secRot);
				break;
			case 'T':
				setVals(south, east, north, west, bottom, top, 'B', secRot);
				
			}
			
		}
		
		//rotates cube clockwise from perspective of primRot
		public void rotateSec(){
			
			secRot = ++secRot % 4; //0-3
			
			switch(primRot){
			case 'N':
				setVals(north, bottom, south, top, east, west, primRot, secRot);
				break;
			case 'E':
				setVals(top, east, bottom, west, south, north, primRot, secRot);
				break;
			case 'S':
				setVals(north, top, south, bottom, west, east, primRot, secRot);
				break;
			case 'W':
				setVals(bottom, east, top, west, north, south, primRot, secRot);
				break;
			case 'T':
				setVals(west, north, east, south, top, bottom, primRot, secRot);
				break;
			case 'B':
				setVals(east, south, west, north, top, bottom, primRot, secRot);
				break;
			}
			
		}
		
	}
	
}


