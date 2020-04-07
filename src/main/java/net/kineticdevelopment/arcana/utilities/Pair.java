package net.kineticdevelopment.arcana.utilities;

import java.util.Objects;

// Thank you, https://stackoverflow.com/a/8229791/9777506
public class Pair<A, B>{
	
	A first;
	B second;
	
	public Pair(A first, B second){
		this.first = first;
		this.second = second;
	}
	
	public static <A, B> Pair<A, B> of(A first, B second){
		return new Pair<>(first, second);
	}
	
	public A getFirst(){
		return first;
	}
	
	public B getSecond(){
		return second;
	}
	
	public void setFirst(A first){
		this.first = first;
	}
	
	public void setSecond(B second){
		this.second = second;
	}
	
	public Pair<B, A> flip(){
		return of(getSecond(), getFirst());
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Pair))
			return false;
		Pair<?, ?> pair = (Pair<?, ?>)o;
		return Objects.equals(getFirst(), pair.getFirst()) && Objects.equals(getSecond(), pair.getSecond());
	}
	
	public int hashCode(){
		return Objects.hash(getFirst(), getSecond());
	}
}