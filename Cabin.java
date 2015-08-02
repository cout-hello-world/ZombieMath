/*
 * Copyright 2015 Henry Elliott
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Cabin {
	private final double foodPerPrepDay;
	private final double waterPerPrepDay;
	private final double bulletsPerPrepDay;

	private int foodDays = -1;
	private int waterDays = -1;
	private int bulletsDays = -1;

	public Cabin(int food, int water, int bullets) {
		if (food < 0 || water < 0 || bullets < 0) {
			throw new IllegalArgumentException("All inputs must be nonnegative.");
		}
		int sum;
		if ((sum = food + water + bullets) != 100) {
			throw new IllegalArgumentException("Inputs sum to " + sum +
			        " percent.\nThey must sum to 100 percent.");
		}

		foodPerPrepDay = food / 100.0 * FOOD_PER_MAN_HOUR * MAN_HOURS_PER_DAY;
		waterPerPrepDay = water / 100.0 * WATER_PER_MAN_HOUR * MAN_HOURS_PER_DAY;
		bulletsPerPrepDay = bullets / 100.0 * BULLETS_PER_MAN_HOUR *
		                                  MAN_HOURS_PER_DAY;
	}

	public String survivalStatus() {
		if (foodDays == -1) {
			foodDays = outOfFood();
		}
		if (waterDays == -1) {
			waterDays = outOfWater();
		}
		if (bulletsDays == -1) {
			bulletsDays = outOfBullets();
		}
		int foodDeath = foodDays + DAYS_WITHOUT_FOOD;
		int waterDeath = waterDays + DAYS_WITHOUT_WATER;
		int bulletsDeath = bulletsDays + DAYS_WITHOUT_BULLETS;
		if (foodDeath > DAYS && waterDeath > DAYS && bulletsDeath > DAYS) {
			return "You survived.";
		}
		if (foodDeath < waterDeath && foodDeath < bulletsDeath) {
			return "You died of starvation on day " + foodDeath + ".";
		}
		if (waterDeath < foodDeath && waterDeath < bulletsDeath) {
			return "You died of dehydration on day " + waterDeath + ".";
		}
		return "Zombies killed you when you ran out of bullets on day " +
		       bulletsDays + ".";
	}

	public String foodStatus() {
		foodDays = outOfFood();
		switch (foodDays) {
		case OK:
			return "You did not run out of food.";
		default:
			return "You'd run out of food on day " + foodDays+ ".";
		}
	}

	public String waterStatus() {
		waterDays = outOfWater();
		switch (waterDays) {
		case OK:
			return "You did not run out of water.";
		default:
			return "You'd run out of water on day " + waterDays + ".";
		}
	}

	public String bulletsStatus() {
		bulletsDays = outOfBullets();
		switch (bulletsDays) {
		case OK:
			return "You did not run out of bullets.";
		default:
			return "You'd run out of bullets on day " + bulletsDays + ".";
		}
	}

	private int outOfFood() {
		double food = 0.0; 
		for (int i = -PREP_DAYS + 1; i <= DAYS; i++) {
			if (i < 1) {
				food += foodPerPrepDay;
			}
			food -= FOOD_PER_DAY;
			//System.out.println(i + " " + food + " food");
			if (food < 0.0) {
				return i;
			}
		}
		return OK;
	}

	private int outOfWater() {
		double water = 0.0;
		for (int i = -PREP_DAYS + 1; i <= DAYS; i++) {
			if (i < 1) {
				water += waterPerPrepDay;
			}
			water -= WATER_PER_DAY;
			//System.out.println(i + " " + water + " water");
			if (water < 0.0) {
				return i;
			}
		}
		return OK;
	}

	private int outOfBullets() {
		double bullets = 0.0;
		double zombies = 1.0;
		for (int i = -PREP_DAYS + 1; i <= DAYS; i++) {
			if (i < 1) {
				bullets += bulletsPerPrepDay;
			} else {
				bullets -= BULLETS_PER_ZOMBIE * zombies;
				zombies *= ZOMBIE_FACTOR;
			}
			//System.out.println(i + " " + bullets + "  bullets");
			if (bullets < 0.0) {
				return i;
			}
		}
		return OK;
	}

	private static final int PREP_DAYS = 30;
	private static final double FOOD_PER_MAN_HOUR = 5000.0;
	private static final double WATER_PER_MAN_HOUR = 64.0;
	private static final double BULLETS_PER_MAN_HOUR = 9.8;
	private static final double MAN_HOURS_PER_DAY = 5.0 * 12.0;

	private static final double FOOD_PER_DAY = 10000.0;
	private static final double WATER_PER_DAY = 320.0;
	private static final double ZOMBIE_FACTOR = Math.exp(0.002 * 24.0);
	private static final double BULLETS_PER_ZOMBIE = 4.0;

	private static final int DAYS = 100;
	private static final int OK = 200;

	private static final int DAYS_WITHOUT_FOOD = 21;
	private static final int DAYS_WITHOUT_WATER = 3;
	private static final int DAYS_WITHOUT_BULLETS = 0;
}
