package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

public class NextButtonClickHandler implements ClickHandler {
   private FlexTable flexTable;
   private DeliciousFavoriteServiceAsync dfSvc;
   private DeliciousFavoriteQuery query;
   
    NextButtonClickHandler( FlexTable _ft, DeliciousFavoriteQuery _query ) {
       flexTable = _ft;
       query = _query;
       dfSvc = GWT.create(DeliciousFavoriteService.class);
   }

   public void onClick( ClickEvent event ) {
     AsyncCallback<ArrayList<DeliciousFavorite>> callback = new AsyncCallback<ArrayList<DeliciousFavorite>>() {
       public void onFailure( Throwable caught ) {
	  Window.alert( caught.getMessage() );
       }
       public void onSuccess( ArrayList<DeliciousFavorite> favsList ) {
	   DeliciousFavoritesHelpers.fillFlexTable( flexTable, favsList );
       }
     };
     
     flexTable.setText(1,0,"Description");
     flexTable.setText(1,1,"Tags");
     flexTable.setText(1,2,"Shared");
     flexTable.setText(1,3,"Private");
     flexTable.setText(1,4,"Date Added");
    
     query.setStartRow( query.getStartRow()+30 );
     if ( query.getStartRow() > 0 ) {
	 Button prevButton = new Button("<< Prev 30", new ClickHandler() {
	    public void onClick( ClickEvent event ) {
		Window.alert( "You clicked the back button" );  
            }  
	 });
         prevButton.addStyleName("deliButton");
         flexTable.setWidget(32,0,prevButton);
     }

     dfSvc.getFavorites( query.getSortColumn(), query.getSortDirection(), query.getStartRow(), callback );    
   }
}