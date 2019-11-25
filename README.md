# MultiThreaded-Dictionary-Server

The aim of this project is to use a Client - Server Architecture to design and implement a Multi-Threaded Server that allows concurrent Clients to Search the meaning(s) of a word, Add a new word along with its meaning(s) and Delete an existing word from a dictionary along with its associated meaning(s). The lowest level of abstraction used for concurrency is Threads and for network communication is Sockets and all components are implemented using Java.


## Running

A sample command to start the server is:

	java –jar DictionaryServer.jar <port> <dictionary-file>

Where \<port> is the port number where the server will listen for incoming client connections and \<dictionary-file> is the path to the file containing the initial dictionary data.

When the client is launched, it creates a TCP socket bound to the server address and port number. This socket remains open for the duration of the client-server interaction. All messages are sent/received through this socket.


A sample command to start the client is:

	java –jar DictionaryClient.jar <server-address> <server-port>

The jar files are located in /out/artifacts folder.

## Server GUI

A GUI developed using the Java Swing library that displays information about Server being ready to accept connections and messages whenever a new Client connects, messages received from Clients or a Client connection terminates. 

## Client GUI

A GUI developed using the Java Swing library that has a Text component to enter text for the corresponding search/add/delete operations along with their respective operation buttons and a message instructing the client on how to use the application. A Text area is present below that is used to display the responses received from the Server including success and failure messages.

## Functional Implementation

Clients can Query/Search for a particular word’s meaning from the dictionary, Add a new word and its associated meaning(s) and Delete an existing words along with all its meaning(s) from the dictionary. The server will handle all the messages sent from the client, catching any exceptions and send back appropriate responses/results to the client. 

## Multi-Threading

This project has been implemented using the Thread per Connection Architecture as it is very convenient and straightforward for the server to manage threads this way and also it guarantees that the main thread in Server only blocks on the accept() call and wont be blocked by other situations. Using Thread per request can suffocate performance with every client having large number of requests and worker pool architecture only has a fixed number of threads we can use from the pool. Hence considering all the above factors, Thread per Connection architecture has been implemented.

Disadvantages of this architecture include that if there are multiple threads accessing the same resource (Dictionary) then it could lead to performance issues. Also, if hundreds of clients try to connect to the server at the same time, then only the first few will be connected and the rest will have to wait for a connection until the previous clients have been disconnected.

Since many Client Threads are to access the same resource, to prevent inconsistency with the Dictionary data, synchronized block is used on the Dictionary object when the Server accesses the dictionary to perform necessary operations. This will give exclusive access to a thread on the Dictionary object for the span of the performed operation to prevent inconsistency in the Dictionary data.
