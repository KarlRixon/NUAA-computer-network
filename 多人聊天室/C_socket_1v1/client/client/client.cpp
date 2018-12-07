#include <WinSock2.h>
#include <stdio.h>
#include <stdlib.h>
#pragma comment(lib,"ws2_32.lib")

int main()
{
	WSADATA wsaData = { 0 };//存放套接字信息
	SOCKET ClientSocket = INVALID_SOCKET;//客户端套接字
	SOCKADDR_IN ServerAddr = { 0 };//服务端地址
	USHORT uPort = 18000;//服务端端口

						 //初始化套接字
	if (WSAStartup(MAKEWORD(2, 2), &wsaData))
	{
		printf("WSAStartup failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//判断套接字版本
	if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 2)
	{
		printf("wVersion was not 2.2\n");
		return -1;
	}
	//创建套接字
	ClientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (ClientSocket == INVALID_SOCKET)
	{
		printf("socket failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//输入服务器IP
	printf("Please input server IP:");
	char IP[32] = { 0 };
	gets_s(IP);
	//输入聊天的用户名
	printf("Please input your name:");
	char name[32] = { 0 };
	gets_s(name);
	//设置服务器地址
	ServerAddr.sin_family = AF_INET;
	ServerAddr.sin_port = htons(uPort);//服务器端口
	ServerAddr.sin_addr.S_un.S_addr = inet_addr(IP);//服务器地址

	printf("connecting......\n");
	//连接服务器
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

	//发送用户名给对方
	iSnedLen = send(ClientSocket, name, strlen(name), 0);
	if (SOCKET_ERROR == iSnedLen)
	{
		printf("send failed with error code: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	//接收对方的用户名
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

	//发送并接收消息
	while (1)
	{
		memset(buffer, 0, sizeof(buffer));
		//发送消息
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
		//printf("send %d bytes to %s\n", iSnedLen, nameOther);//为了美观最好不要打印这个了

		memset(buffer, 0, sizeof(buffer));
		//接收消息
		iRecvLen = recv(ClientSocket, buffer, sizeof(buffer), 0);
		if (SOCKET_ERROR == iRecvLen)
		{
			printf("send failed with error code: %d\n", WSAGetLastError());
			closesocket(ClientSocket);
			WSACleanup();
			return -1;
		}
		//printf("recv %d bytes from %s: ", iRecvLen, nameOther);//为了美观最好不要打印这个了
		strcat(buffer, "\0");
		printf("%s: %s\n", nameOther, buffer);

	}

	closesocket(ClientSocket);
	WSACleanup();
	system("pause");
	return 0;
}