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
  def PreHermitCol(x: Array[Double], s: Array[Double], i: Integer) : Array[Double] = {
    List.fill(i)(0.0) ++
    List(s(i+1) / s(i)) ++
    (for (j <- (i+1) to (x.length-1))
      yield - x(j) * x(i) / (s(i) * s(i+1))).toList
  }.toArray
  def PreHermitMatrix(x: Array[Double], s: Array[Double]) = {
    for (i <- 0 to (x.length-1)) 
      yield PreHermitCol(x, s, i)
  }.transpose
  val conf = new SparkConf().setAppName("PSLQAlgo").setMaster("local")
  val sc = new SparkContext(conf)
  val break = new Breaks
  break.breakable{
    do {
      Console.print("PSLQ>")
      val s = Console.readLine()
      if (s != "q:"){
	      val basis = s.split(" ").toList.map(_.toDouble)
        val partialsums = basis.foldLeft(List(0.0)){ case (l,x) => (l.head + x*x) :: l }
        val rm = new RowMatrix(sc.makeRDD(PreHermitMatrix(basis.toArray, partialsums.toArray).map(r=>Vectors.dense(r.toArray))))
        
      }else break.break()
    } while (true)
  }
}
