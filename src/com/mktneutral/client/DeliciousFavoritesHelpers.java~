package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Anchor;

public class DeliciousFavoritesHelpers {
   public static void fillFlexTable( FlexTable flexTable, ArrayList<DeliciousFavorite> favsList ) {
      int i = 2;
      for ( DeliciousFavorite fav : favsList ) {
          Anchor href = new Anchor();
          href.setTarget("_blank");
          href.setHref( fav.getHref() );
          href.setText( fav.getDescription() );
          flexTable.setWidget( i, 0, href );
          flexTable.setText( i, 1, fav.getTag() );
          flexTable.setText( i, 2, fav.getShared() );
          flexTable.setText( i, 3, fav.getPrivate() );
          flexTable.setText( i, 4, fav.getTime() );
          for ( int j=0; j<5; j++ ) {
              if ( i%2 == 0 ) {
		   flexTable.getCellFormatter().addStyleName(i,j,"evenRow");
              }
              else {
		   flexTable.getCellFormatter().addStyleName(i,j,"oddRow");
              }
          }
          i++;
      }
   }
}