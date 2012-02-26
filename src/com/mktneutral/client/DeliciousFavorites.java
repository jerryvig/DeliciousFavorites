package com.mktneutral.client;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

public class DeliciousFavorites implements EntryPoint {
   //Widget definitions
   private VerticalPanel vertPanel = new VerticalPanel();
   private FlexTable flexTable = new FlexTable();

   //Data definitions.
   private ArrayList<DeliciousFavorite> favoritesList = new ArrayList();

   //Callback definitions
   private DeliciousFavoriteServiceAsync dfSvc = GWT.create(DeliciousFavoriteService.class);

   public void onModuleLoad() {
       flexTable.setText(0,0,"Description");
       flexTable.setText(0,1,"Tags");
       flexTable.setText(0,2,"URL");
       flexTable.setText(0,3,"Shared");
       flexTable.setText(0,4,"Private");
       flexTable.setText(0,5,"Date Added");
       for ( int i=0; i<6; i++ ) {
	  flexTable.getCellFormatter().addStyleName(0,i,"headerCell");
       }
       flexTable.setWidth("75%");

       getFavoritesList();
       
       vertPanel.add( flexTable );
       RootPanel.get("mainPanel").add( vertPanel );              
   }

   private void getFavoritesList() {
      //RPC Code will go here.
      if ( dfSvc == null ) dfSvc = GWT.create(DeliciousFavoriteService.class);

      AsyncCallback<ArrayList<DeliciousFavorite>> callback = new AsyncCallback<ArrayList<DeliciousFavorite>>() {
	  public void onFailure( Throwable caught ) {
              Window.alert( caught.getMessage() );
          }

          public void onSuccess( ArrayList<DeliciousFavorite> favsList ) {
	      for ( DeliciousFavorite fav : favsList ) {
		  favoritesList.add( fav );
              }
              int i = 1;
              for ( DeliciousFavorite fav : favoritesList ) {
		 flexTable.setText( i, 0, fav.getDescription() );
                 flexTable.setText( i, 1, fav.getTag() );
                 Anchor href = new Anchor();
                 href.setTarget("_blank");
                 href.setHref( fav.getHref() );
                 href.setText( fav.getHref() );
                 flexTable.setWidget( i, 2, href );
                 flexTable.setText( i, 3, fav.getShared() );
                 flexTable.setText( i, 4, fav.getPrivate() );
                 flexTable.setText( i, 5, fav.getTime() );
		 i++;
              }
          }
      };
      String[] args = { "dummyArg" };
      dfSvc.getFavorites( args, callback );
   } 
}