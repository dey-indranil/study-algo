package MinStack;

public class MinCoins {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test1();
		test2();
	}
	private static void test1() {
		MinCoins m = new MinCoins();
		int[] coins = {1,3};
		int value = 3;
		int ans = m.minCoins(coins, value);
		System.out.println(ans);
	
	}
	private static void test2() {
		MinCoins m = new MinCoins();
		int[] coins = {1,5,10};
		int value = 17;
		int ans = m.minCoins(coins, value);
		System.out.println(ans);
	}
	/**
	 * If V == 0, then 0 coins required.
		If V > 0
   			minCoins(coins[0..m-1], V) = min {1 + minCoins(V-coin[i])} 
                               where i varies from 0 to m-1 
                               and coin[i] <= V
	 */
	
	public int minCoins(int[] coins, int value) {
		
		int[] dp = new int[value+1];
		dp[0] = 0;
		dpInit(dp);
		for(int j=1;j<=value;j++) {
			for(int i=0;i<coins.length;i++) {
				if(coins[i]>j)
					continue;
				int temp = dp[j-coins[i]];
				if(temp!=Integer.MAX_VALUE && temp+1<dp[j]) {
					dp[j] = temp+1;
				}
			}
		}
		if(dp[value]==Integer.MAX_VALUE)
			return -1;
		return dp[value];
	}
	private void dpInit(int[] dp) {
		for(int i=1;i<dp.length;i++) {
			dp[i] = Integer.MAX_VALUE;
		}
		
	}
}
