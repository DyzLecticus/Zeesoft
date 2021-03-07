package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Rand;

public class TestRand {
	public static void main(String[] args) {
		assert new Rand() != null;
		assert Rand.getRandomFloat(4,4) == 4;
		assert Rand.getRandomFloat(9,3) == 9;
		for (int i = 1; i < 3; i++) {
			float r = Rand.getRandomFloat(0, 1 + i);
			assert r >= 0;
			assert r <= (1 + i);
		}
		assert Rand.getRandomInt(4,4) == 4;
		assert Rand.getRandomInt(9,3) == 9;
		for (int i = 1; i < 3; i++) {
			float r = Rand.getRandomInt(10, 20 + i);
			assert r >= 10;
			assert r <= (20 + i);
		}
	}
}
