# 
# IPWorks SNMP 2022 C++ Edition - Sample Project
# 
# This sample project demonstrates the usage of IPWorks SNMP in a 
# simple, straightforward way. This is not intended to be a complete 
# application. Error handling and other checks are simplified for clarity.
# 
# Copyright (c) 2023 /n software inc. www.nsoftware.com
# 

ifeq ($(shell uname -s), Darwin) # MACOS
R_PATH=-Wl,-rpath,../../lib64/,-rpath,.

FRAMEWORK = -framework Carbon -framework Security
LD_FLAGS = -lipworkssnmp.22.0 -L../../lib64/ -lresolv
LD_FLAGS_SRC = -lz -lresolv
OS_CFLAGS = -D UNIX -arch arm64
MACOS = "darwin% Darwin% macos%"

all:
	g++ $(OS_CFLAGS) -D UNIX -o snmpmgr snmpmgr.cpp  -I../../include/ $(LD_FLAGS) $(FRAMEWORK) $(R_PATH)

src: ../../src/ipworkssnmp.o
	g++ $(OS_CFLAGS) -o snmpmgr ../../src/ipworkssnmp.o snmpmgr.cpp  $(LD_FLAGS_SRC) $(FRAMEWORK)

../../src/ipworkssnmp.o:
	g++ -c -fno-exceptions -fno-rtti -fPIC -arch arm64 -o ../../src/ipworkssnmp.o ../../src/ipworkssnmp.cpp

else # LINUX
ifeq ($(shell uname -m), x86_64)
  LIB=lib64
else
  LIB=lib
endif

R_PATH=-Wl,-rpath,../../$(LIB)/,-rpath,.
LD_FLAGS = -lipworkssnmp  -L../../$(LIB)/ 
LD_FLAGS_SRC = -lz 

all:
	g++ -D UNIX -o snmpmgr snmpmgr.cpp  -I../../include/ $(LD_FLAGS) $(R_PATH) 

src: ../../src/ipworkssnmp.o
	g++ -D UNIX -o snmpmgr ../../src/ipworkssnmp.o snmpmgr.cpp  -I../../include/ $(LD_FLAGS_SRC)

../../src/ipworkssnmp.o:
	g++ -c -fno-exceptions -fno-rtti -fPIC -o ../../src/ipworkssnmp.o ../../src/ipworkssnmp.cpp

clean:
	rm -f ../../src/*.o
	rm -f snmpmgr *.o
endif