package statement;


import java.util.List;

class Statement {
  class X {
    void doThing() {
    }
  }

  void f(List<X> list) {
    for (X item : list) {
      item.doThing();
    }
  }
}