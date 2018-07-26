package cermine.service.models

import org.jdom.Document
import org.jdom.Element
import cermine.service.utils.Xml

case class ReferenceMeta(
  title: String,
  journalTitle: String,
  publisher: String,
  publisherLocation: String,
  abstractText: String,

  year: String,
  lPage: String,
  fPage: String,
  volume: String,
  issue: String,

  authors: List[ContributorMeta],
)


object ReferenceMetaXml {

  def extractReferenceMeta(node: Element): ReferenceMeta = {

    val abstractText = Option(node.getValue)
    val title = Option(node.getChildTextTrim("article-title"))
    val publisher = Option(node.getChildTextTrim("publisher-name"))
    val publisherLocation = Option(node.getChildTextTrim("publisher-loc"))
    val year = Option(node.getChildTextTrim("year"))

    val lPage = Option(node.getChildTextTrim("lpage"))
    val fPage = Option(node.getChildTextTrim("fpage"))
    val journalTitle = Option(node.getChildTextTrim("source"))
    val volume = Option(node.getChildTextTrim("volume"))
    val issue = Option(node.getChildTextTrim("issue"))

    val authors = Xml.selectNodeChildren(node, "string-name")
      .map(ContributorMetaXml.extractContributor(_))
      .filter(ContributorMeta.isEmpty(_))

    ReferenceMeta(
      title.getOrElse(""),
      journalTitle.getOrElse(""),
      publisher.getOrElse(""),
      publisherLocation.getOrElse(""),
      abstractText.getOrElse(""),

      year.getOrElse(""),
      fPage.getOrElse(""),
      lPage.getOrElse(""),
      volume.getOrElse(""),
      issue.getOrElse(""),

      authors
    )

  }

  def extractReferenceMetas(
    nlm: Document,
    xpath: String): List[ReferenceMeta] =
    Xml.selectNodes(nlm, xpath)
       .map(extractReferenceMeta(_))
}
