/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder; //!!!
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Encoder _encoder; //!!!
  private WPI_TalonSRX _talon;
  private Joystick _joystick;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //!!! BEGIN
    // _encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    // _encoder.setMaxPeriod(0.1);
    // _encoder.setMinRate(10);
    // _encoder.setDistancePerPulse(1.0/48);
    // _encoder.setSamplesToAverage(16);
    // _encoder.reset();

    try {
      _joystick = new Joystick(0);
    }
    catch(Exception ex) {
      System.out.println("Error creating joystick");
    }

    try {
      _talon = new WPI_TalonSRX(1);
    }
    catch(Exception ex) {
      System.out.println("Error creating talon");
    }

    if(_talon != null) {
      _talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
      _talon.setSelectedSensorPosition(0);
    }
    //!!! END
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

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
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
    _talon.setSelectedSensorPosition(0);
  }
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //!!! BEGIN
    if(_joystick != null && _talon != null) {
      double value = _joystick.getRawAxis(1);
      SmartDashboard.putNumber("value", value);
      _talon.set(ControlMode.PercentOutput, value);

      int velocity = _talon.getSelectedSensorVelocity();
      SmartDashboard.putNumber("velocity", velocity);
      String direction;
      if(velocity > 0)
        direction = "forward";
      else if(velocity < 0)
        direction = "back";
      else
       direction = "stopped";
      SmartDashboard.putString("direction", direction);
      SmartDashboard.putNumber("position", _talon.getSelectedSensorPosition());
      SmartDashboard.putNumber("RPM", velocity * 600/4096);

      StickyFaults stickyFaults = new StickyFaults();
      _talon.getStickyFaults(stickyFaults);
      Faults faults = new Faults();
      _talon.getFaults(faults);
    }
    
    // int count = _encoder.get();
    // SmartDashboard.putNumber("count", count);

    // double raw = _encoder.getRaw();
    // SmartDashboard.putNumber("raw", raw);
    
    // double distance = _encoder.getDistance();
    // SmartDashboard.putNumber("distance", distance);
    
    // double period = _encoder.getPeriod(); //deprecated in favor of getRate()
    // SmartDashboard.putNumber("period", period);
    
    // double rate = _encoder.getRate();
    // SmartDashboard.putNumber("rate", rate);
    
    // boolean direction = _encoder.getDirection();
    // SmartDashboard.putBoolean("direction", direction);
    
    // boolean stopped = _encoder.getStopped();
    // SmartDashboard.putBoolean("stopped", stopped);
    //!!! END
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
