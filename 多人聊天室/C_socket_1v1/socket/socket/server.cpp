#include <WinSock2.h>
#include <stdio.h>
#include <stdlib.h>
#pragma comment(lib,"ws2_32.lib")

int main()
{
	//存放套接字信息的结构
	WSADATA wsaData = { 0 };
	SOCKET ServerSocket = INVALID_SOCKET;//服务端套接字
	SOCKET ClientSocket = INVALID_SOCKET;//客户端套接字
	SOCKADDR_IN ServerAddr = { 0 };//服务端地址
	SOCKADDR_IN ClientAddr = { 0 };//客户端地址
	int iClientAddrLen = sizeof(ClientAddr);
	USHORT uPort = 18000;//服务器监听端口

						 //初始化套接字
	if (WSAStartup(MAKEWORD(2, 2), &wsaData))
	{
		printf("WSAStartup failed with error code: %d\n", WSAGetLastError());
		return -1;
	}
	//判断版本
	if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 2)
	{
		printf("wVersion was not 2.2\n");
		return -1;
	}
	//创建套接字
	ServerSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (ServerSocket == INVALID_SOCKET)
	{
		printf("socket failed with error code: %d\n", WSAGetLastError());
		return -1;
	}

	//设置服务器地址
	ServerAddr.sin_family = AF_INET;//连接方式
	ServerAddr.sin_port = htons(uPort);//服务器监听端口
	ServerAddr.sin_addr.S_un.S_addr = htonl(INADDR_ANY);//任何客户端都能连接这个服务器

														//绑定服务器
	if (SOCKET_ERROR == bind(ServerSocket, (SOCKADDR*)&ServerAddr, sizeof(ServerAddr)))
	{
		printf("bind failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		return -1;
	}
	//监听有无客户端连接
	if (SOCKET_ERROR == listen(ServerSocket, 1))
	{
		printf("listen failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		WSACleanup();
		return -1;
	}
	//输入用户名
	printf("Please input your name:");
	char name[32] = { 0 };
	gets_s(name);

	printf("waiting connect.....\n");
	//如果有客户端申请连接就接受连接
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

	//发送用户名给对方
	iSendLen = send(ClientSocket, name, strlen(name), 0);
	if (SOCKET_ERROR == iSendLen)
	{
		printf("send failed with error code: %d\n", WSAGetLastError());
		closesocket(ServerSocket);
		closesocket(ClientSocket);
		WSACleanup();
		return -1;
	}
	//接收对方的用户名
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
	//一直接收客户端发来的消息 并且把消息发送回去
	while (1)
	{

		memset(buffer, 0, sizeof(buffer));
		//接收客户端消息
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
		strcat(buffer, "\0"); //加一个截止符,因为接收到的数据没有这个
		printf("%s: %s\n", nameOther, buffer);

		//发送消息给客户端
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
		//printf("send %d bytes to %s\n", iSendLen, nameOther);//为了美观最好不要打印这个了

	}

	closesocket(ClientSocket);
	closesocket(ServerSocket);
	WSACleanup();
	system("pause");
	return 0;
}
