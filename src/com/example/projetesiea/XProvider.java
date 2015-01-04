package com.example.projetesiea;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Element a changer lors d'une copie de provider. marque (**)
 *
 * Changements apportes
 * 
 * /!\ Renommage classe XProvider /!\
 * /!\ Remplacement com.lanouveller.provider -> com.example.provider /!\
 * /!\ Modification du nom du package : com.example.projetesiea; /!\
 *
 * case OBJETS:  return "vnd.android.cursor.dir/vnd.lanouveller.franceexpatries";     -> case OBJETS:  return "vnd.android.cursor.dir/vnd.example.projetesiea";
 * case OBJET_ID:  return "vnd.android.cursor.item/vnd.lanouveller.franceexpatries";  -> case OBJET_ID:  return "vnd.android.cursor.item/vnd.example.projetesiea";
 *
 * Commentaires sur les donnees pays non utilisees
 * 
 * uriMatcher.addURI("com.example.provider.Renseignements", "pays", OBJETS);       -> uriMatcher.addURI("com.example.provider.pays", "pays", OBJETS);
 * uriMatcher.addURI("com.example.provider.Renseignements", "pays/#", OBJET_ID);   -> uriMatcher.addURI("com.example.provider.pays", "pays/#", OBJET_ID);
 *
 * N'A PAS CHANGER ! : public static final Uri CONTENT_RENSEIGNEMENT_URI = Uri.parse("content://com.example.provider.pays/pays");
 *
 */
public class XProvider extends ContentProvider {

	public static final Uri CONTENT_RENSEIGNEMENT_URI = Uri.parse("content://com.example.provider.pays/pays");

  	@Override
  	public boolean onCreate() 
  	{
  		Context context = getContext();

  		DatabaseHelper dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
  		database = dbHelper.getWritableDatabase();

    	return (database == null) ? false : true;
  	}

  	// Fonction de requete.
  	@Override
  	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) 
  	{
  		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

  		qb.setTables(TABLE);

  		// S'il s'agit d'une requete sur une ligne, on limite le resultat.
  		switch(uriMatcher.match(uri)) {
      		case OBJET_ID: qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1)); break;
      		default: break;
  		}
  		
  		// Applique la requete a la base.
  		Cursor cursor = qb.query(database, projection, selection, selectionArgs, null, null, null);

  		// Enregistre le ContextResolver pour qu'il soit averti si le resultat change. 
  		cursor.setNotificationUri(getContext().getContentResolver(), uri);

  		// Renvoie un curseur.
  		return cursor;
  	}

  	// Insertion des donnees.
  	@Override
  	public Uri insert(Uri _uri, ContentValues _initialValues) 
  	{
  		// Insere la nouvelle ligne. Renvoie son numero en cas de succes
  		long rowID = database.insert(TABLE, "nullColumnHack", _initialValues);

  		// Renvoie l'URI de la nouvelle ligne.
  		if(rowID > 0) {
  		  	// **
  			Uri uri = ContentUris.withAppendedId(CONTENT_RENSEIGNEMENT_URI, rowID);
  			getContext().getContentResolver().notifyChange(uri, null);

  			return uri;
  		}

  		throw new SQLException("Echec de l'ajout d'une ligne dans " + _uri);
  	}

  	// Suppression des donnees.
  	@Override
  	public int delete(Uri uri, String where, String[] whereArgs) 
  	{
  		int count;

  		switch(uriMatcher.match(uri)) {
  			case OBJETS: count = database.delete(TABLE, where, whereArgs); break;
  			case OBJET_ID: {
  				String segment = uri.getPathSegments().get(1);
  				count = database.delete(TABLE, KEY_ID + "=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
  			} break;

  			default: throw new IllegalArgumentException("URI non supportée : " + uri);
  		}

  		getContext().getContentResolver().notifyChange(uri, null);

  		return count;
  	}
  	
  	// Mise a jour des donnees.
  	@Override
  	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) 
  	{
  		int count;

  		switch(uriMatcher.match(uri)) {
      		case OBJETS: count = database.update(TABLE, values, where, whereArgs); break;
      		case OBJET_ID: {
      			String segment = uri.getPathSegments().get(1);
                count = database.update(TABLE, values, KEY_ID 
                             				+ "=" + segment 
                             				+ (!TextUtils.isEmpty(where) ? " AND (" 
                             				+ where + ')' : ""), whereArgs);
      		} break;

      		default: throw new IllegalArgumentException("URI inconnue " + uri);
  		}

  		getContext().getContentResolver().notifyChange(uri, null);

    	return count;
  	}

  	// Retourne les donnees sous le format specifie.
  	@Override
  	public String getType(Uri uri) 
  	{
  		switch(uriMatcher.match(uri)) {
  			case OBJETS: 	return "vnd.android.cursor.dir/vnd.example.projetesiea";
  			case OBJET_ID: 	return "vnd.android.cursor.item/vnd.example.projetesiea";
  			default: throw new IllegalArgumentException("URI non supportee : " + uri);
  		}
  	}

  	// Cree les constantes utilisees pour differencier les requetes URI
  	private static final int OBJETS = 1;
  	private static final int OBJET_ID = 2;

  	private static final UriMatcher uriMatcher;

  	// Alloue l'objet UriMatcher. Une URI terminee par 'pays' correspondra a une requete sur tous les renseignements.
  	// Une URI terminee par'/[rowID]'  correspondra a une ligne unique. **
  	static {
  		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  		uriMatcher.addURI("com.example.provider.pays", "pays", OBJETS);
  		uriMatcher.addURI("com.example.provider.pays", "pays/#", OBJET_ID);
  	}

  	// La base de donnees
  	private SQLiteDatabase database;
  	// A changer lors d'une copie du provider **
  	private static final String TAG = "XProvider";
  	// **
  	private static final String DATABASE_NAME = "franceexpatries_renseignements.db";
  	private static final int DATABASE_VERSION = 1;
  	// **
  	private static final String TABLE = "pays";

  	// Noms de colonnes **
  	public static final String KEY_ID = "_id";
  	public static final String KEY_NOM = "nom";
    public static final String KEY_CAPITALE = "capitale";
  	public static final String KEY_MONNAIE = "monnaie";
  	public static final String KEY_POPULATION = "population";
  	public static final String KEY_FORMEETAT = "formeetat";
  	public static final String KEY_LANGUE = "langue";
  	public static final String KEY_CLIMAT = "climat";
  	public static final String KEY_SUPERFICIE = "superficie";
  	public static final String KEY_DENSITE = "densite";
  	public static final String KEY_RELIGION = "religion";
  	public static final String KEY_NOMBRE_EXPATRIES = "nombre_expatries";
  	public static final String KEY_INDICATIF_TEL = "indicatif_tel";

  	// Indexes **
  	public static final int NOM_COLUMN = 1;
  	public static final int MONNAIE_COLUMN = 2;
  	public static final int POPULATION_COLUMN = 3;
  	public static final int FORMEETAT_COLUMN = 4;
  	public static final int LANGUE_COLUMN = 5;
    public static final int CAPITALE_COLUMN = 6;
  	public static final int CLIMAT_COLUMN = 7;
  	public static final int SUPERFICIE_COLUMN = 8;
  	public static final int DENSITE_COLUMN = 9;
  	public static final int RELIGION_COLUMN = 10;
  	public static final int NOMBRE_EXPATRIES_COLUMN = 11;
  	public static final int INDICATIFT_TEL_COLUMN = 12;

  	// Classe helper pour ouvrir, creer et gerer le controle de version de la base.
  	private static class DatabaseHelper extends SQLiteOpenHelper {
  	  	// **
  		private static final String DB_CREATE = "CREATE TABLE " + TABLE 
                                								+ " (" 
																+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
																+ KEY_NOM + " TEXT, "
																+ KEY_CAPITALE + " TEXT, "
																+ KEY_MONNAIE + " TEXT, "
																+ KEY_POPULATION + " TEXT, "
																+ KEY_FORMEETAT + " TEXT, "
																+ KEY_LANGUE + " TEXT, "
																+ KEY_CLIMAT + " TEXT, "
																+ KEY_SUPERFICIE + " TEXT, "
																+ KEY_DENSITE + " TEXT, "
																+ KEY_RELIGION + " TEXT, "
																+ KEY_NOMBRE_EXPATRIES + " TEXT, "
																+ KEY_INDICATIF_TEL + " TEXT);";

      /*private static final String DB_CREATE = "CREATE TABLE " + TABLE 
                                + " (" 
                                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + KEY_NOM + " TEXT, "
                                + KEY_CAPITALE + " TEXT, "
                                + KEY_MONNAIE + " TEXT, "
                                + KEY_POPULATION + " TEXT);";*/

  		public DatabaseHelper(Context context, String name, CursorFactory factory, int version) 
  		{
  			super(context, name, factory, version);
  		}

  		@Override
  		public void onCreate(SQLiteDatabase db) 
  		{
  			db.execSQL(DB_CREATE);
  		}

  		// Initialisee lors d'un changement de version.
  		@Override
  		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
  		{
  			Log.w(TAG, "Mise a jour de la version " + oldVersion + " vers la version " + newVersion 
  					+ ", les anciennes donnees seront detruites ");
  			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
  			onCreate(db);
  		}
  	}
}