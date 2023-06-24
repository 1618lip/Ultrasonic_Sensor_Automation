/*
Ultrasonic Sensor Controller 
Created by Philip Pincencia
June 23, 2023
*/

const int redLED = 3; // red LED
const int blueLED = 4; // blue LED
const int triggerPin = 8; // trigger pin for the sensor
const int echoPin = 9; // echo pin for the sensor
double prevDist = 50; // initialize starting distance to be 50 cm
String s; // the <control> 

/*
Get the distance from the ultrasonic sensor
*/
double distance(int trigger, int echo) {
  digitalWrite(trigger, LOW);
  delayMicroseconds(2);
  digitalWrite(trigger, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigger, LOW);

  double timeTaken = pulseIn(echo, HIGH);
  double dist= timeTaken*0.034/2; // 0.034 is the speed of sound
  // maximum distance is 50 cm
  if (dist > 50) {
    dist = 50;
  }
  return dist;
}

/*
Determine whether the distance is decreasing or increasing
*/
void control() {
  double currentDist = distance(triggerPin, echoPin);
  // if distance is decreasing, send '0'. Otherwise, '1'
  if (currentDist < prevDist - 1.50) {
    Serial.println(0);
  }
  else if (currentDist > prevDist + 1.50) {
    Serial.println(1);
  }
  prevDist = currentDist;
}

void setup() {
  // set baud rate tobe 9600
  Serial.begin(9600);
  
  // define I/O pins
  pinMode(redLED, OUTPUT);
  pinMode(blueLED, OUTPUT);
  pinMode(triggerPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

void loop() {
  // if there is a message from the Java program
  if (Serial.available()) {
    s = Serial.readString();
  }

  // Red LED ONLY means volume
  if (s.equals("volume")) {
    control();
    digitalWrite(redLED, HIGH);
    digitalWrite(blueLED, LOW);
    delay(50);
  }
  // Blue LED ONLY means scroll
  else if (s.equals("scroll")) {
    control();
    digitalWrite(redLED, LOW);
    digitalWrite(blueLED, HIGH);
    delay(400);    
  }
  // Both LED means switch
  else if (s.equals("switch")) {
    control();
    digitalWrite(redLED, HIGH);
    digitalWrite(blueLED, HIGH);
    delay(750);
  }
  // if it's 'end', turn off and reset <s> to be empty
  else {
    s = "";
    digitalWrite(redLED, LOW);
    digitalWrite(blueLED, LOW);
  }
}
