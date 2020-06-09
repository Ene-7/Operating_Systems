#include <stdio.h>
#include <stdlib.h>
int main() 
{
	int processID = fork();
	
	if(processID == 0) {
		printf("I am the child with PID [%d] and my parent has PPID [%d].\n", getpid(), getppid());
		sleep(1);
	}
	
	else {
		printf("I am the parent and my id is [%d]\n", getpid());
		sleep(30);
	}
    return 0;
}
