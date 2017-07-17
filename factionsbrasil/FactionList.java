package factionsbrasil;

import java.lang.reflect.Field;

public class FactionList {
	
	private static final FactionList INSTANCE = new FactionList();
	
	public static final Faction CV = new Faction(0, "Comando Vermelho", "&cCV");
	public static final Faction TCA = new Faction(1, "Terceiro Comando dos Amigos", "&9TCA");
	public static final Faction PCC = new Faction(2, "Primeiro Comando da Capital", "&ePCC");
	
	private static final Faction[] FACTIONS = {CV, TCA, PCC};
	
	public static FactionList getInstance() {
		return INSTANCE;
	}
	
	public static Faction[] getFactions() {
		return FACTIONS;
	}
	
	public static Faction getByName(String name) {
		try {
			Field f = FactionList.class.getField(name.toUpperCase());
			return (Faction) f.get(INSTANCE);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Faction getById(int id) {
		for (Faction f : FACTIONS) {
			if (f.getId() == id)
				return f;
		}
		return null;
	}
	 
}
