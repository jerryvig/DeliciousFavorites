import org.dom4j.io.*
import com.mongodb.*

def db = (new Mongo("localhost")).getDB("delicious")
db.getCollection("posts").drop()
def postsColl = db.createCollection("posts",new BasicDBObject())
def postCount = 0

def document = (new SAXReader()).read( "file:///tmp/all.xml" )
document.selectNodes("//post").each { post ->
  def postObj = new BasicDBObject()
  post.selectNodes("@*").each { attr ->
    def attrName = attr.getName()
    def attrValue = attr.getText()
    postObj.put( attrName, attrValue )
  }
  postsColl.insert( postObj )
  postCount++
  // println postCount
}
