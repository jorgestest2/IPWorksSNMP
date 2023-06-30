# 
# IPWorks SNMP 2022 Python Edition - Sample Project
# 
# This sample project demonstrates the usage of IPWorks SNMP in a 
# simple, straightforward way. This is not intended to be a complete 
# application. Error handling and other checks are simplified for clarity.
# 
# Copyright (c) 2023 /n software inc. www.nsoftware.com
# 

import sys
import string
from ipworkssnmp import *

input = sys.hexversion<0x03000000 and raw_input or input

def fireOnMibNode(e):
  print(e.node_oid + ": " + e.node_label)

try:
  if len(sys.argv) < 3:
    print("Usage: mibbrowser mibfile oid\r\n")
    print("Example: mibbrowser .\\rfc1213-mib ifTable")
    print("Press enter to continue...")
    input()
    sys.exit(0)

  browser = MibBrowser()
  browser.on_mib_node = fireOnMibNode

  # load MIB file and select node
  browser.load_mib(sys.argv[1])
  browser.select_node(sys.argv[2])

  # Display details of node
  print("Details:")
  print(browser.node_oid + ": " + browser.node_label)
  print(browser.node_description + "\r\n")

  # List successors (through OnMIbNode event)
  print("Successors:")
  browser.list_successors()

  print("\r\nPress enter to continue...")
  input()
  sys.exit(0)

except IPWorksSNMPError as e:
  print("IPWorks SNMP Exception: %s" % e.message)
except Exception as e:
  print("Exception: %s" % e)

