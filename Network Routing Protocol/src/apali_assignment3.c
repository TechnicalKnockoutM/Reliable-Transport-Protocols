/**
 * @apali_assignment3
 * @author  Avinash Pali <apali@buffalo.edu>
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
 * This contains the main function. Add further description here....
 */

/*References:
 * 1.http://www.sanfoundry.com/c-program-number-lines-text-file/
 * 2.http://www.codingunit.com/c-tutorial-file-io-using-text-files
 * 3.Beej Guide
 */

#include <stdio.h>
#include <stdlib.h>

#include "../include/global.h"
#include "../include/logger.h"
#include "../include/logger.h"
#include<inttypes.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <net/if.h>
#include <netdb.h>
#include <strings.h>
#include <arpa/inet.h>
typedef u_int8_t uint8_t;
typedef u_int16_t uint16_t;
typedef u_int32_t uint32_t;
# define myport "1234"
#define BUFFERSIZE 100

/*Structure to hold values from topology file*/
struct topology {
	uint16_t field1;
	uint16_t field2;
	struct in_addr ip_addr;
	uint16_t field3;
};

/*Structure to hold values from topology file*/
struct topology_neighbor {
	uint16_t field1;
	uint16_t field2;
	uint16_t field3;
};

/*Structure to store IP address and server ID and Port*/
struct id_with_ip {
	struct in_addr ip_addr1;
	uint16_t server_id;
	uint16_t server_port;
};

//[PA3] Routing Table
struct Routing_Table {
	uint16_t server_id;								//Server Id of Servers
	int16_t path_via;								//Next Hop to reach server
	uint16_t cost;									//Cost to reach server
	struct in_addr server_ip;						//IP Address of server
	int neighbor_flag;								//Flag to check if neighbor
};

//[PA3] Update Packet
struct server_details {
	struct in_addr server_ipaddr;					//IP address of server
	uint16_t server_port;							//Port number of server
	uint16_t empty_field;							//Field for padding
	uint16_t server_id;								//Server id of server
	uint16_t cost;									//Cost to reach server
};

//[PA3] Update Packet
struct update_packet {
	uint16_t number_of_update_fields;				//Number of update fields
	uint16_t sending_server_port;				//Port number of sending server
	struct in_addr sending_server_ipaddr;		//IP address of sending server
	struct server_details servers[];		//Object to server details structure
};

/**
 * main function
 *
 * @param  argc Number of arguments
 * @param  argv The argument list
 * @return 0 EXIT_SUCCESS
 */
int main(int argc, char **argv) {
	/*Init. Logger*/
	cse4589_init_log();

	/*Clear LOGFILE and DUMPFILE*/
	fclose(fopen(LOGFILE, "w"));
	fclose(fopen(DUMPFILE, "wb"));

	/*Start Here*/

	/*Handling command line arguments*/
	struct timeval timeout;
	FILE *ptr_file;
	FILE *ptr_file1;
	int timer_value;
	if (strcmp(argv[1], "-t") == 0) {

		ptr_file1 = fopen(argv[2], "r");
		ptr_file = fopen(argv[2], "r");
		if (strcmp(argv[3], "-i") == 0) {
			timer_value = atoi(argv[4]);
			timeout.tv_sec = atoi(argv[4]);
			timeout.tv_usec = 0;
		}
	} else if (strcmp(argv[1], "-i") == 0) {
		timer_value = atoi(argv[4]);
		timeout.tv_sec = atoi(argv[2]);
		timeout.tv_usec = 0;
		if (strcmp(argv[3], "-t") == 0) {

			ptr_file1 = fopen(argv[4], "r");
			ptr_file = fopen(argv[4], "r");
		}
	}

	char buffer[1000];
	if (!ptr_file)
		return 1;
	int num_servers;
	int num_neighbors;
	int server_id1[100];
	int port1[100];
	int j, size;
	char *a;
	char *m, chr;
	int count_lines = 0;
	int k, c;
	chr = getc(ptr_file1);

	/*Counting number of lines in a file*/
	while (chr != EOF)

	{

		//Count whenever new line is encountered

		if (chr == '\n')

		{

			count_lines = count_lines + 1;

		}

		//take next character from file.

		chr = getc(ptr_file1);

	}

	struct topology tp[5];
	struct topology_neighbor tp_n[4];

	/*Reading from topology file and storing in structure */
	fscanf(ptr_file, "%d ", &num_servers);			//Storing number of servers
	fscanf(ptr_file, "%d ", &num_neighbors);	//Storing number of neighbors

	struct sockaddr_in antelope;
	memset(&antelope, 0, sizeof antelope);
	char *line = NULL;
	size_t len = 0;
	antelope.sin_family = AF_INET;
	int i;
	struct in_addr *in;
	for (i = 0; i < num_servers; i++) {

		if (getline(&line, &len, ptr_file) != -1) {

			char *field1 = malloc(10);
			char *field2 = malloc(10);
			char *field3 = malloc(10);
			char *token = malloc(10);
			char *some_addr;

			strcpy(field1, strtok(line, " "));
			tp[i].field1 = (uint16_t) (atoi(field1));//Storing Server ID in structure

			strcpy(field2, strtok(NULL, " "));
			inet_aton(field2, &antelope.sin_addr);
			tp[i].ip_addr = antelope.sin_addr;	//Storing Server IP in structure
			some_addr = inet_ntoa(tp[i].ip_addr);

			strcpy(field3, strtok(NULL, " "));
			tp[i].field3 = (uint16_t) (atoi(field3));//Storing Server Port in structure
			antelope.sin_port = htons(tp[i].field3);
		}
	}

	for (i = 0; i < num_servers; i++) {
		if (getline(&line, &len, ptr_file) != -1) {

			char *token = malloc(10);
			char *field1 = malloc(10);
			char *field2 = malloc(10);
			char *field3 = malloc(10);

			strcpy(field1, strtok(line, " "));
			tp_n[i].field1 = (uint16_t) (atoi(field1));	//Storing Server ID in structure

			strcpy(field2, strtok(NULL, " "));
			tp_n[i].field2 = (uint16_t) (atoi(field2));	//Storing neighbor Server ID in structure

			strcpy(field3, strtok(NULL, " "));
			tp_n[i].field3 = (uint16_t) (atoi(field3));	//Storing cost to neighbor Server ID in structure

		}
	}

	c = count_lines - 2 - num_neighbors;
	struct id_with_ip id_ip[c];

	for (k = 0; k < c; k++) {
		id_ip[k].ip_addr1 = tp[k].ip_addr;
		id_ip[k].server_id = tp[k].field1;//Storing IP address, Server ID and Server Port in structure
		id_ip[k].server_port = tp[k].field3;
	}

	fclose(ptr_file);
	fclose(ptr_file1);

	int l, e;
	uint16_t temp_serverport;
	uint16_t temp_serverid = tp_n[0].field1;//Storing server ID of current node
	for (l = 0; l < num_servers; l++) {
		if (temp_serverid == tp[l].field1) {
			temp_serverport = tp[l].field3;	//Storing server port of current node
		}
	}

	/*Storing values in Routing table from data in topology file*/

	struct Routing_Table rt[num_servers];

	for (l = 0; l < num_servers; l++) {
		if (temp_serverid == tp[l].field1) {
			rt[l].cost = 0;
			rt[l].neighbor_flag = -1;
			rt[l].server_id = tp[l].field1;
			rt[l].server_ip = tp[l].ip_addr;
			rt[l].path_via = -1;
		} else {
			rt[l].neighbor_flag = -1;
			rt[l].server_id = tp[l].field1;
			rt[l].server_ip = tp[l].ip_addr;
			rt[l].path_via = -1;
			rt[l].cost = 65535;
		}
	}
	for (l = 0; l < num_neighbors; l++) {
		for (e = 0; e < num_servers; e++) {
			if (tp_n[l].field2 == rt[e].server_id) {
				rt[e].neighbor_flag = 1;
				rt[e].cost = tp_n[l].field3;
				rt[e].path_via = rt[e].server_id;
			}
		}
	}

	/*Structure to keep information about neighbors*/
	struct neighbors {
		struct sockaddr_in neighbor_ipaddr;			//Neighbor's IP address
		uint8_t count;//Count variable to set to cost to infinity if 3*timeout occurs
		uint16_t server_id;							//Neighbor's Server ID
		int count_flag;								//Flag for timer
	} neighbor[num_neighbors];

	uint16_t server_id[num_neighbors];
	for (l = 0; l < num_neighbors; l++) {
		neighbor[l].count_flag = -100;
		neighbor[l].server_id = tp_n[l].field2;
	}

	size_t size1 = (sizeof(uint32_t) + sizeof(struct in_addr)
			+ num_servers * (sizeof(struct server_details)));

	/*Creating UDP socket and binding it*/

	struct addrinfo hints, *servinfo, *p;
	int rv, sockfd, select_output;
	int yes = 1;
	fd_set master;
	fd_set read_fds;
	int fdmax, x;
	int numbytes;
	size_t pkt_size = size1;
	struct update_packet *buf1 = malloc(size1);
	socklen_t addr_len;
	FD_ZERO(&master);
	FD_ZERO(&read_fds);
	char s[INET_ADDRSTRLEN];
	char buff[BUFFERSIZE];
	bzero(buff, BUFFERSIZE);
	int packet_recv = 0;

	char port[20];
	sprintf(port, "%d", temp_serverport);

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_DGRAM;
	hints.ai_flags = AI_PASSIVE; // use my IP

	if ((rv = getaddrinfo(NULL, port, &hints, &servinfo)) != 0) { //Extracting port number from topology
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
		return 1;
	}

	// loop through all the results and bind to the first we can

	for (p = servinfo; p != NULL; p = p->ai_next) {

		if ((sockfd = socket(p->ai_family, p->ai_socktype, p->ai_protocol))
				== -1) {
			perror("server: socket");
			continue;
		}

		if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("server: bind");
			continue;
		}
		break;
	}

	if (p == NULL) {
		fprintf(stderr, "server: failed to bind\n");
		return 2;
	}

	FD_SET(sockfd, &master);
	FD_SET(0, &master);

	// keep track of the biggest file descriptor
	fdmax = sockfd;

	for (;;) {
		timeout.tv_sec = timer_value;//Setting timeout value received from arguments
		timeout.tv_usec = 0;

		read_fds = master; // copy it
		select_output = select(fdmax + 1, &read_fds, NULL, NULL, &timeout); //sett the timeout value here
		int y;
		char *some_addr1;
		size_t pkt_size = size1;
		if (select_output == -1) { //Select function error
			perror("select");
			exit(4);
		} else if (select_output == 0) {		//Timer Expires

			//Sending update packet
			send_update_packet(rt, tp, tp_n, num_servers, sockfd);

			//Incrementing the count as timeout occured
			for (x = 0; x < num_neighbors; x++) {
				if (neighbor[x].count_flag != -100) {
					neighbor[x].count++;
				}
			}

			//Check if count reached 3, if yes then set the cost to infinity
			for (x = 0; x < num_neighbors; x++) {
				if (neighbor[x].count == 3) {
					for (y = 0; y < num_servers; y++)
						if (rt[y].server_id == neighbor[x].server_id) {
							//Code for setting cost of node to infinity here
							rt[y].cost = 65535;
						}

				}
			}

		}

		else if (select_output == 1) {

			//If user types something
			if (FD_ISSET(0, &read_fds)) {

				fgets(buff, 100, stdin);

				//If user typed academic_integrity
				if (strncasecmp(buff, "academic_integrity",
						sizeof("academic_integrity") - 1) == 0) {
					cse4589_print_and_log("%s:SUCCESS\n", buff);
					cse4589_print_and_log("I have read and understood the course academic integrity policy located at http://www.cse.buffalo.edu/faculty/dimitrio/courses/cse4589_f14/index.html#integrity");
					printf("\n");
					bzero(buff, BUFFERSIZE);

				}

				//If user typed step
				else if (strncasecmp(buff, "step", sizeof("step") - 1) == 0) {

					send_update_packet(rt, tp, tp_n, num_servers, sockfd);
					cse4589_print_and_log("%s:SUCCESS\n", buff);
					bzero(buff, BUFFERSIZE);
				}

				//If user typed crash
				else if (strncasecmp(buff, "crash", sizeof("crash") - 1) == 0) {

					for (;;) {

					}
					bzero(buff, BUFFERSIZE);
				}

				//If user typed packets
				else if (strncasecmp(buff, "packets", sizeof("packets") - 1)
						== 0) {
					cse4589_print_and_log("%s:SUCCESS\n", buff);
					cse4589_print_and_log("%d\n", packet_recv);
					bzero(buff, BUFFERSIZE);
				}

				//If user typed dump
				else if (strncasecmp(buff, "dump", sizeof("dump") - 1) == 0) {

					cse4589_print_and_log("%s:SUCCESS\n", buff);
					dump(rt, tp, tp_n, num_servers, sockfd);
					bzero(buff, BUFFERSIZE);
				}

				//If user typed display
				else if (strncasecmp(buff, "display", sizeof("display") - 1)
						== 0) {

					cse4589_print_and_log("%s:SUCCESS\n", buff);
					for (l = 0; l < num_servers; l++) {

						cse4589_print_and_log("%-15d%-15d%-15d\n",
								rt[l].server_id, rt[l].path_via, rt[l].cost);

					}
					bzero(buff, BUFFERSIZE);
				}

				//If user typed disable
				else if (strncasecmp(buff, "disable", sizeof("disable") - 1)
						== 0) {
					cse4589_print_and_log("%s:SUCCESS\n", buff);
					char err[10] = { "ERROR" };
					char store[400];

					char *temp = malloc(100);
					char *temp1 = malloc(100);
					int i, j;
					uint16_t temp2;
					temp = strtok(buff, " ");
					temp1 = strtok(NULL, " ");
					temp2 = (uint16_t)atoi(&temp1);
					for (i = 0; i < num_servers; i++) {
						if (rt[i].server_id == temp2) {
							if (rt[i].neighbor_flag == 1) {
								rt[i].cost = 65535;
								rt[i].neighbor_flag = 0;
								rt[i].path_via = -1;

							}
						}
					}
					bzero(buff, BUFFERSIZE);
				}
				else if (strncasecmp(buff, "update", sizeof("update") - 1)
										== 0) {
					cse4589_print_and_log("%s:SUCCESS\n", buff);
									char err[10] = { "ERROR" };
									char *temp1 = malloc(10);
									char *temp2 = malloc(10);
									char *temp3 = malloc(10);
									char *temp4 = malloc(10);

									uint16_t tempx1, tempx2, tempx3, tempx4;
									temp1= strtok(buff, " ");
									temp2= strtok(NULL, " ");
									temp3= strtok(NULL, " ");
									temp4= strtok(NULL, " ");
									tempx1 = (uint16_t) (atoi((temp1)));
									tempx2 = (uint16_t) (atoi(temp2));
									tempx3 = (uint16_t) (atoi(temp3));
									tempx4 = (uint16_t) (atoi(temp4));

									for (i = 0; i < num_servers; i++) {
										if (tempx3 == rt[i].server_id) {
											if (rt[i].neighbor_flag == 1) {
												rt[i].cost = tempx4;

											}

										}

									}

									bzero(buff, BUFFERSIZE);
								}
				else {
					char err2[20] = { "BAD COMMAND" };
					cse4589_print_and_log("%s:%s\n", buff, err2);
				}

			}

			//If we receive any data update the routing table and call bellmand ford algorithm
			if (FD_ISSET(sockfd, &read_fds)) {
				packet_recv++;

				if ((numbytes = recvfrom(sockfd, (struct update_packet*) buf1,
						pkt_size, 0,
						NULL, NULL)) == -1) {
					perror("recvfrom");
					//					exit(1);
				}

				cse4589_print_and_log("RECEIVED A MESSAGE FROM SERVER %d\n",
						temp_serverid);

				for (i = 0; i < num_servers; i++) {
					cse4589_print_and_log("%-15d%-15d\n",
							ntohs(buf1->servers[i].server_id),
							ntohs(buf1->servers[i].cost));
				}

				printf("listener: packet is %d bytes long\n", numbytes);
				uint16_t temp_port = buf1->sending_server_port;

				//Initialize the count to zero if packet is received first time for neighbor
				for (x = 0; x < num_neighbors; x++) {
					if (neighbor[x].count == -100) {
						for (x = 0; x < num_servers; x++) {

							if (id_ip[x].server_port == temp_port) {
								neighbor[x].server_id = id_ip[x].server_id;
							}
							neighbor[x].count = 0;

							break;
						}
					}
				}
				//Call update routing function here
				update_cost_matrix(count_lines, num_servers, num_neighbors,
						tp_n, tp, rt, buf1, temp_serverid);
			}

		}

	} //End of infinite for loop
	return 0;
}

/*Function to make and send update packets to all the neighbors*/
void send_update_packet(struct Routing_Table rt[], struct topology tp[],
		struct topology_neighbor tp_n[], int num_server, int sockfd) {

	int i, j;
	size_t size = (sizeof(uint32_t) + sizeof(struct in_addr)
			+ num_server * (sizeof(struct server_details)));
	struct update_packet *up = malloc(size);

	up->number_of_update_fields = htons(num_server);
	for (i = 0; i < num_server; i++) {
		if (tp_n[0].field1 == tp[i].field1) {
			up->sending_server_ipaddr = (tp[i].ip_addr);
			up->sending_server_port = htons(tp[i].field3);

		}
	}

	for (i = 0; i < num_server; i++) {

		up->servers[i].server_ipaddr = tp[i].ip_addr;

		up->servers[i].server_port = htons(tp[i].field3);

		up->servers[i].empty_field = htons(0);

		up->servers[i].server_id = htons(tp[i].field1);

		if (ntohs(up->servers[i].server_id) == tp_n[0].field1) {
			up->servers[i].cost = htons(0);

		} else {
			for (j = 0; j < num_server; j++) {
				if (ntohs(up->servers[i].server_id) == rt[j].server_id) {
					up->servers[i].cost = htons(rt[j].cost);

				}
			}
		}
	}

	//sending packet to all the neighbors
	for (i = 0; i < num_server; i++) {
		if (rt[i].neighbor_flag == 1) { 		//Checking if server is neighbor

			char *ex;
			struct sockaddr_in s;
			memset(&s, 0, sizeof s);

			s.sin_family = AF_INET;
			s.sin_addr = rt[i].server_ip;
			ex = inet_ntoa(s.sin_addr);
			for (j = 0; j < num_server; j++) {
				if (rt[i].server_id == tp[j].field1) {
					s.sin_port = htons(tp[j].field3);
				}
			}
			socklen_t tolen = sizeof(struct sockaddr_in);

			sendto(sockfd, (struct update_packet*) up, size, 0, &s, tolen);

		}
	}
}

/*Function to update cost matrix*/

void update_cost_matrix(int count_lines, int num_servers, int num_neighbors,
		struct topology_neighbor tp_n[], struct topology tp[],
		struct Routing_Table rt[], struct update_packet up,
		uint16_t temp_serverid) {

	int y = count_lines - 2 - num_servers;
	int z = num_servers + 1;
	uint16_t cost_matrix[z][z];
	int g, h;

	for (g = 1; g < z; g++) {
		for (h = 1; h < z; h++) {
			cost_matrix[g][h] = 65535;
		}
	}

	uint16_t server_id = tp_n[0].field1;
	cost_matrix[server_id][server_id] = 0;//Setting the cost matrix from the topology structure
	for (g = 1; g <= num_neighbors; g++) {
		cost_matrix[server_id][(tp_n[g - 1].field2)] = tp_n[g - 1].field3;

	}

//Updating the cost if any packet is received from neighbor servers
	for (g = 0; g < num_servers; g++) {
		uint16_t server_id_new;
		if (up.sending_server_port == tp[g].field3) {
			server_id_new = tp[g].field1;
			//				cost_matrix[server_id][server_id] = 0;
			for (h = 1; h < z; h++) {
				cost_matrix[server_id_new][up.servers[h - 1].server_id] =
						up.servers[h - 1].cost;
			}
		}
	}
	//Calling bellman ford algorithm
	Bellman_Ford(cost_matrix, temp_serverid, num_neighbors, rt);

}

/*Bellman ford algorithm on Cost matrix*/
void Bellman_Ford(uint16_t cost_matrix[][6], uint16_t temp_serverid,
		int num_neighbor, struct Routing_Table rt[]) {
	int i, j;
	for (i = 0; i < 5; i++) {
		if (rt[i].neighbor_flag == 1) {
			for (j = 0; j < 5; j++) {
				if ((cost_matrix[temp_serverid][rt[i].server_id]
						+ cost_matrix[rt[i].server_id][j + 1])
						< (cost_matrix[temp_serverid][j + 1])) {
					cost_matrix[temp_serverid][j + 1] =
							cost_matrix[temp_serverid][rt[i].server_id]
									+ cost_matrix[rt[i].server_id][j + 1];
					cost_matrix[j + 1][temp_serverid] =
							cost_matrix[temp_serverid][rt[i].server_id]
									+ cost_matrix[rt[i].server_id][j + 1];
					rt[j].cost = cost_matrix[temp_serverid][j + 1];
					rt[j].path_via = rt[i].server_id;

				}

			}

		}
	}
	cost_matrix[temp_serverid][temp_serverid] = 0;
}

/*Function to dump data in dump file*/
void dump(struct Routing_Table rt[], struct topology tp[],
		struct topology_neighbor tp_n[], int num_server, int sockfd) {

	int i, j;

	size_t size = (sizeof(uint32_t) + sizeof(struct in_addr)
			+ num_server * (sizeof(struct server_details)));

	struct update_packet *up = malloc(size);

	up->number_of_update_fields = htons(num_server);
	for (i = 0; i < num_server; i++) {
		if (tp_n[0].field1 == tp[i].field1) {
			up->sending_server_ipaddr = (tp[i].ip_addr); //htonl
			up->sending_server_port = htons(tp[i].field3);

		}
	}

	for (i = 0; i < num_server; i++) {

		up->servers[i].server_ipaddr = tp[i].ip_addr;

		up->servers[i].server_port = htons(tp[i].field3);

		up->servers[i].empty_field = htons(0);

		up->servers[i].server_id = htons(tp[i].field1);

		if (ntohs(up->servers[i].server_id) == tp_n[0].field1) {
			up->servers[i].cost = htons(0);

		} else {
			for (j = 0; j < num_server; j++) {
				if (ntohs(up->servers[i].server_id) == rt[j].server_id) {
					up->servers[i].cost = htons(rt[j].cost);

				}
			}
		}
	}

	cse4589_dump_packet((struct update_packet*) up, size);
}

