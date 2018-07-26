package cermine.service

import io.finch._, io.finch.syntax._
import io.finch.circe._
import io.circe.generic.auto._

import com.twitter.finagle.{Http, Service}

import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await
import pl.edu.icm.cermine._

import java.io.{File, FileInputStream, ByteArrayInputStream}
import org.jdom.Document
import org.jdom.output.{XMLOutputter, Format}
import cermine.service.models._

case class Chunk(length: Int, bytes: Array[Byte])
case class Message(message: String)
case class ResponseMessage(responseMessage: String)

trait FileReader {

    def fileContentStream(fileIn: FileInputStream): Stream[Chunk] = {

      val bytes = Array.fill[Byte](1024)(0)
      val length = fileIn.read(bytes)
      Chunk(length, bytes) #:: fileContentStream(fileIn)

    }

    def usingFileInput[T](fileName: String)(f: (Stream[Chunk]) => T): T = {

      val fileInputStream = new FileInputStream(new File(fileName))
      val stream =
        fileContentStream(fileInputStream) takeWhile { chunk => chunk.length > 0 }
      val result = f(stream)
      fileInputStream.close()
      result
    }
}

object Main extends App {


  val extractPdfText =
    post("pdf" :: path("extract-xml") :: binaryBody) { b: Array[Byte] =>

    val extractor = new ContentExtractor()
    extractor.setPDF(new ByteArrayInputStream(b))
    val resEl = extractor.getContentAsNLM
    val outputter = new XMLOutputter(Format.getPrettyFormat());

    val doc = new Document(resEl)
    val result = outputter.outputString(doc);

    Ok(result)
  }

  val extractedFile = params("exclude") :: binaryBody

  val extractFile = post("pdf" :: path("extract") :: extractedFile) {
    (exclude: Seq[String], b: Array[Byte]) =>

    val extractor = new ContentExtractor()
    extractor.setPDF(new ByteArrayInputStream(b))
    val resEl = extractor.getContentAsNLM

    val doc = new Document(resEl)
    val result = ArticleMetaXml.extractNLM(
      doc,
      new ExtractArticleMetaOption(exclude)
    )

    Ok(result)
  }

  val api: Service[Request, Response] =
    (extractFile :+: extractPdfText).toServiceAs[Application.Json]

  Await.ready(
    Http.server.serve(":8080", api)
  )
}
