import javax.net.ssl.HttpsURLConnection
import java.net.*
import java.io.*
import org.dom4j.io.*
import com.mongodb.*

Authenticator.setDefault(new Authenticator() {
  PasswordAuthentication getPasswordAuthentication() {
   def username = "agentq314"
   def pass = "dk87nup4841"
   return new PasswordAuthentication(username,pass.toCharArray())
  }
});

def url = new URL("https://agentq314:dk87nup4841@api.del.icio.us/v1/posts/all")
def conn = (HttpsURLConnection)url.openConnection()

def db = (new Mongo("localhost")).getDB("delicious")
db.getCollection("posts").drop()
def postsColl = db.createCollection("posts",new BasicDBObject())
def document = (new SAXReader()).read(conn.getInputStream())
document.selectNodes("//post").each { post ->
  def postObj = new BasicDBObject()
  post.selectNodes("@*").each { attr ->
    postObj.put( attr.getName(), attr.getText() )
  }
  postsColl.insert( postObj )
}