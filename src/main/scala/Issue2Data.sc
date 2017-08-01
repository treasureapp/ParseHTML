import org.xml.sax.InputSource
import scala.xml.parsing.NoBindingFactoryAdapter
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.net.HttpURLConnection
import scala.xml.Node
import java.net.URL

object HTML {
  lazy val adapter = new NoBindingFactoryAdapter
  lazy val parser = (new SAXFactoryImpl).newSAXParser

  def load(url: URL, headers: Map[String, String] = Map.empty): Node = {
    val conn = url.openConnection().asInstanceOf[HttpURLConnection]
    for ((k, v) <- headers)
      conn.setRequestProperty(k, v)
    val source = new InputSource(conn.getInputStream)
    adapter.loadXML(source, parser)
  }
}


val site2 = new URL ("http://fundamentals.nasdaq.com/redpage.asp?selected=msft&page=1")
val content = HTML.load(site2)

val parse = content \\ "td" filter (td=>((td\"@class" toString) == "body1" && (td\"@nowrap" toString) == "nowrap")
  || ((td\"@class" toString) == "dkbluemid" && (td\"@nowrap" toString) == "nowrap"))


val a = parse.map(_.text).toList

a.foreach(println)

a(45)//if empty element would be empty


//val values = for {
//  a <- parse
//  if (a \ "@class").text == "body1"
//} yield a.text
//
//


