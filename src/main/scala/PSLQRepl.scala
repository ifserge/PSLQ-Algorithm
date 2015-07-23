/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._
import breeze.linalg._
import breeze.numerics._

object PSLQRepl extends App {
  print("PSLQ>")
  val break = new Breaks
  break.breakable{
    do {
      val sIn = Console.readLine()
      if (sIn != "q:"){
	      val basis = sIn.split(" ").toList.map(_.toDouble)
        val result = PSLQLib.Solve(basis, 1e-6)
        Console.println(result)
      }else break.break()

      Console.print("PSLQ>")
    } while (true)
  }
}
