all: main.o
	gcc -o ipc main.o
main.o: main.c
	gcc -c main.c
.PHONY clean:
	rm -f *.o ipc