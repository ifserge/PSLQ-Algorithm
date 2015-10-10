import scala.math._
import scala.util.parsing.combinator._
import scala.util.Random

class PSLQParser() extends JavaTokenParsers {
  val constants = Map("E" -> math.E, "e" -> math.E, "PI" -> math.Pi, "Pi" -> math.Pi, "pi" -> math.Pi)
  val unaryOps: Map[String,Double => Double] = Map(
   "sqrt" -> (math.sqrt(_)), 
   "abs" -> (math.abs(_)), 
   "ln" -> (math.log(_)) 
  )
  val binaryOps: Map[String,(Double,Double) => Double] = Map(
   "*" -> (_*_), 
   "/" -> (_/_), 
   "+" -> (_+_), 
   "-" -> (_-_), 
   "^" -> (pow(_,_))
  )
  private def fold(d: Double, l: List[~[String,Double]]) = l.foldLeft(d){ case (d1,op~d2) => binaryOps(op)(d1,d2) } 
  private implicit def map2Parser[V](m: Map[String,V]) = m.keys.map(_ ^^ (identity)).reduceLeft(_ | _)
  lazy val expression:  Parser[Double] = sign~term~rep(("+"|"-")~term) ^^ { case s~t~l => fold(s * t,l) }
  lazy val sign:        Parser[Double] = opt("+" | "-") ^^ { case None => 1.0D; case Some("+") => 1.0D; case Some("-") => -1.0D }
  lazy val term:        Parser[Double] = longFactor~rep(("*"|"/")~longFactor) ^^ { case d~l => fold(d,l) }
  lazy val longFactor:  Parser[Double] = shortFactor~rep("^"~shortFactor) ^^ { case d~l => fold(d,l) }
  lazy val shortFactor: Parser[Double] = fpn | sign~(constant | unaryFct | binaryFct | "("~>expression<~")") ^^ { case s~x => s * x }
  lazy val constant:    Parser[Double] = constants ^^ (constants(_))
  lazy val fpn:         Parser[Double] = floatingPointNumber ^^ (_.toDouble) 
  lazy val unaryFct:    Parser[Double] = unaryOps~"("~expression~")" ^^ { case op~_~d~_ => unaryOps(op)(d) }
  lazy val binaryFct:   Parser[Double] = binaryOps~"("~expression~","~expression~")" ^^ { case op~_~d1~_~d2~_ => binaryOps(op)(d1,d2) }
  def evaluate(formula: String) = parseAll(expression,formula).get
}