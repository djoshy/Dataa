package com.fizz.dataa;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

public class Stacker {
	private int top;
	private ArrayList<Sprite> stacksprite;
	Stacker(){
		top=-1;
		stacksprite=new ArrayList<Sprite>();
		
	}
	public int gettop(){
		return top;
	}
	public Sprite retlast(){
		return stacksprite.get(top);
	}
	public void pop(){
		stacksprite.remove(top);
		top--;
	}
	public void push(Sprite sprite){
		top++;
		stacksprite.add(sprite);
	}
}
