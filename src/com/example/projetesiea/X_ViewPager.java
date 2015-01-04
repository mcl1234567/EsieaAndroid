package com.example.projetesiea;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Affichage des correspondants - défilant 
 */
public class X_ViewPager extends ActionBarActivity {
	
	public SectionsPagerAdapter mSectionsPagerAdapter;
	public ViewPager mViewPager;
	 private Context context;

	  	/**
	  	 * Lancement de l'activite X_ViewPager
	  	 */
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);

			setContentView(R.layout.viewpager);
			
			//Activity activity = (Activity) context;
			context = getApplicationContext();

			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), context);
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
		}
		
		@Override
		protected void onResume() 
		{	
			super.onResume();
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
					// Lancement de XActivity ( ListView d'éléments )
					Intent intent = new Intent(this, XActivity.class);
					startActivityForResult(intent, 1);
				}
				return true;
			}

	    	return false;
	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	    {
	    	super.onActivityResult(requestCode, resultCode, data);
	    	if(resultCode == Activity.RESULT_OK) { 	   }
	    }
}