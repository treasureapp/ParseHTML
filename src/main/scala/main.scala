/**
  * Created by Dust on 6/24/2017.
  */

//import java.net.URL
//import scala.xml.XML
import org.xml.sax.InputSource
import scala.xml.parsing.NoBindingFactoryAdapter
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.net.HttpURLConnection
import scala.xml.Node
import scala.collection.JavaConverters._
import java.net.URL
import java.io._

object main {
  def main(args: Array[String]) {

    val stockIds = List(8244,2222)

    val stockID = 8244

    def parseHTML(stock: Int): List[List[Any]] = {

      val site = new URL(s"http://fundamentals.nasdaq.com/nasdaq_fundamentals.asp?CompanyID=$stock&NumPeriods=40&Duration=1&documentType=1")
      val content = HTML.load(site)

      val PeriodsDateHTML = content \\ "td" filter(td=>(td\"@class" toString) == "dkbluert" )

      val FinancialNumbers = content \\ "td" filter(td=>(td\"@class" toString) == "fundnum")

      val y = PeriodsDateHTML.text.split('\n').map(_.trim)

      val z = FinancialNumbers.text.split('\n').map(_.trim)

      val Periods = y.toList.filter(x => x.nonEmpty && x.length <= 3)

      val QuarterDates = y.filter(_.length>3).mkString.split("""(?=\/\d{4})""")toList

      val IncomeStatement = z.toList.filter(_.nonEmpty).grouped(QuarterDates.size)toList

      val table = List(Periods,QuarterDates,List.fill(QuarterDates.length) (stock)) ::: IncomeStatement

      if (table.size >= 1) table else null

    }




    def addList(stocks: List[Int]): List[List[Any]] = {

      val mainTable =  List[List[Any]]()

      //stocks.map(_ => parseHTML(_))
      ???

      //for every stock
      //go through parseHTML
      //add to mainTable
    }

//    val tester = parseHTML(stockID).transpose
//    //tester.foreach(println)
//
//    val tester2 = parseHTML(2222).transpose
//    //tester2.foreach(println)
//
//    val tester3 = tester ::: tester2
//    tester3.foreach(println)

    //val titles = List("Period","Quarter End","Total Revenue","Cost of Revenue","Gross Profit","Operating Expenses - R&D","Operating Expenses - SGA","Operating Income"
    // ,"Additional Income/Expense Items","Earnings Before Interest and Tax","Earnings Expense","Earnings Before Tax"," Income Tax","Net Income-Cont. Operations"," Discontinued Operations"
    // ,"Net Income", "Net Income Application to Common Shareholders")

    val site = new URL(s"http://fundamentals.nasdaq.com/nasdaq_fundamentals.asp?CompanyID=$stockID&NumPeriods=40&Duration=1&documentType=1")

    val content = HTML.load(site) //make HTML to XML

    val a = content \\ "td" filter(
      td=>
        ((td\"@class" toString) == "body1" && (td\"@height" toString) == "20") ||
          ((td\"@class" toString) == "indent" && (td\"@height" toString) == "20") ||
        ((td\"@class" toString) == "fundnum" && (td\"@width" toString) == "80"))


    //println(a.toString)

    val d = a.text.split("(?<=\\d)(?=[A-Z])|\\n").map(_.trim).filter(_.nonEmpty).toList

    val check = d.grouped(41)

    check.foreach(println)

    //table.foreach(println)

//    def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
//      val p = new java.io.PrintWriter(f)
//      try { op(p) } finally { p.close() }
//    }
//
//    val data = table.transpose
//    printToFile(new File("example.txt")) {
//      p => data.foreach(p.println)
//    }


  }
}

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