## Folder Structure

The workspace contains several folders:
- `src`: Contains `App.java` and `SensorArduinoCode.ino`
- `lib`: Contains the `jSerialComm.jar` library used by `App.java`
- `bin`: Contains the generated compiled output files

## About the Project:
I was fascinated by the idea of controlling mundane tasks on the computer without even touching it. Take for example, controlling the computer's volume, I thought it would be really cool to control the volume without touching the computer. To do that, I use one of the most recognizable sensors, which is the ultrasonic sensor. 

## How it works: 
* When `App.java` is run, it will ask the user what they want to control: Volume, Page Scroll, or Tab Switching.
  > **Keywords:**
  > * "Volume" = Control volume 
  > * "Scroll" = Control Page Scrolling
  > * "Switch" = Switch between Tabs
  > * "End" = End any control
  > > **Note:** Capitalization does NOT matter 
* If the user does not enter any of those keywords, `App.java` will keep on asking the user until the user gives one of those four keywords.
* Depending on the keyword, the state of the LEDs would change
  > * "Volume" = RED on
  > * "Scroll" = BLUE on
  > * "Switch" = RED & BLUE on
  > * "End" = RED & BLUE off
* When there's a change in distance, Arduino will send either 0 (denoting decreasing distance) or 1 (denoting increasing distance) to `App.java`. The code I've written here is using this rule:
  > * **"Volume":** decrease distance(0) &rarr; decrease volume, increase distance(1) &rarr; increase volume
  > * **"Scroll":** decrease distance(0) &rarr; scroll down, increase distance(1) &rarr; scroll up
  > * **"Switch":** decrease distance(0) &rarr; switch to left tab, increase distance(1) &rarr; switch to right tab
  > > Feel free to change these settings as you wish
