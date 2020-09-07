# WordSearchEngine-Concurrent-Distributed-Programing
A Word Search Engine-Concurrent-Distributed-Programming University Project using a Concurrent &amp; Distribution Programing aproach.

Project Score: A (out of A,B,C,D). 
Discipline Score: 17 (0-20). 

Here's what this project does: 

The user must run a client that allows him to enter the words and start the demand. The client must send this word to the server that will create tasks that are performed by the workers. Each task consists of searching for an expression or phrase in a news story. THE worker must return to the server a list containing all indexes of occurrences of the word in the news text. After all tasks have been performed, the server groups the results and sends to the customer. The information that the server must send to the client consists of a list with the titles the news in which the word occurs as well as a list of the occurrence indexes for each of that news. After receiving the headlines of the news where the word appears, the client shows the these results. When the user selects one of the news from the list, the customer must send a message to the server asking for the news text. When the server starts up it should read all the news that are part of the corpus. These news are in a set of files in a folder that is passed to the server. The server on receiving a new search request will create a set of tasks, one for each news item, consisting of to look for the expression in one of the news.
