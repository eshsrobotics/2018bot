package org.usfirst.frc.team1759.robot;

public enum StartingSwitchSide {
	left, right;
	
	StartingSwitchSide bName;
	
	private StartingSwitchSide(StartingSwitchSide bName) {
		this.bName = bName;
	}
public void switchStartDetails() {
	switch(bName){
	case left:
		System.out.print("your switch side is left");
		break;
	case right:
		System.out.print("your switch side is right");
		break;
		}
	}
}