/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._
import breeze.linalg._
import breeze.numerics._

object PSLQAlgo extends App {
  val gamma = 2.0 / sqrt(3.0)
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
        val H = DenseMatrix.tabulate(n, n-1) {
          case (i,j) => if (j < i) -y(j)*y(i) / (s(j+1)*s(j)) else if (i == j && i < n) s(j+1) / s(j) else 0
        }
        val A = DenseMatrix.eye[Double](n)
        val B = DenseMatrix.eye[Double](n)
        //step 1: hermitian reduction
        for (i <- 2 to n; j <- i-1 to 1 by -1) {
          val t = floor(0.5 + H(i-1,j-1) / H(j-1,j-1))
          H(i-1, 0 to (j-1)) := H(i-1, 0 to (j-1)) - (H(j-1, 0 to (j-1)) :* t)
          A(i-1, 0 to (n-1)) := A(i-1, 0 to (n-1)) - (A(j-1, 0 to (n-1)) :* t)
          B(0 to (n-1), j-1) := B(0 to (n-1), j-1) + (B(0 to (n-1), i-1) :* t)
        }
        //step 2: iteration procedure
        //for (it <- 1 to 10) {
          val gammaVector = DenseVector.tabulate(n-1){i => pow(gamma, i+1) * H(i,i)}
          val m = argmax(gammaVector) + 1
          val ys = DenseVector.tabulate(n){ i => if (i == m-1) y(m) else if (i == m) y(m-1) else y(i) }
        //}
        
      }else break.break()
    } while (true)
  }
}
