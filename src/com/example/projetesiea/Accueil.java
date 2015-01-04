package com.example.projetesiea;

import java.util.Calendar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Changements apportés
 * 
 * /!\ Supression des fonctionnalités Twitter ** Facebook ** correspondants ** blog ** utilisateur ** choix du pays ** commentaire /!\
 * /!\ package com.lanouveller.franceexpatries; -> package com.example.projetesiea; /!\
 * /!\ Remplacement de Renseignements.class -> XActivity.class /!\
 *
 */
public class Accueil extends Activity {
  	// Stocke l'activité voulue pendant l'initialisation.
	private static int RENSEIGNEMENT_ECHEC = 2;

  	/**
  	 * Lancement de l'activite Accueil
  	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.accueil);
		setTitleColor(Color.WHITE);

		// Configuration des vignettes sur l'accueil.
		configurationVignettes();
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		configurationVignettes();
	}

	/**
	 *  Création du menu Overflow.
	 */
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	// Insertion du menu overflow
    	MenuInflater menuInflater = getMenuInflater();
		//menuInflater.inflate(R.menu.menu_overflow, menu);

      	return true;
    }

	/**
	 *  Gestion 'Menu Overflow'.
	 */
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()) { }

    	return false;
    }

    /**
     * Configuration des items vignettes
     */
    public void configurationVignettes() 
    {
		ImageView imageviewRenseignements = (ImageView) findViewById(R.id.vignette_renseignements);

		// Configuration des vignettes.
		imageviewRenseignements.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent intent = new Intent(Accueil.this, XActivity.class);
				startActivity(intent);
			}
		});		
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	// X_ACTIVITY_ECHEC ou RENSEIGNEMENT_ECHEC
    	if(requestCode == RENSEIGNEMENT_ECHEC && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(Accueil.this, XActivity.class);
			startActivity(intent);
    	}
    }
}