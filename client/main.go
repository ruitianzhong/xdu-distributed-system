package main

import (
	"context"
	"fmt"
	"log"

	"student"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func main() {
	var opts []grpc.DialOption
	opts = append(opts, grpc.WithTransportCredentials(insecure.NewCredentials()))
	conn, err := grpc.Dial("localhost:6666", opts...)
	if err != nil {
		log.Fatalf("%v", err)
	}
	defer conn.Close()
	client := student.NewStudentClient(conn)
	result, err := client.Add(context.Background(), &student.StudentInfo{Name: "John", Age: 22, ID: "123"})
	handleError(err)
	fmt.Println("set result", result.Success)

	stu, err := client.QueryByID(context.Background(), &student.StudentID{Id: "123"})
	handleError(err)
	fmt.Printf("get info %+v\n", stu)

	stu, err = client.QueryByName(context.Background(), &student.StudentName{Name: "John"})
	handleError(err)
	fmt.Printf("get info %+v\n", stu)

	result, err = client.Delete(context.Background(), &student.StudentID{Id: "123"})
	handleError(err)
	fmt.Println("delete result:", result.Success)

	fmt.Println("Client test end!")

}

func handleError(e error) {
	if e != nil {
		log.Println(e)
	}
}
