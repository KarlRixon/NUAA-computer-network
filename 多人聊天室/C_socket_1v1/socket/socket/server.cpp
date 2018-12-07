#include <WinSock2.h>
#include <stdio.h>
#include <stdlib.h>
#pragma comment(lib,"ws2_32.lib")

int main()
{
	//����׽�����Ϣ�Ľṹ
	WSADATA wsaData = { 0 };
	SOCKET ServerSocket = INVALID_SOCKET;//������׽���
	SOCKET ClientSocket = INVALID_SOCKET;//�ͻ����׽���
	SOCKADDR_IN ServerAddr = { 0 };//����˵�ַ
	SOCKADDR_IN ClientAddr = { 0 };//�ͻ��˵�ַ
	int iClientAddrLen = sizeof(ClientAddr);
	USHORT uPort = 18000;//�����������˿�

						 //��ʼ���׽���
	if (WSAStartup(MAKEWORD(2, 2), &wsaData))
	{
		printf("WSAStartup failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//�жϰ汾
	if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 2)
	{
		printf("wVersion was not 2.2\n");
		return -1;
	}
	//�����׽���
	ServerSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (ServerSocket == INVALID_SOCKET)
	{
		printf("socket failed with error code: %d\n", WSAGetLastError());
		return -1;
	}

	//���÷�������ַ
	ServerAddr.sin_family = AF_INET;//���ӷ�ʽ
	ServerAddr.sin_port = htons(uPort);//�����������˿�
	ServerAddr.sin_addr.S_un.S_addr = htonl(INADDR_ANY);//�κοͻ��˶����������������

														//�󶨷�����
	if (SOCKET_ERROR == bind(ServerSocket, (SOCKADDR*)&ServerAddr, sizeof(ServerAddr)))
	{
		printf("bind failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		return -1;
	}
	//�������޿ͻ�������
	if (SOCKET_ERROR == listen(ServerSocket, 1))
	{
		printf("listen failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		WSACleanup();
		return -1;
	}
	//�����û���
	printf("Please input your name:");
	char name[32] = { 0 };
	gets_s(name);

	printf("waiting connect.....\n");
	//����пͻ����������Ӿͽ�������
	ClientSocket = accept(ServerSocket, (SOCKADDR*)&ClientAddr, &iClientAddrLen);
	if (ClientSocket == INVALID_SOCKET)
	{
		printf("accept failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		WSACleanup();
		return -1;
	}
	printf("Successfuuly got a connection from IP:%s Port:%d\n\n\n\n",
		inet_ntoa(ClientAddr.sin_addr), htons(ClientAddr.sin_port));

	char buffer[4096] = { 0 };
	int iRecvLen = 0;
	int iSendLen = 0;

	//�����û������Է�
	iSendLen = send(ClientSocket, name, strlen(name), 0);
	if (SOCKET_ERROR == iSendLen)
	{
		printf("send failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	//���նԷ����û���
	char nameOther[32] = { 0 };
	iRecvLen = recv(ClientSocket, nameOther, sizeof(nameOther), 0);
	if (SOCKET_ERROR == iRecvLen)
	{
		printf("recv failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	strcat(nameOther, "\0");
	//һֱ���տͻ��˷�������Ϣ ���Ұ���Ϣ���ͻ�ȥ
	while (1)
	{

		memset(buffer, 0, sizeof(buffer));
		//���տͻ�����Ϣ
		iRecvLen = recv(ClientSocket, buffer, sizeof(buffer), 0);
		if (SOCKET_ERROR == iRecvLen)
		{
			printf("recv failed with error code: %d\n", WSAGetLastError());
			closesocket(ServerSocket);
			closesocket(ClientSocket);
			WSACleanup();
			return -1;
		}
		//printf("recv %d bytes from %s: ", iRecvLen, nameOther);
		strcat(buffer, "\0"); //��һ����ֹ��,��Ϊ���յ�������û�����
		printf("%s: %s\n", nameOther, buffer);

		//������Ϣ���ͻ���
		memset(buffer, 0, sizeof(buffer));
		printf("%s: ", name);
		gets_s(buffer);
		if (strcmp(buffer, "bye") == 0) break;
		iSendLen = send(ClientSocket, buffer, strlen(buffer), 0);
		if (SOCKET_ERROR == iSendLen)
		{
			printf("send failed with error code: %d\n", WSAGetLastError());
			closesocket(ServerSocket);
			closesocket(ClientSocket);
			WSACleanup();
			return -1;
		}
		//printf("send %d bytes to %s\n", iSendLen, nameOther);//Ϊ��������ò�Ҫ��ӡ�����

	}

	closesocket(ClientSocket);
	closesocket(ServerSocket);
	WSACleanup();
	system("pause");
	return 0;
}
