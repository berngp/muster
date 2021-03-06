package muster

import java.util.Date
import java.text.DateFormat

trait OutputFormatter[R] extends AutoCloseable {
  def startArray(name: String = ""): Unit

  def endArray(): Unit

  def startObject(name: String = ""): Unit

  def endObject(): Unit

  def string(value: String): Unit

  def byte(value: Byte): Unit

  def int(value: Int): Unit

  def long(value: Long): Unit

  def bigInt(value: BigInt): Unit

  def boolean(value: Boolean): Unit

  def short(value: Short): Unit

  def float(value: Float): Unit

  def double(value: Double): Unit

  def bigDecimal(value: BigDecimal): Unit

  def startField(name: String): Unit

  def writeNull(): Unit

  def undefined(): Unit

  def result: R

  def close()
}


trait OutputFormat[R] {
  type Formatter <: OutputFormatter[R]

  def createFormatter: Formatter

  def from[T](out: T)(implicit producer: Producer[T]): R = {
    val fmt = createFormatter
    producer.produce(out, fmt)
    fmt.result
  }
}
