package models;

/**
 *
 * @author Aidan Galbreath
 *
 * This class is used to house various mathematical and program-specific constants.
 *
 */

public class Constants {
        public static final double RADIANS_TO_DEGREES = 180 / Math.PI;
        public static final double DEGREES_TO_RADIANS = Math.PI / 180;
        public static final double WAYPOINT_CAPTURE_DISTANCE = .5;	//Distance (in feet) that we count as an acceptable margin of error for the waypoints
        public static final double WAYPOINT_SLOWDOWN_DISTANCE = 10;
        public static final double MINIMUM_SPEED = .2;
        public static final double MILLI_TO_SEC = 1d / 1000d;
		// There are 1440 pulses per revolution, Diameter is .5 feet.
		public static final double DISTANCE_PER_PULSE = Math.PI * .5 / 1440;

        public static final Point ORIGIN = new Point(0, 0);
		public static final long AUTONOMOUS_TIME_DRIVE_FORWARD_MILLISECONDS = 6000;
		public static final long ACCELERATION_TIME_FRAME = 500;

        /***
         * Lengths and distances less than this are considered to be 0 as far as
         * vectors and points (respectively) are concerned.
         */
        public static final double EPSILON = 0.001;
		public static final double TURN_MARGIN_OF_ERROR = 3;
}
