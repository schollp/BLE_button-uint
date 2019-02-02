
#define VIBPIN 5        // Pin on Arduino board to which the Vibration Actor is connected
#define FSR1 1          // Pin on Arduino board to which the FSR 0 is connected
#define FSR2 2
#define FSR3 3
#define FSR4 4
#define FSR5 5
#define FSR6 6
#define FSR7 7

#define READ_RATE 50     // How often the serial link is read, in milliseconds
#define VIB_RATE 200      // Vibration Interval in milliseconds


byte cmd = 49;               // Stores the next byte of incoming data, which is a "command" to do something
byte prev_cmd = 48;          // Stores the previous cmd in byte
char  fsrReading[] = {'0','0','0','0','0','0','0'};        // the analog reading from the FSR resistor divider, array

uint16_t S1 = 1;
uint16_t S2 = 2;
uint16_t S3 = 3;
uint16_t S4 = 4;
uint16_t S5 = 5;
uint16_t S6 = 255;
uint16_t S7 = 256+255;

//line[1]=1;
//array[1]=(S8 >> 8);

byte ledPins[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};       // an array of pin numbers to which LEDs are attached

byte message[]={S1,S2,S3,S4,S5,S6,S7};

#include <AltSoftSerial.h>
AltSoftSerial BTserial; 

void setup() {

  pinMode(VIBPIN, OUTPUT);
  digitalWrite(VIBPIN, LOW);
  Serial.begin(9600);
  BTserial.begin(9600);  
}



void loop() {

ledPins[0]=S1;
ledPins[2]=S2;
ledPins[4]=S3;
ledPins[6]=S4;
ledPins[8]=S5;
ledPins[10]=S6;
ledPins[12]=S7;


ledPins[12]=(S7 >> 8);
ledPins[13]=S7 & 0xff;




ledPins[2] = 2;
  
  if(BTserial.available()){
   
   if(prev_cmd!=51){
      prev_cmd = cmd;
    }
    cmd = BTserial.read();         // read it and store it in 'cmd'   
  }


  
  switch (cmd) {
    case 51:                    // cmd=3 -> Vibration
      digitalWrite(VIBPIN, HIGH);  
      delay(VIB_RATE);                       
      digitalWrite(VIBPIN, LOW);
      cmd = prev_cmd;
      break;
    case 48:                     // cmd=0 -> Stop Measurement / idle
      break;
      
    case 49:                     // cmd=1 -> Start Measurement
      readFSR(); break;     
    default:
        break;            // do nothing
  } 
  
  delay(READ_RATE);                    // wait READ_RATE for next reading
}




void readFSR(){
  int rnd = random(0,100);

//Serial.print('S1');
//Serial.println(S1);

BTserial.write(ledPins, sizeof(ledPins));
 
//BTserial.write(message, sizeof(message));

 
  return;
}
  
