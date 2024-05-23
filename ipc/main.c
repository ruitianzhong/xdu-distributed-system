#include <stdio.h>
#include <unistd.h>

int main() {
    printf("hello world\n");

    const int ret = fork();
    int parent2p1[2], p12parent[2];

    if (ret == 0) {
    } else if (ret > 0) {
    } else {
        perror("fork:");
        return 0;
    }
}
