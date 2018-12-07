#include <WinSock2.h>
#include <stdio.h>
#include <stdlib.h>
#pragma comment(lib,"ws2_32.lib")

int main()
{
	WSADATA wsaData = { 0 };//����׽�����Ϣ
	SOCKET ClientSocket = INVALID_SOCKET;//�ͻ����׽���
	SOCKADDR_IN ServerAddr = { 0 };//����˵�ַ
	USHORT uPort = 18000;//����˶˿�

						 //��ʼ���׽���
	if (WSAStartup(MAKEWORD(2, 2), &wsaData))
	{
		printf("WSAStartup failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//�ж��׽��ְ汾
	if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 2)
	{
		printf("wVersion was not 2.2\n");
		return -1;
	}
	//�����׽���
	ClientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (ClientSocket == INVALID_SOCKET)
	{
		printf("socket failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//���������IP
	printf("Please input server IP:");
	char IP[32] = { 0 };
	gets_s(IP);
	//����������û���
	printf("Please input your name:");
	char name[32] = { 0 };
	gets_s(name);
	//���÷�������ַ
	ServerAddr.sin_family = AF_INET;
	ServerAddr.sin_port = htons(uPort);//�������˿�
	ServerAddr.sin_addr.S_un.S_addr = inet_addr(IP);//��������ַ

	printf("connecting......\n");
	//���ӷ�����
	if (SOCKET_ERROR == connect(ClientSocket, (SOCKADDR*)&ServerAddr, sizeof(ServerAddr)))
	{
		printf("connect failed with error code: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	printf("connecting server successfully IP:%s Port:%d\n\n\n\n",
		inet_ntoa(ServerAddr.sin_addr), htons(ServerAddr.sin_port));

	char buffer[4096] = { 0 };
	int iRecvLen = 0;
	int iSnedLen = 0;

	//�����û������Է�
	iSnedLen = send(ClientSocket, name, strlen(name), 0);
	if (SOCKET_ERROR == iSnedLen)
	{
		printf("send failed with error code: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	//���նԷ����û���
	char nameOther[32] = { 0 };
	iRecvLen = recv(ClientSocket, nameOther, sizeof(nameOther), 0);
	if (SOCKET_ERROR == iRecvLen)
	{
		printf("send failed with error code: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	strcat(nameOther, "\0");

	//���Ͳ�������Ϣ
	while (1)
	{
		memset(buffer, 0, sizeof(buffer));
		//������Ϣ
		printf("%s: ", name);
		gets_s(buffer);
		if (strcmp(buffer, "bye") == 0) break;
		iSnedLen = send(ClientSocket, buffer, strlen(buffer), 0);
		if (SOCKET_ERROR == iSnedLen)
		{
			printf("send failed with error code: %d\n", WSAGetLastError());
			closesocket(ClientSocket);
			WSACleanup();
			return -1;
		}
		//printf("send %d bytes to %s\n", iSnedLen, nameOther);//Ϊ��������ò�Ҫ��ӡ�����

		memset(buffer, 0, sizeof(buffer));
		//������Ϣ
		iRecvLen = recv(ClientSocket, buffer, sizeof(buffer), 0);
		if (SOCKET_ERROR == iRecvLen)
		{
			printf("send failed with error code: %d\n", WSAGetLastError());
			closesocket(ClientSocket);
			WSACleanup();
			return -1;
		}
		//printf("recv %d bytes from %s: ", iRecvLen, nameOther);//Ϊ��������ò�Ҫ��ӡ�����
		strcat(buffer, "\0");
		printf("%s: %s\n", nameOther, buffer);

	}

	closesocket(ClientSocket);
	WSACleanup();
	system("pause");
	return 0;
}