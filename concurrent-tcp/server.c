#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <threads.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#define BUF_SIZE 100

int handleConnection(int connfd) {
    int ret = fork();
    if (ret == -1) {
        return -1;
    }
    if (ret > 0) {
        close(connfd);
        return ret;
    }


    char buf[BUF_SIZE];
    int n = read(connfd, buf, sizeof(buf));
    printf("server received %d bytes\n", n);
    if (n == -1) {
        perror("read");
        exit(EXIT_FAILURE);
    }
    if (n == 0) {
        close(connfd);
    }

    for (int i = 0; i < n; i++) {
        putchar(buf[i]);
    }
    printf("\n");
    char *s = "pong";
    n = write(connfd, s, strlen(s));
    printf("send %d bytes\n", n);

    close(connfd);

    exit(EXIT_SUCCESS);
}


int main(int argc, char *argv[]) {
    // assigning a name to a socket

    if (argc != 3) {
        printf("Usage: ./server [IP address] [PORT]\n");
        printf("%d\n", argc);
        exit(EXIT_FAILURE);
    }
    char *ip_addr_str = argv[1];
    int port = atoi(argv[2]);

    if (port <= 0 || port > 65535) {
        printf("port %d is invalid\n", port);
        exit(EXIT_FAILURE);
    }

    const int sockfd = socket(AF_INET,SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in server_addr, peer_addr;
    bzero(&server_addr, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(ip_addr_str);
    server_addr.sin_port = htons(port);


    if (bind(sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr)) == -1) {
        perror("accept");
        exit(EXIT_FAILURE);
    }


    if (listen(sockfd, 10) == -1) {
        perror("listen");
        exit(EXIT_FAILURE);
    }

    int len = sizeof(peer_addr);
    const int connfd = accept(sockfd, (struct sockaddr *) &peer_addr, (socklen_t *) &len);
    if (connfd == -1) {
        perror("accept");
        exit(EXIT_FAILURE);
    }
    handleConnection(connfd);
    int status;
    waitpid(-1, &status, 0);

    if (WIFEXITED(status)) {
        printf("child process return %d\n",WEXITSTATUS(status));
    }

    exit(EXIT_SUCCESS);
}
