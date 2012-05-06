package com.mktneutral.client;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
import com.google.gwt.user.client.ui.TextBox;
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
       flexTable.getCellFormatter().addStyleName(0,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(0,0,5);
       flexTable.setText(0,0,"Jerry's Delicious Bookmarks");

       flexTable.getCellFormatter().addStyleName(1,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(1,0,5);
       flexTable.getFlexCellFormatter().setHorizontalAlignment(1,0,HasHorizontalAlignment.ALIGN_CENTER);
 
       TextBox searchTextBox = new TextBox();
       searchTextBox.setVisibleLength(40);
       searchTextBox.setMaxLength(80);
       HorizontalPanel searchPanel = new HorizontalPanel();
       searchPanel.setSpacing(0);
       searchPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
       searchPanel.setWidth("100%");
       searchPanel.add( searchTextBox );
       Button searchButton = new Button("Search");
       searchButton.addStyleName("deliButton");
       searchPanel.add( searchButton );
       flexTable.setWidget(1,0,searchPanel);
      
       flexTable.setText(2,0,"Description");
       flexTable.setText(2,1,"Tags");
       flexTable.setText(2,2,"Shared");
       flexTable.setText(2,3,"Private");
       flexTable.setText(2,4,"Date Added");
       for ( int i=0; i<5; i++ ) {
	  flexTable.getCellFormatter().addStyleName(2,i,"headerCell");
       }
       flexTable.setWidth("100%");
       flexTable.setCellSpacing(0);
        
       flexTable.addClickHandler( new TableClickHandler( flexTable ) );
       getFavoritesList();

       Button nextButton = new Button( "Next 30 >>", new NextButtonClickHandler( flexTable, query ) ); 
       nextButton.addStyleName("deliButton");
       flexTable.setWidget(33,0,nextButton);

       flexTable.getCellFormatter().addStyleName(33,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(33,0,5);       

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