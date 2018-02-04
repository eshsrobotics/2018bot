package org.usfirst.frc.team1759.robot;

public enum StartingPosition {
	leftStart, centerStart, rightStart;

	StartingPosition cName;

	private StartingPosition(StartingPosition cName) {
		this.cName = cName;
	}


public void startDetails() {
	switch(cName){
	case leftStart:
		System.out.print("starting left");
		break;
	case centerStart:
		System.out.print("starting center");
		break;
	case rightStart:
		System.out.print("starting right");
		break;
				
		}
	}

}


