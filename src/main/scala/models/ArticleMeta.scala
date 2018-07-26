package cermine.service.models

import org.jdom.Element
import org.jdom.Document
import cermine.service.utils.Xml


case class ArticleMeta(

  title: String,
  journalTitle: String,
  journalISSN: String,
  publisher: String,

  doi: String,
  urn: String,
  abtractText: String,

  authors: List[ContributorMeta],
  editors: List[ContributorMeta],
  keywords: List[String],
  volume: String,
  issue: String,

  publishDate: Option[String],
  receivedDate: Option[String],
  revisedDate:  Option[String],
  acceptedDate:  Option[String],
  fPage: String,
  lPage: String,
  references: List[ReferenceMeta],
)

object ArticleMetaXml {

  def extractAffiliation(node: Element): (String, String) = {
    val affId = node.getAttributeValue("id")
    val text = node.getValue().trim.replaceFirst(affId, "").trim

    (affId, text)
  }

  def extractAffiliations(
    nlm: Document,
    xpath: String): Map[String, String] = {
    Xml.selectNodes(nlm, xpath)
      .foldLeft(Map[String, String]())((m, n) =>
        m + extractAffiliation(n),
      )
  }

  def extractNLM(nlm: Document, options: ExtractArticleMetaOption): ArticleMeta = {

    val journalTitle =
      if (options.canExtractProperty("journalTitle"))
        Xml.xPathValue(nlm, "/article/front//journal-title")
      else ""

    val journalISSN =
      if (options.canExtractProperty("journalISSN"))
        Xml.xPathValue(nlm, "/article/front//journal-meta/issn[@pub-type='ppub']")
      else ""

    val publisher =
      if (options.canExtractProperty("publisher"))
        Xml.xPathValue(nlm, "/article/front//publisher-name")
      else ""

    val title =
      if (options.canExtractProperty("title"))
        Xml.xPathValue(nlm, "/article/front//article-title")
      else ""

    val doi =

      if (options.canExtractProperty("doi"))
        Xml.xPathValue(nlm, "/article/front//article-id[@pub-id-type='doi']")
      else ""

    val urn =

      if (options.canExtractProperty("urn"))
        Xml.xPathValue(nlm, "/article/front//article-id[@pub-id-type='urn']")
      else ""

    val abstractText =

      if (options.canExtractProperty("abstractText"))
        Xml.xPathValue(nlm, "/article/front//abstract")
      else ""

    val fPage =

      if (options.canExtractProperty("fPage"))
        Xml.xPathValue(nlm, "/article/front/article-meta/fpage")
      else ""

    val lPage =
      if (options.canExtractProperty("lPage"))
        Xml.xPathValue(nlm, "/article/front/article-meta/lpage")
      else ""

    val volume =
      if (options.canExtractProperty("volume"))
        Xml.xPathValue(nlm, "/article/front/article-meta/volume")
      else ""

    val issue =

      if (options.canExtractProperty("issue"))
        Xml.xPathValue(nlm, "/article/front/article-meta/issue")
      else ""

    val affiliations =

      if (options.canExtractProperty("affiliations"))
        extractAffiliations(nlm, "//aff")
      else Map[String, String]()

    val authors =

      if (options.canExtractProperty("authors"))
        ContributorMetaXml.extractContributorsWithAffiliations(
          nlm,
          "//contrib[@contrib-type='author']",
          affiliations
        )
      else List.empty

    val editors =

      if (options.canExtractProperty("editors"))
        ContributorMetaXml.extractContributors(
          nlm,
          "//contrib[@contrib-type='editor']",
        )
      else List.empty

    val publishDate =

      if (options.canExtractProperty("publishDate"))
        Xml.extractDateValue(nlm, "//pub-date")
      else None

    val receivedDate =

      if (options.canExtractProperty("receivedDate"))
        Xml.extractDateValue(nlm, "//history/date[@date-type='received']")
      else None

    val revisedDate =

      if (options.canExtractProperty("revisedDate"))
        Xml.extractDateValue(nlm, "//history/date[@date-type='revised']")
      else None

    val acceptedDate =

      if (options.canExtractProperty("acceptedDate"))
        Xml.extractDateValue(nlm, "//history/date[@date-type='accepted']")
      else None


    val keywords =
      if (options.canExtractProperty("keywords"))
        Xml.selectNodes(nlm, "//kwd")
          .map(e => e.getTextTrim)
      else List()

    val references =

      if (options.canExtractProperty("references"))
        ReferenceMetaXml
          .extractReferenceMetas(nlm, "//ref/mixed-citation")
      else List()

    ArticleMeta(
      title,
      journalTitle,
      journalISSN,
      publisher,

      doi,
      urn,
      abstractText,
      authors,
      editors,

      keywords,
      volume,
      issue,
      publishDate,
      receivedDate,

      revisedDate,
      acceptedDate,
      fPage,
      lPage,
      references
    )
  }
}
