import org.dom4j.Node;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeliciousPostProcessor implements Runnable {
   private Node post;
   private static PreparedStatement iStmt;
   private static PreparedStatement virtStmt;

   DeliciousPostProcessor( Node _post, PreparedStatement _iStmt, PreparedStatement _virtStmt ) {
      iStmt = _iStmt;
      virtStmt = _virtStmt;
      post = _post;     
   }

   public void run() {
      List<Node> attributes = (List<Node>)(this.post.selectNodes("@*"));

      String description = "";
      String href = "";
      String extended = "";
      String hash = "";
      String tag = "";
      int shared = 0;
      int pvt = 0;
      String time = "";

      for ( Node attribute : attributes ) {
	   String attributeName = attribute.getName();
	   if ( attributeName.equals("description") ) {
	       description = attribute.getText();
	       description = description.replaceAll("'","");
	   }
	   if ( attributeName.equals("href") ) {
	       href = attribute.getText();
	   }
	   if ( attributeName.equals("extended") ) {
	       extended = attribute.getText();
	       extended = extended.replaceAll("'","");
	   }
	   if ( attributeName.equals("hash") ) {
	       hash = attribute.getText();
	   }
	   if ( attributeName.equals("tag") ) {
	       tag = attribute.getText();
	       tag = tag.replaceAll("'","");
	   }
	   if ( attributeName.equals("shared") ) {
	       if ( attribute.getText().equals("yes") ) {
		   shared = 1;
	       }
	   }
	   if ( attributeName.equals("private") ) {
	       if ( attribute.getText().equals("yes") ) {
		   pvt = 1;
	       }
	   }
	   if ( attributeName.equals("time") ) {
	       time = attribute.getText();
	   }
       }

       try {
         iStmt.setString(1,description);
	 iStmt.setString(2,extended);
	 iStmt.setString(3,hash);
	 iStmt.setString(4,href);
	 iStmt.setInt(5,pvt);
	 iStmt.setInt(6,shared);
	 iStmt.setString(7,tag);
	 iStmt.setString(8,time.substring(0,10));
	 iStmt.addBatch();

         virtStmt.setString(1,description);
	 virtStmt.setString(2,extended);
	 virtStmt.setString(3,href);
	 virtStmt.setString(4,tag);
	 virtStmt.addBatch();
       } catch ( SQLException sqle ) { sqle.printStackTrace(); }
   }
}