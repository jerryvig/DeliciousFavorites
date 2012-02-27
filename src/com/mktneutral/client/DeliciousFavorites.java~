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

   //Callback definitions
   private DeliciousFavoriteServiceAsync dfSvc = GWT.create(DeliciousFavoriteService.class);

   public void onModuleLoad() {
       flexTable.setText(0,0,"Description");
       flexTable.setText(0,1,"Tags");
       flexTable.setText(0,2,"Shared");
       flexTable.setText(0,3,"Private");
       flexTable.setText(0,4,"Date Added");
       for ( int i=0; i<5; i++ ) {
	  flexTable.getCellFormatter().addStyleName(0,i,"headerCell");
       }
       flexTable.setWidth("100%");
       flexTable.setCellSpacing(0);
        
       flexTable.addClickHandler( new TableClickHandler( flexTable ) );
       getFavoritesList();

       Button nextButton = new Button( "Next 30 >>", new NextButtonClickHandler( flexTable ) ); 
       flexTable.setWidget(31,0,nextButton);
       flexTable.getCellFormatter().addStyleName(31,0,"headerCell");
       flexTable.getFlexCellFormatter().setColSpan(31,0,5);       

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
      dfSvc.getFavorites( "time", "desc", callback );
   } 
}