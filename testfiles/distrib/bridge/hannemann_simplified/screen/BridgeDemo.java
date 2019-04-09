package screen;

public class BridgeDemo {
	
	public BridgeDemo(String s) {
		
	}
	
	
    public static void main(String[] args) { 
        WelcomeStar ws = new WelcomeStar();
        WelcomeCapital wc = new WelcomeCapital();
        InfoCapital ic = new InfoCapital();
        InfoStar is = new InfoStar();  
        
        ws.draw();
        wc.draw();
        ic.draw();
        is.draw();
    }
}
