import org.scalatest.funsuite.AnyFunSuite

class AppSuite extends AnyFunSuite {
  test("Adding 2 and 2 should produce 4") {
    assert(add(2, 2) == 4)
  }
}