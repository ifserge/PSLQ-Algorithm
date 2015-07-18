/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._
import breeze.linalg._
import breeze.numerics._

object PSLQAlgo extends App {
  val break = new Breaks
  break.breakable{
    do {
      Console.print("PSLQ>")
      val sIn = Console.readLine()
      if (sIn != "q:"){
	      val basis = sIn.split(" ").toList.map(_.toDouble)
        val sp = basis.reverse.foldLeft(List[Double]()){ 
          case (l,x) => l match {
            case Nil => List(abs(x))
            case _ => sqrt(pow(l.head,2.0) + x*x) :: l 
          }
        }
        val x = basis
        val s = sp.map(_ / sp.head)
        val y = x.map(_ / sp.head)
        val n = x.length
        //Console.println(x)
        //Console.println(s)
        //Console.println(y)
        val H = DenseMatrix.tabulate(n, n-1) {
          case (i,j) => if (j < i) -y(j)*y(i) / (s(j+1)*s(j)) else if (i == j && i < n) s(j+1) / s(j) else 0
        }
        //Console.println(H)
        val A = DenseMatrix.eye[Double](n)
        val B = DenseMatrix.eye[Double](n)
        for (i <- 2 to n; j <- i-1 to 1 by -1) {
          val t = floor(0.5 + H(i-1,j-1) / H(j-1,j-1))
          Console.println(t)
        }

        
      }else break.break()
    } while (true)
  }
}
