/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
// * Class T7147Robot
// *
// *  TTTTT  77777     1        4    77777
// *    T        7    11       44        7
// *    T       7      1      4 4       7
// *    T      7       1     4  4      7
// *    T      7       1     44444     7
// *    T      7       1        4      7
// *    T      7      111       4      7
// *
// * Edit Date: March 12, 2018
//

// IO list
// -------
// PWM1 Left Drive Motor 1
// PWM2 Left Drive Motor 2
// PWM3 Right Drive Motor 1
// PWM4 Right Drive Motor 2
// PWM5 
// PWM6 
// PWM7
// PWM8
// PWM9
// PWM10
//
// Relays
// R1 
// R2 
// R3 
// R4 
// R5 
// R6
// R7
// R8 
//
// Solenoids
// S1 
// S2 
// S3 
// S4  
// S5 
// S6
// S7
// S8
//
// Digital IO
// DIO1 
// DIO2 
// DIO3 
// DIO4 
// DIO5 
// DIO6 
// DIO7 
// DIO8 
// DIO9 
// DIO10 
// DIO11 
// DIO12 
// DIO13 
// DIO14 
//
// Analog IO
// AIO1 Gyro
// AIO2 
// AIO3 
// AIO4 
// AIO5 
// AIO6 
// AIO7 
// AIO8 
//
// I2C IO - Unused
//
// CAN Bus
// Board ID 1 
// Board ID 2 
// Board ID 3 
//
//
// Operator Interface
// USB port1 Driver joystick
// USB port2 
// USB port3 
// USB port4 
//
// Joystick1 Driver
// X-axis Unused in tank drive
// Y-axis Right motor forward of backward
// Z-axis 
// Button 1 
// Button 2
// Button 3 
// Button 4
// Button 5
// Button 6
// Button 7
// Button 8
// Button 9
// Button 10 
// Button 11 
//
// Joystick 2 Unused
// X-axis 
// Y-axis 
// Z-axis 
// Button 1 
// Button 2 
// Button 3 
// Button 4 
// Button 5 
// Button 6 
// Button 7 
// Button 8 
// Button 9 
// Button 10 
// Button 11 
//

package org.usfirst.frc.team7147.robot;
import edu.wpi.first.wpilibj.IterativeRobot;

//import java.lang.Math;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.AnalogPotentiometer;
//import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.interfaces.Accelerometer;
//import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.Encoder;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	//private static final String kDefaultAuto = "Default";
	//private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
//	private SendableChooser<String> m_chooser = new SendableChooser<>();

	public static SpeedController leftMotor1;
	public static SpeedController leftMotor2;
	public static SpeedController rightMotor1;
    public static SpeedController rightMotor2;

    private final int kLeftMotor1PWMChannel  = 0;     // PWM channel numbers
    private final int kLeftMotor2PWMChannel  = 1;
    private final int kRightMotor1PWMChannel = 2;
    private final int kRightMotor2PWMChannel = 3;

    static Joystick leftStick;
	
   
    DriverStation ds;
	
    public static ADXRS450_Gyro gyro;
    
    public static Timer timer;								// Timer for autonomous mode.

	// Team 7147 to set these variables as needed for autonomous more.
    
	public final double autonomouseDriveTime = 4.0;			// Autonomous drive duration is 2 seconds.
	public final double autonomousMotorSpeed = 0.3;			// Autonomous driving motor speed.
	public final double speedReduction = 0.8;				// Slow a motor by 20%.
	public final double gyroTolerance = 0.5;				// Tolerance is +/- 1/2 degree.
	double robotPos = 0.0;
	String gameData;

	
	void DriveStraight () {
			// Get the gyro value and decide if we need to drive straight, turn left or turn right.
			if(Robot.gyro.getAngle() > gyroTolerance) {		//Too far to the right, so turn left
				setMotors (autonomousMotorSpeed * speedReduction, -autonomousMotorSpeed);
			} else if(Robot.gyro.getAngle() < -gyroTolerance) { //Too far to the left, so turn to the right
				setMotors (autonomousMotorSpeed, -autonomousMotorSpeed  * speedReduction);
			} else {											//Must be within tolerance, do nothing.
				setMotors (autonomousMotorSpeed, -autonomousMotorSpeed);;
			}
	}
	void TurnRight() {
		
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//m_chooser.addDefault("Default Auto", kDefaultAuto);
		//m_chooser.addObject("My Auto", kCustomAuto);
		//SmartDashboard.putData("Auto choices", m_chooser);
	
		leftStick = new Joystick(0);
		
		ds = DriverStation.getInstance();
		
		gyro = new ADXRS450_Gyro();
        gyro.calibrate();

        leftMotor1 = new Spark(kLeftMotor1PWMChannel);
        leftMotor2 = new Spark(kLeftMotor2PWMChannel);
        
        rightMotor1 = new Spark(kRightMotor1PWMChannel);
        rightMotor2 = new Spark(kRightMotor2PWMChannel);
        
        timer = new Timer();
        timer.start();   // Start the timer
        SmartDashboard.putNumber("RobotPos", robotPos);

        //autonomousStart = false;
        
	} // public void robotInit()

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		timer.reset();                          //Set it to zero.
        timer.start();
        
        Robot.gyro.reset();

        setMotors (autonomousMotorSpeed, autonomousMotorSpeed);	// Start the motors in autonomous.
        gameData = DriverStation.getInstance().getGameSpecificMessage();
    } // public void autonomousInit()


	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
//		public void autonomousPeriodic() {switch (m_autoSelected) {
//			case kCustomAuto:
//				// Put custom auto code here
//				break;
//			case kDefaultAuto:
//			default:
//				// Put default auto code here
//				break;
//		}
		robotPos = SmartDashboard.getNumber("Robot Position", 0);
		
		if (timer.get() > autonomouseDriveTime) {
			setMotors (0.0, 0.0);				// Stop the motors! All done!
		} 
		if (robotPos == 0) {
			if(gameData == "LLL" || gameData == "LRL" || gameData == "LRR" || gameData == "LLR") {
				if(timer.get() < 1) {
					setMotors(.4, -.4);
				}
				if(timer.get() > 1 && timer.get() < 2) {
					setMotors(.5, .5);
				}
				if(timer.get() < 3 && timer.get() > 2) {
					setMotors(-.4, .4);
				}
				if(timer.get() > 3 && timer.get() < 4) {
					setMotors(.65, .65);
				}
			}
		}
		if(robotPos == 2) {
			if(gameData == "RRR" || gameData == "RRL" || gameData == "RLR" || gameData == "RLL") {
				if(timer.get() < 1) {
					setMotors(-.4, .4);
				}
				if(timer.get() > 1 && timer.get() < 2) {
					setMotors(.5, .5);
				}
				if(timer.get() < 3 && timer.get() > 2) {
					setMotors(.4, -.4);
				}
				if(timer.get() > 3 && timer.get() < 4) {
					setMotors(.65, .65);
				}
			}
		}
		else {								// Drive using the gyro.
			DriveStraight();
		}

	} // public void autonomousPeriodic() {

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
    	double deadzone = 0.025;
    	double l, ll;
    	double r, rr;
    	double speedscale = 0.5;
    	boolean turbo = false;

		l = leftStick.getRawAxis(1);		// Get the left joy stick value.
    	r = leftStick.getRawAxis(3);		// Get the right joy stick value.
    	
    	turbo = leftStick.getRawButton(8);
    	if(turbo)
    	{
    		speedscale = 1.0;
    	}
    	
    	
    	if ((l < -deadzone) || (l > deadzone))	// Is the value in the dead zone?
    		ll = l * l;						// No. So Square it to make it more linear.
    	else
    		ll = 0;							// Yes, it is in the dead zone make it zero.
    	if (l < 0.0)
    		ll = -ll;				// Adjust the sign.
    	
    	if ((r < -deadzone) || (r > deadzone))	// Is the value in the dead zone?
			rr = r * r;						// No. So Square it to make it more linear.
    	else
			rr = 0;							// Yes, it is in the dead zone make it zero.
    	if (r < 0.0)
    		rr = -rr;
    	
    	setMotors (-ll*speedscale, speedscale*rr);					// Set the motor speeds.
    	System.out.println("ll" + ll + "rr" + rr);
    	} // public void teleopPeriodic()


	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	} // public void testPeriodic() {

	
	void setMotors (double l, double r) {
		Robot.leftMotor1.set (l);				// Set the left motor speed.
		Robot.leftMotor2.set (l);
		Robot.rightMotor1.set(r);				// Set the right motor speed.
		Robot.rightMotor2.set(r);
	} // public void setMotors (l, r) {

} // public class Robot extends IterativeRobot
