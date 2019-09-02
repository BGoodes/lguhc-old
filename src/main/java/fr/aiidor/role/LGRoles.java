package fr.aiidor.role;

public enum LGRoles {
	
	LG("Loup-garou", LGCamps.LoupGarou, 1),
	VPL("Vilain petit Loup", LGCamps.LoupGarou, 0),
	IPL("Infect Père des Loups", LGCamps.LoupGarou, 0),
	LGA("Loup-garou Amnésique", LGCamps.Village, 0),
	
	Voyante("Voyante", LGCamps.Village, 0),
	VoyanteB("Voyante (Bavarde)", LGCamps.Village, 0),
	
	Renard("Renard", LGCamps.Village, 0),
	MontreurDours("Montreur d'ours", LGCamps.Village, 0),
	PetiteFille("Petite Fille", LGCamps.Village, 0),
	Ancien("Ancien", LGCamps.Village, 0),
	Sorciere("Sorcière", LGCamps.Village, 0),
	Salvateur("Salvateur", LGCamps.Village, 0),
	Ange("Ange", LGCamps.Village, 0),
	Citoyen("Citoyen", LGCamps.Village, 0),
	Soeur("Soeur", LGCamps.Village, 0),
	Cupidon("Cupidon", LGCamps.Village, 0),
	Trublion("Trublion", LGCamps.Village, 0),
	Chaman("Chaman", LGCamps.Village, 0),
	Mineur("Mineur", LGCamps.Village, 0),
	Chasseur("Chasseur", LGCamps.Village, 0),
	Corbeau("Corbeau", LGCamps.Village, 0),
	Pyromane("Pyromane", LGCamps.Village, 0),
	Bouc("Bouc Emissaire", LGCamps.Village, 0),
	
	EnfantS("Enfant Sauvage", LGCamps.Village, 0),
	Voleur("Voleur", LGCamps.Voleur, 0),
	Assassin("Assassin", LGCamps.Assassin, 0),
	LGB("Loup-Garou Blanc", LGCamps.LGB, 0),
	
	SV("Simple Villageois", LGCamps.Village, 50),
	
	Valentin("Valentin", LGCamps.Village, 0),
	Copiteur("Copiteur", LGCamps.Voleur, 0);
	
	public String name;
	public LGCamps camp;
	public int number;
	
	LGRoles(String name, LGCamps camp, int number) {
		this.name = name;
		this.camp = camp;
		this.number = number;
	}
}


