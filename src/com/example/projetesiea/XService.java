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
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * Gestion en t�che de fond des requ�tes HTTP / Internet
 * Stockage en BDD (SQLite)
 */
public class XService extends Service {

	public static final String NOUVEAU_RENSEIGNEMENT_INSERE = "Nouveau renseignement enregistr�";
	public static String INFORMATION_REFRESHED = "com.example.projetesiea.INFORMATION_REFRESHED";

	// objet du service
	private AppLookupTask lastLookup = null;

	AlarmManager alarmManager;
	PendingIntent pendingIntent;

	@Override
	public void onCreate() 
	{
		// R�cup�ration du syst�me d'alarmes.
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// Diffusion de l'action RenseignementAlarmReceiver ?
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(X_BroadcastReceiver.ACTION_REFRESH_RENSEIGNEMENT_ALARM), 0);
	}

	/**
	 * Etape 3 : Lancement du thread en t�che de fond.
	 */
  	private class AppLookupTask extends AsyncTask<Void, Pays, Void> {

  		@Override
  		protected Void doInBackground(Void... params) 
  		{
  			// R�cup�re le XML ( REST )
  			URL url;

  			try {
  				String feed = "http://latransition.me/pm/pays.php";

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

  					// R�cup�re une liste de chaque entr�e.
  					NodeList nl = docEle.getElementsByTagName("pays");

  					if(nl != null && nl.getLength() > 0) {
  						for(int i=0; i<nl.getLength(); i++) {
  							Element entry = (Element) nl.item(i);

  							String nom = 			 entry.getElementsByTagName("nom_pays").item(0).getFirstChild().getNodeValue();
  							String capitale = 		 entry.getElementsByTagName("capitale").item(0).getFirstChild().getNodeValue();
  							String monnaie = 		 entry.getElementsByTagName("monnaie").item(0).getFirstChild().getNodeValue();
  							String population = 	 entry.getElementsByTagName("population").item(0).getFirstChild().getNodeValue();
  							String formeEtat = 	 	 entry.getElementsByTagName("formeetat").item(0).getFirstChild().getNodeValue(); 
  							String langue = 		 entry.getElementsByTagName("langue").item(0).getFirstChild().getNodeValue(); 
  							String climat = 		 entry.getElementsByTagName("climat").item(0).getFirstChild().getNodeValue(); 
  							String superficie = 	 entry.getElementsByTagName("superficie").item(0).getFirstChild().getNodeValue(); 
  							String densite = 		 entry.getElementsByTagName("densite").item(0).getFirstChild().getNodeValue(); 
  							String religion = 		 entry.getElementsByTagName("religion").item(0).getFirstChild().getNodeValue();
  							String nombreExpatries = entry.getElementsByTagName("nombreexpatries").item(0).getFirstChild().getNodeValue();
  							String indicatifTel = 	 entry.getElementsByTagName("indicatiftel").item(0).getFirstChild().getNodeValue();
 							
  							Pays pays = new Pays(nom, monnaie, population, formeEtat, langue, capitale, climat, superficie, densite, religion, nombreExpatries, indicatifTel);

  							// Traite le nouveau pays ajout�.
  							addToDB(pays);
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
  		 * Etape 4
  		 */
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
  		launchHTTPRequest();

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
     * Etape 5
     * Insertion / Mise � jour de la base de donn�es.
     * @param _pays
     */
    private void addToDB(Pays _pays) 
    {
    	ContentResolver contentResolver = getContentResolver();

    	// Construit une clause where pour v�rifier que ce pays n'est pas d�j� dans le provider.
    	String where = XProvider.KEY_NOM + " = '" + _pays.getNom() + "'";

    	// Si le pays est nouveau, on l'ins�re.
    	if(contentResolver.query(XProvider.CONTENT_RENSEIGNEMENT_URI, null, where, null, null).getCount() == 0) {

    		ContentValues values = new ContentValues();

		    values.put(XProvider.KEY_NOM, 				_pays.getNom());
		    values.put(XProvider.KEY_CAPITALE, 			_pays.getCapitale());
		    values.put(XProvider.KEY_MONNAIE, 			_pays.getMonnaie());
		    values.put(XProvider.KEY_POPULATION, 		_pays.getPopulation());
		    values.put(XProvider.KEY_FORMEETAT, 		_pays.getFormeEtat());
		    values.put(XProvider.KEY_LANGUE, 			_pays.getLangue());
		    values.put(XProvider.KEY_CLIMAT,			_pays.getClimat());
		    values.put(XProvider.KEY_SUPERFICIE,		_pays.getSuperficie());
		    values.put(XProvider.KEY_DENSITE,			_pays.getDensite());
		    values.put(XProvider.KEY_RELIGION,			_pays.getReligion());
		    values.put(XProvider.KEY_NOMBRE_EXPATRIES,	_pays.getNombreExpatries());
		    values.put(XProvider.KEY_INDICATIF_TEL,		_pays.getIndicatifTel());

    		contentResolver.insert(XProvider.CONTENT_RENSEIGNEMENT_URI, values);

    		endProcess(_pays);
    	}
    }

    /**
     * Etape 6
     * @param _element
     */
    private void endProcess(Pays _element) 
    {
    	Intent intent = new Intent(NOUVEAU_RENSEIGNEMENT_INSERE);
    	intent.putExtra("nom", _element.getNom());

    	// Envoi le pending intent
    	sendBroadcast(intent);
    }

    /**
     * Etape 2 : Lance le thread en t�che de fond.
     */
    private void launchHTTPRequest() 
    {
    	if(lastLookup == null || lastLookup.getStatus().equals(AsyncTask.Status.FINISHED)) {
    		lastLookup = new AppLookupTask();
    		lastLookup.execute();
    	}
    }

}