package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import models.Constants;
import models.TankDriveInterface;
import models.TestableCommandInterface;
import models.Vector2;

/***
 * This command's purpose is to rotate the tank drive continuously until the
 * robot is pointing at the desired heading.
 *
 * This should not be running at the same time as Go(), as they both utilize the TankDrive.
 *
 * @author uakotaobi
 */
public class TurnCommand extends Command implements TestableCommandInterface {

        /**
         * We are not allowed to turn the robot faster than this during each
         * invocation of execute().
         */
        private static final double MAX_TURNING_ANGLE_DEGREES = 0.1;

        /**
         * We are not allowed to turn the robot any more slowly than this
         * during each invocation of execute().
         */
        private static final double MIN_TURNING_ANGLE_DEGREES = 0.005;

        /**
         * The slowest that we can turn is {@link MIN_TURNING_ANGLE_DEGREES}.
         * This corresponds to a "turn magnitude" of 0. (We use these "turn
         * magnitudes" because we can't just instruct a tank drive to rotate by
         * theta degrees--they don't work that way.)
         *
         * The fastest that we can turn is {@link MAX_TURNING_ANGLE_DEGREES}.
         * This corresponds to a turn magnitude of 1.
         *
         * The fastest that a tank drive robot can turn is (-1.0, 1.0)
         * counterclockwise, or (1.0, -1.0) clockwise.
         *
         * What's the right multiplying factor to translate our abstract turn
         * magnitudes into reasonable linear speeds?  The answer can only be
         * determined empirically, and this constant stores that information.
         */
        private static final double TURN_MAGNITUDE_TO_LINEAR_SPEED = 0.05;

        /***
         * The tank drive we're supposed to be turning.
         */
        TankDriveInterface tank;

        /**
         * The forward-pointing vector that the robot had when it started the
         * match.
         */
        Vector2 initialRobotDirection;

        /**
         * The Gyro we use for getting our current heading.
         */
        private Gyro gyro;

        /***
         * This is the heading we ultimately want to be pointing toward.
         */
        Vector2 goalHeading;

        /**
         * Initializes this command.
         *
         * @param tank               The tank drive to turn.
         * @param gyro               The gyro to use for measuring our current
         *                           bearing.
         * @param initialRobotVector The vector that our robot started the
         *                           match with--that is, the vector
         *                           corresponding to a gyro angle of 0.
         */
        public TurnCommand(TankDriveInterface tank, Gyro gyro, Vector2 initialRobotVector) {
                this.tank = tank;
                this.gyro = gyro;
                this.initialRobotDirection = initialRobotVector.normalized();
                this.goalHeading = this.initialRobotDirection;  // By default, we don't turn.
        }

        /**
         * Normalizes the given angle so that it lies in the range [0, 360).
         */
        static private double normalizedAngle(double angleInDegrees) {
            angleInDegrees += Math.ceil(Math.abs(angleInDegrees) / 360) * 360;
            angleInDegrees %= 360;
            return angleInDegrees;
        }

        /***
         * Bring the command to a valid initial state.
         *
         *  Almost everything we calculate in this command is relative to the
         *  initial heading and initial gyro angle.
         */
        @Override
        public synchronized void start() {

                // At the time the robot began its life, its direction was
                // initialRobotDiriection and its gyro angle was 0.
            /*
                final double initialGyroAngleInRadians = 0;

                double currentGyroAngleInRadians = gyro.getAngle() * Constants.DEGREES_TO_RADIANS;
                Vector2 estimatedRobotDirectionAtStart = initialRobotDirection.rotatedBy(currentGyroAngleInRadians - initialGyroAngleInRadians);

                System.out.printf("TurnCommand.start(): current gyro angle is %.2f (%s)\n",
                                  gyro.getAngle(),
                                  estimatedRobotDirectionAtStart);*/
        };

        /***
         * Set the heading that we want to turn toward.
         * @param desiredHeading This vector is relative to the robot's
         *                        *starting vector*, which is assumed to point
         *                        forward.
         */
        public void setHeading(Vector2 desiredHeading) {
                goalHeading = desiredHeading.normalized();
        }

        /***
         * This function is called intermittently over and over until
         * isFinished() returns true.
         *
         * It does a tiny bit of work to turn us toward our target
         * incrementally.
         */
        @Override
        protected void execute() {

                // Calculate θ₀, the angle between our orientation at match
                // start time and the positive X axis.
                double initialAbsoluteAngleInRadians = Math.atan2(initialRobotDirection.y,
                                                                  initialRobotDirection.x);
                double initialAbsoluteAngleInDegrees = normalizedAngle(initialAbsoluteAngleInRadians * Constants.RADIANS_TO_DEGREES);

                // Use θ₀ to calculate θ₁, our current absolute angle.
                double currentAbsoluteAngleInDegrees = normalizedAngle(initialAbsoluteAngleInDegrees + gyro.getAngle());

                // Calculate θ, our desired absolute angle.
                double desiredAbsoluteAngleInRadians = Math.atan2(goalHeading.y,
                                                                  goalHeading.x);
                double desiredAbsoluteAngleInDegrees = normalizedAngle(desiredAbsoluteAngleInRadians * Constants.RADIANS_TO_DEGREES);


                // The difference between θ and θ₁ is how much we have to rotate.
                double degreesToTurn = desiredAbsoluteAngleInDegrees - currentAbsoluteAngleInDegrees;

                // Let's say our current absolute angle is 10 degrees and our
                // desired absolute angle is 340 degrees.  Which is a smarter
                // direction to turn, then?
                //
                // * (340 - 10), which normalizes to 330 degrees (counterclockwise), or
                // * (10 - 340), which normalizes to 30 degrees (clockwise)?
                //
                // Clearly the latter.

                double sign = Math.signum(degreesToTurn);
                degreesToTurn = sign * Math.min(normalizedAngle(degreesToTurn),
                                                normalizedAngle(-degreesToTurn));

                // Turn at the appropriate magnitude.
                //
                // You're probably wondering wondering what I mean!
                //
                // Suppose you have min, max, and current, which lies somewhere
                // between.  The linear interpolation formula to get the
                // parameter of interpolation, u, representing the percentage
                // of distance current is between min and max, is:
                //
                //        (current - min)
                //    u = ---------------
                //          (max - min)
                //
                //    Note that:
                //    * u = 0 when current is at min.
                //    * u = 1 when current is at max.
                //
                // Now consider our robot's turning angle:
                //   * current is degreesToTurn.
                //   * max is the maxTurningAngleDegrees.
                //   * min is the minTurningAngleDegrees.
                //
                // Let's calculate the "magnitude of our turn", which is 1 at
                // the max turning angle and 0 at the minimum.

                double degreesToTurnClipped = degreesToTurn;
                if (Math.abs(degreesToTurn) > MAX_TURNING_ANGLE_DEGREES) {
                        // Not allowed to turn faster than this.
                        degreesToTurnClipped = Math.signum(degreesToTurn) * MAX_TURNING_ANGLE_DEGREES;
                }

                // Angle sanity check.
                System.out.printf("TurnCommand.execute(): Initial[θ₀]: %.2f° | Current[θ₁]: %.2f° [gyro=%.2f°] | Target[θ]: %.2f° [%s] | Degrees to turn: %.2f (from %.2f)        \n",
                        initialAbsoluteAngleInDegrees,
                        currentAbsoluteAngleInDegrees,
                        gyro.getAngle(),
                        desiredAbsoluteAngleInDegrees,
                        goalHeading,
                        degreesToTurnClipped,
                        degreesToTurn);

                // 0 <= turningMagnitude <= 1.
                // As we get closer and closer to our desired goal, we should
                // turn more and more slowly.
                //
                // Warning: In the end, this may be *too* slow.  We
                // may need to increase epsilon and/or the
                // minTurningAngleDegrees.

                double turningMagnitude = (Math.abs(degreesToTurnClipped) - MIN_TURNING_ANGLE_DEGREES) / (MAX_TURNING_ANGLE_DEGREES - MIN_TURNING_ANGLE_DEGREES);
                double linearSpeed = turningMagnitude * TURN_MAGNITUDE_TO_LINEAR_SPEED;

                if (degreesToTurn < -Constants.EPSILON) {
                        // Turn left.
                        tank.tankDrive(-linearSpeed, linearSpeed);
                } else if (degreesToTurn > Constants.EPSILON) {
                        // Turn right.
                        tank.tankDrive(linearSpeed, -linearSpeed);
                }

                // Tank drive sanity check.
                System.out.printf("TurnCommand.execute(): degreesToTurn: %.4f° [magnitude=%.2f] | linearSpeed: %.2f \n",
                        degreesToTurnClipped,
                        turningMagnitude,
                        linearSpeed);

        }

        /***
         * Are we done turning?  Did we hit the vector we set out to hit?
         */
        @Override
        protected boolean isFinished() {

                Vector2 estimatedCurrentHeading = initialRobotDirection.rotatedBy(gyro.getAngle() * Constants.RADIANS_TO_DEGREES);
                double angleDifference = Math.acos(estimatedCurrentHeading.dot(goalHeading));

                if (Math.abs(angleDifference) < Constants.EPSILON) {
                        return true;
                } else {
                        return false;
                }
        }

        @Override
        public void startCommand() {
            start();
        }

        @Override
        public void executeCommand() {
            execute();
        }

        @Override
        public boolean isCommandFinished() {
            return isFinished();
        }
}
