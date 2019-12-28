package fr.aiidor.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGDecription;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGAnge;

public class Joueur {
	
	private UUID uuid;
	private LGRoles role;
	private LGCamps camp;
	private Integer Power = 0;
	
	private String name;
	
	private Boolean Rea = false;
	private LGCamps deathD = null;
	private Boolean Infect = false;	
	private Boolean death = false;
	
	private Joueur couple = null;
	
	public Boolean Rob = false;
	public Joueur Model = null;
	
	public Boolean canVote = false;
	public Joueur whoVote = null;
	public Boolean VoteCible = false;
	public Integer vote = 0;
	
	public ArrayList<Joueur> voteCorbeau = new ArrayList<>();
	
	public Boolean salvation = false;
	public Joueur whoProtect = null;
	
	public Joueur Soeur = null;

	private int kill = 0;
	public Joueur Killer = null;
	
	public boolean trublion = false;
	
	public boolean noPower = false;
	
	public String LastWill = null;
	public String chamanMsg = null;
	
	public LGRoles fakeRole = LGRoles.SV;
	public Boolean cantSee = false;
	
	public Boolean canCancelVote = false;
	public Boolean isFlaire = false;
	
	public List<Joueur> hasInspect = new ArrayList<>();
	
	public List<Joueur> LgList = new ArrayList<>();
	
	public LGAnge ange = null;
	public Joueur angeTarget;
	
	public Location grave;
	
	private LGUHC main;
	
	public Joueur(UUID uuid, LGRoles role, LGCamps camp, LGUHC main) {
		this.uuid = uuid;
		this.role = role;
		this.camp = camp;
		
		setName();
		setpower();
		
		this.main = main;
		
		LgList.add(this);
	}
	
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void setName() {
		name = Bukkit.getPlayer(uuid).getName();
	}
	
	public String getName() {
		return name;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public LGRoles getRole() {
		return role;
	}
	
	public LGRoles getFakeRole() {
		return fakeRole;
	}
	
	public void setRole(LGRoles role) {
		this.role = role;
	}
	
	public LGCamps getCamp() {
		return camp;
	}
	
	public void setCamp(LGCamps camp) {
		this.camp = camp;
	}
	
	public boolean isLg() {
		return camp == LGCamps.LoupGarou || camp == LGCamps.LGB;
	}
	
	public boolean isInfect() {
		return Infect;
	}
	
	public void setInfect(Boolean b) {
		Infect = b;
	}
	
	public Integer getPower() {
		return Power;
	}
	public void setPower(Integer power) {
		this.Power= power;
	}
	
	
	public boolean isRea() {
		return Rea;
	}
	
	public void setRea(Boolean b) {
		Rea = b;
	}
	
	public boolean isDying() {
		return deathD != null;
	}
	
	public boolean isDyingState(LGCamps camp) {
		if (!isDying()) return false;
		return camp == deathD;
	}
	
	public void setDyingState(LGCamps camp) {
		deathD = camp;
	}
	
	
	public boolean isDead() {
		return death;
	}
	
	public void setDead(Boolean b) {
		death = b;
	}
	
	
	public void sendDesc() {
		new LGDecription(this, main).sendDescription();
	}
	
	public boolean isConnected() {
		return getPlayer() != null;
	}
	
	
	private void setpower() {
		if (role == LGRoles.Renard) {
			Power = 3;
		}
		
		if (role == LGRoles.Ancien || role == LGRoles.Citoyen) {
			Power = 2;
		}
		
		if (role == LGRoles.Citoyen) {
			canCancelVote = true;
		}
		
		if (role == LGRoles.Ancien) {
			Power = 1;
		}
		
		if (role == LGRoles.Sorciere || role == LGRoles.IPL) {
			Power = 1;
		}
	}
	
	//VOTE
	public void setVoteCible(Boolean b) {
		this.VoteCible = b;
	}
	public boolean isVoteCible() {
		return VoteCible;
	}
	
	//COUPLE
	public Joueur getCouple() {
		return couple;
	}
	
	public void setCouple(Joueur j) {
		this.couple = j;
	}
	
	public boolean hasCouple() {
		return couple != null;
	}
	
	//ENFANT SAUVAGE
	public boolean hasModel() {
		return Model != null;
	}
	
	//KILL
	public int getKills() {
		return kill;
	}
	
	public void addkill() {
		kill++;
	}
	
	//LG FEUTRE
	public boolean isLgIG() {
		if (role == LGRoles.LGFeutre) {
			return fakeRole.camp == LGCamps.LoupGarou || fakeRole.camp == LGCamps.LGB;
		}
		
		else return camp == LGCamps.LoupGarou || camp == LGCamps.LGB;
	}
	
	public void setFakeRole() {
		
		
		if (!noPower) {
			
			if (main.getFakeRoles().size() == 0) {
				if (isConnected()) {
					getPlayer().sendMessage(main.gameTag + "§bIl n'y a pas de rôles disponible votre rôle ne sera donc pas caché !");
				}
				fakeRole = LGRoles.LGFeutre;
				return;
			}
			
			LGRoles fake = main.getFakeRoles().get(new Random().nextInt(main.getFakeRoles().size()));
			
			if (isConnected()) {
				getPlayer().sendMessage(main.gameTag + "§bVotre rôle de façade à été changé pour le rôle : " + fake.name);
			}
			
			fakeRole = fake;
		}
	}
}
