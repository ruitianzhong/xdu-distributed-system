package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"student"
	"sync"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", "localhost:6666")
	if err != nil {
		log.Fatalf("failed to listen : %v", err)
	}
	var ops []grpc.ServerOption
	grpcServer := grpc.NewServer(ops...)
	server := &StudentServer{}
	server.Init()
	student.RegisterStudentServer(grpcServer, server)
	grpcServer.Serve(lis)
}

type StudentInfo struct {
	Name, Id string
	Age      int
}

func (s *StudentServer) Init() {
	s.id2stu = make(map[string]*StudentInfo)
	s.name2stu = make(map[string]*StudentInfo)

}

type StudentServer struct {
	student.UnimplementedStudentServer
	id2stu   map[string]*StudentInfo
	name2stu map[string]*StudentInfo
	sync.Mutex
}

func (s *StudentServer) HelloWorld(c context.Context, request *student.HelloRequest) (*student.HelloResponse, error) {
	fmt.Println(request.Name)
	return &student.HelloResponse{Message: "Hello world there"}, nil
}

func (s *StudentServer) Add(c context.Context, stu *student.StudentInfo) (*student.Result, error) {
	s.Lock()
	defer s.Unlock()
	p := &StudentInfo{Id: stu.ID, Name: stu.Name, Age: int(stu.Age)}
	s.id2stu[stu.ID] = p
	s.name2stu[stu.Name] = p
	return &student.Result{Success: true}, nil
}
func (s *StudentServer) QueryByID(c context.Context, stu *student.StudentID) (*student.StudentInfo, error) {
	s.Lock()
	defer s.Unlock()
	v := s.id2stu[stu.Id]
	if v == nil {
		return &student.StudentInfo{Age: -1}, nil
	}
	return &student.StudentInfo{Age: int32(v.Age), Name: v.Name, ID: v.Id}, nil
}
func (s *StudentServer) QueryByName(_ context.Context, stu *student.StudentName) (*student.StudentInfo, error) {
	s.Lock()
	defer s.Unlock()
	v := s.name2stu[stu.Name]
	if v == nil {
		return &student.StudentInfo{Age: -1}, nil
	}
	return &student.StudentInfo{Age: int32(v.Age), Name: v.Name, ID: v.Id}, nil
}
func (s *StudentServer) Delete(_ context.Context, stu *student.StudentID) (*student.Result, error) {
	s.Lock()
	defer s.Unlock()
	v := s.id2stu[stu.Id]
	if v == nil {
		return &student.Result{Success: false}, nil
	}
	id, name := v.Id, v.Name
	delete(s.id2stu, id)
	delete(s.name2stu, name)
	return &student.Result{Success: true}, nil
}
