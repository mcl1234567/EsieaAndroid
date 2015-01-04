package com.example.projetesiea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *	BoradcastReceiver
 */
public class X_BroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_REFRESH_RENSEIGNEMENT_ALARM = "com.example.projetesiea.ACTION_REFRESH_RENSEIGNEMENT_ALARM";

 	@Override
 	public void onReceive(Context context, Intent intent) 
 	{
 		context.startService(new Intent(context, XService.class));
 	}
}