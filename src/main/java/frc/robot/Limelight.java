package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
	private static NetworkTable table;
	private static NetworkTableEntry tv, tx, ty, ta;
	private static int v;
	private static double x, y, a;
	private static double moveP, moveMin, moveMax, targetY;
	private static double alignP, alignMin, alignMax;

	static {
		table = NetworkTableInstance.getDefault().getTable("limelight");
		tv = table.getEntry("tv");
		tx = table.getEntry("tx");
		ty = table.getEntry("ty");
		ta = table.getEntry("ta");
	}

	public static void update() {
		table.getEntry("pipeline").setNumber(1);
		table.getEntry("ledMode").setNumber(0);
		v = (int) tv.getDouble(0.0);
		x = tx.getDouble(0.0);
		y = ty.getDouble(0.0);
		a = ta.getDouble(0.0);
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

	public static void setMoveConstants(double p, double min, double max, double y) {
		moveP = p;
		moveMin = min;
		moveMax = max;
		targetY = y;
	}
	public static void setAlignConstants(double p, double min, double max) {
		alignP = p;
		alignMin = min;
		alignMax = max;
	}
	
	public static double align() {
		if (Math.abs(x) > 1) {
			return aclamp(alignP * x, alignMin, alignMax);
		}
		return 0;
	}
	public static double move() {
		if (v == 1) {
			return aclamp(moveP * (targetY - y), moveMin, moveMax);
		}
		return 0;
	}
	
	private static double clamp(double n, double min, double max) {
		return Math.min(Math.max(n, min), max);
	}
	private static double aclamp(double n, double min, double max) {
		if (n > 0) {
			return clamp(n, min, max);
		}
		return clamp(n, -max, -min);
	}

	public static void flash() {
		table.getEntry("ledMode").setNumber(2);
	}
}
