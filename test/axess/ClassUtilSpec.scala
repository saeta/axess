package axess

import org.specs2.mutable._
import org.clapper.classutil.ClassFinder

class ClassUtilSpec extends Specification {
  "ClassFinder" should {
    "find this class" in {
      val cf = ClassFinder()
      val classes = cf.getClasses
      classes.foreach(a => println(a.name))
      val thisClass = classes.filter(a => a.name.contains("ClassUtilSpec"))
      //      val siteTypes = ClassFinder.concreteSubclasses("models.SiteType", classes)

      thisClass.size === 1
    }
  }
}