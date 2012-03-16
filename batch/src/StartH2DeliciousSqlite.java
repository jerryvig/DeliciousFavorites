import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.net.ssl.HttpsURLConnection;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.MalformedURLException;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

public class StartH2DeliciousSqlite {
    private static Connection conn;
    private static Statement stmt;
    private static PreparedStatement prepStmt;

    public static void main( String[] args ) {
       startDB();
       deliciousXML2H2();
       stopDB();
    }

    public static void startDB() {
	try {
	    Class.forName("org.sqlite.JDBC");
	} catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }

	try {
	    conn = DriverManager.getConnection("jdbc:sqlite:/mnt/ramdisk/delicious.db");
	    stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS posts");
	    stmt.executeUpdate("CREATE TABLE posts ( description TEXT, extended TEXT, hash TEXT, href TEXT, private INTEGER, shared INTEGER, tag TEXT, time TEXT )");
	} catch ( SQLException sqle ) { sqle.printStackTrace(); }
    }

    public static void deliciousXML2H2() {
	try {
	    Authenticator.setDefault(new Authenticator() {
		    public PasswordAuthentication getPasswordAuthentication() {
			String username = "agentq314";
			String password = "dk87nup4841";
			return new PasswordAuthentication(username,password.toCharArray());
		    }
	    });

	    URL url = new URL("https://agentq314:dk87nup4841@api.del.icio.us/v1/posts/all");
	    HttpsURLConnection urlConn = (HttpsURLConnection)(url.openConnection());
	    Document doc = (new SAXReader()).read( urlConn.getInputStream() );
	    List<Node> posts = (List<Node>)doc.selectNodes("//post");
            try {          
              prepStmt = conn.prepareStatement("INSERT INTO posts VALUES (?,?,?,?,?,?,?,?)"); 
            } catch ( SQLException sqle ) { sqle.printStackTrace(); }

	    for ( Node post : posts ) {
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
       
	    try {
                conn.setAutoCommit(false);
                prepStmt.executeBatch();
                conn.setAutoCommit(true);

		stmt.executeUpdate("CREATE INDEX posts_tag_idx ON posts (tag)");
		stmt.executeUpdate("CREATE INDEX posts_time_idx ON posts (time)");
                stmt.executeUpdate("CREATE INDEX posts_desc_idx ON posts (description)");
                stmt.executeUpdate("CREATE INDEX posts_ext_idx ON posts (extended)");
                stmt.executeUpdate("CREATE INDEX posts_private_idx ON posts (private)");
                stmt.executeUpdate("CREATE INDEX posts_shared_idx ON posts (shared)");
	    } catch ( SQLException sqle ) { sqle.printStackTrace(); }
	} catch ( IOException ioe ) { ioe.printStackTrace(); }
	catch ( DocumentException doce ) { doce.printStackTrace(); }
    }
 
    public static void stopDB() {
      try {
	  conn.close();
      } catch ( SQLException sqle ) {
	  sqle.printStackTrace(); 
      }
    }
}