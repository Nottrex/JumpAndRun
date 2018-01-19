package game;

public enum Ability {
	DOUBLE_JUMP(10),
	STOMP(10),
	WALL_JUMP(10);

	private int cost;
	Ability(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}
}
