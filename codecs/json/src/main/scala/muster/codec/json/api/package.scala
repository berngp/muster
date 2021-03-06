package muster
package codec
package json

import com.fasterxml.jackson.databind.node.MissingNode
import scala.util.Try
import com.fasterxml.jackson.databind.JsonNode

package object api {

  object JsonFormat extends ProducibleJsonOutput(StringProducible) with JacksonInputFormat[Consumable[_]] {
    private def jic[T](src: T)(fn: (T) => JsonNode): JacksonInputCursor[T] = new JacksonInputCursor[T] {
      protected val node: JsonNode = Try(fn(src)).getOrElse(MissingNode.getInstance())
      val source: T = src
    }

    def createCursor(in: Consumable[_]): JacksonInputCursor[_] = in match {
      case StringConsumable(src) => jic(src)(mapper.readTree)
      case FileConsumable(src) => jic(src)(mapper.readTree)
      case ReaderConsumable(src) => jic(src)(mapper.readTree)
      case InputStreamConsumable(src) => jic(src)(mapper.readTree)
      case ByteArrayConsumable(src) => jic(src)(mapper.readTree)
      case URLConsumable(src) => jic(src)(mapper.readTree)
    }
  }
  implicit class ProducingObject[T](p: T)(implicit prod: Producer[T])  {
    def asJson = JsonFormat.from(p)
    def asPrettyJson = JsonFormat.Pretty.from(p)
  }
}
