#include <syscall.h>

int eq(int num1, int num2) {
  if(num1 == num2) {
    return 1;
  }
  return 0;
}

int gtr(int num1, int num2) {
  if(num2 > num1) {
    return 1;
  }
  return 0;
}

int gtreq(int num1, int num2) {
  if(num2 >= num1) {
    return 1;
  }
  return 0;
}

int or(int num1, int num2) {
  int num = 1;
  if(num1 == num2) {
    return num1;
  }
  return num;
}

int neg(int num) {
  return (num * -1);
}

int sub(int num1, int num2)
{
  return (num2 - num1); 
}

int add(int num1, int num2) {
  return (num1 + num2);
}

int mul(int num1, int num2) {
  return (num1 * num2);
}

int div(int num1, int num2) {
  int num = 0;
  if(num1 != 0) {
    num = (num2 / num1);
  }
  return num;
}

//Reads and input from standard in into a variable
int readint() {

  // For right now we are just returning a 0
  return 0;
}

//Writes an int to standard output
void writeint(int num) {
  char buf[20];
  char result[20] = "0\n";
  char *pos = buf;
  char *writeptr = result;
  int numWritten;
 
  // Handle negative numbers
  if (num < 0) {
    *writeptr++ = '-';
    num = -num;
  }
  
  if (num > 0) {
      
    // Build the number in reverse order
    while (num > 0) {
      *pos++ = (num % 10) + '0';
      num /= 10;
    }
    pos--;
    
    // Now we need to copy the results into the output buffer, reversed
    while (pos > buf) {
      *writeptr++ = *pos--;
    }
    *writeptr++ = *pos;
    *writeptr++ = 10;
    *writeptr++ = 0;
  }
  
  write(1, result, (writeptr - result) - 1);
  
}
