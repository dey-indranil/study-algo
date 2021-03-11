package MinStack;

public class EditDistanceSubstringmatching {
	/**
	 * Suppose that we want to find where a short pattern
P best occurs within a long text T—say, searching for “Skiena” in all its
misspellings (Skienna, Skena, Skina, .. . ) within a long file. Plugging this
288 8. DYNAMIC PROGRAMMING
search into our original edit distance function will achieve little sensitivity,
since the vast majority of any edit cost will consist of deleting that which
is not “Skiena” from the body of the text. Indeed, matching any scattered
··· S ··· k ···i··· e ··· n ··· a and deleting the rest yields an optimal solution.
We want an edit distance search where the cost of starting the match is
independent of the position in the text, so that a match in the middle is not
prejudiced against. Now the goal state is not necessarily at the end of both
strings, but the cheapest place to match the entire pattern somewhere in the
text. Modifying these two functions gives us the correct solution
	 *
	 */
	class Cell{
		int cost, parent;

		@Override
		public String toString() {
			return "Cell [cost=" + cost + ", parent=" + parent + "]";
		}
		
	}
	Cell[][] m = null;
	enum Action {MATCH(0), INSERT(1), DELETE(2); int val; Action(int m){val = m;}};
	int[] costOption = new int[3];
	
	public int editDistance(String s, String t) {
		int i,j,k;
//		s = " " + s;
//		t = " " + t;
		dpInit(s,t);
		for( i=1;i<s.length();i++) {
			for(j=1;j<t.length();j++) {
				costOption[Action.MATCH.val] = m[i-1][j-1].cost + match(s.charAt(i), t.charAt(j));
				costOption[Action.INSERT.val] = m[i][j-1].cost + indel(t.charAt(j));
				costOption[Action.DELETE.val] = m[i-1][j].cost + indel(s.charAt(i));
				
				m[i][j].cost = costOption[Action.MATCH.val];
				m[i][j].parent = Action.MATCH.val;
				for( k=Action.INSERT.val;k<=Action.DELETE.val;k++) {
					if(costOption[k]<m[i][j].cost) {
						m[i][j].cost = costOption[k];
						m[i][j].parent = k;
					}
					
				}
			}
		}
		int[] indices = goalCell(s,t);
		return m[indices[0]][indices[1]].cost;
	}
	
	public void reconstructPath(String s, String t, int i, int j) {
		if(m[i][j].parent==-1) return;
		if(m[i][j].parent == Action.MATCH.val) {
			reconstructPath(s,t,i-1,j-1);
			matchOutput(s,t,i,j);
		} else if(m[i][j].parent == Action.INSERT.val) {
			reconstructPath(s,t,i,j-1);
			insertOutput(t,j);
		} else if(m[i][j].parent == Action.DELETE.val) {
			reconstructPath(s, t, i-1, j);
			deleteOutput(s,i);
		}
	}
	
	private void deleteOutput(String s, int i) {
		System.out.print("D");
	}

	private void insertOutput(String t, int j) {
		System.out.print("I");		
	}

	private void matchOutput(String s, String t, int i, int j) {
		if(s.charAt(i)==t.charAt(j))
			System.out.print("M");
		else
			System.out.print("S");
	}

	private int match(char c, char d) {
		if(c==d)
			return 0;
		return 1;
	}
	public int indel(char c) {
		return 1;
	}
	//the following function changed. This determines where to stop looking. The change is related to 
	// identifying the index where the cost is lowest on the last row instead of just using the last cell of dp matrix
	private int[] goalCell(String s, String t ) {
		int[] goalCellIndexes = new int[2];
		int i = s.length()-1;
		int j=0;
		for(int k=1;k<t.length();k++) {
			if(m[i][k].cost < m[i][j].cost)
				j = k;
		}
		goalCellIndexes[0] = i;
		goalCellIndexes[1] = j;
		return goalCellIndexes;
	}
	private void dpInit(String s, String t) {
		m = new Cell[s.length()+1][t.length()+1];
		for(int i=0;i<s.length()+1;i++) {
			for(int j=0;j<t.length()+1;j++) {
				m[i][j] = new Cell();
			}
		}
		for(int j=0;j<t.length()+1;j++) {
			rowInit(j);
		}
		for(int i=0;i<s.length()+1;i++) {
			colInit(i);
		}
	}
	private void colInit(int i) {
		m[i][0].cost = i;
		if(i>0)
			m[i][0].parent = Action.DELETE.val;
		else
			m[i][0].parent = -1;
	}
	private void rowInit(int i) {
		m[0][i].cost = 0; //changed 
		m[0][i].parent = -1; //changed, this indicates matching can start anywhere in the pattern instead of start in the core editDistance algo
	}
	
	public static void main(String[] args) {
		EditDistanceSubstringmatching ed = new EditDistanceSubstringmatching();
		//ed.test1();
		ed.test4();
	}

	private void test2() {
		//works
		EditDistanceSubstringmatching ed = new EditDistanceSubstringmatching();
		String s = " bcaxybtqcfz";
		String t = " abc";
		int cost = ed.editDistance(s, t);
		int[] indices = ed.goalCell(s,t);
		System.out.println(indices[0]+","+indices[1]);
		ed.reconstructPath(s, t, indices[0], indices[1]);
		System.out.println();
		//ans: 
		for(int i=0;i<s.length();i++) {
			for(int j=0;j<t.length();j++) {
				System.out.print(ed.m[i][j].cost+",");
			}
			System.out.println();
		}
	
		
	}
	private void test3() {
		//does not work
		EditDistanceSubstringmatching ed = new EditDistanceSubstringmatching();
		String s = " bcaxybtqcfzc";
		String t = " abc";
		int cost = ed.editDistance(s, t);
		int[] indices = ed.goalCell(s,t);
		System.out.println(indices[0]+","+indices[1]);
		ed.reconstructPath(s, t, indices[0], indices[1]);
		System.out.println();
		//ans: 
		for(int i=0;i<s.length();i++) {
			for(int j=0;j<t.length();j++) {
				System.out.print(ed.m[i][j].cost+",");
			}
			System.out.println();
		}
	
		
	}
	
	private void test4() {
		//does not work
		EditDistanceSubstringmatching ed = new EditDistanceSubstringmatching();
		String s = " aba";
		String t = " a";
		int cost = ed.editDistance(s, t);
		int[] indices = ed.goalCell(s,t);
		System.out.println(indices[0]+","+indices[1]);
		ed.reconstructPath(s, t, indices[0], indices[1]);
		System.out.println();
		//ans: 
		for(int i=0;i<s.length();i++) {
			for(int j=0;j<t.length();j++) {
				System.out.print(ed.m[i][j].cost+",");
			}
			System.out.println();
		}
	
		
	}

	private void test1() {
		EditDistanceSubstringmatching ed = new EditDistanceSubstringmatching();
		String s = " This is akin to a made up sentence to search author";
		String t = " skiena";
		int cost = ed.editDistance(s, t);
		int[] indices = ed.goalCell(s,t);
		System.out.println(indices[0]+","+indices[1]);
		ed.reconstructPath(s, t, indices[0], indices[1]);
		System.out.println();
		//ans: 
		for(int i=0;i<s.length();i++) {
			for(int j=0;j<t.length();j++) {
				System.out.print(ed.m[i][j].cost+",");
			}
			System.out.println();
		}
	}
}
