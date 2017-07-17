package factionsbrasil.drugs;

public enum DrugType {
	
	COCAINE(0), CANNABIS(1), CRACK(2);
	
	private int id;
	
	DrugType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
