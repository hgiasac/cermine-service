package cermine.service.models

import org.jdom.Element
import org.jdom.Document
import cermine.service.utils._

case class ContributorMetaName(
  name: String,
  givenNames: String,
  surname: String,
)

case class ContributorMeta(
  name: String,
  givenNames: String,
  surname: String,
  emails: List[String],
  affiliations: List[String],
)

object ContributorMeta {

  def displayName(m: ContributorMeta): String =
    if (m.name != null && m.name != "") m.name
    else List(m.surname, m.givenNames)
      .filter((s) => s != null && s != "") mkString ", "


  def isEmpty(model: ContributorMeta): Boolean =
    model.name.isEmpty && model.givenNames.isEmpty &&
    model.surname.isEmpty && model.emails.isEmpty

}

object ContributorMetaXml {

  def extractName(node: Element): ContributorMetaName = {

    val nameEl = Option(node.getChild("name"))
    val name = Option(node.getChildTextTrim("string-name")).getOrElse("")
    nameEl match {
      case Some(el) => ContributorMetaName(
        name,
        Option(el.getChildTextTrim("surname")).getOrElse(""),
        Option(el.getChildTextTrim("given-names")).getOrElse("")
      )
      case None => ContributorMetaName(
        name,
        "",
        ""
      )
    }
  }

  def extractEmails(node: Element): List[String] =
    Xml.getNodeChildren(node)
      .map(_.getTextTrim)
      .filter(Validator.validateEmail(_))

  def extractAffiliations(
    node: Element,
    affiliations: Map[String, String]): List[String] =
      Xml.getNodeChildren(node)
        .map(el => (
          el.getAttributeValue("ref-type"),
          el.getAttributeValue("rid")
        ))
        .filter(t =>
          t._1 == "aff" && t._2 != null
        ).map(t => affiliations.get(t._2))
        .filter(r => r != None)
        .map(_.getOrElse(""))


  def extractContributorWithAffiliations(
    node: Element,
    affiliations: Map[String, String]): ContributorMeta = {

    val contributor = extractContributor(node)
    val affs = extractAffiliations(node, affiliations)

    contributor.copy(affiliations = affs)

  }

  def extractContributor(node: Element): ContributorMeta = {
    val names = extractName(node)
    val emails = extractEmails(node)

    ContributorMeta(
      names.name,
      names.givenNames,
      names.surname,
      emails,
      List(),
    )
  }

  def extractContributors(
    nlm: Document,
    xpath: String): List[ContributorMeta] =
    Xml.selectNodes(nlm, xpath)
       .map(extractContributor(_))


  def extractContributorsWithAffiliations(
    nlm: Document,
    xpath: String,
    affiliations: Map[String, String]): List[ContributorMeta] =
    Xml.selectNodes(nlm, xpath)
      .map(extractContributorWithAffiliations(_, affiliations))

}
