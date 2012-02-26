package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

public class NextButtonClickHandler implements ClickHandler {
   private FlexTable flexTable;
   private DeliciousFavoriteServiceAsync dfSvc;
   private String sortDirection = "desc";

   NextButtonClickHandler( FlexTable _ft ) {
       flexTable = _ft;
       dfSvc = GWT.create(DeliciousFavoriteService.class);
   }

   public void onClick( ClickEvent event ) {
       Window.alert( "You clicked the button" );
   }

   private void doSort( String sortColumn ) {
     AsyncCallback<ArrayList<DeliciousFavorite>> callback = new AsyncCallback<ArrayList<DeliciousFavorite>>() {
       public void onFailure( Throwable caught ) {
	  Window.alert( caught.getMessage() );
       }
       public void onSuccess( ArrayList<DeliciousFavorite> favsList ) {
	   DeliciousFavoritesHelpers.fillFlexTable( flexTable, favsList );
       }
     };

     flexTable.setText(0,0,"Description");
     flexTable.setText(0,1,"Tags");
     flexTable.setText(0,2,"Shared");
     flexTable.setText(0,3,"Private");
     flexTable.setText(0,4,"Date Added");

     String arrowString = "↑";
     if ( sortDirection.equals("asc") ) {
	 sortDirection = "desc";
         arrowString = "↓"; 
     }
     else {
         sortDirection = "asc";
         arrowString = "↑";
     }
     
     if ( sortColumn.equals("description") ) {
	 flexTable.setText(0,0,"Description "+arrowString);
     }
     else if ( sortColumn.equals("tags") ) {
          flexTable.setText(0,1,"Tags "+arrowString);
     } 
     else if ( sortColumn.equals("shared") ) {
          flexTable.setText(0,2,"Shared "+arrowString);
     }
     else if ( sortColumn.equals("private") ) {
          flexTable.setText(0,3,"Tags "+arrowString);
     }
     else if ( sortColumn.equals("time") ) {
          flexTable.setText(0,4,"Date Added "+arrowString);
     }

     dfSvc.getFavorites( sortColumn, sortDirection, callback );
   }
}