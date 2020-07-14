package net.arcanamod.util;

import java.util.Objects;

// I know there's already org.apache.commons.lang3.tuple.Pair, but I didn't when I wrote this
// It doesn't have flip() though
// I'll replace this... later
public final class Pair<A, B>{
	
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
	
	public boolean contains(Object obj){
		return first == obj || second == obj;
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