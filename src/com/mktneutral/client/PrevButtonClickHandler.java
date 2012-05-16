package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class PrevButtonClickHandler implements ClickHandler {
   private FlexTable flexTable;
   private DeliciousFavoriteServiceAsync dfSvc;
   private DeliciousFavoriteQuery query;
   
   PrevButtonClickHandler( FlexTable _ft, DeliciousFavoriteQuery _query ) {
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
    
     query.setStartRow( query.getStartRow()-30 );
    
     drawNextPreviousButtons();

     dfSvc.getFavorites( query, callback );    
   }

   public void drawNextPreviousButtons( ) {
     if ( query.getStartRow() > 0 ) {
	 Button prevButton = new Button("<< Prev 30", new PrevButtonClickHandler( flexTable, query ));
         Button nextButton = new Button("Next 30 >>", new NextButtonClickHandler( flexTable, query ));

         prevButton.setStyleName("deliButton");
         nextButton.setStyleName("deliButton");
         FlowPanel flowPanel = new FlowPanel();
         flowPanel.add( prevButton );
         flowPanel.add( nextButton );

         flexTable.getFlexCellFormatter().setStyleName(33,0,"footerCell");
         flexTable.getFlexCellFormatter().addStyleName(33,0,"roundedBottom");
         flexTable.getFlexCellFormatter().setHorizontalAlignment(33,0,HasHorizontalAlignment.ALIGN_CENTER);
         flexTable.setWidget(33,0, flowPanel );           
     }
     else {
         Button nextButton = new Button("Next 30 >>", new NextButtonClickHandler( flexTable, query ));
         nextButton.setStyleName("deliButton");
         FlowPanel flowPanel = new FlowPanel();
         flowPanel.add( nextButton );

         flexTable.getFlexCellFormatter().setStyleName(33,0,"footerCell");
         flexTable.getFlexCellFormatter().addStyleName(33,0,"roundedBottom");
         flexTable.getFlexCellFormatter().setHorizontalAlignment(33,0,HasHorizontalAlignment.ALIGN_CENTER);
         flexTable.setWidget(33,0, flowPanel ); 
     }
   }
}