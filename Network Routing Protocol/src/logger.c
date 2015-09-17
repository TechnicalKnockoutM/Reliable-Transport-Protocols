/**
 * @logger
 * @author  Swetank Kumar Saha <swetankk@buffalo.edu>
 * @version 1.0
 *
 * @section LICENSE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details at
 * http://www.gnu.org/copyleft/gpl.html
 *
 * @section DESCRIPTION
 *
 * Contains logging functions to be used by CSE489/589 students for PA3.
 */
#include <stdlib.h>
#include <strings.h>
#include <stdio.h>
#include <stdarg.h>
#include <time.h>

#include "../include/global.h"
#include "../include/logger.h"

char LOGFILE[FILEPATH_LEN];
char DUMPFILE[FILEPATH_LEN];
void cse4589_init_log()
{
	/*Get hostname and build file paths*/
        FILE* fp;
        fp = popen("echo $HOSTNAME | tr '.' '\n' | sed -n 1p", "r"); //Gets the local hostname (without the domain name part)
        if (fp == NULL) {
                printf("Oops! Failed to get hostname. Contact the course staff!\n" );
                exit(1);
        }

        char* hostname = (char*) malloc(HOSTNAME_LEN*sizeof(char));
	bzero(hostname, HOSTNAME_LEN);
        fscanf(fp, "%s[^\n]", hostname);

        bzero(LOGFILE, FILEPATH_LEN);
	sprintf(LOGFILE, "./logs/assignment3_log_%s", hostname);	

	bzero(DUMPFILE, FILEPATH_LEN);
	sprintf(DUMPFILE, "./logs/assignment3_dump_%s", hostname);

	/*Clean up*/
	free(hostname);
	fclose(fp);
}

/**
 * Writes the C string passed in format to both STDOUT and LOGFILE.
 * Like printf, replaces the format specifiers with the given arguments.
 * Return code is written to ret_print (for printing to STDOUT) 
 * and ret_log (for the LOGFILE printing).
 *
 * ret_print either contains the number of characters printed OR a negative value 
 * indicating the error code.
 * ret_print either contains the number of characters logged OR a negative value 
 * indicating the error code. error code -100 indicates unable to open LOGFILE.
 *
 * @param  format Format string to be printed
 * @param  ... Variable number of arguments to replace format specifiers
 */
int ret_print, ret_log;
void cse4589_print_and_log(const char* format, ...)
{
	va_list args_pointer;

	/* Print to STDOUT */
    	va_start(args_pointer, format);
   	ret_print = vprintf(format, args_pointer);

    	/* Write to LOG File */
    	FILE* fp;
    	if((fp = fopen(LOGFILE, "a")) == NULL){
    		ret_log = -100;
    		/* clean up before exit */
    		va_end(args_pointer);
	}
	fprintf(fp, "[PA3:Start:%u]\n", (unsigned)time(NULL));
    	va_start(args_pointer, format);
    	ret_log = vfprintf(fp, format, args_pointer);
	fprintf(fp, "[PA3:End]\n");

    	/* clean up */
	fclose(fp);
    	va_end(args_pointer);
}

/**
 * Writes the given packet to a binary DUMPFILE.
 *
 * @param  packet a pointer to the memory location of beginning of packet
 * @param  bytes size of the packet in bytes
 * @return ret_value number of bytes written OR negative value indicating the error code
 */
int cse4589_dump_packet(const void* packet, size_t bytes)
{
	int ret_value ;

	/* Dump Packet to file */
    	FILE* fp;
    	if((fp = fopen(DUMPFILE, "wb+")) == NULL)
    		return -100;

    	ret_value = fwrite(packet, 1, bytes, fp);
    	fclose(fp);

    	return ret_value;
}
