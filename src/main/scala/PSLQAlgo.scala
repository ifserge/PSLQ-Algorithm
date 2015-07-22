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
        val ys = DenseVector.tabulate(n){ i => y(i) }
        while (min(ys.map(_.abs)) > 1e-6) {
          val gammaVector = DenseVector.tabulate(n-1){i => pow(gamma, i+1) * H(i,i)}
          val m = argmax(gammaVector) + 1
          
          //step 3: swap m-1 <-> m
          ys := DenseVector.tabulate(n){ case(i) => if (i == m-1) ys(m) else if (i == m) ys(m-1) else ys(i) }
          val Hm = DenseVector.tabulate(n-1){ i => H(m-1, i) }
          H(m-1, 0 to (n-2)) := H(m, 0 to (n-2))
          H(m, 0 to (n-2)) := Hm.t
          val Am = DenseVector.tabulate(n){ i=>A(m-1, i) }
          A(m-1, ::) := A(m, ::)
          A(m, ::) := Am.t
          val Bm = DenseVector.tabulate(n){i => B(i, m-1) }
          B(::, m-1) := B(::, m)
          B(::, m) := Bm

          //step 4: not a corner case
          if (m <= n-2) {
            val t0 = sqrt(H(m-1,m-1) * H(m-1,m-1) + H(m-1,m)*H(m-1,m));
            val t1 = H(m-1,m-1) / t0;
            val t2 = H(m-1,m) / t0;
            for (i <- m to n) {
              val t3 = H(i-1,m-1)
              val t4 = H(i-1,m)
              H(i-1,m-1) = t1 * t3 + t2 * t4;
              H(i-1,m) = -t2 * t3 + t1 * t4;
            }
          }

          //step 5: reduction
          for (i <- (m+1) to n) {
            val js = Math.min(i-1, m+1)
            for (j <- js to 1 by -1) {
              val t = Math.floor(0.5 + H(i-1,j-1) / H(j-1,j-1));
              ys(j-1) = ys(j-1) + t * ys(i-1)
              H(i-1, 0 to (j-1)) := H(i-1, 0 to (j-1)) - (H(j-1, 0 to (j-1)) :* t)
              A(i-1, 0 to (n-1)) := A(i-1, 0 to (n-1)) - (A(j-1, 0 to (n-1)) :* t)
              B(0 to (n-1), j-1) := B(0 to (n-1), j-1) + (B(0 to (n-1), i-1) :* t)
            }
          }
        }
        //step 6: result
        val resultColumn = argmin(ys.map(_.abs))
        val result = B(::,resultColumn)
        Console.println(result)
      }else break.break()
    } while (true)
  }
}
