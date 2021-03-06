package com.mktneutral.client;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.InlineHTML;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class DeliciousFavorites implements EntryPoint {
   //Widget definitions
   private VerticalPanel vertPanel = new VerticalPanel();
   private FlexTable flexTable = new FlexTable();
   private DeliciousFavoriteQuery query = new DeliciousFavoriteQuery( 0, "time", "desc", "list" );

   //Callback definitions
   private DeliciousFavoriteServiceAsync dfSvc = GWT.create(DeliciousFavoriteService.class);

   public void onModuleLoad() {
       InlineHTML iHtml = new InlineHTML("<strong style='font-size:18pt;'>Jerry's Delicious Bookmarks</strong>");
       flexTable.getCellFormatter().addStyleName(0,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(0,0,5);
       flexTable.setWidget(0,0,iHtml);

       flexTable.getCellFormatter().addStyleName(1,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(1,0,5);
       flexTable.getFlexCellFormatter().setHorizontalAlignment(1,0,HasHorizontalAlignment.ALIGN_CENTER);
 
       TextBox searchTextBox = new TextBox();
       searchTextBox.setVisibleLength(40);
       searchTextBox.setMaxLength(80);
       searchTextBox.addStyleName("searchTextBox");
       searchTextBox.addKeyDownHandler( new EnterKeyDownHandler( flexTable, searchTextBox, query ) );

       FlowPanel searchPanel = new FlowPanel();
       searchPanel.add( searchTextBox );
       Button searchButton = new Button( "Search", new SearchButtonClickHandler( flexTable, searchTextBox, query ) );
       searchButton.setStyleName("deliButton");
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
        
       flexTable.addClickHandler( new TableClickHandler( flexTable, query ) );
       getFavoritesList();

       Button nextButton = new Button( "Next 30 >>", new NextButtonClickHandler( flexTable, query ) ); 
       nextButton.setStyleName("deliButton");
       flexTable.setWidget(33,0,nextButton);

       flexTable.getCellFormatter().addStyleName(33,0,"footerCell");
       flexTable.getFlexCellFormatter().setColSpan(33,0,5);
       flexTable.getCellFormatter().addStyleName(33,0,"roundedBottom");
       flexTable.getCellFormatter().addStyleName(0,0,"roundedTop");

       vertPanel.add( flexTable );
       vertPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );

       FlowPanel copyPanel = new FlowPanel();
       copyPanel.add( new InlineHTML("<br><strong style='color:#007FFF;'>Copyright &copy; 2012 MktNeutral.com</strong><br>") );       
       
       RootPanel.get("mainPanel").add( vertPanel );
       RootPanel.get("mainPanel").add( copyPanel );
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

      dfSvc.getFavorites( query, callback );
      //dfSvc.getFavorites( "time", "desc", 0, callback );
   } 
}