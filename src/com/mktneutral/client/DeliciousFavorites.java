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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class DeliciousFavorites implements EntryPoint {
   //Widget definitions
   private VerticalPanel vertPanel = new VerticalPanel();
   private FlexTable flexTable = new FlexTable();
   private DeliciousFavoriteQuery query = new DeliciousFavoriteQuery( 0, "time", "desc" );

   //Callback definitions
   private DeliciousFavoriteServiceAsync dfSvc = GWT.create(DeliciousFavoriteService.class);

   public void onModuleLoad() {
       flexTable.setText(0,0,"Jerry's Delicious Bookmarks");
       flexTable.getCellFormatter().addStyleName(0,0,"footerCell");

       flexTable.getFlexCellFormatter().setColSpan(0,0,5);
       flexTable.setText(1,0,"Description");
       flexTable.setText(1,1,"Tags");
       flexTable.setText(1,2,"Shared");
       flexTable.setText(1,3,"Private");
       flexTable.setText(1,4,"Date Added");
       for ( int i=0; i<5; i++ ) {
	  flexTable.getCellFormatter().addStyleName(1,i,"headerCell");
       }
       flexTable.setWidth("100%");
       flexTable.setCellSpacing(0);
        
       flexTable.addClickHandler( new TableClickHandler( flexTable ) );
       getFavoritesList();

       Button nextButton = new Button( "Next 30 >>", new NextButtonClickHandler( flexTable, query ) ); 
       nextButton.addStyleName("deliButton");
       flexTable.setWidget(32,0,nextButton);

       flexTable.getCellFormatter().addStyleName(32,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(32,0,5);       

       vertPanel.add( flexTable );
       vertPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
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
	     DeliciousFavoritesHelpers.fillFlexTable( flexTable, favsList );     
          }
      };
      dfSvc.getFavorites( "time", "desc", 0, callback );
   } 
}