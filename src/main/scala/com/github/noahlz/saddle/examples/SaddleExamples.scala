package com.github.noahlz.saddle.examples

import org.saddle._
import org.saddle.array._
import org.saddle.index.InnerJoin

object SaddleExamples {
  def main(args: Array[String]) = {
    val frame = partitionSeries()
  }

  def partitionSeries() = {
    // 'a' to 'z'
    val index: Index[String] = range('a', 'z' + 1, 1).map(i => s"${i.toChar}")

    // Sparse data
    val input: Series[String, Double] = Series(
      "a" -> 10.0, "b" -> 25.0,                           // 35.0
      "e" -> 40.0, "f" -> 40.0, "g" -> 20.0, "h" -> 25.0, // 125.0
      "x" -> 10.0, "y" -> 25.0, "z" -> 10.0)              // 45.0

    println("==== Data ====")
    input.print(input.length)
    println()

    val data = input.reindex(index)

    println("=== Indexed ===")
    data.print(data.length)
    println()

      // Mark every row were NA -> Double (or vice versa)
      def paritionFn(s: Series[String, Double]) = {
        val (x, y) = s.at(0) -> s.at(1)
        if ((x.isNA && !y.isNA) || (!x.isNA && y.isNA)) 1
        else 0
      }

    // Use rolling to identify contiguous NA and non-NA groups
    val initialValue = data.head(1).mapValues(v => if (v.isNaN) 0 else 1)
    val changes: Series[String, Int] = initialValue concat data.rolling(2, paritionFn)
    val groups = changes.cumSum

    // save for later
    val frame = groups.hjoin(data, how = InnerJoin)

    // better way to do this with inner join?
    val table = Series(data.values, Index(groups.values.toSeq: _*))

    val result = table.groupBy.combine(_.sum)

    println("==== Grouped ====")
    result.print(result.length)
    println()

    println("=== As Frame ===")
    frame.print(frame.rowIx.length)
    println()

    frame
  }
}
