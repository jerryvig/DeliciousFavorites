package com.mktneutral.server;

import java.util.ArrayList;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import com.mktneutral.client.DeliciousFavorite;
import com.mktneutral.client.DeliciousFavoriteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliciousFavoriteServiceImpl extends RemoteServiceServlet implements DeliciousFavoriteService {
   private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");   
   private SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yyyy");

   public ArrayList<DeliciousFavorite> getFavorites( String sortColumn, String sortDirection, int startRow ) {
      try {
	 Class.forName("org.h2.Driver");
      } catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }

      Connection conn = null;
      Statement stmt = null;
      try {
	  conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/mem:delicious","sa","");
         stmt = conn.createStatement();
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      ArrayList<DeliciousFavorite> favoritesList = new ArrayList<DeliciousFavorite>();

      try {
        ResultSet rs = stmt.executeQuery("SELECT * FROM posts ORDER BY time DESC");
        while ( rs.next() ) {
	   String desc = rs.getString(2);
           String href = rs.getString(5);
           String tag = rs.getString(8);
           boolean shared = rs.getBoolean(7);
           boolean pvt = rs.getBoolean(6);
           String sharedString = "";
           String pvtString = "";
           if ( shared ) {
	      sharedString = "yes";
           }
           else {
              sharedString = "no";
           }
           if ( pvt ) {
	       pvtString = "yes";
           }
           else {
               pvtString = "no";
           }

           Date time = new Date( rs.getDate(9).getTime() );
           favoritesList.add( new DeliciousFavorite( desc, href, tag, sharedString, pvtString, dateFmt.format(time) ) );
        }
      } catch ( SQLException sqle ) { sqle.printStackTrace(); }

      return favoritesList;
   }

   public ArrayList<DeliciousFavorite> getFavoritesMongo( String sortColumn, String sortDirection, int startRow ) {
       DB db = null;
       try {
          db = (new Mongo("localhost")).getDB("delicious");
       } catch ( UnknownHostException uhe ) { uhe.printStackTrace(); }

       ArrayList<DeliciousFavorite> favoritesList = new ArrayList();
      
       BasicDBObject sortObj = new BasicDBObject();
       BasicDBObject gtObj = new BasicDBObject();
       gtObj.put("$gt","0");
       BasicDBObject findObj = new BasicDBObject();

       int sortDirectionInt = 1;
       if ( sortDirection.equals("desc") ) sortDirectionInt = -1;

       if ( sortColumn.equals("description") ) {
          sortObj.put("description",sortDirectionInt);
          findObj.put("description",gtObj);          
       }
       else if ( sortColumn.equals("tags") ) {
	  sortObj.put("tag",sortDirectionInt);
          findObj.put("tag",gtObj); 
       }
       else if ( sortColumn.equals("shared") ) {
	  sortObj.put("shared",sortDirectionInt);
          findObj.put("shared",gtObj);
       }
       else if ( sortColumn.equals("private") ) {
          sortObj.put("private",sortDirectionInt);
          findObj.put("private",gtObj);
       }
       else if ( sortColumn.equals("time") ) {
          sortObj.put("time",sortDirectionInt);
       }
      
       DBCursor cur = db.getCollection("posts").find( findObj ).sort( sortObj ).skip(startRow).limit(30);

       while ( cur.hasNext() ) {
	  DBObject nextObj = cur.next();
          String dateString = nextObj.get("time").toString().substring(0,9);
          try {
            Date dateValue = dateFormatter.parse( dateString );
            String outDateString = dateFmt.format( dateValue );
            favoritesList.add( new DeliciousFavorite( nextObj.get("description").toString(), nextObj.get("href").toString(), nextObj.get("tag").toString(), nextObj.get("shared").toString(), nextObj.get("private").toString(), outDateString ) );
	  } catch ( ParseException pe ) {}
       }

       return favoritesList;
   } 
}