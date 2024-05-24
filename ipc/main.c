#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <semaphore.h>
#include <sys/wait.h>
#define  BUFSIZE 26 // 100 kB > 64 kB

#define errExit(msg)    do { perror(msg); exit(EXIT_FAILURE); \
} while (0)

struct sharedbuf {
    sem_t sem1;
    sem_t sem2;
    size_t cnt;
    char buf[BUFSIZE];
    int shmfd;
};


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

void p1(int readfd, int writefd, struct sharedbuf *buf1, struct sharedbuf *buf2) {
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
    // p1 send data to p2 via shared memory
    buf1->cnt = 0;
    for (int i = 0; i < BUFSIZE; i++) {
        char c = 'a' + (i % 26);
        buf1->cnt += 1;
        buf1->buf[i] = c;
        if (sem_post(&buf1->sem2) == -1) {
            errExit("p1::sem_post::sem1");
        }
        if (sem_wait(&buf1->sem1) == -1) {
            errExit("p1::sem_wait::sem1");
        }
        // sleep 0.5ms
        usleep(500);
    }

    for (int i = 0; i < BUFSIZE; i++) {
        if (sem_wait(&buf2->sem1) == -1) {
            errExit("sem_wait");
        }

        char c = buf2->buf[i];
        printf("p1 gets char %c\n", c);
        if (sem_post(&buf2->sem2) == -1) {
            errExit("sem_post");
        }
        usleep(500);
    }
}


void p2(int readfd, int writefd, struct sharedbuf *buf1, struct sharedbuf *buf2) {
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

    for (int i = 0; i < BUFSIZE; i++) {
        if (sem_wait(&buf1->sem2) == -1) {
            errExit("p2::sem_wait::sem2");
        }
        char c = buf1->buf[i];

        printf("p2 is reading char %c\n", c);
        if (sem_post(&buf1->sem1) == -1) {
            errExit("p2::sem_post::sem1");
        }
        usleep(500);
    }
    buf2->cnt = 0;
    for (int i = 0; i < BUFSIZE; i++) {
        buf2->cnt++;
        buf2->buf[i] = '0' + i % 10;
        if (sem_post(&buf2->sem1) == -1) {
            errExit("sem_post");
        }
        if (sem_wait(&buf2->sem2) == -1) {
            errExit("sem_wait");
        }
    }
}

void init_sharedbuf_sem(struct sharedbuf *buf) {
    if (sem_init(&buf->sem1, 1, 0) == -1) {
        perror("sem1");
        exit(EXIT_FAILURE);
    }
    // init sem2
    if (sem_init(&buf->sem2, 1, 0) == -1) {
        perror("sem2");
        exit(EXIT_FAILURE);
    }
}

struct sharedbuf *init_sharedbuf_by_posix_shm(const char *shmpath) {
    shm_unlink(shmpath);
    // ensure the creation of the file
    // shared memory through POSIX shared memory
    int shmfd = shm_open(shmpath,O_CREAT | O_EXCL | O_RDWR,S_IRUSR | S_IWUSR);
    if (shmfd == -1) {
        perror("shm_open");
        exit(EXIT_FAILURE);
    }

    if (ftruncate(shmfd, sizeof(struct sharedbuf)) == -1) {
        perror("ftruncate");
        exit(EXIT_FAILURE);
    }
    struct sharedbuf *buf = mmap(NULL, sizeof(*buf),PROT_READ | PROT_WRITE, MAP_SHARED, shmfd, 0);
    /*   After a call to mmap(2) the file descriptor
     *   may be closed without affecting the memory mapping.
     */
    close(shmfd);

    buf->cnt = 0;
    buf->shmfd = shmfd;
    init_sharedbuf_sem(buf);

    return buf;
}

struct sharedbuf *init_shared_buf_by_shared_mmap() {
    struct sharedbuf *buf = mmap(NULL, sizeof(*buf),PROT_WRITE | PROT_WRITE,MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (buf == MAP_FAILED) {
        errExit("mmap anonymous");
    }
    buf->shmfd = -1;
    buf->cnt = 0;
    init_sharedbuf_sem(buf);
    return buf;
}

// ipc (inter-process communication)
int main() {
    // unidirectional pipe
    int pipefd_1_to_parent[2], pipefd_2_to_parent[2];
    int pipefd_parent_to_1[2], pipefd_parent_to_2[2];
    char *shmpath = "/ipc";
    int pids[2];

    struct sharedbuf *buf1 = init_sharedbuf_by_posix_shm(shmpath),
            *buf2 = init_shared_buf_by_shared_mmap();
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
        p1(pipefd_parent_to_1[0], pipefd_1_to_parent[1], buf1, buf2);
        exit(EXIT_SUCCESS);
    } else if (ret == -1) {
        perror("fork:");
        exit(EXIT_FAILURE);
    }
    close(pipefd_1_to_parent[1]);
    close(pipefd_parent_to_1[0]);
    pids[0] = ret;
    ret = fork();
    if (ret == 0) {
        close(pipefd_2_to_parent[0]);
        close(pipefd_parent_to_2[1]);
        p2(pipefd_parent_to_2[0], pipefd_2_to_parent[1], buf1, buf2);
        exit(EXIT_SUCCESS);
    } else if (ret == -1) {
        perror("fork");
        exit(EXIT_FAILURE);
    }
    pids[1] = ret;
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

    // wait all the child process to exit here.
    for (int i = 0; i < 2; i++) {
        int status;
        int result = waitpid(pids[i], &status, 0);
        if (result == -1) {
            perror("waitpid");
            continue;
        }
        if (WIFEXITED(status)) {
            printf("pid %d exit code %d\n", pids[i],WEXITSTATUS(status));
        }
    }

    shm_unlink(shmpath);
    exit(EXIT_SUCCESS);
}
