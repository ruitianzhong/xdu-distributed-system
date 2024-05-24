#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void readInt(int *store, int readfd) {
    int offset = 0;
    while (offset < sizeof(int)) {
        char *p = (char *) store;
        int n = read(readfd, p + offset, sizeof(int) - offset);
        if (n == 0) {
            printf("unexpected EOF\n");
            exit(EXIT_FAILURE);
        }
        if (n == -1) {
            perror("p2:read");
            exit(EXIT_FAILURE);
        }
        offset += n;
    }
}

void p1(int readfd, int writefd) {
    // p1 to p2 (test)
    for (int i = 0; i < 5; i++) {
        write(writefd, &i, sizeof(int));
    }

    // p2 to p1 (test)
    for (int i = 0; i < 5; i++) {
        int temp;
        readInt(&temp, readfd);

        printf("p1 receive %d\n", temp);
    }
}


void p2(int readfd, int writefd) {
    // p1 to p2 (test)
    for (int i = 0; i < 5; i++) {
        int temp;

        readInt(&temp, readfd);

        printf("p2 receive %d\n", temp);
    }

    // p2 to p1 (test)
    for (int i = 5; i < 10; i++) {
        write(writefd, &i, sizeof(int));
    }
}


int main() {
    // unidirectional pipe
    int pipefd_1_to_parent[2], pipefd_2_to_parent[2];
    int pipefd_parent_to_1[2], pipefd_parent_to_2[2];
    // create the pipe
    if (pipe(pipefd_1_to_parent) == -1 || pipe(pipefd_2_to_parent) == -1 || pipe(pipefd_parent_to_1) == -1 ||
        pipe(pipefd_parent_to_2) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    int ret = fork();
    if (ret == 0) {
        close(pipefd_1_to_parent[0]);
        close(pipefd_parent_to_1[1]);
        p1(pipefd_parent_to_1[0], pipefd_1_to_parent[1]);
        exit(EXIT_SUCCESS);
    } else if (ret == -1) {
        perror("fork:");
        exit(EXIT_FAILURE);
    }
    close(pipefd_1_to_parent[1]);
    close(pipefd_parent_to_1[0]);
    ret = fork();
    if (ret == 0) {
        close(pipefd_2_to_parent[0]);
        close(pipefd_parent_to_2[1]);
        p2(pipefd_parent_to_2[0], pipefd_2_to_parent[1]);
        exit(EXIT_SUCCESS);
    } else if (ret == -1) {
        perror("fork");
        exit(EXIT_FAILURE);
    }

    close(pipefd_parent_to_2[0]);
    close(pipefd_2_to_parent[1]);

    // p1 -> parent -> p2 (test)
    for (int i = 0; i < 5; i++) {
        int temp;
        read(pipefd_1_to_parent[0], &temp, sizeof temp);
        write(pipefd_parent_to_2[1], &temp, sizeof temp);
    }

    // p2 -> parent -> p1 (test)
    for (int i = 0; i < 5; i++) {
        int temp;
        read(pipefd_2_to_parent[0], &temp, sizeof temp);
        write(pipefd_parent_to_1[1], &temp, sizeof temp);
    }


    exit(EXIT_SUCCESS);
}
