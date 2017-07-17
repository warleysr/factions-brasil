package factionsbrasil.player;

public enum Role {
	
	LIDER(0), PATRAO(1), TRAFICANTE(2);
	
	private int id;
	
	Role(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Role getById(int id) {
		for (Role role : values())
			if (role.getId() == id)
				return role;
		return null;
	}

}
