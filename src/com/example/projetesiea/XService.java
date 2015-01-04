package com.example.projetesiea;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Changements apportés
 * 
 * /!\ Supression de la variable : pays_utilisateur /!\
 * /!\ Supression du code accedant aux préférences /!\
 * /!\ Modification du nom du package : com.example.projetesiea; /!\
 * /!\ Modification du nom de la classe : RenseignementsService -> XService
 * /!\ Modification de l'appel de classe : Renseignements.class -> XActivity.class
 * /!\ Modification de l'appel de classe : RenseignementAlarmReceiver -> XAlarmReceiver
 * /!\ Modification RenseignementProvider -> XProvider
 * Suppression des Log.i()
 * Commentaires sur les données pays non utilisées
 *
 * A faire : Supression des : ???
 *
 */
public class XService extends Service {

	public static final String NOUVEAU_RENSEIGNEMENT_INSERE = "Nouveau renseignement enregistré";
	public static String INFORMATION_REFRESHED = "com.example.projetesiea.INFORMATION_REFRESHED";
	/*private Notification nouvelleNotification; // ???*/
	//public static final int NOTIFICATION_ID = 1; // ???

  // objet du service
	private AppLookupTask lastLookup = null;

	AlarmManager alarmManager;
	PendingIntent pendingIntent;

	@Override
	public void onCreate() 
	{
		// Configuration de la notification. // ???
		/*int icon = R.drawable.icon;
		String tickerText = "Nouveau renseignement enregistré";
		long when = System.currentTimeMillis();
		// Création d'une nouvelle notif. à chaque insertion. // ???
		nouvelleNotification = new Notification(icon, tickerText, when);*/

		// Récupération du système d'alarmes.
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// Diffusion de l'action RenseignementAlarmReceiver ?
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(XAlarmReceiver.ACTION_REFRESH_RENSEIGNEMENT_ALARM), 0);
	}

	/**
	 * Etape 3 : Lancement des threads en background.
	 */
  	private class AppLookupTask extends AsyncTask<Void, Pays, Void> {

  		@Override
  		protected Void doInBackground(Void... params) 
  		{
  			// Récupére le XML
  			URL url;

  			try {
  				String feed = "http://latransition.me/pm/pays.php"; // fonctionnel

  				url = new URL(feed);

  				URLConnection connection;
  				connection = url.openConnection();

  				HttpURLConnection httpConnection = (HttpURLConnection) connection;
  				int responseCode = httpConnection.getResponseCode();

  				if(responseCode == HttpURLConnection.HTTP_OK) {
  					InputStream in = httpConnection.getInputStream();

  					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  					DocumentBuilder db = dbf.newDocumentBuilder();

  					// Parse le flux.
  					Document dom = db.parse(in);
  					Element docEle = dom.getDocumentElement();

  					// Récupère une liste de chaque entrée.
  					NodeList nl = docEle.getElementsByTagName("pays");

  					if(nl != null && nl.getLength() > 0) {
  						for(int i=0; i<nl.getLength(); i++) {
  							Element entry = (Element) nl.item(i);

  							String nom = 					entry.getElementsByTagName("nom_pays").item(0).getFirstChild().getNodeValue();
  							String capitale = 				entry.getElementsByTagName("capitale").item(0).getFirstChild().getNodeValue();
  							String monnaie = 				entry.getElementsByTagName("monnaie").item(0).getFirstChild().getNodeValue();
  							String population = 			entry.getElementsByTagName("population").item(0).getFirstChild().getNodeValue();
  							/*String formeEtat = 			entry.getElementsByTagName("formeetat").item(0).getFirstChild().getNodeValue();
  							String roi =					entry.getElementsByTagName("roi").item(0).getFirstChild().getNodeValue();
  							String presidentGouv = 			entry.getElementsByTagName("presidentgouvernement").item(0).getFirstChild().getNodeValue(); 
  							String langue = 				entry.getElementsByTagName("langue").item(0).getFirstChild().getNodeValue();		
  							String gouvernement = 			entry.getElementsByTagName("gouvernement").item(0).getFirstChild().getNodeValue(); 
  							String premierMinistre = 		entry.getElementsByTagName("premierministre").item(0).getFirstChild().getNodeValue(); 
  							String presidentRepublique = 	entry.getElementsByTagName("presidentrepublique").item(0).getFirstChild().getNodeValue(); 
  							String climat = 				entry.getElementsByTagName("climat").item(0).getFirstChild().getNodeValue(); 
  							String superficie = 			entry.getElementsByTagName("superficie").item(0).getFirstChild().getNodeValue(); 
  							String densite = 				entry.getElementsByTagName("densite").item(0).getFirstChild().getNodeValue(); 
  							String religion = 				entry.getElementsByTagName("religion").item(0).getFirstChild().getNodeValue(); 
  							String pib = 					entry.getElementsByTagName("pib").item(0).getFirstChild().getNodeValue();  
  							String nombreExpatries = 		entry.getElementsByTagName("nombreexpatries").item(0).getFirstChild().getNodeValue(); 
  							String tauxChomage = 			entry.getElementsByTagName("tauxchomage").item(0).getFirstChild().getNodeValue(); 
  							String indicatifTel = 			entry.getElementsByTagName("indicatiftel").item(0).getFirstChild().getNodeValue();*/  							

  							//Pays pays = new Pays(nom, monnaie, population, formeEtat, roi, presidentGouv, langue, capitale, gouvernement, premierMinistre, 
  								//	presidentRepublique, climat, superficie, densite, religion, pib, nombreExpatries, tauxChomage, indicatifTel);
  							Pays pays = new Pays(nom, monnaie, population, "", "", "", "", capitale, "", "", "", "", "", "", "", "", "", "", "");

  							// Traite le nouveau pays ajouté.
  							ajoutPays(pays);
  						}
  					}
  				}
  			} catch (MalformedURLException e) {
  				e.printStackTrace();
  			} catch (IOException e) {
  				e.printStackTrace();
  			} catch (ParserConfigurationException e) {
  				e.printStackTrace();
  			} catch (SAXException e) {
  				e.printStackTrace();
  			}
  			finally { }

  			return null;
  		}

      /**
       * ???
       
    	@Override
    	protected void onProgressUpdate(Pays... values) 
    	{
    		// Récupération d'une notification.
    		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    		Context context = getApplicationContext();
    		String expandedText = values[0].getNom();
    		String expandedTitle = "M: taratata";

    		// Démarre une nouvelle activité.
    		Intent intent = new Intent(XService.this, XActivity.class);
    		PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);

    		// Configuration d'une notification.
    		nouvelleNotification.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);
    		nouvelleNotification.when = java.lang.System.currentTimeMillis();

    		// Envoi d'une notification.
    		notificationManager.notify(NOTIFICATION_ID, nouvelleNotification);

    		Toast.makeText(context, expandedTitle, Toast.LENGTH_SHORT).show();
    	}*/

    	@Override
    	protected void onPostExecute(Void result) 
    	{
    		// Appel des receivers.
  	  	sendBroadcast(new Intent(INFORMATION_REFRESHED));
  	  	stopSelf();
    	}
  	}

  	/**
  	 * Etape 1 : Lancement du service.
  	 */
  	@Override
  	public int onStartCommand(Intent intent, int flags, int startId) 
  	{
  		refreshRenseignements();

      return Service.START_NOT_STICKY;
    }

    /**
     * 
     */
    @Override
    public IBinder onBind(Intent intent) 
    {
    	return null;
    }

    /**
     * Insertion / Mise à jour de la base de données.
     * @param _pays
     */
    private void ajoutPays(Pays _pays) 
    {
    	ContentResolver contentResolver = getContentResolver();

    	// Construit une clause where pour vérifier que ce pays n'est pas déjà dans le provider.
    	String where = XProvider.KEY_NOM + " = '" + _pays.getNom() + "'";

    	// Si le pays est nouveau, on l'insère.
    	if(contentResolver.query(XProvider.CONTENT_RENSEIGNEMENT_URI, null, where, null, null).getCount() == 0) {

    		ContentValues values = new ContentValues();

		    values.put(XProvider.KEY_NOM, 						_pays.getNom());
        values.put(XProvider.KEY_CAPITALE,          _pays.getCapitale());
		    values.put(XProvider.KEY_MONNAIE, 					_pays.getMonnaie());
		    values.put(XProvider.KEY_POPULATION,				_pays.getPopulation());
		    /*values.put(RenseignementProvider.KEY_FORMEETAT, 				_pays.getFormeEtat());
		    values.put(RenseignementProvider.KEY_ROI, 						_pays.getRoi());
		    values.put(RenseignementProvider.KEY_PRESIDENT_GOUVERNEMENT, 	_pays.getPresidentGouvernement());
		    values.put(RenseignementProvider.KEY_LANGUE, 					_pays.getLangue());
		    values.put(RenseignementProvider.KEY_GOUVERNEMENT, 				_pays.getGouvernement());
		    values.put(RenseignementProvider.KEY_PREMIER_MINISTRE,			_pays.getPremierMinistre());
		    values.put(RenseignementProvider.KEY_PRESIDENT_REPUBLIQUE,		_pays.getPresidentRepublique());
		    values.put(RenseignementProvider.KEY_PRESIDENT_GOUVERNEMENT,	_pays.getPresidentGouvernement());
		    values.put(RenseignementProvider.KEY_CLIMAT,					_pays.getClimat());
		    values.put(RenseignementProvider.KEY_SUPERFICIE,				_pays.getSuperficie());
		    values.put(RenseignementProvider.KEY_DENSITE,					_pays.getDensite());
		    values.put(RenseignementProvider.KEY_RELIGION,					_pays.getReligion());
		    values.put(RenseignementProvider.KEY_PIB,						_pays.getPIB());
		    values.put(RenseignementProvider.KEY_NOMBRE_EXPATRIES,			_pays.getNombreExpatries());
		    values.put(RenseignementProvider.KEY_TAUX_CHOMAGE,				_pays.getTauxChomage());
		    values.put(RenseignementProvider.KEY_INDICATIF_TEL,				_pays.getIndicatifTel());*/

    		contentResolver.insert(XProvider.CONTENT_RENSEIGNEMENT_URI, values);

    		annoncePays(_pays);
    	}
    }

    /**
     * ???
     * @param _pays
     */
    private void annoncePays(Pays _pays) 
    {
    	Intent intent = new Intent(NOUVEAU_RENSEIGNEMENT_INSERE);
    	intent.putExtra("nom", _pays.getNom());

    	// Envoi le pending intent ?
    	sendBroadcast(intent);
    }

    /**
     * Etape 2 : Lance les threads en background.
     */
    private void refreshRenseignements() 
    {
    	if(lastLookup == null || lastLookup.getStatus().equals(AsyncTask.Status.FINISHED)) {
    		lastLookup = new AppLookupTask();
    		lastLookup.execute();
    	}
    }

}