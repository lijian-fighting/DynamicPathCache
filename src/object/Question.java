package object;

import java.util.Random;

public class Question {
	private int a;
	private int b;
	public Question(int max){
		Random ra =new Random();
		a=ra.nextInt(max);
		b=ra.nextInt(max);
	}
	public int getstart(){
		return this.a;
	}
	public int getend(){
		return this.b;
	}
}
