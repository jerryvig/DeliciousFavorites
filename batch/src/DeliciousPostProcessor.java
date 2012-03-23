import org.dom4j.Node;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeliciousPostProcessor implements Runnable {
   private String postString;
   private static PreparedStatement iStmt;
   private static PreparedStatement virtStmt;

   DeliciousPostProcessor( String _postString, PreparedStatement _iStmt, PreparedStatement _virtStmt ) {
      iStmt = _iStmt;
      virtStmt = _virtStmt;
      postString = _postString;     
   }

   public void run() {
       //String description = "";
       //String href = "";
       //String extended = "";
       //String hash = "";
       //String tag = "";
      int shared = 0;
      int pvt = 0;
      //String time = "";

      try {
      String[] pieces = this.postString.split("=\"");
      for ( int i=0; i<pieces.length; i++ ) {
	 if ( pieces[i].endsWith("description") ) {
	    iStmt.setString(1,pieces[i+1].split("\"")[0]);
            virtStmt.setString(1,pieces[i+1].split("\"")[0]);
         }
         else if ( pieces[i].endsWith("href") ) {
	    iStmt.setString(4,pieces[i+1].split("\"")[0]);
            virtStmt.setString(3,pieces[i+1].split("\"")[0]);
         }
         else if ( pieces[i].endsWith("extended") ) {
	   iStmt.setString(2,pieces[i+1].split("\"")[0]);
           virtStmt.setString(2,pieces[i+1].split("\"")[0]);
         }
         else if ( pieces[i].endsWith("hash") ) {
	     iStmt.setString(3,pieces[i+1].split("\"")[0]);
         }
         else if ( pieces[i].endsWith("tag") ) {
	     iStmt.setString(7,pieces[i+1].split("\"")[0]);
             virtStmt.setString(4,pieces[i+1].split("\"")[0]);
         }
         else if ( pieces[i].endsWith("shared") ) {
	   if ( pieces[i+1].split("\"")[0].equals("yes") ) shared = 1;
         }
         else if ( pieces[i].endsWith("private") ) {
	   if ( pieces[i+1].split("\"")[0].equals("yes") ) pvt = 1;
         }
         else if ( pieces[i].endsWith("time") ) {
	   iStmt.setString(8,pieces[i+1].split("\"")[0]);
         }
      }

      iStmt.setInt(5,pvt);
      iStmt.setInt(6,shared);
      iStmt.addBatch();
      virtStmt.addBatch();
     } catch ( SQLException sqle ) { sqle.printStackTrace(); }
   }
}