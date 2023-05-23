/*
 * IPWorks SNMP 2022 Java Edition- Demo Application
 *
 * Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
 *
 */

import java.io.*;

import ipworkssnmp.*;

public class trapmgr {

  Snmpagent agent;
  Snmptrapmgr trapmgr;

  public trapmgr() {

    try {
      agent = new Snmpagent();
      agent.setLocalEngineId("MyEngine");

      System.out.println("Starting agent...");
      agent.setAcceptData(true);

      trapmgr = new Snmptrapmgr();
      trapmgr.addSnmptrapmgrEventListener(new TrapMgrEvents(this));

      System.out.println("Starting trap manager...");
      trapmgr.setActive(true);

      //Secure traps are available in version 3. You may also choose V1 or V2c, but
      //this demo uses V3 to demonstrate secure traps
      System.out.println("\r\nSending a trap...");
      agent.setSNMPVersion(agent.snmpverV2c);
      agent.sendTrap("255.255.255.255", "coldStart");

      agent.sendSecureTrap("255.255.255.255", "coldStart", "desuser", 1, "des_password", 1, "des_password"); //md5 and des hardcoded here

    } catch (IPWorksSNMPException ex) {
      System.out.println("IPWorks SNMP Exception: " + ex.getMessage());
    } catch (Exception ex) {
      System.out.println("Exception: " + ex.getMessage());
    }
  }
  public static void main(String[] args) {
    new trapmgr();

  }

  public void onTrap (SnmptrapmgrTrapEvent arg0) {
    System.out.println("\r\n---- Trap received!");
    System.out.println("    Source address: " + arg0.sourceAddress);
    System.out.println("    Trap OID: " + arg0.trapOID);
    System.out.println("    Time stamp: " + String.valueOf(arg0.timeStamp));
  }

  public void onGetUserPassword (SnmptrapmgrGetUserPasswordEvent arg0) {

    if (arg0.passwordType == 1) {
      System.out.println("\r\nVerifying authentication password for incoming secure trap.");
      System.out.println("    User: " + arg0.user);
      //now, the trap manager checks to see what password is defined for this
      //user and engineid, and sets the password parameter to the event
      //If the password matches what the agent sent, the trap manager
      //continues to process the request, moving on to encryption password,
      //if sent. Otherwise, the message is rejected.
      if (arg0.user.equals("desuser")) {
        arg0.password = "des_password";
      }
    } else //encryption password
    {
      System.out.println("\r\nVerifying encryption password for incoming secure trap.");
      System.out.println("    User: " + arg0.user);
      //the trap maanger attempts to decrypt the trap with the password
      //it has defined for that user. If the trap manager doesn't have the
      //right password define, it can't read the trap.
      //While the authentication password is used to make sure that the
      //right agent has sent the trap, the encryption password makes certain
      //that no unauthorized manager can read the data
      if (arg0.user.equals("desuser")) {
        arg0.password = "des_password";
      }
    }

  }

}

class TrapMgrEvents implements SnmptrapmgrEventListener{
  trapmgr instance;

  public TrapMgrEvents(trapmgr instance) {
    this.instance = instance;
  }

  public void badPacket(SnmptrapmgrBadPacketEvent arg0) {}
  public void cacheEntry(SnmptrapmgrCacheEntryEvent arg0) {}
  public void checkEngine(SnmptrapmgrCheckEngineEvent arg0) {}
  public void discoveryRequest(SnmptrapmgrDiscoveryRequestEvent arg0) {}
  public void error(SnmptrapmgrErrorEvent arg0) {}
  public void getUserPassword(SnmptrapmgrGetUserPasswordEvent arg0) {
    instance.onGetUserPassword(arg0);
  }
  public void getUserSecurityLevel(SnmptrapmgrGetUserSecurityLevelEvent arg0) {}
  public void hashPassword(SnmptrapmgrHashPasswordEvent arg0) {}
  public void informRequest(SnmptrapmgrInformRequestEvent arg0) {}
  public void packetTrace(SnmptrapmgrPacketTraceEvent arg0) {}
  public void trap(SnmptrapmgrTrapEvent arg0) {
    instance.onTrap(arg0);
  }
}
class ConsoleDemo {
  private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

  static String input() {
    try {
      return bf.readLine();
    } catch (IOException ioe) {
      return "";
    }
  }
  static char read() {
    return input().charAt(0);
  }

  static String prompt(String label) {
    return prompt(label, ":");
  }
  static String prompt(String label, String punctuation) {
    System.out.print(label + punctuation + " ");
    return input();
  }

  static String prompt(String label, String punctuation, String defaultVal)
  {
	System.out.print(label + " [" + defaultVal + "] " + punctuation + " ");
	String response = input();
	if(response.equals(""))
		return defaultVal;
	else
		return response;
  }

  static char ask(String label) {
    return ask(label, "?");
  }
  static char ask(String label, String punctuation) {
    return ask(label, punctuation, "(y/n)");
  }
  static char ask(String label, String punctuation, String answers) {
    System.out.print(label + punctuation + " " + answers + " ");
    return Character.toLowerCase(read());
  }

  static void displayError(Exception e) {
    System.out.print("Error");
    if (e instanceof IPWorksSNMPException) {
      System.out.print(" (" + ((IPWorksSNMPException) e).getCode() + ")");
    }
    System.out.println(": " + e.getMessage());
    e.printStackTrace();
  }
}




