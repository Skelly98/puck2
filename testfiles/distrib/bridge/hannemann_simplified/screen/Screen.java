package screen;

public abstract class Screen {
  abstract void draw();
}

class WelcomeCapital extends Screen {
  void draw() {
    printCapital("Welcome");
  }
  void printCapital(String s) {
    System.out.println(s.toUpperCase());
  }
}

class WelcomeStar extends Screen {
  void draw() {
    printStar("Welcome");
  }
  void printStar(String s) {
    System.out.println("*** " + s + " ***");
  }
}

class InfoCapital extends Screen {
  void draw() {
    printCapital("Some info");
  }
  void printCapital(String s) {
    System.out.println(s.toUpperCase());
  }
}

class InfoStar extends Screen {
  void draw() {
    printStar("Some info");
  }
  
  void draw(int num, int truc , int machin, boolean isWorking, float x, String s) {
	 System.out.println("draw multiple param");
  }
  void printStar(String s) {
    System.out.println("*** " + s + " ***");
  }
}
