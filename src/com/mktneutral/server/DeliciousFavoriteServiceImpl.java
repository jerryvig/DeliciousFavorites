package com.mktneutral.server;

import java.util.ArrayList;
import java.net.UnknownHostException;
import com.mktneutral.client.DeliciousFavorite;
import com.mktneutral.client.DeliciousFavoriteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DeliciousFavoriteServiceImpl extends RemoteServiceServlet implements DeliciousFavoriteService {
   
   public ArrayList<DeliciousFavorite> getFavorites( String[] args ) {
       DB db = null;
       try {
          db = (new Mongo("localhost")).getDB("delicious");
       } catch ( UnknownHostException uhe ) { uhe.printStackTrace(); }

       ArrayList<DeliciousFavorite> favoritesList = new ArrayList();
       DBCursor cur = db.getCollection("posts").find().limit( 200 );

       while ( cur.hasNext() ) {
	  DBObject nextObj = cur.next();
          favoritesList.add( new DeliciousFavorite( nextObj.get("description").toString(), nextObj.get("href").toString(), nextObj.get("tag").toString(), nextObj.get("shared").toString(), nextObj.get("private").toString(), nextObj.get("time").toString() ) );                           
       }
       return favoritesList;
   } 
}