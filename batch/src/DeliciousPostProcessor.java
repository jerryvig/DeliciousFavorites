import org.dom4j.Node;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DeliciousPostProcessor implements Runnable {
   private static Node post;
   private static PreparedStatement prepStmt;

   DeliciousPostProcessor( Node _post ) {
       post = _post;   
   }

    public void run() {
       List<Node> attributes = (List<Node>)(post.selectNodes("@*"));

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
	       else if ( attribute.getText().equals("no") ) {
		   shared = 0;
	       }
	   }
	   if ( attributeName.equals("private") ) {
	       if ( attribute.getText().equals("yes") ) {
		   pvt = 1;
	       }
	       else if ( attribute.getText().equals("no") ) {
		   pvt = 0;
	       }
	   }
	   if ( attributeName.equals("time") ) {
	       time = attribute.getText();
	   }
       }

       try {
          prepStmt.setString(1,description);
	  prepStmt.setString(2,extended);
	  prepStmt.setString(3,hash);
	  prepStmt.setString(4,href);
	  prepStmt.setInt(5,pvt);
	  prepStmt.setInt(6,shared);
	  prepStmt.setString(7,tag);
	  prepStmt.setString(8,time.substring(0,10));
	  prepStmt.addBatch();

       } catch ( SQLException sqle ) { sqle.printStackTrace(); }
    }
}