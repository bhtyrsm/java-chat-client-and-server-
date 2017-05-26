package chatroom;

import java.io.*;
import java.net.*;
import java.util.*;

public class chatServer {
	static Vector ClientSockets;
	static Vector LoginNames;
	
	
	chatServer()throws IOException{
		ServerSocket server=new ServerSocket(5217);
		ClientSockets=new Vector();
		LoginNames = new Vector();
		
		while(true){
			Socket client=server.accept();
			AcceptClient acceptClient=new AcceptClient(client);
		}
	}
	
	
	//test purpose
	public static void main(String[] args) throws IOException{
		
		chatServer client=new chatServer();
	}
	
	
	class AcceptClient extends Thread{
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;
		
		AcceptClient(Socket client)throws IOException{
			ClientSocket=client;
			din=new DataInputStream(ClientSocket.getInputStream());
			dout=new DataOutputStream(ClientSocket.getOutputStream());
			
			String LoginName=din.readUTF();
			LoginNames.add(LoginName);
			ClientSockets.add(ClientSocket);
			
			start();
			
		}
		
		public void run(){
			while (true){
				try {
					String msgFromClient=din.readUTF();
					StringTokenizer st=new StringTokenizer(msgFromClient);
					String LoginName=st.nextToken();
					String MsgType=st.nextToken();
					int lo=-1;
					String msg="";
					while(st.hasMoreTokens()){
						msg=msg+" "+st.nextToken();
					}
					
					if(MsgType.equals("LOGIN")){
						for(int i=0; i<LoginNames.size();i++){
							Socket pSocket=(Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut=new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(LoginName+" has  logged in.");
						}
						
					}
					else if(MsgType.equals("LOGOUT")){
						for(int i=0; i<LoginNames.size();i++){
							if(LoginName==LoginNames.elementAt(i))
								lo=i;
							Socket pSocket=(Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut=new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(LoginName+" has  logged out.");
						}
						if(lo>=0){
							LoginNames.removeElementAt(lo);
							ClientSockets.removeElementAt(lo);
						}
						
					}
					else{
						for(int i=0; i<LoginNames.size();i++){
							Socket pSocket=(Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut=new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(LoginName+": "+msg);
						}
					}
	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		
	}
}
