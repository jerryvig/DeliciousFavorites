package com.mktneutral.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DeliciousFavoriteQuery implements IsSerializable {
   private int startRow;
   private String sortColumn;
   private String sortDirection;
   private String queryType;

   DeliciousFavoriteQuery() {}

   DeliciousFavoriteQuery( int _startRow, String _sortColumn, String _sortDirection, String _queryType ) {
      startRow = _startRow;
      sortColumn = _sortColumn;
      sortDirection = _sortDirection;
      queryType = _queryType;
   }

   public int getStartRow() {
       return startRow;
   } 

   public String getSortColumn() {
       return sortColumn;
   }

   public String getSortDirection() {
       return sortDirection;
   }

   public String getQueryType() {
       return queryType;
   }

   public void setStartRow( int _startRow ) {
       startRow = _startRow;
   } 

   public void setSortColumn( String _sortColumn ) {
       sortColumn = _sortColumn;
   } 
 
   public void setSortDirection( String _sortDirection ) {
       sortDirection = _sortDirection;
   }

   public void setQueryType( String _queryType ) {
       queryType = _queryType;
   }
}