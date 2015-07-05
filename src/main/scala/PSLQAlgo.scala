/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD

object PSLQAlgo extends App {
  val conf = new SparkConf().setAppName("PSLQAlgo").setMaster("local")
  val sc = new SparkContext(conf)
  val break = new Breaks
  break.breakable{
    do {
      Console.print("PSLQ>")
      val s = Console.readLine()
      if (s != "q:"){
	      val basis = s.split(" ").toList.map(BigDecimal(_))
        val partialsums = basis.foldLeft(List(BigDecimal(0))){ case (l,x) => (l.head + x*x) :: l }
        Console.println(partialsums)
      }else break.break()
    } while (true)
  }
}
