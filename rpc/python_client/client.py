import grpc
import student_pb2_grpc
import student_pb2

channel = grpc.insecure_channel('localhost:6666')
stub = student_pb2_grpc.StudentStub(channel=channel)

result = stub.Add(student_pb2.StudentInfo(name="Tim",ID="321",age=22))
print(result)

stu = stub.QueryByID(student_pb2.StudentID(id="321"))

print(stu)

stu = stub.QueryByName(student_pb2.StudentName(name="Tim"))

print(stu)

result = stub.Delete(student_pb2.StudentID(id="321"))

print(result)
