package com.mktneutral.client;

public class DeliciousFavoriteQuery {
   private int startRow;
   private String sortColumn;
   private String sortDirection;

   DeliciousFavoriteQuery( int _startRow, String _sortColumn, String _sortDirection ) {
      startRow = _startRow;
      sortColumn = _sortColumn;
      sortDirection = _sortDirection;
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

   public void setStartRow( int _startRow ) {
       startRow = _startRow;
   } 

   public void setSortColumn( String _sortColumn ) {
       sortColumn = _sortColumn;
   } 
 
   public void setSortDirection( String _sortDirection ) {
       sortDirection = _sortDirection;
   }
}