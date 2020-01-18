package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
	private static NetworkTable table;
	private static NetworkTableEntry tv, tx, ty, ta;
	private static int v;
	private static double x, y, a;
	private static double targetY;
	private static PID movePID, alignPID;

	static {
		table = NetworkTableInstance.getDefault().getTable("limelight");
		tv = table.getEntry("tv");
		tx = table.getEntry("tx");
		ty = table.getEntry("ty");
		ta = table.getEntry("ta");
	}

	public static void update() {
		table.getEntry("pipeline").setNumber(2);
		table.getEntry("ledMode").setNumber(0);
		v = (int) tv.getDouble(0.0);
		x = tx.getDouble(0.0);
		y = ty.getDouble(0.0);
		a = ta.getDouble(0.0);
		if (v == 0) {
			movePID.reset();
			alignPID.reset();
		}
		movePID.update(targetY - y);
		alignPID.update(x);
	}

	public static void disable() {
		table.getEntry("pipeline").setNumber(0);
	}

	public static int getV() {
		return v;
	}
	public static double getX() {
		return x;
	}
	public static double getY() {
		return y;
	}
	public static double getA() {
		return a;
	}
	public static NetworkTableEntry getEntry(String entry) {
		return table.getEntry(entry);
	}

	public static void setMoveConstants(double kp, double ki, double kd, double y) {
		movePID = new PID(kp, ki, kd);
		targetY = y;
	}
	public static void setAlignConstants(double kp, double ki, double kd) {
		alignPID = new PID(kp, ki, kd);
	}
	
	public static double align() {
		if (Math.abs(x) > 1) {
			return alignPID.getPower();
		}
		return 0;
	}
	public static double move() {
		if (v == 1) {
			return movePID.getPower();
		}
		return 0;
	}

	public static void flash() {
		table.getEntry("ledMode").setNumber(2);
	}
}
