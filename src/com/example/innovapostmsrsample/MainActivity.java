package com.example.innovapostmsrsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.symbol.emdk.*;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults.STATUS_CODE;

public class MainActivity extends Activity implements EMDKListener {

	//Assign the profile name used in EMDKConfig.xml    
	private String profileName = "InnovaPostMSRProfile";    
	  
	//Declare a variable to store ProfileManager object    
	private ProfileManager mProfileManager = null;    
	  
	//Declare a variable to store EMDKManager object    
	private EMDKManager emdkManager = null;     
	
	TextView textViewCardNo = null;
	TextView textViewExpiryDate = null;
		 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textViewCardNo = (TextView) findViewById(R.id.textViewCardNo);
		textViewExpiryDate = (TextView) findViewById(R.id.textViewExpiryDate);
		
		//The EMDKManager object will be created and returned in the callback.    
		EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);    
		  
		//Check the return status of getEMDKManager    
		if(results.statusCode == STATUS_CODE.FAILURE)    
		{    
			//Failed to create EMDKManager object    
		  
		} 
 
		//In case we have been launched by the DataWedge intent plug-in    
		Intent i = getIntent();    
		handleDecodeData(i); 		 
	}

	@Override
	public void onClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpened(EMDKManager emdkManager) {
		// TODO Auto-generated method stub
		 this.emdkManager = emdkManager;    
		 //Get the ProfileManager object to process the profiles    
		 mProfileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
		 
		 if(mProfileManager != null)    
		 {    
		     try {  
		  
		         String[] modifyData = new String[1];    
		         //Call processPrfoile with profile name and SET flag to create the profile. The modifyData can be null.    
		  
		         EMDKResults results = mProfileManager.processProfile(profileName, ProfileManager.PROFILE_FLAG.SET, modifyData);    
		          if(results.statusCode == STATUS_CODE.FAILURE)    
		          {    
		              //Failed to set profile    
		          }    
		      }
		     catch (Exception ex) {  
		         // Handle any exception  
		     }  
		  
		 }    		 
	}
	
	 @Override    
	 protected void onDestroy() {    
	     // TODO Auto-generated method stub    
	     super.onDestroy();    
	     //Clean up the objects created by EMDK manager    
	     emdkManager.release();    
	 } 	
	 
	 //We need to handle any incoming intents, so let override the onNewIntent method    
	 @Override    
	 public void onNewIntent(Intent i) {    
		 handleDecodeData(i);    
	 }   	 
	 
	 //This function is responsible for getting the data from the intent    
	 private void handleDecodeData(Intent i)    
	 {    
		 Toast.makeText(getApplicationContext(), "handleDecodeData entered", Toast.LENGTH_LONG);
		 
			//Check the intent action is for us
	        if (i.getAction().contentEquals("com.example.innovapostmsrsample.RECVR")) 
	        {
	        	//Get the source of the data
	        	String source = i.getStringExtra("com.motorolasolutions.emdk.datawedge.source");
	        	
	        	//Check if the data has come from the msr
	        	if(source.equalsIgnoreCase("msr"))
	        	{           	
	        		//Get the data from the intent
	    	        String data = i.getStringExtra("com.motorolasolutions.emdk.datawedge.data_string");
//	    	        String name = data.split(";")[0].split(" ")[1];
	            	String track2 = data.split(";")[1];
	            	String[] track2Data = track2.split("=");
	            	String cardNo = track2Data[0];
	            	String expiryDate = track2Data[1].substring(2, 4) + "/" + track2Data[1].substring(0, 2);	//track2Data[1].substring(0, 4);
	            	
	    	        //Check that we have received data
	    	        if(data != null && data.length() > 0)
	    	        {
	    	        	//Display the data to textViewCardNo and textViewExpiryDate;
	    	        	textViewCardNo.setText(cardNo);
	    	        	textViewExpiryDate.setText(expiryDate);
	    	        }
	        	}
	        }	  
	 } 	 
}
