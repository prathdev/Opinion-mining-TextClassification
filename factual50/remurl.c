#include<stdio.h>
#include<stdlib.h>
#include<string.h>

int main(int argc, char *argv[])
{
int i;
for(i=1;i<=50;i++)
printf("mv %d.txt.tmp %d.txt\n",i,i);
}
