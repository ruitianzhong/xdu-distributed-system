# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: student.proto
# Protobuf Python Version: 4.25.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rstudent.proto\"\x1c\n\x0cHelloRequest\x12\x0c\n\x04name\x18\x01 \x01(\t\" \n\rHelloResponse\x12\x0f\n\x07message\x18\x01 \x01(\t\"\x12\n\x10\x41\x64\x64StudentResult\"4\n\x0bStudentInfo\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\n\n\x02ID\x18\x02 \x01(\t\x12\x0b\n\x03\x61ge\x18\x03 \x01(\x05\"\x19\n\x06Result\x12\x0f\n\x07success\x18\x01 \x01(\x08\"\x17\n\tStudentID\x12\n\n\x02id\x18\x01 \x01(\t\"\x1b\n\x0bStudentName\x12\x0c\n\x04name\x18\x01 \x01(\t2\xcf\x01\n\x07Student\x12-\n\nHelloWorld\x12\r.HelloRequest\x1a\x0e.HelloResponse\"\x00\x12\x1e\n\x03\x41\x64\x64\x12\x0c.StudentInfo\x1a\x07.Result\"\x00\x12\'\n\tQueryByID\x12\n.StudentID\x1a\x0c.StudentInfo\"\x00\x12+\n\x0bQueryByName\x12\x0c.StudentName\x1a\x0c.StudentInfo\"\x00\x12\x1f\n\x06\x44\x65lete\x12\n.StudentID\x1a\x07.Result\"\x00\x42\x1d\n\x0eink.zrt.clientZ\x0b../;studentb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'student_pb2', _globals)
if _descriptor._USE_C_DESCRIPTORS == False:
  _globals['DESCRIPTOR']._options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\016ink.zrt.clientZ\013../;student'
  _globals['_HELLOREQUEST']._serialized_start=17
  _globals['_HELLOREQUEST']._serialized_end=45
  _globals['_HELLORESPONSE']._serialized_start=47
  _globals['_HELLORESPONSE']._serialized_end=79
  _globals['_ADDSTUDENTRESULT']._serialized_start=81
  _globals['_ADDSTUDENTRESULT']._serialized_end=99
  _globals['_STUDENTINFO']._serialized_start=101
  _globals['_STUDENTINFO']._serialized_end=153
  _globals['_RESULT']._serialized_start=155
  _globals['_RESULT']._serialized_end=180
  _globals['_STUDENTID']._serialized_start=182
  _globals['_STUDENTID']._serialized_end=205
  _globals['_STUDENTNAME']._serialized_start=207
  _globals['_STUDENTNAME']._serialized_end=234
  _globals['_STUDENT']._serialized_start=237
  _globals['_STUDENT']._serialized_end=444
# @@protoc_insertion_point(module_scope)
