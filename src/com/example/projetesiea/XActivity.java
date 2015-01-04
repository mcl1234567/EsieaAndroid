package com.example.projetesiea;

import java.util.ArrayList;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Activité d'affichage de la ListView ( pays )
 */
public class XActivity extends ActionBarActivity {
	RenseignementReceiver receiver;

  	// Gestion des elements (item) du contenu.
  	ArrayAdapter<String> adapter;
  	ArrayList<String> arraylist = new ArrayList<String>();

  	float height_screen;
  	float width_screen;

  	/**
  	 * Classe Broadcast Receiver.
  	 */
  	public class RenseignementReceiver extends BroadcastReceiver {
  		@Override
    	public void onReceive(Context context, Intent intent) 
    	{
  	      // Ecriture des donnees de la BDD vers le layout.
  			displayFromBDD();
    	}
  	}

    @Override
  	public void onCreate(Bundle savedInstanceState) 
  	{
  		super.onCreate(savedInstanceState);
  		
  		// Configuration affichage (layout xml)
  		configurationLayout();

  		// Lance le service pour gérer les requêtes HTTP
  		launchIntentService();

	  	// Ecriture des donnees de la BDD vers le layout.
	  	displayFromBDD();
  	}

    @Override
    public void onResume() 
    {
    	receiver = new RenseignementReceiver();
    	registerReceiver(receiver, new IntentFilter(XService.NOUVEAU_RENSEIGNEMENT_INSERE));

        // Ecriture des donnees de la BDD vers le layout.
    	displayFromBDD();

    	super.onResume();
    }

    @Override
    public void onPause() 
    {
    	unregisterReceiver(receiver);
    	super.onPause();
    }

	/**
	 *  Creation du menu 'overflow'.
	 */
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	// Insertion du menu overflow
		getMenuInflater().inflate(R.menu.menu_xactivity, menu);

      	return true;
    }

	/**
	 *  Gestion du menu 'overflow'.
	 */
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	super.onOptionsItemSelected(item);

    	int itemId = item.getItemId();
		if(itemId == R.id.action_accueil) {			
			Intent intent = new Intent(this, Accueil.class);
			startActivityForResult(intent, 1);			
			return true;
		}
		if(itemId == R.id.action_xviewpager) {			
			Intent intent = new Intent(this, X_ViewPager.class);
			startActivityForResult(intent, 2);			
			return true;
		}

    	return false;
    }

	/**
	 * Lance la recherche du service Internet
     * Recupere les donnees du serveur.
	 */
  	private void launchIntentService() 
  	{
  		startService(new Intent(this, XService.class));
  	}

  	/**
  	 * Ajout d'un renseignement.
  	 */
	public void ajoutRenseignement(String _renseignement) 
	{
		arraylist.add(_renseignement);
		adapter.notifyDataSetChanged();
	}

	/**
     * Ecriture des donnees de la BDD vers le layout.
	 */
	public void displayFromBDD() 
	{
		arraylist.clear();

		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(XProvider.CONTENT_RENSEIGNEMENT_URI, null, null, null, null);

		if(cursor.moveToFirst()) {
			do {
				String nom = 		cursor.getString(XProvider.NOM_COLUMN);
				String monnaie = 	cursor.getString(XProvider.MONNAIE_COLUMN);
				String population = cursor.getString(XProvider.POPULATION_COLUMN);
				String formeetat = 	cursor.getString(XProvider.FORMEETAT_COLUMN);
				String langue = 	cursor.getString(XProvider.LANGUE_COLUMN);
				String capitale = 	cursor.getString(XProvider.CAPITALE_COLUMN);
				String climat = 	cursor.getString(XProvider.CLIMAT_COLUMN);
				String superficie = cursor.getString(XProvider.SUPERFICIE_COLUMN);
				String densite = 	cursor.getString(XProvider.DENSITE_COLUMN);
				String religion = 	cursor.getString(XProvider.RELIGION_COLUMN);
				String nb_expat = 	cursor.getString(XProvider.NOMBRE_EXPATRIES_COLUMN);
				String tel = 		cursor.getString(XProvider.INDICATIFT_TEL_COLUMN);

				if(nom.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Nom du pays : " + nom);

				if(monnaie.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Monnaie : " + monnaie);
				
				if(population.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Population : " + population);
				
				if(formeetat.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Forme de l'Etat : " + formeetat);
				
				if(langue.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Langue : " + langue);
				
				if(capitale.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Capitale : " + capitale);
				
				if(climat.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Climat : " + climat);
				
				if(superficie.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Superficie : " + superficie);
				
				if(densite.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Densité : " + densite);
				
				if(religion.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Religion : " + religion);
				
				if(nb_expat.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Nombre d'expatriés : " + nb_expat);
				
				if(tel.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Téléphone : " + tel);

			} while(cursor.moveToNext());
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == 3 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(XActivity.this, X_ViewPager.class);
			startActivity(intent);
    	}

    	else if(requestCode == 4 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(XActivity.this, Accueil.class);
			startActivity(intent);    		
    	}
    }

    /**
     * Configuration du layout par defaut.
     */
    public void configurationLayout() 
    {
  		setContentView(R.layout.renseignements);

  		// Configuration de la ListView des elements.
  		ListView listview = (ListView) this.findViewById(R.id.listview_);
  		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
  		listview.setAdapter(adapter);
    }
}