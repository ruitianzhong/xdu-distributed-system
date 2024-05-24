## pipe()

pipefd[0] refers
to the read end of the pipe. pipefd[1] refers to the write end of the
pipe.

Partial read from pipe?

## Shared Memory

+ shm_open() creates and opens a new, or opens an existing, POSIX shared
  memory object.
+ The shm_unlink() function performs the converse opera‐
  tion, removing an object previously created by shm_open()
+ shmat
+ shmget
+ For portable use, a shared memory object should be identified by a name of the
  form /somename;
+ The file descriptor is normally used in subsequent calls to ftrun‐
  cate(2) (for a newly created object) and mmap(2). After a call to
  mmap(2)  the file descriptor may be closed without affecting the memory
  mapping
+ The operation of shm_unlink() is analogous to unlink(2): it removes a
  shared memory object name, and, once all processes have unmapped the
  object, de-allocates and destroys the contents of the associated memory
  region.
+ int fd = shm_open(shmpath, O_CREAT | O_EXCL | O_RDWR,
  S_IRUSR | S_IWUSR);

O_EXCL Ensure that this call creates the file: if this flag is speci‐
fied in conjunction with O_CREAT, and pathname already exists, then open() fails with the error EEXIST.

## Semaphore

int sem_init(sem_t *sem, int pshared, unsigned int value);

If pshared is nonzero, then the semaphore is shared between processes,
and should be located in a region of shared memory 
