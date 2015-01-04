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
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activité d'affichage de la ListView ( pays )
 */
public class XActivity extends Activity {
	//NotificationManager notificationManager; //
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
  			displayFromBDD();

    		//notificationManager.cancel(XService.NOTIFICATION_ID);    		// 
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

	  	// Configuration d'une notification 
		//notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);	//
  	}

    @Override
    public void onResume() 
    {
    	// Gestion des nouvelles donnees en cours de traitement.
    	//notificationManager.cancel(XService.NOTIFICATION_ID);	  //

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
      super.onActivityResult(requestCode, resultCode, data);

    	if(resultCode == Activity.RESULT_OK) {
    		// Lance le service pour gérer les requêtes HTTP
    		launchIntentService();
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