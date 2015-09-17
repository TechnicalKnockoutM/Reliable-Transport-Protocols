#ifndef LOGGER_H_
#define LOGGER_H_

#define FILEPATH_LEN 256

extern char LOGFILE[FILEPATH_LEN];
extern char DUMPFILE[FILEPATH_LEN];

extern int ret_print, ret_log;

void cse4589_init_log();
void cse4589_print_and_log(const char* format, ...);
int cse4589_dump_packet(const void* packet, size_t length);

#endif
