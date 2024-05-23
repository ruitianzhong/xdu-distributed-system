## Concurrent TCP Server/Client

socket()

```c
 int socket(int domain, int type, int protocol);
```

domain: AF_INET

type: SOCK_STREAM | SOCK_NONBLOCK

The protocol specifies a particular protocol to be used with
the socket.

protocol: 0

In ip(7):

tcp_socket = socket(AF_INET, SOCK_STREAM, 0);

Address format

An IP socket address is defined as a combination of an IP interface address and a 16-bit port number. The basic IP
protocol does
not supply port numbers, they are implemented by higher level protocols like udp(7) and tcp(7). On raw sockets sin_port
is set
to the IP protocol.

### Byte Order

htonl, htons, ntohl, ntohs - convert values between host and
network byte order

The htonl() function converts the unsigned integer hostlong
from host byte order to network byte order.

The htons()  function converts the unsigned short integer
hostshort from host byte order to network byte order.

## Listen

int listen(int sockfd, int backlog);

listen() marks the socket referred to by sockfd as a passive
socket, that is, as a socket that will be used to accept in‐
coming connection requests using accept(2)

The backlog argument defines the maximum length to which the
queue of pending connections for sockfd may grow.

## Accept

int accept(int sockfd, struct sockaddr *addr, socklen_t *addrlen);

The newly created socket is not in the listening
state. The original socket sockfd is unaffected by this
call.

The argument addr is a pointer to a sockaddr structure.
This structure is filled in with the address of the peer
socket, as known to the communications layer.

## Read

On success, the number of bytes read is returned (zero indi‐
cates end of file),

## Shutdown

shutdown - shut down part of a full-duplex connection

close -> ref_cnt == 0
shutdown -> do not consider ref_cnt

## Internet Address Manipulation

in_addr_t inet_addr(const char *cp);

## Connect

The connect() system call connects the socket referred to by
the file descriptor sockfd to the address specified by addr.

## Write

Note that a successful write() may transfer fewer than count
bytes. Such partial writes can occur for various reasons;
for example, because there was insufficient space on the
disk device to write all of the requested bytes, or because
a blocked write() to a socket, pipe, or similar was inter‐
rupted by a signal handler after it had transferred some,
but before it had transferred all of the requested bytes.
In the event of a partial write, the caller can make another
write()  call to transfer the remaining bytes.

## Wait

pid_t wait(int *wstatus)

All of these system calls are used to wait for state changes
in a child of the calling process, and obtain information
about the child whose state has changed.

In the case of a terminated child, performing a wait allows
the system to release the resources associated with the
child; if a wait is not performed, then the terminated child
remains in a "zombie" state

-1 meaning wait for any child process.

```
       WIFEXITED(wstatus)
              returns true if the child terminated  normally,  that
              is,  by  calling exit(3) or _exit(2), or by returning
              from main().

       WEXITSTATUS(wstatus)
              returns the exit status of the child.  This  consists
              of  the  least significant 8 bits of the status argu‐
              ment that the child specified in a call to exit(3) or
              _exit(2) or as the argument for a return statement in
              main().  This macro should be employed only if WIFEX‐
              ITED returned true.
```

## Time-Wait Recycle

[link](https://www.cnblogs.com/aoxueshou/p/13546814.html)

## Strtol()

