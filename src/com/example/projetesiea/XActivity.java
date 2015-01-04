package com.example.projetesiea;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Changements apportes (a completer)
 * 
 * /!\ Supression de la variable : pays_utilisateur /!\
 * /!\ Supression du code accedant aux preferences /!\
 * /!\ Supression du code accedant aux commentaires /!\
 * /!\ Supression du code accedant au menu sliding /!\
 * /!\ Supression de SHOW_PREFERENCES /!\
 * /!\ Commentaire de if(requestCode == SHOW_PREFERENCES) { ... } /!\
 * /!\ Modification du nom du package : com.example.projetesiea; /!\
 * Modification du nom de la classe : XService.class
 * Modification de l'appel de classe : XActivity
 * Supression des donnees pays non utilisees
 *
 * A faire : Supression des : ???
 *
 */
public class XActivity extends Activity {
	// ???
	//NotificationManager notificationManager;
	RenseignementReceiver receiver;

  	// Gestion des elements (item) du contenu.
  	ArrayAdapter<String> adapter;
  	ArrayList<String> arraylist = new ArrayList<String>();

  	/**
  	 * Classe Broadcast Receiver.
  	 */
  	public class RenseignementReceiver extends BroadcastReceiver {
  		@Override
    	public void onReceive(Context context, Intent intent) 
    	{
  	      // Ecriture des donnees de la BDD vers le layout.
  			chargementFromBDD();
    		// ???
    		//notificationManager.cancel(XService.NOTIFICATION_ID);
    	}
  	}

    @Override
  	public void onCreate(Bundle icicle) 
  	{
  		super.onCreate(icicle);

      // Configuration affichage (layout xml)
  		configurationLayout();

      // Lance la recherche du service Internet
	  	majRenseignements();

	  	// Ecriture des donnees de la BDD vers le layout.
	  	chargementFromBDD();

	  	// Configuration d'une notification (???)
		//notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
  	}

    @Override
    public void onResume() 
    {
    	// Gestion des nouvelles donnees en cours de traitement. (???)
    	//notificationManager.cancel(XService.NOTIFICATION_ID);

    	receiver = new RenseignementReceiver();
    	registerReceiver(receiver, new IntentFilter(XService.NOUVEAU_RENSEIGNEMENT_INSERE));

      // Ecriture des donnees de la BDD vers le layout.
    	chargementFromBDD();

    	super.onResume();
    }

    @Override
    public void onPause() 
    {
    	unregisterReceiver(receiver);
    	super.onPause();
    }

	/**
	 * Lance la recherche du service Internet
   * Recupere les donnees du serveur.
	 */
  	private void majRenseignements() 
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
	public void chargementFromBDD() 
	{
		arraylist.clear();

		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(XProvider.CONTENT_RENSEIGNEMENT_URI, null, null, null, null);

		if(cursor.moveToFirst()) {
			do {
				String nom = 					cursor.getString(XProvider.NOM_COLUMN);
				String capitale = 				cursor.getString(XProvider.CAPITALE_COLUMN);
				String monnaie = 				cursor.getString(XProvider.MONNAIE_COLUMN);
				String population = 			cursor.getString(XProvider.POPULATION_COLUMN);

				if(nom.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Nom du pays : " + nom);
				
				if(capitale.equalsIgnoreCase(" ")) ;
				else ajoutRenseignement("Capitale : " + capitale);
				
				if(monnaie.equalsIgnoreCase(" ")) ;
				else {
					ajoutRenseignement("Monnaie : " + monnaie);
					ajoutRenseignement("Monnaie : " + monnaie);
				}

			} while(cursor.moveToNext());
		}
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);

    	// Insertion du menu overflow
    	MenuInflater menuInflater = getMenuInflater();
		  //menuInflater.inflate(R.menu.menu_overflow, menu);

      return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()) { }

    	return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
      super.onActivityResult(requestCode, resultCode, data);

      //if(requestCode == SHOW_PREFERENCES && resultCode == Activity.RESULT_OK) {
    	if(resultCode == Activity.RESULT_OK) {
        // Lance la recherche du service Internet
    		majRenseignements();
    	}
    }

    /**
     * Configuration du layout par défaut.
     */
    public void configurationLayout() 
    {
  		setContentView(R.layout.renseignements);

  		// Configuration de la ListView des éléments.
  		ListView listview = (ListView) this.findViewById(R.id.listview_);
  		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
  		listview.setAdapter(adapter);
    }
}