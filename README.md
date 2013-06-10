# FireOtter

FireOtter is a simple CSV parsing library for building tests on human-readable, spreadsheet-based specifications.

## Usage

Add FireOtter to the list of dependencies in build.sbt:

```
resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.versal" %% "fireotter" % "0.1.0-SNAPSHOT" % "test"
```

## How it works

FireOtter isn't much more than a CSV parser, but it provides a handy tool for test-driven development based on *code-free* test specifications written as human-readable spreadsheets.

First, establish a convention for how to express test specifications as spreadsheet rows.  Then write the code necessary to bind them, given as a `Traversable[Seq[String]]`, to your favorite test framework

Now you can run through the usual TDD loop:

1. Describe a desired feature in terms of your expression convention
2. Write the code needed to make the tests pass
3. Repeat

## Example

*CSV test specifications:*

<table>
  <tr><th>test</th><th>function</th><th>input</th><th>expected output</th></tr>
  <tr><td>1 + 1 = 2</td><td>add</td><td>"1</td><td>1"</td><td>2</td></tr>
  <tr><td>2 - 1 = 1</td><td>subtract</td><td>"2</td><td>1"</td><td>1</td></tr>
  <tr><td>2 * 3 = 6</td><td>multiply</td><td>"2</td><td>3"</td><td>6</td></tr>
  <tr><td>6 / 3 = 2</td><td>divide</td><td>"6</td><td>3"</td><td>2</td></tr>
</table>

*Code under test:*

```scala
object Arithmetic {
  def add(x: Int, y: Int): Int = x + y
  def subtract(x: Int, y: Int): Int = x - y
  def multiply(x: Int, y: Int): Int = x * y
  def divide(x: Int, y: Int): Int = x / y
}
```

*CSV specification parser and tester:*

```scala
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
```

*Test output under ScalaTest:*

```
> test
[info] ArithmeticTest:
[info] - 1 + 1 = 2
[info] - 2 - 1 = 1
[info] - 2 * 3 = 6
[info] - 6 / 3 = 2
[info] Passed: : Total 4, Failed 0, Errors 0, Passed 4, Skipped 0
[success] Total time: 0 s, completed Jun 5, 2013 1:01:05 PM
```
