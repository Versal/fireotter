package com.versal.fireotter.tests

object Arithmetic {
  def add(x: Int, y: Int): Int = x + y
  def subtract(x: Int, y: Int): Int = x - y
  def multiply(x: Int, y: Int): Int = x * y
  def divide(x: Int, y: Int): Int = x / y
}

class ArithmeticTest extends org.scalatest.FunSuite {

  import com.versal.fireotter._

  val specs: Traversable[Seq[String]] = csv(resource("arithmetic.csv"))

  specs foreach { spec =>

    val inputs: Seq[Int] = spec(2).split(",").map(_.toInt)

    val output = spec(1) match {
      case "add"      => Arithmetic.add(inputs(0), inputs(1))
      case "subtract" => Arithmetic.subtract(inputs(0), inputs(1))
      case "multiply" => Arithmetic.multiply(inputs(0), inputs(1))
      case "divide"   => Arithmetic.divide(inputs(0), inputs(1))
    }

    val expectedOutput: Int = spec(3).toInt

    test(spec(0)) { assert(output === expectedOutput) }
  }

}
