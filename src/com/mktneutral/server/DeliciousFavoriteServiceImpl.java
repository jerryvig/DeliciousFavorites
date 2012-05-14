package com.mktneutral.server;

import java.util.ArrayList;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import com.mktneutral.client.DeliciousFavorite;
import com.mktneutral.client.DeliciousFavoriteService;
import com.mktneutral.client.DeliciousFavoriteQuery;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliciousFavoriteServiceImpl extends RemoteServiceServlet implements DeliciousFavoriteService {
   private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");   
   private SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yy");

   public ArrayList<DeliciousFavorite> getFavorites( DeliciousFavoriteQuery _query ) {
      try {
	 Class.forName("org.sqlite.JDBC");
      } catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }

      Connection conn = null;
      Statement stmt = null;
      try {
	 conn = DriverManager.getConnection("jdbc:sqlite:webapps/DeliciousFavorites/WEB-INF/delicious.db");
         stmt = conn.createStatement();
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      String sortColumn = _query.getSortColumn();
      String sortDirection = _query.getSortDirection();
      String searchQuery = _query.getQueryString();
      int startRow = _query.getStartRow();

      String orderBy = "time";
      if ( sortColumn.equals("description") ) {
	  orderBy = "description";
      }
      else if ( sortColumn.equals("tags") ) {
          orderBy = "tag";
      }
      else if ( sortColumn.equals("shared") ) {
          orderBy = "shared";
      }
      else if ( sortColumn.equals("private") ) {
          orderBy = "private";
      }
      else if ( sortColumn.equals("time") ) {
          orderBy = "time";
      }

      ArrayList<DeliciousFavorite> favoritesList = new ArrayList<DeliciousFavorite>();
 
      try {
        ResultSet rs = null;

	if ( _query.getQueryType().equals("list") ) {
           rs = stmt.executeQuery("SELECT * FROM posts ORDER BY "+orderBy+" "+sortDirection.toUpperCase()+" LIMIT 30 OFFSET "+Integer.toString(startRow));

           while ( rs.next() ) {
	   String desc = rs.getString(1);
           String href = rs.getString(4);
           String tag = rs.getString(7);
           int shared = rs.getInt(6);
           int pvt = rs.getInt(5);
           String sharedString = "";
           String pvtString = "";
           if ( shared == 1 ) {
	      sharedString = "yes";
           }
           else {
              sharedString = "no";
           }
           if ( pvt == 1 ) {
	       pvtString = "yes";
           }
           else {
               pvtString = "no";
           }

           //Date time = new Date( rs.getDate(9).getTime() );
           try {
             Date time = dateFormatter.parse(rs.getString(8));
             favoritesList.add( new DeliciousFavorite( desc, href, tag, sharedString, pvtString, dateFmt.format(time) ) );
	   } catch ( ParseException pe ) { pe.printStackTrace(); }
	   }
        }
        else if ( _query.getQueryType().equals("search") ) {
	  orderBy = "description";
          rs = stmt.executeQuery("SELECT * FROM posts_fts3 WHERE posts_fts3 MATCH '"+searchQuery+"' ORDER BY "+orderBy+" "+sortDirection.toUpperCase()+" LIMIT 30 OFFSET "+Integer.toString(startRow));

          while ( rs.next() ) {
           String desc = rs.getString(1);
           String href = rs.getString(3);
           String tag = rs.getString(4);
           int shared = 0;
           int pvt = 0;
           String sharedString = "";
           String pvtString = "";
           if ( shared == 1 ) {
	      sharedString = "yes";
           }
           else {
              sharedString = "no";
           }
           if ( pvt == 1 ) {
	       pvtString = "yes";
           }
           else {
               pvtString = "no";
           }

           //Date time = new Date( rs.getDate(9).getTime() );
            try {
	       //Date time = dateFormatter.parse(rs.getString(8));
	     Date time = new Date();
             favoritesList.add( new DeliciousFavorite( desc, href, tag, sharedString, pvtString, dateFmt.format(time) ) );
	    } catch ( Exception pe ) { pe.printStackTrace(); }
	  }   

        }       
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      return favoritesList;
   }

    /*
   public ArrayList<DeliciousFavorite> searchFavorites( DeliciousFavoriteQuery _query ) {
      try {
	 Class.forName("org.sqlite.JDBC");
      } catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }

      Connection conn = null;
      Statement stmt = null;
      try {
	 conn = DriverManager.getConnection("jdbc:sqlite:delicious.db");
         stmt = conn.createStatement();
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      String orderBy = "description";
      if ( sortColumn.equals("description") ) {
	  orderBy = "description";
      }
      else if ( sortColumn.equals("tags") ) {
          orderBy = "tag";
      }
      else if ( sortColumn.equals("shared") ) {
          orderBy = "shared";
      }
      else if ( sortColumn.equals("private") ) {
          orderBy = "private";
      }
      else if ( sortColumn.equals("time") ) {
          orderBy = "time";
      }
       
      orderBy = "description";

      ArrayList<DeliciousFavorite> favoritesList = new ArrayList<DeliciousFavorite>();

      try {
	ResultSet rs = stmt.executeQuery("SELECT * FROM posts_fts3 WHERE posts_fts3 MATCH '"+searchQuery+"' ORDER BY "+orderBy+" "+sortDirection.toUpperCase()+" LIMIT 30 OFFSET "+Integer.toString(startRow));

        //ResultSet rs = stmt.executeQuery("SELECT * FROM posts_fts3 WHERE posts_fts3 MATCH '"+searchQuery+"' LIMIT 30");

        while ( rs.next() ) {
           String desc = rs.getString(1);
           String href = rs.getString(3);
           String tag = rs.getString(4);
           int shared = 0;
           int pvt = 0;
           String sharedString = "";
           String pvtString = "";
           if ( shared == 1 ) {
	      sharedString = "yes";
           }
           else {
              sharedString = "no";
           }
           if ( pvt == 1 ) {
	       pvtString = "yes";
           }
           else {
               pvtString = "no";
           }

           //Date time = new Date( rs.getDate(9).getTime() );
           try {
	       //Date time = dateFormatter.parse(rs.getString(8));
	     Date time = new Date();
             favoritesList.add( new DeliciousFavorite( desc, href, tag, sharedString, pvtString, dateFmt.format(time) ) );
	   } catch ( Exception pe ) { pe.printStackTrace(); }
        }
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      return favoritesList;
      } */

}