/*
 * Program Name:
 * Alliance:
 * Starting position
 * Functions of the program:
 *  - STEP1 =   gets the foundation into the build site
 *  - STEP2
 *
 *
 *
 */

package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.HardwareProfile;
import org.firstinspires.ftc.teamcode.Libs.DriveMecanum;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleoOpRuntoP", group = "Comp")
//@Disabled

public class TeloOpRuntoP extends LinearOpMode {

    private final static HardwareProfile robot = new HardwareProfile();
    private LinearOpMode opMode = this;

    public TeloOpRuntoP(){

    }   // end of BrokenBotTS constructor

    public void runOpMode() {
        double startTime;
        double timeElapsed;
        double v1, v2, v3, v4, robotAngle, powerLevel = 1;
        double modePower = 1;
        double theta = 0;
        int mArm = 0;
        int mBase = 0;
        double dpadup, dpaddown, dpadleft, dpadright;
        double r;
        double rightX, rightY;
        double rightA, RightB;
        double wristPosition = 0.5;
        double spinpower = 0;
        boolean spintoggle = false;
        boolean fieldCentric = true;
        int targetPosition = 0;
        double linearServoPosition = 0.5;

        telemetry.addData("Robot State = ", "NOT READY");
        telemetry.update();

        /*
         * Setup the initial state of the robot
         */
        robot.init(hardwareMap);
        robot.motorBase.setTargetPosition(0);
        robot.motorArm.setTargetPosition(0);
        robot.motorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        /*
         * Initialize the drive class
         */
        DriveMecanum drive = new DriveMecanum(robot, opMode);

        /*
         * Calibrate / initialize the gyro sensor
         */

//        robot.servoGrab.setPosition(0);
//        robot.servoGrab.setPosition(0.5);

        telemetry.addData("Z Value = ", drive.getZAngle());
        telemetry.addData("Greetings = ", "HOME CHICKEN");
        telemetry.addData("Robot state = ", "INITIALIZED");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            /*
             * Mecanum Drive Control section
             */
            if (fieldCentric) {             // verify that the user hasn't disabled field centric drive
//                theta = robot.imu.getAngularOrientation().firstAngle;
            } else {
//                theta = 0;      // do not adjust for the angular position of the robot
            }
            robotAngle = Math.atan2(gamepad1.left_stick_y, (-gamepad1.left_stick_x)) - Math.PI / 4;
            rightX = gamepad1.right_stick_x;
            rightY = -gamepad1.right_stick_y;
            r = -Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y);
            v1 = (r * Math.cos(robotAngle - Math.toRadians(theta)) + rightX + rightY) * powerLevel;
            v2 = (r * Math.sin(robotAngle - Math.toRadians(theta)) - rightX + rightY) * powerLevel;
            v3 = (r * Math.sin(robotAngle - Math.toRadians(theta)) + rightX + rightY) * powerLevel;
            v4 = (r * Math.cos(robotAngle - Math.toRadians(theta)) - rightX + rightY) * powerLevel;


            //  robot.servoLinear.setPosition(linearServoPosition);

            robot.motorLF.setPower(v1 * modePower);
            robot.motorRF.setPower(v2 * modePower);
            robot.motorLR.setPower(v3 * modePower);
            robot.motorRR.setPower(v4 * modePower);

            if (gamepad1.dpad_left) {
                mBase = mBase + 40;
            }
            if (gamepad1.dpad_right) {
                mBase = mBase - 40;
            }
            if (mBase > 0 ){
                mBase = 0;
            }

            robot.motorBase.setPower(.80);
            robot.motorBase.setTargetPosition(mBase);


        if (gamepad1.dpad_down) {
            mArm = mArm - 20;
        }
        if (gamepad1.dpad_up) {
            mArm = mArm + 20;
        }
        robot.motorArm.setPower(0.7);
        robot.motorArm.setTargetPosition(mArm);

        if (gamepad1.right_trigger > 0) {
            wristPosition = 0;
            if (wristPosition > 1) wristPosition = 1;
        } else if (gamepad1.left_trigger > 0) {
            wristPosition = 1;
            if (wristPosition < 0) wristPosition = 0;
        } else wristPosition = 0.5;
        robot.servoWrist.setPosition(wristPosition);


        if (!gamepad1.x ^ gamepad1.b) {
            spintoggle = true;
        }

        if (gamepad1.x && spintoggle) {
            spintoggle = false;
            if (spinpower == 0) {
                spinpower = .3;
            } else
                spinpower = spinpower + .1;
        }
        if (gamepad1.b && spintoggle) {
            spintoggle = false;
            if (spinpower == 0) {
                spinpower = -.3;
            } else
                spinpower = spinpower - .1;

        }
        ;
        if (gamepad1.a) {
            spinpower = 0;
        }
        if (gamepad1.y) {
            mArm = 700;
            mBase = -950;
        }
            if (gamepad1.right_bumper) {
                mArm = 650;
                mBase = 0;
            }
            if (gamepad1.left_bumper) {
                mArm = 280;
                mBase = 0;
            }
        robot.motorTurnTable.setPower(spinpower);
/*
            if(gamepad1.dpad_up || gamepad2.dpad_up){   // open the claw
                robot.servoGrab.setPosition(0);
            } else if(gamepad1.dpad_down || gamepad2.dpad_down){  // close the claw
                robot.servoGrab.setPosition(0.4);
            }
*/

        //    telemetry.addData("Servo Position = ", robot.servoLinear.getPosition());
        telemetry.addData("motorLF = ", robot.motorLF.getCurrentPosition());
        telemetry.addData("motorLR = ", robot.motorLR.getCurrentPosition());
        telemetry.addData("motorRF = ", robot.motorRF.getCurrentPosition());
        telemetry.addData("motorRR = ", robot.motorRR.getCurrentPosition());
        telemetry.addData("motorArm = ", robot.motorArm.getCurrentPosition());
        telemetry.addData("motorBase = ", robot.motorBase.getCurrentPosition());
        telemetry.addData("spinpower = ", spinpower);
        telemetry.addData("Arm Setpoint = ", mArm);
        telemetry.addData("Base Setpoint = ", mBase);
        //    telemetry.addData("Shooter Encoder = ", robot.motorShooter.getCurrentPosition());
        //telemetry.addData("IMU Value: ", theta);
        telemetry.update();


        }   // end of while opModeIsActive()

    }   // end of runOpMode method
}   // end of Linear op mode
