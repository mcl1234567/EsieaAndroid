package com.example.projetesiea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Changements apportes
 * 
 * /!\ Renommage classe XAlarmReceiver /!\
 * /!\ Remplacement "com.lanouveller.franceexpatries.ACTION_REFRESH_RENSEIGNEMENT_ALARM"; -> "com.example.projetesiea.ACTION_REFRESH_RENSEIGNEMENT_ALARM"; /!\
 * /!\ Modification du nom du package : com.example.projetesiea; /!\
 * /!\ Rennomage XService.class /!\
 * Supression Log.i()
 *
 */
public class XAlarmReceiver extends BroadcastReceiver {

	public static final String ACTION_REFRESH_RENSEIGNEMENT_ALARM = "com.example.projetesiea.ACTION_REFRESH_RENSEIGNEMENT_ALARM";

 	@Override
 	public void onReceive(Context context, Intent intent) 
 	{
 		context.startService(new Intent(context, XService.class));
 	}
}