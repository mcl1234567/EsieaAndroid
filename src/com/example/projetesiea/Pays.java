package com.example.projetesiea;


/**
 * Classe de l'élément Pays
 */
public class Pays {
	private String nom, monnaie;
	private String population, formeEtat;
	private String langue, capitale;
	private String climat, superficie;
	private String densite, religion;
	private String nombreExpatries, indicatifTel;

	public Pays(String _nom, String _monnaie, String _population, String _formeEtat, String _langue, String _capitale, String _climat, 
			String _superficie, String _densite, String _religion, String _nombreExpatries, String _indicatifTel) 
	{
		this.nom = _nom;
		this.monnaie = _monnaie;
		this.population = _population;
		this.formeEtat = _formeEtat;
		this.langue = _langue;
		this.capitale = _capitale;
		this.climat = _climat;
		this.superficie = _superficie;
		this.densite = _densite;
		this.religion = _religion;
		this.nombreExpatries = _nombreExpatries;
		this.indicatifTel = _indicatifTel;	
	}

	public String getNom() { return this.nom; }
	public String getMonnaie() { return this.monnaie; }
	public String getPopulation() { return this.population; }
	public String getFormeEtat() { return this.formeEtat; }
	public String getLangue() { return this.langue; }
	public String getCapitale() { return this.capitale; }
	public String getClimat() { return this.climat; }
	public String getSuperficie() { return this.superficie; }
	public String getDensite() { return this.densite; }
	public String getReligion() { return this.religion; }
	public String getNombreExpatries() { return this.nombreExpatries; }
	public String getIndicatifTel() { return this.indicatifTel; }
}