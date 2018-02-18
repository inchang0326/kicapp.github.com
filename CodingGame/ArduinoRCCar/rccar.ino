#include <QueueArray.h>
#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
#include <avr/power.h>
#endif

#define NEOPIXEL_PIN 7
#define BUZZER_PIN 8
#define FORWARD_BACKWARD_TIME 100
#define LEFT_RIGHT_TIME 75
#define AA 262 // 도 
#define BB 294 // 레 
#define CC 330 // 미 
#define DD 349 // 파 
#define EE 392 // 솔 
#define FF 440 // 라 
#define GG 494 // 시
#define HH 554 // 도
int answerNotes[4] = { CC, EE, GG, HH };

// Connect HC-06
SoftwareSerial BTSerial(12, 13);

// NeoPixel config
// IMPORTANT: To reduce NeoPixel burnout risk, add 1000 uF capacitor across
// pixel power leads, add 300 - 500 Ohm resistor on first pixel's data input
// and minimize distance between Arduino and first pixel.  Avoid connecting
// on a live circuit...if you must, connect GND first.
Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, NEOPIXEL_PIN, NEO_GRB + NEO_KHZ800);
QueueArray<String> queue;

String input;
char c_input[100];
char *ptr;
int A_1A = 6;
int A_1B = 11;
int B_1A = 3;
int B_1B = 5;
int speed = 255;

void setup()
{
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  #if defined (__AVR_ATtiny85__)
  if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  #endif
  // End of trinket special code

  //L298 모터드라이버의 핀들을 출력으로 변경합니다.
  pinMode(A_1A, OUTPUT);
  pinMode(A_1B, OUTPUT);
  pinMode(B_1A, OUTPUT);
  pinMode(B_1B, OUTPUT);
  digitalWrite(A_1A, LOW);
  digitalWrite(A_1B, LOW);
  digitalWrite(B_1A, LOW);
  digitalWrite(B_1B, LOW);


  // Initialize all pixels to 'off'
  strip.begin();
  strip.show();

  Serial.begin(9600);
  Serial.println("Hello!");

  // set the data rate for the BT port
  BTSerial.begin(9600);
  BTSerial.setTimeout(10);
}

void loop()
{

  // BT –> Data –> Serial
  if (BTSerial.available()) {
    // 명령어 분할
    input = BTSerial.readString();
    input.toCharArray(c_input, sizeof(c_input));
    ptr = strtok(c_input, "\n ");
    // 모든 명령어 실행
    while (ptr != NULL) {
      Serial.println(ptr);
      if (strcmp(ptr, "앞") == 0)
      {
        Serial.println("앞 response");
        for(int i=0; i < FORWARD_BACKWARD_TIME; i++) {
          Serial.println("test");
          forward();
        }
        stop();
      }
      else if (strcmp(ptr, "뒤") == 0)
      {
        Serial.println("뒤 response");
        for(int i=0; i < FORWARD_BACKWARD_TIME; i++) {
          Serial.println("test");
          backward();
        }
        stop();
      }
      else if (strcmp(ptr, "오른쪽") == 0)
      {
        Serial.println("오른쪽 response");
        for(int i=0; i < LEFT_RIGHT_TIME; i++) {
          Serial.println("test");
          right();
        }
        stop();
      }
      else if (strcmp(ptr, "왼쪽") == 0)
      {
        Serial.println("왼쪽 response");
        for(int i=0; i < LEFT_RIGHT_TIME; i++) {
          Serial.println("test");
          left();
        }
        stop();
      }
      else if (strcmp(ptr, "빨강") == 0)
      {
        Serial.println("빨강 response");
        lightRed();
      }
      else if (strcmp(ptr, "초록") == 0)
      {
        Serial.println("초록 response");
        lightGreen();
      }
      else if (strcmp(ptr, "노랑") == 0)
      {
        Serial.println("노랑 response");
        lightYellow();
      }
      else if (strcmp(ptr, "파랑") == 0)
      {
        Serial.println("파랑 response");
        lightBlue();
      }
      else if (strcmp(ptr, "경적") == 0)
      {
        Serial.println("경적 response");
        tone(BUZZER_PIN, 500, 1000);
      }
      else if (strcmp(ptr, "끄기") == 0)
      {
        Serial.println("끄기 response");
        cancleLight();
        stop();
      }
      else if (strcmp(ptr, "정답") == 0)
      {
        Serial.println("정답 response");
        for (int i = 0; i < 4; i++) {
          tone (BUZZER_PIN, answerNotes[i], 200);
          delay (100);
        }
      }
      else
      {
        BTSerial.write("flag");
      }
      ptr = strtok(NULL, "\n ");
    }
  }
}

void colorWipe(uint32_t c, uint8_t wait) {
  for (uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, c);
    strip.show();
    delay(wait);
  }
}

void lightRed() {
  colorWipe(strip.Color(255, 0, 0), 0);
}

void lightYellow() {
  colorWipe(strip.Color(255, 255, 0), 0);
}

void lightGreen() {
  colorWipe(strip.Color(0, 255, 0), 0);
}

void lightBlue() {
  colorWipe(strip.Color(0, 0, 255), 0);
}

void cancleLight() {
  colorWipe(strip.Color(0, 0, 0), 0);
}

void forward()
{
  //모터A
  analogWrite(A_1A, speed);
  analogWrite(A_1B, 0);
  //모터B
  analogWrite(B_1A, speed);
  analogWrite(B_1B, 0);
}

void backward()
{
  //모터A
  analogWrite(A_1A, 0);
  analogWrite(A_1B, speed);
  //모터B
  analogWrite(B_1A, 0);
  analogWrite(B_1B, speed); 
}

void left()
{
  //모터A
  analogWrite(A_1A, speed);
  analogWrite(A_1B, 0);
  //모터B
  analogWrite(B_1A, 0);
  analogWrite(B_1B, speed);
}

void right() 
{
  //모터A
  analogWrite(A_1A, 0);
  analogWrite(A_1B, speed);
  //모터B
  analogWrite(B_1A, speed);
  analogWrite(B_1B, 0);
}

void stop() 
{
  //모터A
  analogWrite(A_1A, 0);
  analogWrite(A_1B, 0);
  //모터B
  analogWrite(B_1A, 0);
  analogWrite(B_1B, 0);
}

