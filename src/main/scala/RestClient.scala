import org.apache.http._
import org.apache.http.client.entity._
import org.apache.http.client.methods._
import org.apache.http.impl.client._
import org.apache.http.client.utils._
import org.apache.http.message._
import org.apache.http.params._

object RestClient {

  def main(args: Array[String]) {

    val release = "0.2.0"

    if(args.size < 2) {
      println(s"RestClient version $release\n")
      println("Usage: <action: post, get, delete> <-d: parameters> <-h: headers>> <url>\n")
      println("Parameters and headers are key value pair separated by a comma, example: -h header1=1,header2=2")
      System.exit(2)
    }

    // Parse request parameters and headers
    def parseArgs(args: Array[String]): Map[String, List[String]] = {
      def nameValuePair(paramName: String) = {
        def values(commaSeparatedValues: String) = commaSeparatedValues.split(",").toList
        val index = args.indexOf(paramName)
        (paramName, if (index == -1) Nil else values(args(index + 1)))
      }
      Map(nameValuePair("-d"), nameValuePair("-h"))
    }

    val command = args.head
    val params = parseArgs(args)
    val url = args.last

    command match {
      case "post" => handlePostRequest
      case "get" => handleGetRequest
      case "delete" => handleDeleteRequest
      case "options" => handleOptionsRequest
    }

    def splitByEqual(nameValue: String): Array[String] = nameValue.split('=')

    // Create BasicHeader for each header name/value pair
    def headers = for (nameValue <- params("-h")) yield {
      def tokens = splitByEqual(nameValue)
      new BasicHeader(tokens(0), tokens(1))
    }

    // Create BasicHeader for each header name/value pair
    def formEntity = {
      def toJavaList(scalaList: List[BasicNameValuePair]) = {
        java.util.Arrays.asList(scalaList.toArray: _*)
      }
      def formParams = for (nameValue <- params("-d")) yield {
        def tokens = splitByEqual(nameValue)
        new BasicNameValuePair(tokens(0), tokens(1))
      }
      // Create URL-encoded form entity
      def formEntity = new UrlEncodedFormEntity(toJavaList(formParams), "UTF-8")
      formEntity
    }

    def handlePostRequest = {
      val httppost = new HttpPost(url)
      headers.foreach { httppost.addHeader(_) }
      httppost.setEntity(formEntity)
      val responseBody = new DefaultHttpClient().execute(httppost, new BasicResponseHandler())
      println(responseBody)
    }

    def handleGetRequest = {
      val query = params("-d").mkString("&")
      val httpget = new HttpGet(s"${url}?${query}")
      headers.foreach { httpget.addHeader(_) }
      val responseBody = new DefaultHttpClient().execute(httpget, new BasicResponseHandler())
      println(responseBody)
    }

    def handleDeleteRequest = {
      val httpDelete = new HttpDelete(url)
      val httpResponse = new DefaultHttpClient().execute(httpDelete)
      println(httpResponse.getStatusLine())
    }

    def handleOptionsRequest = {
      val httpOptions = new HttpOptions(url)
      headers.foreach { httpOptions.addHeader(_) }
      val httpResponse = new DefaultHttpClient().execute(httpOptions)
      println(httpOptions.getAllowedMethods(httpResponse))
    }
  }

}
