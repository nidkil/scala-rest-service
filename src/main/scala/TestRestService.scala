package restservice

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.{ HashMap => JHashMap, Map => JMap }
import scala.collection.JavaConversions._
import scala.xml.NodeSeq
import scala.xml.Elem
import scala.xml.PrettyPrinter

class TestRestService extends HttpServlet {

  val release = "0.2.0"

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    response.setContentType("text/html")
    response.setCharacterEncoding("UTF-8")

    prettyPrintResponse(request, response, "doGet")
  }

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {
    response.setContentType("text/html")
    response.setCharacterEncoding("UTF-8")

    prettyPrintResponse(request, response, "doPost")
  }

  override def doDelete(request: HttpServletRequest, response: HttpServletResponse) {
    response.setContentType("text/html")
    response.setCharacterEncoding("UTF-8")

    prettyPrintResponse(request, response, "doDelete")
  }

  def prettyPrintResponse(request: HttpServletRequest, response: HttpServletResponse, callType: String) {
    val responseBody: NodeSeq =
      <html>
        <title>{ callType } [TestRestService version {release}]</title>
        <body>
          <h1>{ callType } method called</h1>
          parameters:<br/>{ parameters(request) }<br/><br/>
          headers:<br/>{ headers(request) }
        </body>
      </html>

    val printer = new PrettyPrinter(80, 2)

    response.getWriter.write(printer.formatNodes(responseBody))
  }

  def makeValues(values: Array[String]): String = {
    var s = ""
    for (v: String <- values) if (s.length() == 0) s += s"$v" else s += s" $v"
    s
  }

  def parameters(request: HttpServletRequest): Elem = {
    val result =
      <div>
        {
          for ((k: String, v: Array[String]) <- request.getParameterMap()) yield 
          	<pre>    {k} = {makeValues(v)}</pre>
        }
      </div>
    result
  }

  def headers(request: HttpServletRequest): Elem = {
    val result =
      <div>
        {
          for ((k: String) <- request.getHeaderNames.asInstanceOf[java.util.Enumeration[String]]) yield 
          	<pre>    {k} = {request.getHeader(k)}</pre>
        }
      </div>
    result
  }

}
