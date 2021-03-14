package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;

public class TestRand {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
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
		
		List<Rand> rands = new ArrayList<Rand>();
		for (int i = 0; i < 100; i++) {
			rands.add(new Rand());
		}
		List<Rand> randsBefore = new ArrayList<Rand>(rands);
		Rand.randomizeList(rands);
		assert rands.size() == randsBefore.size();
		int same = 0;
		for (Rand rand: rands) {
			if (randsBefore.get(same)!=rand) {
				break;
			}
			same++;
		}
		assert same < 100;
		
		assert Rand.selectRandomFromList(rands) != null;
	}
}
