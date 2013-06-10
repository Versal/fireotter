package com.versal.fireotter

object `package` {

  def resource(name: String): String = getClass.getClassLoader.getResource(name).getPath

  def csv(path: String): Traversable[Seq[String]] = new Traversable[Seq[String]] {

    import java.io.{BufferedReader, InputStreamReader, FileInputStream}
  
    override def foreach[U](f: Seq[String] => U): Unit = {
      val reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))
      try {
        reader.readLine()
        var next = true
        while (next) {
          val line = reader.readLine()
          if (line != null) f(parse(line))
          else next = false
        }
      } finally {
        reader.close()
      }
    }
  
    def toMap[T, U](toPair: Seq[String] => (T, U)): Map[T, U] = {
      val mapBuilder = Map.newBuilder[T, U]
      for (row <- this) mapBuilder += toPair(row)
      mapBuilder.result
    }
  
    private def parse(line: String): Seq[String] = {
      var quoted = false 
      var prevWasQuote = false 
      var escape = false
      val cell = new StringBuilder
      var cells: Seq[String] = Seq.empty
      for (char <- line) {
        if (escape) {
          cell += char
          escape = false
        } else if (char == '\\') {
          escape = true
        } else if (char == '"') {
          if (prevWasQuote && !quoted) cell += char
          quoted = !quoted
        } else if (char == ',' && !quoted) {
          cells = cells ++ Seq(cell.result)
          cell.clear
        } else {
          cell += char
        }
        prevWasQuote = (char == '"')
      }
      cells ++ Seq(cell.result)
    }

  }
  
}
