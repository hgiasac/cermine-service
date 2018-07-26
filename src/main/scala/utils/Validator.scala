package cermine.service.utils

object Validator {
  def emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  def validateEmail(e: String): Boolean = e match {
    case null => false
    case e if e.trim.isEmpty => false
    case e if emailRegex.findFirstMatchIn(e).isDefined => true
    case _ => false
  }
}
