import java.util.List;
import java.util.LinkedList;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
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
	    conn = DriverManager.getConnection("jdbc:sqlite:/mnt/ramdisk/delicious.db");
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
            final LinkedList<Callable<Object>> nodeTasks = new LinkedList<Callable<Object>>();

            XMLInputFactory xmlif = XMLInputFactory.newInstance();
	    URL url = new URL("https://agentq314:dk87nup4841@api.del.icio.us/v1/posts/all");
	    HttpsURLConnection urlConn = (HttpsURLConnection)(url.openConnection());
	    // Document doc = (new SAXReader()).read( urlConn.getInputStream() );
            //  List<Node> posts = (List<Node>)doc.selectNodes("//post");
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String content = "";
            String s;
            while( (s=reader.readLine())!=null ) {
		content += s;
            }
            reader.close();
            
            int[] startIndices = new int[1001];
            int[] endIndices = new int[1001];
            int postCount = 0;            
            String postString = "";
            boolean addToPostString = false;

            for ( int i=0; i<content.length()-5; i++ ) {
	       if ( content.substring(i,i+5).equals("<post") ) {
		   startIndices[postCount] = i;
                   addToPostString = true;
               }
               if ( content.substring(i,i+2).equals("/>") ) {
                   endIndices[postCount] = i+2;
                   String postStr = content.substring(startIndices[postCount],endIndices[postCount]);
                   nodeTasks.add(Executors.callable(new DeliciousPostProcessor(postStr, iStmt, virtStmt)));
                   addToPostString = false;
                   postString = "";
                   postCount++;
               }
               if ( addToPostString ) {
		   postString += content.substring(i,i+1);
	       }
            }

            //System.out.println( content );
	    /*
            for ( Node post : posts ) {
                nodeTasks.add(Executors.callable(new DeliciousPostProcessor(post, iStmt, virtStmt)));
		} */
       
            try {
	       execSvc.invokeAll( nodeTasks );
               execSvc.shutdown();
	    } catch ( InterruptedException ie ) { ie.printStackTrace(); }

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
	// catch ( DocumentException doce ) { doce.printStackTrace(); }
        //catch ( XMLStreamException e ) { e.printStackTrace(); }
    }
 
    public static void stopDB() {
      try {
	  conn.close();
      } catch ( SQLException sqle ) {
	  sqle.printStackTrace(); 
      }
    }
}