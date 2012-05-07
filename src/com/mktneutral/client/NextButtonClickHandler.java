package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

public class NextButtonClickHandler extends ButtonClickHandler {
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
     
     flexTable.setText(2,0,"Description");
     flexTable.setText(2,1,"Tags");
     flexTable.setText(2,2,"Shared");
     flexTable.setText(2,3,"Private");
     flexTable.setText(2,4,"Date Added");
    
     query.setStartRow( query.getStartRow()+30 );
      
     drawNextPreviousButtons();

     dfSvc.getFavorites( query.getSortColumn(), query.getSortDirection(), query.getStartRow(), callback );    
   }
}