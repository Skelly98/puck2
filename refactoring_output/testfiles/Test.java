package test;

class SuperTest extends Test implements Foo {
  int r;

  void superMethod(Test t, Foo f) {
  }

  void t();

  void t(Test t);
}

public class Test implements Foo {
  int r;

  SuperTest f;

  void f() {
  }

  int m(int x) {
    return x * x;
  }

  int m(double x) {
    return (int) x;
  }

  int m(Foo f) {
  }
}

interface Foo {
  void t();

  void t(Test t);
}