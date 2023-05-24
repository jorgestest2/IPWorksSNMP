/*
 * IPWorks SNMP 2022 C++ Edition - Demo Application
 *
 * Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
 *
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "../../include/ipworkssnmp.h"
#define LINE_LEN 100


class MySNMP: public SNMPTrapMgr
{
public:

	virtual int FireError(SNMPTrapMgrErrorEventParams *e)
	{
		printf("Error %i: %s\n", e->ErrorCode, e->Description);
		return 0 ;
	}

	virtual int FireTrap(SNMPTrapMgrTrapEventParams *e)
	{
		printf("Received Trap: %s\n\t", e->TrapOID);
		printf("Source: %s\n\n", e->SourceAddress);
		return 0;
	}

	virtual int FireCheckEngine(SNMPTrapMgrCheckEngineEventParams *e)
	{
		e->Accept = true;
		return 0;
	}

	virtual int FireGetUserPassword(SNMPTrapMgrGetUserPasswordEventParams *e)
	{
		if (strcmp(e->User, "myuser") == 0)
		{
			if (e->PasswordType == 1) e->Password = (char*)"my_password";
		}
		else if (strcmp(e->User, "desuser") == 0)
		{
			if (e->PasswordType == 1) e->Password = (char*)"des_password";
			if (e->PasswordType == 2) e->Password = (char*)"des_password";
		}

		//Note that if the encryption used for a user is AES or 3DES the EncryptionAlgorithm parameter must also be set.
		//For AES: e->EncryptionAlgorithm = 2. For 3DES: e->EncryptionAlgorithm = 3.
		//The default value of e->EncryptionAlgorithm is 1 which is applicable to DES encryption.
		return 0;
	}

	virtual int FireGetUserSecurityLevel(SNMPTrapMgrGetUserSecurityLevelEventParams *e)
	{
		//Here you can control what passwords (none, auth, enc) are needed by the GetUserPassword event,
		//where the user authentication gets performed.
		return 0;
	}
};


int main()
{
	MySNMP snmp;

	snmp.SetActive(true);

	printf("Listening for traps.  Ctrl-C to quit\n\n");
	while (true)
	{
		snmp.DoEvents();
	}

	printf("\nPress enter to continue...");
	getchar();
	exit(1);
	return 0;
}

