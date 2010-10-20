import scala.io.Source
import scala.xml.XML
import scala.xml.Elem
import scala.List

import java.net.URLEncoder

class Reflexa() {
    val url = "http://labs.preferred.jp/reflexa/api.php"

    /*
     * reflexa apiへアクセス
     */
    def getXMLSource(query: String) = {
        val src = Source.fromURL(url + "?q=" + query + "&format=xml")
        XML.loadString(src.mkString)
    }

    /*
     * 検索結果のxmlをパースし、結果の配列を作成
     */
    def makeWordList(query: String) = {
        val xml = getXMLSource(query)
        val words = xml \\ "word"
        words size match {
            case 0 => Array()
            case _ => words.map(word => word.text).toArray
        }
    }

    /* 
     * 検索文字列からqueryを作成
     * 文字列同士は「%20」で連結
     */
    def makeQuery(search_words: Array[String]) = {
        search_words.map(URLEncoder.encode).mkString("%20")
    }
    
    def search(search_words: Array[String]) = {
        val query = makeQuery(search_words)
        makeWordList(query)
    }
}

object reflexa {
    def main(arg: Array[String]) = {
        val Ref = new Reflexa()
        val line = Console.readLine(">>> ")
        val split_reg = """[\s　]""".r
        val list = Ref.search(split_reg.split(line))
        list.map(println)
    }
}

// vim: set ts=4 sw=4 et:
