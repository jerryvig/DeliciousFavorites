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
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.InputStreamReader;

public class StartH2DeliciousSqlite {
    private static Connection conn;
    private static Statement stmt;
    private static PreparedStatement virtStmt;
    private static PreparedStatement iStmt;

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
	    conn = DriverManager.getConnection("jdbc:sqlite:/mnt/BigDrive/gwt-testing/DeliciousFavorites/delicious.db");
	    stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS posts");
	    stmt.executeUpdate("CREATE TABLE posts (description TEXT,extended TEXT,hash TEXT,href TEXT,private INTEGER,shared INTEGER,tag TEXT,time TEXT)");
            stmt.executeUpdate("DROP TABLE IF EXISTS posts_fts3");
            stmt.executeUpdate("CREATE VIRTUAL TABLE posts_fts3 USING fts3( description TEXT, extended TEXT, href TEXT, tag TEXT )");
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

            try {
              iStmt = conn.prepareStatement("INSERT INTO posts VALUES(?,?,?,?,?,?,?,?)"); 
            } catch ( SQLException sqle ) { sqle.printStackTrace(); }
            try {
		virtStmt = conn.prepareStatement("INSERT INTO posts_fts3 VALUES(?,?,?,?)");
            } catch ( SQLException sqle ) { sqle.printStackTrace(); }

            final ExecutorService execSvc = Executors.newFixedThreadPool(4);

	    URL url = new URL("https://agentq314:dk87nup4841@api.del.icio.us/v1/posts/all");
	    HttpsURLConnection urlConn = (HttpsURLConnection)(url.openConnection());
            InputStreamReader reader = new InputStreamReader(urlConn.getInputStream());
            char[] buffer5 = new char[1];
            char[] buffer2 = new char[1];
            
            int s = 0;
            while( (s=reader.read(buffer5,0,1)) != -1 ) {
	      if ( buffer5[0] == '<' ) {
		 String postString = new String( buffer5 ); 
		 while ( (s=reader.read(buffer2,0,1)) != -1 ) {
                     postString = postString.concat( new String(buffer2) );
		     if ( buffer2[0] == '>' ) {
			execSvc.execute(new DeliciousPostProcessor(postString, iStmt, virtStmt));
                        postString = "";
                        break; 
                     }
                 }
	      }
            }
            reader.close();
           
	    execSvc.shutdown();

	    try {
               conn.setAutoCommit(false);
               iStmt.executeBatch();
               virtStmt.executeBatch();
               conn.setAutoCommit(true);

	       stmt.executeUpdate("CREATE INDEX posts_tag_idx ON posts(tag)");
	       stmt.executeUpdate("CREATE INDEX posts_time_idx ON posts(time)");
               stmt.executeUpdate("CREATE INDEX posts_desc_idx ON posts(description)");
               stmt.executeUpdate("CREATE INDEX posts_ext_idx ON posts(extended)");
               stmt.executeUpdate("CREATE INDEX posts_private_idx ON posts(private)");
               stmt.executeUpdate("CREATE INDEX posts_shared_idx ON posts(shared)");
	    } catch ( SQLException sqle ) { sqle.printStackTrace(); }
	} catch ( IOException ioe ) { ioe.printStackTrace(); }
    }
 
    public static void stopDB() {
      try {
	  conn.close();
      } catch ( SQLException sqle ) {
	  sqle.printStackTrace(); 
      }
    }
}