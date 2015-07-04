/**
 * User: Sergey Fironov
 * Date: 04.07.15
 * Time: 11:42
 */

import scala.util.control._

object PSLQAlgo extends App {
  val break = new Breaks
  break.breakable{
    do {
      Console.print("PSLQ>")
      val s = Console.readLine()
      if (s != "q:"){
        Console.println(s)
      }else break.break()
    } while (true)
  }
}
