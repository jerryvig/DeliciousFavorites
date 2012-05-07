package com.mktneutral.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class ButtonClickHandler implements ClickHandler {
   private FlexTable flexTable;
   private DeliciousFavoriteServiceAsync dfSvc;
   private DeliciousFavoriteQuery query;
   
   public void onClick( ClickEvent event ) {
   }
    
   public void drawNextPreviousButtons() {
     if ( query.getStartRow() > 0 ) {
	 Button prevButton = new Button("<< Prev 30", new PrevButtonClickHandler( flexTable, query ));
         Button nextButton = new Button("Next 30 >>", new NextButtonClickHandler( flexTable, query ));

         prevButton.addStyleName("deliButton");
         nextButton.addStyleName("deliButton");
         FlowPanel flowPanel = new FlowPanel();
         flowPanel.add( prevButton );
         flowPanel.add( nextButton );

         flexTable.getFlexCellFormatter().setStyleName(33,0,"footerCell");
         flexTable.getFlexCellFormatter().setHorizontalAlignment(33,0,HasHorizontalAlignment.ALIGN_CENTER);
         flexTable.setWidget(33,0, flowPanel );           
     }
     else {
         Button nextButton = new Button("Next 30 >>", new NextButtonClickHandler( flexTable, query ));
         nextButton.addStyleName("deliButton");
         FlowPanel flowPanel = new FlowPanel();
         flowPanel.add( nextButton );

         flexTable.getFlexCellFormatter().setStyleName(33,0,"footerCell");
         flexTable.getFlexCellFormatter().setHorizontalAlignment(33,0,HasHorizontalAlignment.ALIGN_CENTER);
         flexTable.setWidget(33,0, flowPanel ); 
     }
   }
}