package MinStack;

public class EditDistance {
	
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
	private int[] goalCell(String s, String t ) {
		int[] goalCellIndexes = new int[2];
		goalCellIndexes[0] = s.length()-1;
		goalCellIndexes[1] = t.length()-1;
		return goalCellIndexes;
	}
	private void dpInit(String s, String t) {
		m = new Cell[s.length()+1][t.length()+1];
		for(int i=0;i<s.length()+1;i++) {
			for(int j=0;j<t.length()+1;j++) {
				m[i][j] = new Cell();
			}
		}
		for(int i=0;i<s.length()+1;i++) {
			rowInit(i);
		}
		for(int i=0;i<t.length()+1;i++) {
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
		m[0][i].cost = i;
		if(i>0)
			m[0][i].parent = Action.INSERT.val;
		else 
			m[0][i].parent = -1;
	}
	
	public static void main(String[] args) {
		EditDistance ed = new EditDistance();
		ed.test1();
	}

	private void test1() {
		EditDistance ed = new EditDistance();
		String s = " thou shalt not";
		String t = " you should not";
		int cost = ed.editDistance(s, t);
		int[] indices = goalCell(s,t);
		ed.reconstructPath(s, t, indices[0], indices[1]);
		System.out.println();
		//ans: DSMMMMMISMSMMMM
		for(int i=0;i<s.length()+1;i++) {
			for(int j=0;j<t.length()+1;j++) {
				System.out.print(ed.m[i][j].cost+",");
			}
			System.out.println();
		}
	}
}
