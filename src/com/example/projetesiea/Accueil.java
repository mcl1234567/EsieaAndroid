package com.example.projetesiea;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * Activité 'Accueil'
 * Vignettes cliquables
 * Actionbar disponible
 */
public class Accueil extends ActionBarActivity {

	private static int XACTIVITY_INTENT = 1;
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
	protected void onResume() 
	{	
		super.onResume();
		configurationVignettes();
	}

	/**
	 *  Creation du menu 'overflow'.
	 */
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	// Insertion du menu overflow
		getMenuInflater().inflate(R.menu.main, menu);

      	return true;
    }

	/**
	 *  Gestion du menu 'overflow'.
	 */
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	super.onOptionsItemSelected(item);

    	int itemId = item.getItemId();
		if(itemId == R.id.action_settings) {
			{
				Intent intent = new Intent(this, XActivity.class);
				startActivityForResult(intent, XACTIVITY_INTENT);
			}
			return true;
		}

    	return false;
    }

    /**
     * Configuration des items vignettes
     */
    public void configurationVignettes() 
    {
		ImageView imageviewListView = (ImageView) findViewById(R.id.vignette_renseignements);
		ImageView imageviewViewPager = (ImageView) findViewById(R.id.vignette_correspondants);

		// Configuration des vignettes.
		imageviewListView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent intent = new Intent(Accueil.this, XActivity.class);
				startActivity(intent);
			}
		});

		imageviewViewPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent intent = new Intent(Accueil.this, X_ViewPager.class);
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