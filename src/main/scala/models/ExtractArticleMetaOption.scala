package cermine.service.models


class ExtractArticleMetaOption(exclude: Seq[String]) {

  def canExtractProperty(name: String): Boolean =
    exclude.contains(name) == false

}
