#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <time.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/ip.h>
#include <arpa/inet.h>
#define BUF_MAX 100

int main(int argc, char *argv[]) {
    if (argc != 4) {
        printf("Usage: ./client [IP] [PORT] [TIME(second)]\n");
        exit(EXIT_FAILURE);
    }

    char *ip_str = argv[1];
    int port = atoi(argv[2]);
    if (port <= 0 || port > 65535) {
        printf("port %d is invalid\n", port);
        exit(EXIT_FAILURE);
    }
    int time = atoi(argv[3]);
    if (time < 0 || time > 20) {
        printf("invalid time : %d\n", time);
        exit(EXIT_FAILURE);
    }


    const int sockfd = socket(AF_INET,SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    struct sockaddr_in server_addr;
    bzero(&server_addr, sizeof(server_addr));

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(ip_str);
    server_addr.sin_port = htons(port);

    if (connect(sockfd, (struct sockaddr *) &server_addr, sizeof server_addr) == -1) {
        perror("connect");
        exit(EXIT_FAILURE);
    }

    const char *s = "ping";
    int n = write(sockfd, s, strlen(s));
    if (n == -1) {
        perror("write");
        exit(EXIT_FAILURE);
    }
    printf("write %d bytes\n", n);
    char buf[BUF_MAX];
    int i = 0;

    while (i < BUF_MAX) {
        n = read(sockfd, buf + i, (sizeof buf) - i);
        if (n == 0) {
            printf("EOF is detected\n");
            break;
        }
        printf("n= %d\n", n);
        i += n;
    }

    for (int j = 0; j < i; j++) {
        putchar(buf[j]);
    }
    putchar('\n');

    close(sockfd);

    return 0;
}
