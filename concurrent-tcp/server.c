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

void handleSIGCHILD(int sig) {
    int status;

    const int pid = waitpid(-1, &status, WNOHANG);
    // WNOHANG to achieve concurency
    if (pid == 0) {
        printf("no state change for now\n");
        return;
    }
    if (pid == -1) {
        perror("waitpid");
        return;
    }

    if (WIFEXITED(status)) {
        printf("child process %d return %d\n", pid,WEXITSTATUS(status));
    }
}

int handleConnection(const int connfd, const int serverfd) {
    const int ret = fork();
    if (ret == -1) {
        return -1;
    }
    if (ret > 0) {
        close(connfd);
        return ret;
    }
    close(serverfd);
    uint32_t time;
    char *p = (char *) &time;
    int i = 0;
    while (i < sizeof(uint32_t)) {
        const int n = read(connfd, p + i, sizeof(time));
        if (n == -1) {
            close(connfd);
            exit(EXIT_FAILURE);
        }
        if (n == 0) {
            printf("EOF\n");
            exit(EXIT_FAILURE);
        }
        i += n;
    }
    time = ntohl(time);

    printf("server received %d bytes time %d\n", i, time);
    // sleeping for a long time
    sleep(time);

    const char *s = "ok";
    const int n = write(connfd, s, strlen(s));
    if (n == -1) {
        close(connfd);
        exit(EXIT_FAILURE);
    }
    printf("send %d bytes\n", n);

    close(connfd);

    exit(EXIT_SUCCESS);
}


int main(int argc, char *argv[]) {
    // assigning a name to a socket

    if (argc != 3) {
        printf("Usage: ./server [IP address] [PORT]\n");
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
        perror("bind");
        exit(EXIT_FAILURE);
    }


    if (listen(sockfd, 10) == -1) {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    signal(SIGCHLD, handleSIGCHILD);
    for (;;) {
        int len = sizeof(peer_addr);
        const int connfd = accept(sockfd, (struct sockaddr *) &peer_addr, (socklen_t *) &len);
        if (connfd == -1) {
            perror("accept");
            exit(EXIT_FAILURE);
        }
        handleConnection(connfd, sockfd);
    }
}
