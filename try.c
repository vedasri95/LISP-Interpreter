#include <unistd.h>
#include <fcntl.h>
#include<stdlib.h>
#include <stdio.h>
#include <string.h>
 
int main(void)
{
	 
  int fd, sz;
  char *c = (char *) calloc(5, sizeof(char));
 
  fd = open("context.txt", O_RDONLY);
  
  
  if (fd < 0) { perror("r1"); exit(1); }
 
  sz = read(fd, c, 1);
  printf("%d bytes  were read.\n", fd, sz);
  c[sz] = '\0';
  printf("Those bytes are as follows: % s\n", c);

  lseek(fd,0);
	if(strcmp(c,"1") == 0){
		fd_main = open("privateFile.txt",O_RDONLY);
		
		while(1){
			sz = read(fd, c, 1);
			lseek(fd,0);
			c[sz] = '\0';
			if(strcmp(c,"0")){
				close(fd);
				break;
			}
		}
		
	}
  
	
	return 0;
}