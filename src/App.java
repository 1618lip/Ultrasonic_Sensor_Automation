/**
 * Ultrasonic Sensor Automation
 * Created by Philip Pincencia
 * June 23, 2023
 * 
 * Control Volume, Page Scroll, and Tab Switching
 * using the ultrasonic distance sensor. 
 */

import com.fazecast.jSerialComm.*; // Serial Communication library
import java.awt.AWTException;
import java.awt.Robot; // To automate key movement
import java.awt.event.KeyEvent; // KeyCodes to press Keys
import java.util.Scanner; // For getting input from user

/**
 * This class contains two helper methods and a main method
 * Keep in mind: Do not compile the program, just run it. 
 */
public class App {
    // COMM settings
    private static final String COMMPORT = "COM6";
    private static final int BAUDRATE = 9600;
    private static final int newDATABITS = 8;
    private static final int SLEEPTIME = 1000; // milliseconds
    
    // virtual keys (change this according to your computer's settings)
    private static final int VOLDOWN = KeyEvent.VK_F10;
    private static final int VOLUP = KeyEvent.VK_F11;
    private static final int SCROLLDOWN = KeyEvent.VK_PAGE_DOWN;
    private static final int SCROLLUP = KeyEvent.VK_PAGE_UP;

    /**
     * Run the main method
     * 
     * @param args no argument passed
     * @throws Exception if port not available
     */
    public static void main(String[] args) throws Exception {
        // COM6 is Arduino Mega 2560
        var serialPort = SerialPort.getCommPort(COMMPORT);
        // set up the settings for the COMMPORT
        serialPort.setComPortParameters(BAUDRATE, newDATABITS, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        // exit if COMMPORT doesn't exist 
        if (!serialPort.openPort()) {
            System.out.println("\nCOM Port not available!");
            return;
        }
        while (true) {
            // get what to control
            String control = getControl();
            // remove the previous thing control 
            serialPort.removeDataListener();
            serialPort.getOutputStream().write(control.getBytes());
            Thread.sleep(SLEEPTIME);

            // Create a listener for receiving serial data
            serialPort.addDataListener(new SerialPortDataListener() {

                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        byte[] newData = new byte[serialPort.bytesAvailable()];
                        // read bytes from serial until it reaches the determined length
                        serialPort.readBytes(newData, newData.length);
                        // Convert the received bytes to integers
                        for (byte b : newData) {
                            if (Character.isDigit((char) b)) {
                                int intValue = Character.getNumericValue((char) b);
                                keyPress(intValue, control);
                            }
                        }
                    }
                }
            });
        }
    }
        
    /**
     * Get what aspect the user wants to control
     * @return volume or brightness
     */
    public static String getControl() {
      // okay to be exposed
        Scanner user = new Scanner(System.in);
        String toReturn = "";
        while (true) {
            System.out.print(
                "Control Volume, Scroll, or Switch? (Enter \"end\" to deactivate)\n Answer: "
            );
            // ask the user what they want to control
            toReturn = user.nextLine().toLowerCase();
            // break if user's answer is valid
            if (toReturn.equals("volume") || 
                toReturn.equals("scroll") || toReturn.equals("end") 
                || toReturn.equals("switch")) {
                System.out.println("Control " + toReturn);
                break;
            }
            // Repeat until user's answer is valid
            System.out.println(
                "Answer must be either \'volume\', \'scroll\', or \'switch\'\n"
            );
        }
        return toReturn;
    }
    

    /**
     * Virtually presses the keys 
     * according to the value and what is 
     * to be controlled
     * @param value 0 or 1
     * @param control the thing we want to control
     */
    public static void keyPress(int value, String control) {
        try {
            Robot robot = new Robot();
            if (control.equals("volume")) {
                // 0 for lower volume, 1 for increase volume
                if (value == 0) {
                    robot.keyPress(VOLDOWN);
                    
                }
                else {
                    robot.keyPress(VOLUP);
                }
            }
            else if (control.equals("scroll")) {
                // 0 for scroll down, 1 for scroll up
                if (value == 0) {
                    robot.keyPress(SCROLLDOWN);
                }
                else {
                    robot.keyPress(SCROLLUP);
                }
            }
            else if (control.equals("switch")) {
                // 0 for switch to left tab, 1 for switch to the right tab
                if (value == 0) {
                    // These are default keys for switching tabs
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(KeyEvent.VK_TAB);
                    // works fine without releasing the keys
                    /* 
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    */
                }
                else {
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_TAB);
                    //robot.keyRelease(KeyEvent.VK_CONTROL);
                    //robot.keyRelease(KeyEvent.VK_TAB);
                }
            }
        } 
        catch (AWTException e) {
          e.printStackTrace();
        }
    }
}
