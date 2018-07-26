package cermine.service.utils

import scala.util.{Try, Success, Failure}
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.xpath.XPath;
import scala.collection.JavaConverters._


object Xml {

  def getNodeChildren(node: Element): List[Element] =
    node.getChildren.asScala
      .toList.asInstanceOf[List[Element]]

  def selectNodeChildren(node: Element, xpath: String): List[Element] =
    node.getChildren(xpath)
      .asScala
      .toList.asInstanceOf[List[Element]]

  def selectNodes(nlm: Document, xpath: String): List[Element] =
     XPath.newInstance(xpath)
      .selectNodes(nlm)
      .asScala
      .toList
       .asInstanceOf[List[Element]]

  def xPathOptionValue(nlm: Document, xpath: String): Option[String] =
    Option(XPath.newInstance(xpath).valueOf(nlm))
      .map((s: String) => s.trim)

  def xPathValue(nlm: Document, xpath: String): String =
    xPathOptionValue(nlm, xpath) match {
      case Some(s) => s
      case None => ""
    }


  def extractDateValue(nlm: Document, xpath: String): Option[String] = {
    val pubYear = xPathValue(nlm, xpath + "/year")
    val pubMonth = xPathValue(nlm, xpath + "/month")
    val pubDay = xPathValue(nlm, xpath + "/day")

    if (pubYear.isEmpty && pubMonth.isEmpty && pubDay.isEmpty)
      None
    else Some(
      List(pubYear, pubMonth, pubDay)
        .filter(false == _.isEmpty) mkString "-"
    )

  }

}
