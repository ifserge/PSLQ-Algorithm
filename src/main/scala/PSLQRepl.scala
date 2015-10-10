/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._
import breeze.linalg._
import breeze.numerics._

object PSLQRepl extends App {
  println("""Please input the list of expressions. Type "q:" to quit.""")
  println("""Type eps:value to set the accuracy value. The default one is 1e-6""")
  val parserMath = new PSLQParser
  val break = new Breaks
  var eps = 1e-6
  break.breakable{
    do {
      val sIn = Console.readLine("PSLQ> ")
      if (sIn.startsWith("eps:")) {
        eps = (sIn.drop(4).toDouble)
      } else
      if (sIn != "q:"){
	      val basis = sIn.split(" ").toList.map(parserMath.evaluate(_))
        val result = PSLQLib.Solve(basis, eps)
        Console.println(result)
      }else break.break()
    } while (true)
  }
}
